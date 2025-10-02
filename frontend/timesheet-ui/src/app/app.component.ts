import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TimesheetFormComponent } from './components/timesheet-form/timesheet-form.component';
import { NavbarComponent } from './components/navbar/navbar.component';


@Component({
  selector: 'app-root',
  imports: [RouterOutlet,TimesheetFormComponent, NavbarComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'timesheet-ui';
}
