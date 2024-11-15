// src/app/CreateAccount/createAccount.component.ts

import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms'; // Import FormsModule
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-create-account',
  templateUrl: './createAccount.component.html',
  styleUrls: ['./createAccount.component.css'],
  standalone: true,
  imports: [FormsModule, CommonModule]
})
export class CreateAccountComponent {
  cliente = {
    nombreCompleto: '',
    email: '',
    telefono: '',
    nombreUsuario: '',
    contrasena: ''
  };

  direccion = {
    calle: '',
    ciudad: '',
    estado: '',
    codigoPostal: '',
    pais: '',
    tipo: 'Envío'
  };

  onSubmit() {
    // Aquí puedes procesar los datos del formulario
    if (
        !this.cliente.nombreCompleto ||
        !this.cliente.email ||
        !this.cliente.telefono ||
        !this.cliente.nombreUsuario ||
        !this.cliente.contrasena ||
        !this.direccion.calle ||
        !this.direccion.ciudad||
        !this.direccion.estado ||
        !this.direccion.codigoPostal ||
        !this.direccion.pais
    ) {
        return;
    }
    console.log('Datos del Cliente:', this.cliente);
    console.log('Dirección:', this.direccion);
  }
}
