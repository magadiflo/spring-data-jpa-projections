package dev.magadiflo.projections.app.web;

import dev.magadiflo.projections.app.persistence.projections.*;
import dev.magadiflo.projections.app.persistence.repository.IPersonRepository;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
