package Business.Entities;

/**
 * The type Crypto.
 */
public class Crypto {
    private String name;
    private String category;
    private double currentPrice;
    private double initialPrice;
    private int volatility;

    /**
     * Instantiates a new Crypto.
     *
     * @param name         the name
     * @param category     the category
     * @param currentPrice the current price
     * @param initialPrice the initial price
     * @param volatility   the volatility
     */
    public Crypto(String name, String category, double currentPrice, double initialPrice, int volatility) {
        this.name = name;
        this.category = category;
        this.currentPrice = currentPrice;
        this.initialPrice = initialPrice;
        this.volatility = volatility;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() { return name; }

    /**
     * Gets category.
     *
     * @return the category
     */
    public String getCategory() { return category; }

    /**
     * Gets current price.
     *
     * @return the current price
     */
    public double getCurrentPrice() { return currentPrice; }

    /**
     * Gets initial price.
     *
     * @return the initial price
     */
    public double getInitialPrice() { return initialPrice; }

    /**
     * Gets volatility.
     *
     * @return the volatility
     */
    public int getVolatility() { return volatility; }

    /**
     * Sets current price.
     *
     * @param currentPrice the current price
     */
    public void setCurrentPrice(double currentPrice) { this.currentPrice = currentPrice; }
    //public void setInitialPrice(double initialPrice) { this.initialPrice = initialPrice; }

    /**
     * Buy.
     */
    public void buy() {
        currentPrice = currentPrice * 1.01;
    }

    /**
     * Sell.
     */
    public void sell() {
        currentPrice = currentPrice * 0.99; // amb aquesta operació en principi mai està per sota de zero
    }

    /**
     * Calculate market gap double.
     *
     * @return the double
     */
    public double calculateMarketGap() {
        return currentPrice - initialPrice;
    }
}
