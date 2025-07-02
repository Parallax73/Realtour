import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { Chat } from '@app/models/chat';
import { Message } from '@app/models/message';


@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private apiUrl = 'http://localhost:8080/api/v1/chats';

  constructor(private http: HttpClient) { }

  getChatList(): Observable<any> {
    return this.http.get<any>(
      `${this.apiUrl}/get-chats`
    );
  }

  createChat(unitId: string, realtorUsername: string): Observable<any> {
    return this.http.post<any>(
      this.apiUrl + '/create',
      {}
    );
  }


  getChat(id: string): Observable<Chat> {
    return this.http.get<any>(`${this.apiUrl}/get/${id}`).pipe(
        map(data => ({
            id: data._id,
            clientEmail: data.clientEmail,
            clientName: data.clientName,
            realtorEmail: data.realtorEmail,
            realtorName: data.realtorName,
            unitId: data.unitId,
            unitAddress: data.unitAddress || null,
            unitNumber: data.unitNumber,
            messages: data.messages.map((message: Message) => ({
                sender: message.sender,
                content: message.content,
                timestamp: message.timestamp
            }))
        }))
    );
}

  sendMessage(chatId: string, messageText: string): Observable<any> {
    return this.http.post<any>(
      `${this.apiUrl}/${chatId}/messages`,
      { content: messageText }
    );
  }

  
}