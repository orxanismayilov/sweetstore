package sample.utils;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoadPropertyUtil {

    public static Properties loadPropertiesFile(String url) {
        Properties properties = new Properties();
        try {
            InputStream input =LoadPropertyUtil.class.getClassLoader().getResourceAsStream(url);
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
