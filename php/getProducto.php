<?php

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

// Configuración de la base de datos
$host = 'postgres';
$dbname = getenv('POSTGRES_DB'); // Nombre de la base de datos
$user = getenv('POSTGRES_USER'); // Usuario de la base de datos
$pass = getenv('POSTGRES_PASSWORD'); // Contraseña de la base de datos
$port = '5432'; // Puerto de PostgreSQL

try {
    // Conexión a la base de datos
    $dsn = "pgsql:host=$host;port=$port;dbname=$dbname";
    $pdo = new PDO($dsn, $user, $pass);

    // Configurar el modo de error de PDO
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // Obtener el ID del producto desde la URL
    $id = isset($_GET['id']) ? intval($_GET['id']) : null;

    if ($id) {
        // Consulta SQL para obtener los detalles del producto, incluyendo su categoría, marca y URL de la imagen
        $sql = "SELECT 
                    p.productoid,
                    p.nombreproducto,
                    p.descripcion,
                    p.precio,
                    p.stock,
                    p.materiales,
                    p.peso,
                    p.altura,
                    p.ancho,
                    p.profundidad,
                    c.nombrecategoria AS categoria,
                    m.nombremarca AS marca,
                    (SELECT ip.urlimagen FROM imagenesproducto ip WHERE ip.productoid = p.productoid LIMIT 1) AS urlimagen
                FROM productos p
                JOIN categorias c ON p.categoriaid = c.categoriaid
                JOIN marcas m ON p.marcaid = m.marcaid
                WHERE p.productoid = :id";

        $stmt = $pdo->prepare($sql);
        $stmt->bindParam(':id', $id, PDO::PARAM_INT);
        $stmt->execute();

        // Obtener el producto como un arreglo asociativo
        $producto = $stmt->fetch(PDO::FETCH_ASSOC);

        // Comprobar si se encontró el producto
        if ($producto) {
            echo json_encode($producto);
        } else {
            echo json_encode(["error" => "Producto no encontrado."]);
        }
    } else {
        echo json_encode(["error" => "ID no proporcionado."]);
    }
} catch (PDOException $e) {
    // Enviar un mensaje de error si la conexión falla
    echo json_encode(["error" => "Error en la conexión: " . $e->getMessage()]);
}

?>

