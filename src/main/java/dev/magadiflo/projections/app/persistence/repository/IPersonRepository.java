package dev.magadiflo.projections.app.persistence.repository;

import dev.magadiflo.projections.app.persistence.entity.Person;
import dev.magadiflo.projections.app.persistence.projections.IPersonFullLocation;
import dev.magadiflo.projections.app.persistence.projections.IPersonLocation;
import dev.magadiflo.projections.app.persistence.projections.IPersonLocationDefault;
import dev.magadiflo.projections.app.persistence.projections.PersonLocationDTO2;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface IPersonRepository extends JpaRepository<Person, Long> {

    // Using Closed Projection
    @Query(value = """
            SELECT p.name AS name, p.phone_number AS phoneNumber, a.street AS street
            FROM persons AS p
                INNER JOIN addresses AS a ON(p.address_id = a.id)
            WHERE p.id = :id
            """, nativeQuery = true)
    IPersonLocation getPersonLocation(@Param("id") Long personId);

    // Using Open Projection
    @Query(value = """
            SELECT p.name AS name, p.phone_number AS phoneNumber, a.street AS street
            FROM persons AS p
                INNER JOIN addresses AS a ON(p.address_id = a.id)
            WHERE p.id = :id
            """, nativeQuery = true)
    IPersonFullLocation getPersonFullLocation(@Param("id") Long personId);

    // Using a projection interface using a default method for custom logic
    @Query(value = """
            SELECT p.name AS name, p.phone_number AS phoneNumber, a.street AS street
            FROM persons AS p
                INNER JOIN addresses AS a ON(p.address_id = a.id)
            WHERE p.id = :id
            """, nativeQuery = true)
    IPersonLocationDefault getPersonLocationDefault(@Param("id") Long personId);

    // Class based
    @Query(value = """
            SELECT p.name AS name, p.phone_number AS phoneNumber, a.street AS street
            FROM persons AS p
                INNER JOIN addresses AS a ON(p.address_id = a.id)
            WHERE p.id = :id
            """, nativeQuery = true)
    Tuple getPersonLocationDTO(@Param("id") Long personId);

    /**
     * Using named query
     * <p>
     * El getPersonLocationDTONamingQuery, debe estar en una anotación dentro de su entidad
     * correspondiente
     */
    @Query(name = "getPersonLocationDTONamingQuery", nativeQuery = true)
    PersonLocationDTO2 getPersonLocationDTO2(@Param("id") Long personId);

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

    @Query(value = """
            SELECT p.name AS name, p.phone_number AS phoneNumber, a.street AS street
            FROM persons AS p
                INNER JOIN addresses AS a ON(p.address_id = a.id)
            WHERE p.name LIKE %:name%
            """, nativeQuery = true)
    <T> Collection<T> getPersonLocationDynamicallyList(@Param("name") String name, Class<T> type);
}
