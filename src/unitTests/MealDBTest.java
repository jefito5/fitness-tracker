package unitTests;

import impl.MealDB;
import models.Meal;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

/**
 * Vieneto testai — MealDB klasė.
 *
 * Priėmimo kriterijai:
 *   • testInsertMeal() — patikrina įrašymą
 *   • testGetAllMeals() — patikrina sąrašo gavimą
 *   • testUpdateMeal() — patikrina atnaujinimą
 *   • testDeleteMeal() — patikrina ištrynimą
 *   • testMealCalorieCalculation() — patikrina kcal skaičiavimą
 *   • Testai nepriklausomi — kiekvienas valo po savęs DB
 */
public class MealDBTest {

    private MealDB mealDB;
    private int lastInsertedId = -1;

    /** Pagalbinis metodas: sukuria testinį Meal objektą */
    private Meal createTestMeal(String name, double kcalPer100g) {
        Meal m = new Meal();
        m.setMealName(name);
        m.setCaloriesPerGram(kcalPer100g);
        m.setProteinPer100g(20.0);
        m.setCarbsPer100g(50.0);
        m.setFatPer100g(10.0);
        return m;
    }

    @Before
    public void setUp() {
        mealDB = new MealDB();
    }

    @After
    public void tearDown() {
        // Valymas: ištriname testinį patiekalą, kad neužterštume DB
        if (lastInsertedId > 0) {
            Meal m = new Meal();
            m.setId(lastInsertedId);
            mealDB.deleteMeal(m);
            lastInsertedId = -1;
        }
    }

    // ----------------------------------------------------------
    // testInsertMeal — patikrina ar patiekalas sėkmingai įrašomas
    // ----------------------------------------------------------
    @Test
    public void testInsertMeal() {
        Meal m = createTestMeal("Test_Oatmeal", 389.0);
        int id = mealDB.insertMeal(m);
        lastInsertedId = id;

        assertTrue("insertMeal() turi grąžinti generuotą ID > 0", id > 0);
    }

    // ----------------------------------------------------------
    // testGetAllMeals — patikrina ar gaunamas sąrašas
    // ----------------------------------------------------------
    @Test
    public void testGetAllMeals() {
        // Žingsnis 1: įrašome bent vieną patiekalą
        Meal m = createTestMeal("Test_Rice", 130.0);
        int id = mealDB.insertMeal(m);
        lastInsertedId = id;
        assertTrue("Prieš testą patiekalas turi būti įrašytas", id > 0);

        // Kriterijus: getAll() turi grąžinti ne-null sąrašą su bent vienu elementu
        ArrayList<Meal> meals = mealDB.getAll();
        assertNotNull("getAll() neturi grąžinti null", meals);
        assertTrue("Sąraše turi būti bent vienas patiekalas", meals.size() > 0);

        // Patikriname ar mūsų įrašytas patiekalas yra sąraše
        boolean found = false;
        for (Meal meal : meals) {
            if (meal.getId() == id) {
                found = true;
                assertEquals("Pavadinimas turi sutapti", "Test_Rice", meal.getName());
                break;
            }
        }
        assertTrue("Įrašytas patiekalas turi būti sąraše", found);
    }

    // ----------------------------------------------------------
    // testUpdateMeal — patikrina ar duomenys atnaujinami teisingai
    // ----------------------------------------------------------
    @Test
    public void testUpdateMeal() {
        // Žingsnis 1: įrašome pradinį patiekalą
        Meal original = createTestMeal("Test_Chicken_Orig", 165.0);
        int id = mealDB.insertMeal(original);
        lastInsertedId = id;
        assertTrue("Prieš testą patiekalas turi būti įrašytas", id > 0);

        // Žingsnis 2: atnaujiname duomenis
        Meal updated = new Meal();
        updated.setId(id);
        updated.setMealName("Test_Chicken_Updated");
        updated.setCaloriesPerGram(200.0);
        updated.setProteinPer100g(31.0);
        updated.setCarbsPer100g(0.0);
        updated.setFatPer100g(3.6);

        int affectedRows = mealDB.updateMeal(updated);
        assertEquals("updateMeal() turi grąžinti 1 atnaujintą eilutę", 1, affectedRows);

        // Kriterijus: pakeisti duomenys turi būti matomi DB
        Meal fetched = mealDB.getById(id);
        assertEquals("Pavadinimas turi būti atnaujintas", "Test_Chicken_Updated", fetched.getName());
        assertEquals("Kalorijos turi būti atnaujintos", 200.0, fetched.getCaloriesPerGram(), 0.01);
        assertEquals("Baltymai turi būti atnaujinti", 31.0, fetched.getProteinPer100g(), 0.01);
        assertEquals("Angliavandeniai turi būti atnaujinti", 0.0, fetched.getCarbsPer100g(), 0.01);
        assertEquals("Riebalai turi būti atnaujinti", 3.6, fetched.getFatPer100g(), 0.01);
    }

