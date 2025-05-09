package Business.Entities;

public class Purchase {
    private String crypto;
    private int units;
    private double priceUnit;

    public Purchase(Crypto crypto, int units, double priceUnit) {
        this.crypto = crypto.getName();
        this.units = units;
        this.priceUnit = priceUnit;
    }

    public String getCrypto() { return crypto; }
    public double getUnits() { return units; }
    public double getPriceUnit() { return priceUnit; }
}
