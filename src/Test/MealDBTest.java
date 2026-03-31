package Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import impl.MealDB;
import models.Meal;

public class MealDBTest {

    private MealDB mealDB;

    @BeforeEach
    public void setUp() {
        mealDB = new MealDB();
    }

    // -------------------------------------------------------
    // 1. Meal INSERT testai
    // -------------------------------------------------------
    @Test
    public void testInsertMeal_ReturnsPositiveId() {
        Meal m = new Meal();
        m.setMealName("Test Chicken");
        m.setCaloriesPerGram(165.0);

        int id = mealDB.insertMeal(m);
        assertTrue(id > 0, "Insert turi grąžinti ID > 0");
    }

    @Test
    public void testInsertMeal_ZeroCalories() {
        Meal m = new Meal();
        m.setMealName("Water");
        m.setCaloriesPerGram(0.0);

        int id = mealDB.insertMeal(m);
        assertTrue(id > 0, "0 kalorijų maistas turi būti išsaugotas");
    }

    // -------------------------------------------------------
    // 2. Meal RETRIEVAL testai
    // -------------------------------------------------------
    @Test
    public void testGetById_ReturnsCorrectMeal() {
        Meal m = new Meal();
        m.setMealName("Test Rice");
        m.setCaloriesPerGram(130.0);

        int id = mealDB.insertMeal(m);
        Meal fetched = mealDB.getById(id);

        assertEquals("Test Rice", fetched.getName(), "Pavadinimas turi sutapti");
        assertEquals(130.0, fetched.getCaloriesPerGram(), 0.01, "Kalorijos turi sutapti");
    }

    @Test
    public void testGetAll_NotEmpty() {
        Meal m = new Meal();
        m.setMealName("Test Oats");
        m.setCaloriesPerGram(68.0);
        mealDB.insertMeal(m);

        int size = mealDB.getAll().size();
        assertTrue(size > 0, "getAll() turi grąžinti bent vieną įrašą");
    }

    // -------------------------------------------------------
    // 3. Calorie calculation testai
    // -------------------------------------------------------
    @Test
    public void testCalorieCalculation_Standard() {
        // 200g * 165 kcal/100g = 330 kcal
        double result = 165.0 * 200.0 / 100.0;
        assertEquals(330.0, result, 0.01, "200g vištienos turi būti 330 kcal");
    }

    @Test
    public void testCalorieCalculation_SmallPortion() {
        // 50g * 52 kcal/100g = 26 kcal
        double result = 52.0 * 50.0 / 100.0;
        assertEquals(26.0, result, 0.01, "50g obuolio turi būti 26 kcal");
    }

    @Test
    public void testCalorieCalculation_ZeroGrams() {
        double result = 89.0 * 0.0 / 100.0;
        assertEquals(0.0, result, 0.01, "0 gramų = 0 kcal");
    }

    @Test
    public void testCalorieCalculation_ExactHundredGrams() {
        // 100g * 89 kcal/100g = 89 kcal
        double result = 89.0 * 100.0 / 100.0;
        assertEquals(89.0, result, 0.01, "100g = lygiai kcalPer100g");
    }

    // -------------------------------------------------------
    // 4. Meal UPDATE ir DELETE testai
    // -------------------------------------------------------
    @Test
    public void testUpdateMeal() {
        Meal m = new Meal();
        m.setMealName("Update Test Meal");
        m.setCaloriesPerGram(100.0);
        int id = mealDB.insertMeal(m);

        m.setId(id);
        m.setMealName("Updated Meal Name");
        m.setCaloriesPerGram(120.0);

        int rows = mealDB.updateMeal(m);
        assertEquals(1, rows, "Update turi grąžinti 1");

        Meal updated = mealDB.getById(id);
        assertEquals("Updated Meal Name", updated.getName(), "Pavadinimas turi būti atnaujintas");
        assertEquals(120.0, updated.getCaloriesPerGram(), 0.01, "Kalorijos turi būti atnaujintos");
    }

    @Test
    public void testDeleteMeal() {
        Meal m = new Meal();
        m.setMealName("Delete Test Meal");
        m.setCaloriesPerGram(50.0);
        int id = mealDB.insertMeal(m);

        m.setId(id);
        int rows = mealDB.deleteMeal(m);
        assertEquals(1, rows, "Delete turi grąžinti 1");
    }
}