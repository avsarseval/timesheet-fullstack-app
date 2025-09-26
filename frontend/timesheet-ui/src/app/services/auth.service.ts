import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
// Asenkron operasyonlar için Observable
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  // Spring Boot backend API'mizin ana URL'si
  private apiUrl = 'http://localhost:8080/api/auth';

  // HttpClient servisini constructor'da "inject" ediyoruz (bağımlılık enjeksiyonu)
  constructor(private http: HttpClient) { }


  login(credentials: any): Observable<any> {
    // API'nin /login endpoint'ine POST isteği gönderiyoruz.
    // İsteğin gövdesine (body) kullanıcının girdiği bilgileri koyuyoruz.
    return this.http.post(`${this.apiUrl}/login`, credentials);
  }
  getToken(): string | null {
    return localStorage.getItem('access_token');
  }

  // YENİ METOT: Kullanıcının giriş yapıp yapmadığını kontrol eder
  isLoggedIn(): boolean {
    return this.getToken() !== null;
  }

  // YENİ METOT: Çıkış yaparken token'ı siler
  logout(): void {
    localStorage.removeItem('access_token');
    // TODO: Kullanıcıyı /login sayfasına yönlendir. Bu işlemi bileşen içinde yapacağız.
  }

  // TODO: Register metodu da buraya eklenecek
  // TODO: Logout metodu da buraya eklenecek
}
