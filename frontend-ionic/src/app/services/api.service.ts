import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private base = '/api';

  constructor(private http: HttpClient) {}

  get<T>(endpoint: string, params?: Record<string, string | number>): Observable<T> {
    let httpParams = new HttpParams();
    if (params) {
      Object.keys(params).forEach(k => httpParams = httpParams.set(k, String(params[k])));
    }
    return this.http.get<T>(this.base + endpoint, { params: httpParams });
  }

  post<T>(endpoint: string, body: any): Observable<T> {
    return this.http.post<T>(this.base + endpoint, body);
  }

  /**
   * Public POST that does not include Authorization header.
   * Use for login or public endpoints that must not include an auth token.
   */
  postPublic<T>(endpoint: string, body: any): Observable<T> {
    return this.http.post<T>(this.base + endpoint, body);
  }

  put<T>(endpoint: string, body: any = {}): Observable<T> {
    return this.http.put<T>(this.base + endpoint, body);
  }

  patch<T>(endpoint: string, body: any = {}): Observable<T> {
    return this.http.patch<T>(this.base + endpoint, body);
  }

}
