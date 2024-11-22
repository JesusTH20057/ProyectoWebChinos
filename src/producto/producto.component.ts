import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-producto',
  templateUrl: './producto.component.html',
  styleUrls: ['./producto.component.css'],
  standalone: true,
  imports: [CommonModule],
})
export class ProductoComponent implements OnInit {
  producto: any = null; // Información del producto

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id'); // Obtener el ID del producto
    if (id) {
      this.cargarProducto(+id);
    }
  }

  // Cargar producto desde el backend
  cargarProducto(id: number) {
    this.http.get(`/backend/getProducto.php?id=${id}`).subscribe(
      (data: any) => {
        this.producto = data; // Asignar los datos del producto
      },
      error => {
        console.error('Error al cargar el producto:', error);
        alert('No se pudo cargar el producto.');
      }
    );
  }

  // Método para regresar al dashboard
  regresarAlDashboard() {
    this.router.navigate(['/dashboard']);
  }

  // Método para ir a la vista de modificar el producto
  irAModificarProducto() {
    this.router.navigate(['/modificar-producto', this.producto.productoid]);
  }
}
