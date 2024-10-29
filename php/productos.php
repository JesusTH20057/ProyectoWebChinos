<?php

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

$host = 'postgres'; 
$dbname = getenv('POSTGRES_DB'); 
$user = getenv('POSTGRES_USER'); 
$pass = getenv('POSTGRES_PASSWORD'); 
$port = '5432'; 

try {
    // Crear una nueva instancia de PDO
    $dsn = "pgsql:host=$host;port=$port;dbname=$dbname";
    $pdo = new PDO($dsn, $user, $pass);

    // Configurar el modo de error a excepción para facilitar la depuración
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // Consulta SQL para obtener productos con su categoría, marca y URL de la imagen
    $sql = "SELECT 
                p.ProductoID, 
                p.NombreProducto, 
                p.Descripcion, 
                p.Precio, 
                p.Materiales, 
                p.Peso, 
                p.Altura, 
                p.Ancho, 
                p.Profundidad, 
                c.NombreCategoria, 
                m.NombreMarca,
                ip.URLImagen
            FROM Productos p
            JOIN Categorias c ON p.CategoriaID = c.CategoriaID
            JOIN Marcas m ON p.MarcaID = m.MarcaID
            LEFT JOIN ImagenesProducto ip ON p.ProductoID = ip.ProductoID";

    // Preparar y ejecutar la consulta SQL
    $stmt = $pdo->prepare($sql);
    $stmt->execute();

    // Obtener todas las filas como un arreglo asociativo
    $productos = $stmt->fetchAll(PDO::FETCH_ASSOC);

    // Establecer el encabezado Content-Type a application/json
    header('Content-Type: application/json');

    // Salida del resultado como JSON
    echo json_encode($productos);

} catch (PDOException $e) {
    // Capturar y mostrar el error
    echo json_encode(["error" => "Conexión fallida: " . $e->getMessage()]);
}
?>
