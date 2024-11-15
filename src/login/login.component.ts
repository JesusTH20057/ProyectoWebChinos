// src/app/login/login.component.ts
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule, HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar'; // Optional: For Snackbar notifications

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  standalone: true,
  imports: [FormsModule, CommonModule, HttpClientModule, MatSnackBarModule] // Include HttpClientModule and MatSnackBarModule
})
export class LoginComponent {
  cliente = {
    nombreUsuario: '',
    contrasena: ''
  };

  // Optional: Variables for displaying messages
  errorMessage: string = '';
  successMessage: string = '';

  constructor(
    private http: HttpClient,
    private snackBar: MatSnackBar // Optional: Inject MatSnackBar
  ) {}

  // Método para manejar el envío del formulario
  iniciarSesion(form: FormsModule) {
    // Clear previous messages
    this.errorMessage = '';
    this.successMessage = '';

    // Validate input
    if (!this.cliente.nombreUsuario || !this.cliente.contrasena) {
      this.errorMessage = 'Por favor, ingresa el nombre de usuario y la contraseña.';
      this.showErrorMessage('Por favor, ingresa el nombre de usuario y la contraseña.');
      return;
    }

    // Prepare payload
    const payload = {
      nombreUsuario: this.cliente.nombreUsuario,
      contrasena: this.cliente.contrasena
    };

    // Define the URL to your PHP authentication script
    const url = '/backend/autentication.php'; // **Replace with your actual PHP script URL**

    // Make the POST request
    this.http.post<any>(url, payload).subscribe({
      next: (response) => {
        if (response.success) {
          this.successMessage = response.message;
          this.showSuccessMessage(response.message);
          // Optionally, store user info or token
          // localStorage.setItem('currentUser', JSON.stringify(response.user));
          // Redirect to a protected route if necessary
          // window.location.href = '/dashboard'; // Or use Angular Router for navigation
        } else {
          this.errorMessage = 'Inicio de sesión fallido.';
          this.showErrorMessage('Inicio de sesión fallido.');
        }
      },
      error: (error: HttpErrorResponse) => {
        if (error.status === 0) {
          // A client-side or network error occurred.
          this.errorMessage = 'Ocurrió un error de red. Inténtalo de nuevo.';
          this.showErrorMessage('Ocurrió un error de red. Inténtalo de nuevo.');
        } else {
          // Backend returned an unsuccessful response code.
          this.errorMessage = error.error.error || 'Credenciales inválidas.';
          this.showErrorMessage(error.error.error || 'Credenciales inválidas.');
        }
        console.error('Error:', error);
      }
    });
  }

  // Optional: Methods to display Snackbar messages
  private showSuccessMessage(message: string) {
    this.snackBar.open(message, 'Cerrar', {
      duration: 3000, // Duration in milliseconds
      horizontalPosition: 'right',
      verticalPosition: 'top',
      panelClass: ['snackbar-success'] // Define styles in CSS
    });
  }

  private showErrorMessage(message: string) {
    this.snackBar.open(message, 'Cerrar', {
      duration: 3000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
      panelClass: ['snackbar-error'] // Define styles in CSS
    });
  }
}
