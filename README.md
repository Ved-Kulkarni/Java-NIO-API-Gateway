# 🚀 High-Performance API Gateway (Java NIO)

> Built from scratch to understand how real-world servers like NGINX and Netty work internally.

---

## 📌 Overview

This project is a high-performance API Gateway built from scratch using Core Java (no frameworks).  
It leverages Java NIO and an event-driven architecture to efficiently handle concurrent connections and implement rate limiting.

---

## ⚙️ Features

- Event-driven server using Java NIO + Selector
- Custom HTTP parsing (without external libraries)
- Modular routing system
- Time-based rate limiting (configurable via environment variables)
- Buffer reuse optimization (low GC overhead)
- Dockerized deployment
- Load tested using `wrk`

---

## 🛠️ How to Run the Project

### 1️⃣ Prerequisites

Make sure you have the following installed:

- Java 17+
- Maven
- Docker (optional for containerized run)
- wrk (for load testing)

### 2️⃣ Install Dependencies (Mac)

```bash
brew install maven
brew install docker
brew install wrk
```

### 3️⃣ Run Locally
```bash
mvn clean package
java -cp target/http-server-1.0-SNAPSHOT.jar com.ved.App
```

Server will start on:
http://localhost:9091

### 4️⃣ Test Endpoints

```bash
curl localhost:9091/hello
curl localhost:9091/health
curl localhost:9091/api
```

### 5️⃣ Configure Rate Limit

Default:
25 requests per second

Run with custom limit:
```bash
RATE_LIMIT=5 java -cp target/http-server-1.0-SNAPSHOT.jar com.ved.App
```

### 6️⃣ Run with Docker

```bash
mvn package
docker build -t ved-nio-server .
docker run -p 9091:9091 -e RATE_LIMIT=25 ved-nio-server
```

### 7️⃣ Load Testing

```bash
wrk -t2 -c50 -d2s http://localhost:9091/api
```

----

## 🧠 Architecture
```
Client
  ↓
NIO Server (Selector)
  ↓
HTTP Parser
  ↓
Router
  ↓
Rate Limiter
  ↓
Response
```

----
## ⚡ Performance

Tested using:
```bash
wrk -t2 -c50 -d2s http://localhost:9091/api
```

Results:

~7.5K requests/sec
Handles concurrent load efficiently
Proper rate limiting under stress

----

## 🧪 Test

curl localhost:9091/api

----

## 📂 Project Structure
```
com.ved
  ├── http       → Request/Response models
  ├── nio        → NIO server + rate limiting
  ├── router     → Routing logic
  ├── server     → Blocking server (for comparison)
  └── App.java   → Entry point
```

---

## 📈 Key Learnings

Difference between blocking vs non-blocking I/O\
Event-driven architecture using Selector\
Rate limiting (fixed window)\
Performance optimization using ByteBuffer reuse\
Docker-based deployment
