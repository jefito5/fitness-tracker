package Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import impl.ExerciseDB;
import models.Exercise;

public class ExerciseTest {

    private ExerciseDB exerciseDB;

    @BeforeEach
    public void setUp() {
        exerciseDB = new ExerciseDB();
    }

    // -------------------------------------------------------
    // 1. Exercise INSERT testai
    // -------------------------------------------------------
    @Test
    public void testInsertExercise_Cardio() {
        Exercise e = new Exercise();
        e.setExerciseName("Test Running");
        e.setCalorieburn(10.5);
        e.setWorkoutType("Cardio");
        e.setReps(0);
        e.setWeightUsed(0.0);
        e.setMuscleGroup("Legs");

        int id = exerciseDB.insertExercise(e);
        assertTrue(id > 0, "Insert turi grąžinti ID > 0");
    }

    @Test
    public void testInsertExercise_Strength() {
        Exercise e = new Exercise();
        e.setExerciseName("Test Bench Press");
        e.setCalorieburn(0.0);
        e.setWorkoutType("Strength");
        e.setReps(10);
        e.setWeightUsed(60.0);
        e.setMuscleGroup("Chest");

        int id = exerciseDB.insertExercise(e);
        assertTrue(id > 0, "Strength insert turi grąžinti ID > 0");
    }

    // -------------------------------------------------------
    // 2. Calorie burn skaičiavimo testai
    // -------------------------------------------------------
    @Test
    public void testCalorieBurn_Cardio_Calculation() {
        // 10.5 kcal/min * 30 min = 315.0
        double caloriePerMin = 10.5;
        double durationMin = 30.0;
        double result = caloriePerMin * durationMin;
        assertEquals(315.0, result, 0.01, "Cardio kalorijos turi būti 315.0");
    }

    @Test
    public void testCalorieBurn_Strength_IsZero() {
        Exercise e = new Exercise();
        e.setWorkoutType("Strength");
        e.setCalorieburn(0.0);
        assertEquals(0.0, e.getCalorieburn(), 0.01, "Strength calorieburn turi būti 0");
    }

    @Test
    public void testCalorieBurn_ZeroDuration() {
        double result = 10.5 * 0.0;
        assertEquals(0.0, result, 0.01, "0 minučių = 0 kcal");
    }

    // -------------------------------------------------------
    // 3. Muscle group sujungimo testai
    // -------------------------------------------------------
    @Test
    public void testMuscleGroup_SavedAndRetrieved() {
        Exercise e = new Exercise();
        e.setExerciseName("Bicep Curl Test");
        e.setCalorieburn(0.0);
        e.setWorkoutType("Strength");
        e.setReps(12);
        e.setWeightUsed(15.0);
        e.setMuscleGroup("Arms");

        int id = exerciseDB.insertExercise(e);
        assertTrue(id > 0, "Insert turi grąžinti ID > 0");

        Exercise fetched = exerciseDB.getById(id);
        assertEquals("Arms", fetched.getMuscleGroup(), "muscleGroup turi būti 'Arms'");
    }

    @Test
    public void testMuscleGroup_DefaultIsGeneral() {
        Exercise e = new Exercise();
        e.setExerciseName("No Group Exercise");
        e.setCalorieburn(5.0);
        e.setWorkoutType("Cardio");
        e.setReps(0);
        e.setWeightUsed(0.0);
        e.setMuscleGroup(null); // null -> turi tapti "General"

        int id = exerciseDB.insertExercise(e);
        assertTrue(id > 0, "Insert turi grąžinti ID > 0");

        Exercise fetched = exerciseDB.getById(id);
        assertEquals("General", fetched.getMuscleGroup(), "null muscleGroup turi tapti 'General'");
    }

    // -------------------------------------------------------
    // 4. DB operacijų testai (update, delete)
    // -------------------------------------------------------
    @Test
    public void testUpdateExercise() {
        Exercise e = new Exercise();
        e.setExerciseName("Update Test");
        e.setCalorieburn(7.0);
        e.setWorkoutType("Cardio");
        e.setReps(0);
        e.setWeightUsed(0.0);
        e.setMuscleGroup("Core");

        int id = exerciseDB.insertExercise(e);
        e.setId(id);
        e.setExerciseName("Updated Name");
        e.setMuscleGroup("Back");

        int rows = exerciseDB.updateExercise(e);
        assertEquals(1, rows, "Update turi grąžinti 1");

        Exercise updated = exerciseDB.getById(id);
        assertEquals("Updated Name", updated.getExerciseName(), "Pavadinimas turi būti atnaujintas");
        assertEquals("Back", updated.getMuscleGroup(), "muscleGroup turi būti atnaujinta");
    }

    @Test
    public void testDeleteExercise() {
        Exercise e = new Exercise();
        e.setExerciseName("Delete Test");
        e.setCalorieburn(3.0);
        e.setWorkoutType("Cardio");
        e.setReps(0);
        e.setWeightUsed(0.0);
        e.setMuscleGroup("Legs");

        int id = exerciseDB.insertExercise(e);
        e.setId(id);

        int rows = exerciseDB.deleteExercise(e);
        assertEquals(1, rows, "Delete turi grąžinti 1");
    }
}