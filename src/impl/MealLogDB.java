package impl;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.ConnectionFactory;
import database.IdailylogDB;
import models.DailyMealLog;
import models.User;

public class MealLogDB implements IdailylogDB{
	private Connection conn;
	public MealLogDB(){
		conn=ConnectionFactory.getConnection();
	}

	@Override
	public int insertDailyLog(DailyMealLog ml) {
		String sql="insert into DailyMealLog(totalCalorieIntake,mealID,userId,Date) values(?,?,?,?)";
		LocalDate today=LocalDate.now();
		try{
			PreparedStatement pstmt=conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setDouble(1, ml.getTotalCalorieIntake());
			pstmt.setInt(2, ml.getMealId());
			pstmt.setInt(3, ml.getUserId());
			String dd=String.valueOf(today);
			pstmt.setObject(4, dd);
			
			if(pstmt.executeUpdate()>0){
				ResultSet rs=pstmt.getGeneratedKeys();
				
				if(rs.next())
					return rs.getInt(1);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public ArrayList<DailyMealLog> getAll() {
		ArrayList<DailyMealLog> calorie=new ArrayList<>();
		LocalDate today=LocalDate.now();
		String sql="select totalCalorieIntake from dailymeallog where Date=?";
		try{
			PreparedStatement pstmt=conn.prepareStatement(sql);
			String aa=String.valueOf(today);
			pstmt.setObject(1, aa);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				DailyMealLog dml=new DailyMealLog();
				//dml.setId(rs.getInt("id"));
			
				dml.setTotalCalorieIntake(rs.getDouble("totalCalorieIntake"));
				calorie.add(dml);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return calorie;
	}
	@Override
	public ArrayList<Double> getSum(){
		ArrayList<Double> aa=new ArrayList<>();
		LocalDate today=LocalDate.now();
		User u=new User();
		String sql="select totalCalorieIntake from dailymeallog where Date=?";
		try{
			PreparedStatement pstmt=conn.prepareStatement(sql);
			String asd=String.valueOf(today);
			pstmt.setObject(1, asd);
			//pstmt.setInt(2, u.getId());
			ResultSet rs=pstmt.executeQuery();
			
			while(rs.next()){
			aa.add(rs.getDouble("totalCalorieIntake"));
			
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return aa;

	}

	public ArrayList<Object[]> getTodayMealLogs(int userId) {
		ArrayList<Object[]> logs = new ArrayList<>();
		LocalDate today = LocalDate.now();
		String sql = "SELECT d.mealID, m.MealName, m.CaloriePerGram, d.totalCalorieIntake " +
				"FROM DailyMealLog d JOIN meals m ON d.mealID = m.id " +
				"WHERE d.Date=? AND d.userId=?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, String.valueOf(today));
			pstmt.setInt(2, userId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Object[] row = new Object[4];
				row[0] = rs.getInt("mealID");
				row[1] = rs.getString("MealName");
				row[2] = rs.getDouble("CaloriePerGram");
				row[3] = rs.getDouble("totalCalorieIntake");
				logs.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return logs;
	}


	public ArrayList<Object[]> getMealLogsByDate(int userId, String date) {
		ArrayList<Object[]> logs = new ArrayList<>();
		String sql = "SELECT d.mealID, m.MealName, m.CaloriePerGram, d.totalCalorieIntake " +
				"FROM DailyMealLog d JOIN meals m ON d.mealID = m.id " +
				"WHERE d.Date=? AND d.userId=?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, date);
			pstmt.setInt(2, userId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Object[] row = new Object[4];
				row[0] = rs.getInt("mealID");
				row[1] = rs.getString("MealName");
				row[2] = rs.getDouble("CaloriePerGram");
				row[3] = rs.getDouble("totalCalorieIntake");
				logs.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return logs;
	}

	public double getTodayCalories(int userId) {
		String sql = "SELECT SUM(totalCalorieIntake) FROM DailyMealLog WHERE userId=? AND Date=?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			pstmt.setString(2, String.valueOf(LocalDate.now()));
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) return rs.getDouble(1);
		} catch (SQLException e) { e.printStackTrace(); }
		return 0;
	}

	/**
	 * Returns total macronutrients consumed for the given user on the given date.
	 * Formula:  grams_consumed = totalCalorieIntake * 100 / CaloriePerGram
	 *           macro_total    = grams_consumed * macro_per_100g / 100
	 *                         = totalCalorieIntake * macro_per_100g / CaloriePerGram
	 *
	 * @return double[3]  { totalProtein, totalCarbs, totalFat }  – all in grams, never null.
	 */
	public double[] getMacroSummary(int userId, String date) {
		double[] result = {0.0, 0.0, 0.0};
		String sql =
			"SELECT " +
			"  SUM(d.totalCalorieIntake * COALESCE(m.protein_per_100g, 0) / NULLIF(m.CaloriePerGram, 0)) AS totalProtein, " +
			"  SUM(d.totalCalorieIntake * COALESCE(m.carbs_per_100g,   0) / NULLIF(m.CaloriePerGram, 0)) AS totalCarbs,   " +
			"  SUM(d.totalCalorieIntake * COALESCE(m.fat_per_100g,     0) / NULLIF(m.CaloriePerGram, 0)) AS totalFat      " +
			"FROM DailyMealLog d " +
			"JOIN meals m ON d.mealID = m.id " +
			"WHERE d.Date = ? AND d.userId = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, date);
			pstmt.setInt(2, userId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				result[0] = rs.getDouble("totalProtein");
				result[1] = rs.getDouble("totalCarbs");
				result[2] = rs.getDouble("totalFat");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

}