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
('Mancuernas', 'Juego de mancuernas ajustables de 10kg', 499.99, 50, 'https://verlo.co/gym/pesas.webp'),
('Banco de Pesas', 'Banco ajustable para ejercicios de fuerza', 999.99, 15, 'https://verlo.co/gym/bench.webp'),
('Bicicleta Estática', 'Bicicleta estática con resistencia magnética', 3499.99, 10, 'https://verlo.co/gym/bike.webp'),
('Pelota de Pilates', 'Pelota de pilates anti-explosión de 65 cm', 299.99, 100, 'https://verlo.co/gym/pilates.webp'),
('Cuerda para Saltar', 'Cuerda para saltar de velocidad con asas antideslizantes', 199.99, 200, 'https://verlo.co/gym/rope.webp'),
('Kettlebell', 'Kettlebell de 12kg para entrenamiento funcional', 599.99, 30, 'https://verlo.co/gym/kettlebelt.webp'),
('Colchoneta de Yoga', 'Colchoneta antideslizante para yoga y pilates', 399.99, 50, 'https://verlo.co/gym/yoga.webp'),
('Chaleco Lastrado', 'Chaleco lastrado de 10kg para entrenamiento de resistencia', 1299.99, 25, 'https://verlo.co/gym/vest.webp');


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