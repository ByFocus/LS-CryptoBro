package Business.Entities;

import java.util.List;

public class User {
    private String username;
    private String mail;
    private String password;
    private double balance;
    private boolean cryptoDeletedFlag;
    //private List<Purchase> purchases;

    public User(String username, String password, String mail, double balance, boolean cryptoDeletedFlag) {
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.balance = balance;
        this.cryptoDeletedFlag = cryptoDeletedFlag;
       // purchases = null;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getMail() { return mail; }
    public double getBalance() { return balance; }
    public boolean getCryptoDeletedFlag() { return cryptoDeletedFlag; }

    public void setBalance(double balance) { this.balance = balance; }
    public void setCryptoDeletedFlag(boolean flag) { this.cryptoDeletedFlag = flag; }

   /* public void addPurchase(Purchase purchase) {
        purchases.add(purchase);
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }*/
}
