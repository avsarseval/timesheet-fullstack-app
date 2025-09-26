import { Routes } from '@angular/router';

import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { TimesheetComponent } from './pages/timesheet/timesheet.component';
import { AdminComponent } from './pages/admin/admin.component';
import { authGuard } from './guards/auth-guard'; // Oluşturduğumuz Guard'ı import et

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },

  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  {
    path: 'timesheet',
    component: TimesheetComponent,
    canActivate: [authGuard]
  },
  {
    path: 'admin',
    component: AdminComponent,
    canActivate: [authGuard]
  }
];
