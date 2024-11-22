import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms'; // Import FormsModule
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-agregar-producto',
  templateUrl: './agregar-producto.component.html',
  styleUrls: ['./agregar-producto.component.css'],
  standalone: true,
  imports: [FormsModule, CommonModule]
})
export class AgregarProductoComponent implements OnInit {
  producto: any = {
    nombre: '',
    descripcion: '',
    precio: null,
    stock: null,
    imagen: '',
    categoriaid: null,
    marca: '', // Campo para la marca como texto
    materiales: '',
    peso: null,
    altura: null,
    ancho: null,
    profundidad: null
  };

  categorias: any[] = [];
  marcas: any[] = [];

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit() {
    this.fetchCategorias();
    this.fetchMarcas();
  }

  // Obtener categorías
  fetchCategorias() {
    this.http.get<any[]>('/backend/getCategorias.php').subscribe(
      data => {
        this.categorias = data.map(item => ({
          id: item.id, // Asume que el backend devuelve `id` para CategoriaID
          nombre: item.nombre // Asume que el backend devuelve `nombre` para NombreCategoria
        }));
      },
      error => {
        console.error('Error al obtener categorías:', error);
        alert('Error al obtener las categorías. Verifica la conexión.');
      }
    );
  }

    // Obtener marcas desde el backend
    fetchMarcas() {
      this.http.get<any[]>('/backend/getMarcas.php').subscribe(
        data => {
          this.marcas = data.map(item => ({
            id: item.id,
            nombre: item.nombre
          }));
        },
        error => {
          console.error('Error al obtener marcas:', error);
        }
      );
    }


  agregarProducto() {
    if (
      !this.producto.nombre ||
      !this.producto.precio ||
      !this.producto.stock ||
      !this.producto.imagen ||
      !this.producto.categoriaid ||
      !this.producto.marcaid
    ) {
      alert('Por favor completa todos los campos obligatorios');
      return;
    }

    this.http.post('/backend/addProduct.php', this.producto).subscribe(
      (response: any) => {
        if (response && response.message) {
          alert(response.message);
          this.router.navigate(['/dashboard']);
        } else if (response && response.error) {
          alert(response.error);
        }
      },
      error => {
        console.error('Error al agregar el producto:', error);
        alert('Error al agregar el producto');
      }
    );
  }
}

