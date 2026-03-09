package models;

import java.util.Date;

public class DailyExerciseLog {
	private int id;
	private double totalCalorieBurn;
	private Date date;
	private int userId;
	private int exerciseId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getTotalCalorieBurn() {
		return totalCalorieBurn;
	}
	public void setTotalCalorieBurn(double totalCalorieBurn) {
		this.totalCalorieBurn = totalCalorieBurn;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getExerciseId() {
		return exerciseId;
	}
	public void setExerciseId(int exerciseId) {
		this.exerciseId = exerciseId;
	}
	
}
