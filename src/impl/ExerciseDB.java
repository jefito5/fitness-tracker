package impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import database.ConnectionFactory;
import database.IexerciseDB;
import models.Exercise;

public class ExerciseDB implements IexerciseDB {
    private Connection conn;

    public ExerciseDB() {
        conn = ConnectionFactory.getConnection();
    }

    @Override
    public int insertExercise(Exercise ee) {
        String sql = "INSERT INTO exercise(ExerciseName, CalorieburnPerMin, workoutType, reps, weightUsed) VALUES(?,?,?,?,?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, ee.getExerciseName());
            pstmt.setDouble(2, ee.getCalorieburn());
            pstmt.setString(3, ee.getWorkoutType() != null ? ee.getWorkoutType() : "Cardio");
            pstmt.setInt(4, ee.getReps());
            pstmt.setDouble(5, ee.getWeightUsed());

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
    public int updateExercise(Exercise ee) {
        String sql = "UPDATE exercise SET ExerciseName=?, CalorieburnPerMin=?, workoutType=?, reps=?, weightUsed=? WHERE id=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, ee.getExerciseName());
            pstmt.setDouble(2, ee.getCalorieburn());
            pstmt.setString(3, ee.getWorkoutType());
            pstmt.setInt(4, ee.getReps());
            pstmt.setDouble(5, ee.getWeightUsed());
            pstmt.setInt(6, ee.getId());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int deleteExercise(Exercise ee) {
        String sql = "DELETE FROM exercise WHERE id=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, ee.getId());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public ArrayList<Exercise> getExercise() {
        ArrayList<Exercise> exercises = new ArrayList<>();
        String sql = "SELECT * FROM exercise";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Exercise ee = new Exercise();
                ee.setId(rs.getInt("id"));
                ee.setExerciseName(rs.getString("ExerciseName"));
                ee.setCalorieburn(rs.getDouble("CalorieburnPerMin"));
                ee.setWorkoutType(rs.getString("workoutType"));
                ee.setReps(rs.getInt("reps"));
                ee.setWeightUsed(rs.getDouble("weightUsed"));
                exercises.add(ee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exercises;
    }

    @Override
    public Exercise getById(int exerciseID) {
        String sql = "SELECT * FROM exercise WHERE id=?";
        Exercise ee = new Exercise();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, exerciseID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                ee.setId(rs.getInt("id"));
                ee.setExerciseName(rs.getString("ExerciseName"));
                ee.setCalorieburn(rs.getDouble("CalorieburnPerMin"));
                ee.setWorkoutType(rs.getString("workoutType"));
                ee.setReps(rs.getInt("reps"));
                ee.setWeightUsed(rs.getDouble("weightUsed"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ee;
    }
}
