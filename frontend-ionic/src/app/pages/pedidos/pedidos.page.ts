import { Component } from '@angular/core';
import { IonicModule, ToastController } from '@ionic/angular';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PedidosService } from '../../services/pedidos.service';
import { Router } from '@angular/router';
import { forkJoin } from 'rxjs';
import { InventarioService } from '../../services/inventario.service';
import { EnviosService } from '../../services/envios.service';
import { ClientesService } from '../../services/clientes.service';
import { ProductosService } from '../../services/productos.service';
import { BodegasService } from '../../services/bodegas.service';
import { TransportistasService } from '../../services/transportistas.service';
import { RutasService } from '../../services/rutas.service';

@Component({
  selector: 'app-pedidos',
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule],
  templateUrl: './pedidos.page.html',
  styleUrls: ['./pedidos.page.scss']
})
export class PedidosPage {
  loading = false;
  error = '';
  pedidos: any[] = [];
  existencias: any[] = [];
  clientes: any[] = [];
  productos: any[] = [];
  bodegas: any[] = [];
  posicionSeleccionada = '';
  transportistas: any[] = [];
  rutas: any[] = [];
  pedidoParaEnvio: any = null;
  planEnvio = { idTransportista: 0, idRuta: 0, fechaEstimadaEntrega: '', observacion: '' };

  // Form defaults
  idCliente = 1;
  idProducto = 1;
  idBodega = 1;
  cantidad = 1;
  precioUnitario = 15000;

  creating = false;

  constructor(
    private pedidosService: PedidosService,
    private inventarioService: InventarioService,
    private enviosService: EnviosService,
    private clientesService: ClientesService,
    private productosService: ProductosService,
    private bodegasService: BodegasService,
    private transportistasService: TransportistasService,
    private rutasService: RutasService,
    private router: Router,
    private toastCtrl: ToastController
  ) {}

  ngOnInit(): void {
    this.cargarOperacion();
  }

  cargarOperacion() {
    this.loading = true;
    this.error = '';
    forkJoin({
      pedidos: this.pedidosService.obtenerPedidos(),
      existencias: this.inventarioService.listarExistencias(),
      clientes: this.clientesService.listarClientes(),
      productos: this.productosService.listarProductos(),
      bodegas: this.bodegasService.listarBodegas(),
      transportistas: this.transportistasService.listarTransportistas(),
      rutas: this.rutasService.listarRutas()
    }).subscribe({
      next: ({ pedidos, existencias, clientes, productos, bodegas, transportistas, rutas }) => {
        this.pedidos = pedidos || [];
        this.existencias = existencias || [];
        this.clientes = clientes || [];
        this.productos = productos || [];
        this.bodegas = bodegas || [];
        this.transportistas = transportistas || [];
        this.rutas = rutas || [];
        if (!this.clientes.some(c => Number(c.idCliente ?? c.id) === Number(this.idCliente)) && this.clientes[0]) {
          this.idCliente = this.clientes[0].idCliente ?? this.clientes[0].id;
        }
        const primera = this.existencias[0];
        if (primera) {
          this.idProducto = primera.idProducto ?? primera.id_producto ?? this.idProducto;
          this.idBodega = primera.idBodega ?? primera.id_bodega ?? this.idBodega;
          this.posicionSeleccionada = `${this.idProducto}|${this.idBodega}`;
          this.actualizarPosicion();
        }
        this.loading = false;
      },
      error: () => { this.error = 'No se pudo sincronizar pedidos e inventario'; this.loading = false; }
    });
  }

  loadPedidos() {
    this.loading = true;
    this.error = '';
    this.pedidosService.obtenerPedidos().subscribe({
      next: (r) => { this.pedidos = r || []; this.loading = false; },
      error: (e) => { this.error = 'Error al cargar pedidos'; this.loading = false; }
    });
  }

  async crearPedido() {
    if (!this.stockSuficiente) {
      await this.showToast('Stock insuficiente para reservar esta cantidad');
      return;
    }
    this.creating = true;
    this.error = '';
    const body = {
      idCliente: Number(this.idCliente),
      detalles: [
        {
          idProducto: Number(this.idProducto),
          idBodega: Number(this.idBodega),
          cantidad: Number(this.cantidad),
          precioUnitario: Number(this.precioUnitario)
        }
      ]
    };

    this.pedidosService.crearPedido(body).subscribe({
      next: async () => {
        this.creating = false;
        await this.showToast('Pedido creado correctamente');
        this.cargarOperacion();
      },
      error: async (err) => {
        this.creating = false;
        const msg = err && err.error && err.error.message ? err.error.message : 'Error al crear pedido';
        await this.showToast(msg);
        this.error = msg;
      }
    });
  }

  get existenciaSeleccionada(): any {
    return this.existencias.find(e =>
      Number(e.idProducto ?? e.id_producto) === Number(this.idProducto) &&
      Number(e.idBodega ?? e.id_bodega) === Number(this.idBodega));
  }

  get stockDisponible(): number {
    const e = this.existenciaSeleccionada;
    return Number(e?.stockDisponible ?? e?.stock_disponible ?? 0);
  }

  get stockSuficiente(): boolean {
    return Number(this.cantidad) > 0 && Number(this.cantidad) <= this.stockDisponible;
  }

