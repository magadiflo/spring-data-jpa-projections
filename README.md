# [Proyecciones: Recuperar datos con Spring/Boot con mejor rendimiento (Spring Data Projections)](https://www.youtube.com/watch?v=64CjU6xNdx4)

- Tutorial tomado del canal de youtube de **SACAViX Tech**
- [Documentación Oficial de Spring Projections](https://docs.spring.io/spring-data/jpa/reference/repositories/projections.html)

## ¿Qué es una proyección?

Es un mecanismo por el cual podemos obtener o acceder a elementos particulares de nuestro modelo de datos.

`Spring Data Projection` permite hacer uso de este mecanismo en Spring.

## Tipos de proyecciones

- Interfaz cerrada
- Interfaz abierta
- Basada en clase
- Basada en clase usando NamedQuery
- Proyección dinámica

## Dependencias

````xml
<!--Spring Boot 3.2.3-->
<!--Java 21-->
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
````

## Configuraciones del application.yml

````yml
server:
  port: 8080
  error:
    include-message: always

spring:
  application:
    name: spring-boot-jpa-projections

  datasource:
    url: jdbc:mysql://localhost:3306/db_spring_data_jpa
    username: admin
    password: magadiflo

  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        format_sql: true

logging:
  level:
    org.hibernate.SQL: DEBUG
````

## Entidades

````java

@Data
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String street;
    private String city;
    private String state;
    private Integer postalCode;
    private String country;
}
````

````java

@Data
@Entity
@Table(name = "persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phoneNumber;
    private String email;
    private Long addressId;
}
````

## Creando tablas

Al ejecutar la aplicación, las tablas se crean automáticamente gracias a la configuración que establecimos en el
`application.yml`. A continuación se muestran el diagrama de las tablas generadas.

![Entities](./assets/01.entities.png)

Aunque no definimos explícitamente la relación entre personas y direcciones, esa relación lo haremos a nivel de código,
por esa razón es que en la entidad `persons` establecimos el atributo `address_id` donde guardaremos la referencia
a la tabla `address`.
