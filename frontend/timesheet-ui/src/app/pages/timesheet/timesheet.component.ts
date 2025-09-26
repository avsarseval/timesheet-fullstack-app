import {Component, OnInit} from '@angular/core'; // OnInit'i import et
import {CommonModule} from '@angular/common';
import {TimesheetFormComponent} from '../../components/timesheet-form/timesheet-form.component';
import {TimesheetService} from '../../services/timesheet.service';
import {Timesheet} from '../../models/timesheet.model'; // Modeli import et

@Component({
  selector: 'app-timesheet',
  standalone: true,
  imports: [CommonModule, TimesheetFormComponent],
  templateUrl: './timesheet.component.html',
  styleUrls: ['./timesheet.component.scss']
})
export class TimesheetComponent implements OnInit { // OnInit'i implement et

  timesheets: Timesheet[] = []; // Gelen verileri tutacağımız dizi

  // Servisi inject et
  constructor(private timesheetService: TimesheetService) {
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
}
