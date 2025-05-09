package Persistance;

import Business.Entities.Crypto;
import java.util.List;

public interface CryptoDAO {
    boolean createCrypto(Crypto crypto);
    boolean updateCrypto(Crypto crypto);
    List<Crypto> getAllCryptos();
    Crypto getCryptoByName(String name);
    void deleteCrypto(String cryptoname);
}