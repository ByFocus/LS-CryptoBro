package Business.Entities;

/**
 * The type User.
 * Represents a user of the system with credentials, balance, and crypto activity status.
 */
public class User {
    private String username;
    private String mail;
    private String password;
    private double balance;
    private boolean cryptoDeletedFlag;

    /**
     * Instantiates a new User.
     *
     * @param username          the username of the user
     * @param password          the password of the user
     * @param mail              the email address of the user
     * @param balance           the current balance of the user
     * @param cryptoDeletedFlag indicates if user's crypto has been marked as deleted
     */
    public User(String username, String password, String mail, double balance, boolean cryptoDeletedFlag) {
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.balance = balance;
        this.cryptoDeletedFlag = cryptoDeletedFlag;
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() { return username; }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() { return password; }

    /**
     * Gets the email.
     *
     * @return the email
     */
    public String getMail() { return mail; }

    /**
     * Gets the account balance.
     *
     * @return the balance
     */
    public double getBalance() { return balance; }

    /**
     * Checks if the user's crypto has been marked as deleted.
     *
     * @return the crypto deleted flag
     */
    public boolean getCryptoDeletedFlag() { return cryptoDeletedFlag; }

    /**
     * Sets the account balance.
     *
     * @param balance the new balance
     */
    public void setBalance(double balance) { this.balance = balance; }
}
