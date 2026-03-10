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
	}
	@Override
	public int insertMeal(Meal m) {
		String sql="insert into meals(MealName,CaloriePerGram) values(?,?)";
		try{
			PreparedStatement pstmt=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, m.getName());
			pstmt.setDouble(2, m.getCaloriesPerGram());
			
			if(pstmt.executeUpdate()>0){
				ResultSet rs=pstmt.getGeneratedKeys();
				
				if(rs.next())
					return rs.getInt(1);

			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return 0;
	}
	@Override
	public int updateMeal(Meal m) {
		String sql="update meals set MealName=?,CaloriePerGram=? where id=?";
		try{
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(3, m.getId());
			pstmt.setString(1, m.getName());
			pstmt.setDouble(2, m.getCaloriesPerGram());
	
			
			return pstmt.executeUpdate();
			
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public int deleteMeal(Meal m) {
		String sql="delete from meals where id=?";
		try{
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, m.getId());
			return pstmt.executeUpdate();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public ArrayList<Meal> getAll() {
		
		ArrayList<Meal> meals=new ArrayList<>();
		String sql="select * from meals";
		try{
			PreparedStatement pstmt=conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			
			while(rs.next()){
				Meal m =new Meal();
				m.setId(rs.getInt("id"));
				m.setMealName(rs.getString("MealName"));
				m.setCaloriesPerGram(rs.getDouble("CaloriePerGram"));
				meals.add(m);
				
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return meals;
		
	}
	@Override
	public Meal getById(int mealID) {
		String sql="select * from meals where id=?";
		Meal m =new Meal();
		try{
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, mealID);  //make this int later
			ResultSet rs=pstmt.executeQuery();
			
			if(rs.next()){
				m.setId(rs.getInt("id"));
				m.setMealName(rs.getString("MealName"));
				m.setCaloriesPerGram((rs.getDouble("CaloriePerGram")));	
			}	
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return m;
	}



}
