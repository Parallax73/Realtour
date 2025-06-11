import { ApplicationConfig, provideZoneChangeDetection, inject } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { providePrimeNG } from 'primeng/config';
import { provideHttpClient, withInterceptors, HttpHandlerFn, HttpRequest } from '@angular/common/http';
import BlueTheme from './mypreset';
import { routes } from './app.routes';
import {CookieService} from 'ngx-cookie-service';
import { AuthService } from './services/auth/auth.service';

const authInterceptorFn = (req: HttpRequest<unknown>, next: HttpHandlerFn) => {
  return inject(AuthService).intercept(req, {
    handle: (request) => next(request)
  });
};

export const appConfig: ApplicationConfig = {
  providers: [
    AuthService,
    CookieService,
    provideHttpClient(
      withInterceptors([authInterceptorFn])
    ),
    provideAnimationsAsync(),
    providePrimeNG({ theme: BlueTheme, ripple: false }),
    provideZoneChangeDetection({ eventCoalescing: true }), 
    provideRouter(routes)
  ]
};