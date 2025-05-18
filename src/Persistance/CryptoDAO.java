package Persistance;

import Business.Entities.Crypto;
import Persistance.PersistanceExceptions.ConfigurationFileError;
import Persistance.PersistanceExceptions.PersistanceException;

import java.io.File;
import java.util.List;

public interface CryptoDAO {
    void createCrypto(Crypto crypto) throws PersistanceException;
    void updateCrypto(Crypto crypto) throws PersistanceException;
    List<Crypto> getAllCryptos() throws PersistanceException;
    Crypto getCryptoByName(String name) throws PersistanceException;
    void deleteCrypto(String cryptoname) throws PersistanceException;
    double getCryptoCurrentPrice(String cryptoName) throws PersistanceException;
    List<Crypto> getCryptoByCategory (String category) throws PersistanceException;
    List<String> getCategories () throws PersistanceException;
    void updateCryptoPrice(String cryptoName, Double price)  throws PersistanceException;

    String addCryptosFromFile(File file) throws PersistanceException;
}