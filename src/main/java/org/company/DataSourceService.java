package org.company;

import java.io.IOException;
import java.io.InputStream;

/**
 * This interface defines the methods for classes that
 * implement a data provider, like a file, a database, an API, etc.
 */
public interface DataSourceService {

    /**
     * Reads and returns the data from a specific source.
     * Implement here the logic to retrieve data from a file, database, API, etc.
     * @return the data retrieved
     */
    <T> InputStream read(T[] params) throws IOException;

    /**
     * Writes the data to a specific destination.
     * Implement here the logic to write/save the data to a file, database, API, etc.
     * @param params the data to be written
     */
    <T> void write(T[] params) throws IOException;
}
