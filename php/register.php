<?php

// register.php

// Configuración de la conexión a la base de datos
$host = 'postgres'; 
$dbname = getenv('POSTGRES_DB'); 
$user = getenv('POSTGRES_USER'); 
$pass = getenv('POSTGRES_PASSWORD'); 
$port = '5432'; 

try {
    $dsn = "pgsql:host=$host;port=$port;dbname=$dbname";
    $pdo = new PDO($dsn, $user, $pass);

    // Establecer el modo de error de PDO a excepción
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(["error" => "Error de conexión a la base de datos: " . $e->getMessage()]);
    exit;
}

// Función para validar el formato del correo electrónico
function isValidEmail($email) {
    return filter_var($email, FILTER_VALIDATE_EMAIL);
}

// Función para validar la fortaleza de la contraseña (opcional)
function isValidPassword($password) {
    // Por ejemplo, al menos 6 caracteres
    return strlen($password) >= 6;
}

// Verificar si la solicitud es POST
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Obtener los datos JSON de la solicitud
    $data = json_decode(file_get_contents('php://input'), true);

    // Validar la entrada
    if (!isset($data['nombre']) || !isset($data['nombreUsuario']) || !isset($data['correo']) || !isset($data['contrasena'])) {
        http_response_code(400);
        echo json_encode(["error" => "Faltan campos obligatorios."]);
        exit;
    }

    $nombre = trim($data['nombre']);
    $nombreUsuario = trim($data['nombreUsuario']);
    $correo = trim($data['correo']);
    $contrasena = $data['contrasena'];
    $telefono = isset($data['telefono']) ? trim($data['telefono']) : null; // Opcional

    // Validaciones básicas
    if (empty($nombre) || empty($nombreUsuario) || empty($correo) || empty($contrasena)) {
        http_response_code(400);
        echo json_encode(["error" => "Todos los campos obligatorios deben estar completos."]);
        exit;
    }

    if (!isValidEmail($correo)) {
        http_response_code(400);
        echo json_encode(["error" => "Formato de correo electrónico inválido."]);
        exit;
    }

    if (!isValidPassword($contrasena)) {
        http_response_code(400);
        echo json_encode(["error" => "La contraseña debe tener al menos 6 caracteres."]);
        exit;
    }

    try {
        // Verificar si el correo electrónico ya está registrado
        $stmtEmail = $pdo->prepare("SELECT clienteid FROM clientes WHERE email = :correo");
        $stmtEmail->bindParam(':correo', $correo);
        $stmtEmail->execute();

        if ($stmtEmail->fetch(PDO::FETCH_ASSOC)) {
            http_response_code(409); // Conflict
            echo json_encode(["error" => "El correo electrónico ya está registrado."]);
            exit;
        }

        // Verificar si el nombre de usuario ya está en uso
        $stmtUsername = $pdo->prepare("SELECT clienteid FROM clientes WHERE nombreusuario = :nombreUsuario");
        $stmtUsername->bindParam(':nombreUsuario', $nombreUsuario);
        $stmtUsername->execute();

        if ($stmtUsername->fetch(PDO::FETCH_ASSOC)) {
            http_response_code(409); // Conflict
            echo json_encode(["error" => "El nombre de usuario ya está en uso."]);
            exit;
        }

        // Hash de la contraseña usando bcrypt
        $hashedPassword = password_hash($contrasena, PASSWORD_BCRYPT);

        // Insertar el nuevo usuario en la base de datos
        $insertStmt = $pdo->prepare("
            INSERT INTO clientes (nombrecompleto, email, telefono, nombreusuario, contrasena)
            VALUES (:nombreCompleto, :correo, :telefono, :nombreUsuario, :contrasena)
            RETURNING clienteid
        ");
        $insertStmt->bindParam(':nombreCompleto', $nombre);
        $insertStmt->bindParam(':correo', $correo);
        $insertStmt->bindParam(':telefono', $telefono);
        $insertStmt->bindParam(':nombreUsuario', $nombreUsuario);
        $insertStmt->bindParam(':contrasena', $hashedPassword);
        $insertStmt->execute();

        $nuevoClienteID = $insertStmt->fetchColumn();

        // Opcional: Enviar correo de bienvenida o confirmación
        // Puedes implementar aquí el envío de un correo electrónico de confirmación si lo deseas.

        // Responder con éxito
        echo json_encode([
            "success" => true,
            "message" => "Registro exitoso. Ahora puedes iniciar sesión.",
            "clienteID" => $nuevoClienteID
        ]);
    } catch (PDOException $e) {
        http_response_code(500);
        echo json_encode(["error" => "Error del servidor: " . $e->getMessage()]);
    }
} else {
    http_response_code(405);
    echo json_encode(["error" => "Método no permitido."]);
}
?>
