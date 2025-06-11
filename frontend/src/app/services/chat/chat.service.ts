import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Chat } from '../websocket/websocket.service'

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private apiUrl = 'http://localhost:8080/api/v1/chats';

  constructor(private http: HttpClient) { }


  getOrCreateChat(unitId: number, realtorUsername: string): Observable<Chat> {
    const token = this.getToken(); //
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });


    return this.http.post<Chat>(
        this.apiUrl,
        {},
        {
            headers,
            params: { unitId, realtorUsername }
        }
    );
  }

  private getToken(): string {
    return document.cookie
      .split('; ')
      .find(row => row.startsWith('__Secure-JWT='))
      ?.split('=')[1] || '';
  }
}
