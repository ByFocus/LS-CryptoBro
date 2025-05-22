package Business.Entities;

import java.util.List;

/**
 * The type User.
 */
public class User {
    private String username;
    private String mail;
    private String password;
    private double balance;
    private boolean cryptoDeletedFlag;
    //private List<Purchase> purchases;

    /**
     * Instantiates a new User.
     *
     * @param username          the username
     * @param password          the password
     * @param mail              the mail
     * @param balance           the balance
     * @param cryptoDeletedFlag the crypto deleted flag
     */
    public User(String username, String password, String mail, double balance, boolean cryptoDeletedFlag) {
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.balance = balance;
        this.cryptoDeletedFlag = cryptoDeletedFlag;
       // purchases = null;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() { return username; }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() { return password; }

    /**
     * Gets mail.
     *
     * @return the mail
     */
    public String getMail() { return mail; }

    /**
     * Gets balance.
     *
     * @return the balance
     */
    public double getBalance() { return balance; }

    /**
     * Gets crypto deleted flag.
     *
     * @return the crypto deleted flag
     */
    public boolean getCryptoDeletedFlag() { return cryptoDeletedFlag; }

    /**
     * Sets balance.
     *
     * @param balance the balance
     */
    public void setBalance(double balance) { this.balance = balance; }

    /**
     * Sets crypto deleted flag.
     *
     * @param flag the flag
     */
    public void setCryptoDeletedFlag(boolean flag) { this.cryptoDeletedFlag = flag; }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) { this.password = password; }
   /* public void addPurchase(Purchase purchase) {
        purchases.add(purchase);
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }*/
}
