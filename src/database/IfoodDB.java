package database;

import models.Food;
import java.util.List;

public interface IfoodDB {
    void insert(Food food);
    boolean isEmpty();
    List<Food> searchByName(String query);
    List<Food> getAll();
}
