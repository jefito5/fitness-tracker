package unitTests;

import impl.ExerciseDB;
import models.Exercise;
import database.ConnectionFactory;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Vieneto testai — ExerciseDB klasė
 *
 * Priėmimo kriterijai:
 *  - testInsertExercise()  : patikrina įrašymą su workoutType, reps, weightUsed laukais
 *  - testGetExerciseById() : patikrina gavimą pagal ID
 *  - testUpdateExercise()  : patikrina atnaujinimą
 *  - testDeleteExercise()  : patikrina ištrynimą
 */
public class ExerciseDBTest {

    private ExerciseDB exerciseDB;

    // Kaupiame įterptų testinių įrašų ID sąrašą, kad galėtume išvalyti po testų
    private final java.util.List<Integer> insertedIds = new ArrayList<>();

    // ── Pagalbinis metodas: sukuria testinį Exercise objektą ──────────────────
    private Exercise buildTestExercise(String name) {
        Exercise e = new Exercise();
        e.setExerciseName(name);
        e.setCalorieburn(8.5);
        e.setWorkoutType("Strength");   // <-- priėmimo kriterijus: workoutType
        e.setReps(12);                  // <-- priėmimo kriterijus: reps
        e.setWeightUsed(70.0);         // <-- priėmimo kriterijus: weightUsed
        e.setMuscleGroup("Chest");
        return e;
    }

    // ── Pagalbinis metodas: tiesiogiai ištrina iš DB pagal ID ────────────────
    private void deleteById(int id) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM exercise WHERE id=?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Before
    public void setUp() {
        exerciseDB = new ExerciseDB();
        insertedIds.clear();
    }

    @After
    public void tearDown() {
        // Valome testiniais duomenimis sukurtus įrašus, kad nekaupiami šiukšliniai DB
        for (int id : insertedIds) {
            deleteById(id);
        }
        insertedIds.clear();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  testInsertExercise
    //  Kriterijus: sėkmingas įrašymas su workoutType, reps, weightUsed laukais
    // ══════════════════════════════════════════════════════════════════════════
    @Test
    public void testInsertExercise() {
        Exercise e = buildTestExercise("Push-Up Test");

        int generatedId = exerciseDB.insertExercise(e);
        insertedIds.add(generatedId); // registruojame valymui

        // Turi grąžinti galiojantį ID
        assertTrue("insertExercise() turi grąžinti ID > 0", generatedId > 0);

        // Tikriname, ar įrašyti laukai atspindimi DB
        Exercise saved = exerciseDB.getById(generatedId);
        assertNotNull("Gautas pratimas neturi būti null", saved);
        assertEquals("ExerciseName turi sutapti",  "Push-Up Test", saved.getExerciseName());
        assertEquals("workoutType turi sutapti",   "Strength",     saved.getWorkoutType());
        assertEquals("reps turi sutapti",          12,             saved.getReps());
        assertEquals("weightUsed turi sutapti",    70.0,           saved.getWeightUsed(), 0.001);
        assertEquals("CalorieburnPerMin turi sutapti", 8.5,        saved.getCalorieburn(), 0.001);
        assertEquals("muscleGroup turi sutapti",   "Chest",        saved.getMuscleGroup());
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  testGetExerciseById
    //  Kriterijus: getById() grąžina teisingą objektą pagal ID
    // ══════════════════════════════════════════════════════════════════════════
    @Test
    public void testGetExerciseById() {
        Exercise e = buildTestExercise("Squat Test");
        int id = exerciseDB.insertExercise(e);
        insertedIds.add(id);

        Exercise fetched = exerciseDB.getById(id);

        assertNotNull("getById() neturi grąžinti null esamam ID", fetched);
        assertEquals("ID turi sutapti",           id,            fetched.getId());
        assertEquals("ExerciseName turi sutapti", "Squat Test",  fetched.getExerciseName());
        assertEquals("workoutType turi sutapti",  "Strength",    fetched.getWorkoutType());
        assertEquals("reps turi sutapti",         12,            fetched.getReps());
        assertEquals("weightUsed turi sutapti",   70.0,          fetched.getWeightUsed(), 0.001);
    }

    @Test
    public void testGetExerciseByIdReturnsEmptyForUnknownId() {
        // Neegzistuojantis ID — tikimės tuščio (default) objekto, ne išimties
        Exercise result = exerciseDB.getById(Integer.MAX_VALUE);
        assertNotNull("getById() neturi mesti išimties nežinomam ID", result);
        assertEquals("Nežinomo ID atveju ID turi būti 0", 0, result.getId());
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  testUpdateExercise
    //  Kriterijus: atnaujinti duomenys išlieka DB
    // ══════════════════════════════════════════════════════════════════════════
    @Test
    public void testUpdateExercise() {
        // 1. Pirmiau įterpiame pradinį įrašą
        Exercise e = buildTestExercise("Deadlift Test");
        int id = exerciseDB.insertExercise(e);
        insertedIds.add(id);

        // 2. Keičiame laukus
        e.setId(id);
        e.setExerciseName("Deadlift Test UPDATED");
        e.setWorkoutType("Cardio");
        e.setReps(20);
        e.setWeightUsed(0.0);
        e.setCalorieburn(12.0);
        e.setMuscleGroup("Back");

        int rows = exerciseDB.updateExercise(e);
        assertEquals("updateExercise() turi grąžinti 1 (vienas atnaujintas eilutė)", 1, rows);

        // 3. Perskaitome ir tikriname
        Exercise updated = exerciseDB.getById(id);
        assertNotNull("Atnaujintas įrašas neturi būti null", updated);
        assertEquals("ExerciseName turi atsinaujinti",  "Deadlift Test UPDATED", updated.getExerciseName());
        assertEquals("workoutType turi atsinaujinti",   "Cardio",                updated.getWorkoutType());
        assertEquals("reps turi atsinaujinti",          20,                      updated.getReps());
        assertEquals("weightUsed turi atsinaujinti",    0.0,                     updated.getWeightUsed(), 0.001);
        assertEquals("muscleGroup turi atsinaujinti",   "Back",                  updated.getMuscleGroup());
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  testDeleteExercise
    //  Kriterijus: įrašas pašalinamas iš DB
    // ══════════════════════════════════════════════════════════════════════════
    @Test
    public void testDeleteExercise() {
        Exercise e = buildTestExercise("Bench Press Test");
        int id = exerciseDB.insertExercise(e);
        // Neregistruojame insertedIds — ištrinsime patys testo viduje

        e.setId(id);
        int rows = exerciseDB.deleteExercise(e);
        assertEquals("deleteExercise() turi grąžinti 1 (viena ištrinta eilutė)", 1, rows);

        // Po ištrynimo getById() turi grąžinti tuščią objektą (id == 0)
        Exercise deleted = exerciseDB.getById(id);
        assertNotNull("getById() neturi mesti išimties po ištrynimo", deleted);
        assertEquals("Ištrynus įrašą getById() turi grąžinti id == 0", 0, deleted.getId());
    }
}