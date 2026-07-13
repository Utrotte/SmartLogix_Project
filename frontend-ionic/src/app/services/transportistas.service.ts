import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class TransportistasService {
  constructor(private api: ApiService) {}

  listarTransportistas(): Observable<any[]> {
    return this.api.get<any[]>('/transportistas');
  }
  crear(data: any): Observable<any> { return this.api.post<any>('/transportistas', data); }
}
