import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Client } from '../../utils/models/client';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor() { }

  private http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/v1/users';
  private token: string = '';

    createClient(client: Client): Observable<string> {
        return this.http.post<string>(`${this.apiUrl}/register-client`, client)
            .pipe(
                tap(receivedToken => {
                    this.token = receivedToken;
                    console.log(this.token+"SDIOAHOSDHIHAOIDS");
                    
                })
            );
    }

}
