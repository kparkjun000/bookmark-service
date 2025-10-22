#!/bin/bash
echo "Starting build process..."

# Maven build
echo "Building with Maven..."
mvn clean package -DskipTests

# Check if build was successful
if [ -f target/todo-api-1.0.0.jar ]; then
    echo "Build successful!"
    echo "JAR file created: target/todo-api-1.0.0.jar"
else
    echo "Build failed - JAR file not found"
    exit 1
fi

echo "Build complete!"