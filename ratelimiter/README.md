# API Rate Limiter Service (Token Bucket Algorithm)

## Project Overview
This project is an **API Rate Limiter Service** built using **Spring Boot (Java)**.
It controls the number of API requests a user can make within a time window, preventing abuse and ensuring fair usage.

The project implements the **Token Bucket Algorithm**.

---

 Tech Stack
- Java
- Spring Boot
- Maven
- Postman (Testing)
- JUnit (Unit Testing)

---

 Rate Limiting Algorithm Used: Token Bucket
### Token Bucket Logic:
- Each user has a bucket with a fixed capacity.
- Tokens refill at a constant rate per second.
- Each API request consumes 1 token.
- If no tokens are available → request blocked with **HTTP 429**.

---

 Features Implemented (Phase 1)
 Rate Limit Validation  
 Configurable limit (capacity + refill rate)  
 User based rate limiting using query param `user`  
 HTTP 429 response when limit exceeded  
 Response Headers:
- `X-RateLimit-Limit`
- `X-RateLimit-Remaining`
- `X-RateLimit-Reset`

 Health Check Endpoint  
 Reset Endpoint  
 In-memory storage using HashMap  
 Unit Tests (minimum 5)

---

 API Endpoints
 Health Check
**GET**
http://localhost:9090/health�
Response:
Service is running!


---

 Rate Limit Check Endpoint
**GET**
http://localhost:9090/api/check?user=priyanshu


Success Response (200):
Request Allowed


Limit Exceeded Response (429):
Too Many Requests (429)


---

 Reset Endpoint
**POST**
http://localhost:9090/api/reset?user=priyanshu

Response:
Rate limit reset done for user: priyanshu


---

 Running the Project

 Step 1: Clone Repo
git clone https://github.com/Priyanshu007804/API-Rate-Limiter-Service.git
cd ratelimiter

Step 2: Run Application
./mvnw spring-boot:run
Server runs on:
http://localhost:9090

 Running Tests
./mvnw test

Postman Testing
A Postman collection can be used to test:
Health endpoint
Rate limiting behavior
Reset endpoint
Headers output


Author
Priyanshu Singha Roy