# TCL Travels

Airline reservation system built with Spring Boot and React + TypeScript.

## Architecture

- **Backend**: Spring Boot 4.0.1 with Java 21
- **Frontend**: React 18 + TypeScript with Vite
- **Database**: PostgreSQL
- **Authentication**: JWT (JSON Web Tokens)
- **Security**: Role-based access control (CUSTOMER, EMPLOYEE, MANAGER)

## Prerequisites

- Java 21
- Node.js 20.x
- PostgreSQL
- Maven 3.x

## Database Setup

Create PostgreSQL database and configure via environment variables:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/tcltravels
export DB_USERNAME=postgres
export DB_PASSWORD=your_password
```

Or use defaults (localhost:5432/tcltravels, user: postgres, empty password).

## Development Workflow

### Backend Only

```bash
# Run Spring Boot backend on port 8080
./mvnw spring-boot:run

# Run tests
./mvnw test
```

### Frontend Only

```bash
cd frontend

# Install dependencies
npm install

# Run dev server on port 5173 (proxies /api to localhost:8080)
npm run dev

# Build for production
npm run build
```

### Full Stack Development

**Terminal 1** - Backend:
```bash
./mvnw spring-boot:run
```

**Terminal 2** - Frontend:
```bash
cd frontend && npm run dev
```

Open http://localhost:5173 in your browser. Frontend hot-reloads on changes, backend auto-restarts with DevTools.

## Production Build

Maven build includes frontend build automatically:

```bash
# Build complete application (backend + frontend)
./mvnw clean package

# Run production build
java -jar target/tcltravels-0.0.1-SNAPSHOT.jar
```

Frontend is bundled into `/static` directory and served by Spring Boot.

## API Endpoints

### Authentication
- `POST /api/v1/auth/login` - User login
- `POST /api/v1/auth/register` - Customer registration
- `POST /api/v1/auth/refresh` - Refresh access token

### Customer (Requires CUSTOMER role)
- `GET /api/v1/users/me` - Get current user profile
- `PUT /api/v1/users/me` - Update profile
- `GET /api/v1/flights` - Search flights
- `GET /api/v1/flights/{id}/{number}/stops` - Get flight stops
- `GET /api/v1/reservations` - Get user reservations
- `POST /api/v1/reservations` - Create reservation
- `POST /api/v1/reservations/bid` - Place auction bid

### Admin (Requires EMPLOYEE or MANAGER role)
- `GET /api/v1/admin/dashboard` - Dashboard data
- `GET /api/v1/admin/sales` - Sales reports
- `GET /api/v1/admin/reservations` - Reservation management
- `GET /api/v1/admin/flights` - Flight management

### Employee Management (Requires MANAGER role only)
- `GET /api/v1/admin/employees` - List employees
- `POST /api/v1/admin/employees` - Create employee
- `PUT /api/v1/admin/employees/{id}` - Update employee
- `DELETE /api/v1/admin/employees/{id}` - Delete employee

## Project Structure

```
tcltravels/
├── src/main/java/com/thomaslent/tcltravels/
│   ├── controllers/
│   │   ├── api/              # REST API controllers
│   │   └── SpaController.java # Frontend routing
│   ├── services/             # Business logic
│   ├── repositories/         # JPA repositories
│   ├── entities/             # JPA entities
│   ├── dto/                  # Data transfer objects
│   ├── security/             # Security configuration
│   │   ├── jwt/              # JWT authentication
│   │   ├── ApiSecurityConfig.java
│   │   └── SecurityConfig.java
│   └── config/               # Application configuration
│       └── ApiExceptionHandler.java
├── src/main/resources/
│   ├── application.properties
│   ├── schema.sql            # Database schema
│   └── data.sql              # Sample data
├── frontend/                  # React TypeScript app
│   ├── src/
│   │   ├── api/              # API client
│   │   ├── components/       # React components
│   │   ├── pages/            # Page components
│   │   ├── context/          # React Context (auth)
│   │   └── App.tsx           # Root component
│   ├── package.json
│   ├── vite.config.ts
│   └── tailwind.config.ts
└── pom.xml
```

## Security

- JWT tokens stored in localStorage (access token) and httpOnly cookies (refresh token)
- Access tokens expire after 15 minutes
- Refresh tokens expire after 7 days
- CORS configured for development (localhost:5173)
- Role-based authorization with Spring Security @PreAuthorize
- Password hashing with BCrypt

## Testing

Backend tests use H2 in-memory database in PostgreSQL compatibility mode:

```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=UserServiceTest

# Run specific test method
./mvnw test -Dtest=UserServiceTest#testMethod
```

Frontend tests (if configured):

```bash
cd frontend
npm test
```

## Environment Variables

### Backend
- `DB_URL` - PostgreSQL connection URL
- `DB_USERNAME` - Database username
- `DB_PASSWORD` - Database password
- `JWT_SECRET` - Secret key for JWT signing (min 256 bits)
- `CORS_ALLOWED_ORIGINS` - Allowed CORS origins (comma-separated)

### Frontend
- `VITE_API_BASE_URL` - API base URL (defaults to `/api/v1`)

## License

Copyright © 2024 Thomas Lent
