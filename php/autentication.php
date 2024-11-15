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
    die("Error connecting to database: " . $e->getMessage());
}

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

    $nombreUsuario = $data['nombreUsuario'];
    $contrasena = $data['contrasena'];

    try {
        // Prepare the SQL query, ensuring case-insensitive match for column names
        $stmt = $pdo->prepare("SELECT * FROM Clientes WHERE nombreusuario = :nombreUsuario");
        $stmt->bindParam(':nombreUsuario', $nombreUsuario);
        $stmt->execute();

        $user = $stmt->fetch(PDO::FETCH_ASSOC);

        // Check if user exists and verify the password
        if ($user && password_verify($contrasena, $user['contrasena'])) {
            echo json_encode([
                "success" => true,
                "message" => "Login successful.",
                "user" => [
                    "id" => $user['clienteid'],
                    "username" => $user['nombreusuario']
                ]
            ]);
        } else {
            http_response_code(401);
            echo json_encode(["error" => "Invalid username or password."]);
        }
    } catch (PDOException $e) {
        http_response_code(500);
        echo json_encode(["error" => "Server error: " . $e->getMessage()]);
    }
} else {
    http_response_code(405);
    echo json_encode(["error" => "Method not allowed."]);
}
?>
