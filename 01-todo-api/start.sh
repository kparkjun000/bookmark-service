#!/bin/bash
echo "Starting Todo API application..."

# Set default values
PORT=${PORT:-8080}
SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE:-production}

echo "PORT: $PORT"
echo "Profile: $SPRING_PROFILES_ACTIVE"
echo "Database URL: ${DATABASE_URL:0:30}..."

# Start the application
exec java -Xmx512m -Xms256m \
  -Dserver.port=$PORT \
  -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE \
  -Djava.security.egd=file:/dev/./urandom \
  -jar target/todo-api-1.0.0.jar