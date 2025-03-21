# Online Book Store

An online Book Store API developed with **Spring boot** and secured using **JWT-based authentication**. Users can purchase available books from the store.

## 1. Tech Stack ##

- **Java 17** 
- **Spring Boot 3.4.3** 
- **H2 Database** - (Embedded, no separate installation needed)
- **JWT** - Authentication Mechanism


## 2. Project Structure ##

```
online-book-store/
│──config (Security config)
│──controller (Controller layer)
│──dto (data transfer objects)
│──entity (models of the application)
│──enums ( enum objects)
│──exception (exception handling class)
│──repository (DAOs or data access objects)
│──service (business logic )
│──util (utility class)

```
---

## 3. Local Setup & Installation ##

- **Clone the repository:**
  `git clone https://github.com/musty-codified/online-book-store.git`
- **Build and download dependencies using maven:** `mvn clean install`

- **Configure environment:** Open and configure `application-dev.yml` with your H2 Database configurations.

- **Start the server:** `mvn spring-boot:run`

- The server will start on **`http://localhost:8090`**.

---



