import { Component } from '@angular/core';
import { Router, NavigationEnd, RouterLink, RouterLinkActive } from '@angular/router';
import { NgFor, NgIf } from '@angular/common';
import { IonApp, IonRouterOutlet, IonIcon } from '@ionic/angular/standalone';
import { filter } from 'rxjs/operators';
import { AuthService } from './services/auth.service';
import { AvisosService } from './services/avisos.service';
import { addIcons } from 'ionicons';
import {
  gridOutline,
  cartOutline,
  peopleOutline,
  pricetagOutline,
  cubeOutline,
  cardOutline,
  busOutline,
  carOutline,
  gitBranchOutline,
  businessOutline,
  notificationsOutline,
  menuOutline,
  searchOutline,
  helpCircleOutline,
  chevronDownOutline,
  chevronBackOutline,
  chevronForwardOutline,
  clipboardOutline,
  calendarOutline,
  warningOutline,
  logOutOutline
} from 'ionicons/icons';

interface MenuItem {
  label: string;
  icon: string;
  route: string;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    IonApp,
    IonRouterOutlet,
    IonIcon,
    RouterLink,
    RouterLinkActive,
    NgFor,
    NgIf
  ],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  currentUrl = '';
  avisosPendientes = 0;

  menuItems: MenuItem[] = [
    { label: 'Panel principal', icon: 'grid-outline', route: '/dashboard' },
    { label: 'Pedidos', icon: 'cart-outline', route: '/pedidos' },
    { label: 'Clientes', icon: 'people-outline', route: '/clientes' },
    { label: 'Catálogo', icon: 'pricetag-outline', route: '/productos' },
    { label: 'Inventario', icon: 'cube-outline', route: '/inventario' },
    { label: 'Pagos', icon: 'card-outline', route: '/pagos' },
    { label: 'Envíos', icon: 'bus-outline', route: '/envios' },
    { label: 'Transportistas', icon: 'car-outline', route: '/transportistas' },
    { label: 'Rutas', icon: 'git-branch-outline', route: '/rutas' },
    { label: 'Bodegas', icon: 'business-outline', route: '/bodegas' },
    { label: 'Avisos', icon: 'notifications-outline', route: '/avisos' },
  ];

  constructor(private router: Router, private authService: AuthService, private avisosService: AvisosService) {
    this.avisosService.pendientes$.subscribe(total => this.avisosPendientes = total);
    addIcons({
      'grid-outline': gridOutline,
      'cart-outline': cartOutline,
      'people-outline': peopleOutline,
      'pricetag-outline': pricetagOutline,
      'cube-outline': cubeOutline,
      'card-outline': cardOutline,
      'bus-outline': busOutline,
      'car-outline': carOutline,
      'git-branch-outline': gitBranchOutline,
      'business-outline': businessOutline,
      'notifications-outline': notificationsOutline,
      'menu-outline': menuOutline,
      'search-outline': searchOutline,
      'help-circle-outline': helpCircleOutline,
      'chevron-down-outline': chevronDownOutline,
      'chevron-back-outline': chevronBackOutline,
      'chevron-forward-outline': chevronForwardOutline,
      'clipboard-outline': clipboardOutline,
      'calendar-outline': calendarOutline,
      'warning-outline': warningOutline,
      'log-out-outline': logOutOutline
    });
    this.currentUrl = this.router.url;

    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: any) => {
        this.currentUrl = event.urlAfterRedirects;
        if (!this.currentUrl.includes('/login')) this.actualizarAvisos();
      });
  }

  get isLoginPage(): boolean {
    return this.currentUrl.includes('/login');
  }

  sidebarCollapsed = window.matchMedia('(max-width: 760px)').matches;

  toggleSidebar(): void {
    this.sidebarCollapsed = !this.sidebarCollapsed;
  }

  closeMobileSidebar(): void {
    if (window.matchMedia('(max-width: 760px)').matches) this.sidebarCollapsed = true;
  }

  get usuario(): any { return this.authService.obtenerUsuario(); }

  get nombreUsuario(): string {
    return this.usuario?.nombre || this.usuario?.correo || 'Usuario';
  }

  get rolUsuario(): string {
    const roles = this.usuario?.roles || this.usuario?.nombresRoles;
    return Array.isArray(roles) && roles.length ? roles.join(', ') : 'Operador';
  }

  get inicialesUsuario(): string {
    return this.nombreUsuario.trim().split(/\s+/).slice(0, 2)
      .map((parte: string) => parte.charAt(0).toUpperCase()).join('') || 'U';
  }

  cerrarSesion(): void {
    this.authService.cerrarSesion();
    void this.router.navigateByUrl('/login', { replaceUrl: true });
  }

  actualizarAvisos(): void {
    this.avisosService.listarAvisos().subscribe({ next: avisos => this.avisosPendientes = (avisos || []).filter(a => a.estado_aviso !== 'LEIDO').length, error: () => this.avisosPendientes = 0 });
  }
}
