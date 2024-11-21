<?php
// Database connection settings
$host = 'postgres'; 
$dbname = getenv('POSTGRES_DB'); 
$user = getenv('POSTGRES_USER'); 
$pass = getenv('POSTGRES_PASSWORD'); 
$port = '5432'; 

try {
    $dsn = "pgsql:host=$host;port=$port;dbname=$dbname";
    $pdo = new PDO($dsn, $user, $pass);

    // Set PDO error mode to exception
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    error_log("Database connection error: " . $e->getMessage()); // Log error
    die(json_encode(["error" => "Error connecting to the database."])); // Avoid exposing details to users
}

// Set response headers
header('Content-Type: application/json');

// Check if the request is a POST request
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Retrieve the JSON payload
    $data = json_decode(file_get_contents('php://input'), true);

    // Validate input
    if (!isset($data['nombreUsuario']) || !isset($data['contrasena'])) {
        http_response_code(400);
        echo json_encode(["error" => "Missing username or password."]);
        exit;
    }

    // Sanitize input
    $nombreUsuario = filter_var($data['nombreUsuario'], FILTER_SANITIZE_STRING);
    $contrasena = $data['contrasena'];

    try {
        // Prepare the SQL query with case-insensitive username matching
        $stmt = $pdo->prepare("SELECT clienteid, nombreusuario, contrasena FROM Clientes WHERE LOWER(nombreusuario) = LOWER(:nombreUsuario)");
        $stmt->bindParam(':nombreUsuario', $nombreUsuario);
        $stmt->execute();

        $user = $stmt->fetch(PDO::FETCH_ASSOC);

        // Check if user exists and verify the password
        if ($user && password_verify($contrasena, $user['contrasena'])) {
            // Determine the user's role
            $role = (strtolower($user['nombreusuario']) === 'admin') ? 'admin' : 'user';

            echo json_encode([
                "success" => true,
                "message" => "Login successful.",
                "user" => [
                    "id" => $user['clienteid'],
                    "username" => $user['nombreusuario'],
                    "role" => $role // Include user role for client handling
                ]
            ]);
        } else {
            http_response_code(401);
            echo json_encode(["error" => "Invalid username or password."]);
        }
    } catch (PDOException $e) {
        error_log("Query error: " . $e->getMessage()); // Log detailed error
        http_response_code(500);
        echo json_encode(["error" => "An unexpected server error occurred."]);
    }
} else {
    http_response_code(405);
    echo json_encode(["error" => "Method not allowed."]);
}
?>
