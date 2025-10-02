// src/app/components/app-confirm-box/app-confirm-box.component.ts

import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-confirm-box',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './app-confirm-box.component.html',
  styleUrls: ['./app-confirm-box.component.scss']
})
export class AppConfirmBoxComponent {
  // Dışarıdan gösterilecek mesajı alıyoruz. Varsayılan bir mesaj da ekleyelim.
  @Input() message: string = 'Bu işlemi yapmak istediğinizden emin misiniz?';

  // Dışarıya "onaylandı" veya "iptal edildi" bilgisini gönderecek olaylar.
  @Output() confirm = new EventEmitter<void>();
  @Output() cancel = new EventEmitter<void>();

  // "Evet" butonuna basıldığında bu metot çalışır.
  onConfirm(): void {
    this.confirm.emit();
  }

  // "Hayır" veya 'x' butonuna basıldığında bu metot çalışır.
  onCancel(): void {
    this.cancel.emit();
  }
}
