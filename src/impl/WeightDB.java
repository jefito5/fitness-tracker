package impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import database.ConnectionFactory;
import database.IweightDB;
import models.User;
import models.Weight;

public class WeightDB implements IweightDB{
	private Connection conn;
	public WeightDB(){
		conn=ConnectionFactory.getConnection();
	}
	@Override
	public int insertWeight(Weight w) {
		String sql="insert into weights(WeightM,WeightE,UserId,Date,average) values(?,?,?,?,?)";
		LocalDate today = LocalDate.now();
		try{
			PreparedStatement pstmt=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			pstmt.setDouble(1, w.getWeightM());
			pstmt.setInt(3, w.getUserId());
			pstmt.setInt(2, 0);
			pstmt.setDouble(5, 0);
			String ss= String.valueOf(today);
			pstmt.setObject(4, ss);

			
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
	public int updateWeight(Weight w) {
		String sql="update weights set WeightE=?,average=? where Date=? AND UserId=?";
		LocalDate today = LocalDate.now();
		try{
			PreparedStatement pstmt = conn.prepareStatement(sql);
			String aa=String.valueOf(today);
			pstmt.setObject(3, aa);
			pstmt.setDouble(1, w.getWeightE());
			pstmt.setDouble(2, w.getAverage());
			pstmt.setInt(4, w.getUserId());
			return pstmt.executeUpdate();
			
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public ArrayList<Double> getWeight(Weight wt) {
		ArrayList<Double> weights=new ArrayList<>();
		String sql="select * from weights where Date>=? AND Date<=? AND UserId=?";
		try{
			PreparedStatement pstmt=conn.prepareStatement(sql);
			//Weight wt=new Weight();
			String asd=String.valueOf(wt.getPeriod1());
			pstmt.setObject(1, asd);
			String asds=String.valueOf(wt.getPeriod2());
			pstmt.setObject(2, asds);
			pstmt.setInt(3, wt.getUserId());
			ResultSet rs=pstmt.executeQuery();
			
			while(rs.next()){
				weights.add(rs.getDouble("average"));		
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return weights;
	}

	@Override
	public ArrayList<Double> getList() {
		ArrayList<Double> got=new ArrayList<>();
		LocalDate today=LocalDate.now();
		String sql="select * from weights where Date=?";
		try{
			PreparedStatement pstmt=conn.prepareStatement(sql);
			Weight wt=new Weight();
			String asd=String.valueOf(today);
			pstmt.setObject(1, asd);
			//pstmt.setInt(2, wt.getUserId());

			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				//got.add(rs.getDouble("average"));
				got.add(rs.getDouble("WeightM"));
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return got;
	}
	@Override
	public ArrayList<Double> getAverages() {
		ArrayList<Double> gets=new ArrayList<>();
		LocalDate today=LocalDate.now();
		// Try today's average first
		String sql="select * from weights where Date=?";
		try{
			PreparedStatement pstmt=conn.prepareStatement(sql);
			String asd=String.valueOf(today);
			pstmt.setObject(1, asd);

			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				gets.add(rs.getDouble("average"));
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		// If no weight for today, fall back to the most recent entry
		if(gets.isEmpty() || gets.get(0)==0){
			gets.clear();
			String fallback="select WeightM from weights ORDER BY Date DESC LIMIT 1";
			try{
				PreparedStatement pstmt=conn.prepareStatement(fallback);
				ResultSet rs=pstmt.executeQuery();
				if(rs.next()){
					gets.add(rs.getDouble("WeightM"));
				}
			}
			catch(SQLException e){
				e.printStackTrace();
			}
		}
		return gets;
	}
		
	@Override
	public ArrayList<Double> getStartEnd() {
		ArrayList<Double> start=new ArrayList<>();
		//LocalDate today=LocalDate.now();
		String sql="select * from weights";
		try{
			PreparedStatement pstmt=conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				start.add(rs.getDouble("average"));
				//gets.add(rs.getDouble("WeightM"));
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return start;
	}
	
	// PRIDĖTA: Naujas metodas diagramoms su DIAGNOSTIKA
    public ArrayList<Weight> getWeightsByDateRange(int userId, String startDate, String endDate) {
        ArrayList<Weight> weightsInRange = new ArrayList<>();
        String sql = "SELECT * FROM weights WHERE UserId = ? AND Date >= ? AND Date <= ? ORDER BY Date ASC";
        
        try {
            System.out.println("DIAGNOSTIKA: Ieškome svorių. UserID=" + userId + ", Nuo=" + startDate + ", Iki=" + endDate);
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setString(2, startDate);
            pstmt.setString(3, endDate);
            
            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next()){
                Weight w = new Weight();
                w.setWeightId(rs.getInt("id"));
                w.setWeightM(rs.getDouble("WeightM"));
                w.setWeightE(rs.getDouble("WeightE"));
                w.setUserId(rs.getInt("UserId"));
                w.setDate(java.sql.Date.valueOf(rs.getString("Date"))); 
                w.setAverage(rs.getDouble("average"));
                weightsInRange.add(w);
                
                System.out.println("DIAGNOSTIKA: Rastas svoris " + w.getAverage() + " kg, data: " + rs.getString("Date"));
            }
            
            System.out.println("DIAGNOSTIKA: Iš viso rasta įrašų šiame periode: " + weightsInRange.size());
            
        } catch(Exception e) { // Gaudo absoliučiai visas klaidas!
            System.out.println("DIAGNOSTIKA: Įvyko klaida traukiant duomenis:");
            e.printStackTrace();
        }
        return weightsInRange;
    }
	
}
