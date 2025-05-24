package Business.Entities;

/**
 * The type Crypto.
 * Represents a cryptocurrency with market information such as price and volatility.
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
     * @param name         the name of the cryptocurrency
     * @param category     the category (e.g., utility, security)
     * @param currentPrice the current market price
     * @param initialPrice the initial market price
     * @param volatility   the volatility factor
     */
    public Crypto(String name, String category, double currentPrice, double initialPrice, int volatility) {
        this.name = name;
        this.category = category;
        this.currentPrice = currentPrice;
        this.initialPrice = initialPrice;
        this.volatility = volatility;
    }

    /**
     * Gets the name of the cryptocurrency.
     *
     * @return the name
     */
    public String getName() { return name; }

    /**
     * Gets the category of the cryptocurrency.
     *
     * @return the category
     */
    public String getCategory() { return category; }

    /**
     * Gets the current market price of the cryptocurrency.
     *
     * @return the current price
     */
    public double getCurrentPrice() { return currentPrice; }

    /**
     * Gets the initial market price of the cryptocurrency.
     *
     * @return the initial price
     */
    public double getInitialPrice() { return initialPrice; }

    /**
     * Gets the volatility factor of the cryptocurrency.
     *
     * @return the volatility
     */
    public int getVolatility() { return volatility; }

    /**
     * Calculates the market gap (difference between current and initial price).
     *
     * @return the market gap as a double
     */
    public double calculateMarketGap() {
        return currentPrice - initialPrice;
    }
}
