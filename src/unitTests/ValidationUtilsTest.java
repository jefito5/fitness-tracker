package unitTests;

import org.junit.Test;
import static org.junit.Assert.*;

// Pakeisk "utils", jei tavo ValidationUtils guli kitoje pakuotėje
import utils.ValidationUtils; 

public class ValidationUtilsTest {

    @Test
    public void testValidateName() {
        // TEIGIAMI SCENARIJAI: Viskas gerai, turi grąžinti TRUE
        assertTrue("Turi leisti normalų vardą", ValidationUtils.validateName("Jonas"));
        assertTrue("Turi leisti vardą su tarpu", ValidationUtils.validateName("Ona Vaitkute"));

        // NEIGIAMI SCENARIJAI: Blogi duomenys, turi grąžinti FALSE
        assertFalse("Neturi leisti tuščio vardo", ValidationUtils.validateName(""));
        assertFalse("Neturi leisti vardo tik iš tarpų", ValidationUtils.validateName("   "));
        assertFalse("Neturi leisti null reikšmės", ValidationUtils.validateName(null));
        
        // Saugumo (SQL Injection) testas
        assertFalse("Neturi leisti SQL injection simbolių", ValidationUtils.validateName("Jonas' OR 1=1--"));
    }

    @Test
    public void testValidateCalories() {
        // TEIGIAMI SCENARIJAI
        assertTrue("Turi veikti su normaliais skaičiais", ValidationUtils.validateCalories("250.5"));
        assertTrue("Turi veikti su nuliu", ValidationUtils.validateCalories("0"));

        // NEIGIAMI SCENARIJAI
        assertFalse("Neturi leisti raidžių/teksto", ValidationUtils.validateCalories("obuolys"));
        assertFalse("Neturi leisti neigiamų skaičių", ValidationUtils.validateCalories("-50"));
        assertFalse("Neturi leisti nerealistiškai didelių skaičių", ValidationUtils.validateCalories("999999"));
    }

    @Test
    public void testValidateMetrics() {
        // TEIGIAMI SCENARIJAI
        assertTrue("80.5 kg ir 25 metai turi praeiti", ValidationUtils.validateMetrics(80.5, 25));

        // NEIGIAMI SCENARIJAI
        assertFalse("Neturi leisti neigiamo svorio", ValidationUtils.validateMetrics(-10, 25));
        assertFalse("Neturi leisti neigiamo amžiaus", ValidationUtils.validateMetrics(80.5, -5));
        assertFalse("Neturi leisti nulinių reikšmių", ValidationUtils.validateMetrics(0, 0));
    }
}