// src/app/pago/pago.component.ts
import { Component, OnInit, AfterViewInit } from '@angular/core';
import { StripeService } from '../stripe/stripe.service';
import { CartService } from '../carrito/cart.service';
import { environment } from '../environments/environment';
import { Router } from '@angular/router';
import { Stripe, StripeCardElement } from '@stripe/stripe-js';
import { FormsModule } from '@angular/forms'; // Import FormsModule
import { CommonModule } from '@angular/common'; // Import CommonModule
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar'; // Import MatSnackBar
import { ReceiptService } from '../recibo_service/reciboService'; // Import ReceiptService

interface Producto {
  id: number;
  nombre: string;
  precio: number;
  imagen: string;
}

@Component({
  selector: 'app-pago',
  templateUrl: './pago.component.html',
  styleUrls: ['./pago.component.css'],
  standalone: true,
  imports: [FormsModule, CommonModule, MatSnackBarModule]
})
export class PagoComponent implements OnInit, AfterViewInit {
  datosPago = {
    nombre: '',
  };

  cartItems: Producto[] = [];
  total: number = 0;
  isProcessing: boolean = false;
  paymentSuccess: boolean = false;
  paymentError: string = '';

  stripe: Stripe | null = null;
  card: StripeCardElement | null = null;

  constructor(
    private stripeService: StripeService,
    private cartService: CartService,
    private receiptService: ReceiptService, // Inject ReceiptService
    private router: Router,
    private snackBar: MatSnackBar // Inject MatSnackBar
  ) {}

  async ngOnInit(): Promise<void> {
    this.cartService.getCartItems().subscribe(items => {
      this.cartItems = items;
      this.total = items.reduce((acc, item) => acc + item.precio, 0);
    });

    this.stripe = await this.stripeService.getStripe();
  }

  async ngAfterViewInit(): Promise<void> {
    await this.stripeService.initializeElements();
    this.card = this.stripeService.getCardElement();

    if (this.card) {
      this.card.on('change', (event) => {
        const displayError = document.getElementById('card-errors');
        if (event.error) {
          if (displayError) {
            displayError.textContent = event.error.message;
          }
        } else {
          if (displayError) {
            displayError.textContent = '';
          }
        }
      });
    }
  }

  
  async procesarPago(event: Event) {
    event.preventDefault();

    if (!this.stripe || !this.card) {
      return;
    }

    if (this.cartItems.length === 0) {
      this.paymentError = 'El carrito está vacío.';
      return;
    }

    this.isProcessing = true;
    this.paymentError = '';

    // Calcular la cantidad en centavos
    const amount = Math.round(this.total * 100); // Asegurarse de que sea un entero

    try {
      // Paso 1: Crear PaymentIntent en el backend
      const response = await fetch(`${environment.backendUrl}create-payment-intent.php`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          amount: amount,
          currency: 'usd', // Cambia a la moneda que prefieras
        }),
      });

      const paymentIntent = await response.json();

      if (response.status !== 200) {
        throw new Error(paymentIntent.error || 'Error al crear el PaymentIntent.');
      }

      // Paso 2: Confirmar el pago con el cliente
      const result = await this.stripe.confirmCardPayment(paymentIntent.clientSecret, {
        payment_method: {
          card: this.card,
          billing_details: {
            name: this.datosPago.nombre,
          },
        },
      });

      if (result.error) {
        // Mostrar error al usuario
        this.paymentError = result.error.message || 'Error en el pago.';
        this.isProcessing = false;
      } else {
        if (result.paymentIntent.status === 'succeeded') {
          // Pago exitoso
          this.paymentSuccess = true;
          console.log('Pago exitoso:', result.paymentIntent);

          // Map `Producto` to `ReceiptItem`
          const receiptItems = this.cartItems.map(item => ({
            id: item.id,
            nombre: item.nombre,
            precio: item.precio,
            cantidad: 1 || 1,
          }));

          // Generar Recibo
          this.receiptService.generateReceipt(receiptItems, this.total);

          this.snackBar.open('Pago exitoso! Recibo descargado.', 'Cerrar', {
            duration: 3000,
            horizontalPosition: 'right',
            verticalPosition: 'top',
            panelClass: ['snackbar-success'],
          });

          // Limpiar Carrito
          this.cartService.clearCart();

          this.isProcessing = false;
        }
      }
    } catch (error: any) {
      console.error('Error procesando el pago:', error);
      this.paymentError = error.message || 'Ocurrió un error al procesar el pago. Por favor, intenta de nuevo.';
      this.isProcessing = false;
    }
  }


  // Método público para navegar al inicio
  volverAlInicio(): void {
    this.router.navigate(['/inicio']);
  }
}
