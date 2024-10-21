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
    FOREIGN KEY (ClienteID) REFERENCES Clientes(ClienteID)
);

CREATE TABLE CuentasPago (
    CuentaPagoID SERIAL PRIMARY KEY,
    ClienteID INT NOT NULL,
    TipoCuenta VARCHAR(50) NOT NULL, -- Por ejemplo, 'PayPal', 'Tarjeta de Crédito'
    DetalleCuenta VARCHAR(255) NOT NULL,
    FOREIGN KEY (ClienteID) REFERENCES Clientes(ClienteID)
);

CREATE TABLE Categorias (
    CategoriaID SERIAL PRIMARY KEY,
    NombreCategoria VARCHAR(100) NOT NULL
);

CREATE TABLE Marcas (
    MarcaID SERIAL PRIMARY KEY,
    NombreMarca VARCHAR(100) NOT NULL
);

CREATE TABLE Proveedores (
    ProveedorID SERIAL PRIMARY KEY,
    NombreProveedor VARCHAR(255) NOT NULL,
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
    FOREIGN KEY (CategoriaID) REFERENCES Categorias(CategoriaID),
    FOREIGN KEY (MarcaID) REFERENCES Marcas(MarcaID)
);

CREATE TABLE ProductoProveedores (
    ProductoID INT NOT NULL,
    ProveedorID INT NOT NULL,
    PRIMARY KEY (ProductoID, ProveedorID),
    FOREIGN KEY (ProductoID) REFERENCES Productos(ProductoID),
    FOREIGN KEY (ProveedorID) REFERENCES Proveedores(ProveedorID)
);

CREATE TABLE ImagenesProducto (
    ImagenID SERIAL PRIMARY KEY,
    ProductoID INT NOT NULL,
    URLImagen VARCHAR(255) NOT NULL,
    FOREIGN KEY (ProductoID) REFERENCES Productos(ProductoID)
);

CREATE TABLE VariacionesProducto (
    VariacionID SERIAL PRIMARY KEY,
    ProductoID INT NOT NULL,
    Color VARCHAR(50),
    Opciones VARCHAR(255),
    CantidadEnStock INT NOT NULL,
    FOREIGN KEY (ProductoID) REFERENCES Productos(ProductoID)
);

CREATE TABLE Pedidos (
    PedidoID SERIAL PRIMARY KEY,
    ClienteID INT NOT NULL,
    FechaHora TIMESTAMP NOT NULL,
    TotalPrecio DECIMAL(10, 2) NOT NULL,
    EstadoPedido VARCHAR(50) NOT NULL,
    DireccionEnvioID INT NOT NULL,
    CuentaPagoID INT NOT NULL,
    FOREIGN KEY (ClienteID) REFERENCES Clientes(ClienteID),
    FOREIGN KEY (DireccionEnvioID) REFERENCES Direcciones(DireccionID),
    FOREIGN KEY (CuentaPagoID) REFERENCES CuentasPago(CuentaPagoID)
);

CREATE TABLE DetallePedido (
    DetallePedidoID SERIAL PRIMARY KEY,
    PedidoID INT NOT NULL,
    ProductoID INT NOT NULL,
    VariacionID INT,
    Cantidad INT NOT NULL,
    PrecioUnitario DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (PedidoID) REFERENCES Pedidos(PedidoID),
    FOREIGN KEY (ProductoID) REFERENCES Productos(ProductoID),
    FOREIGN KEY (VariacionID) REFERENCES VariacionesProducto(VariacionID)
);

CREATE TABLE Pagos (
    PagoID SERIAL PRIMARY KEY,
    PedidoID INT NOT NULL,
    FechaHoraPago TIMESTAMP NOT NULL,
    Monto DECIMAL(10, 2) NOT NULL,
    EstadoPago VARCHAR(50) NOT NULL,
    IDTransaccionProcesador VARCHAR(255),
    FOREIGN KEY (PedidoID) REFERENCES Pedidos(PedidoID)
);
