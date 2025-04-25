#  Challenge Coupon API

API REST para la gestión de cupones, desarrollada con enfoque en buenas prácticas de arquitectura y rendimiento.

---

## Tecnologías Utilizadas

- **Spring Boot**
- **Arquitectura Hexagonal (Ports & Adapters)**
- **MongoDB**
- **Redis**
- **Hostinger KVM2 (Producción)**

---

## Repositorio y Documentación

- **Repositorio GitHub**: [challenge-coupon](https://github.com/JonathanJulio03/challenge-coupon)
- **Swagger UI (Documentación de API)**: [https://challenge-test.online/swagger-ui/index.html](https://challenge-test.online/swagger-ui/index.html)

---

## Tabla de Contenido

- [Instrucciones](#️instrucciones)
  - [Despliegue Local](#despliegue-local)
  - [Despliegue con Docker](#despliegue-con-docker)
- [Arquitectura y Lógica de la Aplicación](#arquitectura-y-lógica-de-la-aplicación)
  - [Arquitectura Hexagonal](#arquitectura-hexagonal)
  - [Algoritmo Implementado](#algoritmo-implementado)
- [CI/CD](#cicd)
  - [GitHub Actions](#github-actions)

---

## Instrucciones

### Despliegue Local

1. Configura el archivo `application.yml` y las variables de entorno desde `.env`.
2. Ejecuta los siguientes comandos:
```cmd
mvn clean install
mvn spring-boot:run
```

### Despliege en Docker
```cmd
docker-compose up --build -d .
```
---
## Arquitectura y Logica de la Aplicación

### Arquitectura hexagonal
La Arquitectura Hexagonal propone que nuestro dominio sea el núcleo de las capas y que este no se acople a nada externo. En lugar de hacer uso explícito y mediante el principio de inversión de dependencias nos acoplamos a contratos (interfaces o puertos) y no a implementaciones concretas.

<img src="doc/image/architecture_hexagonal_model.png" width="500"/>

<img src="doc/image/architecture_hexagonal.png" width="500"/>

### Algorítmo implementado
- **Programación Dinámica:**
    - Evita recalcular soluciones repetidas. 
    - Reduce complejidad de exponencial a polinomial: O(n * W) donde n es número de objetos y W es capacidad. 
    - Garantiza una solución óptima para el problema clásico (si se usa la versión exacta).

<img src="doc/image/bigO.png" width="500"/>

## CI/CD
### GitHub Actions
El CI/CD realiza las siguientes acciones:
  **Install**
  - sudo apt update && sudo apt upgrade -y
  - sudo apt install openjdk-23-jdk redis-server mongodb -y
  - sudo apt install redis-tools -y
  - git clone https://JonathanJulio03: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@github.com/JonathanJulio03/challenge-coupon.git coupon
  - sudo apt install certbot python3-certbot-nginx
  - sudo certbot --nginx
  - sudo apt update && sudo apt install docker.io docker-compose -y
  - chmod +x mvnw
  - docker-compose up --build -d

  **Logs**
  - docker logs -f coupon-app

  **Config redirect**
  - sudo nano /etc/nginx/sites-available/default
  - sudo systemctl restart nginx