# Use the official PHP image with Apache
FROM php:7.4-apache

# Copy your application files to the container
COPY . /var/www/html/

# Optional: install any additional PHP extensions or dependencies
# RUN docker-php-ext-install mysqli pdo pdo_mysql
