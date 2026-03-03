import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import impl.MealDB;
import models.Meal;

public class MealDBTest {

    @Test
    public void testInsertMeal() {
        MealDB db = new MealDB();
        Meal meal = new Meal("Obuolys", 100.0, 0.5);
        assertDoesNotThrow(() -> db.insert(meal));
    }

    @Test
    public void testMealName() {
        Meal meal = new Meal("Bananas", 150.0, 0.9);
        assertEquals("Bananas", meal.getName());
    }

    @Test
    public void testMealCalories() {
        Meal meal = new Meal("Ryžiai", 200.0, 3.6);
        assertEquals(3.6, meal.getCaloriesPerGram());
    }
}