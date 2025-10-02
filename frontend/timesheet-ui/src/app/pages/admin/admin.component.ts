import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TimesheetService } from '../../services/timesheet.service';
import { TimesheetFormComponent } from '../../components/timesheet-form/timesheet-form.component';
import { AppConfirmBoxComponent } from '../../components/app-confirm-box/app-confirm-box.component';
import { Timesheet } from '../../models/timesheet.model';
import { ToastrService } from 'ngx-toastr';


@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule,TimesheetFormComponent, AppConfirmBoxComponent],
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {
  allTimesheets: any[] = [];
  isLoading = true;
  isConfirmModalOpen = false;
  idToDelete: number | null = null;

  currentPage = 0;
  pageSize = 10;
  totalPages = 0;
  isModalOpen = false;
  currentTimesheet: Timesheet | null = null;

  constructor(private timesheetService: TimesheetService,private toastr: ToastrService) {}
  ngOnInit(): void {
    // Bileşen ilk yüklendiğinde verileri çekmek için yeni metodu çağır.
    this.loadTimesheets();
  }

  // --- VERİ ÇEKME MANTIĞINI AYRI BİR METODA TAŞIYALIM ---
  loadTimesheets(): void {
    this.isLoading = true; // Yükleme başladığında true yap
    this.timesheetService.getAllTimesheets(this.currentPage, this.pageSize).subscribe({
      next: (response) => {
        // Backend'den gelen 'Page' nesnesinin 'content' alanı asıl listeyi içerir.
        this.allTimesheets = response.content;

        // Diğer sayfalama bilgilerini de alalım.
        this.totalPages = response.totalPages;

        this.isLoading = false; // Yükleme bittiğinde false yap
      },
      error: (err) => {
        console.error('Admin verileri çekilirken hata oluştu:', err);
        this.isLoading = false;
      }
    });
  }

  // --- İLERİ VE GERİ GİTMEK İÇİN METOTLAR (BONUS) ---
  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.loadTimesheets();
    }
  }

  previousPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadTimesheets();
    }
  }
  onDelete(id: number | undefined): void {
    if (!id) {
      this.toastr.error("Silme işlemi için ID bulunamadı!");
      return;
    }
    this.idToDelete = id; // Hangi ID'nin silineceğini sakla
    this.isConfirmModalOpen = true; // Onay penceresini aç
  }

  // Onay penceresinden "Evet" cevabı geldiğinde bu metot çalışır.
  confirmDelete(): void {
    if (!this.idToDelete) return; // Güvenlik kontrolü

    this.timesheetService.adminDeleteTimesheet(this.idToDelete).subscribe({
      next: (response: any) => {
        this.toastr.success(response || 'Kayıt başarıyla silindi.');
        this.loadTimesheets();
      },
      error: (err: any) => {
        this.toastr.error('Bir hata oluştu, kayıt silinemedi.');
      }
    });

    this.closeConfirmModal(); // İşlem bittikten sonra pencereyi kapat
  }

  // Onay penceresini kapatmak için
  closeConfirmModal(): void {
    this.isConfirmModalOpen = false;
    this.idToDelete = null; // Saklanan ID'yi temizle
  }


  onEdit(timesheet: Timesheet): void {
    this.currentTimesheet = { ...timesheet }; // Düzenlenecek veriyi kopyala
    this.isModalOpen = true; // Modal'ı aç
  }

  closeModal(): void {
    this.isModalOpen = false;
  }

  // Formdan gelen 'timesheetUpdated' olayını yakalayıp listeyi yeniliyoruz.
  onTimesheetUpdated(): void {
    this.loadTimesheets();
    this.closeModal();
  }


  exportToCsv(): void {
    const headers = ['Kullanıcı', 'Tarih', 'Başlangıç', 'Bitiş', 'Açıklama'];

    // Veri modelinizde 'user' nesnesi olduğunu varsayarak yolu düzeltelim
    const data = this.allTimesheets.map(entry => [
      entry.username,
      entry.date,
      entry.startTime,
      entry.endTime,
      // Açıklama null veya undefined ise boş string'e çevir ve virgülleri temizle
      (entry.description || '').replace(/,/g, ';')
    ]);

    // Başlıkları ve verileri birleştirerek CSV satırlarını oluştur
    const rows = [headers.join(','), ...data.map(row => row.join(','))];

    // Tüm satırları yeni satır karakteriyle birleştir
    let csvString = rows.join('\n');

    // --- EN ÖNEMLİ DEĞİŞİKLİK BURADA ---
    // UTF-8 BOM'u dosyanın başına ekliyoruz.
    const blob = new Blob(['\uFEFF' + csvString], { type: 'text/csv;charset=utf-8;' });

    // İndirme linkini oluşturuyoruz
    const link = document.createElement("a");
    if (link.download !== undefined) { // Tarayıcı indirmeyi destekliyor mu kontrolü
      const url = URL.createObjectURL(blob);
      link.setAttribute("href", url);
      link.setAttribute("download", "zaman_cizelgeleri.csv");
      link.style.visibility = 'hidden';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    }
  }
}
