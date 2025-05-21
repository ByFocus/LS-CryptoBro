package Persistance;

import Business.Entities.Crypto;
import Business.Entities.Purchase;
import Business.Entities.User;
import Persistance.PersistanceExceptions.PersistanceException;

import java.util.List;

public interface PurchaseDAO {
    void addPurchase(User user, Purchase purchase)  throws PersistanceException;
    List<String> getUsernamesByCryptoName(String cryptoName) throws PersistanceException;
    List<Purchase> getPurchasesByUserName(String user) throws PersistanceException;
    void substractUnits(Purchase purchase, String username, int unitsToSubstract) throws PersistanceException;
    double sellAllPurchasesFromCrypto(String cryptoName, String userName) throws PersistanceException;
}