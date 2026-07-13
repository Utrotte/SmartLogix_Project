import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastController } from '@ionic/angular';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.page.html',
  styleUrls: ['./login.page.scss'],
})
export class LoginPage {
  correo = '';
  password = '';
  mostrarPassword = false;
  loading = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    private toastController: ToastController
  ) {}

  iniciarSesion(): void {
    if (this.loading) {
      return;
    }

    this.loading = true;

    this.authService.iniciarSesion(this.correo, this.password).subscribe({
      next: async (ok) => {
        this.loading = false;

        if (ok) {
          await this.router.navigateByUrl('/dashboard');
          return;
        }

        await this.mostrarToast('No se recibió token de autenticación.');
      },
      error: async (error) => {
        console.error('Error login:', error);
        this.loading = false;
        await this.mostrarToast('Error al iniciar sesión. Verifica las credenciales.');
      }
    });
  }

  private async mostrarToast(message: string): Promise<void> {
    const toast = await this.toastController.create({
      message,
      duration: 2500,
      position: 'top',
      color: 'danger'
    });

    await toast.present();
  }
}
