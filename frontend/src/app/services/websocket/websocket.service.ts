import { Injectable, OnDestroy } from '@angular/core';
import { Client, StompSubscription } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { BehaviorSubject, Observable, Subscription } from 'rxjs';


export interface ChatMessage {
  content: string;
  senderUsername: string;
  timestamp: string;
}


export interface Chat {
  id: string;
  realtor: any;
  client: any;
  unit: any;
  messages: ChatMessage[];
  timestamp: string;
}

@Injectable({
  providedIn: 'root'
})
export class WebsocketService implements OnDestroy {
  private stompClient: Client;
  private chatSubject = new BehaviorSubject<Chat | null>(null);
   private topicSubscription: StompSubscription | null = null;

  public chat$: Observable<Chat | null> = this.chatSubject.asObservable();

  constructor() {

    this.stompClient = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
      connectHeaders: { Authorization: `Bearer ${this.getToken()}` },
      debug: (str) => { console.log('STOMP:', str); },
      reconnectDelay: 5000,
    });

    this.stompClient.onConnect = () => {
      console.log('STOMP: Connected!');

      if (this.chatSubject.value?.id) {
          this.subscribeToChatTopic(this.chatSubject.value.id);
      }
    };

    this.stompClient.activate();
  }


  public joinChat(chat: Chat): void {

      if (this.chatSubject.value && this.chatSubject.value.id !== chat.id) {
          this.leaveChat();
      }

      this.chatSubject.next(chat);

      if (this.stompClient.connected) {
          this.subscribeToChatTopic(chat.id);
      }
  }

  private subscribeToChatTopic(chatId: string): void {
    const topic = `/topic/chat/${chatId}`;
    console.log(`Subscribing to ${topic}`);

    this.topicSubscription = this.stompClient.subscribe(topic, (message) => {
      const updatedChat = JSON.parse(message.body);
      this.chatSubject.next(updatedChat);
    });
  }


  sendMessage(chatId: string, messageContent: string): void {
    if (this.stompClient.connected) {
      const chatMessage: Partial<ChatMessage> = {
        content: messageContent,
      };

      this.stompClient.publish({
        destination: `/app/chat/${chatId}/sendMessage`,
        body: JSON.stringify(chatMessage)
      });
    } else {
        console.error("Cannot send message, STOMP client is not connected.");
    }
  }


  public leaveChat(): void {
      if (this.topicSubscription) {
          this.topicSubscription.unsubscribe();
          this.topicSubscription = null;
          this.chatSubject.next(null);
          console.log("Left chat and unsubscribed from topic.");
      }
  }

  ngOnDestroy() {
    this.leaveChat();
    this.stompClient.deactivate();
  }

   private getToken(): string {

    return document.cookie

      .split('; ')

      .find(row => row.startsWith('__Secure-JWT='))

      ?.split('=')[1] || '';

  }
}
