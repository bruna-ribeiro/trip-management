package org.company;

import java.io.*;

public class FileService implements DataSourceService {

    @Override
    public <T> InputStream read(T[] params) throws IOException {
            return new FileInputStream((String) params[0]);
    }

    @Override
    public <T> void write(T[] params) throws IOException {
        String filename = (String) params[0];
        try (OutputStream out = new FileOutputStream(filename)) {
            StringBuffer data = (StringBuffer) params[1];
            out.write(data.toString().getBytes());
        }
    }
}
