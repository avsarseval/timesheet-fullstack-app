import {Component, OnInit} from '@angular/core'; // OnInit'i import et
import {CommonModule} from '@angular/common';
import {TimesheetFormComponent} from '../../components/timesheet-form/timesheet-form.component';
import {TimesheetService} from '../../services/timesheet.service';
import {Timesheet} from '../../models/timesheet.model'; // Modeli import et
import { ToastrService } from 'ngx-toastr';
@Component({
  selector: 'app-timesheet',
  standalone: true,
  imports: [CommonModule, TimesheetFormComponent],
  templateUrl: './timesheet.component.html',
  styleUrls: ['./timesheet.component.scss']
})
export class TimesheetComponent implements OnInit { // OnInit'i implement et

  timesheets: Timesheet[] = []; // Gelen verileri tutacağımız dizi
  isModalOpen = false; // <-- 1. Modal durumunu tutacak değişkeni ekleyin
  isEditMode = false;
  currentTimesheet: Timesheet | null = null;


  // Servisi inject et
  constructor(private timesheetService: TimesheetService,private toastr: ToastrService) {
  }

  // Component ilk yüklendiğinde bu metot otomatik olarak çalışır
  ngOnInit(): void {
    this.loadTimesheets();
  }

  loadTimesheets(): void {
    this.timesheetService.getTimesheetsForUser().subscribe(
      response => {
        if (response) {
          this.timesheets = response?.content;
          console.log('Zaman çizelgeleri başarıyla çekildi:', this.timesheets);
        } else {
          console.error('Zaman çizelgeleri çekilirken hata oluştu:');
        }
      });
  }
  // --- 2. Modal'ı açmak ve kapatmak için metotlar ekleyin ---
  openModal(): void {
    this.isEditMode = false; // "Yeni Giriş" modunda aç
    this.currentTimesheet = null; // Düzenlenecek veriyi temizle
    this.isModalOpen = true;
  }


  closeModal(): void {
    this.isModalOpen = false;
  }

  // --- 3. Yeni bir zaman çizelgesi eklendiğinde listeyi yenilemek için---
  onTimesheetCreated(): void {
    this.loadTimesheets(); // Verileri yeniden çek
    this.closeModal();     // Modal'ı kapat
  }
  onDelete(id: number | undefined ): void {
    if (!id) {
      console.error("Silme işlemi için ID bulunamadı!");
      return;
    }
    // Kullanıcıya silme işlemi öncesi onay sorusu sormak iyi bir pratiktir.
    if (confirm('Bu kaydı silmek istediğinizden emin misiniz?')) {
      this.timesheetService.deleteTimesheet(id).subscribe({
        next: () => {
          console.log('Kayıt başarıyla silindi.');
          this.toastr.success("Başarılı",'Kayıt başarıyla silindi!');
          this.loadTimesheets(); // Liste'yi yenile
        },
        error: (err) => {
          console.error('Kayıt silinirken hata oluştu:', err);
          this.toastr.error(err,'Kayıt Silinemedi!');
        }
      });
    }
  }

  // --- YENİ METOT: DÜZENLEME İŞLEVİNİ BAŞLATMA ---
  onEdit(timesheet: Timesheet): void {
    this.isEditMode = true; // "Düzenleme" modunda aç
    this.currentTimesheet = { ...timesheet }; // Düzenlenecek veriyi kopyala
    this.isModalOpen = true; // Modal'ı aç
  }

  // --- GÜNCELLEME İŞLEVİ ---
  // Bu metot, form bileşeninden gelen güncellenmiş veriyi alacak.
  onTimesheetUpdated(): void {
    this.loadTimesheets();
    this.closeModal();
  }
}
