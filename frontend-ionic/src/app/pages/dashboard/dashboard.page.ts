import { Component, OnInit } from '@angular/core';
import { NgFor, NgIf } from '@angular/common';
import { IonIcon } from '@ionic/angular/standalone';
import { RouterLink } from '@angular/router';
import { DashboardService } from '../../services/dashboard.service';

interface KpiCard {
  title: string;
  value: string;
  trend: string;
  trendType: 'positive' | 'negative';
  icon: string;
  color: 'blue' | 'green' | 'yellow' | 'red';
}

interface PedidoReciente {
  id: string;
  cliente: string;
  fecha: string;
  total: string;
  estado: string;
  badge: string;
  item: string;
  sku: string;
}

interface AlertaOperativa {
  titulo: string;
  descripcion: string;
  fecha: string;
  tipo: string;
  badge: string;
  icon: string;
  contexto: string;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [NgFor, NgIf, IonIcon, RouterLink],
  templateUrl: './dashboard.page.html',
  styleUrls: ['./dashboard.page.scss'],
})
export class DashboardPage implements OnInit {
  totalEnvios = 0;
  enviosProgramados = 0;
  enviosTransito = 0;
  enviosEntregados = 0;
  kpis: KpiCard[] = [
    {
      title: 'Pedidos de hoy',
      value: '24',
      trend: '↑ 9%',
      trendType: 'positive',
      icon: 'cart-outline',
      color: 'blue',
    },
    {
      title: 'Pagos pendientes',
      value: '8',
      trend: '↑ 14%',
      trendType: 'negative',
      icon: 'card-outline',
      color: 'green',
    },
    {
      title: 'Envíos en tránsito',
      value: '13',
      trend: '↑ 8%',
      trendType: 'positive',
      icon: 'bus-outline',
      color: 'blue',
    },
    {
      title: 'Stock crítico',
      value: '5',
      trend: '↑ 25%',
      trendType: 'negative',
      icon: 'warning-outline',
      color: 'yellow',
    },
  ];

  pedidosRecientes: PedidoReciente[] = [
    {
      id: '#PED-10485',
      cliente: 'Comercial Andina S.A.',
      fecha: '24/05/2025 10:15',
      total: '$ 1.250.000',
      estado: 'Confirmado',
      badge: 'blue',
      item: 'Producto', sku: '',
    },
    {
      id: '#PED-10484',
      cliente: 'Distribuciones Norte',
      fecha: '24/05/2025 09:42',
      total: '$ 980.500',
      estado: 'En preparación',
      badge: 'yellow',
      item: 'Producto', sku: '',
    },
    {
      id: '#PED-10483',
      cliente: 'Global Retail Ltda.',
      fecha: '24/05/2025 09:10',
      total: '$ 2.315.750',
      estado: 'Pendiente',
      badge: 'gray',
      item: 'Producto', sku: '',
    },
    {
      id: '#PED-10482',
      cliente: 'Mercados del Sur',
      fecha: '23/05/2025 16:55',
      total: '$ 750.240',
      estado: 'Enviado',
      badge: 'green',
      item: 'Producto', sku: '',
    },
    {
      id: '#PED-10481',
      cliente: 'Inversiones Patagonia',
      fecha: '23/05/2025 15:23',
      total: '$ 1.420.000',
      estado: 'Entregado',
      badge: 'green',
      item: 'Producto', sku: '',
    },
  ];

