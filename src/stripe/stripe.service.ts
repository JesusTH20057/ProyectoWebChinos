// src/app/services/stripe.service.ts
import { Injectable } from '@angular/core';
import { loadStripe, Stripe, StripeElements, StripeCardElement } from '@stripe/stripe-js';
import { environment } from '../environments/environment'; // Ajusta la ruta según tu estructura

@Injectable({
  providedIn: 'root'
})
export class StripeService {
  private stripePromise: Promise<Stripe | null>;
  private elements: StripeElements | null = null;
  private card: StripeCardElement | null = null;

  constructor() {
    this.stripePromise = loadStripe(environment.stripePublishableKey);
  }

  async initializeElements(): Promise<void> {
    const stripe = await this.stripePromise;
    if (!stripe) {
      throw new Error('Stripe failed to initialize.');
    }

    this.elements = stripe.elements();
    this.card = this.elements.create('card', {
      style: {
        base: {
          fontSize: '16px',
          color: '#424770',
          '::placeholder': {
            color: '#aab7c4',
          },
        },
        invalid: {
          color: '#9e2146',
        },
      },
    });

    this.card.mount('#card-element');
  }

  getStripe(): Promise<Stripe | null> {
    return this.stripePromise;
  }

  getCardElement(): StripeCardElement | null {
    return this.card;
  }
}
