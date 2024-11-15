// src/app/CreateAccount/createAccount.component.ts

import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule, HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router'; // Import Router for navigation

@Component({
  selector: 'app-create-account',
  templateUrl: './createAccount.component.html',
  styleUrls: ['./createAccount.component.css'],
  standalone: true,
  imports: [FormsModule, CommonModule, HttpClientModule, MatSnackBarModule]
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
    tipo: 'Envío' // Default value
  };

  errorMessage: string = '';
  successMessage: string = '';

  constructor(
    private http: HttpClient,
    private snackBar: MatSnackBar,
    private router: Router // Inject Router
  ) {}

  onSubmit() {
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.validarFormulario()) {
      this.errorMessage = 'Por favor, completa todos los campos obligatorios.';
      this.showErrorMessage('Por favor, completa todos los campos obligatorios.');
      return;
    }

    const payload = {
      cliente: this.cliente,
      direccion: this.direccion
    };

    const url = '/backend/register.php'; // Replace with your actual PHP script URL

    this.http.post<any>(url, payload).subscribe({
      next: (response) => {
        if (response.success) {
          this.successMessage = response.message;
          this.showSuccessMessage(response.message);
          this.resetForm();

          // Redirect to the login page after successful account creation
          setTimeout(() => this.router.navigate(['/login']), 3000); // 3 seconds delay for user to see the success message
        } else {
          this.errorMessage = 'Error al crear la cuenta.';
          this.showErrorMessage(response.message || 'Error al crear la cuenta.');
        }
      },
      error: (error: HttpErrorResponse) => {
        if (error.status === 0) {
          this.errorMessage = 'Ocurrió un error de red. Inténtalo de nuevo.';
          this.showErrorMessage('Ocurrió un error de red. Inténtalo de nuevo.');
        } else {
          this.errorMessage = error.error.error || 'Error en el registro.';
          this.showErrorMessage(error.error.error || 'Error en el registro.');
        }
        console.error('Error:', error);
      }
    });
  }

  private validarFormulario(): boolean {
    return (
      this.cliente.nombreCompleto.trim() !== '' &&
      this.cliente.email.trim() !== '' &&
      this.cliente.telefono.trim() !== '' &&
      this.cliente.nombreUsuario.trim() !== '' &&
      this.cliente.contrasena.trim() !== '' &&
      this.direccion.calle.trim() !== '' &&
      this.direccion.ciudad.trim() !== '' &&
      this.direccion.estado.trim() !== '' &&
      this.direccion.codigoPostal.trim() !== '' &&
      this.direccion.pais.trim() !== ''
    );
  }

  private resetForm() {
    this.cliente = {
      nombreCompleto: '',
      email: '',
      telefono: '',
      nombreUsuario: '',
      contrasena: ''
    };
    this.direccion = {
      calle: '',
      ciudad: '',
      estado: '',
      codigoPostal: '',
      pais: '',
      tipo: 'Envío'
    };
  }

  private showSuccessMessage(message: string) {
    this.snackBar.open(message, 'Cerrar', {
      duration: 3000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
      panelClass: ['snackbar-success']
    });
  }

  private showErrorMessage(message: string) {
    this.snackBar.open(message, 'Cerrar', {
      duration: 3000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
      panelClass: ['snackbar-error']
    });
  }
}