  alertas: AlertaOperativa[] = [
    {
      titulo: 'Producto con bajo stock',
      descripcion: 'El producto “Parlante Bluetooth X9” tiene stock crítico (3 unidades).',
      fecha: 'Hoy, 10:32',
      tipo: 'Advertencia',
      badge: 'yellow',
      icon: 'cube-outline',
      contexto: '',
    },
    {
      titulo: 'Pago rechazado',
      descripcion: 'El pago del pedido #PED-10480 por $850.000 fue rechazado.',
      fecha: 'Hoy, 09:47',
      tipo: 'Crítico',
      badge: 'red',
      icon: 'card-outline',
      contexto: '',
    },
    {
      titulo: 'Transportista sin cupos disponibles',
      descripcion: 'El transportista “Logística Express” no tiene cupos disponibles para hoy.',
      fecha: 'Ayer, 16:20',
      tipo: 'Información',
      badge: 'purple',
      icon: 'bus-outline',
      contexto: '',
    },
  ];

  constructor(private dashboardService: DashboardService) {}

  ngOnInit(): void {
    this.dashboardService.obtenerResumen().subscribe(resumen => {
      this.kpis = [
        { title: 'Pedidos activos', value: String(resumen.totalPedidos), trend: 'Operación sincronizada', trendType: 'positive', icon: 'cart-outline', color: 'blue' },
        { title: 'Pagos registrados', value: String(resumen.totalPagos), trend: 'Control financiero', trendType: 'positive', icon: 'card-outline', color: 'green' },
        { title: 'Envíos operativos', value: String(resumen.totalEnvios), trend: 'Trazabilidad activa', trendType: 'positive', icon: 'bus-outline', color: 'blue' },
        { title: 'Stock disponible', value: String(resumen.stockDisponibleTotal), trend: `${resumen.stockReservadoTotal} reservadas`, trendType: 'negative', icon: 'warning-outline', color: 'yellow' }
      ];
      const clientes = resumen.clientes || []; const productos = resumen.productos || [];
      const clienteNombre = (id: number) => clientes.find((c: any) => Number(c.idCliente ?? c.id) === Number(id))?.nombre || 'Cliente no disponible';
      const productoDePedido = (p: any) => productos.find((producto: any) => Number(producto.idProducto ?? producto.id) === Number(p.detalles?.[0]?.idProducto));
      this.pedidosRecientes = (resumen.pedidos || []).slice(0, 5).map((p: any) => { const producto = productoDePedido(p); return { id: `#SLX-${p.id}`, cliente: clienteNombre(p.idCliente), fecha: new Date(p.fechaCreacion).toLocaleString('es-CL'), total: new Intl.NumberFormat('es-CL', { style: 'currency', currency: 'CLP', maximumFractionDigits: 0 }).format(p.total), estado: p.estadoPedido, badge: ['DESPACHADO','ENTREGADO','ENVIO_PROGRAMADO'].includes(p.estadoPedido) ? 'green' : 'blue', item: producto?.nombre || 'Ítem no disponible', sku: producto?.codigoSku || '' }; });
      const envios = resumen.envios || []; this.totalEnvios = envios.length;
      this.enviosProgramados = envios.filter((e: any) => e.estadoEnvio?.nombre === 'PROGRAMADO').length;
      this.enviosTransito = envios.filter((e: any) => e.estadoEnvio?.nombre === 'EN_TRANSITO').length;
      this.enviosEntregados = envios.filter((e: any) => e.estadoEnvio?.nombre === 'ENTREGADO').length;
      this.alertas = (resumen.avisos || []).slice(0, 4).map((a: any) => { const pedido = (resumen.pedidos || []).find((p: any) => Number(p.id) === Number(a.idPedido)); const producto = a.productoNombre || productoDePedido(pedido || {})?.nombre; const cliente = a.clienteNombre || (pedido ? clienteNombre(pedido.idCliente) : ''); return { titulo: a.asunto, descripcion: a.mensaje, fecha: new Date(a.fecha_creacion).toLocaleString('es-CL'), tipo: a.estado_aviso, badge: a.tipo_aviso === 'STOCK' ? 'yellow' : 'purple', icon: a.tipo_aviso === 'STOCK' ? 'cube-outline' : 'notifications-outline', contexto: [cliente, producto].filter(Boolean).join(' · ') }; });
    });
  }
}
