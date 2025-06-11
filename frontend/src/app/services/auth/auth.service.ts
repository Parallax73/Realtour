import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CookieService } from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root'
})
export class AuthService implements HttpInterceptor {
  constructor(private cookieService: CookieService) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const bypassUrls = [
      '/register-client',
      '/register-realtor',
      'http://localhost:8080/api/v1/users/login'
    ];

    
    const shouldBypass = bypassUrls.some(url => request.url.includes(url));

    if (shouldBypass) {
      return next.handle(request); 
    }

    const authReq = request.clone({
      withCredentials: true
    });

    return next.handle(authReq);
  }

  setToken(token: string) {
    localStorage.setItem('Token', token);
  }

  getToken() {
    return localStorage.getItem('Token');
  }

  deleteToken() {
    this.cookieService.delete('__Secure-JWT');
    return localStorage.removeItem('Token');
  }

  isTokenPresent() {
    return localStorage.getItem('Token') != null;
  }
}
