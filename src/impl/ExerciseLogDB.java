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

public class ExerciseLogDB implements IdailyElogDB  {

	private Connection conn;
	public ExerciseLogDB(){
		conn=ConnectionFactory.getConnection();
	}
	
	@Override
	public int insertDailyLog(DailyExerciseLog el) {
	String sql="insert into DailyExerciseLog(totalCalorieBurn,exerciseID,userId,Date) values(?,?,?,?)";
		LocalDate today=LocalDate.now();
		try{
			PreparedStatement pstmt=conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setDouble(1, el.getTotalCalorieBurn());
			pstmt.setInt(2, el.getExerciseId());
			pstmt.setInt(3, el.getUserId());
			String ddd=String.valueOf(today);
			pstmt.setObject(4, ddd);
			
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
	public ArrayList<Double> getburnSum(){
		ArrayList<Double> aea=new ArrayList<>();
		LocalDate today=LocalDate.now();
		String sql="select totalCalorieBurn from dailyexerciselog where Date=?";
		try{
			PreparedStatement pstmt=conn.prepareStatement(sql);
			String asd=String.valueOf(today);
			pstmt.setObject(1, asd);
			ResultSet rs=pstmt.executeQuery();
			
			while(rs.next()){
			aea.add(rs.getDouble("totalCalorieBurn"));
			

			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return aea;
		
	}

}
