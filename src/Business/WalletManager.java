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
 * The type WalletManager.
 * Handles all logic related to user wallets, transactions, and estimated gains.
 */
public class WalletManager {

    /**
     * Message shown when the user does not have enough balance for a transaction.
     */
    static String NOT_ENOUGH_BALANCE = "Bro, no tienes suficiente dinero, conoce tu lugar";

    /**
     * Singleton instance of WalletManager.
     */
    public static WalletManager instance;

    /**
     * Instantiates a new WalletManager.
     */
    public WalletManager() {
    }

    /**
     * Gets the singleton instance of WalletManager.
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
     * Removes units from a user's purchase and updates their balance accordingly.
     *
     * @param user     the user
     * @param purchase the purchase
     * @param units    the number of units to remove
     */
    public void removeTransaction(User user, Purchase purchase, int units) {
        try {
            PurchaseDAO purchaseDAO = new PurchaseSQLDAO();
            CryptoManager cryptoManager = new CryptoManager();
            double benefit = units * cryptoManager.getCryptoByName(purchase.getCrypto()).getCurrentPrice();
            purchaseDAO.subtractUnits(purchase, user.getUsername(), units);
            AccountManager.getInstance().updateUserBalance(benefit);
            CryptoManager.getCryptoManager().makeTransaction(purchase.getCrypto(), units);
            MarketManager.getMarketManager().notify(EventType.USER_ESTIMATED_GAINS_CHANGED);
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }

    /**
     * Adds a purchase transaction for a user, deducting the cost from their balance.
     *
     * @param user   the user
     * @param crypto the crypto being purchased
     * @param units  the number of units to purchase
     */
    public void addTransaction(User user, Crypto crypto, int units) {
        double cost = crypto.getCurrentPrice() * units;
        if (cost <= user.getBalance()) {
            try {
                Purchase p = new Purchase(crypto.getName(), units, crypto.getCurrentPrice());
                PurchaseDAO purchaseDAO = new PurchaseSQLDAO();
                purchaseDAO.addPurchase(user, p);
                AccountManager.getInstance().updateUserBalance(-1 * cost);
                CryptoManager.getCryptoManager().makeTransaction(crypto.getName(), units);
            } catch (PersistanceException e) {
                throw new DataPersistanceError(e.getMessage());
            }
        } else {
            throw new UnsufficientBalance(NOT_ENOUGH_BALANCE);
        }
    }

    /**
     * Retrieves the wallet (purchases) for a given user.
     *
     * @param username the username
     * @return the list of purchases
     */
    public List<Purchase> getWalletByUserName(String username) {
        PurchaseDAO purchaseDao = new PurchaseSQLDAO();
        try {
            return purchaseDao.getPurchasesByUserName(username);
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }

    /**
     * Gets the total number of units of a specific crypto in the user's wallet.
     *
     * @param userName   the user's name
     * @param cryptoName the crypto name
     * @return the total units owned
     */
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
     * Calculates the estimated gains of a user based on current crypto prices.
     *
     * @param userName the user's name
     * @return the estimated gain value
     */
    public double calculateEstimatedGainsByUserName(String userName) {
        List<Purchase> purchases = getWalletByUserName(userName);
        double gains = 0;
        for (Purchase purchase : purchases) {
            gains += purchase.getUnits() * (new CryptoManager().getCryptoCurrentPrice(purchase.getCrypto()) - purchase.getPriceUnit());
        }
        return gains;
    }

    /**
     * Notifies the market if a user's crypto value has changed.
     *
     * @param cryptoName the crypto name
     */
    public void notifyChangeInCryptoValue(String cryptoName) {
        try {
            String currentUser = AccountManager.getInstance().getCurrentUserName();
            List<String> usernames = new PurchaseSQLDAO().getUsernamesByCryptoName(cryptoName);
            if (usernames.contains(currentUser)) {
                MarketManager.getMarketManager().notify(EventType.USER_ESTIMATED_GAINS_CHANGED);
            }
        } catch (BusinessExeption _) {
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }

    /**
     * Warns users who owned a crypto that has been deleted and updates their balance.
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

    /**
     * Deletes all purchases associated with a specific user.
     *
     * @param userName the username
     */
    public void deleteWalletFromUser(String userName) {
        try {
            PurchaseDAO purchaseDAO = new PurchaseSQLDAO();
            purchaseDAO.deletePurchasesFromUser(userName);
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }
}
