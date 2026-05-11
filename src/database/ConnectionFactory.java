package database;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

//=============================================== FT-131 ======================================================
    private static void createTablesFromFile() {
        try {
            // Tavo esama logika...
            String sqlContent = Files.readString(Paths.get("src/DBtables.sql"));
            String[] sqlCommands = sqlContent.split(";");

            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            for (String command : sqlCommands) {
                if (!command.trim().isEmpty()) {
                    stmt.execute(command);
                }
            }
            
            // --- NAUJA: DB ATSPARUMO TESTAVIMO KRITERIJAI ---
            
            // 1. Lentelė sukuriama automatiškai, jei neegzistuoja
            stmt.execute("CREATE TABLE IF NOT EXISTS preset_exercises (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(100), type VARCHAR(50))");
            
            // 2. ALTER TABLE nepavyksta (stulpelis jau yra) — klaida ignoruojama
            try {
                stmt.execute("ALTER TABLE preset_exercises ADD COLUMN calories_per_hour DOUBLE");
            } catch (SQLException ignored) {
                // Klaida tyliai ignoruojama, nes stulpelis jau egzistuoja
            }
            
            stmt.close();
            conn.commit();
            conn.setAutoCommit(true);
            System.out.println(">>> Lentelės sėkmingai sukurtos ir atnaujintos! <<<");
            
            // 3. exercises.json nerastas — naudojamas fallback
            checkAndLoadExercises();
            
        } catch (Exception e) {
            System.out.println(">>> Klaida nuskaitant DBtables.sql failą <<<:" + e.getMessage());
            try { conn.rollback(); conn.setAutoCommit(true); } catch (Exception ignored) {}
        }
    }

    private static void checkAndLoadExercises() {
        // Pirmiausia patikriname, ar lentelė jau turi duomenų, kad nekrautume jų 100 kartų
        String checkSql = "SELECT COUNT(*) FROM preset_exercises";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(checkSql)) {
            if (rs.next() && rs.getInt(1) > 0) {
                return; // DB jau turi pratimų, nieko nedarome
            }
        } catch (SQLException e) {
            System.out.println("Klaida tikrinant preset_exercises: " + e.getMessage());
        }

        String insertSql = "INSERT INTO preset_exercises (name, type) VALUES (?, ?)";
        java.io.File jsonFile = new java.io.File("exercises.json");

        try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
            
            // KRITERIJUS: Patikriname, ar failas egzistuoja
            if (jsonFile.exists()) {
                System.out.println("Skaitomas exercises.json failas...");
                String content = Files.readString(Paths.get("exercises.json"));

                // Kadangi nenorime naudoti išorinių bibliotekų, tekstą iškarpome patys:
                String[] objects = content.split("\\{"); 
                
                for (String obj : objects) {
                    if (!obj.contains("\"name\"")) continue; // Praleidžiame tuščias vietas
                    
                    try {
                        // Ištraukiame pavadinimą ("name")
                        String name = obj.split("\"name\"\\s*:\\s*\"")[1].split("\"")[0];
                        // Ištraukiame kategoriją ("category")
                        String category = obj.split("\"category\"\\s*:\\s*\"")[1].split("\"")[0];
                        
                        pstmt.setString(1, name);
                        pstmt.setString(2, category);
                        pstmt.addBatch(); // Pridedame į paketą greitesniam įrašymui
                    } catch (Exception ignored) {
                        // Jei kažkuri eilutė blogai suformatuota, ją praleidžiame
                    }
                }
                
                pstmt.executeBatch(); // Įrašome viską vienu metu
                System.out.println(">>> Pratimai sėkmingai įkrauti iš exercises.json! <<<");

            } else {
                // KRITERIJUS: exercises.json nerastas — fallback pratimų sąrašas naudojamas
                System.out.println("ĮSPĖJIMAS: exercises.json nerastas. Naudojamas FALLBACK pratimų sąrašas.");
                
                String[][] fallbackExercises = {
                    {"Bench Press", "strength"},
                    {"Push-Up", "strength"},
                    {"Deadlift", "strength"},
                    {"Running 10 km/h", "cardio"},
                    {"Cycling (moderate)", "cardio"},
                    {"Plank", "strength"}
                };
                
                for (String[] ex : fallbackExercises) {
                    pstmt.setString(1, ex[0]);
                    pstmt.setString(2, ex[1]);
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
                System.out.println(">>> Atsarginiai (Fallback) pratimai įkrauti! <<<");
            }
            
        } catch (Exception e) {
            System.out.println("Klaida įkeliant pratimus: " + e.getMessage());
        }
    }
//==========================================================================================================================
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