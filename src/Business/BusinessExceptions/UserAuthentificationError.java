package Business.BusinessExceptions;

public class UserAuthentificationError extends RuntimeException {
    public UserAuthentificationError(String message) {
        super(message);
    }
}
