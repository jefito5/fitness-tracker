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
	public ExerciseDB(){
		conn=ConnectionFactory.getConnection();
	}
	@Override
	public int insertExercise(Exercise ee) {
		String sql="insert into exercise(ExerciseName,CalorieburnPerMin) values(?,?)";
		try{
			PreparedStatement pstmt=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, ee.getExerciseName());
			pstmt.setDouble(2, ee.getCalorieburn());
			
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
	public int updateExercise(Exercise ee) {
		String sql="update exercise set ExerciseName=?,CalorieBurnPerMin=? where id=?";
		try{
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(3, ee.getId());
			pstmt.setString(1, ee.getExerciseName());
			pstmt.setDouble(2, ee.getCalorieburn());
	
			
			return pstmt.executeUpdate();
			
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int deleteExercise(Exercise ee) {
		String sql="delete from exercise where id=?";
		try{
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, ee.getId());
			return pstmt.executeUpdate();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public ArrayList<Exercise> getExercise() {
		ArrayList<Exercise> exercises=new ArrayList<>();
		String sql="select * from exercise";
		try{
			PreparedStatement pstmt=conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			
			while(rs.next()){
				Exercise ee =new Exercise();
				ee.setId(rs.getInt("id"));
				ee.setExerciseName(rs.getString("ExerciseName"));
				ee.setCalorieburn(rs.getDouble("CalorieBurn"));
				exercises.add(ee);
				
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return exercises;
	}


	@Override
	public Exercise getById(int exerciseID) {
		String sql="select * from exercise where id=?";
		Exercise ee =new Exercise();
		try{
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, exerciseID);  //make this int later
			ResultSet rs=pstmt.executeQuery();
			
			if(rs.next()){
				ee.setId(rs.getInt("id"));
				ee.setExerciseName(rs.getString("ExerciseName"));
				ee.setCalorieburn(rs.getInt("CalorieBurnPerMin"));
					
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return ee;
	}
}
