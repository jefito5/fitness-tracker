package models;

public class Exercise {
    private int id;
    private String exerciseName;
    private double calorieburn;
    private String workoutType;
    private int reps;
    private double weightUsed;
    private String muscleGroup;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getExerciseName() { return exerciseName; }
    public void setExerciseName(String exerciseName) { this.exerciseName = exerciseName; }

    public double getCalorieburn() { return calorieburn; }
    public void setCalorieburn(double d) { this.calorieburn = d; }

    public String getWorkoutType() { return workoutType; }
    public void setWorkoutType(String workoutType) { this.workoutType = workoutType; }

    public int getReps() { return reps; }
    public void setReps(int reps) { this.reps = reps; }

    public double getWeightUsed() { return weightUsed; }
    public void setWeightUsed(double weightUsed) { this.weightUsed = weightUsed; }

    public String getMuscleGroup() { return muscleGroup; }
    public void setMuscleGroup(String muscleGroup) { this.muscleGroup = muscleGroup; }
}