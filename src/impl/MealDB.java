package impl;

import database.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.Meal;

public class MealDB {

    public void insert(Meal meal) {
        try {
            Connection c = ConnectionFactory.getConnection();
            PreparedStatement ps = c.prepareStatement(
                "INSERT INTO meals (name, grams, calories) VALUES (?, ?, ?)"
            );
            ps.setString(1, meal.getName());
            ps.setDouble(2, meal.getGrams());
            ps.setDouble(3, meal.getcaloriesPerGram());
            ps.execute();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Meal> getAll() {
        List<Meal> list = new ArrayList<>();
        try {
            Connection c = ConnectionFactory.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT * FROM meals");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Meal meal = new Meal(
                    rs.getString("name"),
                    rs.getDouble("grams"),
                    rs.getDouble("calories")
                );
                list.add(meal);
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void update(String name, Meal meal) {
        try {
            Connection c = ConnectionFactory.getConnection();
            PreparedStatement ps = c.prepareStatement(
                "UPDATE meals SET name = ?, grams = ?, calories = ? WHERE name = ?"
            );
            ps.setString(1, meal.getName());
            ps.setDouble(2, meal.getGrams());
            ps.setDouble(3, meal.getCaloriesPerGram());
            ps.setString(4, name);
            ps.execute();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(String name) {
        try {
            Connection c = ConnectionFactory.getConnection();
            PreparedStatement ps = c.prepareStatement("DELETE FROM meals WHERE name = ?");
            ps.setString(1, name);
            ps.execute();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