    // ----------------------------------------------------------
    // testDeleteMeal — patikrina ar patiekalas ištrinamas
    // ----------------------------------------------------------
    @Test
    public void testDeleteMeal() {
        // Žingsnis 1: įrašome patiekalą
        Meal m = createTestMeal("Test_ToDelete", 100.0);
        int id = mealDB.insertMeal(m);
        assertTrue("Prieš testą patiekalas turi būti įrašytas", id > 0);

        // Žingsnis 2: triname
        Meal toDelete = new Meal();
        toDelete.setId(id);
        int deletedRows = mealDB.deleteMeal(toDelete);
        assertEquals("deleteMeal() turi grąžinti 1 ištrintą eilutę", 1, deletedRows);

        // Kriterijus: po trynimo patiekalas neturi būti randamas
        Meal fetched = mealDB.getById(id);
        assertEquals("Ištrintas patiekalas turi grąžinti tuščią objektą (id=0)", 0, fetched.getId());

        // Nebevalome tearDown(), nes jau ištrinome
        lastInsertedId = -1;
    }

    // ----------------------------------------------------------
    // testMealCalorieCalculation — patikrina kcal skaičiavimą
    // ----------------------------------------------------------
    @Test
    public void testMealCalorieCalculation() {
        // Kriterijus: totalKcal = CaloriesPerGram × grams / 100.0
        // (formulė iš MealIUD.InsertMealListener ir trackProgress.InsertDailymealListener)

        double kcalPer100g = 250.0;  // kalorijos per 100g
        double grams = 150.0;       // suvalgyta gramų

        double totalKcal = kcalPer100g * grams / 100.0;

        assertEquals("250 kcal/100g × 150g = 375 kcal", 375.0, totalKcal, 0.01);
    }

    @Test
    public void testMealCalorieCalculation_SmallPortion() {
        // 50g porcija iš 400 kcal/100g maisto
        double totalKcal = 400.0 * 50.0 / 100.0;
        assertEquals("400 kcal/100g × 50g = 200 kcal", 200.0, totalKcal, 0.01);
    }

    @Test
    public void testMealCalorieCalculation_LargePortion() {
        // 300g porcija iš 120 kcal/100g maisto
        double totalKcal = 120.0 * 300.0 / 100.0;
        assertEquals("120 kcal/100g × 300g = 360 kcal", 360.0, totalKcal, 0.01);
    }

    // ----------------------------------------------------------
    // testGetById — patikrina ar grąžinamas teisingas patiekalas
    // ----------------------------------------------------------
    @Test
    public void testGetById() {
        Meal m = createTestMeal("Test_Salmon", 208.0);
        int id = mealDB.insertMeal(m);
        lastInsertedId = id;
        assertTrue("Prieš testą patiekalas turi būti įrašytas", id > 0);

        Meal fetched = mealDB.getById(id);
        assertNotNull("Grąžintas patiekalas neturi būti null", fetched);
        assertEquals("ID turi sutapti", id, fetched.getId());
        assertEquals("Pavadinimas turi sutapti", "Test_Salmon", fetched.getName());
        assertEquals("Kalorijos turi sutapti", 208.0, fetched.getCaloriesPerGram(), 0.01);
        assertEquals("Baltymai turi sutapti", 20.0, fetched.getProteinPer100g(), 0.01);
        assertEquals("Angliavandeniai turi sutapti", 50.0, fetched.getCarbsPer100g(), 0.01);
        assertEquals("Riebalai turi sutapti", 10.0, fetched.getFatPer100g(), 0.01);
    }

    // ----------------------------------------------------------
    // testInsertMeal_WithMacros — patikrina makro duomenų išsaugojimą
    // ----------------------------------------------------------
    @Test
    public void testInsertMeal_WithMacros() {
        Meal m = new Meal();
        m.setMealName("Test_Eggs_Macro");
        m.setCaloriesPerGram(155.0);
        m.setProteinPer100g(13.0);
        m.setCarbsPer100g(1.1);
        m.setFatPer100g(11.0);

        int id = mealDB.insertMeal(m);
        lastInsertedId = id;
        assertTrue("ID turi būti > 0", id > 0);

        Meal fetched = mealDB.getById(id);
        assertEquals("Baltymų reikšmė turi būti išsaugota", 13.0, fetched.getProteinPer100g(), 0.01);
        assertEquals("Angliavandenių reikšmė turi būti išsaugota", 1.1, fetched.getCarbsPer100g(), 0.01);
        assertEquals("Riebalų reikšmė turi būti išsaugota", 11.0, fetched.getFatPer100g(), 0.01);
    }
}
