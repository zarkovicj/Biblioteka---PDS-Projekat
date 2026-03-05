# Biblioteka - Mikroservisna Aplikacija

## Tehnologije

- **Java 21**
- **Spring Boot 3.4.2**
- **Spring Cloud 2023.0.3**
- **Maven**
- **H2 Database** (in-memory)
- **Docker & Docker Compose** (opciono)
- **OpenFeign**
- **Resilience4j** (Circuit Breaker & Retry)

---

## Preduslov

Pre pokretanja projekta, potrebno je instalirati:

- [Java 21](https://www.oracle.com/java/technologies/downloads/#java21)
- [Maven 3.8+](https://maven.apache.org/download.cgi)
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (opciono)

---

## Pokretanje Projekta

### Opcija 1: Docker Compose 

```bash
# 1. Kloniraj projekat
git clone <https://github.com/zarkovicj/Biblioteka---PDS-Projekat.git>
cd Biblioteka

# 2. Build JAR fajlove
mvn clean package -DskipTests

# 3. Pokreni sve servise
docker-compose up --build

# Isključivanje servisa
docker-compose down
```

### Opcija 2: Ručno 
**VAŽNO: Redosled pokretanja je bitan!**

```bash
# 1. Kloniraj projekat
git clone <https://github.com/zarkovicj/Biblioteka---PDS-Projekat.git>
cd Biblioteka

# 2. Eureka Server
cd eureka-server
mvn spring-boot:run
# Sačekaj da se pokrene na http://localhost:8761

# 3. Config Server (u novom terminalu)
cd config-server
mvn spring-boot:run

# 4. Readers Service (u novom terminalu)
cd readers-service
mvn spring-boot:run

# 5. Loans Service (u novom terminalu)
cd loans-service
mvn spring-boot:run

# 6. API Gateway (u novom terminalu)
cd api-gateway
mvn spring-boot:run
```

Ili korišćenjem IntelliJ IDEA ili drugih editora:
1. Otvori projekat u IntelliJ IDEA
2. Pokreni servise po redosledu (desni klik na main klasu → Run):
   - EurekaServerApplication
   - ConfigServerApplication
   - ReadersServiceApplication
   - LoansServiceApplication
   - ApiGatewayApplication
