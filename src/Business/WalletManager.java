package Business;

import Business.BusinessExceptions.UnsufficientBalance;
import Business.Entities.*;
import Persistance.PurchaseDAO;
import Persistance.PurchaseSQLDAO;

import java.util.List;

public class WalletManager {

     static String NOT_ENOUGH_BALANCE = "Bro, no tienes suficiente dinero, conoce tu lugar";

    public WalletManager() {

    }
    public void addTransaction(User user, Crypto crypto, int units) {
        if (crypto.getCurrentPrice() * units >= user.getBalance()) {
            Purchase p = new Purchase(crypto, units, crypto.getCurrentPrice());
            user.addPurchase(p);
            PurchaseDAO purchaseDAO = new PurchaseSQLDAO();
            purchaseDAO.addTransaction(user.getUsername(), p);
        } else {
            throw new UnsufficientBalance(NOT_ENOUGH_BALANCE);
        }
    }
    public List<Purchase> getWalletByUserName(String username) {
        PurchaseDAO purchaseDao = new PurchaseSQLDAO();
        return purchaseDao.getPurchasesByUserName();
    }
    public void recalculateGainsUser(User user) {

        List <Purchase> purchases = user.getPurchases();
        double gains = 0;
        for (Purchase purchase : purchases) {
            Crypto crypto;
            // TODO:obté la crypto de cada purchase
            //crypto = new CryptoManager().getCryptoByName(purchase.getCryptoName());
            crypto = new Crypto("PROBA", "PROBA", 2350, 1000, 3);
            gains+= purchase.getUnits() * (crypto.getCurrentPrice() -purchase.getPriceUnit());
        }
        user.setEstimatedGains(gains);
    }
}
