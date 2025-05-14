package Business;

import Business.BusinessExceptions.DataPersistanceError;
import Business.Entities.Crypto;
import Persistance.CryptoDAO;
import Persistance.CryptoSQLDAO;
import Persistance.PersistanceExceptions.PersistanceException;

import java.util.List;

public class CryptoManager{
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

    public synchronized void makeTransaction(String cryptoName, int units) {
    }

    public double getCryptoCurrentPrice(String cryptoName) {
        try {
            return new CryptoSQLDAO().getCryptoCurrentPrice(cryptoName);
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }

    public void deleteCrypto(String cryptoName) {

    }
}
