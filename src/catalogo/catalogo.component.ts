// src/app/catalogo/catalogo.component.ts
import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { CartService } from '../carrito/cart.service'; 

interface Producto {
  id: number;
  nombre: string;
  precio: number;
  imagen: string;
}

@Component({
  selector: 'app-catalogo',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './catalogo.component.html',
  styleUrls: ['./catalogo.component.css']
})
export class CatalogoComponent implements OnInit {
  productos: Producto[] = [];

  constructor(private http: HttpClient, private cartService: CartService) {} // Inject CartService

  ngOnInit() {
    console.log('CatalogoComponent initialized');
    this.fetchProductos();
  }

  fetchProductos() {
    console.log('Fetching products...');
    this.http.get<any[]>('/backend/productos.php').subscribe(
      data => {
        console.log('Data received:', data);
        this.productos = data.map(item => ({
          id: item.productoid,
          nombre: item.nombreproducto,
          precio: parseFloat(item.precio),
          imagen: item.urlimagen
        }));
      },
      error => {
        console.error('Error fetching products:', error);
      }
    );
  }

  agregarAlCarrito(producto: Producto) {
    this.cartService.addToCart(producto); // Use CartService to add product
  }
}
