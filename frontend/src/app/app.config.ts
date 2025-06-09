import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { providePrimeNG } from 'primeng/config';
import { provideHttpClient } from '@angular/common/http';
import BlueTheme from './mypreset';

import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(),
    provideAnimationsAsync(),
    providePrimeNG({ theme: BlueTheme, ripple: false }),
    provideZoneChangeDetection({ eventCoalescing: true }), 
    provideRouter(routes)
  ]
};