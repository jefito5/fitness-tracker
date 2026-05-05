package models;

public class User {
    private int id;
    private String name;
    private String gender;
    private int age;
    private String password;
    private double height; // in centimeters
    private int calorieGoal = 2000; // daily calorie goal

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
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
    public void setName(String name) {
        this.name = name;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public double getHeight() {
        return height;
    }
    public void setHeight(double height) {
        this.height = height;
    }
    public int getCalorieGoal() {
        return calorieGoal;
    }
    public void setCalorieGoal(int calorieGoal) {
        this.calorieGoal = calorieGoal;
    }
}
