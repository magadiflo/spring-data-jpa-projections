package dev.magadiflo.projections.app.persistence.entity;

import dev.magadiflo.projections.app.persistence.projections.PersonLocationDTO2;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;
import lombok.Data;

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
