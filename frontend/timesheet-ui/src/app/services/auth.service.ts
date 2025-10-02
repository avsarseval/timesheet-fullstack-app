import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  // Spring Boot backend API'mizin ana URL'si
  private apiUrl = 'http://localhost:8080/api/auth';

  // HttpClient servisini constructor'da "inject" ediyoruz (bağımlılık enjeksiyonu)
  constructor(private http: HttpClient, private router: Router) {

  }

  private decodeToken(token: string): any {
    try {
      // Token'ın orta (payload) kısmını alıp Base64'ten çözüyoruz
      return JSON.parse(atob(token.split('.')[1]));
    } catch (e) {
      console.error('Token çözülürken hata oluştu:', e);
      return null;
    }
  }



  getRoles(): string[] {
    const token = this.getToken();
    if (token) {
      const decodedToken = this.decodeToken(token);
      // Backend'den gelen role isminin "roles" anahtarı altında bir dizi olduğunu varsayıyoruz.
      // Örn: { ..., "roles": ["ROLE_USER", "ROLE_ADMIN"] }
      return decodedToken?.roles || [];
    }
    return [];
  }

  // --- ARADIĞIMIZ EKSİK METOT ---
  isAdmin(): boolean {
    // getRoles() metodunu kullanarak kullanıcının rollerinde 'ROLE_ADMIN' olup olmadığını kontrol et.
    return this.getRoles().includes('ROLE_ADMIN');
  }

  isLoggedIn(): boolean {
    return this.getToken() !== null;
  }
  login(credentials: any): Observable<any> {
    // API'nin /login endpoint'ine POST isteği gönderiyoruz.
    // İsteğin gövdesine (body) kullanıcının girdiği bilgileri koyuyoruz.
    return this.http.post(`${this.apiUrl}/login`, credentials);
  }
  getToken(): string | null {
    return localStorage.getItem('access_token');
  }


  logout(): void {
    // 1. Local Storage'dan access_token'ı temizle.
    // Anahtar isminin tam olarak 'access_token' olduğundan emin olun.
    localStorage.removeItem('access_token');

    // İsteğe bağlı: Varsa diğer kullanıcı bilgilerini de temizle
    // localStorage.removeItem('user_info');

    console.log('Kullanıcı çıkış yaptı, token silindi.');

    // 2. Kullanıcıyı login sayfasına yönlendir.
    // Bu, kullanıcının korumalı sayfalarda kalmasını engeller.
    this.router.navigate(['/login']);
  }

  register(userData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, userData);
  }

}
