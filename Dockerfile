FROM openjdk:25-oraclelinux8

WORKDIR /app

COPY target/hello-security-0.0.1-SNAPSHOT.jar /app/hello-security-0.0.1-SNAPSHOT.jar

CMD ["java", "-jar", "hello-security-0.0.1-SNAPSHOT.jar"]