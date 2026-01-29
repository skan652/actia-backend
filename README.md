# Air Quality Monitoring System - Backend API

A Spring Boot 3.3 backend application for the ACTIA Air Quality Tracking System. This API provides real-time air quality data monitoring, user authentication, data visualization endpoints, and WebSocket support for live updates.

## Overview

The Air Quality Monitoring System is a comprehensive backend service that:

- Manages air quality sensor data (gas and temperature readings)
- Handles user authentication with JWT tokens
- Provides email notifications and user management
- Streams real-time data via WebSocket connections
- Supports serial port communication with IoT devices
- Stores data in MongoDB for scalability

## Features

- **User Authentication & Authorization**
  - JWT-based authentication
  - Role-based access control (Admin, User)
  - User registration and password recovery
  - Session management with token refresh

- **Real-time Data Monitoring**
  - WebSocket support for live data streaming
  - Serial port integration for sensor data collection
  - MongoDB data persistence
  - Audit logging for data changes

- **Data Management**
  - Frame 1 & Frame 2 data collection
  - Gas and temperature parameter tracking
  - Timeline and CSV export support
  - Data analysis endpoints

- **Communication**
  - Email notifications via SMTP
  - RESTful API with OpenAPI/Swagger documentation
  - WebSocket handlers for real-time updates

- **Security**
  - Spring Security integration
  - JWT token management
  - Password encryption with Spring Security
  - CORS support

## Technology Stack

- **Framework**: Spring Boot 3.3.0
- **Language**: Java 17
- **Database**: MongoDB (with reactive driver)
- **Security**: Spring Security + JWT (JJWT)
- **WebSocket**: Spring WebSocket
- **Email**: Spring Mail (Gmail SMTP)
- **Documentation**: Springdoc OpenAPI (Swagger)
- **Serial Communication**: jSerialComm
- **Build Tool**: Maven
- **Containerization**: Docker & Docker Compose

## Prerequisites

- Java 17 or higher
- Maven 3.8+
- MongoDB 5.0+
- Docker & Docker Compose (optional, for containerized setup)

## Installation

### 1. Clone the Repository

```bash
git clone https://github.com/mayssa979/actia-backend.git
cd actia-backend
```

### 2. Environment Configuration

Copy the `.env.example` file to create your local `.env` file:

```bash
cp .env.example .env
```

Edit the `.env` file in the root directory with the following variables:

```env
# Backend API URL
API_BASE_URL=http://localhost:8080

# Database configuration
DB_HOST=localhost
DB_PORT=5432
DB_USER=your_db_user
DB_PASSWORD=your_db_password
DB_NAME=your_db_name

# External service API keys (e.g., weather API, maps API)
MAPS_API_KEY=your_mapbox_api_key_here

# SendGrid Email Configuration (for failed login alerts and email notifications)
SENDGRID_API_KEY=your_sendgrid_api_key_here
```

