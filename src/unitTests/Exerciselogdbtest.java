package unitTests;

import impl.ExerciseDB;
import impl.ExerciseLogDB;
import models.DailyExerciseLog;
import models.Exercise;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.*;

// ========================================================== ExerciseLogDB unit testai =======================================================
// User Story: Vieneto testai — ExerciseLogDB klasė
// Priėmimo kriterijai:
//   * testInsertExerciseLog()      — patikrina pratimo log įrašymą
//   * testGetExerciseLogsByDate()  — patikrina filtravimą pagal datą
//   * testDeleteExerciseLog()      — patikrina ištrynimą

public class Exerciselogdbtest {

    private ExerciseLogDB exerciseLogDB;
    private ExerciseDB    exerciseDB;
    private final int TEST_USER_ID = 9001;       // Atskiras testinis vartotojas, kad nesimaišytų su realiais duomenimis
    private int testExerciseId;                  // Pratimas, kurį naudoja visi testai (FOREIGN KEY į exercise lentelę)

    // Pagalbinis metodas: sukuria testinį pratimą realioje exercise lentelėje
    private Exercise createTestExercise() {
        Exercise e = new Exercise();
        e.setExerciseName("TEST_Exercise_" + System.currentTimeMillis());
        e.setCalorieburn(10.0);
        e.setWorkoutType("Cardio");
        e.setReps(0);
        e.setWeightUsed(0);
        e.setMuscleGroup("General");
        return e;
    }

    // Pagalbinis metodas: sukuria testinį DailyExerciseLog objektą
    private DailyExerciseLog createTestLog(int exerciseId, double calories, double duration) {
        DailyExerciseLog log = new DailyExerciseLog();
        log.setUserId(TEST_USER_ID);
        log.setExerciseId(exerciseId);
        log.setTotalCalorieBurn(calories);
        log.setDurationMinutes(duration);
        return log;
    }

    @Before
    public void setUp() {
        // Kriterijus: ExerciseLogDB ir ExerciseDB inicializuojami prieš kiekvieną testą
        exerciseLogDB = new ExerciseLogDB();
        exerciseDB    = new ExerciseDB();

        // Užtikriname, kad durationMinutes stulpelis egzistuoja
        exerciseLogDB.ensureDurationColumn();

        // Pridedame testinį pratimą, į kurį nukreips visi log įrašai
        testExerciseId = exerciseDB.insertExercise(createTestExercise());
        assertTrue("Setup: testinis pratimas turi būti įrašytas su ID > 0", testExerciseId > 0);
    }

    // ----------------------------------------------------------
    // testInsertExerciseLog — patikrina pratimo log įrašymą
    // ----------------------------------------------------------
    @Test
    public void testInsertExerciseLog() {
        // Kriterijus: insertDailyLog() turi grąžinti generuotą ID > 0
        DailyExerciseLog log = createTestLog(testExerciseId, 250.0, 30.0);

        int insertedId = exerciseLogDB.insertDailyLog(log);

        assertTrue("Pratimo log įrašymas turi grąžinti ID > 0", insertedId > 0);

        // Papildoma: po įrašymo įrašas turi atsirasti šios dienos sąraše
        ArrayList<Object[]> todayLogs = exerciseLogDB.getTodayLogs(TEST_USER_ID);
        assertNotNull("Šiandienos logų sąrašas negali būti null", todayLogs);
        assertFalse("Po įrašymo šiandienos sąrašas neturi būti tuščias", todayLogs.isEmpty());

        // Surandame ką tik įrašytą eilutę pagal ID
        boolean found = false;
        for (Object[] row : todayLogs) {
            if ((int) row[0] == insertedId) {
                found = true;
                assertEquals("totalCalorieBurn turi sutapti", 250.0, (double) row[3], 0.01);
                assertEquals("durationMinutes turi sutapti", 30.0, (double) row[6], 0.01);
                break;
            }
        }
        assertTrue("Naujai įrašytas log turi būti randamas šiandienos sąraše", found);
    }

