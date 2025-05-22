package Persistance.PersistanceExceptions;

/**
 * The type Persistance exception.
 */
public abstract class PersistanceException extends Exception {
    /**
     * Instantiates a new Persistance exception.
     *
     * @param message the message
     */
    public PersistanceException(String message) {
        super(message);
    }
}
