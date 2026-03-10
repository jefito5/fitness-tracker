package impl;

import database.ConnectionFactory;
import models.PresetExercise;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;

public class PresetExerciseDB {

    public void seedIfEmpty() {
        try {
            Connection conn = ConnectionFactory.getConnection();

            conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS preset_exercises (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ExerciseName TEXT NOT NULL, " +
                "workoutType TEXT NOT NULL, " +
                "muscleGroup TEXT, " +
                "met REAL DEFAULT 0)"
            );

            try { conn.createStatement().execute("ALTER TABLE exercise ADD COLUMN workoutType TEXT DEFAULT 'Cardio'"); } catch (Exception ignored) {}
            try { conn.createStatement().execute("ALTER TABLE exercise ADD COLUMN reps INTEGER DEFAULT 0"); } catch (Exception ignored) {}
            try { conn.createStatement().execute("ALTER TABLE exercise ADD COLUMN weightUsed REAL DEFAULT 0"); } catch (Exception ignored) {}

            ResultSet rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM preset_exercises");
            if (rs.next() && rs.getInt(1) > 0) return;

            InputStream is = getClass().getClassLoader().getResourceAsStream("exercises.json");
            if (is == null) is = getClass().getResourceAsStream("/exercises.json");
            if (is == null) {
                for (String path : new String[]{"src/resources/exercises.json","resources/exercises.json","exercises.json"}) {
                    File f = new File(path);
                    if (f.exists()) { is = new FileInputStream(f); break; }
                }
            }

            if (is == null) {
                System.out.println(">>> exercises.json not found, using fallback <<<");
                seedFallback(conn);
                return;
            }

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] chunk = new byte[1024];
            int n;
            while ((n = is.read(chunk)) != -1) buffer.write(chunk, 0, n);
            seedFromJson(conn, buffer.toString("UTF-8"));
            System.out.println(">>> Preset exercises loaded from exercises.json <<<");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void seedFromJson(Connection conn, String json) throws Exception {
        boolean prevAutoCommit = conn.getAutoCommit();
        conn.setAutoCommit(false);
        try {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO preset_exercises (ExerciseName, workoutType, muscleGroup, met) VALUES (?,?,?,?)");
            int count = 0;
            for (String obj : json.split("\\{")) {
                if (!obj.contains("\"name\"")) continue;
                String name     = extractStr(obj, "name");
                String category = extractStr(obj, "category");
                String muscle   = extractArr(obj, "primaryMuscles");
                double met      = extractDbl(obj, "met");
                if (name == null || name.isEmpty()) continue;
                if (!"strength".equals(category) && !"cardio".equals(category)) continue;
                ps.setString(1, name);
                ps.setString(2, "strength".equals(category) ? "Strength" : "Cardio");
                ps.setString(3, muscle != null ? muscle : "");
                ps.setDouble(4, met);
                ps.addBatch();
                count++;
            }
            ps.executeBatch();
            conn.commit();
            System.out.println(">>> Seeded " + count + " exercises <<<");
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(prevAutoCommit);
        }
    }

    private String extractStr(String obj, String key) {
        int i = obj.indexOf("\"" + key + "\""); if (i < 0) return null;
        int s = obj.indexOf("\"", i + key.length() + 3); if (s < 0) return null;
        int e = obj.indexOf("\"", s + 1); if (e < 0) return null;
        return obj.substring(s + 1, e);
    }
    private String extractArr(String obj, String key) {
        int i = obj.indexOf("\"" + key + "\""); if (i < 0) return null;
        int a = obj.indexOf("[", i); if (a < 0) return null;
        int s = obj.indexOf("\"", a); if (s < 0) return null;
        int e = obj.indexOf("\"", s + 1); if (e < 0) return null;
        return obj.substring(s + 1, e);
    }
    private double extractDbl(String obj, String key) {
        int i = obj.indexOf("\"" + key + "\""); if (i < 0) return 0;
        int c = obj.indexOf(":", i); if (c < 0) return 0;
        StringBuilder num = new StringBuilder();
        for (char ch : obj.substring(c + 1).trim().toCharArray()) {
            if (Character.isDigit(ch) || ch == '.') num.append(ch);
            else if (num.length() > 0) break;
        }
        try { return num.length() > 0 ? Double.parseDouble(num.toString()) : 0; } catch (Exception e) { return 0; }
    }

    private void seedFallback(Connection conn) throws Exception {
        boolean prevAutoCommit = conn.getAutoCommit();
        conn.setAutoCommit(false);
        try {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO preset_exercises (ExerciseName, workoutType, muscleGroup, met) VALUES (?,?,?,?)");
            Object[][] data = {
                {"Bench Press","Strength","chest",0.0}, {"Incline Bench Press","Strength","chest",0.0},
                {"Back Squat","Strength","quadriceps",0.0}, {"Leg Press","Strength","quadriceps",0.0},
                {"Deadlift","Strength","lower back",0.0}, {"Romanian Deadlift","Strength","hamstrings",0.0},
                {"Pull-Up","Strength","lats",0.0}, {"Lat Pulldown","Strength","lats",0.0},
                {"Barbell Row","Strength","middle back",0.0}, {"Dumbbell Row","Strength","middle back",0.0},
                {"Overhead Press","Strength","shoulders",0.0}, {"Lateral Raise","Strength","shoulders",0.0},
                {"Barbell Curl","Strength","biceps",0.0}, {"Hammer Curl","Strength","biceps",0.0},
                {"Tricep Pushdown","Strength","triceps",0.0}, {"Skull Crusher","Strength","triceps",0.0},
                {"Lunge","Strength","quadriceps",0.0}, {"Bulgarian Split Squat","Strength","quadriceps",0.0},
                {"Hip Thrust","Strength","glutes",0.0}, {"Glute Bridge","Strength","glutes",0.0},
                {"Plank","Strength","abdominals",0.0}, {"Crunch","Strength","abdominals",0.0},
                {"Calf Raise","Strength","calves",0.0}, {"Dumbbell Shoulder Press","Strength","shoulders",0.0},
                {"Running 8 km/h","Cardio","",8.0}, {"Running 10 km/h","Cardio","",10.0},
                {"Running 12 km/h","Cardio","",11.5}, {"Walking 5 km/h","Cardio","",3.5},
                {"Cycling (moderate)","Cardio","",7.5}, {"Cycling (vigorous)","Cardio","",10.0},
                {"Stationary Bike","Cardio","",7.0}, {"Jump Rope","Cardio","",10.0},
                {"Swimming (moderate)","Cardio","",6.0}, {"Swimming (vigorous)","Cardio","",9.8},
                {"Rowing Machine","Cardio","",7.0}, {"Elliptical","Cardio","",5.0},
                {"Stair Climber","Cardio","",8.0}, {"HIIT","Cardio","",10.0},
            };
            for (Object[] r : data) {
                ps.setString(1,(String)r[0]); ps.setString(2,(String)r[1]);
                ps.setString(3,(String)r[2]);
                ps.setDouble(4, ((Number)r[3]).doubleValue()); // FIX: Number cast
                ps.addBatch();
            }
            ps.executeBatch();
            conn.commit(); // FIX: commit po batch
            System.out.println(">>> Fallback: " + data.length + " exercises seeded <<<");
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(prevAutoCommit);
        }
    }

    public ArrayList<PresetExercise> getByType(String workoutType) {
        ArrayList<PresetExercise> list = new ArrayList<>();
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM preset_exercises WHERE workoutType = ? ORDER BY muscleGroup, ExerciseName");
            ps.setString(1, workoutType);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PresetExercise pe = new PresetExercise();
                pe.setId(rs.getInt("id"));
                pe.setExerciseName(rs.getString("ExerciseName"));
                pe.setWorkoutType(rs.getString("workoutType"));
                pe.setMuscleGroup(rs.getString("muscleGroup"));
                pe.setMet(rs.getDouble("met"));
                list.add(pe);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}
