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
                createTablesFromFile();
            } catch (SQLException ex) {
                System.out.println("Nepavyko prisijungti prie DB!");
                ex.printStackTrace();
            }
        }
        return conn;
    }

    private static void createTablesFromFile() {
        try {
            String sqlContent = Files.readString(Paths.get("src/DBtables.sql"));
            String[] sqlCommands = sqlContent.split(";");
            
            Statement stmt = conn.createStatement();
            
            for (String command : sqlCommands) {
                if (!command.trim().isEmpty()) {
                    stmt.execute(command);
                }
            }
            System.out.println(">>> Lentelės sėkmingai sukurtos iš DBtables.sql failo! <<<");
            
        } catch (Exception e) {
            System.out.println(">>> Klaida nuskaitant DBtables.sql failą <<<:" + e.getMessage());
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