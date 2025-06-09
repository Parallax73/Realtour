import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Client } from '../../utils/models/client';
import { Realtor } from '../../utils/models/realtor';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor() { }

  private http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/v1/users';
  private token: string = '';

  createClient(client: Client): Observable<string> {
    console.log('Creating client with data:', { ...client, password: '[REDACTED]' });
    
    return this.http.post('${this.apiUrl}/register-client', client, { 
      responseType: 'text',  // <-- This is the key change
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
          // If we somehow get here with a 201 status, it's still a success
          return "Registration successful";
        }
        // For actual errors
        const errorMessage = error.error || error.message || 'An unexpected error occurred';
        return throwError(() => errorMessage);
      })
    );
  }

  createRealtor(realtor: Realtor): Observable<string> {
    return this.http.post('${this.apiUrl}/register-realtor', realtor, { 
      responseType: 'text',  // <-- Same change here
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


  loginClient(email: string, password: string) {
  const bodyData = {
    email: email,
    password: password
  };

  this.http.post(`${this.apiUrl}/login-client`, bodyData, {
    responseType: 'text'
  }).subscribe((result) => {
    const jwt = result.substring(16).replaceAll('"', "").replaceAll("}", "");
    this.jwt
  });
}

}