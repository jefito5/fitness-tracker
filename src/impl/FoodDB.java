package impl;

import database.ConnectionFactory;
import database.IfoodDB; // Importuojame naują sąsają
import models.Food;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodDB implements IfoodDB { // Pridėtas implements
    private Connection conn;

    public FoodDB() {
        this.conn = ConnectionFactory.getConnection();
    }

    @Override // Pridėta anotacija
    public boolean isEmpty() {
        String sql = "SELECT COUNT(*) FROM foods";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1) == 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override // Pridėta anotacija
    public void insert(Food food) {
        String sql = "INSERT INTO foods (name, calories_per_100g, protein_per_100g, carbs_per_100g, fat_per_100g) VALUES (?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, food.getName());
            pstmt.setDouble(2, food.getCalories());
            pstmt.setDouble(3, food.getProtein());
            pstmt.setDouble(4, food.getCarbs());
            pstmt.setDouble(5, food.getFat());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override // Pridėta anotacija
    public List<Food> searchByName(String query) {
        List<Food> results = new ArrayList<>();
        String sql = "SELECT * FROM foods WHERE name LIKE ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + query + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                results.add(new Food(
                    rs.getString("name"),
                    rs.getDouble("calories_per_100g"),
                    rs.getDouble("protein_per_100g"),
                    rs.getDouble("carbs_per_100g"),
                    rs.getDouble("fat_per_100g")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    public List<Food> getAll() {
        List<Food> results = new ArrayList<>();
        String sql = "SELECT * FROM foods";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                results.add(new Food(
                    rs.getString("name"),
                    rs.getDouble("calories_per_100g"),
                    rs.getDouble("protein_per_100g"),
                    rs.getDouble("carbs_per_100g"),
                    rs.getDouble("fat_per_100g")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * Patikrina ar maistas su tokiu pavadinimu jau yra DB (case-insensitive).
     * Jei ne — įrašo. Grąžina true jei įrašyta, false jei jau egzistavo.
     */
    public boolean insertIfNew(Food food) {
        String checkSql = "SELECT COUNT(*) FROM foods WHERE LOWER(name) = LOWER(?)";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, food.getName().trim());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false; // Jau egzistuoja
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        insert(food);
        return true;
    }
}