# Use the official PHP image with Apache
FROM php:7.4-apache

# Install necessary PHP extensions and Apache modules
RUN apt-get update && apt-get install -y libpq-dev git unzip \
    && docker-php-ext-install pdo pdo_pgsql pgsql \
    && a2enmod rewrite

# Install Composer
COPY --from=composer:latest /usr/bin/composer /usr/bin/composer

# Set the working directory
WORKDIR /var/www/html

# Copy Angular build files to the container
COPY dist/web/ /var/www/html/

# Copy PHP backend files to the container
COPY php/ /var/www/html/backend/

# Install PHP dependencies including Stripe SDK
WORKDIR /var/www/html/backend
RUN composer install

# Copy the custom Apache configuration
COPY apache-config.conf /etc/apache2/sites-available/000-default.conf

# Set proper permissions
RUN chown -R www-data:www-data /var/www/html

# Expose port 80 (optional, since it's already mapped in docker-compose)
EXPOSE 80

# Start Apache in the foreground
CMD ["apache2-foreground"]
