package Business;

import Business.Entities.*;

import java.util.List;

public class WalletManager {
    public WalletManager() {

    }
    public void addTransaction(User user, Crypto crypto, int units) {

    }
    public List<Purchase> getWalletByUserName(String username) {
        return null;
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
