<?php

// Verificar si la solicitud es POST
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $data = json_decode(file_get_contents('php://input'), true);

    // Validar los datos de entrada
    if (!isset($data['token']) || !isset($data['nuevaContrasena'])) {
        http_response_code(400);
        echo json_encode(["error" => "Faltan campos obligatorios."]);
        exit;
    }

    $token = $data['token'];
    $nuevaContrasena = $data['nuevaContrasena'];

    try {
        // Verificar el token
        $stmt = $pdo->prepare("
            SELECT clienteid 
            FROM password_resets 
            WHERE token = :token AND fecha_expiracion > NOW()
        ");
        $stmt->bindParam(':token', $token);
        $stmt->execute();

        $usuario = $stmt->fetch(PDO::FETCH_ASSOC);

        if ($usuario) {
            // Actualizar la contraseña
            $hashedPassword = password_hash($nuevaContrasena, PASSWORD_BCRYPT);
            $stmtUpdate = $pdo->prepare("
                UPDATE clientes 
                SET contrasena = :contrasena 
                WHERE clienteid = :clienteid
            ");
            $stmtUpdate->bindParam(':contrasena', $hashedPassword);
            $stmtUpdate->bindParam(':clienteid', $usuario['clienteid']);
            $stmtUpdate->execute();

            // Eliminar el token después de usarlo
            $stmtDelete = $pdo->prepare("DELETE FROM password_resets WHERE token = :token");
            $stmtDelete->bindParam(':token', $token);
            $stmtDelete->execute();

            echo json_encode(["success" => true, "message" => "Contraseña actualizada correctamente."]);
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
