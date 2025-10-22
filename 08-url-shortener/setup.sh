#!/bin/bash
# Linux/Mac Setup Script for URL Shortener Project

echo "========================================"
echo "URL Shortener Setup Script"
echo "========================================"
echo

# Check Java
echo "Checking Java installation..."
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH"
    echo "Please install Java 21"
    exit 1
fi
java -version
echo

# Check Maven
echo "Checking Maven installation..."
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven is not installed or not in PATH"
    echo "Please install Maven 3.9.x"
    exit 1
fi
mvn -version
echo

# Check PostgreSQL (optional)
echo "Checking PostgreSQL installation..."
if ! command -v psql &> /dev/null; then
    echo "WARNING: PostgreSQL is not installed or not in PATH"
    echo "You can use Docker instead or install PostgreSQL manually"
fi
echo

# Build URL Shortener Service
echo "Building URL Shortener Service..."
cd url-shortener-service
mvn clean package -DskipTests
if [ $? -ne 0 ]; then
    echo "ERROR: Failed to build URL Shortener Service"
    exit 1
fi
cd ..
echo "URL Shortener Service built successfully!"
echo

# Build Analytics Service
echo "Building Analytics Service..."
cd analytics-service
mvn clean package -DskipTests
if [ $? -ne 0 ]; then
    echo "ERROR: Failed to build Analytics Service"
    exit 1
fi
cd ..
echo "Analytics Service built successfully!"
echo

echo "========================================"
echo "Setup completed successfully!"
echo "========================================"
echo
echo "Next steps:"
echo "1. Start PostgreSQL database"
echo "   - Use Docker: docker-compose up -d postgres"
echo "   - Or install PostgreSQL locally"
echo
echo "2. Start URL Shortener Service:"
echo "   cd url-shortener-service"
echo "   mvn spring-boot:run"
echo
echo "3. Start Analytics Service (in another terminal):"
echo "   cd analytics-service"
echo "   mvn spring-boot:run"
echo
echo "4. Access services:"
echo "   - URL Shortener: http://localhost:8080"
echo "   - Analytics: http://localhost:8081"
echo
echo "For more information, see QUICKSTART.md"
echo

