package Business.Entities;

public class Purchase {
    private String crypto;
    private int units;
    private double priceUnit;

    public Purchase(String cryptoName, int units, double priceUnit) {
        this.crypto = cryptoName;
        this.units = units;
        this.priceUnit = priceUnit;
    }

    public String getCrypto() { return crypto; }
    public int getUnits() { return units; }
    public double getPriceUnit() { return priceUnit; }
}
