# 📖 BookShop API 
This API is designed to manage an online bookstore, offering features such as book catalog management, shopping carts, order processing, user registration, JWT authentication, and role-based access control for users and administrators. It is an ideal solution for individuals or businesses looking to create an efficient and scalable online bookstore with streamlined order and inventory management.
## 📌 Technologies & Tools
- **Core Language**: Java 17
- **Framework**: Spring Boot (with Spring Web, Spring Data JPA, Spring Security)
- **Database**: MySQL (with Liquibase for schema migrations)
- **Testing**: JUnit, MockMvc
- **API Documentation**: Swagger
- **API Testing**: Postman
- **Dependency Management**: Maven
- **Containerization**: Docker, Docker Compose
- **Object Mapping**: MapStruct
## ⚡ Functionality
The project provides a comprehensive set of features for managing users, books, categories, orders, and the shopping cart. Below is a breakdown of the main functionalities:

**👤 User Management** (`AuthController`)
- User registration and authentication.
- Secure login with validation.
- Managing user roles and permissions.
  
**📚 Book Management** (`BookController`)
- Adding new books with details (title, author, price, description, etc.).
- Editing and deleting book entries.
- Searching for books by title, author, category, or keywords.
- Retrieving detailed information about a specific book.
  
**🏷 Category Management** (`CategoryController`)
- Creating and organizing book categories.
- Editing and deleting categories.
- Fetching a list of available categories.
  
**🛍 Order Management** (`OrderController`)
- Creating new orders based on cart contents.
- Tracking order history for users.
- Viewing detailed order information.
  
**🛒 Shopping Cart Management** (`ShoppingCartController`)
- Adding books to the shopping cart.
- Updating the quantity of selected books.
- Removing books from the cart.
- Viewing the current cart status before checkout.
## 📊 Database Schema
<img width="932" alt="Знімок екрана 2025-03-24 о 10 58 37" src="https://github.com/user-attachments/assets/e4b9e552-9860-4378-acef-86cb51a1946f" />

## 🛠️ Setup & Installation
1️⃣ **Clone the Repository**
  
  First, clone the repository to your local machine:
  
  ```bash
  git clone https://github.com/trokhim03/BookShop-API.git
  ```
2️⃣ **Build the Project**
  
  Use Maven to clean and package the project:
  ```bash
  mvn clean package
  ```
3️⃣ **Start the Application with Docker**

  Run the following command to start the application using Docker Compose:
  
  ```bash
  docker-compose up -d
  ```
  > The `-d` flag runs the containers in detached mode.

## 🗄 Connecting to a Custom Database

If you want to use a custom MySQL database, update the application.properties file with your database credentials:

``` properties
# Database Configuration
spring.datasource.url=jdbc:mysql://<YOUR_DB_HOST>:<YOUR_DB_PORT>/<YOUR_DB_NAME>
spring.datasource.username=<YOUR_DB_USERNAME>
spring.datasource.password=<YOUR_DB_PASSWORD>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Hibernate settings
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true

# JWT Configuration
jwt.expiration=<TIME_SESSION_EXPIRATION_IN_MS>
jwt.secret=<SECRET_WORD>
```

## 📖 API Documentation

Explore the API endpoints with Swagger UI:

**🔗 [Swagger UI](http://localhost:8080/swagger-ui/index.html)**

<img width="1709" alt="image" src="https://github.com/user-attachments/assets/caaabbbd-c15b-40b8-a3a3-608a93acc060" />


## 📌 Example API Request

To test the API, you can use curl or Postman:

```bash
curl -X GET "http://localhost:8080/api/books" -H "Authorization: Bearer <YOUR_ACCESS_TOKEN>"
```


