# Spring Rest Docs Demo

Ejemplo de uso de spring rest docs.

## Ejecutar proyecto

Primero se debe obtener las dependencias de maven:

```bash
mvn clean package
```

Al hacer package tambien se generará la documentación.

Para ejecutar el proyecto:

```bash
mvn spring-boot:run
```

La doc se encontrará disponible en http://localhost:8080

## Sonarqube

Para analizar en proyecto en una instancia local de SonarQube corriendo en el puerto 9000:

```bash
mvn clean verify sonar:sonar \
  -Dsonar.projectKey=spring-rest-docs-demo \
  -Dsonar.projectName='spring-rest-docs-demo' \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=$SONARQUBE_LOCAL_TOKEN
```
