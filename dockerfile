# Stage 1: Build Angular app with Node.js 18
FROM node:18 as build

WORKDIR /app

# Copy package.json and package-lock.json to install dependencies first
COPY package*.json ./

# Install Angular dependencies
RUN npm install

# Copy the rest of the Angular project files
COPY . .

# Build the Angular app with the correct base href
RUN npx ng build --base-href /browser/

# Stage 2: Setup PHP-Apache server
FROM php:7.4-apache

# Install necessary PHP extensions and Apache modules
RUN apt-get update && apt-get install -y libpq-dev unzip curl \
    && docker-php-ext-install pdo pdo_pgsql pgsql \
    && a2enmod rewrite

# Install Composer
RUN curl -sS https://getcomposer.org/installer | php -- --install-dir=/usr/local/bin --filename=composer

# Set environment variable for Stripe Secret Key
ENV STRIPE_SECRET_KEY=sk_test_51QFIvsEpXV9Cp1v5not64nJtBJUIHvlEemlf6TlKmECGhMKlWCg0IT0Ehy4yUWeZbBrAlcAW1rLj2FHIdUiimlnY00NbO1Xhbq

# Set the working directory for the PHP backend
WORKDIR /var/www/html/backend

# Copy all PHP backend files to the container
COPY php/ /var/www/html/backend/

# Install PHP dependencies (including Stripe PHP SDK)
RUN composer install --no-dev --optimize-autoloader \
    && ls -al /var/www/html/backend

# Set the working directory for the Apache server root
WORKDIR /var/www/html

# Create the directory for the Angular app
RUN mkdir -p /var/www/html/browser/

# Copy Angular build files from Stage 1
COPY --from=build /app/dist/web/browser/ /var/www/html/browser/

# Copy the Apache configuration
COPY apache-config.conf /etc/apache2/sites-available/000-default.conf

# Set proper permissions
RUN chown -R www-data:www-data /var/www/html

RUN composer --version

# Expose port 80
EXPOSE 80

# Start Apache in the foreground
CMD ["apache2-foreground"]
