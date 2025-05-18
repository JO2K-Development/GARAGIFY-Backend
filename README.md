## Local development:
- use local spring profile


## 🛠️ Running Local Services in network

To run both frontend and backend services locally using Docker Compose, follow these steps:

### 1. Create a Shared Docker Network

This enables containers from separate Docker Compose projects to communicate with each other:

```bash
docker network create garagify-shared-network
```

### 2. Run docker compose for frontend and backend service

``` bash
docker compose up
```