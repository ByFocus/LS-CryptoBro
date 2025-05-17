package Persistance;

import Business.Entities.Crypto;
import Persistance.PersistanceExceptions.FileTypeException;

import java.io.File;
import java.util.List;

public interface CryptoFileReadingDAO {
    List<Crypto> readCryptoFromFile(File file) throws FileTypeException;
}
