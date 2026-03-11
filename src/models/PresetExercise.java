package models;

/**
 * Represents a preset exercise from the preset_exercises table.
 * Strength exercises have muscleGroup set.
 * Cardio exercises have met (Metabolic Equivalent) set for calorie calculation.
 */
public class PresetExercise {
    private int id;
    private String exerciseName;
    private String workoutType;   // "Cardio" or "Strength"
    private String muscleGroup;   // Strength only (e.g. "Chest", "Legs")
    private double met;           // Cardio only — used for: calories = MET * weightKg * hours

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getExerciseName() { return exerciseName; }
    public void setExerciseName(String name) { this.exerciseName = name; }

    public String getWorkoutType() { return workoutType; }
    public void setWorkoutType(String workoutType) { this.workoutType = workoutType; }

    public String getMuscleGroup() { return muscleGroup; }
    public void setMuscleGroup(String muscleGroup) { this.muscleGroup = muscleGroup; }

    public double getMet() { return met; }
    public void setMet(double met) { this.met = met; }

    @Override
    public String toString() { return exerciseName; } // for JComboBox display
}
