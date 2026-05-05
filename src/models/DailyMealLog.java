package models;

import java.util.Date;

public class DailyMealLog {
	private int id;
	private double totalCalorieIntake;
	private Date date;
	private int userId;
	private int mealId;
	private double totalC;
	
	
	public double getTotalC() {
		return totalC;
	}
	public void setTotalC(double totalC) {
		this.totalC = totalC;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getTotalCalorieIntake() {
		return totalCalorieIntake;
	}
	public void setTotalCalorieIntake(double totalCalorieIntake) {
		this.totalCalorieIntake = totalCalorieIntake;
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
	public int getMealId() {
		return mealId;
	}
	public void setMealId(int mealId) {
		this.mealId = mealId;
	}	
}
