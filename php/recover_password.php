<?php

// recover-password.php

// Configuración de la conexión a la base de datos
$host = 'postgres'; 
$dbname = getenv('POSTGRES_DB'); 
$user = getenv('POSTGRES_USER'); 
$pass = getenv('POSTGRES_PASSWORD'); 
$port = '5432'; 

try {
    $dsn = "pgsql:host=$host;port=$port;dbname=$dbname";
    $pdo = new PDO($dsn, $user, $pass);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(["error" => "Error de conexión a la base de datos: " . $e->getMessage()]);
    exit;
}

// Verificar si la solicitud es POST
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $data = json_decode(file_get_contents('php://input'), true);

    // Validar los datos de entrada
    if (!isset($data['nombreUsuario']) || !isset($data['correo'])) {
        http_response_code(400);
        echo json_encode(["error" => "Faltan campos obligatorios."]);
        exit;
    }

    $nombreUsuario = trim($data['nombreUsuario']);
    $correo = trim($data['correo']);

    try {
        // Verificar si el usuario existe en la base de datos
        $stmt = $pdo->prepare("SELECT clienteid, contrasena FROM clientes WHERE nombreusuario = :nombreUsuario AND email = :correo");
        $stmt->bindParam(':nombreUsuario', $nombreUsuario);
        $stmt->bindParam(':correo', $correo);
        $stmt->execute();

        $usuario = $stmt->fetch(PDO::FETCH_ASSOC);

        if ($usuario) {
            // Generar un token único para la recuperación
            $token = bin2hex(random_bytes(16));

            // Insertar token en la tabla password_resets
            $stmtToken = $pdo->prepare("
                INSERT INTO password_resets (clienteid, token, contrasena, fecha_expiracion)
                VALUES (:clienteid, :token, :contrasena, NOW() + INTERVAL '1 HOUR')
            ");
            $stmtToken->bindParam(':clienteid', $usuario['clienteid']);
            $stmtToken->bindParam(':token', $token);
            $stmtToken->bindParam(':contrasena', $usuario['contrasena']); // Guardamos el hash de la contraseña
            $stmtToken->execute();

            // Enviar el token al correo electrónico del usuario (simulado aquí)
            echo json_encode([
                "success" => true,
                "message" => "Revisa tu correo para continuar con la recuperación de contraseña.",
                "token" => $token // En un entorno real, este token se enviaría por correo.
            ]);
        } else {
            http_response_code(404);
            echo json_encode(["error" => "Usuario no encontrado."]);
        }
    } catch (PDOException $e) {
        http_response_code(500);
        echo json_encode(["error" => "Error del servidor: " . $e->getMessage()]);
    }
} elseif ($_SERVER['REQUEST_METHOD'] === 'PUT') {
    // Cambiar la contraseña del usuario
    $data = json_decode(file_get_contents('php://input'), true);

    // Validar los datos de entrada
    if (!isset($data['token']) || !isset($data['nuevaContrasena'])) {
        http_response_code(400);
        echo json_encode(["error" => "Faltan campos obligatorios."]);
        exit;
    }

    $token = trim($data['token']);
    $nuevaContrasena = trim($data['nuevaContrasena']);

    // Verificar si el token es válido
    try {
        $stmt = $pdo->prepare("SELECT clienteid FROM password_resets WHERE token = :token AND fecha_expiracion > NOW()");
        $stmt->bindParam(':token', $token);
        $stmt->execute();

        $registro = $stmt->fetch(PDO::FETCH_ASSOC);

        if ($registro) {
            // Cifrar la nueva contraseña
            $hashContrasena = password_hash($nuevaContrasena, PASSWORD_BCRYPT);

            // Actualizar la contraseña en la tabla de clientes
            $stmtUpdate = $pdo->prepare("UPDATE clientes SET contrasena = :contrasena WHERE clienteid = :clienteid");
            $stmtUpdate->bindParam(':contrasena', $hashContrasena);
            $stmtUpdate->bindParam(':clienteid', $registro['clienteid']);
            $stmtUpdate->execute();

            // Eliminar el token después de su uso
            $stmtDelete = $pdo->prepare("DELETE FROM password_resets WHERE token = :token");
            $stmtDelete->bindParam(':token', $token);
            $stmtDelete->execute();

            echo json_encode([
                "success" => true,
                "message" => "Contraseña actualizada exitosamente."
            ]);
        } else {
            http_response_code(400);
            echo json_encode(["error" => "Token inválido o expirado."]);
        }
    } catch (PDOException $e) {
        http_response_code(500);
        echo json_encode(["error" => "Error del servidor: " . $e->getMessage()]);
    }
} else {
    http_response_code(405);
    echo json_encode(["error" => "Método no permitido."]);
}

