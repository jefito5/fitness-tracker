package unitTests;

import impl.MealDB;
import impl.MealLogDB;
import models.DailyMealLog;
import models.Meal;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.*;

// =================================================== Integracinis testas — maisto įvedimas ir dienos log ===================================================
// User Story: Kaip vartotojas noriu patikrinti maisto įvedimo integraciją su trackProgress.
// Priėmimo kriterijai:
//   * Maistas sėkmingai įvedamas su Meal Name, Calories/100g, Weight(g)
//   * Total kcal skaičiuojamas teisingai realiu laiku
//   * Po INSERT MEAL — maistas matomas trackProgress lentelėje šiandien
//   * Tuščių laukų validacija veikia
//   * Neteisingi (neskaitiniai) duomenys — rodoma klaidos žinutė
//
// Integracija apjungia tris sluoksnius:
//   GUI logika (MealIUD#InsertMealListener) → MealDB.insertMeal → MealLogDB.insertDailyLog → MealLogDB.getMealLogsByDate
// (paskutinį naudoja trackProgress, kad parodytų šios dienos eilutes lentelėje).

public class Mealentryintegrationtest {

    private MealDB    mealDB;
    private MealLogDB mealLogDB;
    private final int TEST_USER_ID = 9002;     // Atskiras integracinis vartotojas

    @Before
    public void setUp() {
        mealDB    = new MealDB();
        mealLogDB = new MealLogDB();
    }

    // ----------------------------------------------------------
    // Pagalbinis: imituoja InsertMealListener tikrinimo + skaičiavimo + įrašymo logiką.
    // Grąžina rezultato objektą su klaidos pranešimu (jei buvo) ir įrašytomis reikšmėmis.
    // ----------------------------------------------------------
    private static class MealEntryResult {
        boolean success;
        String  errorMessage;
        double  totalKcal;
        int     mealId;
        int     logId;
    }

    private MealEntryResult submitMealForm(String mealName, String caloriesPer100g, String weightG, int userId) {
        MealEntryResult r = new MealEntryResult();

        // Pirmas validacijos žingsnis (atitinka InsertMealListener)
        if (mealName == null || mealName.isEmpty() || caloriesPer100g == null || caloriesPer100g.isEmpty()) {
            r.errorMessage = "Meal name and calories/100g are required.";
            return r;
        }
        if (weightG == null || weightG.isEmpty()) {
            r.errorMessage = "Please enter weight in grams.";
            return r;
        }

        try {
            double grams       = Double.parseDouble(weightG);
            double kcalPer100g = Double.parseDouble(caloriesPer100g);
            double totalKcal   = kcalPer100g * grams / 100.0;     // realaus laiko skaičiavimo formulė

            Meal m = new Meal();
            m.setMealName(mealName);
            m.setCaloriesPerGram(kcalPer100g);
            int mealId = mealDB.insertMeal(m);

            DailyMealLog dml = new DailyMealLog();
            dml.setMealId(mealId);
            dml.setUserId(userId);
            dml.setTotalCalorieIntake(totalKcal);
            int logId = mealLogDB.insertDailyLog(dml);

            r.success   = true;
            r.totalKcal = totalKcal;
            r.mealId    = mealId;
            r.logId     = logId;
            return r;
        } catch (NumberFormatException ex) {
            r.errorMessage = "Numeric values required for calories, weight, and macros.";
            return r;
        }
    }

    // ----------------------------------------------------------
    // 1. Maistas sėkmingai įvedamas su Meal Name, Calories/100g, Weight(g)
    // ----------------------------------------------------------
    @Test
    public void testMealInsertedSuccessfullyWithValidInput() {
        MealEntryResult r = submitMealForm("IntegTest_Apple", "52", "150", TEST_USER_ID);

        assertTrue("Įvedus pilnus duomenis maistas turi būti įrašytas (success=true)", r.success);
        assertNull("Sėkmingo įrašymo metu klaidos žinutė turi būti null", r.errorMessage);
        assertTrue("Meal turi gauti ID > 0", r.mealId > 0);
        assertTrue("DailyMealLog turi gauti ID > 0", r.logId > 0);
    }

    // ----------------------------------------------------------
    // 2. Total kcal skaičiuojamas teisingai realiu laiku
    // ----------------------------------------------------------
    @Test
    public void testTotalKcalCalculatedCorrectly() {
        // 52 kcal / 100g * 150g = 78 kcal
        MealEntryResult r = submitMealForm("IntegTest_KcalCalc", "52", "150", TEST_USER_ID);

        assertTrue("Įrašymas turi pavykti", r.success);
        assertEquals("Total kcal turi būti apskaičiuotas pagal formulę: kcal/100g * g / 100",
                     78.0, r.totalKcal, 0.001);

        // Patikriname ir su kita verte
        MealEntryResult r2 = submitMealForm("IntegTest_KcalCalc2", "250", "80", TEST_USER_ID);
        assertEquals("250 * 80 / 100 = 200", 200.0, r2.totalKcal, 0.001);

        // Trupmeninės reikšmės
        MealEntryResult r3 = submitMealForm("IntegTest_KcalCalc3", "37.5", "120", TEST_USER_ID);
        assertEquals("37.5 * 120 / 100 = 45.0", 45.0, r3.totalKcal, 0.001);
    }

