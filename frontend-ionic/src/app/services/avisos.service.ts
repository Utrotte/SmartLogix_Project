import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { BehaviorSubject, Observable, tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AvisosService {
  private pendientesSubject = new BehaviorSubject<number>(0);
  pendientes$ = this.pendientesSubject.asObservable();
  constructor(private api: ApiService) {}

  listarAvisos(): Observable<any[]> {
    return this.api.get<any[]>('/avisos').pipe(tap(avisos => this.pendientesSubject.next((avisos || []).filter(a => a.estado_aviso !== 'LEIDO').length)));
  }
  marcarLeido(id: number): Observable<any> { return this.api.patch<any>(`/avisos/${id}/estado`, { estadoAviso: 'LEIDO' }); }
}
