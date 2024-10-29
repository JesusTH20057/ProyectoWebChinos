// src/app/services/cart.service.ts
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

interface Producto {
  id: number;
  nombre: string;
  precio: number;
  imagen: string;
}

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private cartItems: Producto[] = [];
  private cartItemsSubject: BehaviorSubject<Producto[]> = new BehaviorSubject<Producto[]>([]);

  constructor() {}

  // Add a product to the cart
  addToCart(product: Producto): void {
    this.cartItems.push(product);
    this.cartItemsSubject.next(this.cartItems);
    console.log(`Added to cart: ${product.nombre}`);
  }

  // Remove a product from the cart by ID
  removeFromCart(productId: number): void {
    this.cartItems = this.cartItems.filter(item => item.id !== productId);
    this.cartItemsSubject.next(this.cartItems);
    console.log(`Removed from cart: Product ID ${productId}`);
  }

  // Get the current cart items as an observable
  getCartItems(): Observable<Producto[]> {
    return this.cartItemsSubject.asObservable();
  }

  // Clear the cart
  clearCart(): void {
    this.cartItems = [];
    this.cartItemsSubject.next(this.cartItems);
    console.log('Cart cleared');
  }
}
