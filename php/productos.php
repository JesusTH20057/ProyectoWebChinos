<?php

$host = 'postgres'; 
$dbname = getenv('POSTGRES_DB'); 
$user = getenv('POSTGRES_USER'); 
$pass = getenv('POSTGRES_PASSWORD'); 
$port = '5432'; 

try {
    // Create a new PDO instance
    $dsn = "pgsql:host=$host;port=$port;dbname=$dbname";
    $pdo = new PDO($dsn, $user, $pass);

    // Set error mode to exception for easier debugging
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // SQL query to fetch products with their category and brand (if you want to include related tables)
    $sql = "SELECT p.ProductoID, p.NombreProducto, p.Descripcion, p.Precio, 
                   p.Materiales, p.Peso, p.Altura, p.Ancho, p.Profundidad, 
                   c.NombreCategoria, m.NombreMarca
            FROM Productos p
            JOIN Categorias c ON p.CategoriaID = c.CategoriaID
            JOIN Marcas m ON p.MarcaID = m.MarcaID";

    // Prepare and execute the SQL query
    $stmt = $pdo->query($sql);

    // Fetch all the rows as an associative array
    $productos = $stmt->fetchAll(PDO::FETCH_ASSOC);

    // Set the Content-Type header to application/json
    header('Content-Type: application/json');

    // Output the result as JSON
    echo json_encode($productos);

} catch (PDOException $e) {
    // Catch and display the error
    echo json_encode(["error" => "Connection failed: " . $e->getMessage()]);
}
?>
