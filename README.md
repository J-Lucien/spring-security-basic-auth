# Spring Boot Application with Enhanced Security Features

This application is an implementation of Spring Security Basic, designed to integrate my cybersecurity expertise into development. It incorporates essential mechanisms to enhance application security, including robust protection against brute force attacks, which is vital for preventing unauthorized access.

## Features

1. **Spring Security Basic Authentication**
  
  - Protects the application endpoints with basic authentication.
2. **IP-Based Rate Limiting**
  
  - Prevents attackers from making a large number of attempts in a short period.
3. **CAPTCHA Verification**
  
  - implement CAPTCHA after multiple failed login attempts to block automated attacks
  - Blocks access for 30 seconds after exceeding failed attempts
  - Requires CAPTCHA verification for subsequent login attempts
4. **Brute Force Mitigation**
  
  - While these measures do not guarantee the complete elimination of threats, they make the attack process sufficiently tedious to discourage attackers.
5. **Multi-Factor Authentication (MFA)** (In Progress)
  
  - Adds an additional layer of security to verify user identity.
6. **User Access Pages**
  
  - **Login Page**: Authenticates users.
    
  - **Home Page**: Main page accessible after successful login.
    
  - **Test Page**: A sample page to verify access restrictions.
    
  - **Blocked Page**: Displays a message for blocked users or failed authentication attempts.
  - **Error page**: A fallback page displayed when a user tries to access a non-existent resource.
    

## Technologies Used

- **Java 21 (OpenJDK)**
  
- **Spring Boot**
  
- **Spring Security**
  
- **Maven**
  

## Getting Started

### Prerequisites

Ensure you have the following installed:

- Java 21
  
- Maven
  
- A compatible IDE (e.g., IntelliJ IDEA, Eclipse)
  

### Installation

1. Clone the repository:
  
  ```
  git clone https://github.com/J-Lucien/spring-security-basic-auth.git
  ```
  
2. Build the project using Maven:
  
  ```
  mvn clean install
  ```
  
3. Run the application:
  
  ```
  mvn spring-boot:run
  ```
  
4. Access the application at `http://localhost:8081`.

### Notes on Security Implementation

- This project includes custom implementations, such as timing attack mitigations, even though Spring Security provides built-in protections. These additions serve as a demonstration of my understanding of secure coding practices and my ambition to create resilient applications.