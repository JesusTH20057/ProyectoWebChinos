# Phsyco Bunny

![](https://media1.tenor.com/m/dglU4dQtm-QAAAAC/anime-guitar.gif)

para que funcione las rutas tienes que cambiar esta linea: en dist/web/browser/index.html
  <base href="/browser">


remover todos los volumenes:
docker system prune -a --volumes

remover contenedor:
docker-compose down

mostrar volumenes:
docker volume ls   

remover docker volume
docker volume rm proyectowebchinos_pgdata

postgres:
psql -U admin -d gymstore

mostrar tablas 
\dt

env creds in docker composer
