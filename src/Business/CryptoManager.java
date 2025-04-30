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

    public synchronized void makeTransaction(String cryptoName, int units) {
    }
}
