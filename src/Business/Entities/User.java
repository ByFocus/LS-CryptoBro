package Business.Entities;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private double balance;
    private double estimatedGains; //TODO: canviar i fer que es recalculi en el "view", per evitar guard-lo, i així en el wallet manager només se li ha de passar el userName, y ha de retornar el valor
    private boolean cryptoDeletedFlag;
    private List<Purchase> purchases;

    public User(String username, String password, double balance, boolean cryptoViewFlag) {
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.cryptoDeletedFlag = cryptoViewFlag;
        this.purchases = new ArrayList<>();
        this.estimatedGains = 0;
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
    public void setEstimatedGains(double estimatedGains) {
        this.estimatedGains = estimatedGains;
    }
}
