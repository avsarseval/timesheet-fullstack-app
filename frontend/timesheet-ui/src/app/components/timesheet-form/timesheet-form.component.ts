import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms'; // FormsModule'u import ediyoruz
import {TimesheetService} from '../../services/timesheet.service';

@Component({
  selector: 'app-timesheet-form',
  standalone: true,
  imports: [CommonModule, FormsModule], // FormsModule'u imports dizisine ekliyoruz
  templateUrl: './timesheet-form.component.html',
  styleUrls: ['./timesheet-form.component.scss']
})
export class TimesheetFormComponent {

  // Form verilerini tutacak bir nesne tanımlıyoruz
  timesheetEntry = {
    date: '',
    startTime: '',
    endTime: '',
    description: ''
  };

  // TimesheetService'i constructor'da inject ediyoruz
  constructor(private timesheetService: TimesheetService) {
  }

  // Form gönderildiğinde çalışacak olan metod
  onSubmit() {
    console.log('Yeni zaman çizelgesi verileri API\'ye gönderiliyor:', this.timesheetEntry);

    // Servisimizdeki createTimesheet metodunu çağırıyoruz
    this.timesheetService.createTimesheet(this.timesheetEntry).subscribe(
      response => {
        if (response) {
          console.log('Zaman çizelgesi başarıyla kaydedildi:', response);
          // BAŞARILI!
          // TODO: Formu temizle ve sayfadaki listeyi yenile.
          alert('Yeni zaman girişi başarıyla eklendi!');
          window.location.reload(); // ŞİMDİLİK en kolay yöntemle sayfayı yeniliyoruz.
        } else {
          console.error('Zaman çizelgesi kaydedilirken hata oluştu:');
          // BAŞARISIZ!
          // TODO: Kullanıcıya bir hata mesajı göster.
          alert('Bir hata oluştu. Lütfen tekrar deneyin.');
        }
      });
  }
}


