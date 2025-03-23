# Online Book Store

An online bookstore API built with **Spring boot** featuring book browsing, shopping cart, checkout, and user
authentication.
The system also simulates payment processing and supports role-based access control.

## 1. Tech Stack ##

- **Java 17+(LTS)**
- **Spring Boot 3.x.x**
- **H2 Database** (Embedded, no separate installation needed)
- **Spring Security with JWT**
- **JUnit 5 + Mockito** (unit testing)
- **Maven** (build tool)
- **Git**

## 2. Local Setup & Installation ##

- **Clone and build:**

  `git clone https://github.com/musty-codified/online-book-store.git`

  `mvn clean install`

- **Configure environment:**  Configure `application-dev.yml` with your custom configurations

- **Run the Application:** `mvn spring-boot:run`

- **The Application will start on:** `http://localhost:8090`

---

## 3. API Endpoints ##

The REST API endpoints are prefixed with `/api/v1` due to the context-path setting.

### 3.1 User Auth APIs ###

- (POST) [Register new user](http://localhost:8090/api/v1/users) `/api/v1/users`
- (POST) [Authenticate & get JWT token](http://localhost:8090/api/v1/auth/login) `/api/v1/auth/login`

### 3.2 Book Management APIs ###

- (GET) [Search or list books](http://localhost:8090/api/v1/books)
  `/api/v1/books?pageNumber=1&pageSize=2&searchText=Hitchhiker`

### 3.3 Cart Management APIs ###

- (POST) [Add item to cart](http://localhost:8090/api/v1/carts/{userId}) `/api/v1/carts/1`
- (GET) [View cart contents](http://localhost:8090/api/v1/carts/{userId}) `/api/v1/carts/1`
- (DELETE) [Clear cart contents](http://localhost:8090/api/v1/carts/{userId}) `/api/v1/carts/1`

### 3.4 Order Management APIs ###

- (POST) [Checkout using payment method](http://localhost:8090/api/v1/orders/checkout) `/api/v1/orders/checkout`
- (GET) [View order history](http://localhost:8090/api/v1/orders/{userId}/history)
  `/api/v1/orders/1/history?pageNumber=1&pageSize=2`

### 3.5 API Authentication ###

- Secure endpoints require a **JWT token** for access.
- Obtain a token via the **User Login API** and pass in the request header:
  ```sh
  Authorization: Bearer <your-token-here>
  ```

---

## 4. Running Unit Tests ##

Unit testing covers key functionalities.
- **To run unit tests:** `mvn test`
- **The test suite uses JUnit 5 and Mockito**
- **Services tested include: UserService, CartService, OrderService, and BookService**

## 5. application.yml Configuration ##
Authorization is abstracted via configurable permissions in application.yml:
This configuration is automatically mapped using the Spring @ConfigurationProperties setup in the `PermissionConfig` class present under the `config` package:
```
access-control-list:
  permissions:
    - permission: "user.read"
      methods: ["GET"]
      patterns: ["/books/**", "/carts/**", "/orders/**"]

    - permission: "user.write"
      methods: ["POST", "PUT"]
      patterns: ["/carts/**", "/orders/**"]

    - permission: "user.delete"
      methods: [ "DELETE"]
      patterns: ["/carts/**"]

```
Below is a full sample of the application.yml:
```
spring:
  datasource:
    url: jdbc:h2:mem:book_store_db
    driver-class-name: org.h2.Driver
    username: sa
    password: password
  h2:
    console:
      enabled: true
  jpa:
    hibernate:
      ddl-auto: update
    database-platform: org.hibernate.dialect.H2Dialect
    
server:
  servlet:
    context-path: /api/v1
  port: 8090    

jwt:
  secret: ${JWT_SECRET:eab7efd5799142acb5f308ba2e54cdfa589bfc369f8c9a7bfe53ddab4f5421ce}
  expiration: 3600 # 1 hour

access-control-list:
  permissions:
    - permission: "user.read"
      methods: ["GET"]
      patterns: ["/books/**", "/carts/**", "/orders/**"]

    - permission: "user.write"
      methods: ["POST", "PUT"]
      patterns: ["/carts/**", "/orders/**"]

    - permission: "user.delete"
      methods: [ "DELETE"]
      patterns: ["/carts/**"]

```
Access H2 console at:

- **URL:** `http://localhost:8090/api/v1/h2-console`
- **JDBC URL:** `jdbc:h2:mem:book_store_db`
- **Username:** `sa`
- **Password:** `<your password>`

## 6. Order Checkout Sample Payloads ##

### Request

```
  {
    "userId": 1,
    "paymentMethod": "TRANSFER"
  }
```
### Response

```
  {
    "success": true,
    "message": "Request successfully processed",
    "timestamp": "2025-03-22T11:32:04+0000",
    "data": {
        "userId": 1,
        "grandTotal": 147.0,
        "orderStatus": "PENDING",
        "orderItemsDto": [
            {
                "bookId": 3,
                "orderId": 1,
                "quantity": 3,
                "unitPrice": 39.0
            },
            {
                "bookId": 7,
                "orderId": 1,
                "quantity": 1,
                "unitPrice": 30.0
            }
        ]
    }
```




