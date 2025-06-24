import { Component, OnDestroy, OnInit } from '@angular/core';
import { WebsocketService } from 'src/app/services/websocket/websocket.service';
import { Subscription } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-chat-page',
  templateUrl: './chat-page.component.html',
  styleUrls: ['./chat-page.component.scss'],
  imports: [
    FormsModule,
    CommonModule
  ]
})
export class ChatPageComponent implements OnInit, OnDestroy {
  private chatId = '685ade2361f77d4c9ee0f4de'; 
  private messageSub!: Subscription;
  public messageText: string = '';

  constructor(public websocketService: WebsocketService) {}

  ngOnInit(): void {
    this.websocketService.connect(this.chatId); 

    this.messageSub = this.websocketService.chatMessages$.subscribe(chat => {
      console.log('Updated chat:', chat);
    });
  }

  sendMessage(): void {
    if (this.messageText.trim()) {
      this.websocketService.sendMessage(this.messageText.trim());
      this.messageText = '';
    }
  }

  ngOnDestroy(): void {
    this.messageSub?.unsubscribe();
    this.websocketService.disconnect();
  }
}
