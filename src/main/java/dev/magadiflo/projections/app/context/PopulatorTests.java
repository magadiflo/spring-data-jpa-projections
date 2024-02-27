package dev.magadiflo.projections.app.context;

import dev.magadiflo.projections.app.persistence.entity.Address;
import dev.magadiflo.projections.app.persistence.entity.Person;
import dev.magadiflo.projections.app.persistence.repository.IAddressRepository;
import dev.magadiflo.projections.app.persistence.repository.IPersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * La interfaz InitializingBean es una interfaz que se encuentra en el paquete org.springframework.beans.factory
 * de Spring Framework. Esta interfaz define un solo método llamado afterPropertiesSet(), que una clase puede
 * implementar para realizar acciones de inicialización después de que todas las propiedades necesarias hayan
 * sido establecidas por Spring.
 * <p>
 * Cuando un bean implementa la interfaz InitializingBean, Spring Framework llama automáticamente al método
 * afterPropertiesSet() después de que se hayan establecido todas las propiedades del bean y antes de que se
 * ponga en servicio el bean.
 */

@RequiredArgsConstructor
@Slf4j
@Component
public class PopulatorTests implements InitializingBean {

    private final IAddressRepository addressRepository;
    private final IPersonRepository personRepository;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Iniciamos con la población de la base de datos");
        this.populate();
        log.info("Finalización de la población de la base de datos");
    }

    private void populate() {
        this.personRepository.deleteAll();
        this.addressRepository.deleteAll();

        for (int i = 0; i < 9; i++) {
            Address addressDB = this.addressRepository.save(this.getRandomAddress(i));
            Person randomPerson = this.getRandomPerson(i);
            randomPerson.setAddressId(addressDB.getId());
            this.personRepository.save(randomPerson);
        }
    }

    private Address getRandomAddress(int i) {
        Address address = new Address();
        address.setCity("City " + i);
        address.setCountry("Country " + i);
        address.setPostalCode(i);
        address.setState("State " + i);
        address.setStreet("Street " + i);
        return address;
    }

    private Person getRandomPerson(int i) {
        Person person = new Person();
        person.setName("Persona " + i);
        person.setEmail("person_" + i + "@gmail.com");
        person.setPhoneNumber("92" + i + "63248" + i);
        return person;
    }
}
