# Spring Boot Application with Enhanced Security Features

This application is an implementation of Spring Security Basic, designed to integrate my cybersecurity expertise into development. It incorporates essential mechanisms to enhance application security, including robust protection against brute force attacks, which is vital for preventing unauthorized access.

## Features

1. **Spring Security Basic Authentication**
  
  - Protects the application endpoints with basic authentication.
2. **IP-Based Rate Limiting**
  
  - Prevents attackers from making a large number of attempts in a short period.
3. **CAPTCHA Verification** (In Progress)
  
  - Implements CAPTCHA after several failed attempts to block automated attacks.
4. **Brute Force Mitigation** (In Progress)
  
  - While these measures do not guarantee the complete elimination of threats, they make the attack process sufficiently tedious to discourage attackers.
5. **Multi-Factor Authentication (MFA)** (In Progress)
  
  - Adds an additional layer of security to verify user identity.
6. **User Access Pages**
  
  - **Login Page**: Authenticates users.
    
  - **Home Page**: Main page accessible after successful login.
    
  - **Test Page**: A sample page to verify access restrictions.
    
  - **Blocked Page**: Displays a message for blocked users or failed authentication attempts.
    

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
