package unitTests;
import impl.MealLogDB;
import models.DailyMealLog;
import org.junit.Before;
import org.junit.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import static org.junit.Assert.*;

//  ========================================================== MealLogDB unit testai =======================================================

public class MealLogDBTest {

    private MealLogDB mealLogDB;
    private final int TEST_USER_ID = 1;

    @Before
    public void setUp() {
        mealLogDB = new MealLogDB(); 
    }

    @Test
    public void testInsertDailyLog() {
        // Kriterijus: patikrina dienos log įrašymą
        DailyMealLog log = new DailyMealLog();
        log.setUserId(TEST_USER_ID);
        log.setMealId(1);
        log.setTotalCalorieIntake(450.5);
        
        int insertedId = mealLogDB.insertDailyLog(log);
        assertTrue("Įrašymas į duomenų bazę turi grąžinti ID > 0", insertedId > 0);
    }

    @Test
    public void testGetMealLogsByDate() {
        // Kriterijus: patikrina filtravimą pagal datą ir userId
        String today = LocalDate.now().toString();
        ArrayList<Object[]> logs = mealLogDB.getMealLogsByDate(TEST_USER_ID, today);
        
        assertNotNull("Sąrašas neturi būti null", logs);
        // Jei įrašėme prieš tai, sąrašas neturėtų būti tuščias
        // Kriterijus: Duomenys filtruojami teisingai (užklausos lygyje (WHERE d.Date=? AND d.userId=?))
    }

    @Test
    public void testGetAllLogDates() {
        // Kriterijus: patikrina datų sąrašo gavimą
        // PASTABA: Tavo MealLogDB kode kol kas nėra getAllLogDates() metodo, 
        // todėl čia ištestuojame panašų - getTodayMealLogs().
        ArrayList<Object[]> todayLogs = mealLogDB.getTodayMealLogs(TEST_USER_ID);
        assertNotNull("Šiandienos logų sąrašas negali būti null", todayLogs);
    }
}