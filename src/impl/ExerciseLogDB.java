package impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import database.ConnectionFactory;
import database.IdailyElogDB;
import models.DailyExerciseLog;

public class ExerciseLogDB implements IdailyElogDB {
    private Connection conn;

    public ExerciseLogDB() {
        conn = ConnectionFactory.getConnection();
    }

    public void ensureDurationColumn() {
        try {
            java.sql.Statement st = conn.createStatement();
            java.sql.ResultSet rs = st.executeQuery("PRAGMA table_info(DailyExerciseLog)");
            boolean found = false;
            while (rs.next()) {
                if ("durationMinutes".equalsIgnoreCase(rs.getString("name"))) { found = true; break; }
            }
            rs.close(); st.close();
            if (!found) {
                conn.createStatement().execute("ALTER TABLE DailyExerciseLog ADD COLUMN durationMinutes REAL DEFAULT 0");
                System.out.println(">>> durationMinutes stulpelis pridėtas <<<");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public int insertDailyLog(DailyExerciseLog el) {
        String sql = "INSERT INTO DailyExerciseLog(totalCalorieBurn, exerciseID, userId, Date, durationMinutes) VALUES(?,?,?,?,?)";
        LocalDate today = LocalDate.now();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setDouble(1, el.getTotalCalorieBurn());
            pstmt.setInt(2, el.getExerciseId());
            pstmt.setInt(3, el.getUserId());
            pstmt.setObject(4, String.valueOf(today));
            pstmt.setDouble(5, el.getDurationMinutes());

            if (pstmt.executeUpdate() > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public ArrayList<Double> getburnSum() {
        ArrayList<Double> aea = new ArrayList<>();
        LocalDate today = LocalDate.now();
        String sql = "SELECT totalCalorieBurn FROM DailyExerciseLog WHERE Date=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, String.valueOf(today));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                aea.add(rs.getDouble("totalCalorieBurn"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aea;
    }

    // Grąžina šiandienos pratimų log'us su muscleGroup [7]
    public ArrayList<Object[]> getTodayLogs(int userId) {
        ArrayList<Object[]> list = new ArrayList<>();
        LocalDate today = LocalDate.now();
        String sql =
            "SELECT del.id, e.ExerciseName, e.workoutType, del.totalCalorieBurn, " +
            "e.reps, e.weightUsed, del.durationMinutes, e.muscleGroup " +
            "FROM DailyExerciseLog del " +
            "JOIN exercise e ON del.exerciseID = e.id " +
            "WHERE del.userId = ? AND del.Date = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setString(2, String.valueOf(today));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getInt("id"),            
                    rs.getString("ExerciseName"), 
                    rs.getString("workoutType"),  
                    rs.getDouble("totalCalorieBurn"), 
                    rs.getDouble("weightUsed"),   
                    rs.getDouble("durationMinutes"), 
                    rs.getString("muscleGroup")  
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int deleteTodayLog(int logId) {
        String sql = "DELETE FROM DailyExerciseLog WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, logId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}