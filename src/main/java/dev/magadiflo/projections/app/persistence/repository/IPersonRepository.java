package dev.magadiflo.projections.app.persistence.repository;

import dev.magadiflo.projections.app.persistence.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPersonRepository extends JpaRepository<Person, Long> {
}
