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
import { DashboardComponent } from '../dashboard/dashboard.component';
import { ProductoComponent } from '../producto/producto.component';
import { AgregarProductoComponent } from '../agregar-producto/agregar-producto.component';
import { ModificarProductoComponent } from '../modificar-producto/modificar-producto.component';


export const routes: Routes = [
  { path: '', component: InicioComponent },
  { path: 'inicio', component: InicioComponent },
  { path: 'catalogo', component: CatalogoComponent },
  { path: 'nosotros', component: NosotrosComponent },
  { path: 'pago', component: PagoComponent },
  { path: 'terminos', component: TerminosComponent },
  { path: 'carrito', component: CarritoComponent },
  { path: 'createAccount', component: CreateAccountComponent},
  { path: 'login', component: LoginComponent},  
  { path: 'recuperarContrasena', component: RecuperarContrasenaComponent},
  { path: 'dashboard', component: DashboardComponent /*, canActivate: [AdminGuard] */ },
  { path: 'producto/:id', component: ProductoComponent },
  { path: 'agregar-producto', component: AgregarProductoComponent },
  { path: 'modificar-producto/:id', component: ModificarProductoComponent},

  { path: '**', redirectTo: '' } // Redirect unknown routes to home
];