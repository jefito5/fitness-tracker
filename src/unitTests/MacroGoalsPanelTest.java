package unitTests;

import impl.MacroGoalDB;
import impl.MealLogDB;
import models.MacroGoal;
import database.ConnectionFactory;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Integracinis testas — MacroGoalsPanel funkcionalumas
 *
 * Priėmimo kriterijai:
 *  1. Makroelementų tikslai (baltymai, riebalai, angliavandeniai) išsaugomi
 *  2. Tikslai atsinaujina po redagavimo
 *  3. Rodomas progresas pagal dienos suvartojimą
 *
 * Šis testas tikrina MacroGoalDB + MealLogDB sluoksnius kartu
 * (integracinis lygis), imituodamas tai, ką MacroGoalsPanel atlieka GUI.
 */
public class MacroGoalsPanelTest {

    // Naudojame atskirą testinį userId, kad nekliudytume realių duomenų
    private static final int TEST_USER_ID = 999_001;

    private MacroGoalDB macroGoalDB;
    private final java.util.List<Integer> createdProfileIds = new ArrayList<>();

    // ── Pagalbinis metodas: sukuria MacroGoal objektą ────────────────────────
    private MacroGoal buildGoal(String name, double carb, double protein, double fat) {
        return new MacroGoal(TEST_USER_ID, name, carb, protein, fat);
    }

