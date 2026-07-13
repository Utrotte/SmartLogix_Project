import { Component } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { TransportistasService } from '../../services/transportistas.service';
@Component({ selector: 'app-transportistas', standalone: true, imports: [IonicModule, CommonModule, FormsModule, RouterLink], templateUrl: './transportistas.page.html', styleUrls: ['./transportistas.page.scss'] })
export class TransportistasPage {
  loading=false; error=''; items:any[]=[]; mostrarFormulario=false;
  nuevo:any={nombre:'',rut:'',correo:'',telefono:'',tipoVehiculo:'Furgón',capacidadKg:3500,entregasHoy:0,puntualidad:100};
  constructor(private svc:TransportistasService){}
  ngOnInit():void{this.load();}
  load():void{this.loading=true;this.svc.listarTransportistas().subscribe({next:r=>{this.items=r||[];this.loading=false},error:()=>{this.error='No se pudo cargar la flota';this.loading=false}});}
  crear():void{this.svc.crear(this.nuevo).subscribe(()=>{this.mostrarFormulario=false;this.nuevo={nombre:'',rut:'',correo:'',telefono:'',tipoVehiculo:'Furgón',capacidadKg:3500,entregasHoy:0,puntualidad:100};this.load();});}
}
