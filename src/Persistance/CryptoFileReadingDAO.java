package Persistance;

import Business.Entities.Crypto;
import Persistance.PersistanceExceptions.FileTypeException;

import java.io.File;
import java.util.List;

/**
 * The interface Crypto file reading dao.
 */
public interface CryptoFileReadingDAO {
    /**
     * Read crypto from file list.
     *
     * @param file the file
     * @return the list
     * @throws FileTypeException the file type exception
     */
    List<Crypto> readCryptoFromFile(File file) throws FileTypeException;
}
