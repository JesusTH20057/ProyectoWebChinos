<?php

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST, PUT, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Authorization");

// Configuración de la base de datos
$host = 'postgres';
$dbname = getenv('POSTGRES_DB');
$user = getenv('POSTGRES_USER');
$pass = getenv('POSTGRES_PASSWORD');
$port = '5432';

try {
    // Conexión a la base de datos
    $dsn = "pgsql:host=$host;port=$port;dbname=$dbname";
    $pdo = new PDO($dsn, $user, $pass);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // Obtener datos del cuerpo de la solicitud
    $data = json_decode(file_get_contents("php://input"), true);

    // Validar que todos los datos necesarios están presentes
    if (
        isset($data['productoid']) &&
        isset($data['nombreproducto']) &&
        isset($data['precio']) &&
        isset($data['stock']) &&
        isset($data['categoriaid']) &&
        isset($data['marcaid']) &&
        isset($data['urlimagen'])
    ) {
        // Actualizar los datos del producto en la tabla `Productos`
        $sqlProducto = "UPDATE productos SET
                            nombreproducto = :nombreproducto,
                            descripcion = :descripcion,
                            categoriaid = :categoriaid,
                            marcaid = :marcaid,
                            precio = :precio,
                            stock = :stock,
                            materiales = :materiales,
                            peso = :peso,
                            altura = :altura,
                            ancho = :ancho,
                            profundidad = :profundidad
                        WHERE productoid = :productoid";

        $stmtProducto = $pdo->prepare($sqlProducto);
        $stmtProducto->execute([
            ':nombreproducto' => $data['nombreproducto'],
            ':descripcion' => $data['descripcion'] ?? null,
            ':categoriaid' => $data['categoriaid'],
            ':marcaid' => $data['marcaid'],
            ':precio' => $data['precio'],
            ':stock' => $data['stock'],
            ':materiales' => $data['materiales'] ?? null,
            ':peso' => $data['peso'] ?? null,
            ':altura' => $data['altura'] ?? null,
            ':ancho' => $data['ancho'] ?? null,
            ':profundidad' => $data['profundidad'] ?? null,
            ':productoid' => $data['productoid']
        ]);

        // Comprobar si ya existe una imagen para el producto
        $sqlImagenCheck = "SELECT COUNT(*) FROM imagenesproducto WHERE productoid = :productoid";
        $stmtImagenCheck = $pdo->prepare($sqlImagenCheck);
        $stmtImagenCheck->execute([':productoid' => $data['productoid']]);
        $imagenExiste = $stmtImagenCheck->fetchColumn();

        if ($imagenExiste > 0) {
            // Si existe, actualizar la URL de la imagen
            $sqlImagenUpdate = "UPDATE imagenesproducto SET urlimagen = :urlimagen WHERE productoid = :productoid";
            $stmtImagenUpdate = $pdo->prepare($sqlImagenUpdate);
            $stmtImagenUpdate->execute([
                ':urlimagen' => $data['urlimagen'],
                ':productoid' => $data['productoid']
            ]);
        } else {
            // Si no existe, insertar una nueva fila en `ImagenesProducto`
            $sqlImagenInsert = "INSERT INTO imagenesproducto (productoid, urlimagen) VALUES (:productoid, :urlimagen)";
            $stmtImagenInsert = $pdo->prepare($sqlImagenInsert);
            $stmtImagenInsert->execute([
                ':productoid' => $data['productoid'],
                ':urlimagen' => $data['urlimagen']
            ]);
        }

        // Respuesta de éxito
        echo json_encode(["message" => "Producto actualizado con éxito."]);
    } else {
        // Respuesta en caso de datos incompletos
        echo json_encode(["error" => "Datos incompletos."]);
    }
} catch (PDOException $e) {
    // Manejo de errores de conexión
    echo json_encode(["error" => "Error en la conexión: " . $e->getMessage()]);
}

?>


