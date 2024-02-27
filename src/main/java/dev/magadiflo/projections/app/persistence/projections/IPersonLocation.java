package dev.magadiflo.projections.app.persistence.projections;

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
