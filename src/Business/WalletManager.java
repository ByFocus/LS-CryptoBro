package Business;

import Business.BusinessExceptions.BusinessExeption;
import Business.BusinessExceptions.DataPersistanceError;
import Business.BusinessExceptions.UnsufficientBalance;
import Business.Entities.*;
import Persistance.PersistanceExceptions.PersistanceException;
import Persistance.PurchaseDAO;
import Persistance.PurchaseSQLDAO;

import javax.print.attribute.standard.RequestingUserName;
import java.util.List;

public class WalletManager {
     static String NOT_ENOUGH_BALANCE = "Bro, no tienes suficiente dinero, conoce tu lugar";

     public static WalletManager instance;

    public WalletManager() {
    }

    public static WalletManager getInstance() {
        if (instance == null) {
            instance = new WalletManager();
        }
        return instance;
    }
    public void addTransaction(User user, Crypto crypto, int units) {
        if (crypto.getCurrentPrice() * units >= user.getBalance()) {
            try{
                Purchase p = new Purchase(crypto.getName(), units, crypto.getCurrentPrice());
            //user.addPurchase(p); no es nnecesario, solo actualizarlo en la base de datos, eso si
            PurchaseDAO purchaseDAO = new PurchaseSQLDAO();
            purchaseDAO.addPurchase(user, p);
            //TODO: falta actualizar el balance del usuario
            //TODO: falta notificar que se ha cambiado el balance
            //TODO: falta notificar que ha cambiado el precio de una crypto, y cambiarlo llamando al add Transaction del Crypto
            } catch (PersistanceException e) {
                throw new DataPersistanceError(e.getMessage());
            }
        } else {
            throw new UnsufficientBalance(NOT_ENOUGH_BALANCE);
        }
    }
    public List<Purchase> getWalletByUserName(String username) {
        PurchaseDAO purchaseDao = new PurchaseSQLDAO();
        try {
            return purchaseDao.getPurchasesByUserName(username);
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }
    public double calculateEstimatedGainsByUserName(String userName) {

       // esto no es yas List <Purchase> purchases = user.getPurchases();
        List<Purchase> purchases = getWalletByUserName(userName);
        double gains = 0;
        for (Purchase purchase : purchases) {
            gains+= purchase.getUnits() * (new CryptoManager().getCryptoCurrentPrice(purchase.getCrypto()) - purchase.getPriceUnit());
        }
        //user.setEstimatedGains(gains);
        return gains;
    }

    public void notifyChangeInCryptoValue(String cryptoName) {
        try{
            String currentUser = AccountManager.getInstance().getCurrentUser().getUsername();
            List<String> usernames = new PurchaseSQLDAO().getUsernamesByCryptoName(cryptoName);
            if (usernames.contains(currentUser)) {
                MarketManager.getMarketManager().notify(EventType.USER_ESTIMATED_GAINS_CHANGED);
            }
        }
        catch(BusinessExeption _){/*if there is no user*/}
        catch(PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }

    public void warnUsersCryptoDeleted(String cryptoName) {
        try {
            PurchaseDAO purchaseDAO = new PurchaseSQLDAO();
            List<String> usersToWarn = purchaseDAO.getUsernamesByCryptoName(cryptoName);
            if (usersToWarn != null) {
                for (String user : usersToWarn) {
                    double benefits = purchaseDAO.sellAllPurchasesFromCrypto(cryptoName, user);
                    AccountManager.getInstance().warnCryptoDeleted(benefits, user);
                }
            }
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }
}
