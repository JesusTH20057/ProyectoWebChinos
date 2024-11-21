// src/app/login/login.component.ts
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule, HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar'; // Optional: For Snackbar notifications
import { Router } from '@angular/router'; // Import Router for navigation

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  standalone: true,
  imports: [FormsModule, CommonModule, HttpClientModule, MatSnackBarModule] // Include necessary modules
})
export class LoginComponent {
  cliente = {
    nombreUsuario: '',
    contrasena: ''
  };

  // Variables for displaying messages
  errorMessage: string = '';
  successMessage: string = '';

  constructor(
    private http: HttpClient,
    private snackBar: MatSnackBar,
    private router: Router // Inject Router
  ) {}

  // Method to handle form submission
  iniciarSesion() { // No parameters
    // Clear previous messages
    this.router.navigate(['/recover-password']);
    this.errorMessage = '';
    this.successMessage = '';

    // Validate input
    if (!this.cliente.nombreUsuario || !this.cliente.contrasena) {
      this.errorMessage = 'Por favor, ingresa el nombre de usuario y la contraseña.';
      this.showErrorMessage(this.errorMessage);
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
          this.showSuccessMessage(this.successMessage);

          // Store user information in localStorage (optional)
          localStorage.setItem('currentUser', JSON.stringify(response.user));

          // Check if user is admin and redirect to /dashboard
          if (response.user.role === 'admin') {
            this.router.navigate(['/dashboard']);
          } else {
            // Optionally, handle other user roles
            this.router.navigate(['/home']); // Replace '/home' with your desired route
          }
        } else {
          this.errorMessage = 'Inicio de sesión fallido.';
          this.showErrorMessage(this.errorMessage);
        }
      },
      error: (error: HttpErrorResponse) => {
        if (error.status === 0) {
          // A client-side or network error occurred.
          this.errorMessage = 'Ocurrió un error de red. Inténtalo de nuevo.';
          this.showErrorMessage(this.errorMessage);
        } else {
          // Backend returned an unsuccessful response code.
          this.errorMessage = error.error.error || 'Credenciales inválidas.';
          this.showErrorMessage(this.errorMessage);
        }
        console.error('Error:', error);
      }
    });
  }

  // Methods to display Snackbar messages
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
