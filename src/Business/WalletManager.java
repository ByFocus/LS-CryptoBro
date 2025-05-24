package Business;

import Business.BusinessExceptions.BusinessExeption;
import Business.BusinessExceptions.DataPersistanceError;
import Business.BusinessExceptions.UnsufficientBalance;
import Business.Entities.*;
import Persistance.PersistanceExceptions.PersistanceException;
import Persistance.PurchaseDAO;
import Persistance.SQL.PurchaseSQLDAO;

import java.util.List;

/**
 * The type Wallet manager.
 */
public class WalletManager {
    /**
     * The Not enough balance.
     */
    static String NOT_ENOUGH_BALANCE = "Bro, no tienes suficiente dinero, conoce tu lugar";

    /**
     * The constant instance.
     */
    public static WalletManager instance;

    /**
     * Instantiates a new Wallet manager.
     */
    public WalletManager() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static WalletManager getInstance() {
        if (instance == null) {
            instance = new WalletManager();
        }
        return instance;
    }

    /**
     * Remove transaction.
     *
     * @param user     the user
     * @param purchase the purchase
     * @param units    the units
     */
    public void removeTransaction(User user, Purchase purchase, int units){
        try{
            PurchaseDAO purchaseDAO = new PurchaseSQLDAO();
            CryptoManager cryptoManager = new CryptoManager();
            double benefit = units * cryptoManager.getCryptoByName(purchase.getCrypto()).getCurrentPrice();
            purchaseDAO.subtractUnits(purchase, user.getUsername(), units);
            AccountManager.getInstance().updateUserBalance(benefit);
            CryptoManager.getCryptoManager().makeTransaction(purchase.getCrypto(), units); // modifica el precio de la crypto
            MarketManager.getMarketManager().notify(EventType.USER_ESTIMATED_GAINS_CHANGED);
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }

    /**
     * Add transaction.
     *
     * @param user   the user
     * @param crypto the crypto
     * @param units  the units
     */
    public void addTransaction(User user, Crypto crypto, int units) {
        double cost = crypto.getCurrentPrice() * units;
        if (cost <= user.getBalance()) {
            try{
                Purchase p = new Purchase(crypto.getName(), units, crypto.getCurrentPrice());
                PurchaseDAO purchaseDAO = new PurchaseSQLDAO();
                purchaseDAO.addPurchase(user, p);
                AccountManager.getInstance().updateUserBalance(-1*cost);
                CryptoManager.getCryptoManager().makeTransaction(crypto.getName(), units);
            } catch (PersistanceException e) {
                throw new DataPersistanceError(e.getMessage());
            }
        } else {
            throw new UnsufficientBalance(NOT_ENOUGH_BALANCE);
        }
    }

    /**
     * Gets wallet by user name.
     *
     * @param username the username
     * @return the wallet by user name
     */
    public List<Purchase> getWalletByUserName(String username) {
        PurchaseDAO purchaseDao = new PurchaseSQLDAO();
        try {
            return purchaseDao.getPurchasesByUserName(username);
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }

    public int getNumCryptoInWallet(String userName, String cryptoName) {
        PurchaseDAO purchaseDao = new PurchaseSQLDAO();
        try {
            List<Purchase> wallet = purchaseDao.getPurchasesByUserName(userName);
            int numCrypto = 0;
            for (Purchase purchase : wallet) {
                if (purchase.getCrypto().equals(cryptoName)) {
                    numCrypto += purchase.getUnits();
                }
            }

            return numCrypto;
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }

    /**
     * Calculate estimated gains by user name double.
     *
     * @param userName the user name
     * @return the double
     */
    public double calculateEstimatedGainsByUserName(String userName) {
        List<Purchase> purchases = getWalletByUserName(userName);
        double gains = 0;
        for (Purchase purchase : purchases) {
            gains+= purchase.getUnits() * (new CryptoManager().getCryptoCurrentPrice(purchase.getCrypto()) - purchase.getPriceUnit());
        }
        return gains;
    }

    /**
     * Notify change in crypto value.
     *
     * @param cryptoName the crypto name
     */
    public void notifyChangeInCryptoValue(String cryptoName) {
        try{
            String currentUser = AccountManager.getInstance().getCurrentUserName();
            List<String> usernames = new PurchaseSQLDAO().getUsernamesByCryptoName(cryptoName);
            if (usernames.contains(currentUser)) {
                MarketManager.getMarketManager().notify(EventType.USER_ESTIMATED_GAINS_CHANGED);
            }
        }
        catch(BusinessExeption _){/*if there is no user (it's the admin)*/}
        catch(PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }

    /**
     * Warn users crypto deleted.
     *
     * @param cryptoName the crypto name
     */
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

    public void deleteWalletFromUser(String userName) {
        try {
            PurchaseDAO purchaseDAO = new PurchaseSQLDAO();
            purchaseDAO.deletePurchasesFromUser(userName);
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }
}
