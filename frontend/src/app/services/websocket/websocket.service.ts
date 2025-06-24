import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {
  private socket: WebSocket | null = null;
  private readonly socketBaseUrl = 'ws://localhost:8080/ws/chat';
  private readonly cookieName = 'SecureJWT';
  private chatId: string | null = null;

  public chatMessages$ = new BehaviorSubject<any>(null);

  constructor(private cookieService: CookieService) {}

  connect(chatId: string): void {
    const token = this.cookieService.get(this.cookieName);

    if (!token) {
      console.error('JWT token not found in cookies');
      return;
    }

    this.chatId = chatId;
    const url = `${this.socketBaseUrl}?chatId=${chatId}&token=${token}`;
    this.socket = new WebSocket(url);

    this.socket.onopen = () => {
      console.log('WebSocket connection opened');
    };

    this.socket.onmessage = (event: MessageEvent) => {
      try {
        const data = JSON.parse(event.data);
        this.chatMessages$.next(data);
      } catch (err) {
        console.error('Failed to parse WebSocket message', err);
      }
    };

    this.socket.onerror = (event: Event) => {
      console.error('WebSocket error', event);
    };

    this.socket.onclose = () => {
      console.log('WebSocket connection closed');
      this.socket = null;
    };
  }

  sendMessage(content: string): void {
    if (!this.socket || this.socket.readyState !== WebSocket.OPEN || !this.chatId) {
      console.error('WebSocket not connected or chatId missing');
      return;
    }

    const message = {
      content,
      timestamp: null
    };

    this.socket.send(JSON.stringify(message));
  }

  disconnect(): void {
    if (this.socket) {
      this.socket.close();
      this.socket = null;
    }
    this.chatId = null;
  }
}