package Business.Entities;

public class Crypto {
    private String name;
    private String category;
    private double currentPrice;
    private double initialPrice;
    private int volatility;

    public Crypto(String name, String category, double currentPrice, double initialPrice, int volatility) {
        this.name = name;
        this.category = category;
        this.currentPrice = currentPrice;
        this.initialPrice = initialPrice;
        this.volatility = volatility;
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getCurrentPrice() { return currentPrice; }
    public double getInitialPrice() { return initialPrice; }
    public int getVolatility() { return volatility; }

    public void setCurrentPrice(double currentPrice) { this.currentPrice = currentPrice; }
    //public void setInitialPrice(double initialPrice) { this.initialPrice = initialPrice; }

    public void buy() {
        currentPrice = currentPrice * 1.01;
    }

    public void sell() {
        currentPrice = currentPrice * 0.99; // amb aquesta operació en principi mai està per sota de zero
    }

    public double calculateMarketGap() {
        return currentPrice - initialPrice;
    }
}
