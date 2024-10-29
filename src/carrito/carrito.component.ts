// src/app/carrito/carrito.component.ts
import { Component, OnInit } from '@angular/core';
import { CartService } from '../carrito/cart.service'; 
import { Observable } from 'rxjs';
import { CommonModule } from '@angular/common';

interface Producto {
  id: number;
  nombre: string;
  precio: number;
  imagen: string;
}

@Component({
  selector: 'app-carrito',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './carrito.component.html',
  styleUrls: ['./carrito.component.css']
})

export class CarritoComponent implements OnInit {
  cartItems$: Observable<Producto[]>; 
  total: number = 0;

  constructor(private cartService: CartService) {
    this.cartItems$ = this.cartService.getCartItems();
  }

  ngOnInit(): void {
    this.cartItems$.subscribe(items => {
      this.total = items.reduce((acc, item) => acc + item.precio, 0);
    });
  }

  removeFromCart(productId: number): void {
    this.cartService.removeFromCart(productId);
  }

  clearCart(): void {
    this.cartService.clearCart();
  }
}
