package Business.Entities;

/**
 * The type Purchase.
 * Represents a cryptocurrency purchase made by a user, including the crypto name, number of units, and price per unit.
 */
public class Purchase {
    private String crypto;
    private int units;
    private double priceUnit;

    /**
     * Instantiates a new Purchase.
     *
     * @param cryptoName the name of the cryptocurrency
     * @param units      the number of units purchased
     * @param priceUnit  the price per unit at the time of purchase
     */
    public Purchase(String cryptoName, int units, double priceUnit) {
        this.crypto = cryptoName;
        this.units = units;
        this.priceUnit = priceUnit;
    }

    /**
     * Gets the name of the cryptocurrency.
     *
     * @return the crypto name
     */
    public String getCrypto() { return crypto; }

    /**
     * Gets the number of units purchased.
     *
     * @return the number of units
     */
    public int getUnits() { return units; }

    /**
     * Gets the price per unit at the time of purchase.
     *
     * @return the unit price
     */
    public double getPriceUnit() { return priceUnit; }

}
