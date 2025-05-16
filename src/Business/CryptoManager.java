package Business;

import Business.BusinessExceptions.BusinessExeption;
import Business.BusinessExceptions.DataPersistanceError;
import Business.Entities.Crypto;
import Persistance.CryptoDAO;
import Persistance.CryptoSQLDAO;
import Persistance.PersistanceExceptions.PersistanceException;

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
    public List<Crypto> getAllCryptos()  throws PersistanceException{
        CryptoDAO cryptoDA0 =new CryptoSQLDAO();
        return cryptoDA0.getAllCryptos();
    }

    public Crypto getCryptoByName(String cryptoName)  throws PersistanceException{
        CryptoDAO cryptoDA0 =new CryptoSQLDAO();
        return cryptoDA0.getCryptoByName(cryptoName);
    }

    public void botMakeTransaction(String cryptoName, boolean buy){
        makeTransaction(cryptoName, buy? 1: -1);
        new WalletManager().notifyChangeInCryptoValue(cryptoName);
    }

    public synchronized void makeTransaction(String cryptoName, int units) throws BusinessExeption {
        try {
            double priceMultiplier = (units>0? 1.01 : 0.99);
            CryptoDAO cryptoDA0 =new CryptoSQLDAO();
            double newPrice = cryptoDA0.getCryptoCurrentPrice(cryptoName)*priceMultiplier;
            cryptoDA0.updateCryptoPrice(cryptoName, newPrice);
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

    public void deleteCrypto(String cryptoName) {

    }
}
