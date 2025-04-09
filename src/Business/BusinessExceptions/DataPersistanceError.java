package Business.BusinessExceptions;

public class DataPersistanceError extends RuntimeException {
    public DataPersistanceError(String message) {
        super(message);
    }
}
