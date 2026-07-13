import { Component } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';
import { EnviosService } from '../../services/envios.service';
import { TransportistasService } from '../../services/transportistas.service';
import { RutasService } from '../../services/rutas.service';
import { PedidosService } from '../../services/pedidos.service';
import { ClientesService } from '../../services/clientes.service';
import { ProductosService } from '../../services/productos.service';
import { PagosService } from '../../services/pagos.service';

@Component({ selector: 'app-envios', standalone: true, imports: [IonicModule, CommonModule, RouterLink], templateUrl: './envios.page.html', styleUrls: ['./envios.page.scss'] })
export class EnviosPage {
  loading = false; error = ''; items: any[] = []; transportistas: any[] = []; rutas: any[] = []; pedidos:any[]=[]; clientes:any[]=[]; productos:any[]=[]; pagos:any[]=[];
  constructor(private svc: EnviosService, private transportistasService: TransportistasService, private rutasService: RutasService, private pedidosService:PedidosService, private clientesService:ClientesService, private productosService:ProductosService, private pagosService:PagosService) {}
  ngOnInit(): void { this.load(); }
  load(): void { this.loading = true; this.error = ''; forkJoin({ envios: this.svc.listarEnvios(), transportistas: this.transportistasService.listarTransportistas(), rutas: this.rutasService.listarRutas(), pedidos:this.pedidosService.obtenerPedidos(), clientes:this.clientesService.listarClientes(), productos:this.productosService.listarProductos(), pagos:this.pagosService.listarPagos() }).subscribe({ next: r => { this.items = r.envios || []; this.transportistas = r.transportistas || []; this.rutas = r.rutas || []; this.pedidos=(r.pedidos||[]).slice(0,8);this.clientes=r.clientes||[];this.productos=r.productos||[];this.pagos=r.pagos||[]; this.loading = false; }, error: () => { this.error = 'No se pudo cargar la operación de despachos'; this.loading = false; } }); }
  estado(item: any): string { return item.estadoEnvio?.nombre || 'PROGRAMADO'; }
  etiquetaEstado(item: any): string { return this.estado(item).replace(/_/g, ' '); }
  transportista(id: number): string { return this.transportistas.find(t => Number(t.idTransportista) === Number(id))?.nombre || 'Sin asignar'; }
  ruta(id: number): string { return this.rutas.find(r => Number(r.idRuta) === Number(id))?.nombre || 'Ruta operativa'; }
  siguienteEstado(item: any): string | null { const estado = this.estado(item); return estado === 'PROGRAMADO' ? 'EN_TRANSITO' : estado === 'EN_TRANSITO' ? 'ENTREGADO' : null; }
  etiquetaAccion(item: any): string { return this.estado(item) === 'PROGRAMADO' ? 'Confirmar salida' : 'Confirmar entrega'; }
  avanzar(item: any): void { const estado = this.siguienteEstado(item); if (estado) this.svc.cambiarEstado(item.idEnvio, estado).subscribe(() => this.load()); }
  get enTransito(): number { return this.items.filter(i => this.estado(i) === 'EN_TRANSITO').length; }
  get entregados(): number { return this.items.filter(i => this.estado(i) === 'ENTREGADO').length; }
  cliente(pedido:any):string{return this.clientes.find(c=>Number(c.idCliente??c.id)===Number(pedido.idCliente))?.nombre||'Cliente no disponible';}
  producto(pedido:any):string{return this.productos.find(p=>Number(p.idProducto??p.id)===Number(pedido.detalles?.[0]?.idProducto))?.nombre||'Ítem no disponible';}
  envioPedido(pedido:any):any{return this.items.find(e=>Number(e.idPedido)===Number(pedido.id));}
  pagoPedido(pedido:any):any{return this.pagos.find(p=>Number(p.idPedido)===Number(pedido.id));}
  etapaPedido(pedido:any):string{const envio=this.envioPedido(pedido);if(envio)return this.etiquetaEstado(envio);const estado=pedido.estadoPedido||'';if(estado==='EN_PREPARACION'||estado==='PAGO_APROBADO')return 'LISTO PARA DESPACHO';if(estado==='PENDIENTE_PAGO'||estado==='CREADO')return 'PENDIENTE DE PAGO';return estado.replace(/_/g,' ');}
}
