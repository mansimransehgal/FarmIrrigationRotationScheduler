
package com.farm.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class DBConnection {

    private static Properties properties = new Properties();

    static {
        try (FileInputStream input =
                new FileInputStream("db.properties")) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not load db.properties", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.user"),
                properties.getProperty("db.password")
        );
    }
}
