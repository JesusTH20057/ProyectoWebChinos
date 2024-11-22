<?php

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

// Configuración de la conexión a la base de datos
$host = 'postgres';
$dbname = getenv('POSTGRES_DB');
$user = getenv('POSTGRES_USER');
$pass = getenv('POSTGRES_PASSWORD');
$port = '5432';

try {
    // Crear conexión
    $dsn = "pgsql:host=$host;port=$port;dbname=$dbname";
    $pdo = new PDO($dsn, $user, $pass);

    // Configurar el modo de error de PDO
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // Obtener el ID del producto de la URL
    if (isset($_GET['id'])) {
        $productoID = intval($_GET['id']);

        // Eliminar el producto de la base de datos
        $sql = "DELETE FROM productos WHERE productoID = :id";
        $stmt = $pdo->prepare($sql);
        $stmt->bindParam(':id', $productoID, PDO::PARAM_INT);
        $stmt->execute();

        // Verificar si se eliminó correctamente
        if ($stmt->rowCount() > 0) {
            echo json_encode(["message" => "Producto eliminado exitosamente"]);
        } else {
            echo json_encode(["error" => "Producto no encontrado"]);
        }
    } else {
        echo json_encode(["error" => "ID del producto no proporcionado"]);
    }
} catch (PDOException $e) {
    echo json_encode(["error" => "Error en la conexión: " . $e->getMessage()]);
}
?>
