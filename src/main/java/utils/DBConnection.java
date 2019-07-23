package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static String DAO_PROPERTY_URL="/resources/properties/db.properties";
    private static Properties daoProperties;

    public static Connection getConnection() {
        daoProperties= LoadPropertyUtil.loadPropertiesFile(DAO_PROPERTY_URL);
        try {
            Class.forName (daoProperties.getProperty("localedb.driver")).newInstance ();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection (daoProperties.getProperty("localedb.url"), daoProperties.getProperty("localedb.user"), daoProperties.getProperty("localedb.password"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
