// src/app/components/timesheet-form/timesheet-form.component.ts

import { Component, EventEmitter, Output, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TimesheetService } from '../../services/timesheet.service';
import { Timesheet } from '../../models/timesheet.model';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-timesheet-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './timesheet-form.component.html',
  styleUrls: ['./timesheet-form.component.scss']
})
export class TimesheetFormComponent implements OnInit {
  @Input() isEditMode = false;
  @Input() timesheetToEdit: Timesheet | null = null;
  @Input() isAdminContext = false;
  @Output() timesheetCreated = new EventEmitter<void>();
  @Output() timesheetUpdated = new EventEmitter<void>();
  @Output() closeModal = new EventEmitter<void>();

  // Form verilerini tutan nesne, doğrudan modelimizi kullanabilir.
  timesheetEntry: Timesheet = {
    date: '',
    startTime: '',
    endTime: '',
    description: ''
  };

  constructor(private timesheetService: TimesheetService, private toastr: ToastrService) { }

  ngOnInit(): void {
    if (this.isEditMode && this.timesheetToEdit) {
      // Düzenleme modunda, formu gelen verinin bir kopyasıyla doldur.
      this.timesheetEntry = { ...this.timesheetToEdit };
    }
  }

  onSubmit() {
    // --- onSubmit MANTIĞINI GÜNCELLİYORUZ ---
    if (this.isEditMode) {
      if (this.isAdminContext) {
        // Eğer admin düzenleme yapıyorsa:
        this.updateAsAdmin();
      } else {
        // Eğer normal kullanıcı düzenleme yapıyorsa:
        this.updateAsUser();
      }
    } else {
      // Yeni kayıt oluşturma (bu sadece normal kullanıcı için geçerli)
      this.createNewTimesheet();
    }
  }

  updateAsAdmin(): void {
    if (!this.timesheetEntry.id) { return; }

    this.timesheetService.adminUpdateTimesheet(this.timesheetEntry.id, this.timesheetEntry).subscribe({
      next: () => {
        this.toastr.success('Kayıt admin tarafından başarıyla güncellendi!');
        this.timesheetUpdated.emit();
      },
      error: (err) =>  this.toastr.error('Güncelleme sırasında bir hata oluştu.')
    });
  }

  updateAsUser(): void {
    if (!this.timesheetEntry.id) { return; }

    this.timesheetService.updateTimesheet(this.timesheetEntry.id, this.timesheetEntry).subscribe({
      next: () => {
        this.toastr.success('Kayıt başarıyla güncellendi!');
        this.timesheetUpdated.emit();
      },
      error: (err) => this.toastr.error('Güncelleme sırasında bir hata oluştu.')
    });
  }
  createNewTimesheet(): void {
    this.timesheetService.createTimesheet(this.timesheetEntry).subscribe({
      next: () => {
        this.toastr.success('Yeni zaman girişi başarıyla eklendi!');
        this.timesheetCreated.emit();
      },
      error: (err) => this.toastr.error('Kayıt sırasında bir hata oluştu.')
    });
  }


  onClose(): void {
    this.closeModal.emit();
  }
}
