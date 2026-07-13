import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ClientesService {
  constructor(private api: ApiService) {}

  listarClientes(): Observable<any[]> {
    return this.api.get<any[]>('/clientes');
  }
}
