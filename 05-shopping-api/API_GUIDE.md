# Shopping Mall API - Usage Guide

This guide provides detailed examples of how to use the Shopping Mall API.

## Base URL

- **Local**: `http://localhost:8080`
- **Production**: `https://your-app-name.herokuapp.com`

## API Documentation

Interactive API documentation is available at:

- **Swagger UI**: `/swagger-ui.html`
- **OpenAPI JSON**: `/api-docs`

## Authentication

Currently, this API does not implement authentication. In a production environment, you should add authentication (e.g., JWT, OAuth2).

## Common Headers

```
Content-Type: application/json
Accept: application/json
```

## API Examples

### 1. Category Management

#### Get All Categories

```http
GET /api/categories
```

**Response:**

```json
[
  {
    "id": 1,
    "name": "Electronics",
    "description": "Electronic devices and accessories",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  }
]
```

#### Create Category

```http
POST /api/categories
Content-Type: application/json

{
  "name": "Sports",
  "description": "Sports equipment and gear"
}
```

### 2. Product Management

#### Get All Products (Paginated)

```http
GET /api/products?page=0&size=10&sort=name,asc
```

**Response:**

```json
{
  "content": [
    {
      "id": 1,
      "name": "Laptop Pro 15",
      "description": "High-performance laptop",
      "price": 1299.99,
      "stockQuantity": 50,
      "imageUrl": "https://example.com/laptop.jpg",
      "categoryId": 1,
      "categoryName": "Electronics",
      "isActive": true,
      "createdAt": "2024-01-01T10:00:00",
      "updatedAt": "2024-01-01T10:00:00"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 50,
  "totalPages": 5
}
```

#### Search Products

```http
GET /api/products/search?keyword=laptop&page=0&size=10
```

#### Create Product

```http
POST /api/products
Content-Type: application/json

{
  "name": "Wireless Mouse",
  "description": "Ergonomic wireless mouse with USB receiver",
  "price": 29.99,
  "stockQuantity": 100,
  "imageUrl": "https://example.com/mouse.jpg",
  "categoryId": 1,
  "isActive": true
}
```

#### Update Product

```http
PUT /api/products/1
Content-Type: application/json

{
  "name": "Laptop Pro 15 - Updated",
  "description": "High-performance laptop with upgraded specs",
  "price": 1399.99,
  "stockQuantity": 45,
  "imageUrl": "https://example.com/laptop-new.jpg",
  "categoryId": 1,
  "isActive": true
}
```

### 3. User Management

#### Create User

```http
POST /api/users
Content-Type: application/json

{
  "email": "customer@example.com",
  "name": "John Customer",
  "phone": "+1-555-1234",
  "address": "123 Shopping Street, City, Country"
}
```

#### Get User by Email

```http
GET /api/users/email/customer@example.com
```

### 4. Shopping Cart

#### Get User's Cart

```http
GET /api/carts/user/1
```

**Response:**

```json
{
  "id": 1,
  "userId": 1,
  "items": [
    {
      "id": 1,
      "productId": 1,
      "productName": "Laptop Pro 15",
      "price": 1299.99,
      "quantity": 2,
      "subtotal": 2599.98
    }
  ],
  "totalAmount": 2599.98
}
```

#### Add Item to Cart

```http
POST /api/carts/user/1/items
Content-Type: application/json

{
  "productId": 1,
  "quantity": 2
}
```

#### Update Cart Item Quantity

```http
PUT /api/carts/user/1/items/1?quantity=3
```

#### Remove Item from Cart

```http
DELETE /api/carts/user/1/items/1
```

#### Clear Cart

```http
DELETE /api/carts/user/1
```

### 5. Order Management

#### Create Order from Cart

```http
POST /api/orders
Content-Type: application/json

{
  "userId": 1,
  "shippingAddress": "123 Shopping Street, City, Country"
}
```

**Response:**

