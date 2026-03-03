package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

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

    public static void initializeDatabase() {
        try {
            Connection c = getConnection();
            Statement stmt = c.createStatement();

            stmt.execute(
                "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "gender TEXT, " +
                "age INTEGER, " +
                "password TEXT" +
                ")"
            );

            stmt.execute(
                "CREATE TABLE IF NOT EXISTS meals (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "grams REAL, " +
                "calories REAL" +
                ")"
            );

            stmt.execute(
                "CREATE TABLE IF NOT EXISTS exercises (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "exerciseName TEXT NOT NULL, " +
                "calorieburn REAL" +
                ")"
            );

            c.close();
            System.out.println("Database connected and tables created successfully!");
        } catch (Exception e) {
            System.out.println("Database connection FAILED:");
            e.printStackTrace();
        }
    }
}
