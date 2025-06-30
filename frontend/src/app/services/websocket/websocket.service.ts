import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { AuthService } from 'src/app/services/auth/auth.service';

export interface Chat {
  id: string;
  clientEmail: string;
  clientName: string;
  realtorEmail: string;
  realtorName: string;
  unitId: string;
  unitAddress: string | null;
  unitNumber: string;
  messages: Message[];
}

export interface Message {
  sender: string;
  content: string;
  timestamp: string;
}

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {
  private socket: WebSocket | null = null;
  private readonly socketBaseUrl = 'ws://localhost:8080/ws/chat';
  public chatMessages$ = new BehaviorSubject<Chat | null>(null);
  private reconnectAttempts = 0;
  private maxReconnectAttempts = 5;
  private reconnectDelay = 3000;

  constructor(private authService: AuthService) {}

  connect(chatId: string): void {
    const token = this.authService.getToken();
    if (!token) return;

    const url = `${this.socketBaseUrl}?chatId=${chatId}&token=${token}`;
    this.disconnect();
    
    this.socket = new WebSocket(url);

    this.socket.onopen = () => {
      this.reconnectAttempts = 0;
    };

    this.socket.onmessage = (event: MessageEvent) => {
      try {
        const parsedData = JSON.parse(event.data);
        this.chatMessages$.next(parsedData);
      } catch (error) {
        console.error('Error parsing WebSocket message:', error);
      }
    };

    this.socket.onerror = (error) => {
      console.error('WebSocket error:', error);
    };

    this.socket.onclose = () => {
      if (this.reconnectAttempts < this.maxReconnectAttempts) {
        this.reconnectAttempts++;
        setTimeout(() => this.connect(chatId), this.reconnectDelay);
      }
    };
  }

  sendMessage(content: string): void {
    if (!this.socket || this.socket.readyState !== WebSocket.OPEN) return;
    this.socket.send(JSON.stringify({ content }));
  }

  disconnect(): void {
    if (this.socket) {
      this.socket.close();
      this.socket = null;
    }
  }
}