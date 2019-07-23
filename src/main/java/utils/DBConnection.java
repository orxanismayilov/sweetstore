package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static String DAO_PROPERTY_URL="sample/resource/properties/db.properties";
    private static Properties daoProperties;

    public static Connection getConnection() {
        daoProperties= LoadPropertyUtil.loadPropertiesFile(DAO_PROPERTY_URL);
        try {
            Class.forName (daoProperties.getProperty("maindb.driver")).newInstance ();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection (daoProperties.getProperty("maindb.url"), daoProperties.getProperty("maindb.user"), daoProperties.getProperty("maindb.password"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
