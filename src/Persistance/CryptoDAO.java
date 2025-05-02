package Persistance;

import Business.Entities.Crypto;
import java.util.List;

public interface CryptoDAO {
    boolean updateCrypto(Crypto crypto);
    List<Crypto> getAllCryptos();
    Crypto getCryptoByName(String name);
    float getCryptoCurrentPrice(String name);
}