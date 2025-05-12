package Business.BusinessExceptions;

public class UserAuthentificationError extends BusinessExeption {
    public UserAuthentificationError(String message) {
        super("Bro, error con el user: " + message);
    }
}
