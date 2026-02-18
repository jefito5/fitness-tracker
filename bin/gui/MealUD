package gui;

import javax.swing.*;
import impl.MealDB;
import models.Meal;

public class MealUD {

    public MealUD() {
        JFrame f = new JFrame("Add Meal");
        JTextField name = new JTextField();
        JTextField grams = new JTextField();
        JButton save = new JButton("Save");

        save.addActionListener(e -> {
            Meal m = new Meal(name.getText(),
                              Double.parseDouble(grams.getText()),
                              2.0);
            new MealDB().insert(m);
        });

        f.setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS));
        f.add(name);
        f.add(grams);
        f.add(save);
        f.pack();
        f.setVisible(true);
    }
}
