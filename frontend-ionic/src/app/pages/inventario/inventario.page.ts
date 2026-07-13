import { Component } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { CommonModule } from '@angular/common';
import { InventarioService } from '../../services/inventario.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-inventario',
  standalone: true,
  imports: [IonicModule, CommonModule],
  templateUrl: './inventario.page.html',
  styleUrls: ['./inventario.page.scss']
})
export class InventarioPage {
  loading = false;
  error: string | null = null;
  existencias: any[] = [];

  constructor(private inventarioService: InventarioService, private router: Router) {}

  ngOnInit(): void {
    this.loadExistencias();
  }

  loadExistencias() {
    this.loading = true;
    this.error = null;
    this.inventarioService.listarExistencias().subscribe({
      next: (r) => { this.existencias = r ?? []; this.loading = false; },
      error: (e) => { this.error = 'Error al cargar existencias'; this.loading = false; }
    });
  }

  volver() { this.router.navigate(['/dashboard']); }
  get stockTotal(): number { return this.existencias.reduce((s, e) => s + Number(e.stockActual ?? 0), 0); }
  get reservadoTotal(): number { return this.existencias.reduce((s, e) => s + Number(e.stockReservado ?? 0), 0); }
  get disponibleTotal(): number { return this.existencias.reduce((s, e) => s + Number(e.stockDisponible ?? 0), 0); }
}
