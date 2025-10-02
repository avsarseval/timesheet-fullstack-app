import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router'; // RouterLink'i import et
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule,AbstractControl } from '@angular/forms'; // ReactiveFormsModule'ü import et
import { AuthService } from '../../services/auth.service'; // Servis yolunuzu doğrulayın
import { ToastrService } from 'ngx-toastr'

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule, // Formlar için
    RouterLink // <a routerLink="..."> için
  ],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private toastr: ToastrService
  ) { }

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [
        Validators.required,
        // Bu tek Regex, hem 8 karakter uzunluğunu hem de diğer tüm kuralları kontrol eder.
        Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$')
      ]]
    });
  }
  // Ana 'password' form kontrolüne erişim için
  get password(): AbstractControl {
    return this.registerForm.get('password')!;
  }

  // Şifre alanı değiştirildi mi kontrolü
  get passwordTouched(): boolean {
    return this.password.dirty || this.password.touched;
  }

  // Her bir kuralı ayrı ayrı kontrol eden getter'lar
  get hasMinLength(): boolean {
    return this.password.value.length >= 8;
  }
  get hasUpperCase(): boolean {
    return /[A-Z]/.test(this.password.value);
  }
  get hasLowerCase(): boolean {
    return /[a-z]/.test(this.password.value);
  }
  get hasDigit(): boolean {
    return /[0-9]/.test(this.password.value);
  }
  get hasSymbol(): boolean {
    // İhtiyacınıza göre sembol listesini genişletebilirsiniz: /[^A-Za-z0-9]/
    return /[!@#$%^&*]/.test(this.password.value);
  }

  onSubmit(): void {
    if (this.registerForm.invalid) {
      return;
    }

    this.authService.register(this.registerForm.value).subscribe({
      next: (response) => {
        this.toastr.success('Şimdi giriş yapabilirsiniz.', 'Kayıt Başarılı!');        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.toastr.error(err.error.message, 'Kayıt Başarısız!'); // <-- YENİSİ
      }
    });
  }
}
