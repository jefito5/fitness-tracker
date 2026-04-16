package impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import database.ConnectionFactory;
import database.ImealDB;
import models.Meal;


public class MealDB implements ImealDB{
	
	private Connection conn;
	public MealDB(){
		conn=ConnectionFactory.getConnection();
		initSchema();
	}

	/**
	 * Adds the three macro columns to the meals table if they do not already exist.
	 * Each ALTER TABLE is wrapped in its own try-catch so a pre-existing column
	 * (which causes an exception in SQLite) does not block the others.
	 */
	private void initSchema() {
		String[] alters = {
			"ALTER TABLE meals ADD COLUMN protein_per_100g REAL DEFAULT 0",
			"ALTER TABLE meals ADD COLUMN carbs_per_100g   REAL DEFAULT 0",
			"ALTER TABLE meals ADD COLUMN fat_per_100g     REAL DEFAULT 0"
		};
		for (String sql : alters) {
			try (Statement st = conn.createStatement()) {
				st.execute(sql);
			} catch (SQLException e) {
				// Column already exists – safe to ignore
			}
		}
	}

	@Override
	public int insertMeal(Meal m) {
		String sql = "INSERT INTO meals(MealName, CaloriePerGram, protein_per_100g, carbs_per_100g, fat_per_100g) " +
		             "VALUES(?,?,?,?,?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, m.getName());
			pstmt.setDouble(2, m.getCaloriesPerGram());
			pstmt.setDouble(3, m.getProteinPer100g());
			pstmt.setDouble(4, m.getCarbsPer100g());
			pstmt.setDouble(5, m.getFatPer100g());

			if (pstmt.executeUpdate() > 0) {
				ResultSet rs = pstmt.getGeneratedKeys();
				if (rs.next())
					return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int updateMeal(Meal m) {
		String sql = "UPDATE meals SET MealName=?, CaloriePerGram=?, " +
		             "protein_per_100g=?, carbs_per_100g=?, fat_per_100g=? WHERE id=?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, m.getName());
			pstmt.setDouble(2, m.getCaloriesPerGram());
			pstmt.setDouble(3, m.getProteinPer100g());
			pstmt.setDouble(4, m.getCarbsPer100g());
			pstmt.setDouble(5, m.getFatPer100g());
			pstmt.setInt(6, m.getId());
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int deleteMeal(Meal m) {
		String sql = "DELETE FROM meals WHERE id=?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, m.getId());
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public ArrayList<Meal> getAll() {
		ArrayList<Meal> meals = new ArrayList<>();
		String sql = "SELECT * FROM meals";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Meal m = new Meal();
				m.setId(rs.getInt("id"));
				m.setMealName(rs.getString("MealName"));
				m.setCaloriesPerGram(rs.getDouble("CaloriePerGram"));
				m.setProteinPer100g(rs.getDouble("protein_per_100g"));
				m.setCarbsPer100g(rs.getDouble("carbs_per_100g"));
				m.setFatPer100g(rs.getDouble("fat_per_100g"));
				meals.add(m);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return meals;
	}

	@Override
	public Meal getById(int mealID) {
		String sql = "SELECT * FROM meals WHERE id=?";
		Meal m = new Meal();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, mealID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				m.setId(rs.getInt("id"));
				m.setMealName(rs.getString("MealName"));
				m.setCaloriesPerGram(rs.getDouble("CaloriePerGram"));
				m.setProteinPer100g(rs.getDouble("protein_per_100g"));
				m.setCarbsPer100g(rs.getDouble("carbs_per_100g"));
				m.setFatPer100g(rs.getDouble("fat_per_100g"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return m;
	}
}