import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class EnviosService {
  constructor(private api: ApiService) {}

  listarEnvios(): Observable<any[]> {
    return this.api.get<any[]>('/envios');
  }

  crearEnvio(envio: any): Observable<any> {
    return this.api.post<any>('/envios', envio);
  }
  cambiarEstado(idEnvio: number, estado: string): Observable<any> {
    const estados: Record<string, number> = { PROGRAMADO: 1, EN_TRANSITO: 2, ENTREGADO: 3 };
    return this.api.put<any>(`/envios/${idEnvio}/estado/${estados[estado] || 1}`);
  }
}
