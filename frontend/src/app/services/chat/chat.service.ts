import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private apiUrl = 'http://localhost:8080/api/v1/chats';

  constructor(private http: HttpClient) { }


  getChats(): Observable<any> {
    const token = this.getToken();
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.get<any>(
      `${this.apiUrl}/get-chats`,
      {
        headers,
        withCredentials: true
      }
    );
  }

  getOrCreateChat(unitId: string, realtorUsername: string): Observable<any> {
    const token = this.getToken();
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.post<any>(
        this.apiUrl + '/create',
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
      .find(row => row.startsWith('SecureJWT='))
      ?.split('=')[1] || '';
  }
}
