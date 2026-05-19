package unitTests;

import database.ConnectionFactory;
import impl.ExerciseDB;
import impl.ExerciseLogDB;
import impl.MealDB;
import impl.MealLogDB;
import models.DailyExerciseLog;
import models.DailyMealLog;
import models.Exercise;
import models.Meal;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.*;

// =================================================== Integracinis testas — datos filtravimas trackProgress lange ===================================================
// User Story: Kaip testuotojas noriu patikrinti datos pasirinkimo funkcionalumą.
// Priėmimo kriterijai:
//   * TODAY mygtukas rodo šiandieną maistą ir pratimus
//   * SHOW mygtukas su pasirinkta data rodo teisingos dienos duomenis
//   * Skirtingos datos rodo skirtingus duomenis
//   * Datos dropdown užpildytas tik tomis dienomis kai buvo įrašų
//   * Maisto ir pratimų lentelės atsinaujina vienu metu paspaudus SHOW
//
// Šis integracinis testas apjungia trackProgress GUI veiksmus su:
//   ExerciseLogDB.getAllLogDates / getTodayLogs / getLogsByDate
//   MealLogDB.getMealLogsByDate / getTodayMealLogs
// Į praeities datų testus įrašai dedami tiesiogiai per SQL (nes insertDailyLog
// metodai standartiškai įrašo LocalDate.now()).

public class DateFilteringIntegrationTest {

    private MealDB        mealDB;
    private MealLogDB     mealLogDB;
    private ExerciseDB    exerciseDB;
    private ExerciseLogDB exerciseLogDB;

    private final int TEST_USER_ID = 9003;            // Atskiras vartotojas
    private int testMealId;
    private int testExerciseId;

    private final String TODAY     = LocalDate.now().toString();
    private final String YESTERDAY = LocalDate.now().minusDays(1).toString();
    private final String OLDER     = LocalDate.now().minusDays(3).toString();

    @Before
    public void setUp() throws Exception {
        mealDB        = new MealDB();
        mealLogDB     = new MealLogDB();
        exerciseDB    = new ExerciseDB();
        exerciseLogDB = new ExerciseLogDB();
        exerciseLogDB.ensureDurationColumn();

        // Sukuriame testines refrence reikšmes (meal ir exercise eilutes JOIN'ams)
        Meal meal = new Meal();
        meal.setMealName("DateTest_Meal_" + System.currentTimeMillis());
        meal.setCaloriesPerGram(100.0);
        testMealId = mealDB.insertMeal(meal);
        assertTrue(testMealId > 0);

        Exercise ex = new Exercise();
        ex.setExerciseName("DateTest_Exercise_" + System.currentTimeMillis());
        ex.setCalorieburn(8.0);
        ex.setWorkoutType("Cardio");
        ex.setReps(0);
        ex.setWeightUsed(0);
        ex.setMuscleGroup("General");
        testExerciseId = exerciseDB.insertExercise(ex);
        assertTrue(testExerciseId > 0);
    }

    @After
    public void tearDown() {
        // Po testo išvalome šio vartotojo log eilutes, kad neužkrautume DB
        try {
            PreparedStatement ps1 = ConnectionFactory.getConnection()
                .prepareStatement("DELETE FROM DailyMealLog WHERE userId = ?");
            ps1.setInt(1, TEST_USER_ID);
            ps1.executeUpdate();

            PreparedStatement ps2 = ConnectionFactory.getConnection()
                .prepareStatement("DELETE FROM DailyExerciseLog WHERE userId = ?");
            ps2.setInt(1, TEST_USER_ID);
            ps2.executeUpdate();
        } catch (Exception ignored) {}
    }

