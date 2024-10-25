package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBPropertyUtil {
    public static String getPropertyString(String propertyFileName) {
        Properties properties = new Properties();
        String connectionString = "";

        try (InputStream input = new FileInputStream(propertyFileName)) {
            properties.load(input);
            String host = properties.getProperty("hostname");
            String dbName = properties.getProperty("dbname");
            String user = properties.getProperty("username");
            String password = properties.getProperty("password");
            String port = properties.getProperty("port");

            // Constructing the connection string
            connectionString = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?user=" + user + "&password=" + password;

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return connectionString;
    }
}
