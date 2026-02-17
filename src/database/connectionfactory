package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {

    private static final String URI = "jdbc:sqlite:fitness.db";

    public static Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(URI);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
