package org.company;

public class ParserFactory {

    public static ParserService getParser(String format) {
        if ("csv".equals(format)) {
            return new CsvParser();
//        } else if ("json".equals(format)) {
//            return new JsonInputParser();
//        } else if ("xml".equals(format)) {
//            return new XmlInputParser();
        } else {
            throw new UnsupportedOperationException("Unsupported file format: " + format);
        }
    }

}
