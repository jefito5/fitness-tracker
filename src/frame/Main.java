package frame;
import database.ConnectionFactory;
//import gui.MealUD;
import gui.Iud;
public class Main {
    public static void main(String[] args) {
        ConnectionFactory.getConnection();
        new Iud();
    }
}
