package Business.Entities;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private double balance;
    private boolean cryptoViewFlag;
    private List<Purchase> purchases;

    public User(String username, String password, double balance, boolean cryptoViewFlag) {
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.cryptoViewFlag = cryptoViewFlag;
        this.purchases = new ArrayList<>();
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public double getBalance() { return balance; }
    public boolean getCryptoViewFlag() { return cryptoViewFlag; }

    public void setBalance(double balance) { this.balance = balance; }
    public void setCryptoViewFlag(boolean flag) { this.cryptoViewFlag = flag; }

    public void addPurchase(Purchase purchase) {
        purchases.add(purchase);
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }
}
