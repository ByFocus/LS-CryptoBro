package Business.BusinessExceptions;

public class CryptoDelated extends RuntimeException {
    public CryptoDelated(String message) {
        super(message);
    }
}
