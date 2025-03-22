# Online Book Store

An online bookstore API built with **Spring boot** and secured using **JWT-based authentication**. The API featuring book browsing, shopping cart, checkout, and user authentication. 
The system also simulates payment processing and supports role-based access control.

## 1. Tech Stack ##

- **Java 17+(LTS)** 
- **Spring Boot 3.x.x** 
- **H2 Database** - (Embedded, no separate installation needed)
- **Spring Security with JWT** 
- **JUnit 5 + Mockito** - (unit testing)
- **Maven** (build tool)


## 2. Local Setup & Installation ##
- **Ensure you have the following installed**:

- **Java 17+**
- **Maven 3.8+**
- **Git**

- **Clone and build:**
  `git clone https://github.com/musty-codified/online-book-store.git`
  `mvn clean install`

- **Configure environment:** Configure `application-dev.yml` with your H2 Database configurations.

- **Run the Application:** `mvn spring-boot:run`

- **The Application will start on:** `http://localhost:8090`.

---

## 3. API Endpoints ##

The REST API endpoints are prefixed with `/api/v1` due to the context-path setting.

### 3.1 User Auth APIs ###

- (POST) [Register new user](http://localhost:8090/api/v1/users) `/api/v1/users`
- (POST) [Authenticate & get JWT token](http://localhost:8090/api/v1/auth/login) `/api/v1/auth/login`

### 3.2 Book Management APIs ###

- (GET) [Search or list books](http://localhost:8090/api/v1/books) `/api/v1/books?pageNumber=1&pageSize=2&searchText=Hitchhiker`

### 3.3 Cart Management APIs ###

- (POST) [Add item to cart](http://localhost:8090/api/v1/carts/{userId}) `/api/v1/carts/1/`
- (GET) [View cart contents](http://localhost:8090/api/v1/carts/{userId}) `/api/v1/carts/1`
- (DELETE) [Clear cart contents](http://localhost:8090/api/v1/carts/{userId}) `/api/v1/carts/1`

### 3.4 Order Management APIs ###

- (POST) [Checkout using payment method](http://localhost:8090/api/v1/orders/checkout) `/api/v1/orders/checkout/`
- (GET) [View order history](http://localhost:8090/api/v1/orders/{userId}/history) `/api/v1/orders/1/history?pageNumber=1&pageSize=2`

### 3.5 API Authentication

- Secure endpoints require a **JWT token** for access.
- Obtain a token via the **User Login API** and pass in the request header:
  ```sh
  Authorization: Bearer <your-token-here>
  ```
---

## 6.1 Database Access ##

Access H2 console at:

- **URL:** `http://localhost:8090/api/v1/h2-console`
- **JDBC URL:** `jdbc:h2:mem:book_store_db`
- **Username:** `sa`
- **Password:** ``





