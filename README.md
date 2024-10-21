# Phsyco Bunny

![](https://media1.tenor.com/m/dglU4dQtm-QAAAAC/anime-guitar.gif)

para que funcione las rutas tienes que cambiar esta linea: en dist/web/browser/index.html
  <base href="/browser">


# Proyecto Web - Instrucciones de Docker y PostgreSQL

### Actualizar el archivo de rutas en Angular

Para que las rutas funcionen correctamente en Angular, debes realizar el siguiente cambio:

1. Edita el archivo `dist/web/browser/index.html`.
2. Asegúrate de que no haya errores de rutas comentando o ajustando las rutas incorrectas que puedan causar problemas.

---

### Comandos para manejar Docker

1. **Remover todos los volúmenes y datos inactivos:**

   ```bash
   docker system prune -a --volumes
