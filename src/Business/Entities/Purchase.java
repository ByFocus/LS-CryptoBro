package Business.Entities;

/**
 * The type Purchase.
 */
public class Purchase {
    private String crypto;
    private int units;
    private double priceUnit;

    /**
     * Instantiates a new Purchase.
     *
     * @param cryptoName the crypto name
     * @param units      the units
     * @param priceUnit  the price unit
     */
    public Purchase(String cryptoName, int units, double priceUnit) {
        this.crypto = cryptoName;
        this.units = units;
        this.priceUnit = priceUnit;
    }

    /**
     * Gets crypto.
     *
     * @return the crypto
     */
    public String getCrypto() { return crypto; }

    /**
     * Gets units.
     *
     * @return the units
     */
    public int getUnits() { return units; }

    /**
     * Gets price unit.
     *
     * @return the price unit
     */
    public double getPriceUnit() { return priceUnit; }

}
