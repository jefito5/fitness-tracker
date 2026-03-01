package models;

public class Meal {
    private String name;
    private double grams;
    private double caloriesPerGram;

	public Meal(){}
    public Meal(String name, double grams, double caloriesPerGram) {
        this.name = name;
        this.grams = grams;
        this.caloriesPerGram = caloriesPerGram;
    }
    public String getName() {
        return name;
    }

    public double getGrams() {
        return grams;
    }

    public void setMealName(String mealName) {
		this.name = mealName;
	}
	public double getcaloriesPerGram() {
		return caloriesPerGram;
	}
	public void setCalorie(double caloriesPerGram) {
		this.caloriesPerGram = caloriesPerGram;
	}
}
