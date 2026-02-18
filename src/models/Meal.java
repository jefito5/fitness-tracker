package models;

public class Meal {
    private String name;
    private double grams;
    private double caloriesPerGram;

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

    public double getCaloriesPerGram() {
        return caloriesPerGram;
    }
}
