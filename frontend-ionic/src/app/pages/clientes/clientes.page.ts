import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { Router } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { FormsModule } from '@angular/forms';

interface Cliente {
  idCliente?: number;
  id?: number;
  nombre?: string;
  apellidoRazonSocial?: string;
  documento?: string;
  correo?: string;
  telefono?: string;
  estado?: string;
  nombreTipoCliente?: string;
}

@Component({
  selector: 'app-clientes',
  standalone: true,
  imports: [CommonModule, IonicModule, FormsModule],
  templateUrl: './clientes.page.html',
  styleUrls: ['./clientes.page.scss']
})
export class ClientesPage implements OnInit {
  clientes: Cliente[] = [];
  loading = false;
  error = '';
  mostrarFormulario = false;
  nuevo: any = { nombre: '', apellidoRazonSocial: '', documento: '', correo: '', telefono: '', nombreTipoCliente: 'Empresa' };

  constructor(
    private apiService: ApiService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarClientes();
  }

  cargarClientes(): void {
    this.loading = true;
    this.error = '';

    this.apiService.get<Cliente[]>('/clientes').subscribe({
      next: (data) => {
        this.clientes = Array.isArray(data) ? data : [];
        this.loading = false;
      },
      error: (err) => {
        console.error('Error cargando clientes:', err);
        this.error = 'No se pudo cargar la información de clientes.';
        this.loading = false;
      }
    });
  }

  volverDashboard(): void {
    this.router.navigate(['/dashboard']);
  }

  obtenerId(cliente: Cliente): number | string {
    return cliente.idCliente ?? cliente.id ?? 'Sin ID';
  }
  crearCliente(): void {
    this.apiService.post<Cliente>('/clientes', this.nuevo).subscribe(() => {
      this.mostrarFormulario = false;
      this.nuevo = { nombre: '', apellidoRazonSocial: '', documento: '', correo: '', telefono: '', nombreTipoCliente: 'Empresa' };
      this.cargarClientes();
    });
  }
}