**Important Notes:**
- The `.env` file is gitignored and should never be committed
- Always use `.env.example` as a template for setting up new environments
- For SendGrid, get your API key from [SendGrid Dashboard](https://app.sendgrid.com/settings/api_keys)

### 3. Start MongoDB

#### Using Docker Compose (Recommended):

```bash
docker-compose up -d
```

This will start:

- MongoDB on `localhost:27017`
- Mongo Express UI on `localhost:8081`

#### Using Local MongoDB

```bash
# Ensure MongoDB is running
mongod
```

### 4. Build the Application

```bash
mvn clean package
```

### 5. Run the Application

```bash
mvn spring-boot:run
```

The server will start on `http://localhost:8080`

## API Documentation

Once the application is running, access the interactive API documentation:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## Project Structure

```text
src/main/java/com/example/air/
├── AirApplication.java              # Main Spring Boot entry point
├── auditing/                        # Audit event tracking
├── auth/                            # Authentication controllers & services
│   ├── AuthenticationController.java
│   ├── AuthenticationService.java
│   └── JwtService.java
├── config/                          # Application configuration
│   ├── ApplicationConfig.java
│   ├── JwtAuthenticationFilter.java
│   ├── SecurityConfiguration.java
│   └── OpenApiConfig.java
├── controller/                      # REST API endpoints
│   ├── EmailController.java
│   ├── Frame1Controller.java
│   └── Frame2Controller.java
├── demo/                            # Demo endpoints
│   ├── AdminController.java
│   ├── DemoController.java
│   └── ManagementController.java
├── entity/                          # JPA/MongoDB entities
│   ├── Frame1.java
│   ├── Frame2.java
│   ├── User.java
│   └── Token.java
├── repository/                      # Data access layer
│   ├── Frame1Repository.java
│   ├── Frame2Repository.java
│   ├── UserRepository.java
│   └── TokenRepository.java
├── service/                         # Business logic layer
│   ├── Frame1Service.java
│   ├── Frame2Service.java
│   ├── EmailService.java
│   ├── UserService.java
│   └── SerialPortReader.java
├── token/                           # Token management
│   ├── Token.java
│   └── TokenRepository.java
├── user/                            # User management
│   ├── User.java
│   ├── UserRepository.java
│   ├── UserService.java
│   ├── Role.java
│   └── Permission.java
└── webSocket/                       # WebSocket handlers
    ├── Frame1WebSocketHandler.java
    ├── Frame2WebSocketHandler.java
    └── WebSocketConfig.java
```

## Key Endpoints

### Authentication

- `POST /api/v1/auth/register` - Register new user
- `POST /api/v1/auth/authenticate` - User login
- `POST /api/v1/auth/refresh-token` - Refresh JWT token
- `POST /api/v1/auth/logout` - User logout

### User Management

- `GET /api/v1/user/profile` - Get user profile
- `PUT /api/v1/user/change-password` - Change password
- `POST /api/v1/user/password-recovery` - Recover password

### Data Collection

- `GET /api/v1/frame1` - Get Frame 1 data
- `POST /api/v1/frame1` - Create Frame 1 entry
- `GET /api/v1/frame2` - Get Frame 2 data
- `POST /api/v1/frame2` - Create Frame 2 entry

### Email

- `POST /email/sendEmail` - Send email notification
- `POST /email/sendFailedLoginAlert` - Send suspicious login attempt alert

Request body for login alert:
```json
{
  "email": "user@example.com"
}
```

### Admin

- `GET /api/v1/admin/users` - List all users (admin only)
- `GET /api/v1/management/data` - Get system management data

### WebSocket

- `WS /ws/frame1` - Real-time Frame 1 data stream
- `WS /ws/frame2` - Real-time Frame 2 data stream

*Refer to Swagger documentation for complete endpoint specifications.*

## WebSocket Usage

Connect to WebSocket endpoints for real-time data streaming:

```javascript
// Example: Connect to Frame 1 WebSocket
const socket = new WebSocket('ws://localhost:8080/ws/frame1');

socket.onmessage = (event) => {
  const data = JSON.parse(event.data);
  console.log('New Frame 1 data:', data);
};

socket.onerror = (error) => {
  console.error('WebSocket error:', error);
};
```

## Serial Port Communication

The application includes `SerialPortReader` for IoT device communication. Ensure:

- Device is properly connected to serial port
- Port configuration is set in `application.properties`
- Required permissions are granted for serial port access

## MongoDB Administration

Access Mongo Express UI for database management:

- **URL**: http://localhost:8081
- **Username**: rootuser
- **Password**: rootpass

## Testing

Run the test suite:

```bash
mvn test
```

## Build & Deployment

### Build JAR

```bash
mvn clean package
```

The JAR file will be generated at `target/air-0.0.1-SNAPSHOT.jar`

### Docker Build (Optional)

```bash
docker build -t actia-backend:latest .
docker run -p 8080:8080 --network mongodb_network actia-backend:latest
```

## Troubleshooting

### MongoDB Connection Issues

- Verify MongoDB is running: `mongosh`
- Check credentials in `application.properties`
- Ensure network connectivity if using Docker

### Email Not Sending

- Verify Gmail credentials and app-specific password
- Check SMTP settings in `application.properties`
- Ensure "Less secure app access" is enabled if not using app-specific password

### Port Already in Use

```bash
# Find process using port 8080
lsof -i :8080
# Kill the process
kill -9 <PID>
```

### Serial Port Permission Denied (Linux/Mac)

```bash
# Add user to dialout group (Linux)
sudo usermod -a -G dialout $USER

# Reset USB (Mac)
sudo killall -9 com.apple.usbmuxd
```

## Security Notes

⚠️ **Important**: Before pushing to production:

1. **Change default credentials**:
   - Update JWT secret key (currently hardcoded)
   - Change MongoDB username and password
   - Use environment variables for sensitive data

2. **Email Configuration**:
   - Use environment variables for email credentials
   - Never commit actual email passwords

3. **Database**:
   - Enable MongoDB authentication
   - Use strong passwords
   - Consider deploying to MongoDB Atlas for production

4. **API Security**:
   - Implement rate limiting
   - Enable HTTPS/TLS
   - Configure CORS appropriately for your domain
   - Add request validation

5. **.env File**:
   - Add `.env` to `.gitignore` (already configured)
   - Never commit sensitive credentials

## Environment Variables

The application uses the following environment variables (can be set in `application.properties` or `.env`):

| Variable | Default | Description |
|----------|---------|-------------|
| `spring.data.mongodb.host` | localhost | MongoDB host |
| `spring.data.mongodb.port` | 27017 | MongoDB port |
| `spring.data.mongodb.username` | rootuser | MongoDB username |
| `spring.data.mongodb.password` | rootpass | MongoDB password |
| `spring.mail.username` | - | Gmail address for sending emails |
| `spring.mail.password` | - | Gmail app-specific password |
| `application.security.jwt.secret-key` | - | JWT secret key |
| `application.security.jwt.expiration` | 86400000 | JWT token expiration (ms) |
| `server.port` | 8080 | Server port |

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is UNLICENSED. All rights reserved.

## Authors

- **Skander Adam Afi** - Backend Development
- **Mayssa** - Project Coordination

## Support

For issues, bug reports, or feature requests, please [open an issue](https://github.com/mayssa979/actia-backend/issues) on GitHub.

## Changelog

### Version 0.0.1-SNAPSHOT

- Initial release
- Core authentication system
- Real-time WebSocket data streaming
- MongoDB persistence
- JWT token management
- Email notification system
- Role-based access control
