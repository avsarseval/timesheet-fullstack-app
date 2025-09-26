import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Timesheet } from '../models/timesheet.model';
@Injectable({
  providedIn: 'root'
})
export class TimesheetService {
  private apiUrl = 'http://localhost:8080/api/timesheets'; // Timesheet endpoint'inin adresi

  constructor(private http: HttpClient) { }

  // Giriş yapmış kullanıcının tüm zaman çizelgelerini getirir
  getTimesheetsForUser(): Observable<any> {
    // Not: Bu endpoint'in Spring Security tarafından korunuyor olması gerekir.
    // Hangi kullanıcıya ait olduğunu JWT Token'dan anlayacak.
    return this.http.get<any>(`${this.apiUrl}`);
  }

  // Yeni bir zaman çizelgesi oluşturur
  createTimesheet(timesheetData: Timesheet): Observable<any> {
    return this.http.post<any>(this.apiUrl, timesheetData);
  }
}