```json
{
  "id": 1,
  "orderNumber": "ORD20240101120000001",
  "userId": 1,
  "totalAmount": 2599.98,
  "status": "PENDING",
  "shippingAddress": "123 Shopping Street, City, Country",
  "items": [
    {
      "id": 1,
      "productId": 1,
      "productName": "Laptop Pro 15",
      "price": 1299.99,
      "quantity": 2,
      "subtotal": 2599.98
    }
  ],
  "createdAt": "2024-01-01T12:00:00",
  "updatedAt": "2024-01-01T12:00:00"
}
```

#### Get User's Orders

```http
GET /api/orders/user/1?page=0&size=10
```

#### Update Order Status

```http
PATCH /api/orders/1/status?status=CONFIRMED
```

Available statuses:

- `PENDING`
- `CONFIRMED`
- `PROCESSING`
- `SHIPPED`
- `DELIVERED`
- `CANCELLED`
- `REFUNDED`

#### Cancel Order

```http
DELETE /api/orders/1
```

### 6. Payment Processing

#### Create Payment

```http
POST /api/payments
Content-Type: application/json

{
  "orderId": 1,
  "method": "CREDIT_CARD",
  "paymentDetails": "Visa ending in 1234"
}
```

Available payment methods:

- `CREDIT_CARD`
- `DEBIT_CARD`
- `BANK_TRANSFER`
- `PAYPAL`
- `CASH_ON_DELIVERY`

**Response:**

```json
{
  "id": 1,
  "orderId": 1,
  "orderNumber": "ORD20240101120000001",
  "amount": 2599.98,
  "method": "CREDIT_CARD",
  "status": "PENDING",
  "transactionId": "TXN202401011200000010001",
  "paymentDetails": "Visa ending in 1234",
  "createdAt": "2024-01-01T12:05:00",
  "updatedAt": "2024-01-01T12:05:00"
}
```

#### Process Payment

```http
POST /api/payments/1/process
```

This simulates payment processing and updates the payment status to `COMPLETED` and the order status to `CONFIRMED`.

#### Refund Payment

```http
POST /api/payments/1/refund
```

## Error Responses

All errors follow a consistent format:

```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Product not found with id: 999",
  "path": "/api/products/999"
}
```

### Common HTTP Status Codes

- `200 OK` - Successful request
- `201 Created` - Resource created successfully
- `204 No Content` - Successful deletion
- `400 Bad Request` - Invalid request data
- `404 Not Found` - Resource not found
- `409 Conflict` - Duplicate resource
- `500 Internal Server Error` - Server error

## Pagination

List endpoints support pagination with the following query parameters:

- `page` - Page number (0-indexed, default: 0)
- `size` - Items per page (default: 20)
- `sort` - Sort field and direction (e.g., `name,asc`)

Example:

```http
GET /api/products?page=0&size=20&sort=price,desc
```

## Workflow Example

### Complete Shopping Flow

1. **Browse Products**

```http
GET /api/products/active
```

2. **Add Items to Cart**

```http
POST /api/carts/user/1/items
{
  "productId": 1,
  "quantity": 2
}
```

3. **View Cart**

```http
GET /api/carts/user/1
```

4. **Create Order**

```http
POST /api/orders
{
  "userId": 1,
  "shippingAddress": "123 Main St"
}
```

5. **Create Payment**

```http
POST /api/payments
{
  "orderId": 1,
  "method": "CREDIT_CARD"
}
```

6. **Process Payment**

```http
POST /api/payments/1/process
```

7. **Check Order Status**

```http
GET /api/orders/1
```

## Testing with cURL

### Create a User

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "name": "Test User",
    "phone": "+1-555-0000",
    "address": "Test Address"
  }'
```

### Add Item to Cart

```bash
curl -X POST http://localhost:8080/api/carts/user/1/items \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 1,
    "quantity": 2
  }'
```

## Tips and Best Practices

1. **Check Stock Availability**: Before adding items to cart, verify product stock
2. **Handle Cart Cleanup**: Clear cart after successful order creation
3. **Order Validation**: Validate order status before processing payments
4. **Error Handling**: Always check error responses for detailed information
5. **Pagination**: Use pagination for large datasets to improve performance

## Rate Limiting

Currently, there is no rate limiting implemented. For production, consider adding rate limiting to prevent abuse.

## Support

For issues or questions, please refer to the main README.md or contact the development team.
