package impl;

import database.ConnectionFactory;
import models.Meal;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class MealDB {

    public void insert(Meal meal) {
        try {
            Connection c = ConnectionFactory.getConnection();
            PreparedStatement ps = c.prepareStatement(
                "INSERT INTO meals (name, grams, calories) VALUES (?, ?, ?)"
            );
            ps.setString(1, meal.getName());
            ps.setDouble(2, meal.getGrams());
            ps.setDouble(3, meal.getCaloriesPerGram());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
