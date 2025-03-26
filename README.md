# Online Book Store

An online bookstore API built with **Spring boot** featuring book browsing, shopping cart, checkout, and user
authentication.
The system also simulates payment processing and supports role-based access control.

## 1. Tech Stack ##

- **Java 17+(LTS)**
- **Spring Boot  3.x.x**
- **H2 Database** (Embedded, no separate installation needed)
- **Spring Security with JWT**
- **JUnit 5 + Mockito** (unit testing)
- **Maven** (build tool)
- **Git** 

## 2. Local Setup & Installation ##

- **Clone and build:**

  `git clone https://github.com/musty-codified/online-book-store.git`

  `mvn clean install`

- **Configure environment:**  Configure `application.yml` with your custom configurations

- **Run the Application:** `mvn spring-boot:run`

- **The Application will start on:** `http://localhost:8090`

---

## 3. API Endpoints ##

The REST API endpoints are prefixed with `/api/v1` due to the context-path setting.

### 3.1 User Auth APIs ###

- (POST)  `/api/v1/users` Register new user
- (POST)  `/api/v1/auth/login` Authenticate & get JWT token

### 3.2 Book Management API ###

- (GET) `/api/v1/books?pageNumber=1&pageSize=2&searchText=Hitchhiker` Search or list books

### 3.3 Cart Management APIs ###

- (POST)  `/api/v1/carts/{userId}` Add item to cart
- (GET)  `/api/v1/carts/{userId}` View cart contents
- (DELETE)  `/api/v1/carts/{userId}` Clear cart contents

### 3.4 Order Management APIs ###

- (POST)  `/api/v1/orders/checkout` Checkout using payment method
- (GET)  `/api/v1/orders/{userId}/history?pageNumber=1&pageSize=2` View order history


### 3.5 API Authentication ###

- Secure endpoints require a **JWT token** for access.
- Obtain a token via the **User Login endpoint** and pass it in the request header:
  ```sh
  Authorization: Bearer <your-token-here>
  ```
- Each request goes through a **JWT filter** that validates token & sets security context
---

## 4. Running Unit Tests ##

Unittests covers key functionalities.
- **To run unit tests:** `mvn test`
- **The test suite uses JUnit 5 and Mockito**
- **Services tested include: UserService, CartService, OrderService, and BookService**

## 5. Configuration ##

All app settings are in `application.yml` including Authorization permissions.
This auth configuration is mapped using Spring @ConfigurationProperties setup in `PermissionConfig` class, 
which is present under the `config` package:

 application.yml:
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
    context-path: /api/v1  # Important: Setting the context path
  port: 8090  # Or any other port  

jwt:
  secret: your_secret_key_here  # IMPORTANT: Change this in production!  Use a strong, randomly generated secret.
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




