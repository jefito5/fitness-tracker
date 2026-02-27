package models;

public class Meal {
	private int id;
	private String mealName;
	private double caloriesPerGram;
	
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
	public double getcaloriesPerGram() {
		return caloriesPerGram;
	}
	public void setcaloriesPerGram(double caloriesPerGram) {
		this.caloriesPerGram = caloriesPerGram;
	}	
}
