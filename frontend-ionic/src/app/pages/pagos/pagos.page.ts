import { Component } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { CommonModule } from '@angular/common';
import { PagosService } from '../../services/pagos.service';
import { FormsModule } from '@angular/forms';
import { switchMap } from 'rxjs';

@Component({
  selector: 'app-pagos',
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule],
  templateUrl: './pagos.page.html',
  styleUrls: ['./pagos.page.scss']
})
export class PagosPage {
  loading = false;
  error = '';
  items: any[] = [];
  pagoSeleccionado: any = null;
  referenciaValidacion = '';
  resultadoValidacion = 'APROBADO';
  guardando = false;

  constructor(private svc: PagosService) {}

  ngOnInit(): void { this.load(); }

  load() { this.loading = true; this.error = ''; this.svc.listarPagos().subscribe({ next: r => { this.items = r || []; this.loading = false }, error: () => { this.error = 'No se pudo cargar la información'; this.loading = false } }); }
  abrirValidacion(pago: any) { this.pagoSeleccionado = pago; this.referenciaValidacion = ''; this.resultadoValidacion = 'APROBADO'; }
  guardarPago() {
    if (!this.referenciaValidacion.trim() || !this.resultadoValidacion || this.guardando) return;
    this.guardando = true;
    const eraPendiente = this.nombreEstado(this.pagoSeleccionado) === 'PENDIENTE';
    this.svc.registrarValidacion(this.pagoSeleccionado.idPago, this.resultadoValidacion, this.referenciaValidacion.trim()).pipe(
      switchMap(() => eraPendiente && this.resultadoValidacion === 'APROBADO' ? this.svc.aprobar(this.pagoSeleccionado.idPago) : this.svc.listarPagos())
    ).subscribe({ next: () => { this.guardando = false; this.pagoSeleccionado = null; this.load(); }, error: () => { this.guardando = false; this.error = 'No se pudo guardar la validación'; } });
  }
  get montoTotal(): number { return this.items.reduce((s, p) => s + Number(p.monto || 0), 0); }
  get pendientes(): number { return this.items.filter(p => this.nombreEstado(p) === 'PENDIENTE').length; }
  nombreMetodo(pago: any): string { return pago.metodoPago?.nombre || pago.metodoPago || 'Por definir'; }
  nombreEstado(pago: any): string { return pago.estadoPago?.nombre || pago.estadoPago || 'PENDIENTE'; }
  referencia(pago: any): string { return pago.referenciaTransaccion || pago.referencia || `PAY-${pago.idPago}`; }
  fecha(pago: any): any { return pago.fechaRegistro || pago.fechaPago; }
}
