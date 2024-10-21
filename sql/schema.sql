-- Crear la extensión para manejar UUIDs si es necesario (opcional)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Tabla Clientes
CREATE TABLE Clientes (
    ClienteID SERIAL PRIMARY KEY,
    NombreCompleto VARCHAR(255) NOT NULL,
    Email VARCHAR(255) NOT NULL UNIQUE,
    Telefono VARCHAR(20),
    NombreUsuario VARCHAR(100) NOT NULL UNIQUE,
    Contrasena VARCHAR(255) NOT NULL
);

-- Tabla Direcciones
CREATE TABLE Direcciones (
    DireccionID SERIAL PRIMARY KEY,
    ClienteID INT NOT NULL,
    Calle VARCHAR(255) NOT NULL,
    Ciudad VARCHAR(100) NOT NULL,
    Estado VARCHAR(100),
    CodigoPostal VARCHAR(20),
    Pais VARCHAR(100) NOT NULL,
    Tipo VARCHAR(50), -- Por ejemplo, 'Envío', 'Facturación'
    FOREIGN KEY (ClienteID) REFERENCES Clientes(ClienteID) ON DELETE CASCADE
);

-- Tabla CuentasPago
CREATE TABLE CuentasPago (
    CuentaPagoID SERIAL PRIMARY KEY,
    ClienteID INT NOT NULL,
    TipoCuenta VARCHAR(50) NOT NULL, -- Por ejemplo, 'PayPal', 'Tarjeta de Crédito'
    DetalleCuenta VARCHAR(255) NOT NULL,
    FOREIGN KEY (ClienteID) REFERENCES Clientes(ClienteID) ON DELETE CASCADE
);

-- Tabla Categorias
CREATE TABLE Categorias (
    CategoriaID SERIAL PRIMARY KEY,
    NombreCategoria VARCHAR(100) NOT NULL UNIQUE
);

-- Tabla Marcas
CREATE TABLE Marcas (
    MarcaID SERIAL PRIMARY KEY,
    NombreMarca VARCHAR(100) NOT NULL UNIQUE
);

-- Tabla Proveedores
CREATE TABLE Proveedores (
    ProveedorID SERIAL PRIMARY KEY,
    NombreProveedor VARCHAR(255) NOT NULL UNIQUE,
    Contacto VARCHAR(255)
);

-- Tabla Productos
CREATE TABLE Productos (
    ProductoID SERIAL PRIMARY KEY,
    NombreProducto VARCHAR(255) NOT NULL,
    Descripcion TEXT,
    CategoriaID INT NOT NULL,
    MarcaID INT NOT NULL,
    Precio DECIMAL(10, 2) NOT NULL,
    Materiales VARCHAR(255),
    Peso DECIMAL(10, 2),
    Altura DECIMAL(10, 2),
    Ancho DECIMAL(10, 2),
    Profundidad DECIMAL(10, 2),
    FOREIGN KEY (CategoriaID) REFERENCES Categorias(CategoriaID) ON DELETE SET NULL,
    FOREIGN KEY (MarcaID) REFERENCES Marcas(MarcaID) ON DELETE SET NULL
);

-- Tabla ProductoProveedores
CREATE TABLE ProductoProveedores (
    ProductoID INT NOT NULL,
    ProveedorID INT NOT NULL,
    PRIMARY KEY (ProductoID, ProveedorID),
    FOREIGN KEY (ProductoID) REFERENCES Productos(ProductoID) ON DELETE CASCADE,
    FOREIGN KEY (ProveedorID) REFERENCES Proveedores(ProveedorID) ON DELETE CASCADE
);

-- Tabla ImagenesProducto
CREATE TABLE ImagenesProducto (
    ImagenID SERIAL PRIMARY KEY,
    ProductoID INT NOT NULL,
    URLImagen VARCHAR(255) NOT NULL,
    FOREIGN KEY (ProductoID) REFERENCES Productos(ProductoID) ON DELETE CASCADE
);

-- Tabla VariacionesProducto
CREATE TABLE VariacionesProducto (
    VariacionID SERIAL PRIMARY KEY,
    ProductoID INT NOT NULL,
    Color VARCHAR(50),
    Opciones VARCHAR(255),
    CantidadEnStock INT NOT NULL CHECK (CantidadEnStock >= 0),
    FOREIGN KEY (ProductoID) REFERENCES Productos(ProductoID) ON DELETE CASCADE
);

-- Tabla Pedidos
CREATE TABLE Pedidos (
    PedidoID SERIAL PRIMARY KEY,
    ClienteID INT NOT NULL,
    FechaHora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    TotalPrecio DECIMAL(10, 2) NOT NULL CHECK (TotalPrecio >= 0),
    EstadoPedido VARCHAR(50) NOT NULL,
    DireccionEnvioID INT NOT NULL,
    CuentaPagoID INT NOT NULL,
    FOREIGN KEY (ClienteID) REFERENCES Clientes(ClienteID) ON DELETE CASCADE,
    FOREIGN KEY (DireccionEnvioID) REFERENCES Direcciones(DireccionID) ON DELETE SET NULL,
    FOREIGN KEY (CuentaPagoID) REFERENCES CuentasPago(CuentaPagoID) ON DELETE SET NULL
);

