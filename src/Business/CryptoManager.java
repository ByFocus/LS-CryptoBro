package Business;

import Business.Entities.Crypto;
import Persistance.CryptoDAO;
import Persistance.CryptoSQLDAO;

import java.util.List;

public class CryptoManager {
    public List<Crypto> getAllCryptos() {
        CryptoDAO cryptoDA0 =new CryptoSQLDAO();
        return cryptoDA0.getAllCryptos();
    }

    public Crypto getCryptoByName(String cryptoName) {
        CryptoDAO cryptoDA0 =new CryptoSQLDAO();
        return cryptoDA0.getCryptoByName(cryptoName);
    }

    public void botMakeTransaction(String cryptoName, boolean buy){
        makeTransaction(cryptoName, buy? 1: -1);
        new WalletManager().notifyChangeInCryptoValue(cryptoName);
    }

    public synchronized void makeTransaction(String cryptoName, int units) {
    }

    public float getCryptoCurrentPrice(String cryptoName) {
        return new CryptoSQLDAO().getCryptoCurrentPrice(cryptoName);
    }

    public void deleteCrypto(String cryptoName) {

    }
}
