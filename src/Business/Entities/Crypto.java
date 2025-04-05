package Business.Entities;

public class Crypto {
    private String name;
    private String market;
    private double lastChange;
    private double lastValue;
    private double volatility;

    public Crypto(String name, String market, double lastChange, double lastValue, double volatility) {
        this.name = name;
        this.market = market;
        this.lastChange = lastChange;
        this.lastValue = lastValue;
        this.volatility = volatility;
    }

    public String getName() { return name; }
    public String getMarket() { return market; }
    public double getLastChange() { return lastChange; }
    public double getLastValue() { return lastValue; }
    public double getVolatility() { return volatility; }

    public void setLastChange(double lastChange) { this.lastChange = lastChange; }
    public void setLastValue(double lastValue) { this.lastValue = lastValue; }
}
