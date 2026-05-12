package unitTests;
import impl.WeightDB;
import impl.WaistDB;
import models.Weight;
import models.Waist;
import org.junit.Before;
import org.junit.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import static org.junit.Assert.*;

//  ========================================================== WaistDB ir WeightDB unit testai ==================================================

public class MetricsDBTest {

    private WeightDB weightDB;
    private WaistDB waistDB;
    private final int TEST_USER_ID = 1;

    @Before
    public void setUp() {
        weightDB = new WeightDB();
        waistDB = new WaistDB();
    }

    @Test
    public void testInsertWeight() {
        // Kriterijus: patikrina svorio įrašymą su data
        Weight w = new Weight();
        w.setUserId(TEST_USER_ID);
        w.setWeightM(85.0);
        // Data tavo kode automatiškai priskiriama viduje (LocalDate.now())
        
        int resultId = weightDB.insertWeight(w);
        assertTrue("Svorio įrašymas turi sugeneruoti ir grąžinti ID", resultId > 0);
    }

    @Test
    public void testGetWeightHistory() {
        // Kriterijus: patikrina istorijos gavimą pagal userId
        // Testuojame naudojant tavo naująjį metodą
        String startDate = "2000-01-01";
        String endDate = LocalDate.now().toString();
        ArrayList<Weight> history = weightDB.getWeightsByDateRange(TEST_USER_ID, startDate, endDate);
        
        assertNotNull("Svorio istorija negali būti null", history);
        if(!history.isEmpty()) {
            assertEquals("Ištraukta istorija turi priklausyti testuojamam vartotojui", TEST_USER_ID, history.get(0).getUserId());
        }
    }

    @Test
    public void testInsertWaist() {
        // Kriterijus: patikrina juosmens matavimo įrašymą
        Waist measurement = new Waist();
        measurement.setUserId(TEST_USER_ID);
        measurement.setWaistM(82.5);

        int resultId = waistDB.insertWaist(measurement);
        assertTrue("Juosmens įrašymas turi pavykti", resultId > 0);
    }

    @Test
    public void testGetWaistHistory() {
        // Kriterijus: patikrina juosmens istorijos gavimą
        ArrayList<Waist> history = waistDB.getWaist(); // Tavo kode tai grąžina visus
        assertNotNull("Juosmens istorija neturi būti null", history);
    }
}