package org.company;

public class DataSourceFactory {

    public static DataSourceService getDataSource(String datasource) {
        if ("file".equals(datasource)) {
            return new FileService();
//        } else if ("database".equals(datasource)) {
//            return new ...;
//        } else if ("restapi".equals(datasource)) {
//            return new ...;
        } else {
            throw new UnsupportedOperationException("Unsupported data source: " + datasource);
        }
    }

}
