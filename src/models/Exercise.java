package models;

public class Exercise {
    private int id;
    private String exerciseName;
    private double calorieburn;        // CalorieburnPerMin (for cardio = MET based, for strength = 0)
    private String workoutType;        // "Cardio" or "Strength"
    private int reps;                  // Strength only
    private double weightUsed;         // Strength only (kg)
    private String muscleGroup;    // Primary muscle group targeted (e.g., "Chest", "Back", "Legs", "Arms", "Shoulders", "Core")

    public String getMuscleGroup() { return muscleGroup; }
    public void setMuscleGroup(String muscleGroup) { this.muscleGroup = muscleGroup; }

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
}