    // ── Pagalbinis metodas: tiesiogiai valo testinį vartotoją iš visų lentelių
    private void cleanupTestUser() {
        try {
            Connection conn = ConnectionFactory.getConnection();
            for (String sql : new String[]{
                "DELETE FROM macro_goals WHERE userId=?",
                "DELETE FROM user_calorie_goal WHERE userId=?"
            }) {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, TEST_USER_ID);
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Before
    public void setUp() {
        macroGoalDB = new MacroGoalDB();
        createdProfileIds.clear();
        cleanupTestUser(); // šviežia pradžia
    }

    @After
    public void tearDown() {
        cleanupTestUser();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  1 KRITERIJUS: Makroelementų tikslai išsaugomi
    //     — patikrinama, ar sukurti profiliai išlieka DB
    //     — baltymai, riebalai ir angliavandeniai saugomi tiksliai
    // ══════════════════════════════════════════════════════════════════════════
    @Test
    public void testMacroGoalsAreSaved() {
        MacroGoal goal = buildGoal("Testinis profilis", 50.0, 30.0, 20.0);

        int id = macroGoalDB.insert(goal);
        createdProfileIds.add(id);

        assertTrue("insert() turi grąžinti ID > 0", id > 0);

        // Paskaitome visus profilius vartotojui ir suraskime mūsų
        ArrayList<MacroGoal> profiles = macroGoalDB.getByUser(TEST_USER_ID);
        assertFalse("Profilių sąrašas neturi būti tuščias", profiles.isEmpty());

        MacroGoal saved = profiles.stream()
                .filter(g -> g.getId() == id)
                .findFirst()
                .orElse(null);

        assertNotNull("Įrašytas profilis turi būti randamas pagal ID", saved);
        assertEquals("profileName turi sutapti",   "Testinis profilis", saved.getProfileName());
        assertEquals("carbPercent turi sutapti",   50.0, saved.getCarbPercent(),   0.001);
        assertEquals("proteinPercent turi sutapti",30.0, saved.getProteinPercent(),0.001);
        assertEquals("fatPercent turi sutapti",    20.0, saved.getFatPercent(),    0.001);
    }

    @Test
    public void testMultipleMacroProfilesSaved() {
        // Galima išsaugoti kelis profilius tam pačiam vartotojui
        int id1 = macroGoalDB.insert(buildGoal("Keto",     5.0,  30.0, 65.0));
        int id2 = macroGoalDB.insert(buildGoal("Balanced", 50.0, 25.0, 25.0));
        createdProfileIds.add(id1);
        createdProfileIds.add(id2);

        ArrayList<MacroGoal> profiles = macroGoalDB.getByUser(TEST_USER_ID);
        assertEquals("Turi būti 2 išsaugoti profiliai", 2, profiles.size());
    }

    @Test
    public void testCalorieGoalIsSaved() {
        // Kalorinis tikslas išsaugomas ir grąžinamas tiksliai
        macroGoalDB.saveCalorieGoal(TEST_USER_ID, 2500.0);

        double saved = macroGoalDB.getCalorieGoal(TEST_USER_ID);
        assertEquals("Kalorinis tikslas turi sutapti", 2500.0, saved, 0.001);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  2 KRITERIJUS: Tikslai atsinaujina po redagavimo
    //     — update() keičia saugomas reikšmes
    //     — setActive() perjungia aktyvų profilį
    // ══════════════════════════════════════════════════════════════════════════
    @Test
    public void testMacroGoalUpdatedAfterEdit() {
        // 1. Įterpiame pradinį profilį
        MacroGoal goal = buildGoal("Pirminis", 40.0, 35.0, 25.0);
        int id = macroGoalDB.insert(goal);
        createdProfileIds.add(id);

        // 2. Redaguojame profilį (imituoja „Save Profile" veiksmą GUI)
        goal.setId(id);
        goal.setProfileName("Atnaujintas");
        goal.setCarbPercent(30.0);
        goal.setProteinPercent(45.0);
        goal.setFatPercent(25.0);

        int rows = macroGoalDB.update(goal);
        assertEquals("update() turi grąžinti 1 (viena atnaujinta eilutė)", 1, rows);

        // 3. Patikriname, ar pakeitimai išliko
        ArrayList<MacroGoal> profiles = macroGoalDB.getByUser(TEST_USER_ID);
        MacroGoal updated = profiles.stream()
                .filter(g -> g.getId() == id)
                .findFirst()
                .orElse(null);

        assertNotNull("Atnaujintas profilis turi būti randamas", updated);
        assertEquals("profileName turi atsinaujinti",    "Atnaujintas", updated.getProfileName());
        assertEquals("carbPercent turi atsinaujinti",    30.0, updated.getCarbPercent(),   0.001);
        assertEquals("proteinPercent turi atsinaujinti", 45.0, updated.getProteinPercent(),0.001);
        assertEquals("fatPercent turi atsinaujinti",     25.0, updated.getFatPercent(),    0.001);
    }

    @Test
    public void testSetActiveProfileSwitchesCorrectly() {
        // Sukuriame du profilius
        int id1 = macroGoalDB.insert(buildGoal("Profilis A", 50.0, 25.0, 25.0));
        int id2 = macroGoalDB.insert(buildGoal("Profilis B", 40.0, 40.0, 20.0));
        createdProfileIds.add(id1);
        createdProfileIds.add(id2);

        // Aktyvuojame pirmąjį
        macroGoalDB.setActive(TEST_USER_ID, id1);
        MacroGoal active = macroGoalDB.getActive(TEST_USER_ID);
        assertNotNull("Aktyvus profilis turi egzistuoti", active);
        assertEquals("Aktyvus turi būti id1", id1, active.getId());

        // Perjungiame į antrąjį — imituoja „Set Active" paspaudimą GUI
        macroGoalDB.setActive(TEST_USER_ID, id2);
        MacroGoal newActive = macroGoalDB.getActive(TEST_USER_ID);
        assertNotNull("Naujas aktyvus profilis turi egzistuoti", newActive);
        assertEquals("Aktyvus turi būti id2", id2, newActive.getId());
        assertTrue("isActive() turi būti true", newActive.isActive());

        // Pirmasis nebeaktyvus
        ArrayList<MacroGoal> all = macroGoalDB.getByUser(TEST_USER_ID);
        MacroGoal formerActive = all.stream().filter(g -> g.getId() == id1).findFirst().orElse(null);
        assertNotNull(formerActive);
        assertFalse("Buvęs aktyvus profilis turi tapti neaktyviu", formerActive.isActive());
    }

    @Test
    public void testCalorieGoalUpdatedAfterEdit() {
        // Išsaugome ir tada pakeičiame kalorių tikslą
        macroGoalDB.saveCalorieGoal(TEST_USER_ID, 2000.0);
        macroGoalDB.saveCalorieGoal(TEST_USER_ID, 2800.0); // atnaujinimas

        double updated = macroGoalDB.getCalorieGoal(TEST_USER_ID);
        assertEquals("Kalorinis tikslas turi atsinaujinti į naują reikšmę", 2800.0, updated, 0.001);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  3 KRITERIJUS: Rodomas progresas pagal dienos suvartojimą
    //     — MacroGoal gramų skaičiavimo metodai veikia teisingai
    //     — getMacroSummary() grąžina masyvą be išimčių (net be log įrašų)
    //     — progreso santykis skaičiuojamas teisingai
    // ══════════════════════════════════════════════════════════════════════════
    @Test
    public void testMacroGramsCalculatedFromGoal() {
        // Profilis: 50% carb / 30% protein / 20% fat, 2000 kcal tikslas
        MacroGoal goal = new MacroGoal(TEST_USER_ID, "Calc Test", 50.0, 30.0, 20.0);

        double dailyCal = 2000.0;

        // Angliavandeniai: (2000 * 50/100) / 4 = 250g
        assertEquals("Carb gramų skaičiavimas neteisingas",
                250.0, goal.getCarbGrams(dailyCal), 0.01);

        // Baltymai: (2000 * 30/100) / 4 = 150g
        assertEquals("Protein gramų skaičiavimas neteisingas",
                150.0, goal.getProteinGrams(dailyCal), 0.01);

        // Riebalai: (2000 * 20/100) / 9 ≈ 44.44g
        assertEquals("Fat gramų skaičiavimas neteisingas",
                44.44, goal.getFatGrams(dailyCal), 0.01);
    }

    @Test
    public void testProgressRatioCalculation() {
        // Imituoja progreso žiedo logiką iš MacroGoalsPanel.RingPanel
        MacroGoal goal = new MacroGoal(TEST_USER_ID, "Ring Test", 50.0, 30.0, 20.0);
        double dailyCal = 2000.0;

        double targetCarbs = goal.getCarbGrams(dailyCal);   // 250g
        double consumed    = 125.0;                          // 125g suvartota

        double ratio = targetCarbs > 0 ? Math.min(consumed / targetCarbs, 1.0) : 0.0;

        assertEquals("Progreso santykis turi būti 0.5 (50%)", 0.5, ratio, 0.001);
    }

    @Test
    public void testProgressRatioDoesNotExceedOne() {
        // Persivalgymo atveju (consumed > target) santykis apribojamas iki 1.0
        MacroGoal goal = new MacroGoal(TEST_USER_ID, "Overflow Test", 50.0, 30.0, 20.0);
        double dailyCal = 2000.0;

        double target   = goal.getCarbGrams(dailyCal); // 250g
        double consumed = 400.0;                        // viršijama

        double ratio = target > 0 ? Math.min(consumed / target, 1.0) : 0.0;
        assertEquals("Santykis neturi viršyti 1.0", 1.0, ratio, 0.001);
    }

    @Test
    public void testGetMacroSummaryReturnsArrayForUserWithNoLogs() {
        // getMacroSummary() turi grąžinti {0,0,0} masyvą, jei log nėra
        // (imituoja MacroGoalsPanel.refreshRings() pirmojo atidarymui)
        MealLogDB mealLogDB = new MealLogDB();
        String today = String.valueOf(LocalDate.now());

        double[] result = mealLogDB.getMacroSummary(TEST_USER_ID, today);

        assertNotNull("getMacroSummary() neturi grąžinti null", result);
        assertEquals("Masyvas turi turėti 3 elementus", 3, result.length);
        assertEquals("Protein turi būti 0 kai nėra log", 0.0, result[0], 0.001);
        assertEquals("Carbs turi būti 0 kai nėra log",   0.0, result[1], 0.001);
        assertEquals("Fat turi būti 0 kai nėra log",     0.0, result[2], 0.001);
    }

    @Test
    public void testNoActiveProfileReturnedWhenNoneSet() {
        // Jei nė vienas profilis neaktyvus, getActive() turi grąžinti null
        macroGoalDB.insert(buildGoal("Neaktyvus A", 50.0, 25.0, 25.0));
        // setActive() niekada nekviestas

        MacroGoal active = macroGoalDB.getActive(TEST_USER_ID);
        assertNull("Neaktyvuoto vartotojo getActive() turi grąžinti null", active);
    }
}
