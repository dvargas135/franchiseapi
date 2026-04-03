# Franchise API

Reactive REST API built with Spring Boot for managing franchises, branches, and products.

## Overview

This project was developed as a technical assessment. It provides endpoints to:

- create a franchise
- add branches to a franchise
- add products to a branch
- delete products from a branch
- update product stock
- retrieve the product with the highest stock per branch for a given franchise

It also includes bonus features such as:

- update franchise name
- update branch name
- update product name
- JWT authentication
- Docker support
- Swagger / OpenAPI documentation
- integration testing with Testcontainers

---

## Tech Stack

- Java 21
- Spring Boot 4.0.5
- Spring WebFlux
- Spring Data R2DBC
- MySQL
- Spring Security
- JWT
- Swagger / OpenAPI
- Docker + Docker Compose
- Testcontainers
- JUnit 5

---

## Architecture

The application follows a layered architecture with feature-based packaging:

```text
com.dan.franchiseapi
├── auth
├── branch
├── common
├── config
├── franchise
├── product
└── report
```

Each feature contains its own:

- controller
- dto
- mapper
- model
- repository
- service

---

## Domain Model

### Franchise
A franchise contains:

- `id`
- `name`

### Branch
A branch contains:

- `id`
- `name`
- `franchiseId`

### Product
A product contains:

- `id`
- `name`
- `stock`
- `branchId`

### AppUser
Used for authentication:

- `id`
- `username`
- `password`
- `role`

---

## Database Schema

The application uses MySQL with the following tables:

- `franchises`
- `branches`
- `products`
- `app_users`

### Constraints

- franchise name must be unique globally
- branch name must be unique within a franchise
- product name must be unique within a branch
- product stock must be greater than or equal to 0

---

## API Endpoints

## Authentication

### Login
`POST /api/auth/login`

Request:
```json
{
  "username": "admin",
  "password": "admin123"
}
```

Response:
```json
{
  "token": "your-jwt-token"
}
```

---

## Franchise Endpoints

### Create franchise
`POST /api/franchises`

Request:
```json
{
  "name": "McDonalds"
}
```

### Update franchise name
`PATCH /api/franchises/{franchiseId}/name`

Request:
```json
{
  "name": "Burger King"
}
```

---

## Branch Endpoints

### Add branch to franchise
`POST /api/franchises/{franchiseId}/branches`

Request:
```json
{
  "name": "Downtown Branch"
}
```

### Update branch name
`PATCH /api/branches/{branchId}/name`

Request:
```json
{
  "name": "Central Branch"
}
```

---

## Product Endpoints

### Add product to branch
`POST /api/branches/{branchId}/products`

Request:
```json
{
  "name": "Big Mac",
  "stock": 100
}
```

### Delete product from branch
`DELETE /api/branches/{branchId}/products/{productId}`

### Update product stock
`PATCH /api/products/{productId}/stock`

Request:
```json
{
  "stock": 120
}
```

### Update product name
`PATCH /api/products/{productId}/name`

Request:
```json
{
  "name": "Big Mac Combo"
}
```

---

## Report Endpoint

### Get top-stock product per branch for a franchise
`GET /api/franchises/{franchiseId}/top-stock-products`

Response:
```json
[
  {
    "branchId": 1,
    "branchName": "Downtown Branch",
    "productId": 10,
    "productName": "Big Mac",
    "stock": 120
  }
]
```

If multiple products in the same branch share the same highest stock, all matching products may be returned.

---

## Authentication

All business endpoints are protected with JWT authentication.

### Default seeded user

- username: `admin`
- password: `admin123`

### Using the token

Include the token in the `Authorization` header:

```http
Authorization: Bearer your-jwt-token
```

---

## Running the Project with Docker

## Prerequisites

- Docker
- Docker Compose

## Start the application

```bash
docker compose up --build
```

The API will be available at:

- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`

## Stop the application

```bash
docker compose down
```

## Recreate the database from scratch

```bash
docker compose down -v
docker compose up --build
```

---

## Local Test Execution

Integration tests use Testcontainers, so Docker must be running.

Run tests with:

```bash
./mvnw test
```

---

## Swagger / OpenAPI

Swagger UI is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

You can authenticate in Swagger by:

1. calling `/api/auth/login`
2. copying the returned JWT
3. clicking the **Authorize** button
4. pasting:

```text
Bearer your-jwt-token
```

---

## Example cURL Flow

## 1. Login

```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' | sed -E 's/.*"token":"([^"]+)".*/\1/')
```

## 2. Create franchise

```bash
curl -X POST http://localhost:8080/api/franchises \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"name":"McDonalds"}'
```

## 3. Add branch

```bash
curl -X POST http://localhost:8080/api/franchises/1/branches \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"name":"Downtown Branch"}'
```

## 4. Add product

```bash
curl -X POST http://localhost:8080/api/branches/1/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"name":"Big Mac","stock":100}'
```

## 5. Update stock

```bash
curl -X PATCH http://localhost:8080/api/products/1/stock \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"stock":120}'
```

## 6. Get report

```bash
curl -X GET http://localhost:8080/api/franchises/1/top-stock-products \
  -H "Authorization: Bearer $TOKEN"
```

---

## Validation and Error Handling

The API includes centralized exception handling.

Common responses:

- `400 Bad Request` for validation errors
- `401 Unauthorized` for missing or invalid JWT
- `404 Not Found` when an entity does not exist
- `409 Conflict` for duplicate names
- `500 Internal Server Error` for unexpected errors

Example error response:

```json
{
  "timestamp": "2026-04-03T06:55:41.882564749Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Franchise name is required",
  "path": "/api/franchises"
}
```

---

## Testing

The project includes integration tests using:

- Spring Boot Test
- WebTestClient
- Testcontainers
- MySQL container

Current test coverage includes:

- successful login
- unauthorized access to protected endpoint
- successful authenticated franchise creation

---

## Security Notes

This project seeds a default admin user for demonstration purposes.

For a production environment, the following should be improved:

- externalize secrets securely
- rotate JWT secret and use environment variables
- disable default test credentials
- restrict Swagger in production
- add role-based authorization if needed

---

# Terraform scaffold for AWS deployment

This Terraform scaffold provisions a minimal AWS deployment path for the Franchise API:

- Amazon RDS MySQL instance
- AWS Secrets Manager secrets for the database password and JWT secret
- AWS App Runner service that runs the Docker image from Amazon ECR
- AWS App Runner VPC connector so the service can reach the private RDS instance
- IAM roles for ECR image access and secret access
- Security groups and subnet group

## Important notes

1. This scaffold assumes you have already built and pushed the application image to Amazon ECR.
2. It uses the default VPC and default subnets in the selected AWS region to keep the example short.
3. The application is not "already deployed" just because it has a Dockerfile. Terraform plus a real apply step is what turns it into cloud infrastructure.
4. The App Runner health check is pointed at `/swagger-ui/index.html` because that endpoint is available in the current app. You can change it later to a dedicated health endpoint.

## Suggested workflow

```bash
terraform init
terraform plan -var-file="terraform.tfvars"
terraform apply -var-file="terraform.tfvars"
```

## Suggested follow-up improvements

- Replace default VPC usage with dedicated VPC, private subnets, and NAT
- Add a proper `/actuator/health` endpoint and point the App Runner health check to it
- Store additional app configuration in AWS Systems Manager Parameter Store
- Add Route 53, ACM, and a custom domain
- Add Terraform remote state

---

## Author

Dan V.
