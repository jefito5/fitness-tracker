package models;

public class Food {
    private int id;
    private String name;
    private double calories;
    private double protein;
    private double carbs;
    private double fat;

    public Food(String name, double calories, double protein, double carbs, double fat) {
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public double getCalories() { return calories; }
    public double getProtein() { return protein; }
    public double getCarbs() { return carbs; }
    public double getFat() { return fat; }
}