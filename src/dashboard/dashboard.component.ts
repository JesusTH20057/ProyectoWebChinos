// src/app/dashboard/dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router'; // Importar el enrutador
import { HttpClient } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CommonModule } from '@angular/common';

interface Producto {
  id: number;
  nombre: string;
  precio: number;
  stock: number;
  imagen: string;
}

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
  standalone: true,
  imports: [CommonModule],
})
export class DashboardComponent implements OnInit {
  productos: Producto[] = [];

  constructor(
    private http: HttpClient,
    private snackBar: MatSnackBar,
    private router: Router // Inyectar el enrutador
  ) {}

  ngOnInit() {
    this.fetchProductos();
  }

  // Obtener productos
  fetchProductos() {
    this.http.get<any[]>('/backend/productsDashboard.php').subscribe(
      data => {
        this.productos = data.map(item => ({
          id: item.productoid,
          nombre: item.nombreproducto,
          precio: parseFloat(item.precio),
          stock: item.stock,
          imagen: item.urlimagen
        }));
      },
      error => {
        console.error('Error fetching products:', error);
      }
    );
  }

  // Ver detalles de un producto
  verProducto(id: number) {
    this.router.navigate(['/producto', id]); // Redirigir a la vista del producto
  }

  // Eliminar un producto
  eliminarProducto(event: Event, id: number) {
    event.stopPropagation(); // Evitar que el evento active el click en la fila
    if (confirm('¿Estás seguro de eliminar este producto?')) {
      this.http.delete(`/backend/deleteProduct.php?id=${id}`).subscribe(
        (response: any) => {
          if (response && response.message) {
            this.showSnackbar(response.message, 'success'); // Mostrar mensaje de éxito
            this.fetchProductos(); // Actualizar la lista de productos
          } else if (response && response.error) {
            this.showSnackbar(response.error, 'error'); // Mostrar error si ocurre
          }
        },
        error => {
          console.error('Error eliminando el producto:', error);
          this.showSnackbar('Ocurrió un error al eliminar el producto', 'error');
        }
      );
    }
  }

  // Agregar un producto
  irAAgregarProducto() {
    this.router.navigate(['/agregar-producto']); // Redirigir a la vista para agregar un producto
  }

  // Mostrar notificaciones
  showSnackbar(message: string, type: 'success' | 'error') {
    this.snackBar.open(message, 'Cerrar', {
      duration: 3000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
      panelClass: [`snackbar-${type}`]
    });
  }
}




