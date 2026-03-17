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
	

	public void ensureDurationColumn() {
		try {
			java.sql.Statement st = conn.createStatement();
			java.sql.ResultSet rs = st.executeQuery("PRAGMA table_info(DailyExerciseLog)");
			boolean found = false;
			while (rs.next()) {
				if ("durationMinutes".equalsIgnoreCase(rs.getString("name"))) { found = true; break; }
			}
			rs.close(); st.close();
			if (!found) {
				conn.createStatement().execute("ALTER TABLE DailyExerciseLog ADD COLUMN durationMinutes REAL DEFAULT 0");
				System.out.println(">>> durationMinutes stulpelis pridėtas <<<");
			}
		} catch (Exception e) { e.printStackTrace(); }
	}

	@Override
	public int insertDailyLog(DailyExerciseLog el) {
	String sql="insert into DailyExerciseLog(totalCalorieBurn,exerciseID,userId,Date,durationMinutes) values(?,?,?,?,?)";
		LocalDate today=LocalDate.now();
		try{
			PreparedStatement pstmt=conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setDouble(1, el.getTotalCalorieBurn());
			pstmt.setInt(2, el.getExerciseId());
			pstmt.setInt(3, el.getUserId());
			String ddd=String.valueOf(today);
			pstmt.setObject(4, ddd);
			pstmt.setDouble(5, el.getDurationMinutes());
			
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

	public java.util.ArrayList<Object[]> getTodayLogs(int userId) {
		java.util.ArrayList<Object[]> list = new java.util.ArrayList<>();
		LocalDate today = LocalDate.now();
		String sql =
			"SELECT del.id, e.ExerciseName, e.workoutType, del.totalCalorieBurn, e.reps, e.weightUsed, del.durationMinutes " +
			"FROM DailyExerciseLog del " +
			"JOIN exercise e ON del.exerciseID = e.id " +
			"WHERE del.userId = ? AND del.Date = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, userId);
			ps.setString(2, String.valueOf(today));
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new Object[]{
					rs.getInt("id"),
					rs.getString("ExerciseName"),
					rs.getString("workoutType"),
					rs.getDouble("totalCalorieBurn"),
					rs.getInt("reps"),
					rs.getDouble("weightUsed"),
					rs.getDouble("durationMinutes")
				});
			}
		} catch (SQLException e) { e.printStackTrace(); }
		return list;
	}

	public int deleteTodayLog(int logId) {
		String sql = "DELETE FROM DailyExerciseLog WHERE id = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, logId);
			return ps.executeUpdate();
		} catch (SQLException e) { e.printStackTrace(); }
		return 0;
	}

	/**
	 * Grąžina konkretaus vartotojo treniruočių žurnalą pasirinktai datai.
	 * @param userId  vartotojo ID
	 * @param date    data formatu "yyyy-MM-dd"
	 */
	public java.util.ArrayList<Object[]> getLogsByDate(int userId, String date) {
		java.util.ArrayList<Object[]> list = new java.util.ArrayList<>();
		String sql =
			"SELECT del.id, e.ExerciseName, e.workoutType, del.totalCalorieBurn, e.reps, e.weightUsed, del.durationMinutes " +
			"FROM DailyExerciseLog del " +
			"JOIN exercise e ON del.exerciseID = e.id " +
			"WHERE del.userId = ? AND del.Date = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, userId);
			ps.setString(2, date);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new Object[]{
					rs.getInt("id"),
					rs.getString("ExerciseName"),
					rs.getString("workoutType"),
					rs.getDouble("totalCalorieBurn"),
					rs.getInt("reps"),
					rs.getDouble("weightUsed"),
					rs.getDouble("durationMinutes")
				});
			}
		} catch (SQLException e) { e.printStackTrace(); }
		return list;
	}

	/**
	 * Grąžina visas dienas nuo pirmojo DB įrašo (treniruotė ARBA maistas) iki šiandien.
	 */
	public java.util.ArrayList<String> getAllLogDates(int userId) {
		java.util.ArrayList<String> dates = new java.util.ArrayList<>();
		// Rasti seniausią datą iš abiejų lentelių
		String sql =
			"SELECT MIN(earliest) FROM (" +
			"  SELECT MIN(Date) AS earliest FROM DailyExerciseLog WHERE userId = ?" +
			"  UNION ALL" +
			"  SELECT MIN(Date) AS earliest FROM DailyMealLog WHERE userId = ?" +
			")";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, userId);
			ps.setInt(2, userId);
			ResultSet rs = ps.executeQuery();
			if (rs.next() && rs.getString(1) != null) {
				java.time.LocalDate start = java.time.LocalDate.parse(rs.getString(1));
				java.time.LocalDate today = java.time.LocalDate.now();
				// Generuoti kiekvieną dieną nuo start iki today (naujiausios viršuje)
				java.time.LocalDate d = today;
				while (!d.isBefore(start)) {
					dates.add(d.toString());
					d = d.minusDays(1);
				}
			} else {
				// Jei DB tuščia — rodyti tik šiandieną
				dates.add(java.time.LocalDate.now().toString());
			}
		} catch (SQLException e) { e.printStackTrace(); }
		return dates;
	}

}