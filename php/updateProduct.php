<?php

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
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

    if (isset($data['productoid'])) {
        // Actualizar el producto
        $sql = "UPDATE productos SET
                    nombreproducto = COALESCE(:nombreproducto, nombreproducto),
                    descripcion = COALESCE(:descripcion, descripcion),
                    categoriaid = COALESCE(:categoriaid, categoriaid),
                    marcaid = COALESCE(:marcaid, marcaid),
                    precio = COALESCE(:precio, precio),
                    stock = COALESCE(:stock, stock),
                    materiales = COALESCE(:materiales, materiales),
                    peso = COALESCE(:peso, peso),
                    altura = COALESCE(:altura, altura),
                    ancho = COALESCE(:ancho, ancho),
                    profundidad = COALESCE(:profundidad, profundidad)
                WHERE productoid = :productoid";

        $stmt = $pdo->prepare($sql);
        $stmt->execute([
            ':nombreproducto' => $data['nombreproducto'] ?? null,
            ':descripcion' => $data['descripcion'] ?? null,
            ':categoriaid' => $data['categoriaid'] ?? null,
            ':marcaid' => $data['marcaid'] ?? null,
            ':precio' => $data['precio'] ?? null,
            ':stock' => $data['stock'] ?? null,
            ':materiales' => $data['materiales'] ?? null,
            ':peso' => $data['peso'] ?? null,
            ':altura' => $data['altura'] ?? null,
            ':ancho' => $data['ancho'] ?? null,
            ':profundidad' => $data['profundidad'] ?? null,
            ':productoid' => $data['productoid']
        ]);

        // Actualizar o insertar la imagen
        if (isset($data['urlimagen'])) {
            $sqlImagenCheck = "SELECT COUNT(*) FROM imagenesproducto WHERE productoid = :productoid";
            $stmtImagenCheck = $pdo->prepare($sqlImagenCheck);
            $stmtImagenCheck->execute([':productoid' => $data['productoid']]);
            $imagenExiste = $stmtImagenCheck->fetchColumn();

            if ($imagenExiste > 0) {
                $sqlImagenUpdate = "UPDATE imagenesproducto SET urlimagen = :urlimagen WHERE productoid = :productoid";
                $stmtImagenUpdate = $pdo->prepare($sqlImagenUpdate);
                $stmtImagenUpdate->execute([
                    ':urlimagen' => $data['urlimagen'],
                    ':productoid' => $data['productoid']
                ]);
            } else {
                $sqlImagenInsert = "INSERT INTO imagenesproducto (productoid, urlimagen) VALUES (:productoid, :urlimagen)";
                $stmtImagenInsert = $pdo->prepare($sqlImagenInsert);
                $stmtImagenInsert->execute([
                    ':productoid' => $data['productoid'],
                    ':urlimagen' => $data['urlimagen']
                ]);
            }
        }

        echo json_encode(["message" => "Producto actualizado con éxito."]);
    } else {
        echo json_encode(["error" => "ID del producto no proporcionado."]);
    }
} catch (PDOException $e) {
    echo json_encode(["error" => "Error en la conexión: " . $e->getMessage()]);
}

?>



