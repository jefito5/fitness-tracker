package models;

public class Meal {
    private int id;
    private String name;
    private double grams;
    private double caloriesPerGram;
    private double proteinPer100g;
    private double carbsPer100g;
    private double fatPer100g;

	public Meal(){}
    public Meal(String name, double grams, double caloriesPerGram) {
        this.name = name;
        this.grams = grams;
        this.caloriesPerGram = caloriesPerGram;
    }

    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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

	public double getCaloriesPerGram() {
		return caloriesPerGram;
	}
	public void setCaloriesPerGram(double caloriesPerGram) {
		this.caloriesPerGram = caloriesPerGram;
	}

	public double getProteinPer100g() {
		return proteinPer100g;
	}
	public void setProteinPer100g(double proteinPer100g) {
		this.proteinPer100g = proteinPer100g;
	}

	public double getCarbsPer100g() {
		return carbsPer100g;
	}
	public void setCarbsPer100g(double carbsPer100g) {
		this.carbsPer100g = carbsPer100g;
	}

	public double getFatPer100g() {
		return fatPer100g;
	}
	public void setFatPer100g(double fatPer100g) {
		this.fatPer100g = fatPer100g;
	}
}