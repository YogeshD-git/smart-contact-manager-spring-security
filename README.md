# smart contact manager
A smart contact management system developed with Spring Boot, JPA and Spring Security for authentication, MySQL for data storage, and Thymeleaf for front-end views.

This is a **Spring Boot** application for managing users and their contacts. The system includes functionalities for user authentication and contact management. Built using **Spring Boot**, **JPA**, **Hibernate**, **Thymeleaf**, **Spring Security**, and **MySQL**.

## Key Features

### 1. **User Management**
   - **Register**: Allows users to create new accounts.
   - **Login**: Secure user login with authentication.
   - **Logout**: Users can log out of the system securely.

### 2. **Contact Management**
   - **Show Contacts**: Displays a list of saved contacts.
   - **Add Contacts**: Users can add new contacts.
   - **Update Contacts**: Modify existing contact details.
   - **Search Contact**: Search for specific contacts by name.

## Technologies Used
- **Spring Boot**: Core framework for building the application.
- **JPA**: Data persistence layer for managing the database.
- **Thymeleaf**: Template engine for the front-end.
- **Spring Security**: Secures user authentication and authorization.
- **MySQL**: Database used to store user and contact data.
- **STS (Spring Tool Suite)**: IDE used for development.

## Database Setup

1. Install MySQL and create a database called `user_contact_db`.
2. Update `application.properties` with your MySQL credentials.
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/user_contact_db
   spring.datasource.username=root
   spring.datasource.password=your_password
