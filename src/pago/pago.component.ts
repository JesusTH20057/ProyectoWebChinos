// pago.component.ts
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-pago',
  templateUrl: './pago.component.html',
  styleUrls: ['./pago.component.css'],
  standalone: true,
  imports: [FormsModule]
})
export class PagoComponent {
  datosPago = {
    nombre: '',
    numero: '',
    expiracion: '',
    cvv: ''
  };

  procesarPago() {
    // Implement your payment processing logic here
    console.log('Procesando pago:', this.datosPago);

    // Example: Send the payment data to a backend service
    // this.paymentService.procesarPago(this.datosPago).subscribe(
    //   response => {
    //     // Handle successful payment
    //   },
    //   error => {
    //     // Handle payment error
    //   }
    // );
  }
}
