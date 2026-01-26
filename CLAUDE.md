# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and Development Commands

### Backend

```bash
# Run the Spring Boot application (port 8080)
./mvnw spring-boot:run

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=UserServiceTest

# Run a single test method
./mvnw test -Dtest=UserServiceTest#testMethod

# Build the project (includes frontend build)
./mvnw clean package

# Skip tests during build
./mvnw clean package -DskipTests
```

### Frontend

```bash
cd frontend

# Install dependencies
npm install

# Run dev server (port 5173, proxies /api to localhost:8080)
npm run dev

# Build for production
npm run build

# Type check
npm run type-check

# Lint
npm run lint
```

### Full Stack Development

For development, run both backend and frontend concurrently:

**Terminal 1** - Backend:
```bash
./mvnw spring-boot:run
```

**Terminal 2** - Frontend:
```bash
cd frontend && npm run dev
```

Open http://localhost:5173. Frontend hot-reloads on changes, backend auto-restarts with DevTools.

## Database Setup

PostgreSQL database required. Configure via environment variables or use defaults:
- `DB_URL` (default: `jdbc:postgresql://localhost:5432/tcltravels`)
- `DB_USERNAME` (default: `postgres`)
- `DB_PASSWORD` (default: empty)

Schema is managed manually via `src/main/resources/schema.sql` - Hibernate DDL is disabled.

## Architecture

Spring Boot 4.0.1 / Java 21 backend with React 18 + TypeScript frontend. Stateless REST API with JWT authentication.

### Backend Structure

```
src/main/java/com/thomaslent/tcltravels/
├── controllers/
│   ├── api/              # REST API controllers (/api/v1/*)
│   └── SpaController.java # Forwards frontend routes to index.html
├── services/             # Business logic
├── repositories/         # JPA data access interfaces
├── entities/             # JPA entity classes (domain model)
├── dto/                  # Data transfer objects for API
├── security/             # Spring Security configuration
│   ├── jwt/              # JWT token provider and filters
│   ├── ApiSecurityConfig.java # Stateless API security
│   └── SecurityConfig.java    # Password encoder bean
└── config/
    └── ApiExceptionHandler.java # Global API error handling
```

### Frontend Structure

```
frontend/
├── src/
│   ├── api/              # API client with JWT interceptors
│   ├── components/       # Reusable React components
│   ├── pages/            # Page components (routes)
│   │   ├── admin/        # Admin pages (EMPLOYEE/MANAGER only)
│   │   └── *.tsx         # Customer pages
│   ├── context/          # React Context (AuthContext)
│   └── App.tsx           # Root component with routing
├── package.json
├── vite.config.ts        # Vite config with proxy
└── tailwind.config.ts    # Tailwind CSS v4
```

**Key domain entities:** User/Person (authentication), Customer/Employee (roles), Flight/Leg/Fare (flight data), Reservation/Passenger (bookings), Auction (seat bidding)

**Security:**
- JWT authentication (15-min access tokens, 7-day refresh tokens)
- Role-based access control: CUSTOMER, EMPLOYEE, MANAGER
- @PreAuthorize annotations on API controllers
- CORS configured for development (localhost:5173)

## Testing

Tests use H2 in-memory database in PostgreSQL compatibility mode. Test configuration in `src/test/resources/application.properties` disables schema initialization since tests manage their own data.

Testing stack: JUnit 5, Mockito, Spring Security Test, AssertJ.
