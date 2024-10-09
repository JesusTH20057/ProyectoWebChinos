// catalogo.component.ts
import { Component } from '@angular/core';

interface Producto {
  id: number;
  nombre: string;
  descripcion: string;
  precio: number;
  imagen: string;
}

@Component({
  selector: 'app-catalogo',
  templateUrl: './catalogo.component.html',
  styleUrls: ['./catalogo.component.css']
})
export class CatalogoComponent {

    

  productos: Producto[] = [
  ];

  agregarAlCarrito(producto: Producto) {
    console.log(`Producto agregado: ${producto.nombre}`);
  }
}
