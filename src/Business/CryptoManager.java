package Business;

import Business.BusinessExceptions.BusinessExeption;
import Business.BusinessExceptions.DataPersistanceError;
import Business.Entities.Crypto;
import Persistance.CryptoDAO;
import Persistance.CryptoSQLDAO;
import Persistance.PersistanceExceptions.PersistanceException;
import Persistance.PurchaseSQLDAO;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class CryptoManager{
    private static CryptoManager instance;

    public CryptoManager(){
    }

    public static CryptoManager getCryptoManager() {
        if (instance == null) {
            instance = new CryptoManager();
        }
        return instance;
    }
    public List<Crypto> getAllCryptos()  throws BusinessExeption{
        try {
            CryptoDAO cryptoDA0 = new CryptoSQLDAO();
            return cryptoDA0.getAllCryptos();
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }

    public Crypto getCryptoByName(String cryptoName)  throws BusinessExeption{
        try {
            CryptoDAO cryptoDA0 =new CryptoSQLDAO();
            return cryptoDA0.getCryptoByName(cryptoName);
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }

    public synchronized void botMakeTransaction(String cryptoName, boolean buy){
        makeTransaction(cryptoName, buy? 1: -1);
        new WalletManager().notifyChangeInCryptoValue(cryptoName);
    }

    public synchronized void makeTransaction(String cryptoName, int units) throws BusinessExeption {
        try {
            double priceMultiplier = (units>0? 1.01 : 0.99);
            CryptoDAO cryptoDA0 =new CryptoSQLDAO();
            double newPrice = cryptoDA0.getCryptoCurrentPrice(cryptoName)*priceMultiplier;
            cryptoDA0.updateCryptoPrice(cryptoName, newPrice);
            MarketManager.getMarketManager().notify(EventType.CRYPTO_VALUES_CHANGED);
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }

    public double getCryptoCurrentPrice(String cryptoName) throws BusinessExeption {
        try {
            return new CryptoSQLDAO().getCryptoCurrentPrice(cryptoName);
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }

    public void deleteCrypto(String cryptoName) throws BusinessExeption {
        try {
            new WalletManager().warnUsersCryptoDeleted(cryptoName);
            CryptoDAO cryptoDA0 = new CryptoSQLDAO();
            cryptoDA0.deleteCrypto(cryptoName);
            MarketManager.getMarketManager().notify(EventType.CRYPTO_VALUES_CHANGED);
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }

    public String addCryptoFromFiles(List<File> files)  throws BusinessExeption {
        try {
            CryptoDAO cryptoDAO = new CryptoSQLDAO();
            StringBuilder log = new StringBuilder();
            for (int i = 0; i < files.size(); i++) {
                log.append("\nFile #" + (i + 1) + ": " + files.get(i).getName()+"\n");
                try {
                    log.append(cryptoDAO.addCryptosFromFile(files.get(i)));
                } catch (PersistanceException err) {
                    log.append(err.getMessage());
                }
                log.append("\n");
            }
            MarketManager.getMarketManager().notify(EventType.CRYPTO_VALUES_CHANGED);
            return log.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public String[] getAllCryptoNames() throws BusinessExeption{
        List<Crypto> cryptos = getAllCryptos();
        String[] cryptoNames = new String[cryptos.size()];
        for (int i = 0; i < cryptos.size(); i++) {
            cryptoNames[i] = cryptos.get(i).getName();
        }
        return cryptoNames;
    }


    public void updateCryptoPrice(String cryptoName, double price) {
        try {
            CryptoDAO cryptoDA0 = new CryptoSQLDAO();
            cryptoDA0.updateCryptoPrice(cryptoName, price);
            MarketManager.getMarketManager().notify(EventType.CRYPTO_VALUES_CHANGED);
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }
}
