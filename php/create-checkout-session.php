<?php
require 'vendor/autoload.php';

\Stripe\Stripe::setApiKey(getenv('STRIPE_SECRET_KEY'));

// Set your success and cancel URLs
$YOUR_DOMAIN = 'http://localhost:8082';

header('Content-Type: application/json');

try {
    $checkout_session = \Stripe\Checkout\Session::create([
        'payment_method_types' => ['card'],
        'line_items' => [[
            'price_data' => [
                'currency' => 'usd',
                'product_data' => [
                    'name' => 'Gym Membership',
                ],
                'unit_amount' => 5000, // Amount in cents ($50.00)
            ],
            'quantity' => 1,
        ]],
        'mode' => 'payment',
        'success_url' => $YOUR_DOMAIN . '/success.html',
        'cancel_url' => $YOUR_DOMAIN . '/cancel.html',
    ]);

    echo json_encode(['id' => $checkout_session->id]);
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode(['error' => $e->getMessage()]);
}
?>
