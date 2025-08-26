# ---------- Stage 1: Build (multi-module Maven) ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace

# Copy POMs first to leverage Docker cache
COPY pom.xml .
COPY api-spec/pom.xml api-spec/pom.xml
COPY impl/pom.xml impl/pom.xml

# Pre-fetch dependencies
RUN mvn -q -B -DskipTests dependency:go-offline

# Copy sources and build
COPY api-spec/src api-spec/src
COPY impl/src impl/src

RUN mvn -q -B -DskipTests clean package

# ---------- Stage 2: Runtime ----------
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the runnable JAR from the impl module
COPY --from=build /workspace/impl/target/*.jar /app/app.jar

ENV TZ=Asia/Ho_Chi_Minh
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
