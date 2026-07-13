import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Observable, map } from 'rxjs';

interface LoginRequest { correo: string; contrasena: string }

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(private api: ApiService) {}

  iniciarSesion(correo: string, contrasena: string): Observable<boolean> {
    // Clear any existing session before attempting login
    localStorage.removeItem('auth_token');
    localStorage.removeItem('auth_user');

    const body: LoginRequest = { correo, contrasena };
    // Use public POST that does not send Authorization header
    return this.api.postPublic<any>('/autenticacion/iniciar-sesion', body).pipe(
      map(resp => {
        if (resp && resp.token) {
          this.guardarSesion(resp);
          return true;
        }
        return false;
      })
    );
  }

  guardarSesion(resp: any): void {
    localStorage.setItem('auth_token', resp.token);
    localStorage.setItem('auth_user', JSON.stringify(resp.usuario ?? {}));
  }

  obtenerToken(): string | null { return localStorage.getItem('auth_token'); }
  obtenerUsuario(): any { return JSON.parse(localStorage.getItem('auth_user') || '{}'); }
  estaAutenticado(): boolean { return !!this.obtenerToken(); }
  cerrarSesion(): void { localStorage.removeItem('auth_token'); localStorage.removeItem('auth_user'); }

}
