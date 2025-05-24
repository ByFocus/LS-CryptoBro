package Business;

/**
 * The enum EventType.
 * Defines types of events that can be triggered in the business logic layer
 * for notifying observers, such as updates in data or system state.
 */
public enum EventType {
    /**
     * Indicates that one or more cryptocurrency prices have changed.
     */
    CRYPTO_VALUES_CHANGED,

    /**
     * Indicates that the user's balance has been updated.
     */
    USER_BALANCE_CHANGED,

    /**
     * Indicates that a new historical value has been recorded for a cryptocurrency.
     */
    NEW_HISTORICAL_VALUE,

    /**
     * Indicates that the user's estimated gains have changed.
     */
    USER_ESTIMATED_GAINS_CHANGED,

    /**
     * Indicates that new cryptocurrencies have been added to the system.
     */
    NEW_CRYPTOS
}
