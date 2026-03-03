package impl;

import database.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.Exercise;

public class ExerciseDB {

    public void insert(Exercise exercise) {
        try {
            Connection c = ConnectionFactory.getConnection();
            PreparedStatement ps = c.prepareStatement(
                "INSERT INTO exercises (exerciseName, calorieburn) VALUES (?, ?)"
            );
            ps.setString(1, exercise.getExerciseName());
            ps.setDouble(2, exercise.getCalorieburn());
            ps.execute();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Exercise> getAll() {
        List<Exercise> list = new ArrayList<>();
        try {
            Connection c = ConnectionFactory.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT * FROM exercises");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Exercise ex = new Exercise();
                ex.setId(rs.getInt("id"));
                ex.setExerciseName(rs.getString("exerciseName"));
                ex.setCalorieburn(rs.getDouble("calorieburn"));
                list.add(ex);
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Exercise getById(int id) {
        Exercise ex = null;
        try {
            Connection c = ConnectionFactory.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT * FROM exercises WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ex = new Exercise();
                ex.setId(rs.getInt("id"));
                ex.setExerciseName(rs.getString("exerciseName"));
                ex.setCalorieburn(rs.getDouble("calorieburn"));
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ex;
    }

    public void update(Exercise exercise) {
        try {
            Connection c = ConnectionFactory.getConnection();
            PreparedStatement ps = c.prepareStatement(
                "UPDATE exercises SET exerciseName = ?, calorieburn = ? WHERE id = ?"
            );
            ps.setString(1, exercise.getExerciseName());
            ps.setDouble(2, exercise.getCalorieburn());
            ps.setInt(3, exercise.getId());
            ps.execute();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try {
            Connection c = ConnectionFactory.getConnection();
            PreparedStatement ps = c.prepareStatement("DELETE FROM exercises WHERE id = ?");
            ps.setInt(1, id);
            ps.execute();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
