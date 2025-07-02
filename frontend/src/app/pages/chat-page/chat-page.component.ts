import { Component, OnDestroy, OnInit, ViewChild, ElementRef } from '@angular/core';
import { WebsocketService } from 'src/app/services/websocket/websocket.service';
import { AuthService } from 'src/app/services/auth/auth.service';
import { Subscription } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ChatService } from '@app/services/chat/chat.service';
import { MenuNavbarComponent } from "../../components/menu-navbar/menu-navbar.component";
import { ButtonModule } from 'primeng/button';
import { FileUploadModule } from 'primeng/fileupload';
import { UnitService } from '@app/services/unit/unit.service';
import { RouterService } from '@app/services/router/router.service';
import { Unit } from '@app/models/unit';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-chat-page',
  templateUrl: './chat-page.component.html',
  styleUrls: ['./chat-page.component.scss'],
  standalone: true,
  imports: [
    FormsModule,
    CommonModule,
    ButtonModule,
    FileUploadModule,
    MenuNavbarComponent
  ],
  providers: [DatePipe]
})
export class ChatPageComponent implements OnInit, OnDestroy {
  @ViewChild('messageContainer') private messageContainer!: ElementRef;
  
  public chats: any[] = [];
  public selectedChat: any = null;
  public selectedUnit: Unit | null = null;
  public messageText: string = '';
  public messages: any[] = [];
  private messageSub!: Subscription;
  public currentUser: any;
  chatIsSelected = false;

  constructor(
    public websocketService: WebsocketService,
    public authService: AuthService,
    private chatService: ChatService,
    private unitService: UnitService,
    public routerService: RouterService,
    private datePipe: DatePipe
  ) {}

  ngOnInit(): void {
    const token = this.authService.getToken();
    if (!token) return;
    
    this.chatService.getChatList().subscribe({
      next: (chats: any) => {
        this.chats = chats;
        if (chats && chats.length > 0) {
          const firstChat = chats[0];
          if (firstChat.clientEmail === this.authService.getUserEmail()) {
            this.currentUser = {
              email: firstChat.clientEmail,
              name: firstChat.clientName
            };
          } else {
            this.currentUser = {
              email: firstChat.realtorEmail,
              name: firstChat.realtorName
            };
          }
        }
      },
      error: (error) => {
        console.error('Error fetching chats:', error);
      }
    });
  }

  scrollToBottom(): void {
    try {
      setTimeout(() => {
        const element = this.messageContainer.nativeElement;
        element.scrollTop = element.scrollHeight;
      }, 100);
    } catch (err) {}
  }

  handleChatSelection(chat: any): void {
  if (!chat || !chat.id) return;

  this.chatIsSelected = true;
  
  if (this.selectedChat && this.selectedChat.id !== chat.id) {
    this.websocketService.disconnect();
  }
  
  this.unitService.getUnitById(chat.unitId).subscribe({
    next: (unit) => {
      this.selectedChat = chat;
      this.selectedUnit = {
        ...unit,
        address: unit.address || chat.unitAddress, 
        number: unit.number || chat.unitNumber 
      };
      this.messages = chat.messages || [];
      
      if (!this.messageSub || this.selectedChat.id !== chat.id) {
        this.connectWebSocket(chat.id);
      }
    },
    error: (error) => {
      this.chatIsSelected = false;
    }
  });
}

  private connectWebSocket(chatId: string) {
    if (this.messageSub) {
      this.messageSub.unsubscribe();
    }
    
    this.websocketService.connect(chatId);
    this.messageSub = this.websocketService.chatMessages$.subscribe({
      next: (updatedChat) => {
        if (updatedChat && updatedChat.messages) {
          this.messages = updatedChat.messages;
          this.scrollToBottom();
        }
      },
      error: (error) => {}
    });
  }

  isCurrentUserMessage(msg: any): boolean {
    return msg.sender === this.currentUser.email;
  }

  getMessageSenderName(msg: any): string {
    if (this.isCurrentUserMessage(msg)) {
      return 'You';
    }
    return msg.sender === this.selectedChat.clientEmail ? 
      this.selectedChat.clientName : 
      this.selectedChat.realtorName;
  }

  getLastMessage(chat: any): string {
    if (chat.messages && chat.messages.length > 0) {
      const lastMessage = chat.messages[chat.messages.length - 1];
      const sender = lastMessage.sender === this.currentUser?.email ? 'You' : 
        (lastMessage.sender === chat.clientEmail ? chat.clientName : chat.realtorName);
      return `${sender}: ${lastMessage.content}`;
    }
    return 'No messages yet';
  }

  navigateToRealtorProfile(): void {
    const realtorId = this.selectedUnit?.realtor?.id;
    if (realtorId) {
      this.routerService.navigateToRealtor(realtorId);
    }
  }

  navigateToUnitPage(): void {
    const unitId = this.selectedUnit?.id;
    if (unitId) {
      this.routerService.navigateToUnit(unitId);
    }
  }

  sendMessage(): void {
    if (this.messageText.trim() && this.selectedChat) {
      this.websocketService.sendMessage(this.messageText.trim());
      this.messageText = '';
      this.scrollToBottom();
    }
  }

  ngOnDestroy(): void {
    if (this.messageSub) {
      this.messageSub.unsubscribe();
    }
    if (this.selectedChat) {
      this.websocketService.disconnect();
    }
  }
  
  choose(event: any, callback: () => void) {
    callback();
  }
}