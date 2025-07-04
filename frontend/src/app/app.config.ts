import { ApplicationConfig, provideZoneChangeDetection, inject, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { providePrimeNG } from 'primeng/config';
import { provideHttpClient, withInterceptors, HttpHandlerFn, HttpRequest, HttpClient } from '@angular/common/http';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { HttpLoaderFactory } from './translate-loader';
import BlueTheme from './mypreset';
import { routes } from './app.routes';
import { CookieService } from 'ngx-cookie-service';
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
    provideRouter(routes),
    importProvidersFrom(
      TranslateModule.forRoot({
        loader: {
          provide: TranslateLoader,
          useFactory: HttpLoaderFactory,
          deps: [HttpClient]
        },
        defaultLanguage: 'en'
      })
    )
  ]
};