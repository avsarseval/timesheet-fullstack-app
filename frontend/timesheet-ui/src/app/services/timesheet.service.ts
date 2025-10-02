import { Injectable } from '@angular/core';
import { HttpClient,HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Timesheet } from '../models/timesheet.model';
@Injectable({
  providedIn: 'root'
})
export class TimesheetService {
  private apiUrl = 'http://localhost:8080/api';



  constructor(private http: HttpClient) { }

  // Giriş yapmış kullanıcının tüm zaman çizelgelerini getirir
  getTimesheetsForUser(): Observable<any> {
    // Not: Bu endpoint'in Spring Security tarafından korunuyor olması gerekir.
    // Hangi kullanıcıya ait olduğunu JWT Token'dan anlayacak.
    return this.http.get<any>(`${this.apiUrl}/timesheets`);
  }

  // Yeni bir zaman çizelgesi oluşturur
  createTimesheet(timesheetData: Timesheet): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/timesheets`, timesheetData);
  }
  getAllTimesheets(page: number, size: number): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    // Burada da tam ve doğru yolu belirtiyoruz.
    return this.http.get(`${this.apiUrl}/admin/timesheets/all`, { params: params });
  }
  deleteTimesheet(id: number): Observable<any> {
    // Backend'e DELETE isteği gönderir: /api/timesheets/{id}
    return this.http.delete(`${this.apiUrl}/timesheets/${id}`,{ responseType: 'text' });
  }

  // --- YENİ METOT: Zaman Çizelgesini Güncelleme ---
  updateTimesheet(id: number, timesheetData: Timesheet): Observable<any> {
    // Backend'e PUT isteği gönderir: /api/timesheets/{id}
    return this.http.put(`${this.apiUrl}/timesheets/${id}`, timesheetData);
  }
  adminUpdateTimesheet(id: number, timesheetData: any): Observable<any> {
    // Backend kodunuza göre doğru URL: /api/admin/timesheets/{id}
    return this.http.put(`${this.apiUrl}/admin/timesheets/${id}`, timesheetData);
  }

  // --- YENİ ADMİN METODU: Admin Tarafından Silme ---
  adminDeleteTimesheet(id: number): Observable<any> {
    // Backend kodunuza göre doğru URL: /api/admin/timesheets/{id}
    return this.http.delete(`${this.apiUrl}/admin/timesheets/${id}`,  { responseType: 'text' });
  }
}
