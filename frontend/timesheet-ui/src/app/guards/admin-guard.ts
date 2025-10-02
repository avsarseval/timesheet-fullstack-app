// src/app/guards/admin.guard.ts

import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service'; // <-- 1. AuthService'i import ediyoruz

export const adminGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService); // <-- 2. AuthService'i inject ediyoruz
  const router = inject(Router);

  // 3. Token çözme, rol kontrolü gibi tüm karmaşık işleri AuthService'e bırakıyoruz.
  //    Guard'ın tek görevi, AuthService'den gelen cevaba göre karar vermek.
  if (authService.isAdmin()) {
    return true; // Evet, admin. Sayfaya erişebilir.
  }

  // Eğer kullanıcı admin değilse, onu zaman çizelgesi sayfasına yönlendiriyoruz.
  console.warn('Admin yetkisi olmayan kullanıcı /admin yoluna erişmeye çalıştı. Yönlendiriliyor.');
  router.navigate(['/timesheet']);
  return false;
};
