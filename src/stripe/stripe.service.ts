// stripe.service.ts
import { Injectable } from '@angular/core';
import { loadStripe, Stripe } from '@stripe/stripe-js';

@Injectable({
  providedIn: 'root'
})
export class StripeService {
  private stripePromise: Promise<Stripe | null>;

  constructor() {
    this.stripePromise = loadStripe('your_stripe_publishable_key');
  }

  async createCheckoutSession() {
    const response = await fetch('http://localhost:8082/backend/create-checkout-session.php', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      }
    });

    const session = await response.json();

    const stripe = await this.stripePromise;
    if (stripe) {
      const { error } = await stripe.redirectToCheckout({
        sessionId: session.id,
      });
      if (error) {
        console.error(error.message);
      }
    }
  }
}
