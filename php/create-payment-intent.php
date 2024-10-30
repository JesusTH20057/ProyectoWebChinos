<?php
// php/backend/create-payment-intent.php

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);


require '/var/www/html/backend/vendor/autoload.php';

use Stripe\Stripe;
use Stripe\PaymentIntent;

// Configurar las cabeceras para CORS
header('Content-Type: application/json');
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Authorization");

// Manejar solicitudes OPTIONS para CORS preflight
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    exit();
}

// Obtener el cuerpo de la solicitud
$input = json_decode(file_get_contents('php://input'), true);

// Validar los parámetros necesarios
if (!isset($input['amount']) || !isset($input['currency'])) {
    http_response_code(400);
    echo json_encode(['error' => 'Missing required parameters.']);
    exit();
}

$amount = intval($input['amount']); // Cantidad en centavos
$currency = strtolower($input['currency']); // Ejemplo: 'usd'

// Configurar la clave secreta de Stripe desde una variable de entorno
$stripeSecretKey = getenv('STRIPE_SECRET_KEY');
if (!$stripeSecretKey) {
    http_response_code(500);
    echo json_encode(['error' => 'Stripe secret key not configured.']);
    exit();
}

Stripe::setApiKey($stripeSecretKey);

try {
    // Crear el PaymentIntent
    $paymentIntent = PaymentIntent::create([
        'amount' => $amount,
        'currency' => $currency,
        // Puedes agregar más parámetros como 'metadata', 'description', etc.
    ]);

    // Devolver el clientSecret al frontend
    echo json_encode([
        'clientSecret' => $paymentIntent->client_secret,
    ]);
} catch (\Stripe\Exception\ApiErrorException $e) {
    http_response_code(500);
    echo json_encode(['error' => $e->getMessage()]);
}
?>