    // ----------------------------------------------------------
    // 3. Po INSERT MEAL — maistas matomas trackProgress lentelėje šiandien
    //    (trackProgress kviečia MealLogDB.getMealLogsByDate(userId, currentViewDate))
    // ----------------------------------------------------------
    @Test
    public void testMealVisibleInTrackProgressTableToday() {
        String mealName = "IntegTest_Visible_" + System.currentTimeMillis();
        MealEntryResult r = submitMealForm(mealName, "100", "250", TEST_USER_ID);

        assertTrue("Maistas turi būti įrašytas prieš tikrinant matomumą", r.success);

        // Lygiai ta pati užklausa, kurią daro trackProgress.Show_Meals_In_JTable()
        String today = LocalDate.now().toString();
        ArrayList<Object[]> trackProgressRows = mealLogDB.getMealLogsByDate(TEST_USER_ID, today);

        assertNotNull("trackProgress duomenys neturi būti null", trackProgressRows);
        assertFalse("Įdėjus maistą šiandien — trackProgress lentelė neturi būti tuščia", trackProgressRows.isEmpty());

        // Suieškome ką tik įdėtą maistą pagal pavadinimą (struktūra: [mealID, MealName, CaloriePerGram, totalCalorieIntake])
        boolean found = false;
        for (Object[] row : trackProgressRows) {
            if (mealName.equals(row[1])) {
                found = true;
                // 100 kcal/100g * 250g / 100 = 250 kcal
                assertEquals("totalCalorieIntake stulpelis trackProgress lentelėje turi atitikti",
                             250.0, (double) row[3], 0.01);
                assertEquals("CaloriePerGram stulpelis turi atitikti įvestą",
                             100.0, (double) row[2], 0.01);
                break;
            }
        }
        assertTrue("Ką tik įvestas maistas '" + mealName + "' turi būti matomas trackProgress lentelėje šiandienai", found);
    }

    // ----------------------------------------------------------
    // 4. Tuščių laukų validacija veikia
    // ----------------------------------------------------------
    @Test
    public void testEmptyFieldsValidation() {
        // Tuščias pavadinimas
        MealEntryResult r1 = submitMealForm("", "100", "150", TEST_USER_ID);
        assertFalse("Tuščias pavadinimas neturi praeiti", r1.success);
        assertNotNull("Tuščio pavadinimo atveju turi būti klaidos žinutė", r1.errorMessage);
        assertTrue("Klaidos žinutėje turi būti minima 'name'",
                   r1.errorMessage.toLowerCase().contains("name"));

        // Tuščios kalorijos
        MealEntryResult r2 = submitMealForm("SomeName", "", "150", TEST_USER_ID);
        assertFalse("Tuščios kalorijos neturi praeiti", r2.success);
        assertNotNull("Tuščio kalorijų lauko atveju turi būti klaidos žinutė", r2.errorMessage);

        // Tuščias svoris
        MealEntryResult r3 = submitMealForm("SomeName", "100", "", TEST_USER_ID);
        assertFalse("Tuščias svoris neturi praeiti", r3.success);
        assertNotNull("Tuščio svorio atveju turi būti klaidos žinutė", r3.errorMessage);
        assertTrue("Klaidos žinutėje turi būti minima 'weight'",
                   r3.errorMessage.toLowerCase().contains("weight"));

        // Visi tušti
        MealEntryResult r4 = submitMealForm("", "", "", TEST_USER_ID);
        assertFalse("Visi tušti laukai neturi praeiti", r4.success);
        assertNotNull("Tuščių laukų atveju turi būti klaidos žinutė", r4.errorMessage);
    }

    // ----------------------------------------------------------
    // 5. Neteisingi (neskaitiniai) duomenys — rodoma klaidos žinutė
    // ----------------------------------------------------------
    @Test
    public void testNonNumericInputShowsErrorMessage() {
        // Neskaitinės kalorijos
        MealEntryResult r1 = submitMealForm("ValidName", "abc", "150", TEST_USER_ID);
        assertFalse("Tekstas kalorijų lauke neturi praeiti", r1.success);
        assertNotNull("Neteisingi duomenys turi grąžinti klaidos žinutę", r1.errorMessage);
        assertTrue("Klaidos žinutėje turi būti minima 'numeric' arba skaičius",
                   r1.errorMessage.toLowerCase().contains("numeric"));

        // Neskaitinis svoris
        MealEntryResult r2 = submitMealForm("ValidName", "100", "heavy", TEST_USER_ID);
        assertFalse("Tekstas svorio lauke neturi praeiti", r2.success);
        assertNotNull("Neteisingi duomenys turi grąžinti klaidos žinutę", r2.errorMessage);

        // Specialūs simboliai
        MealEntryResult r3 = submitMealForm("ValidName", "12$%", "150", TEST_USER_ID);
        assertFalse("Specialūs simboliai neturi praeiti", r3.success);
        assertNotNull("Specialių simbolių atveju turi būti klaidos žinutė", r3.errorMessage);
    }

    // ----------------------------------------------------------
    // 6. Papildomai: po sėkmingo įvedimo getTodayCalories sumuoja teisingai
    //    (užtikrina, kad realaus laiko skaičiavimas atsispindi suvestinėje)
    // ----------------------------------------------------------
    @Test
    public void testTodayCaloriesSumReflectsInsertedMeal() {
        double caloriesBefore = mealLogDB.getTodayCalories(TEST_USER_ID);

        MealEntryResult r = submitMealForm("IntegTest_Sum_" + System.currentTimeMillis(),
                                            "200", "100", TEST_USER_ID);   // 200 kcal
        assertTrue("Maistas turi būti įrašytas", r.success);

        double caloriesAfter = mealLogDB.getTodayCalories(TEST_USER_ID);

        assertEquals("Šiandienos kalorijų suma turi padidėti tiksliai apskaičiuota verte",
                     caloriesBefore + 200.0, caloriesAfter, 0.01);
    }
}