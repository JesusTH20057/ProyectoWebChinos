CREATE TABLE Producto (
    producto_id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    precio DECIMAL(10, 2) NOT NULL,
    stock INT DEFAULT 0
);

CREATE TABLE Cliente (
    cliente_id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    telefono VARCHAR(15)
);

CREATE TABLE Compra (
    compra_id INT PRIMARY KEY AUTO_INCREMENT,
    cliente_id INT,
    producto_id INT,
    cantidad INT NOT NULL,
    fecha_compra DATE NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES Cliente(cliente_id),
    FOREIGN KEY (producto_id) REFERENCES Producto(producto_id)
);
