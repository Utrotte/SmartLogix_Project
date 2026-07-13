import { Component } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { CommonModule } from '@angular/common';
import { AvisosService } from '../../services/avisos.service';

@Component({
  selector: 'app-avisos',
  standalone: true,
  imports: [IonicModule, CommonModule],
  templateUrl: './avisos.page.html',
  styleUrls: ['./avisos.page.scss']
})
export class AvisosPage {
  loading = false;
  error = '';
  avisos: any[] = [];

  constructor(private avisosService: AvisosService) {}

  ngOnInit(): void { this.load(); }

  load() {
    this.loading = true; this.error = '';
    this.avisosService.listarAvisos().subscribe({
      next: (r) => { this.avisos = r || []; this.loading = false; },
      error: () => { this.error = 'No se pudo cargar la información'; this.loading = false; }
    });
  }
  marcarLeido(aviso: any): void { this.avisosService.marcarLeido(aviso.id).subscribe(() => this.load()); }
  get pendientes(): number { return this.avisos.filter(a => a.estado_aviso !== 'LEIDO').length; }
}
