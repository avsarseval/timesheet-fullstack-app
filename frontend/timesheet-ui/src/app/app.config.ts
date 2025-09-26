// DOSYA: src/app/app.config.ts

import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import {provideHttpClient, HTTP_INTERCEPTORS, withInterceptors} from '@angular/common/http';

// Doğru yerden, doğru sınıfı import ediyoruz.
import { AuthInterceptor } from './interceptors/auth.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),

    provideHttpClient(
      withInterceptors([AuthInterceptor])
    ),
  ]
};
