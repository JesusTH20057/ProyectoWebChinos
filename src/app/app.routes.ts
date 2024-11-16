// app.routes.ts
import { RouterModule, Routes } from '@angular/router';
import { InicioComponent } from '../inicio/inicio.component';
import { CatalogoComponent } from '../catalogo/catalogo.component';
import { NosotrosComponent } from '../nosotros/nosotros.component';
import { PagoComponent } from '../pago/pago.component';
import { TerminosComponent } from '../terminos/terminos.component';
import { CarritoComponent } from '../carrito/carrito.component'; // Import CarritoComponent
import {CreateAccountComponent} from '../createAccount/createAccount.component';
import {LoginComponent} from '../login/login.component';
import {RecuperarContrasenaComponent} from '../recuperarContrasena/recuperarContrasena.component';

export const routes: Routes = [
  { path: '', component: InicioComponent },
  { path: 'inicio', component: InicioComponent },
  { path: 'catalogo', component: CatalogoComponent },
  { path: 'nosotros', component: NosotrosComponent },
  { path: 'pago', component: PagoComponent },
  { path: 'terminos', component: TerminosComponent },
  { path: 'carrito', component: CarritoComponent },
  {path: 'createAccount', component: CreateAccountComponent},
  {path: 'login', component: LoginComponent},  
  {path: 'recuperarContrasena', component: RecuperarContrasenaComponent},

  { path: '**', redirectTo: '' } // Redirect unknown routes to home
];