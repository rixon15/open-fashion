Open Fashion Marketplace Backend
This project serves as the backend for a modern online fashion store. It's a robust and secure RESTful API built with Spring Boot, designed to handle the core logic for product management, user authentication, and shopping cart functionality. This backend is ready to be paired with any frontend application to create a complete e-commerce solution.

Features
Secure Authentication: Implemented JSON Web Token (JWT) authentication using Spring Security to protect endpoints and manage user sessions.

User & Role Management: Manages user accounts and assigns roles (USER, ADMIN) to control access to specific API resources.

Product Catalog Management: A comprehensive system for managing a product catalog, including support for various sizes and colors as distinct product variants.

Transactional Data Operations: Ensures data integrity for all CRUD operations on products, categories, colors, and sizes.

Scalable Architecture: Designed with a service-oriented approach, separating business logic from controllers and repositories.

Technologies
Backend
Language: Java 21

Framework: Spring Boot 3.5.5

Database: MySQL

Authentication: Spring Security, JWT (JJWT)

ORM: Spring Data JPA

Build Tool: Maven

Infrastructure
Containerization: Docker, Docker Compose

Build Tool: Maven Wrapper (mvnw)

Architecture
The project is structured as a single microservice backend. It provides a RESTful API and is designed to be easily deployed and scaled using Docker containers.

Getting Started
Prerequisites
To run this project, you'll need:

Java Development Kit (JDK) 21

Docker

Maven (optional, as Maven Wrapper is included)

1. Database Setup with Docker
The easiest way to get the MySQL database up and running is to use the provided Docker configuration.

Navigate to the docker/ directory.

Start the MySQL container using Docker Compose. This will automatically create the database and tables defined in docker/mysql/db_init/schema.sql.

Bash

docker-compose up -d
2. Backend Setup & Run
Open the pom.xml file in your preferred IDE (e.g., IntelliJ IDEA).

Copy the application.properties.sample file to src/main/resources/application.properties and fill in the database connection details. The default values for the Docker setup are provided in docker/spring/Dockerfile.

Run the OpenfashionMarketplaceApplication.java file from your IDE, or use the Maven Wrapper from the project's root directory:

Bash

./mvnw spring-boot:run
The application will start on port 8080 (or the port specified in your application.properties file).

API Endpoints
The API is structured under the /api/v1/ base path. Key endpoints include:

/api/v1/auth: User registration, login, and logout.

/api/v1/products: Publicly accessible endpoints for browsing products.

/api/v1/category, /api/v1/color, /api/v1/size: Endpoints for managing product-related data. Access to these is secured by Spring Security.

Project Status
This is a personal project I'm actively developing to demonstrate my skills. It's a work in progress, and I am continuously adding new features and refining the existing codebase. Feedback and suggestions are always welcome.
