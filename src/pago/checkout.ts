// checkout.component.ts
import { Component } from '@angular/core';
import { StripeService } from './stripe.service';

@Component({
  selector: 'app-checkout',
  template: `<button (click)="checkout()">Checkout</button>`
})
export class CheckoutComponent {
  constructor(private stripeService: StripeService) {}

  checkout() {
    this.stripeService.createCheckoutSession();
  }
}
