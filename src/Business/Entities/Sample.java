package Business.Entities;

import java.util.Date;

/**
 * The type Sample.
 * Represents a historical data point for a cryptocurrency, including price and timestamp.
 */
public class Sample {
    private final double price;
    private final Date date;

    /**
     * Instantiates a new Sample.
     *
     * @param price the price of the cryptocurrency at a specific time
     * @param date  the date and time of the sample
     */
    public Sample(double price, Date date) {
        this.price = price;
        this.date = date;
    }

    /**
     * Gets the price of the cryptocurrency.
     *
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Gets the timestamp of the sample.
     *
     * @return the date
     */
    public Date getDate() {
        return date;
    }
}
