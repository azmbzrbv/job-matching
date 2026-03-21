# AI-Powered Job Matching Engine

A robust, enterprise-grade **Spring Boot backend** designed to connect candidates with their ideal job roles using **Machine Learning-based text analysis**.

This project emphasizes:
- Clean architecture
- Security-first file handling
- Scalable and extensible design patterns

---

## Core Features

### Intelligent Job Matching
- Integrates with a Machine Learning service to calculate **match scores** between:
  - Candidate resumes (PDF)
  - Job descriptions

---

### Decoupled Architecture (DTOs)
- Uses **Data Transfer Objects (DTOs)** to separate:
  - Internal database schema
  - External API responses
- Prevents circular dependencies and improves maintainability

---

### Storage Strategy Pattern
- Implements a flexible **storage interface**
- Current support:
  - Local File System
- Planned:
  - AWS S3 integration (plug-and-play)

---

### Secure File Handling
- Robust PDF upload system with:
  - UUID-based unique file naming (collision-proof)
  - Path Traversal protection for secure file access

---

### Global Exception Handling
- Centralized `@RestControllerAdvice` system
- Converts internal exceptions into clean, standardized JSON responses

---

### Relational Data Modeling
- Complex **JPA relationships** between:
  - Candidates
  - Recruiters
  - Jobs
  - Applications

---

## Tech Stack

- **Java 21**
- **Spring Boot 3.x**
  - Spring Web
  - Spring Data JPA
  - Validation
- **PostgreSQL**
- **Maven**
- **Jackson** (JSON processing)
- **Python + Flask** (ML service for resume parsing)

---

## Future Roadmap

### 1️⃣ Security & Identity (Next Step)

- **JWT Authentication**
  - Implement Spring Security with JSON Web Tokens
  - Enable secure login and user-specific data access

- **Role-Based Access Control (RBAC)**
  - Candidates:
    - Upload CVs
    - Apply to jobs
  - Recruiters:
    - Post jobs
    - View ranked applicants

---

### 2️⃣ Cloud Infrastructure

- **AWS Deployment**
  - Migrate file storage from local system to **Amazon S3**

- **Dockerization**
  - Containerize:
    - Spring Boot backend
    - PostgreSQL database

---

### 3️⃣ Frontend Development (React.js)

- **Candidate Dashboard**
  - Upload resumes
  - Track applications
  - View **Top Matches** (ML-based)

- **Recruiter Portal**
  - Manage job postings
  - View ranked candidate lists

- **Real-time Notifications**
  - WebSockets integration
  - Status updates like:
    - "Under Review"
    - "Accepted"

---

## Project Status

**Under Active Development**

Core backend architecture is implemented.  
Upcoming focus: **Security (JWT) and cloud deployment**

---

## Vision

To build a **scalable, intelligent recruitment platform** that leverages machine learning to:
- Improve hiring efficiency
- Reduce manual screening
- Deliver better candidate-job alignment

---

## Contributing

Contributions, ideas, and feedback are welcome!  
Feel free to open issues or submit pull requests.

---

## License

This project is currently unlicensed. Add a license before public distribution.
