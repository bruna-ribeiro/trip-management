package org.company;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class CsvParser implements ParserService {

    @Override
    public <T, S> List<T> parse(InputStream inputStream, Class<T> clazz, S[] params) throws IOException {

        List<T> objects = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));) {
            if (params != null) {
                Boolean hasHeader = (Boolean) params[0];
                if (hasHeader) reader.readLine(); // header
            }
            String line;
            while ((line = reader.readLine()) != null) {
                T object = createObjectFromString(line, clazz);
                if (object != null) objects.add(object);
            }
        }
        return objects;
    }

    @Override
    public <T, S> StringBuffer format(List<T> objects, Class<T> clazz, S[] params)  {
        StringBuffer sb = new StringBuffer();
        if (params != null && params[0] instanceof String) {
            sb.append(params[0]).append(System.lineSeparator());
        }
        for (T object : objects) {
            String s = createStringFromObject(object, clazz);
            if (s != null) sb.append(s).append(System.lineSeparator());
        }
        return sb;
    }

    private static <T> String createStringFromObject(T object, Class<T> clazz) {
        if (clazz.getName().equalsIgnoreCase("org.company.Trip")) {
            StringBuffer sb = new StringBuffer();
            sb.append(((Trip) object).getStartedAsString()+", ");
            sb.append(((Trip) object).getFinishedAsString()+", ");
            sb.append(((Trip) object).getDurationSecs()+", ");
            sb.append(((Trip) object).getFromStopId()+", ");
            sb.append(((Trip) object).getToStopId()+", ");
            sb.append(((Trip) object).getChargeAmount()+", ");
            sb.append(((Trip) object).getCompanyId()+", ");
            sb.append(((Trip) object).getBusId()+", ");
            sb.append(((Trip) object).getPan()+", ");
            sb.append(((Trip) object).getStatus());
            return sb.toString();
        } else if (clazz.getName().equalsIgnoreCase("org.company.Tap")) {
            // implement here the logic to create a CSV representation of the Tap object
        }
        return null;
    }

    private static <T> T createObjectFromString(String line, Class<T> clazz) {
        if (clazz.getName().equalsIgnoreCase("org.company.Tap")) {
            String[] fields = line.split("\\s*,\\s*");
            if (fields.length < 7) {
                return null;
            }
            Tap tap = null;
            try {
                tap = new Tap();
                tap.setId(fields[0]);
                tap.setDateTime(LocalDateTime.parse(fields[1], DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
                tap.setTapType(fields[2]);
                tap.setStopId(fields[3]);
                tap.setCompanyId(fields[4]);
                tap.setBusId(fields[5]);
                tap.setPan(fields[6]);
            }
            catch (DateTimeParseException ex) {
                System.out.println("Record ID "+fields[0]+" could not be processed: wrong date/time format");
            }
            return (T) tap;
        }
        else if (clazz.getName().equalsIgnoreCase("org.company.Trip")) {
            Trip trip = null;
            // implement here the logic to build a Trip object from the csv line
            return (T) trip;
        }
        return null;
    }
}
