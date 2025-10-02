// DOSYA: src/app/app.config.ts

import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import {provideHttpClient, withInterceptors} from '@angular/common/http';

// Doğru yerden, doğru sınıfı import ediyoruz.
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { provideToastr } from 'ngx-toastr';
import {provideAnimations} from '@angular/platform-browser/animations';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(
      withInterceptors([AuthInterceptor]),
    ),
    provideAnimations(), // Tarayıcı animasyonları için temel sağlayıcı
    provideToastr({      // ngx-toastr için yapılandırma
      timeOut: 3000,
      positionClass: 'toast-top-right',
      preventDuplicates: true,
    }),
  ]
};
