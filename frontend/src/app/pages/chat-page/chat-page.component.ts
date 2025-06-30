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
    
]
})
export class ChatPageComponent implements OnInit, OnDestroy {
  @ViewChild('messageContainer') private messageContainer!: ElementRef;
  
  public chats: any[] = [];
  public selectedChat: any = null;
  public messageText: string = '';
  public messages: any[] = [];
  private messageSub!: Subscription;
  public currentUser: any;
  chatIsSelected = false;

  constructor(
    public websocketService: WebsocketService,
    public authService: AuthService,
    private chatService: ChatService
  ) {}

  ngOnInit(): void {
    const token = this.authService.getToken();
    if (!token) return;
    
    this.chatService.getChats().subscribe({
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
          this.selectChat(chats[0]);
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

  selectChat(chat: any): void {
    this.selectedChat = chat;
    this.messages = chat.messages || [];
    this.websocketService.connect(chat.id);
    
    if (this.messageSub) {
      this.messageSub.unsubscribe();
    }
    
    this.messageSub = this.websocketService.chatMessages$.subscribe({
      next: (updatedChat) => {
        if (updatedChat && updatedChat.messages) {
          this.messages = updatedChat.messages;
          this.scrollToBottom();
        }
      },
      error: (error) => {
        console.error('Error in chat messages subscription:', error);
      }
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

  getChatPartnerName(chat: any): string {
    return chat.clientEmail === this.currentUser.email ? 
      chat.realtorName : 
      chat.clientName;
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
    this.websocketService.disconnect();
  }
  
  choose(event: any, callback: () => void) {
        callback();
    }
}