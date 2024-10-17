-- Tabla Clientes
CREATE TABLE Clientes (
    ClienteID INT AUTO_INCREMENT PRIMARY KEY,
    NombreCompleto VARCHAR(255) NOT NULL,
    Email VARCHAR(255) NOT NULL UNIQUE,
    Telefono VARCHAR(20),
    NombreUsuario VARCHAR(100) NOT NULL UNIQUE,
    Contrasena VARCHAR(255) NOT NULL
);

-- Tabla Direcciones
CREATE TABLE Direcciones (
    DireccionID INT AUTO_INCREMENT PRIMARY KEY,
    ClienteID INT NOT NULL,
    Calle VARCHAR(255) NOT NULL,
    Ciudad VARCHAR(100) NOT NULL,
    Estado VARCHAR(100),
    CodigoPostal VARCHAR(20),
    Pais VARCHAR(100) NOT NULL,
    Tipo VARCHAR(50), -- Por ejemplo, 'Envío', 'Facturación'
    FOREIGN KEY (ClienteID) REFERENCES Clientes(ClienteID)
);

-- Tabla CuentasPago
CREATE TABLE CuentasPago (
    CuentaPagoID INT AUTO_INCREMENT PRIMARY KEY,
    ClienteID INT NOT NULL,
    TipoCuenta VARCHAR(50) NOT NULL, -- Por ejemplo, 'PayPal', 'Tarjeta de Crédito'
    DetalleCuenta VARCHAR(255) NOT NULL,
    FOREIGN KEY (ClienteID) REFERENCES Clientes(ClienteID)
);

-- Tabla Categorias
CREATE TABLE Categorias (
    CategoriaID INT AUTO_INCREMENT PRIMARY KEY,
    NombreCategoria VARCHAR(100) NOT NULL
);

-- Tabla Marcas
CREATE TABLE Marcas (
    MarcaID INT AUTO_INCREMENT PRIMARY KEY,
    NombreMarca VARCHAR(100) NOT NULL
);

-- Tabla Proveedores
CREATE TABLE Proveedores (
    ProveedorID INT AUTO_INCREMENT PRIMARY KEY,
    NombreProveedor VARCHAR(255) NOT NULL,
    Contacto VARCHAR(255)
);

-- Tabla Productos
CREATE TABLE Productos (
    ProductoID INT AUTO_INCREMENT PRIMARY KEY,
    NombreProducto VARCHAR(255) NOT NULL,
    Descripcion TEXT,
    CategoriaID INT NOT NULL,
    MarcaID INT NOT NULL,
    Precio DECIMAL(10,2) NOT NULL,
    Materiales VARCHAR(255),
    Peso DECIMAL(10,2),
    Altura DECIMAL(10,2),
    Ancho DECIMAL(10,2),
    Profundidad DECIMAL(10,2),
    FOREIGN KEY (CategoriaID) REFERENCES Categorias(CategoriaID),
    FOREIGN KEY (MarcaID) REFERENCES Marcas(MarcaID)
);

-- Tabla ProductoProveedores (Relación Muchos a Muchos entre Productos y Proveedores)
CREATE TABLE ProductoProveedores (
    ProductoID INT NOT NULL,
    ProveedorID INT NOT NULL,
    PRIMARY KEY (ProductoID, ProveedorID),
    FOREIGN KEY (ProductoID) REFERENCES Productos(ProductoID),
    FOREIGN KEY (ProveedorID) REFERENCES Proveedores(ProveedorID)
);

-- Tabla ImagenesProducto
CREATE TABLE ImagenesProducto (
    ImagenID INT AUTO_INCREMENT PRIMARY KEY,
    ProductoID INT NOT NULL,
    URLImagen VARCHAR(255) NOT NULL,
    FOREIGN KEY (ProductoID) REFERENCES Productos(ProductoID)
);

-- Tabla VariacionesProducto
CREATE TABLE VariacionesProducto (
    VariacionID INT AUTO_INCREMENT PRIMARY KEY,
    ProductoID INT NOT NULL,
    Color VARCHAR(50),
    Opciones VARCHAR(255),
    CantidadEnStock INT NOT NULL,
    FOREIGN KEY (ProductoID) REFERENCES Productos(ProductoID)
);

-- Tabla Pedidos
CREATE TABLE Pedidos (
    PedidoID INT AUTO_INCREMENT PRIMARY KEY,
    ClienteID INT NOT NULL,
    FechaHora DATETIME NOT NULL,
    TotalPrecio DECIMAL(10,2) NOT NULL,
    EstadoPedido VARCHAR(50) NOT NULL,
    DireccionEnvioID INT NOT NULL,
    CuentaPagoID INT NOT NULL,
    FOREIGN KEY (ClienteID) REFERENCES Clientes(ClienteID),
    FOREIGN KEY (DireccionEnvioID) REFERENCES Direcciones(DireccionID),
    FOREIGN KEY (CuentaPagoID) REFERENCES CuentasPago(CuentaPagoID)
);

-- Tabla DetallePedido
CREATE TABLE DetallePedido (
    DetallePedidoID INT AUTO_INCREMENT PRIMARY KEY,
    PedidoID INT NOT NULL,
    ProductoID INT NOT NULL,
    VariacionID INT,
    Cantidad INT NOT NULL,
    PrecioUnitario DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (PedidoID) REFERENCES Pedidos(PedidoID),
    FOREIGN KEY (ProductoID) REFERENCES Productos(ProductoID),
    FOREIGN KEY (VariacionID) REFERENCES VariacionesProducto(VariacionID)
);

-- Tabla Pagos
CREATE TABLE Pagos (
    PagoID INT AUTO_INCREMENT PRIMARY KEY,
    PedidoID INT NOT NULL,
    FechaHoraPago DATETIME NOT NULL,
    Monto DECIMAL(10,2) NOT NULL,
    EstadoPago VARCHAR(50) NOT NULL,
    IDTransaccionProcesador VARCHAR(255),
    FOREIGN KEY (PedidoID) REFERENCES Pedidos(PedidoID)
);

