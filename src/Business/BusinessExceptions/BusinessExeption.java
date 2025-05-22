package Business.BusinessExceptions;

/**
 * The type Business exeption.
 */
public abstract class BusinessExeption extends RuntimeException {
    /**
     * Instantiates a new Business exeption.
     *
     * @param message the message
     */
    public BusinessExeption(String message) {
        super(message);
    }
}
