import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { Router } from '@angular/router';
import { ApiService } from '../../services/api.service';

interface Bodega {
  id?: number;
  idBodega?: number;
  id_bodega?: number;
  nombre?: string;
  direccion?: string;
  activo?: boolean;
  estado?: string;
}

@Component({
  selector: 'app-bodegas',
  standalone: true,
  imports: [CommonModule, FormsModule, IonicModule],
  templateUrl: './bodegas.page.html',
  styleUrls: ['./bodegas.page.scss']
})
export class BodegasPage implements OnInit {
  bodegas: Bodega[] = [];
  bodegasFiltradas: Bodega[] = [];

  terminoBusqueda = '';
  loading = false;
  error = '';
  mostrarFormulario = false;
  nueva: any = { nombre: '', direccion: '', capacidad: 0, ocupacion: 0 };

  constructor(
    private apiService: ApiService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarBodegas();
  }

  cargarBodegas(): void {
    this.loading = true;
    this.error = '';

    this.apiService.get<Bodega[]>('/bodegas').subscribe({
      next: (data) => {
        this.bodegas = Array.isArray(data) ? data : [];
        this.aplicarFiltro();
        this.loading = false;
      },
      error: (err) => {
        console.error('Error cargando bodegas:', err);
        this.error = 'No se pudo cargar la información de bodegas.';
        this.loading = false;
      }
    });
  }

  aplicarFiltro(): void {
    const termino = this.terminoBusqueda.trim().toLowerCase();

    if (!termino) {
      this.bodegasFiltradas = [...this.bodegas];
      return;
    }

    this.bodegasFiltradas = this.bodegas.filter((bodega) => {
      const id = String(this.obtenerId(bodega)).toLowerCase();
      const nombre = bodega.nombre?.toLowerCase() || '';
      const direccion = bodega.direccion?.toLowerCase() || '';
      const estado = this.obtenerEstado(bodega).toLowerCase();

      return id.includes(termino)
        || nombre.includes(termino)
        || direccion.includes(termino)
        || estado.includes(termino);
    });
  }

  volverDashboard(): void {
    this.router.navigate(['/dashboard']);
  }

  irInventario(): void {
    this.router.navigate(['/inventario']);
  }

  obtenerId(bodega: Bodega): number | string {
    return bodega.idBodega ?? bodega.id_bodega ?? bodega.id ?? 'Sin ID';
  }

  obtenerEstado(bodega: Bodega): string {
    if (bodega.estado) {
      return bodega.estado;
    }

    return bodega.activo === false ? 'INACTIVA' : 'ACTIVA';
  }

  colorEstado(bodega: Bodega): string {
    const estado = this.obtenerEstado(bodega).toUpperCase();

    if (estado === 'ACTIVA' || estado === 'ACTIVO') {
      return 'success';
    }

    if (estado === 'INACTIVA' || estado === 'INACTIVO') {
      return 'danger';
    }

    return 'medium';
  }
  crearBodega(): void {
    this.apiService.post<Bodega>('/bodegas', this.nueva).subscribe(() => {
      this.mostrarFormulario = false; this.nueva = { nombre: '', direccion: '', capacidad: 0, ocupacion: 0 }; this.cargarBodegas();
    });
  }
}
