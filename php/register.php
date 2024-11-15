<?php

//register.php

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
    return strlen($password) >= 6;
}

// Verificar si la solicitud es POST
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Obtener los datos JSON de la solicitud
    $data = json_decode(file_get_contents('php://input'), true);

    // Validar los campos obligatorios
    if (!isset($data['cliente']) || !isset($data['direccion'])) {
        http_response_code(400);
        echo json_encode(["error" => "Faltan campos obligatorios."]);
        exit;
    }

    $cliente = $data['cliente'];
    $direccion = $data['direccion'];

    // Validar la entrada del cliente
    if (!isset($cliente['nombreCompleto'], $cliente['email'], $cliente['nombreUsuario'], $cliente['contrasena'])) {
        http_response_code(400);
        echo json_encode(["error" => "Faltan campos obligatorios en los datos del cliente."]);
        exit;
    }

    $nombre = trim($cliente['nombreCompleto']);
    $nombreUsuario = trim($cliente['nombreUsuario']);
    $correo = trim($cliente['email']);
    $contrasena = $cliente['contrasena'];
    $telefono = isset($cliente['telefono']) ? trim($cliente['telefono']) : null;

    // Validar la entrada de la dirección
    if (!isset($direccion['calle'], $direccion['ciudad'], $direccion['pais'])) {
        http_response_code(400);
        echo json_encode(["error" => "Faltan campos obligatorios en los datos de la dirección."]);
        exit;
    }

    $calle = trim($direccion['calle']);
    $ciudad = trim($direccion['ciudad']);
    $estado = isset($direccion['estado']) ? trim($direccion['estado']) : null;
    $codigoPostal = isset($direccion['codigoPostal']) ? trim($direccion['codigoPostal']) : null;
    $pais = trim($direccion['pais']);
    $tipo = isset($direccion['tipo']) ? trim($direccion['tipo']) : 'Envío';

    // Validaciones básicas
    if (empty($nombre) || empty($nombreUsuario) || empty($correo) || empty($contrasena) || empty($calle) || empty($ciudad) || empty($pais)) {
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

        // Iniciar transacción
        $pdo->beginTransaction();

        // Hash de la contraseña usando bcrypt
        $hashedPassword = password_hash($contrasena, PASSWORD_BCRYPT);

        // Insertar el nuevo cliente en la base de datos
        $insertCliente = $pdo->prepare("
            INSERT INTO clientes (nombrecompleto, email, telefono, nombreusuario, contrasena)
            VALUES (:nombreCompleto, :correo, :telefono, :nombreUsuario, :contrasena)
            RETURNING clienteid
        ");
        $insertCliente->bindParam(':nombreCompleto', $nombre);
        $insertCliente->bindParam(':correo', $correo);
        $insertCliente->bindParam(':telefono', $telefono);
        $insertCliente->bindParam(':nombreUsuario', $nombreUsuario);
        $insertCliente->bindParam(':contrasena', $hashedPassword);
        $insertCliente->execute();

        $nuevoClienteID = $insertCliente->fetchColumn();

        // Insertar la dirección en la base de datos
        $insertDireccion = $pdo->prepare("
            INSERT INTO direcciones (clienteid, calle, ciudad, estado, codigopostal, pais, tipo)
            VALUES (:clienteID, :calle, :ciudad, :estado, :codigoPostal, :pais, :tipo)
        ");
        $insertDireccion->bindParam(':clienteID', $nuevoClienteID);
        $insertDireccion->bindParam(':calle', $calle);
        $insertDireccion->bindParam(':ciudad', $ciudad);
        $insertDireccion->bindParam(':estado', $estado);
        $insertDireccion->bindParam(':codigoPostal', $codigoPostal);
        $insertDireccion->bindParam(':pais', $pais);
        $insertDireccion->bindParam(':tipo', $tipo);
        $insertDireccion->execute();

        // Confirmar la transacción
        $pdo->commit();

        // Responder con éxito
        echo json_encode([
            "success" => true,
            "message" => "Registro exitoso. Ahora puedes iniciar sesión.",
            "clienteID" => $nuevoClienteID
        ]);
    } catch (PDOException $e) {
        $pdo->rollBack();
        http_response_code(500);
        echo json_encode(["error" => "Error del servidor: " . $e->getMessage()]);
    }
} else {
    http_response_code(405);
    echo json_encode(["error" => "Método no permitido."]);
}
?>

