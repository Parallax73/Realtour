import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { AuthService } from '../auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {
  private ws: WebSocket | null = null;
  private reconnectAttempts = 0;
  private maxReconnectAttempts = 5;
  private reconnectDelay = 2000; 
  
  public chatMessages$ = new BehaviorSubject<any>(null);

  constructor(private authService: AuthService) {}

  connect(chatId: string) {
    this.disconnect(); 
    
    const token = this.authService.getToken();
    if (!token) return;

    try {
      const wsUrl = `ws://localhost:8080/ws/chat?chatId=${chatId}&token=${token}`;
      this.ws = new WebSocket(wsUrl);

      this.ws.onopen = () => {
        console.log('WebSocket Connected');
        this.reconnectAttempts = 0; 
      };

      this.ws.onmessage = (event) => {
        const data = JSON.parse(event.data);
        this.chatMessages$.next(data);
      };

      this.ws.onclose = () => {
        console.log('WebSocket Disconnected');
        this.handleReconnect(chatId);
      };

      this.ws.onerror = (error) => {
        console.error('WebSocket error:', error);
      };

    } catch (error) {
      console.error('Error establishing WebSocket connection:', error);
    }
  }

  private handleReconnect(chatId: string) {
    if (this.reconnectAttempts < this.maxReconnectAttempts) {
      this.reconnectAttempts++;
      console.log(`Attempting to reconnect... (${this.reconnectAttempts}/${this.maxReconnectAttempts})`);
      setTimeout(() => this.connect(chatId), this.reconnectDelay);
    }
  }

  disconnect() {
    if (this.ws) {
      this.ws.close();
      this.ws = null;
    }
  }

  sendMessage(message: string) {
    if (this.ws && this.ws.readyState === WebSocket.OPEN) {
      this.ws.send(JSON.stringify({ content: message }));
    } else {
      console.error('WebSocket is not connected');
    }
  }
}