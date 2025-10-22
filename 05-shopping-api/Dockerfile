FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/shopping-api-1.0.0.jar app.jar
CMD java -Dserver.port=$PORT $JAVA_OPTS -jar app.jar

