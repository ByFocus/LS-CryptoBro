package Business.BusinessExceptions;

public abstract class BusinessExeption extends RuntimeException {
    public BusinessExeption(String message) {
        super(message);
    }
}
