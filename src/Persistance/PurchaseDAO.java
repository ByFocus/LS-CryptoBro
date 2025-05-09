package Persistance;

import Business.Entities.Crypto;
import Business.Entities.Purchase;
import Business.Entities.User;

import java.util.List;

public interface PurchaseDAO {
    boolean addPurchase(User user, Purchase purchase);
    List<String> getUsernamesByCryptoName(String cryptoName);
    List<Purchase> getPurchasesByUserName(String user);
}