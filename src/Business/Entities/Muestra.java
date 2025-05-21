package Business.Entities;

import java.util.Date;

public class Muestra {
    private final double precio;
    private final Date fecha;

    public Muestra(double precio, Date fecha) {
        this.precio = precio;
        this.fecha = fecha;
    }

    public double getPrecio() {
        return precio;
    }

    public Date getFecha() {
        return fecha;
    }

}