    // ----------------------------------------------------------
    // testGetExerciseLogsByDate — patikrina filtravimą pagal datą
    // ----------------------------------------------------------
    @Test
    public void testGetExerciseLogsByDate() {
        // Žingsnis 1: įrašome log šiandienai
        DailyExerciseLog log = createTestLog(testExerciseId, 180.5, 20.0);
        int insertedId = exerciseLogDB.insertDailyLog(log);
        assertTrue("Prieš filtravimo testą įrašas turi būti įdėtas", insertedId > 0);

        String today    = LocalDate.now().toString();
        String yesterday= LocalDate.now().minusDays(1).toString();

        // Kriterijus: getLogsByDate(today) turi rasti šiandienos įrašą
        ArrayList<Object[]> todayResults = exerciseLogDB.getLogsByDate(TEST_USER_ID, today);
        assertNotNull("Šiandienos rezultatas neturi būti null", todayResults);
        assertFalse("Šiandienai turi būti bent vienas įrašas", todayResults.isEmpty());

        boolean foundToday = false;
        for (Object[] row : todayResults) {
            if ((int) row[0] == insertedId) {
                foundToday = true;
                break;
            }
        }
        assertTrue("Įrašytas log turi būti randamas filtruojant pagal šiandienos datą", foundToday);

        // Kriterijus: getLogsByDate(yesterday) NETURI grąžinti šiandienos įrašo
        ArrayList<Object[]> yesterdayResults = exerciseLogDB.getLogsByDate(TEST_USER_ID, yesterday);
        assertNotNull("Vakar dienos rezultatas neturi būti null", yesterdayResults);

        boolean foundInYesterday = false;
        for (Object[] row : yesterdayResults) {
            if ((int) row[0] == insertedId) {
                foundInYesterday = true;
                break;
            }
        }
        assertFalse("Šiandien įrašytas log NETURI būti randamas vakar dienos filtre", foundInYesterday);

        // Kriterijus: filtravimas pagal userId taip pat veikia (kitas userId — nieko nerandama)
        ArrayList<Object[]> otherUserResults = exerciseLogDB.getLogsByDate(TEST_USER_ID + 999, today);
        assertNotNull("Kito vartotojo sąrašas neturi būti null", otherUserResults);

        for (Object[] row : otherUserResults) {
            assertNotEquals("Kito vartotojo užklausoje neturi būti mūsų log", insertedId, (int) row[0]);
        }
    }

    // ----------------------------------------------------------
    // testDeleteExerciseLog — patikrina ištrynimą
    // ----------------------------------------------------------
    @Test
    public void testDeleteExerciseLog() {
        // Žingsnis 1: įrašome log, kurį vėliau ištrinsime
        DailyExerciseLog log = createTestLog(testExerciseId, 99.9, 15.0);
        int insertedId = exerciseLogDB.insertDailyLog(log);
        assertTrue("Prieš trynimo testą įrašas turi būti įdėtas", insertedId > 0);

        // Žingsnis 2: ištriname log
        int deletedRows = exerciseLogDB.deleteTodayLog(insertedId);

        // Kriterijus: deleteTodayLog() turi grąžinti 1 (ištrintų eilučių skaičius)
        assertEquals("deleteTodayLog() turi grąžinti 1 ištrintą eilutę", 1, deletedRows);

        // Kriterijus: po trynimo įrašo neturi būti randamame sąraše
        ArrayList<Object[]> todayLogs = exerciseLogDB.getTodayLogs(TEST_USER_ID);
        boolean stillExists = false;
        for (Object[] row : todayLogs) {
            if ((int) row[0] == insertedId) {
                stillExists = true;
                break;
            }
        }
        assertFalse("Ištrintas log neturi būti randamas po trynimo", stillExists);

        // Papildoma: pakartotinis trynimas tos pačios eilutės grąžina 0 (nieko neištrina)
        int secondDelete = exerciseLogDB.deleteTodayLog(insertedId);
        assertEquals("Pakartotinis tos pačios eilutės trynimas turi grąžinti 0", 0, secondDelete);
    }

    // ----------------------------------------------------------
    // Papildomas: testGetAllLogDates — patikrina datų sąrašo gavimą
    // ----------------------------------------------------------
    @Test
    public void testGetAllLogDates() {
        // Įrašome bent vieną log šiandienai
        DailyExerciseLog log = createTestLog(testExerciseId, 50.0, 10.0);
        int insertedId = exerciseLogDB.insertDailyLog(log);
        assertTrue("Įrašas turi būti įdėtas", insertedId > 0);

        // Kriterijus: getAllLogDates() turi grąžinti ne-null sąrašą, kuriame yra bent šiandiena
        ArrayList<String> dates = exerciseLogDB.getAllLogDates(TEST_USER_ID);
        assertNotNull("Datų sąrašas negali būti null", dates);
        assertFalse("Datų sąrašas neturi būti tuščias kai yra įrašų", dates.isEmpty());
        assertTrue("Šiandienos data turi būti datų sąraše", dates.contains(LocalDate.now().toString()));
    }
}