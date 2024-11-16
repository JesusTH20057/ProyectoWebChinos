import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';  // Importa ReactiveFormsModule

@Component({
  selector: 'app-recuperarContrasena',  // Cambiado a 'recuperarContrasena'
  templateUrl: './recuperarContrasena.component.html',  // Cambiado el nombre del archivo
  styleUrls: ['./recuperarContrasena.component.css'],  // Cambiado el nombre del archivo
  standalone: true,
  imports: [ReactiveFormsModule], 
})
export class RecuperarContrasenaComponent {  // Cambiado el nombre de la claseS
  recoveryForm: FormGroup;
  updatePasswordForm: FormGroup;
  step: 'request' | 'update' = 'request'; // Control de las etapas
  token: string | null = null; // Token recibido del servidor

  constructor(private fb: FormBuilder, private http: HttpClient) {
    this.recoveryForm = this.fb.group({
      nombreUsuario: ['', [Validators.required]],
      correo: ['', [Validators.required, Validators.email]],
    });

    this.updatePasswordForm = this.fb.group({
      nuevaContrasena: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  onRequestToken() {
    if (this.recoveryForm.valid) {
      this.http.post('/backend/recover_password.php', this.recoveryForm.value).subscribe({
        next: (response: any) => {
          alert(response.message);
          this.token = response.token; // Guardar el token para el siguiente paso (simulado)
          this.step = 'update'; // Pasar a la etapa de cambio de contraseña
        },
        error: (err) => {
          alert(err.error.error || 'Error al solicitar el token.');
        },
      });
    }
  }

  onUpdatePassword() {
    if (this.updatePasswordForm.valid && this.token) {
      const payload = {
        token: this.token,
        nuevaContrasena: this.updatePasswordForm.value.nuevaContrasena,
      };

      this.http.post('/backend/update_password.php', payload).subscribe({
        next: (response: any) => {
          alert(response.message);
          this.step = 'request'; // Volver al formulario inicial
          this.recoveryForm.reset();
          this.updatePasswordForm.reset();
          this.token = null;
        },
        error: (err) => {
          alert(err.error.error || 'Error al actualizar la contraseña.');
        },
      });
    }
  }
}


