package Persistance;

import Business.Entities.Crypto;
import Persistance.PersistanceExceptions.ConfigurationFileError;
import Persistance.PersistanceExceptions.PersistanceException;

import java.util.List;

public interface CryptoDAO {
    boolean createCrypto(Crypto crypto) throws ConfigurationFileError;
    boolean updateCrypto(Crypto crypto);
    List<Crypto> getAllCryptos();
    Crypto getCryptoByName(String name);
    void deleteCrypto(String cryptoname);
    double getCryptoCurrentPrice(String cryptoName) throws PersistanceException;
    List<Crypto> getCryptoByCategory (String category);
    List<String> getCategories ();
}