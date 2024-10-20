-- Create the Producto table with an image URL
CREATE TABLE Producto (
    producto_id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    precio DECIMAL(10, 2) NOT NULL,
    stock INT DEFAULT 0,
    imagen_url VARCHAR(255)  -- New column for the image URL
);

-- Create the Cliente table
CREATE TABLE Cliente (
    cliente_id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    telefono VARCHAR(15)
);

-- Create the Compra table
CREATE TABLE Compra (
    compra_id SERIAL PRIMARY KEY,
    cliente_id INT,
    producto_id INT,
    cantidad INT NOT NULL,
    fecha_compra DATE NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES Cliente(cliente_id),
    FOREIGN KEY (producto_id) REFERENCES Producto(producto_id)
);

-- Insert sample data into Producto (Gym equipment with image URLs)
INSERT INTO Producto (nombre, descripcion, precio, stock, imagen_url) VALUES 
('Mancuernas', 'Juego de mancuernas ajustables de 10kg', 499.99, 50, 'https://example.com/images/mancuernas.jpg'),
('Barra Olímpica', 'Barra de acero inoxidable para levantamiento olímpico', 1999.99, 20, 'https://example.com/images/barra-olimpica.jpg'),
('Banco de Pesas', 'Banco ajustable para ejercicios de fuerza', 999.99, 15, 'https://example.com/images/banco-pesas.jpg'),
('Cinta de Correr', 'Cinta de correr con inclinación ajustable y monitor digital', 5999.99, 5, 'https://example.com/images/cinta-correr.jpg'),
('Bicicleta Estática', 'Bicicleta estática con resistencia magnética', 3499.99, 10, 'https://example.com/images/bicicleta-estatica.jpg'),
('Pelota de Pilates', 'Pelota de pilates anti-explosión de 65 cm', 299.99, 100, 'https://example.com/images/pelota-pilates.jpg'),
('Cuerda para Saltar', 'Cuerda para saltar de velocidad con asas antideslizantes', 199.99, 200, 'https://example.com/images/cuerda-saltar.jpg'),
('Kettlebell', 'Kettlebell de 12kg para entrenamiento funcional', 599.99, 30, 'https://example.com/images/kettlebell.jpg'),
('Colchoneta de Yoga', 'Colchoneta antideslizante para yoga y pilates', 399.99, 50, 'https://example.com/images/colchoneta-yoga.jpg'),
('Chaleco Lastrado', 'Chaleco lastrado de 10kg para entrenamiento de resistencia', 1299.99, 25, 'https://example.com/images/chaleco-lastrado.jpg');


-- Insert sample data into Cliente
INSERT INTO Cliente (nombre, email, telefono) VALUES 
('Victor Mendoza', 'vumg.ok@gmail.com', '1234567890'),
('Rick Sanchez', 'rick@gmail.com', '0987654321'),
('Roy', 'roy@gmail.com', '3213131321');

-- Insert sample data into Compra
INSERT INTO Compra (cliente_id, producto_id, cantidad, fecha_compra) VALUES
(1, 1, 2, '2024-10-01'),
(2, 3, 1, '2024-10-05'),
(3, 2, 5, '2024-10-10');