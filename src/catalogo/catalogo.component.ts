// catalogo.component.ts
import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

interface Producto {
  id: number;
  nombre: string;
  precio: number;
  imagen: string;
}

@Component({
  selector: 'app-catalogo',
  standalone: true,
  imports: [CommonModule], // No need to import HttpClientModule
  templateUrl: './catalogo.component.html',
  styleUrls: ['./catalogo.component.css']
})
export class CatalogoComponent implements OnInit {
  productos: Producto[] = []; // Define the productos property

  constructor(private http: HttpClient) {}

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
    console.log(`Producto agregado: ${producto.nombre}`);
  }
}
