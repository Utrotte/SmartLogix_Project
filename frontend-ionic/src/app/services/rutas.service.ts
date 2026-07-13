import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class RutasService {
  constructor(private api: ApiService) {}

  listarRutas(): Observable<any[]> {
    return this.api.get<any[]>('/rutas');
  }
  crear(data: any): Observable<any> { return this.api.post<any>('/rutas', data); }
}
