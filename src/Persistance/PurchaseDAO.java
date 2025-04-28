package Persistance;

import Business.Entities.Purchase;
import Business.Entities.User;

public interface PurchaseDAO {
    boolean addPurchase(User user, Purchase purchase);
    String[] getUsernamesByCryptoName(String cryptoName);
}