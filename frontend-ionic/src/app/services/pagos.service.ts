import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class PagosService {
  constructor(private api: ApiService) {}

  listarPagos(): Observable<any[]> {
    return this.api.get<any[]>('/pagos');
  }

  aprobar(idPago: number): Observable<any> { return this.api.put<any>(`/pagos/${idPago}/estado/2`); }
  registrarValidacion(idPago: number, resultado: string, detalle: string): Observable<any> {
    return this.api.post<any>(`/pagos/${idPago}/validaciones`, { resultado, detalle });
  }
}
