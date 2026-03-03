package frame;

import database.ConnectionFactory;
import gui.MealUD;

public class Main {
    public static void main(String[] args) {
        ConnectionFactory.initializeDatabase();
        new MealUD();
    }
}
