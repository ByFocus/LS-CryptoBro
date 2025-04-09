package Business.Entities;

public class Purchase {
    private Crypto crypto;
    private int units;
    private double priceUnit;

    public Purchase(Crypto crypto, int units, double priceUnit) {
        this.crypto = crypto;
        this.units = units;
        this.priceUnit = priceUnit;
    }

    public Crypto getCrypto() { return crypto; }
    public double getUnits() { return units; }
    public double getPriceUnit() { return priceUnit; }
}
