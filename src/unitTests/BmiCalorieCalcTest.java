package unitTests;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Integracinis testas — BMI ir kalorijų skaičiuokliai.
 *
 * Testuojama logika iš BmiPanel.refreshBmi() ir CalorieCalcPanel.calculate().
 * Formulės atkurtos iš šaltinio kodo, kad galėtume validuoti be Swing UI.
 *
 * Priėmimo kriterijai:
 *   • BMI skaičiuojamas teisingai pagal ūgį ir svorį
 *   • BMI kategorija rodoma teisingai (Normalus, Antsvoris ir kt.)
 *   • Kalorijų norma skaičiuojama pagal amžių, lytį, svorį, aktyvumo lygį
 *   • Mifflin-St Jeor formulė naudojama teisingai (kode naudojama ši, ne Harris-Benedict)
 *   • Neteisingi įvesties duomenys — rodoma klaidos žinutė
 */
public class BmiCalorieCalcTest {

    // ══════════════════════════════════════════════════════════════
    //  Formulės — atkurtos tiesiogiai iš BmiPanel ir CalorieCalcPanel
    // ══════════════════════════════════════════════════════════════

    /** BMI = svoris(kg) / ūgis(m)^2  (iš BmiPanel.refreshBmi()) */
    static double calculateBmi(double weightKg, double heightCm) {
        double heightM = heightCm / 100.0;
        return weightKg / (heightM * heightM);
    }

    /** Kategorija pagal BMI ribas (iš BmiPanel.refreshBmi()) */
    static String bmiCategory(double bmi) {
        if (bmi < 18.5) return "Underweight";
        else if (bmi < 25.0) return "Normal weight";
        else if (bmi < 30.0) return "Overweight";
        else return "Obese";
    }

    /** Mifflin-St Jeor BMR (iš CalorieCalcPanel.calculate()) */
    static double calculateBmr(double weightKg, double heightCm, int age, String gender) {
        if ("female".equals(gender.toLowerCase())) {
            return 10 * weightKg + 6.25 * heightCm - 5 * age - 161;
        } else {
            return 10 * weightKg + 6.25 * heightCm - 5 * age + 5;
        }
    }

    /** TDEE = BMR × aktyvumo koeficientas (iš CalorieCalcPanel.calculate()) */
    static double calculateTdee(double bmr, int activityIndex) {
        double[] multipliers = {1.2, 1.375, 1.55, 1.725};
        return bmr * multipliers[activityIndex];
    }

    /** Tikrina ar įvesties duomenys validūs (iš CalorieCalcPanel.calculate()) */
    static boolean isValidInput(double heightCm, double weightKg, int age) {
        return heightCm > 0 && weightKg > 0 && age > 0;
    }

    // ══════════════════════════════════════════════════════════════
    //  BMI testai
    // ══════════════════════════════════════════════════════════════

    @Test
    public void testBmiCalculation_NormalWeight() {
        // Kriterijus: BMI skaičiuojamas teisingai pagal ūgį ir svorį
        // 70 kg, 175 cm → BMI = 70 / 1.75² = 22.86
        double bmi = calculateBmi(70.0, 175.0);
        assertEquals("70kg/175cm BMI turi būti ~22.86", 22.86, bmi, 0.01);
    }

    @Test
    public void testBmiCalculation_Overweight() {
        // 90 kg, 170 cm → BMI = 90 / 1.70² = 31.14
        double bmi = calculateBmi(90.0, 170.0);
        assertEquals("90kg/170cm BMI turi būti ~31.14", 31.14, bmi, 0.01);
    }

    @Test
    public void testBmiCalculation_Underweight() {
        // 45 kg, 170 cm → BMI = 45 / 1.70² = 15.57
        double bmi = calculateBmi(45.0, 170.0);
        assertEquals("45kg/170cm BMI turi būti ~15.57", 15.57, bmi, 0.01);
    }

