import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { Router } from '@angular/router';
import { ApiService } from '../../services/api.service';

interface PrecioProducto {
  precio?: number;
  moneda?: string;
  activo?: boolean;
}

interface ImagenProducto {
  urlImagen?: string;
  url_imagen?: string;
  esPrincipal?: boolean;
  es_principal?: boolean;
  orden?: number;
}

interface Producto {
  idProducto?: number;
  id_producto?: number;
  id?: number;
  codigoSku?: string;
  codigo_sku?: string;
  codigoSKU?: string;
  sku?: string;
  nombre?: string;
  descripcion?: string;
  activo?: boolean;
  estado?: string;
  precioActual?: number;
  precio_actual?: number;
  precio?: number;
  nombreCategoria?: string;
  nombre_categoria?: string;
  nombreMarca?: string;
  nombre_marca?: string;
  precios?: PrecioProducto[];
  imagenes?: ImagenProducto[];
}

@Component({
  selector: 'app-productos',
  standalone: true,
  imports: [CommonModule, FormsModule, IonicModule],
  templateUrl: './productos.page.html',
  styleUrls: ['./productos.page.scss']
})
export class ProductosPage implements OnInit {
  productos: Producto[] = [];
  productosFiltrados: Producto[] = [];

  terminoBusqueda = '';
  loading = false;
  error = '';
  mostrarFormulario = false;
  nuevo: any = { codigoSku: '', nombre: '', descripcion: '', nombreMarca: '', nombreCategoria: '', precioActual: 0 };

  private readonly skusVisibles = [
    'SKU-TV-001',
    'SKU-LMP-001',
    'SKU-SCN-001',
    'SKU-ETQ-001',
    'SKU-RCK-001',
    'SKU-PAL-001'
  ];

  private readonly imagenesPorSku: Record<string, string> = {
    'SKU-TV-001': '/assets/products/monitor-operacional.png',
    'SKU-LMP-001': '/assets/products/lampara-picking.png',
    'SKU-SCN-001': '/assets/products/scanner-zebra.png',
    'SKU-ETQ-001': '/assets/products/impresora-etiquetas.png',
    'SKU-RCK-001': '/assets/products/rack-metalico.png',
    'SKU-PAL-001': '/assets/products/pallet-industrial.png'
  };

  constructor(
    private apiService: ApiService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarProductos();
  }

  cargarProductos(): void {
    this.loading = true;
    this.error = '';

    this.apiService.get<Producto[]>('/catalogo/productos').subscribe({
      next: (data) => {
        const productosRespuesta = Array.isArray(data) ? data : [];

        this.productos = productosRespuesta
          .filter((producto) => this.esProductoVisible(producto));

        this.aplicarFiltro();
        this.loading = false;
      },
      error: (err) => {
        console.error('Error cargando productos:', err);
        this.error = 'No se pudo cargar la información de productos.';
        this.loading = false;
      }
    });
  }

  aplicarFiltro(): void {
    const termino = this.terminoBusqueda.trim().toLowerCase();

    if (!termino) {
      this.productosFiltrados = [...this.productos];
      return;
    }

    this.productosFiltrados = this.productos.filter((producto) => {
      const nombre = producto.nombre?.toLowerCase() || '';
      const sku = this.obtenerSku(producto).toLowerCase();
      const marca = this.obtenerMarca(producto).toLowerCase();
      const categoria = this.obtenerCategoria(producto).toLowerCase();

      return nombre.includes(termino)
        || sku.includes(termino)
        || marca.includes(termino)
        || categoria.includes(termino);
    });
  }

  volverDashboard(): void {
    this.router.navigate(['/dashboard']);
  }

  obtenerId(producto: Producto): number | string {
    return producto.idProducto ?? producto.id_producto ?? producto.id ?? 'Sin ID';
  }

  obtenerSku(producto: Producto): string {
    return producto.codigoSku
      ?? producto.codigo_sku
      ?? producto.codigoSKU
      ?? producto.sku
      ?? 'Sin SKU';
  }

  obtenerMarca(producto: Producto): string {
    return producto.nombreMarca ?? producto.nombre_marca ?? 'No informada';
  }

  obtenerCategoria(producto: Producto): string {
    return producto.nombreCategoria ?? producto.nombre_categoria ?? 'No informada';
  }

  obtenerPrecio(producto: Producto): number {
    const precioActivo = producto.precios?.find((precio) => precio.activo === true);
    const primerPrecio = producto.precios?.[0];

    return Number(
      producto.precioActual
      ?? producto.precio_actual
      ?? producto.precio
      ?? precioActivo?.precio
      ?? primerPrecio?.precio
      ?? 0
    );
  }

  formatearPrecio(producto: Producto): string {
    const precio = this.obtenerPrecio(producto);

    return new Intl.NumberFormat('es-CL', {
      style: 'currency',
      currency: 'CLP',
      maximumFractionDigits: 0
    }).format(precio);
  }

  obtenerImagen(producto: Producto): string {
    const imagenPrincipal = producto.imagenes?.find((imagen) =>
      imagen.esPrincipal === true || imagen.es_principal === true
    );

    const primeraImagen = producto.imagenes?.[0];

    const urlBackend =
      imagenPrincipal?.urlImagen
      ?? imagenPrincipal?.url_imagen
      ?? primeraImagen?.urlImagen
      ?? primeraImagen?.url_imagen;

    if (urlBackend && !urlBackend.includes('cdn.smartlogix.cl')) {
      return urlBackend;
    }

    const sku = this.obtenerSku(producto);
    return this.imagenesPorSku[sku] || '/assets/products/default-product.png';
  }

  obtenerEstado(producto: Producto): string {
    if (producto.estado) {
      return producto.estado;
    }

    return producto.activo === false ? 'INACTIVO' : 'ACTIVO';
  }

  colorEstado(producto: Producto): string {
    const estado = this.obtenerEstado(producto).toUpperCase();

    if (estado === 'ACTIVO' || estado === 'DISPONIBLE') {
      return 'success';
    }

    if (estado === 'INACTIVO' || estado === 'DESCONTINUADO') {
      return 'danger';
    }

    return 'medium';
  }

  private esProductoVisible(producto: Producto): boolean {
    const sku = this.obtenerSku(producto);

    if (!this.skusVisibles.includes(sku)) {
      return false;
    }

    return producto.activo !== false && this.obtenerEstado(producto).toUpperCase() !== 'INACTIVO';
  }
  crearProducto(): void {
    this.apiService.post<Producto>('/catalogo/productos', this.nuevo).subscribe(() => {
      this.skusVisibles.push(this.nuevo.codigoSku);
      this.mostrarFormulario = false; this.nuevo = { codigoSku: '', nombre: '', descripcion: '', nombreMarca: '', nombreCategoria: '', precioActual: 0 };
      this.cargarProductos();
    });
  }
}
