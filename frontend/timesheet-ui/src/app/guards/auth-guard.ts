import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // AuthService'e sor: Kullanıcı giriş yapmış mı?
  if (authService.isLoggedIn()) {
    return true; // Evet, yapmış. Sayfaya erişimine izin ver.
  } else {
    // Hayır, yapmamış. Onu login sayfasına yönlendir.
    router.navigate(['/login']);
    return false; // Sayfaya erişimini engelle.
  }
};
