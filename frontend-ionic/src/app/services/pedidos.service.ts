import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class PedidosService {
  constructor(private api: ApiService) {}

  obtenerPedidos(): Observable<any[]> {
    return this.api.get<any[]>('/pedidos');
  }

  crearPedido(body: any): Observable<any> {
    return this.api.post<any>('/pedidos', body);
  }

  actualizarEstado(id: number, estadoPedido: string): Observable<any> {
    return this.api.patch<any>(`/pedidos/${id}/estado`, { estadoPedido });
  }
}
