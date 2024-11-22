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

    // Obtener los datos del producto desde el cuerpo de la solicitud
    $data = json_decode(file_get_contents("php://input"), true);

    if (!empty($data['nombre']) && !empty($data['precio']) && isset($data['stock']) && !empty($data['imagen'])) {
        // Iniciar una transacción
        $pdo->beginTransaction();

        // Insertar producto en la tabla Productos
        $sqlProducto = "INSERT INTO productos (nombreproducto, descripcion, categoriaid, marcaid, precio, stock, materiales, peso, altura, ancho, profundidad) 
                        VALUES (:nombre, :descripcion, :categoriaid, :marcaid, :precio, :stock, :materiales, :peso, :altura, :ancho, :profundidad) RETURNING productoid";
        $stmtProducto = $pdo->prepare($sqlProducto);
        $stmtProducto->execute([
            ':nombre' => $data['nombre'],
            ':descripcion' => $data['descripcion'] ?? null,
            ':categoriaid' => $data['categoriaid'],
            ':marcaid' => $data['marcaid'],
            ':precio' => $data['precio'],
            ':stock' => $data['stock'],
            ':materiales' => $data['materiales'] ?? null,
            ':peso' => $data['peso'] ?? null,
            ':altura' => $data['altura'] ?? null,
            ':ancho' => $data['ancho'] ?? null,
            ':profundidad' => $data['profundidad'] ?? null
        ]);

        // Obtener el ID del producto recién insertado
        $productoID = $stmtProducto->fetchColumn();

        // Insertar la URL de la imagen en la tabla ImagenesProducto
        $sqlImagen = "INSERT INTO imagenesproducto (productoid, urlimagen) VALUES (:productoid, :urlimagen)";
        $stmtImagen = $pdo->prepare($sqlImagen);
        $stmtImagen->execute([
            ':productoid' => $productoID,
            ':urlimagen' => $data['imagen']
        ]);

        // Confirmar la transacción
        $pdo->commit();

        echo json_encode(["message" => "Producto agregado exitosamente"]);
    } else {
        echo json_encode(["error" => "Datos incompletos para agregar el producto"]);
    }
} catch (PDOException $e) {
    // Revertir la transacción en caso de error
    if ($pdo->inTransaction()) {
        $pdo->rollBack();
    }
    echo json_encode(["error" => "Error en la conexión: " . $e->getMessage()]);
}
?>