  get totalPedido(): number { return Number(this.cantidad) * Number(this.precioUnitario); }

  actualizarPosicion(): void {
    const [producto, bodega] = this.posicionSeleccionada.split('|').map(Number);
    if (!producto || !bodega) return;
    this.idProducto = producto; this.idBodega = bodega;
    const precio = this.productos.find(p => Number(p.idProducto ?? p.id) === producto)?.precioActual;
    if (precio) this.precioUnitario = Number(precio);
  }

  nombreCliente(id: number): string {
    const cliente = this.clientes.find(c => Number(c.idCliente ?? c.id) === Number(id));
    return cliente?.nombre || cliente?.apellidoRazonSocial || 'Cliente no disponible';
  }
  nombreProducto(id: number): string { return this.productos.find(p => Number(p.idProducto ?? p.id) === Number(id))?.nombre || 'Producto no disponible'; }
  skuProducto(id: number): string { return this.productos.find(p => Number(p.idProducto ?? p.id) === Number(id))?.codigoSku || 'Sin SKU'; }
  nombreBodega(id: number): string { return this.bodegas.find(b => Number(b.idBodega ?? b.id) === Number(id))?.nombre || 'Bodega no disponible'; }

  async prepararEnvio(pedido: any) {
    const ahora = new Date();
    const entrega = new Date(ahora.getTime() + 48 * 60 * 60 * 1000);
    const body = {
      idPedido: pedido.id,
      idTransportista: 1,
      estadoEnvio: { idEstadoEnvio: 1 },
      codigoEnvio: `SLX-${String(pedido.id).padStart(6, '0')}`,
      fechaProgramacion: ahora.toISOString().slice(0, 19),
      fechaEstimadaEntrega: entrega.toISOString().slice(0, 19),
      observacion: 'Despacho generado desde el centro de control de pedidos'
    };
    this.enviosService.crearEnvio(body).subscribe({
      next: async () => { await this.showToast('Envío programado correctamente'); void this.router.navigate(['/envios']); },
      error: async (err) => { await this.showToast(err?.error?.message || 'No se pudo programar el envío'); }
    });
  }

  iniciarPreparacion(pedido: any): void {
    this.pedidosService.actualizarEstado(pedido.id, 'EN_PREPARACION').subscribe(() => this.cargarOperacion());
  }

  abrirPlanificacion(pedido: any): void {
    this.pedidoParaEnvio = pedido;
    const transportista = this.transportistas[0];
    const ruta = this.rutas.find(r => Number(r.idTransportista) === Number(transportista?.idTransportista)) || this.rutas[0];
    this.planEnvio = { idTransportista: transportista?.idTransportista || 0, idRuta: ruta?.idRuta || 0, fechaEstimadaEntrega: new Date(Date.now() + 48 * 60 * 60 * 1000).toISOString().slice(0, 16), observacion: 'Entrega coordinada con recepción del cliente' };
  }

  rutasDisponibles(): any[] {
    return this.rutas.filter(r => !this.planEnvio.idTransportista || Number(r.idTransportista) === Number(this.planEnvio.idTransportista));
  }

  confirmarEnvio(): void {
    if (!this.pedidoParaEnvio || !this.planEnvio.idTransportista || !this.planEnvio.idRuta || !this.planEnvio.fechaEstimadaEntrega) return;
    const pedido = this.pedidoParaEnvio;
    const ruta = this.rutas.find(r => Number(r.idRuta) === Number(this.planEnvio.idRuta));
    const body = { idPedido: pedido.id, idTransportista: Number(this.planEnvio.idTransportista), estadoEnvio: { idEstadoEnvio: 1 }, codigoEnvio: `SLX-${String(pedido.id).padStart(6, '0')}`, fechaProgramacion: new Date().toISOString().slice(0, 19), fechaEstimadaEntrega: new Date(this.planEnvio.fechaEstimadaEntrega).toISOString().slice(0, 19), observacion: `${this.planEnvio.observacion} · Ruta: ${ruta?.nombre || ruta?.codigoRuta || 'coordinada'}` };
    this.enviosService.crearEnvio(body).subscribe({
      next: async () => { this.pedidoParaEnvio = null; await this.showToast('Envío programado correctamente'); this.cargarOperacion(); },
      error: async (err) => { await this.showToast(err?.error?.message || 'No se pudo programar el envío'); }
    });
  }

  nombreTransportista(id: number): string { return this.transportistas.find(t => Number(t.idTransportista) === Number(id))?.nombre || 'Sin asignar'; }
  claseEstado(estado: string): string {
    if (estado === 'CREADO' || estado === 'PENDIENTE_PAGO' || estado === 'PAGO_RECHAZADO') return 'pending';
    if (estado === 'ENVIO_PROGRAMADO' || estado === 'DESPACHADO') return 'complete';
    return 'progress';
  }

  goInventario() { this.router.navigate(['/inventario']); }
  irPagos() { this.router.navigate(['/pagos']); }
  goAvisos() { this.router.navigate(['/avisos']); }
  goDashboard() { this.router.navigate(['/dashboard']); }

  private async showToast(msg: string) {
    const t = await this.toastCtrl.create({ message: msg, duration: 3000, position: 'top' });
    await t.present();
  }
}
