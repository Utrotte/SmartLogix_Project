import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class InventarioService {
  constructor(private api: ApiService) {}

  listarExistencias(): Observable<any[]> {
    return this.api.get<any[]>('/inventario/existencias');
  }
}
