# Shopping Mall API

A comprehensive RESTful API service for shopping mall product and order management system built with Spring Boot.

## Features

- **Product Management**: Complete CRUD operations for products
- **Category Management**: Organize products into categories
- **Shopping Cart**: Add, update, and remove items from cart
- **Order Processing**: Create orders from cart, track order status
- **Payment Integration**: Payment processing and refund support
- **User Management**: User registration and profile management

## Technology Stack

- **Java**: 17 (LTS)
- **Spring Boot**: 3.2.5
- **Database**: PostgreSQL 16+
- **ORM**: Spring Data JPA
- **Build Tool**: Maven 3.9.x
- **API Documentation**: SpringDoc OpenAPI (Swagger)
- **Deployment**: Heroku

## Prerequisites

- Java 17 or higher
- Maven 3.9.x or higher
- PostgreSQL 16 or higher

## Local Development Setup

### 1. Clone the repository

```bash
git clone <repository-url>
cd shopping-api
```

### 2. Configure PostgreSQL

Create a PostgreSQL database:

```sql
CREATE DATABASE shopping_db;
```

### 3. Configure Application Properties

Update `src/main/resources/application.yml` with your PostgreSQL credentials:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/shopping_db
    username: your_username
    password: your_password
```

### 4. Build the Application

```bash
mvn clean install
```

### 5. Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Documentation

Once the application is running, you can access the Swagger UI at:

```
http://localhost:8080/swagger-ui.html
```

API documentation is also available in JSON format at:

```
http://localhost:8080/api-docs
```

## API Endpoints

### Categories

- `GET /api/categories` - Get all categories
- `GET /api/categories/{id}` - Get category by ID
- `POST /api/categories` - Create new category
- `PUT /api/categories/{id}` - Update category
- `DELETE /api/categories/{id}` - Delete category

### Products

- `GET /api/products` - Get all products (paginated)
- `GET /api/products/{id}` - Get product by ID
- `GET /api/products/category/{categoryId}` - Get products by category
- `GET /api/products/search?keyword={keyword}` - Search products
- `POST /api/products` - Create new product
- `PUT /api/products/{id}` - Update product
- `DELETE /api/products/{id}` - Delete product

### Users

- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/email/{email}` - Get user by email
- `POST /api/users` - Create new user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Shopping Cart

- `GET /api/carts/user/{userId}` - Get user's cart
- `POST /api/carts/user/{userId}/items` - Add item to cart
- `PUT /api/carts/user/{userId}/items/{cartItemId}` - Update cart item quantity
- `DELETE /api/carts/user/{userId}/items/{cartItemId}` - Remove item from cart
- `DELETE /api/carts/user/{userId}` - Clear cart

### Orders

- `GET /api/orders` - Get all orders
- `GET /api/orders/{id}` - Get order by ID
- `GET /api/orders/number/{orderNumber}` - Get order by order number
- `GET /api/orders/user/{userId}` - Get orders by user
- `POST /api/orders` - Create order from cart
- `PATCH /api/orders/{id}/status` - Update order status
- `DELETE /api/orders/{id}` - Cancel order

### Payments

- `GET /api/payments` - Get all payments
- `GET /api/payments/{id}` - Get payment by ID
- `GET /api/payments/order/{orderId}` - Get payment by order ID
- `POST /api/payments` - Create payment
- `POST /api/payments/{id}/process` - Process payment
- `PATCH /api/payments/{id}/status` - Update payment status
- `POST /api/payments/{id}/refund` - Refund payment

## Heroku Deployment

### 1. Create a Heroku Account

Sign up at [heroku.com](https://www.heroku.com/)

### 2. Install Heroku CLI

Download and install the [Heroku CLI](https://devcenter.heroku.com/articles/heroku-cli)

### 3. Login to Heroku

```bash
heroku login
```

### 4. Create a Heroku App

```bash
heroku create your-app-name
```

### 5. Add PostgreSQL Add-on

```bash
heroku addons:create heroku-postgresql:essential-0
```

### 6. Configure Environment Variables

The PostgreSQL database URL will be automatically set by Heroku as `DATABASE_URL`. The application is configured to use this.

### 7. Deploy to Heroku

```bash
git push heroku main
```

### 8. Access Your Application

```
https://your-app-name.herokuapp.com/swagger-ui.html
```

## Database Schema

The application uses the following main entities:

- **Category**: Product categories
- **Product**: Product information and inventory
- **User**: User accounts
- **Cart**: Shopping carts for users
- **CartItem**: Items in shopping carts
- **Order**: Order information
- **OrderItem**: Items in orders
- **Payment**: Payment transactions

All entities include automatic timestamp tracking (created_at, updated_at).

## Error Handling

The API uses standard HTTP status codes and returns error responses in the following format:

```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Product not found with id: 123",
  "path": "/api/products/123"
}
```

## Validation

Request bodies are validated using Jakarta Bean Validation. Invalid requests return a 400 Bad Request with detailed validation errors.

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License.

## Contact

For questions or support, please contact the development team.
