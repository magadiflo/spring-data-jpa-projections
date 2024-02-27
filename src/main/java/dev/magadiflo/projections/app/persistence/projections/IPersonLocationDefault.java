package dev.magadiflo.projections.app.persistence.projections;

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