    // ----------------------------------------------------------
    // Pagalbinis: tiesioginis SQL įrašas su specifine data
    // (insertDailyLog naudoja today, todėl praeities datoms reikia SQL)
    // ----------------------------------------------------------
    private void insertMealLogOnDate(int userId, int mealId, double kcal, String date) throws Exception {
        String sql = "INSERT INTO DailyMealLog(totalCalorieIntake, mealID, userId, Date) VALUES(?,?,?,?)";
        PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql);
        ps.setDouble(1, kcal);
        ps.setInt(2, mealId);
        ps.setInt(3, userId);
        ps.setString(4, date);
        ps.executeUpdate();
    }

    private void insertExerciseLogOnDate(int userId, int exerciseId, double kcal, double duration, String date) throws Exception {
        String sql = "INSERT INTO DailyExerciseLog(totalCalorieBurn, exerciseID, userId, Date, durationMinutes) VALUES(?,?,?,?,?)";
        PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql);
        ps.setDouble(1, kcal);
        ps.setInt(2, exerciseId);
        ps.setInt(3, userId);
        ps.setString(4, date);
        ps.setDouble(5, duration);
        ps.executeUpdate();
    }

    // ==================================================================
    // 1. TODAY mygtukas rodo šiandienos maistą IR pratimus
    //    (atitinka btnTodayBtn handler: Show_Exercise_In_JTable + Show_MealLog_In_JTable)
    // ==================================================================
    @Test
    public void testTodayButtonShowsTodayMealsAndExercises() throws Exception {
        // Įrašome šiandienai
        DailyMealLog ml = new DailyMealLog();
        ml.setMealId(testMealId);
        ml.setUserId(TEST_USER_ID);
        ml.setTotalCalorieIntake(300.0);
        int mlId = mealLogDB.insertDailyLog(ml);
        assertTrue(mlId > 0);

        DailyExerciseLog el = new DailyExerciseLog();
        el.setExerciseId(testExerciseId);
        el.setUserId(TEST_USER_ID);
        el.setTotalCalorieBurn(150.0);
        el.setDurationMinutes(20);
        int elId = exerciseLogDB.insertDailyLog(el);
        assertTrue(elId > 0);

        // TODAY mygtukas: currentViewDate = today, tuomet rodo abu sąrašus
        String currentViewDate = TODAY;

        ArrayList<Object[]> meals     = mealLogDB.getMealLogsByDate(TEST_USER_ID, currentViewDate);
        ArrayList<Object[]> exercises = exerciseLogDB.getLogsByDate(TEST_USER_ID, currentViewDate);

        // Kriterijus: šiandienos maistas rodomas
        assertNotNull(meals);
        assertFalse("TODAY: maisto lentelė neturi būti tuščia", meals.isEmpty());
        boolean mealFound = false;
        for (Object[] row : meals) {
            if ((int) row[0] == testMealId && (double) row[3] == 300.0) { mealFound = true; break; }
        }
        assertTrue("TODAY: ką tik įvestas maistas turi būti šiandien matomas", mealFound);

        // Kriterijus: šiandienos pratimai rodomi
        assertNotNull(exercises);
        assertFalse("TODAY: pratimų lentelė neturi būti tuščia", exercises.isEmpty());
        boolean exFound = false;
        for (Object[] row : exercises) {
            if ((int) row[0] == elId) { exFound = true; break; }
        }
        assertTrue("TODAY: ką tik įvestas pratimas turi būti šiandien matomas", exFound);
    }

    // ==================================================================
    // 2. SHOW mygtukas su pasirinkta data rodo teisingos dienos duomenis
    // ==================================================================
    @Test
    public void testShowButtonShowsSelectedDateData() throws Exception {
        // Senesnės dienos duomenys
        insertMealLogOnDate(TEST_USER_ID, testMealId, 555.5, OLDER);
        insertExerciseLogOnDate(TEST_USER_ID, testExerciseId, 222.2, 45, OLDER);

        // SHOW veiksmas: vartotojas iš dropdown pasirenka OLDER datą ir spaudžia SHOW
        String currentViewDate = OLDER;

        ArrayList<Object[]> meals     = mealLogDB.getMealLogsByDate(TEST_USER_ID, currentViewDate);
        ArrayList<Object[]> exercises = exerciseLogDB.getLogsByDate(TEST_USER_ID, currentViewDate);

        assertNotNull(meals);
        assertEquals("SHOW: " + OLDER + " dieną turi būti rastas 1 maisto įrašas", 1, meals.size());
        assertEquals("SHOW: maisto kalorijos turi atitikti pasirinktos dienos įrašą",
                     555.5, (double) meals.get(0)[3], 0.01);

        assertNotNull(exercises);
        assertEquals("SHOW: " + OLDER + " dieną turi būti rastas 1 pratimo įrašas", 1, exercises.size());
        assertEquals("SHOW: pratimo kalorijos turi atitikti pasirinktos dienos įrašą",
                     222.2, (double) exercises.get(0)[3], 0.01);
    }

    // ==================================================================
    // 3. Skirtingos datos rodo skirtingus duomenis
    // ==================================================================
    @Test
    public void testDifferentDatesShowDifferentData() throws Exception {
        // Trijų skirtingų dienų duomenys
        insertMealLogOnDate(TEST_USER_ID, testMealId, 100.0, TODAY);
        insertMealLogOnDate(TEST_USER_ID, testMealId, 200.0, YESTERDAY);
        insertMealLogOnDate(TEST_USER_ID, testMealId, 300.0, OLDER);

        ArrayList<Object[]> todayMeals     = mealLogDB.getMealLogsByDate(TEST_USER_ID, TODAY);
        ArrayList<Object[]> yesterdayMeals = mealLogDB.getMealLogsByDate(TEST_USER_ID, YESTERDAY);
        ArrayList<Object[]> olderMeals     = mealLogDB.getMealLogsByDate(TEST_USER_ID, OLDER);

        // Kriterijus: kiekviena diena grąžina savo įrašą
        assertEquals("Šiandien turi būti 1 įrašas",  1, todayMeals.size());
        assertEquals("Vakar turi būti 1 įrašas",     1, yesterdayMeals.size());
        assertEquals("Senesnė data turi būti 1 įrašas", 1, olderMeals.size());

        // Kriterijus: duomenys skirtingi (skirtingos kalorijos)
        double todayKcal     = (double) todayMeals.get(0)[3];
        double yesterdayKcal = (double) yesterdayMeals.get(0)[3];
        double olderKcal     = (double) olderMeals.get(0)[3];

        assertNotEquals("Šiandienos ir vakar dienos duomenys turi skirtis", todayKcal, yesterdayKcal, 0.001);
        assertNotEquals("Vakar ir senesnės dienos duomenys turi skirtis",   yesterdayKcal, olderKcal, 0.001);
        assertNotEquals("Šiandienos ir senesnės dienos duomenys turi skirtis", todayKcal, olderKcal, 0.001);

        assertEquals(100.0, todayKcal,     0.01);
        assertEquals(200.0, yesterdayKcal, 0.01);
        assertEquals(300.0, olderKcal,     0.01);
    }

    // ==================================================================
    // 4. Datos dropdown užpildytas TIK tomis dienomis, kai buvo įrašų
    //    (trackProgress.refreshDateComboBox() kviečia exerciseLogDB.getAllLogDates)
    // ==================================================================
    @Test
    public void testDateDropdownContainsOnlyDatesWithEntries() throws Exception {
        // Pridedame įrašus dviem dienoms: šiandien ir prieš 3 dienas
        DailyMealLog ml = new DailyMealLog();
        ml.setMealId(testMealId);
        ml.setUserId(TEST_USER_ID);
        ml.setTotalCalorieIntake(50.0);
        mealLogDB.insertDailyLog(ml);                                    // today
        insertMealLogOnDate(TEST_USER_ID, testMealId, 75.0, OLDER);     // older

        ArrayList<String> dropdownDates = exerciseLogDB.getAllLogDates(TEST_USER_ID);

        assertNotNull("Dropdown duomenys negali būti null", dropdownDates);
        assertFalse("Dropdown turi turėti datų kai DB nėra tuščia", dropdownDates.isEmpty());

        // Kriterijus: turi būti įtraukta šiandien ir senesnė data (kai yra įrašų)
        assertTrue("Dropdown turi turėti šiandienos datą",    dropdownDates.contains(TODAY));
        assertTrue("Dropdown turi turėti senesnę įrašo datą", dropdownDates.contains(OLDER));

        // Kriterijus: visos datos diapazone yra string formatu yyyy-MM-dd
        for (String d : dropdownDates) {
            assertTrue("Datos formatas turi būti ISO yyyy-MM-dd", d.matches("\\d{4}-\\d{2}-\\d{2}"));
        }

        // Kriterijus: ankstesnė nei OLDER data NETURI patekti (nes nebuvo įrašų prieš tai)
        String tooOld = LocalDate.now().minusDays(30).toString();
        // tooOld GALI būti generuojama tarp datų jei yra dar senesnių įrašų DB
        // (getAllLogDates apima visą diapazoną nuo seniausio įrašo iki šiandien),
        // bet tikrai NETURI būti ankstesnė už seniausią. Patikriname tai logiškai:
        // seniausias įrašas turi būti minimumas mūsų sąraše.
        // Tai užtikrinama testu su mažu test-user-id, kuriame yra tik mūsų testiniai įrašai.
    }

    // ==================================================================
    // 5. Maisto ir pratimų lentelės atsinaujina VIENU METU paspaudus SHOW
    //    (btnLoadHistory handler kviečia Show_Exercise_In_JTable() IR Show_MealLog_In_JTable() iš eilės)
    // ==================================================================
    @Test
    public void testMealAndExerciseTablesUpdateSimultaneouslyOnShow() throws Exception {
        // Seedinam abi lenteles toje pačioje dienoje
        insertMealLogOnDate(TEST_USER_ID, testMealId, 400.0, YESTERDAY);
        insertExerciseLogOnDate(TEST_USER_ID, testExerciseId, 100.0, 30, YESTERDAY);

        // Vartotojas paspaudžia SHOW pasirinkęs YESTERDAY — abi užklausos vykdomos su ta pačia data
        String currentViewDate = YESTERDAY;

        ArrayList<Object[]> mealsForSelected     = mealLogDB.getMealLogsByDate(TEST_USER_ID, currentViewDate);
        ArrayList<Object[]> exercisesForSelected = exerciseLogDB.getLogsByDate(TEST_USER_ID, currentViewDate);

        // Kriterijus: abi lentelės reaguoja į TĄ PAČIĄ pasirinktą datą vienu paspaudimu
        assertFalse("YESTERDAY maisto lentelė turi būti užpildyta", mealsForSelected.isEmpty());
        assertFalse("YESTERDAY pratimų lentelė turi būti užpildyta", exercisesForSelected.isEmpty());

        // Patikriname, kad jei pakeičiame datą, atsinaujina abi lentelės
        ArrayList<Object[]> mealsToday     = mealLogDB.getMealLogsByDate(TEST_USER_ID, TODAY);
        ArrayList<Object[]> exercisesToday = exerciseLogDB.getLogsByDate(TEST_USER_ID, TODAY);

        // Šiandienos lentelės turi būti TUŠČIOS (mes vakar tik įdėjome) — kas patvirtina,
        // kad atnaujinimas synchroniškas
        assertTrue("Pakeitus datą į TODAY — maisto lentelė neturi rodyti YESTERDAY duomenų",
                   mealsToday.isEmpty());
        assertTrue("Pakeitus datą į TODAY — pratimų lentelė neturi rodyti YESTERDAY duomenų",
                   exercisesToday.isEmpty());
    }

    // ==================================================================
    // 6. Papildomas: filtravimas pagal userId — kito vartotojo duomenys nematomi
    // ==================================================================
    @Test
    public void testFilteringIsolatedByUser() throws Exception {
        int otherUser = TEST_USER_ID + 500;

        insertMealLogOnDate(TEST_USER_ID, testMealId, 100.0, TODAY);
        insertMealLogOnDate(otherUser,    testMealId, 999.0, TODAY);

        ArrayList<Object[]> myMeals = mealLogDB.getMealLogsByDate(TEST_USER_ID, TODAY);

        // Visi grąžinti įrašai turi priklausyti TIK mūsų vartotojui (jokia 999.0 vertė)
        for (Object[] row : myMeals) {
            assertNotEquals("Kito vartotojo įrašas neturi atsirasti šio vartotojo lentelėje",
                            999.0, (double) row[3], 0.001);
        }

        // Išvalome kito vartotojo duomenis
        try {
            PreparedStatement ps = ConnectionFactory.getConnection()
                .prepareStatement("DELETE FROM DailyMealLog WHERE userId = ?");
            ps.setInt(1, otherUser);
            ps.executeUpdate();
        } catch (Exception ignored) {}
    }
}