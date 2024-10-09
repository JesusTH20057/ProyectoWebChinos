// app.routes.ts
import { Routes } from '@angular/router';
import { InicioComponent } from '../inicio/inicio.component';
import { CatalogoComponent } from '../catalogo/catalogo.component';
import { NosotrosComponent } from '../nosotros/nosotros.component';
import { PagoComponent } from '../pago/pago.component';
import { TerminosComponent } from '../terminos/terminos.component';

export const routes: Routes = [
  { path: '', component: InicioComponent },
  { path: 'inicio', component: InicioComponent },
  { path: 'catalogo', component: CatalogoComponent },
  { path: 'nosotros', component: NosotrosComponent },
  { path: 'pago', component: PagoComponent },
  { path: 'terminos', component: TerminosComponent },
  { path: '**', redirectTo: '' } // Redirect unknown routes to home
];
