import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; // Import FormsModule

@Component({
  selector: 'app-modificar-producto',
  templateUrl: './modificar-producto.component.html',
  styleUrls: ['./modificar-producto.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule],
})
export class ModificarProductoComponent implements OnInit {
  producto: any = null; // Producto a modificar
  categorias: any[] = [];
  marcas: any[] = [];

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id'); // Obtener el ID del producto desde la URL

    if (id) {
      // Cargar categorías y marcas primero, luego el producto
      this.fetchCategorias(() => {
        this.fetchMarcas(() => {
          this.cargarProducto(+id);
        });
      });
    }
  }

  // Método para cargar categorías
fetchCategorias(callback?: () => void) {
  this.http.get('/backend/getCategorias.php').subscribe(
    (data: any) => {
      this.categorias = data;
      if (callback) callback();
    },
    error => {
      console.error('Error al cargar categorías:', error);
    }
  );
}

// Método para cargar marcas
fetchMarcas(callback?: () => void) {
  this.http.get('/backend/getMarcas.php').subscribe(
    (data: any) => {
      this.marcas = data;
      if (callback) callback();
    },
    error => {
      console.error('Error al cargar marcas:', error);
    }
  );
}

// Método para cargar los datos del producto
cargarProducto(id: number) {
  this.http.get(`/backend/getProducto.php?id=${id}`).subscribe(
    (data: any) => {
      this.producto = data;
      console.log('Producto cargado:', this.producto);
    },
    error => {
      console.error('Error al cargar el producto:', error);
    }
  );
}

  // Guardar los cambios del producto
  guardarCambios() {
    this.http.post('/backend/updateProduct.php', this.producto).subscribe(
      (response: any) => {
        alert('Producto modificado con éxito.');
        this.router.navigate(['/dashboard']);
      },
      error => {
        console.error('Error al modificar el producto:', error);
        alert('No se pudieron guardar los cambios.');
      }
    );
  }
}
