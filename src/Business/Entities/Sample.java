package Business.Entities;

import java.util.Date;

/**
 * The type Muestra.
 */
public class Sample {
    private final double price;
    private final Date date;

    /**
     * Instantiates a new Muestra.
     *
     * @param price the precio
     * @param date  the fecha
     */
    public Sample(double price, Date date) {
        this.price = price;
        this.date = date;
    }

    /**
     * Gets precio.
     *
     * @return the precio
     */
    public double getPrice() {
        return price;
    }

    /**
     * Gets fecha.
     *
     * @return the fecha
     */
    public Date getDate() {
        return date;
    }

}
