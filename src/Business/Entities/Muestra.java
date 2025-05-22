package Business.Entities;

import java.util.Date;

/**
 * The type Muestra.
 */
public class Muestra {
    private final double precio;
    private final Date fecha;

    /**
     * Instantiates a new Muestra.
     *
     * @param precio the precio
     * @param fecha  the fecha
     */
    public Muestra(double precio, Date fecha) {
        this.precio = precio;
        this.fecha = fecha;
    }

    /**
     * Gets precio.
     *
     * @return the precio
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Gets fecha.
     *
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

}
