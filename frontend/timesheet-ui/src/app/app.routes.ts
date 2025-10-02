import { Routes } from '@angular/router';
import { authGuard } from './guards/auth-guard'; // Oluşturduğumuz Guard'ı import et
import { adminGuard } from './guards/admin-guard'; // <-- 1. Guard'ı import et
import { MainLayout } from './layouts/main-layout/main-layout';

export const routes: Routes = [
  // --- GİRİŞ YAPMAMIŞ KULLANICI ROTALARI (Navbar'sız) ---
  {
    path: 'login',
    loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./pages/register/register.component').then(m => m.RegisterComponent)
  },

  // --- ANA UYGULAMA ROTALARI (Navbar'lı ve Korumalı) ---
  {
    path: '', // Ana yol ('/')
    component: MainLayout, // Bu layout'u kullan
    canActivate: [authGuard], // Bu gruba girmek için giriş yapmış olmak zorunlu
    children: [
      {
        path: 'timesheet',
        loadComponent: () => import('./pages/timesheet/timesheet.component').then(m => m.TimesheetComponent)
      },
      {
        path: 'admin',
        loadComponent: () => import('./pages/admin/admin.component').then(m => m.AdminComponent),
        canActivate: [adminGuard] // Admin sayfasına girmek için ekstra admin kontrolü
      },
      // Eğer kullanıcı ana yola ('/') gelirse, onu timesheet'e yönlendir.
      { path: '', redirectTo: 'timesheet', pathMatch: 'full' }
    ]
  },

  // Hiçbir rotayla eşleşmezse, login'e yönlendir.
  { path: '**', redirectTo: 'login' }
];
