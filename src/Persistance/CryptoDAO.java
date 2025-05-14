package Persistance;

import Business.Entities.Crypto;
import Persistance.PersistanceExceptions.ConfigurationFileError;
import Persistance.PersistanceExceptions.PersistanceException;

import java.util.List;

public interface CryptoDAO {
    boolean createCrypto(Crypto crypto) throws PersistanceException;
    boolean updateCrypto(Crypto crypto) throws PersistanceException;
    List<Crypto> getAllCryptos() throws PersistanceException;
    Crypto getCryptoByName(String name) throws PersistanceException;
    void deleteCrypto(String cryptoname) throws PersistanceException;
    double getCryptoCurrentPrice(String cryptoName) throws PersistanceException;
    List<Crypto> getCryptoByCategory (String category) throws PersistanceException;
    List<String> getCategories () throws PersistanceException;
}