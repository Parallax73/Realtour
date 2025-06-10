import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Client } from '../../utils/models/client';
import { Realtor } from '../../utils/models/realtor';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { AuthService } from '../auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  token!: string;
  constructor() { }

  private http = inject(HttpClient);
  authService = inject(AuthService);
  private readonly apiUrl = 'http://localhost:8080/api/v1/users';
 

  createClient(client: Client): Observable<string> {
    console.log('Creating client with data:', { ...client, password: '[REDACTED]' });
    
    return this.http.post(`${this.apiUrl}/register-client`, client, { 
      responseType: 'text', 
      observe: 'response',
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    }).pipe(
      map(response => {
        if (response.status === 201) {
          return response.body || "Registration successful";
        }
        throw new Error('Registration failed');
      }),
      catchError((error: HttpErrorResponse) => {
        if (error.status === 201) {
  
          return "Registration successful";
        }
   
        const errorMessage = error.error || error.message || 'An unexpected error occurred';
        return throwError(() => errorMessage);
      })
    );
  }

  createRealtor(realtor: Realtor): Observable<string> {
    return this.http.post(`${this.apiUrl}/register-realtor`, realtor, { 
      responseType: 'text',  
      observe: 'response',
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    }).pipe(
      map(response => {
        if (response.status === 201) {
          return response.body || "Registration successful";
        }
        throw new Error('Registration failed');
      }),
      catchError((error: HttpErrorResponse) => {
        if (error.status === 201) {
          return "Registration successful";
        }
        const errorMessage = error.error || error.message || 'An unexpected error occurred';
        return throwError(() => errorMessage);
      })
    );
  }


  loginClient(email: string, password: string): Observable<boolean> {
    const bodyData = { email, password };

    return this.http.post(`${this.apiUrl}/login-client`, bodyData, {
      responseType: 'text'
    }).pipe(
      map((token: string) => {
        this.token = token;
        this.authService.setToken(this.token);
        return true; 
      }),
      catchError((error) => {
        console.error("Login failed", error);
        return throwError(() => new Error('Login failed'));
      })
    );
  }

}