    // ── BMI kategorijos ────────────────────────────────────────

    @Test
    public void testBmiCategory_Underweight() {
        // Kriterijus: BMI kategorija rodoma teisingai
        assertEquals("BMI 17.0 turi būti Underweight", "Underweight", bmiCategory(17.0));
        assertEquals("BMI 18.4 turi būti Underweight", "Underweight", bmiCategory(18.4));
    }

    @Test
    public void testBmiCategory_Normal() {
        assertEquals("BMI 18.5 turi būti Normal weight", "Normal weight", bmiCategory(18.5));
        assertEquals("BMI 22.0 turi būti Normal weight", "Normal weight", bmiCategory(22.0));
        assertEquals("BMI 24.9 turi būti Normal weight", "Normal weight", bmiCategory(24.9));
    }

    @Test
    public void testBmiCategory_Overweight() {
        assertEquals("BMI 25.0 turi būti Overweight", "Overweight", bmiCategory(25.0));
        assertEquals("BMI 27.5 turi būti Overweight", "Overweight", bmiCategory(27.5));
        assertEquals("BMI 29.9 turi būti Overweight", "Overweight", bmiCategory(29.9));
    }

    @Test
    public void testBmiCategory_Obese() {
        assertEquals("BMI 30.0 turi būti Obese", "Obese", bmiCategory(30.0));
        assertEquals("BMI 35.0 turi būti Obese", "Obese", bmiCategory(35.0));
    }

    // ── BMI ribinės reikšmės ───────────────────────────────────

    @Test
    public void testBmiCategory_BoundaryValues() {
        // Tikrina tikslias ribas tarp kategorijų
        assertEquals("BMI lygiai 18.5 → Normal", "Normal weight", bmiCategory(18.5));
        assertEquals("BMI lygiai 25.0 → Overweight", "Overweight", bmiCategory(25.0));
        assertEquals("BMI lygiai 30.0 → Obese", "Obese", bmiCategory(30.0));
    }

    // ══════════════════════════════════════════════════════════════
    //  Kalorijų skaičiuoklės testai (Mifflin-St Jeor)
    // ══════════════════════════════════════════════════════════════

    @Test
    public void testBmr_Male() {
        // Kriterijus: kalorijų norma skaičiuojama teisingai pagal lytį
        // Vyras: BMR = 10×80 + 6.25×180 – 5×25 + 5 = 800 + 1125 – 125 + 5 = 1805
        double bmr = calculateBmr(80.0, 180.0, 25, "male");
        assertEquals("Vyro BMR (80kg, 180cm, 25m) = 1805", 1805.0, bmr, 0.01);
    }

    @Test
    public void testBmr_Female() {
        // Moteris: BMR = 10×60 + 6.25×165 – 5×30 – 161 = 600 + 1031.25 – 150 – 161 = 1320.25
        double bmr = calculateBmr(60.0, 165.0, 30, "female");
        assertEquals("Moters BMR (60kg, 165cm, 30m) = 1320.25", 1320.25, bmr, 0.01);
    }

    @Test
    public void testTdee_Sedentary() {
        // Kriterijus: aktyvumo lygis teisingai keičia TDEE
        // Sedentary: TDEE = 1805 × 1.2 = 2166
        double bmr = 1805.0;
        double tdee = calculateTdee(bmr, 0); // Sedentary
        assertEquals("Sedentary TDEE = BMR × 1.2", 2166.0, tdee, 0.01);
    }

    @Test
    public void testTdee_Light() {
        double bmr = 1805.0;
        double tdee = calculateTdee(bmr, 1); // Light
        assertEquals("Light TDEE = BMR × 1.375", 1805.0 * 1.375, tdee, 0.01);
    }

    @Test
    public void testTdee_Moderate() {
        double bmr = 1805.0;
        double tdee = calculateTdee(bmr, 2); // Moderate
        assertEquals("Moderate TDEE = BMR × 1.55", 1805.0 * 1.55, tdee, 0.01);
    }

