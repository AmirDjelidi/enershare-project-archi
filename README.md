# ⚡ EnerShare - Peer-to-Peer Energy Trading Platform

EnerShare is a robust, microservices-based backend architecture designed to facilitate peer-to-peer renewable energy trading between households. It allows users to register their smart meters, produce solar energy, fund their digital wallets, and securely trade energy surplus on a local marketplace.

## 🏗️ Architecture Overview

The system is built using **Java 21** and **Spring Boot**, implementing a decentralized microservices architecture. Each service operates independently with its own dedicated in-memory database (H2) and communicates with others via REST APIs (`RestTemplate`).

### Microservices List
| Service | Port | Description |
| :--- | :--- | :--- |
| 🛡️ **User Service** | `8085` | Handles user registration, authentication, and JWT token generation. |
| 🏠 **Household Service** | `8081` | Manages smart meters, tracking energy production and consumption. |
| 💰 **Wallet Service** | `8082` | Manages digital wallets, deposits, withdrawals, and transaction history. |
| 📈 **Trading Service** | `8083` | The core engine. Manages trading sessions, offers, and bids. Enforces business rules (e.g., checking energy/funds availability). |
| 🔔 **Notification Service**| `8084` | Acts as an inbox, alerting users about successful trades and wallet updates. |

<img width="1061" height="597" alt="RLHTJzim57tthxWA4YiIpKO1YUrXeWqVsxGFHCfusOjnBgbLn-xyCLPD--zpSab8iu1KnRdd79zpQlVSUMvzg1HpQwattF8AXAasHgFskTygX3YSzQOBBjQbDK4NcL76mjxLs_gVCIzzH7-HmXg7zfSKw61knKfw53vOX2bSQhJvYbjaZ0iVoKccPDm8NaF-nCiIxGyzcQj8FOI5UJY4UL" src="https://github.com/user-attachments/assets/ee4766d7-25b0-46eb-8807-bbdd64dc349e" />


## 🚀 Prerequisites

To run this project locally, you need:
* **Java 21** (JDK)
* **Maven** (or use the provided `mvnw` wrapper)
* An API Client like **Postman** (Recommended) or a web browser for Swagger UI.

## 🛠️ Installation & Setup

**1. Clone the repository**
```bash
git clone https://github.com/AmirDjelidi/EnerShare-project-archi
cd enershare-project
```
**2. Start the Microservices**
You need to start each microservice individually. Open 5 separate terminal windows (one for each service) and run the following commands:

*Terminal 1 -User Service:*
```bash
cd user-service
./mvnw spring-boot:run
````
*Terminal 2 - Household Service:*

```bash
cd household-service
./mvnw spring-boot:run
```
*Terminal 3 - Wallet Service:*

```bash
cd wallet-service
./mvnw spring-boot:run
```

*Terminal 4 - Trading Service:*

```bash
cd trading-service
./mvnw spring-boot:run
```
*Terminal 5 - Notification Service:*

```bash
cd notification-service
./mvnw spring-boot:run
```

## 🧪 Testing the Application

### Option A: Using Swagger UI
Each microservice exposes its own OpenAPI documentation. You can interact with the APIs directly through your browser:

* User Service: http://localhost:8085/swagger-ui.html
* Household Service: http://localhost:8081/swagger-ui.html
* Wallet Service: http://localhost:8082/swagger-ui.html
* Trading Service: http://localhost:8083/swagger-ui.html
* Notification Service: http://localhost:8084/swagger-ui.html

### Option B: Using the Postman E2E Collection (Recommended)
We have prepared a complete End-to-End (E2E) testing suite that simulates a full trading scenario (Alice producing energy and selling it to Bob).

* Open Postman.

* Click on Import.

* Copy the raw JSON of the EnerShare - Full E2E Architecture Tests collection and paste it into the "Raw text" tab. [Available Here](https://github.com/AmirDjelidi/EnerShare-project-archi/blob/main/postman-collection.json)

* Run the requests in order (from 1 to 14) to see the magic happen!

### ⚙️ Business Rules Enforced
* Rule 1: A user cannot sell more energy than their household has produced. The Trading Service verifies this synchronously with the Household Service.
* Rule 2: A user cannot buy energy if they lack sufficient funds.
* Rule 3: Upon a successful trade, the seller's energy is deducted and added to the buyer. Funds are transferred accordingly, and real-time notifications are sent to both parties.

## 🧑‍💻 Authors
Amir Djelidi / Clément Tauziède / Tom Klajn
