package impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import database.ConnectionFactory;
import database.IwaistDB;
import models.Waist;
public class WaistDB implements IwaistDB{
	private Connection conn;
	public WaistDB(){
		conn=ConnectionFactory.getConnection();
	}

	@Override
	public Waist getById(int waistID) {
		String sql="select * from waists where id=?";
		Waist ww =new Waist();
		try{
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, waistID);  //make this int later
			ResultSet rs=pstmt.executeQuery();
			
			if(rs.next()){
				ww.setWaistId(rs.getInt("waistId"));
				ww.setWaistM(rs.getDouble("WaistM"));
				ww.setWaistE(rs.getDouble("WaistE"));
				ww.setUserId(rs.getInt("UserId"));
				ww.setDate(rs.getDate("Date"));
				
			}
			
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return ww;
	}

	@Override
	public int insertWaist(Waist w) {
		String sql="insert into waists(WaistM,WaistE,UserId,Date,average) values(?,?,?,?,?)";
		LocalDate today = LocalDate.now();
		try{
			PreparedStatement pstmt=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			pstmt.setDouble(1, w.getWaistM());
			pstmt.setInt(3, w.getUserId());
			pstmt.setInt(2, 0);
			String ss= String.valueOf(today);
			pstmt.setObject(4, ss);
			pstmt.setDouble(5, 0);

			
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
	public int updateWaist(Waist ww) {
		String sql="update waists set WaistE=?,average=? where Date=? AND UserId=?";
		LocalDate today = LocalDate.now();
		try{
			PreparedStatement pstmt = conn.prepareStatement(sql);
			String aa=String.valueOf(today);
			pstmt.setObject(3, aa);
			pstmt.setInt(4, ww.getUserId());
			pstmt.setDouble(1, ww.getWaistE());
			pstmt.setDouble(2, ww.getAverage());
			
	
			
			return pstmt.executeUpdate();
			
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public ArrayList<Waist> getWaist() {
		ArrayList<Waist> waists=new ArrayList<>();
		String sql="select * from waist";
		try{
			PreparedStatement pstmt=conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			
			while(rs.next()){
				Waist ww =new Waist();
				ww.setWaistId(rs.getInt("waistId"));
				ww.setWaistM(rs.getDouble("WaistM"));
				ww.setWaistE(rs.getDouble("WaistE"));
				ww.setUserId(rs.getInt("UserId"));
				ww.setDate(rs.getDate("Date"));
				waists.add(ww);
				
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return waists;
	}


	@Override
	public ArrayList<Double> getLists() {
		ArrayList<Double> gots=new ArrayList<>();
		LocalDate today=LocalDate.now();
		String sql="select * from waists where Date=?";
		try{
			PreparedStatement pstmt=conn.prepareStatement(sql);
			String asd=String.valueOf(today);
			pstmt.setObject(1, asd);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				gots.add(rs.getDouble("WaistM"));
				//got.add(rs.getDouble("WeightE"));
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return gots;
	}	
}

