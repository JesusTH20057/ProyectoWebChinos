-- Crear la extensión para manejar UUIDs si es necesario (opcional)
-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE Clientes (
    ClienteID SERIAL PRIMARY KEY,
    NombreCompleto VARCHAR(255) NOT NULL,
    Email VARCHAR(255) NOT NULL UNIQUE,
    Telefono VARCHAR(20),
    NombreUsuario VARCHAR(100) NOT NULL UNIQUE,
    Contrasena VARCHAR(255) NOT NULL
);

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

CREATE TABLE CuentasPago (
    CuentaPagoID SERIAL PRIMARY KEY,
    ClienteID INT NOT NULL,
    TipoCuenta VARCHAR(50) NOT NULL, -- Por ejemplo, 'PayPal', 'Tarjeta de Crédito'
    DetalleCuenta VARCHAR(255) NOT NULL,
    FOREIGN KEY (ClienteID) REFERENCES Clientes(ClienteID) ON DELETE CASCADE
);

CREATE TABLE Categorias (
    CategoriaID SERIAL PRIMARY KEY,
    NombreCategoria VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE Marcas (
    MarcaID SERIAL PRIMARY KEY,
    NombreMarca VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE Proveedores (
    ProveedorID SERIAL PRIMARY KEY,
    NombreProveedor VARCHAR(255) NOT NULL UNIQUE,
    Contacto VARCHAR(255)
);

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

CREATE TABLE ProductoProveedores (
    ProductoID INT NOT NULL,
    ProveedorID INT NOT NULL,
    PRIMARY KEY (ProductoID, ProveedorID),
    FOREIGN KEY (ProductoID) REFERENCES Productos(ProductoID) ON DELETE CASCADE,
    FOREIGN KEY (ProveedorID) REFERENCES Proveedores(ProveedorID) ON DELETE CASCADE
);

CREATE TABLE ImagenesProducto (
    ImagenID SERIAL PRIMARY KEY,
    ProductoID INT NOT NULL,
    URLImagen VARCHAR(255) NOT NULL,
    FOREIGN KEY (ProductoID) REFERENCES Productos(ProductoID) ON DELETE CASCADE
);

CREATE TABLE VariacionesProducto (
    VariacionID SERIAL PRIMARY KEY,
    ProductoID INT NOT NULL,
    Color VARCHAR(50),
    Opciones VARCHAR(255),
    CantidadEnStock INT NOT NULL CHECK (CantidadEnStock >= 0),
    FOREIGN KEY (ProductoID) REFERENCES Productos(ProductoID) ON DELETE CASCADE
);

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

CREATE TABLE Pagos (
    PagoID SERIAL PRIMARY KEY,
    PedidoID INT NOT NULL,
    FechaHoraPago TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    Monto DECIMAL(10, 2) NOT NULL CHECK (Monto >= 0),
    EstadoPago VARCHAR(50) NOT NULL,
    IDTransaccionProcesador VARCHAR(255),
    FOREIGN KEY (PedidoID) REFERENCES Pedidos(PedidoID) ON DELETE CASCADE
);

-- Índices adicionales para optimizar consultas frecuentes
CREATE INDEX idx_productos_categoria ON Productos(CategoriaID);
CREATE INDEX idx_productos_marca ON Productos(MarcaID);
CREATE INDEX idx_pedidos_cliente ON Pedidos(ClienteID);
CREATE INDEX idx_pedidos_direccion ON Pedidos(DireccionEnvioID);
CREATE INDEX idx_pedidos_cuenta_pago ON Pedidos(CuentaPagoID);
CREATE INDEX idx_detallepedido_pedido ON DetallePedido(PedidoID);
CREATE INDEX idx_detallepedido_producto ON DetallePedido(ProductoID);
CREATE INDEX idx_pagospedido ON Pagos(PedidoID);
