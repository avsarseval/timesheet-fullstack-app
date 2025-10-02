import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  loginForm: FormGroup;

  constructor(private fb: FormBuilder, private authService: AuthService,
              private router: Router,  private toastr: ToastrService) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      console.log('Form verileri API\'ye gönderiliyor:', this.loginForm.value);

      // AuthService'deki login metodunu çağırıyoruz
      this.authService.login(this.loginForm.value).subscribe({
        next: (response) => {
          console.log('Giriş başarılı, Token:', response); // Backend'den gelen token
          console.log('>>> ANGULAR TARAFINDAN ALINAN TOKEN:', response);
          localStorage.setItem('access_token', response.accessToken);
          this.router.navigate(['/timesheet']);
        }, error: (err) => {
          console.error('Giriş başarısız, Hata:', err);
          this.toastr.error('Hatalı kullanıcı adı veya şifre.', 'Giriş Başarısız!');
        }
      });

    } else {
      console.error('Form geçerli değil.');
    }
  }
}
