package models;

public class Exercise {
	private int id;
	private String exerciseName;
	private double calorieburn;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getExerciseName() {
		return exerciseName;
	}
	public void setExerciseName(String exerciseName) {
		this.exerciseName = exerciseName;
	}
	public double getCalorieburn() {
		return calorieburn;
	}
	public void setCalorieburn(double d) {
		this.calorieburn = d;
	}
}
