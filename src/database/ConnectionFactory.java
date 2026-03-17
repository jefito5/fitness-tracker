package database;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionFactory {

    private static final String URI = "jdbc:sqlite:fitness.db";
    private static Connection conn;

    private ConnectionFactory() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            System.out.println("Klaida: Nerastas SQLite tvarkyklės failas!");
            ex.printStackTrace();
        }
    }

    public static Connection getConnection() {
        if (conn == null) {
            try {
                new ConnectionFactory();
                conn = DriverManager.getConnection(URI);
                // WAL režimas prieš viską - mažina locking problemų tikimybę
                Statement pragmaStmt = conn.createStatement();
                pragmaStmt.execute("PRAGMA journal_mode=WAL");
                pragmaStmt.execute("PRAGMA busy_timeout=5000");
                pragmaStmt.close();
                conn.setAutoCommit(true);
                createTablesFromFile();
            } catch (SQLException ex) {
                System.out.println("Nepavyko prisijungti prie DB!");
                ex.printStackTrace();
            }
        }
        try {
            if (conn != null && !conn.getAutoCommit()) {
                conn.setAutoCommit(true);
            }
        } catch (SQLException ignored) {}
        return conn;
    }

    private static void createTablesFromFile() {
        try {
            String sqlContent = Files.readString(Paths.get("src/DBtables.sql"));
            String[] sqlCommands = sqlContent.split(";");

            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            for (String command : sqlCommands) {
                if (!command.trim().isEmpty()) {
                    stmt.execute(command);
                }
            }
            stmt.close();
            conn.commit();
            conn.setAutoCommit(true);
            System.out.println(">>> Lentelės sėkmingai sukurtos iš DBtables.sql failo! <<<");
        } catch (Exception e) {
            System.out.println(">>> Klaida nuskaitant DBtables.sql failą <<<:" + e.getMessage());
            try { conn.rollback(); conn.setAutoCommit(true); } catch (Exception ignored) {}
        }
    }

    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}