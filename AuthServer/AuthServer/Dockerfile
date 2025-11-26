# --- Estágio 1: Build (Compilação) ---
# Usamos uma imagem que já tem o Maven instalado
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build

# Define pasta de trabalho dentro do container
WORKDIR /app

# Copia o arquivo de dependências
COPY pom.xml .

# Baixa as dependências (Isso otimiza o cache do Docker)
RUN mvn dependency:go-offline

# Copia o código fonte do projeto
COPY src ./src

# Compila o projeto e gera o JAR (o comando que você fazia na mão)
RUN mvn clean package -DskipTests

# --- Estágio 2: Runtime (Execução) ---
# Usamos uma imagem leve apenas com o Java para rodar
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copia o JAR gerado no estágio anterior (build) para este estágio
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]