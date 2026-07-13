import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  constructor(private api: ApiService) {}

  obtenerResumen(): Observable<any> {
    return this.api.get<any>('/bff/dashboard/resumen');
  }
}