-- Tabla DetallePedido
CREATE TABLE DetallePedido (
    DetallePedidoID SERIAL PRIMARY KEY,
    PedidoID INT NOT NULL,
    ProductoID INT NOT NULL,
    VariacionID INT,
    Cantidad INT NOT NULL CHECK (Cantidad > 0),
    PrecioUnitario DECIMAL(10, 2) NOT NULL CHECK (PrecioUnitario >= 0),
    FOREIGN KEY (PedidoID) REFERENCES Pedidos(PedidoID) ON DELETE CASCADE,
    FOREIGN KEY (ProductoID) REFERENCES Productos(ProductoID) ON DELETE CASCADE,
    FOREIGN KEY (VariacionID) REFERENCES VariacionesProducto(VariacionID) ON DELETE SET NULL
);

-- Tabla Pagos
CREATE TABLE Pagos (
    PagoID SERIAL PRIMARY KEY,
    PedidoID INT NOT NULL,
    FechaHoraPago TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    Monto DECIMAL(10, 2) NOT NULL CHECK (Monto >= 0),
    EstadoPago VARCHAR(50) NOT NULL,
    IDTransaccionProcesador VARCHAR(255),
    FOREIGN KEY (PedidoID) REFERENCES Pedidos(PedidoID) ON DELETE CASCADE
);

-- Índices adicionales
CREATE INDEX idx_productos_categoria ON Productos(CategoriaID);
CREATE INDEX idx_productos_marca ON Productos(MarcaID);
CREATE INDEX idx_pedidos_cliente ON Pedidos(ClienteID);
CREATE INDEX idx_detallepedido_pedido ON DetallePedido(PedidoID);
CREATE INDEX idx_pagospedido ON Pagos(PedidoID);

-- ---------------------------------------------------------------
-- Inserción de Datos de Ejemplo
-- ---------------------------------------------------------------

-- Insertar categorías
INSERT INTO Categorias (NombreCategoria) VALUES 
('Pesas y Manos Libres'),
('Equipos de Cardio'),
('Accesorios de Entrenamiento'),
('Yoga y Pilates');

-- Insertar marcas
INSERT INTO Marcas (NombreMarca) VALUES 
('FitPro'),
('GymMaster'),
('StrongLife'),
('FlexGear');

-- Insertar proveedores
INSERT INTO Proveedores (NombreProveedor, Contacto) VALUES 
('Proveedor1', 'contacto1@proveedor.com'),
('Proveedor2', 'contacto2@proveedor.com');

-- Insertar productos
INSERT INTO Productos (
    NombreProducto, 
    Descripcion, 
    CategoriaID, 
    MarcaID, 
    Precio, 
    Materiales, 
    Peso, 
    Altura, 
    Ancho, 
    Profundidad
) VALUES 
('Mancuernas', 'Juego de mancuernas ajustables de 10kg', 1, 1, 499.99, 'Acero y goma', 10.0, 15.0, 5.0, 5.0),
('Banco de Pesas', 'Banco ajustable para ejercicios de fuerza', 1, 2, 999.99, 'Acero y acolchado', 50.0, 100.0, 60.0, 30.0),
('Bicicleta Estática', 'Bicicleta estática con resistencia magnética', 2, 3, 3499.99, 'Acero y plástico', 70.0, 120.0, 50.0, 40.0),
('Pelota de Pilates', 'Pelota de pilates anti-explosión de 65 cm', 4, 4, 299.99, 'PVC', 2.0, 65.0, 65.0, 65.0),
('Cuerda para Saltar', 'Cuerda para saltar de velocidad con asas antideslizantes', 3, 1, 199.99, 'Plástico y caucho', 0.5, 150.0, 2.0, 2.0),
('Kettlebell', 'Kettlebell de 12kg para entrenamiento funcional', 1, 2, 599.99, 'Acero fundido', 12.0, 30.0, 30.0, 30.0),
('Colchoneta de Yoga', 'Colchoneta antideslizante para yoga y pilates', 4, 4, 399.99, 'EVA y caucho natural', 1.0, 180.0, 60.0, 2.0),
('Chaleco Lastrado', 'Chaleco lastrado de 10kg para entrenamiento de resistencia', 3, 3, 1299.99, 'Neopreno y acero', 10.0, 30.0, 25.0, 10.0);

-- Insertar imágenes de productos
INSERT INTO ImagenesProducto (ProductoID, URLImagen) VALUES
(1, 'https://verlo.co/gym/pesas.webp'),
(2, 'https://verlo.co/gym/bench.webp'),
(3, 'https://verlo.co/gym/bike.webp'),
(4, 'https://verlo.co/gym/pilates.webp'),
(5, 'https://verlo.co/gym/rope.webp'),
(6, 'https://verlo.co/gym/kettlebelt.webp'),
(7, 'https://verlo.co/gym/yoga.webp'),
(8, 'https://verlo.co/gym/vest.webp');

-- Insertar asociaciones entre productos y proveedores
INSERT INTO ProductoProveedores (ProductoID, ProveedorID) VALUES
(1, 1),
(2, 1),
(3, 2),
(4, 2),
(5, 1),
(6, 1),
(7, 2),
(8, 2);

