<?php
$password = 'admin'; 
$hash = '$2y$12$2YGO9InljsT5kY6FAA8RMe2ZOMDLUVbjCL9kBWvDk.MEVNJpzRM9u'; 

if (password_verify($password, $hash)) {
    echo "Password is correct!";
} else {
    echo "Invalid password.";
}
?>
