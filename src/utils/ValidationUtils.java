package utils;

import javax.swing.JOptionPane;

public class ValidationUtils {

    // 1. Registracija su tuščiu vardu — klaidos žinutė
    public static boolean validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vardas negali būti tuščias!", "Validacijos Klaida", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // 5. SQL injection bandymas apsaugomas tavo kode naudojant PreparedStatement.
        // Tačiau galime papildomai uždrausti pavojingus simbolius varde:
        if (name.contains(";") || name.contains("'") || name.contains("--")) {
            JOptionPane.showMessageDialog(null, "Varde naudojami neleistini simboliai!", "Saugumo Įspėjimas", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    // 2. Maisto kalorijų laukas su tekstu — klaidos žinutė
    public static boolean validateCalories(String caloriesInput) {
        try {
            double cal = Double.parseDouble(caloriesInput);
            if (cal < 0) {
                JOptionPane.showMessageDialog(null, "Kalorijos negali būti neigiamos!", "Klaida", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            // 4. Labai dideli skaičiai (999999) — programa neklumpa, grąžina įspėjimą
            if (cal > 99999) {
                JOptionPane.showMessageDialog(null, "Įvestas nerealistiškai didelis kalorijų skaičius.", "Įspėjimas", JOptionPane.WARNING_MESSAGE);
                return false; 
            }
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Kalorijų laukelyje turi būti skaičius (pvz., 250.5), o ne tekstas!", "Klaida", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // 3. Neigiami skaičiai kaip svoris ar amžius — klaidos žinutė
    public static boolean validateMetrics(double weight, int age) {
        if (weight <= 0 || age <= 0) {
            JOptionPane.showMessageDialog(null, "Svoris ir amžius privalo būti teigiami skaičiai!", "Klaida", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}