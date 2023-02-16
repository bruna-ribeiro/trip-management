package org.company;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ParserService {

    /**
     * Parses data in a specific format.
     * Implement here the logic to parse data in CSV, JSON, XML, etc.
     * @return the data parsed
     */
    <T, S> List<T> parse(InputStream inputStream, Class<T> clazz, S[] params) throws IOException;

    /**
     * Formats data in a specific format.
     * Implement here the logic to format the data into CSV, JSON, XML, etc.
     * @param objects list of objects to be formatted
     * @param clazz the class of the objects to be formatted
     * @param params header, if needed
     */
    <T, S> StringBuffer format(List<T> objects, Class<T> clazz, S[] params) throws IOException;

}
