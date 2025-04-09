package Business.BusinessExceptions;

public class UnsufficientBalance extends RuntimeException {
    public UnsufficientBalance(String message) {
        super(message);
    }
}
