# [Proyecciones: Recuperar datos con Spring/Boot con mejor rendimiento (Spring Data Projections)](https://www.youtube.com/watch?v=64CjU6xNdx4)

- Tutorial tomado del canal de youtube de **SACAViX Tech**
- [Documentación Oficial de Spring Projections](https://docs.spring.io/spring-data/jpa/reference/repositories/projections.html)
- [Uso de Projections en proyecto spring-boot-web-crud]( https://github.com/magadiflo/spring-boot-web-crud.git)

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
      ddl-auto: create-drop
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

## Creando proyecciones

Utilizando el repositorio `IPersonRepository` implementamos los distintos tipos de proyecciones.

### Proyección cerrada

Como observamos, el método retorna una interfaz de proyección cerrada.

````java
public interface IPersonRepository extends JpaRepository<Person, Long> {
    // Using Closed Projection
    @Query(value = """
            SELECT p.name AS name, p.phone_number AS phoneNumber, a.street AS street
            FROM persons AS p
                INNER JOIN addresses AS a ON(p.address_id = a.id)
            WHERE p.id = :id
            """, nativeQuery = true)
    IPersonLocation getPersonLocation(@Param("id") Long personId);
}
````

La interfaz de proyección cerrada define los atributos que la consulta anterior retorna anteponiendo el `get`,
por ejemplo para el `name` definimos el `getName()`, así con los demás atributos:

````java

/**
 * Interface Closed Projection
 * <p>
 * Una interfaz de proyección cuyos métodos de acceso coinciden con las propiedades
 * del agregado de destino se considera una proyección cerrada.
 */
public interface IPersonLocation {
    String getName();

    String getPhoneNumber();

    String getStreet();
}
````

**IMPORTANTE**
> Es importante que en la `consulta SQL`, a cada columna le definamos un `Alias`, ese `Alias` deberá ser igual al
> atributo que definimos en la entidad. Por ejemplo, en la entidad `Person` definimos el atributo llamado `phoneNumber`,
> eso significa que el alias deberá tener el mismo nombre `phoneNumber`.

### Proyección abierta - con @Value

Todas las consultas serán las mismas, lo que cambia es el tipo de proyección que se retorna. En este caso, estamos
retornando una proyección abierta, ya que la interfaz `IPersonFullLocation` define un atributo con el `@Value`:

````java
public interface IPersonRepository extends JpaRepository<Person, Long> {
    // Using Open Projection
    @Query(value = """
            SELECT p.name AS name, p.phone_number AS phoneNumber, a.street AS street
            FROM persons AS p
                INNER JOIN addresses AS a ON(p.address_id = a.id)
            WHERE p.id = :id
            """, nativeQuery = true)
    IPersonFullLocation getPersonFullLocation(@Param("id") Long personId);
}
````

Usamos la anotación `@Value` e internamente usamos el `target` que contendrá el objeto consultado.

````java
/**
 * Interface Open Projection - with @Value
 * <p>
 * Los métodos de acceso en las interfaces de proyección también se pueden utilizar para
 * calcular nuevos valores mediante la anotación @Value.
 * <p>
 * Una interfaz de proyección que utiliza @Value es una proyección abierta. Spring Data no
 * puede aplicar optimizaciones de ejecución de consultas en este caso, porque la expresión
 * SpEL podría usar cualquier atributo de la raíz agregada.
 */
public interface IPersonFullLocation {
    @Value("#{target.name + ' ' + target.phoneNumber + ' ' + target.street}")
    String getFullLocation();
}
````

### Proyección abierta - con Default method

A diferencia de la anotación `@Value`, en este tipo de proyección retornado por la consulta, podemos hacer uso
del método por `default` que nos ofrece las interfaces en java:

````java
public interface IPersonRepository extends JpaRepository<Person, Long> {
    // Using a projection interface using a default method for custom logic
    @Query(value = """
            SELECT p.name AS name, p.phone_number AS phoneNumber, a.street AS street
            FROM persons AS p
                INNER JOIN addresses AS a ON(p.address_id = a.id)
            WHERE p.id = :id
            """, nativeQuery = true)
    IPersonLocationDefault getPersonLocationDefault(@Param("id") Long personId);
}
````

````java
/**
 * Interface Open Projection - with Default Method
 * <p>
 * Una interfaz de proyección que utiliza un método predeterminado para
 * lógica personalizada.
 * <p>
 * Este enfoque requiere que usted pueda implementar lógica basada exclusivamente en
 * los otros métodos de acceso expuestos en la interfaz de proyección.
 */
public interface IPersonLocationDefault {
    String getName();

    String getPhoneNumber();

    String getStreet();

    default String getFullLocation() {
        return getName() + ": " + getPhoneNumber() + " - " + getStreet();
    }
}
````

### Proyección basado en clase

Este tipo de proyección usa la interfaz `Tuple` importado de `jakarta.persistence`. Interfaz para extraer los elementos
de una tupla de resultados de consulta.

````java
// Class based
@Query(value = """
        SELECT p.name AS name, p.phone_number AS phoneNumber, a.street AS street
        FROM persons AS p
            INNER JOIN addresses AS a ON(p.address_id = a.id)
        WHERE p.id = :id
        """, nativeQuery = true)
Tuple getPersonLocationDTO(@Param("id") Long personId);
````

El resultado que nos retorne la consulta, será del tipo `Tuple`. Ahora, para tener un mejor control de los datos debemos
extraer el resultado de la tupla usando sus métodos como el `.get()`, y los valores rellenarlos en otra clase. En
nuestro caso, los valores contenidos en la tupla serán rellenados en el record siguiente:

````java
// Usado en la proyección que recupera una Tuple y los datos serán volcados a este record
public record PersonLocationDTO(String name, String phoneNumber, String street) {
}
````

Más adelante veremos cómo poblar este record con los datos de la tuple, para ser exactos, eso se hará en su endpoint
correspondiente.

### Proyección usando Named Query

Esta consulta devolverá el record `PersonLocationDTO2`, pero la construcción de la consulta misma es lo que varía.
En este caso le estamos dando un valor a la anotación query de nombre `getPersonLocationDTONamingQuery`.

````java
/**
 * Using named query
 * <p>
 * El getPersonLocationDTONamingQuery, debe estar en una anotación dentro de su entidad
 * correspondiente
 */
@Query(name = "getPersonLocationDTONamingQuery", nativeQuery = true)
PersonLocationDTO2 getPersonLocationDTO2(@Param("id") Long personId);
````

Definiendo el record que devolverá la consulta anterior:

````java
// Using named query
public record PersonLocationDTO2(String name, String phoneNumber, String street) {
}
````

Ahora, debemos ir a la entidad `Person` y agregar las anotación `@NamedNativeQuery y @SqlResultSetMapping` cons sus
configuraciones:

````java

@NamedNativeQuery(
        name = "getPersonLocationDTONamingQuery",
        query = """
                SELECT p.name AS name, p.phone_number AS phoneNumber, a.street AS street
                FROM persons AS p
                    INNER JOIN addresses AS a ON(p.address_id = a.id)
                WHERE p.id = :id
                """,
        resultSetMapping = "PersonLocationDTO2Mapping"
)
@SqlResultSetMapping(
        name = "PersonLocationDTO2Mapping",
        classes = @ConstructorResult(
                targetClass = PersonLocationDTO2.class,
                columns = {
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "phoneNumber", type = String.class),
                        @ColumnResult(name = "street", type = String.class)
                }
        )
)
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

Notar que en el `name` de la anotación `@NamedNativeQuery` es el mismo que definimos en el value de la anotación query.
En el `resultSetMapping` definimos el nombre `PersonLocationDTO2Mapping` que enlazará a la
anotación `@SqlResultSetMapping` a través de su `name`. En la anotación `@SqlResultSetMapping` definimos un
`targetClass` que en nuestro caso será el record que vimos en el apartado superior y las demás configuraciones se
sobreentiende.

### Proyección Dinámica

Hasta ahora, hemos utilizado el tipo de proyección como tipo de retorno o tipo de elemento de una colección. Sin
embargo, es posible que desee seleccionar el tipo que se utilizará en el momento de la invocación (lo que lo hace
dinámico). Para aplicar proyecciones dinámicas, utilice un método de consulta como el que se muestra en el siguiente
ejemplo:

````java
/**
 * Dynamically, usando proyección genérica.
 * <p>
 * El class type podría ser cualquier clase que hemos definido en las otras consultas,
 * ya que estamos trabajando todas sobre la misma consulta.
 */
@Query(value = """
        SELECT p.name AS name, p.phone_number AS phoneNumber, a.street AS street
        FROM persons AS p
            INNER JOIN addresses AS a ON(p.address_id = a.id)
        WHERE p.id = :id
        """, nativeQuery = true)
<T> T getPersonLocationDynamically(@Param("id") Long personId, Class<T> type);
````

Aquí definimos otra proyección dinámica, esta vez nos retorna una colección:

````java
public interface IPersonRepository extends JpaRepository<Person, Long> {
    @Query(value = """
            SELECT p.name AS name, p.phone_number AS phoneNumber, a.street AS street
            FROM persons AS p
                INNER JOIN addresses AS a ON(p.address_id = a.id)
            WHERE p.name LIKE %:name%
            """, nativeQuery = true)
    <T> Collection<T> getPersonLocationDynamicallyList(@Param("name") String name, Class<T> type);
}
````

## Creando métodos del rest controller

Para agilizar la creación del proyecto de ejemplo, utilizaremos el repositorio directamente en el controlador, pero
**¡OJO! Eso no se deben hacer, debemos usar una clase de servicio con su interfaz**.

````java

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/persons")
public class PersonRestController {

    /**
     * Como buena práctica se debe trabajar con servicios, pero por simplificar el
     * ejemplo, lo haremos directamente con el repositorio.
     */
    private final IPersonRepository personRepository;

    @GetMapping(path = "/interface-closed")
    public IPersonLocation getSampleInterfaceClosedProjection() {
        return this.personRepository.getPersonLocation(1L);
    }

    @GetMapping(path = "/interface-open")
    public IPersonFullLocation getSampleInterfaceOpenProjection() {
        return this.personRepository.getPersonFullLocation(1L);
    }

    @GetMapping(path = "/interface-open-default-method")
    public IPersonLocationDefault getSampleInterfaceOpenDefaultMethodProjection() {
        return this.personRepository.getPersonLocationDefault(1L);
    }

    @GetMapping(path = "/class-based")
    public PersonLocationDTO getSampleInterfaceClassBasedProjection() {
        Tuple tuple = this.personRepository.getPersonLocationDTO(1L);

        String name = tuple.get("name", String.class);
        String phoneNumber = tuple.get("phoneNumber", String.class);
        String street = tuple.get("street", String.class);

        return new PersonLocationDTO(name.toUpperCase(), phoneNumber.toUpperCase(), street.toUpperCase());
    }

    @GetMapping(path = "/class-based-named-query")
    public PersonLocationDTO2 getSampleInterfaceClassBasedNQProjection() {
        return this.personRepository.getPersonLocationDTO2(1L);
    }

    @GetMapping(path = "/dinamically")
    public Object getSampleInterfaceClassBaseDinamically() {
        return this.personRepository.getPersonLocationDynamically(1L, IPersonLocation.class);
    }

    @GetMapping(path = "/dinamically-list")
    public List<IPersonLocation> getSampleInterfaceClassBaseDinamicallyList() {
        return (List<IPersonLocation>) this.personRepository.getPersonLocationDynamicallyList("per", IPersonLocation.class);
    }

}
````

## Resultados

### Interfaz cerrada

````bash
$  curl -v http://localhost:8080/api/v1/persons/interface-closed | jq

>
< HTTP/1.1 200
<
{
  "name": "Persona 0",
  "street": "Street 0",
  "phoneNumber": "920632480"
}
````

### Interfaz abierta

````bash
$ curl -v http://localhost:8080/api/v1/persons/interface-open | jq

>
< HTTP/1.1 200
<
{
  "fullLocation": "Persona 0 920632480 Street 0"
}
````

### Interfaz abierta con métodos por default

````bash
$ curl -v http://localhost:8080/api/v1/persons/interface-open-default-method | jq

>
< HTTP/1.1 200
<
{
  "name": "Persona 0",
  "street": "Street 0",
  "phoneNumber": "920632480",
  "fullLocation": "Persona 0: 920632480 - Street 0"
}
````

### Basada en clase

````bash
$ url -v http://localhost:8080/api/v1/persons/class-based | jq

>
< HTTP/1.1 200
<
{
  "name": "PERSONA 0",
  "phoneNumber": "920632480",
  "street": "STREET 0"
}
````

### Basada en clase con named query

````bash
$ curl -v http://localhost:8080/api/v1/persons/class-based-named-query | jq

>
< HTTP/1.1 200
<
{
  "name": "Persona 0",
  "phoneNumber": "920632480",
  "street": "Street 0"
}
````

### Dinamically

````bash
$ curl -v http://localhost:8080/api/v1/persons/dinamically | jq

>
< HTTP/1.1 200
<

{
  "name": "Persona 0",
  "street": "Street 0",
  "phoneNumber": "920632480"
}
````

### Dinamically List

````bash
$ curl -v http://localhost:8080/api/v1/persons/dinamically-list | jq

< HTTP/1.1 200
<
[
  {
    "name": "Persona 0",
    "street": "Street 0",
    "phoneNumber": "920632480"
  },
  {
    "name": "Persona 1",
    "street": "Street 1",
    "phoneNumber": "921632481"
  },
  {
    "name": "Persona 2",
    "street": "Street 2",
    "phoneNumber": "922632482"
  },
  {
    "name": "Persona 3",
    "street": "Street 3",
    "phoneNumber": "923632483"
  },
  {
    "name": "Persona 4",
    "street": "Street 4",
    "phoneNumber": "924632484"
  },
  {
    "name": "Persona 5",
    "street": "Street 5",
    "phoneNumber": "925632485"
  },
  {
    "name": "Persona 6",
    "street": "Street 6",
    "phoneNumber": "926632486"
  },
  {
    "name": "Persona 7",
    "street": "Street 7",
    "phoneNumber": "927632487"
  },
  {
    "name": "Persona 8",
    "street": "Street 8",
    "phoneNumber": "928632488"
  }
]
````