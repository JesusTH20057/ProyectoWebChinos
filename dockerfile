# Dockerfile

# Use the official PHP image with Apache
FROM php:7.4-apache

# Install necessary PHP extensions and Apache modules
RUN apt-get update && apt-get install -y libpq-dev \
    && docker-php-ext-install pdo pdo_pgsql pgsql \
    && a2enmod rewrite

# Set the working directory
WORKDIR /var/www/html

# Copy Angular build files to the container
COPY dist/web/ /var/www/html/

# Copy PHP backend files to the container
COPY php/ /var/www/html/backend/

# Copy the custom Apache configuration
COPY apache-config.conf /etc/apache2/sites-available/000-default.conf

# Set proper permissions
RUN chown -R www-data:www-data /var/www/html

# Start Apache in the foreground
CMD ["apache2-foreground"]
