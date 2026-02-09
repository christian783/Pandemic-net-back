# PandemicNet - Contact Tracing System

PandemicNet is a robust contact tracing application built with Spring Boot. It provides a comprehensive set of tools for tracking user movements, identifying at-risk individuals, and generating detailed reports to help manage and mitigate the spread of infectious diseases.

## 🚀 Features

- **User Authentication & Authorization**: Secure registration, login, and logout using JWT (JSON Web Tokens) with token blacklisting support.
- **Contact Tracing**: Effortlessly record and track user entries at various data centers and locations.
- **Risk Analysis**: Automatically identify users who were in close proximity to infected individuals based on overlapping time intervals at specific locations.
- **Graph Visualization**: API support for generating contact networks, visualizing at-risk individuals as nodes and their interactions as edges.
- **Comprehensive Reporting**:
  - **Contact Tracing Reports**: Detailed history of contacts for specific individuals.
  - **Shortest Contact Path**: Discover the chain of contact between any two users.
  - **Status Reports**: Filter and view user health statuses by date range and location.
  - **Export Options**: Export reports to high-quality PDF or CSV formats.
- **Admin Dashboard**: Full-featured user management for administrators.
- **Notifications**: Stay updated with important alerts and notifications.
- **API Documentation**: Integrated Swagger/OpenAPI for easy exploration and testing of all endpoints.

## 🛠️ Tech Stack

- **Backend**: Java 21, Spring Boot 3.4.5
- **Security**: Spring Security, JWT (JJWT)
- **Data Access**: Spring Data JPA, Hibernate
- **Database**: MySQL
- **Reporting**: iText (PDF), OpenCSV (CSV)
- **Documentation**: SpringDoc OpenAPI (Swagger)
- **Utilities**: Lombok, Dotenv

## 📋 Prerequisites

- **Java 21** or higher
- **Maven 3.x**
- **MySQL 8.x**
- **SMTP Server** (for email notifications, e.g., Gmail)

## ⚙️ Configuration

1. **Database Setup**:
   Create a MySQL database named `pandemicnet`.
   ```sql
   CREATE DATABASE pandemicnet;
   ```

2. **Environment Variables**:
   Update the `src/main/resources/application.properties` file or set environment variables for the following:
   - `spring.datasource.url`: Your MySQL connection string.
   - `spring.datasource.username`: Your MySQL username.
   - `spring.datasource.password`: Your MySQL password.
   - `spring.mail.username`: Your SMTP email address.
   - `spring.mail.password`: Your SMTP app-specific password.
   - `jwt.secret`: A strong secret key for JWT signing.

## 🚀 Running the Application

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/pandemicnet.git
   cd pandemicnet
   ```

2. **Build the project**:
   ```bash
   ./mvnw clean install
   ```

3. **Run the application**:
   ```bash
   ./mvnw spring-boot:run
   ```
   The application will start on `http://localhost:8081`.

## 📖 API Documentation

Once the application is running, you can access the interactive Swagger UI at:
[http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)

The API documentation provides detailed information about all available endpoints, request/response formats, and authentication requirements.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📄 License

This project is licensed under the MIT License.
