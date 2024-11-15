// src/app/login/login.component.ts
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms'; // Import FormsModule
import { CommonModule } from '@angular/common'

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  standalone: true,
  imports: [FormsModule, CommonModule]
})
export class LoginComponent {
  cliente = {
    nombreUsuario: '',
    contrasena: ''
  };

  // Método para manejar el envío del formulario
  iniciarSesion(form: FormsModule) {
    if (
        !this.cliente.nombreUsuario ||
        !this.cliente.contrasena
    ) {
        return;
    }

  }
}
