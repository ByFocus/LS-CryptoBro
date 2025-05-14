package Persistance;

import Business.Entities.Crypto;
import Business.Entities.Purchase;
import Business.Entities.User;
import Persistance.PersistanceExceptions.PersistanceException;

import java.util.List;

public interface PurchaseDAO {
    boolean addPurchase(User user, Purchase purchase)  throws PersistanceException;
    List<String> getUsernamesByCryptoName(String cryptoName) throws PersistanceException;
    List<Purchase> getPurchasesByUserName(String user) throws PersistanceException;
}