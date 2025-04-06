package Business.Entities;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private double balance;
    private double estimatedGains;
    private boolean cryptoDeletedFlag;
    private List<Purchase> purchases;

    public User(String username, String password, double balance, boolean cryptoViewFlag) {
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.cryptoDeletedFlag = cryptoViewFlag;
        this.purchases = new ArrayList<>();
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public double getBalance() { return balance; }
    public boolean getCryptoDeletedFlag() { return cryptoDeletedFlag; }

    public void setBalance(double balance) { this.balance = balance; }
    public void setCryptoDeletedFlag(boolean flag) { this.cryptoDeletedFlag = flag; }

    public void addPurchase(Purchase purchase) {
        purchases.add(purchase);
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }
}
