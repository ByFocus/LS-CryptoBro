package Persistance;

import Business.Entities.Crypto;
import Persistance.PersistanceExceptions.FileTypeException;

import java.io.File;
import java.util.List;

/**
 * The interface Crypto file reading dao is a Data Access Object (DAO) interface for
 * reading cryptocurrency data from external files.
 */
public interface CryptoFileReadingDAO {

    /**
     * Reads and parses a file containing cryptocurrency data, converting it
     * into a list of {@link Crypto} objects.
     *
     * @param file the file containing crypto information (e.g., JSON, CSV).
     * @return a list of {@link Crypto} instances parsed from the file.
     * @throws FileTypeException if the file format is invalid, unsupported, or
     *                           contains malformed data.
     */
    List<Crypto> readCryptoFromFile(File file) throws FileTypeException;
}
