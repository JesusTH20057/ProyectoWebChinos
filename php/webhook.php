<?php
require 'vendor/autoload.php';

\Stripe\Stripe::setApiKey(getenv('STRIPE_SECRET_KEY'));

$payload = @file_get_contents('php://input');
$sig_header = $_SERVER['HTTP_STRIPE_SIGNATURE'];
$endpoint_secret = 'your_webhook_secret'; // Replace with your actual webhook secret

try {
    $event = \Stripe\Webhook::constructEvent(
        $payload, $sig_header, $endpoint_secret
    );

    // Handle the event
    switch ($event->type) {
        case 'checkout.session.completed':
            $session = $event->data->object;
            // Fulfill the purchase, e.g., update your database
            break;
        // Add more cases as needed
        default:
            // Unexpected event type
            http_response_code(400);
            exit();
    }

    http_response_code(200);
} catch(\UnexpectedValueException $e) {
    // Invalid payload
    http_response_code(400);
    exit();
} catch(\Stripe\Exception\SignatureVerificationException $e) {
    // Invalid signature
    http_response_code(400);
    exit();
}
?>

<!-- <script src="https://js.stripe.com/v3/"></script> -->
<!--  -->npm install @stripe/stripe-js