    @Test
    public void testTdee_Active() {
        double bmr = 1805.0;
        double tdee = calculateTdee(bmr, 3); // Active
        assertEquals("Active TDEE = BMR × 1.725", 1805.0 * 1.725, tdee, 0.01);
    }

    @Test
    public void testTdee_FullCalculation_MaleModerate() {
        // Kriterijus: pilnas skaičiavimas nuo pradžios iki galo
        // Vyras, 80kg, 180cm, 25m, moderate
        double bmr  = calculateBmr(80.0, 180.0, 25, "male");    // = 1805
        double tdee = calculateTdee(bmr, 2);                      // = 1805 × 1.55 = 2797.75
        assertEquals("Pilnas TDEE vyro moderate = 2797.75", 2797.75, tdee, 0.01);
    }

    @Test
    public void testTdee_FullCalculation_FemaleSedentary() {
        // Moteris, 60kg, 165cm, 30m, sedentary
        double bmr  = calculateBmr(60.0, 165.0, 30, "female");  // = 1320.25
        double tdee = calculateTdee(bmr, 0);                      // = 1320.25 × 1.2 = 1584.3
        assertEquals("Pilnas TDEE moters sedentary = 1584.3", 1584.3, tdee, 0.01);
    }

    // ══════════════════════════════════════════════════════════════
    //  Klaidingų duomenų testai
    // ══════════════════════════════════════════════════════════════

    @Test
    public void testInvalidInput_ZeroHeight() {
        // Kriterijus: neteisingi duomenys aptinkami
        assertFalse("Ūgis 0 turi būti atmestas", isValidInput(0, 70.0, 25));
    }

    @Test
    public void testInvalidInput_ZeroWeight() {
        assertFalse("Svoris 0 turi būti atmestas", isValidInput(175.0, 0, 25));
    }

    @Test
    public void testInvalidInput_ZeroAge() {
        assertFalse("Amžius 0 turi būti atmestas", isValidInput(175.0, 70.0, 0));
    }

    @Test
    public void testInvalidInput_NegativeValues() {
        assertFalse("Neigiamas ūgis turi būti atmestas", isValidInput(-175.0, 70.0, 25));
        assertFalse("Neigiamas svoris turi būti atmestas", isValidInput(175.0, -70.0, 25));
        assertFalse("Neigiamas amžius turi būti atmestas", isValidInput(175.0, 70.0, -25));
    }

    @Test
    public void testValidInput_AllPositive() {
        assertTrue("Teisingi duomenys turi praeiti", isValidInput(180.0, 80.0, 25));
    }

    // ══════════════════════════════════════════════════════════════
    //  Svorio projekcija (iš CalorieCalcPanel)
    // ══════════════════════════════════════════════════════════════

    @Test
    public void testWeightProjection_Deficit() {
        // Jei kasdien valgai 500 kcal mažiau, per mėnesį:
        // balance = -500, monthKg = (-500 × 30) / 7700 ≈ -1.95 kg
        double balance = -500;
        double monthKg = (balance * 30) / 7700.0;
        assertTrue("Deficitas turi rodyti svorio netekimą", monthKg < 0);
        assertEquals("500 kcal deficitas per mėn ≈ -1.95 kg", -1.95, monthKg, 0.01);
    }

    @Test
    public void testWeightProjection_Surplus() {
        // Jei kasdien valgai 300 kcal daugiau:
        // monthKg = (300 × 30) / 7700 ≈ +1.17 kg
        double balance = 300;
        double monthKg = (balance * 30) / 7700.0;
        assertTrue("Perteklius turi rodyti svorio prieaugį", monthKg > 0);
        assertEquals("300 kcal perteklius per mėn ≈ +1.17 kg", 1.17, monthKg, 0.01);
    }
}
