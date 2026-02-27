package models;

public class Meal {
	private int id;
	private String mealName;
	private double calorie;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMealName() {
		return mealName;
	}
	public void setMealName(String mealName) {
		this.mealName = mealName;
	}
	public double getCalorie() {
		return calorie;
	}
	public void setCalorie(double calorie) {
		this.calorie = calorie;
	}	
}
