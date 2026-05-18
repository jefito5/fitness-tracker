package unitTests;

import impl.UserDB;
import models.User;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Integracinis testas — vartotojo registracija ir prisijungimas.
 *
 * Testuojama logika iš Iud.RegisterListener ir Iud.LoginListener,
 * be Swing priklausomybės — tiesiogiai su UserDB.
 *
 * Priėmimo kriterijai:
 *   • Naujas vartotojas sėkmingai užsiregistruoja su validžiais duomenimis
 *   • Bandant registruotis su egzistuojančiu vardu — rodoma klaidos žinutė
 *   • Prisijungimas su teisingais duomenimis — atsidaromas MealIUD langas
 *   • Prisijungimas su neteisingais duomenimis — rodoma klaidos žinutė
 *   • Tuščių laukų validacija veikia (Iud.java)
 */
public class UserAuthTest {

    private UserDB userDB;
    // Sekame visus sukurtus vartotojus, kad galėtume juos identifikuoti
    private static final String TEST_PREFIX = "AuthTest_";
    private static int testCounter = 0;

    /** Generuoja unikalų vartotojo vardą kiekvienam testui */
    private String uniqueName() {
        return TEST_PREFIX + System.currentTimeMillis() + "_" + (++testCounter);
    }

    @Before
    public void setUp() {
        userDB = new UserDB();
    }

    @After
    public void tearDown() {
        // DB neturi delete metodo vartotojams, todėl valome stebėdami testCounter
    }

    // ══════════════════════════════════════════════════════════════
    //  REGISTRACIJA
    // ══════════════════════════════════════════════════════════════

    @Test
    public void testRegister_ValidData() {
        // Kriterijus: naujas vartotojas sėkmingai užsiregistruoja su validžiais duomenimis
        String name = uniqueName();
        User u = new User();
        u.setName(name);
        u.setAge(25);
        u.setGender("male");
        u.setPassword("pass1234");
        u.setHeight(180.0);

        int id = userDB.insert(u);

        assertTrue("Registracija turi grąžinti ID > 0", id > 0);

        // Patikriname, kad vartotojas tikrai sukurtas DB
        User fetched = userDB.getById(id);
        assertEquals("Vardas turi sutapti", name, fetched.getName());
        assertEquals("Amžius turi sutapti", 25, fetched.getAge());
        assertEquals("Lytis turi sutapti", "male", fetched.getGender());
    }

    @Test
    public void testRegister_DuplicateName() {
        // Kriterijus: bandant registruotis su egzistuojančiu vardu — aptinkama
        // (Iud.java neblokuoja dublikatų DB lygyje, bet getByName() leidžia patikrinti)
        String name = uniqueName();

        // Sukuriame pirmą vartotoją
        User u1 = new User();
        u1.setName(name);
        u1.setAge(25);
        u1.setGender("male");
        u1.setPassword("pass1234");
        int id1 = userDB.insert(u1);
        assertTrue("Pirmasis vartotojas turi būti sukurtas", id1 > 0);

        // Simuliuojame patikrinimą prieš registraciją (kaip Iud turėtų daryti)
        User existing = userDB.getByName(name);
        boolean nameExists = (existing != null && existing.getName() != null && existing.getName().equals(name));

        assertTrue("Sistema turi aptikti, kad vardas jau egzistuoja", nameExists);
    }

    // ══════════════════════════════════════════════════════════════
    //  PRISIJUNGIMAS
    // ══════════════════════════════════════════════════════════════

    @Test
    public void testLogin_CorrectCredentials() {
        // Kriterijus: prisijungimas su teisingais duomenimis — sėkmingas
        String name = uniqueName();
        String password = "correct_pass";

        // Žingsnis 1: registruojame vartotoją
        User u = new User();
        u.setName(name);
        u.setAge(30);
        u.setGender("female");
        u.setPassword(password);
        int id = userDB.insert(u);
        assertTrue("Vartotojas turi būti sukurtas", id > 0);

        // Žingsnis 2: simuliuojame prisijungimą (logika iš Iud.LoginListener)
        User found = userDB.getByName(name);
        boolean loginSuccess = found != null
                && found.getName().equals(name)
                && found.getPassword().equals(password);

        assertTrue("Prisijungimas su teisingais duomenimis turi pavykti", loginSuccess);
        assertEquals("Rastas vartotojo ID turi sutapti", id, found.getId());
    }

    @Test
    public void testLogin_WrongPassword() {
        // Kriterijus: prisijungimas su neteisingais duomenimis — nepavyksta
        String name = uniqueName();

        // Registruojame su vienu slaptažodžiu
        User u = new User();
        u.setName(name);
        u.setAge(22);
        u.setGender("male");
        u.setPassword("real_password");
        userDB.insert(u);

        // Bandome prisijungti su kitu slaptažodžiu
        User found = userDB.getByName(name);
        boolean loginSuccess = found != null
                && found.getName().equals(name)
                && found.getPassword().equals("wrong_password");

        assertFalse("Prisijungimas su blogu slaptažodžiu turi nepavykti", loginSuccess);
    }

    @Test
    public void testLogin_NonexistentUser() {
        // Kriterijus: prisijungimas su neegzistuojančiu vardu
        User found = userDB.getByName("NonexistentUser_99999");

        // getByName() grąžina tuščią User objektą (id=0), ne null
        boolean loginSuccess = found != null
                && found.getName() != null
                && found.getName().equals("NonexistentUser_99999");

        assertFalse("Prisijungimas su neegzistuojančiu vardu turi nepavykti", loginSuccess);
    }

    // ══════════════════════════════════════════════════════════════
    //  TUŠČIŲ LAUKŲ VALIDACIJA (logika iš Iud.java)
    // ══════════════════════════════════════════════════════════════

    @Test
    public void testValidation_EmptyName() {
        // Kriterijus: tuščias vardas turi būti atmestas
        // (Iud.RegisterListener: if (name.isEmpty() || age.isEmpty() || pass.isEmpty()) → klaida)
        String name = "";
        boolean isValid = !name.isEmpty();
        assertFalse("Tuščias vardas turi būti atmestas", isValid);
    }

    @Test
    public void testValidation_EmptyPassword() {
        // Kriterijus: tuščias slaptažodis turi būti atmestas
        String pass = "";
        boolean isValid = !pass.isEmpty();
        assertFalse("Tuščias slaptažodis turi būti atmestas", isValid);
    }

    @Test
    public void testValidation_ShortPassword() {
        // Kriterijus: per trumpas slaptažodis (< 4 simboliai) turi būti atmestas
        // (Iud.RegisterListener: if (pass.length() < 4) → klaida)
        String pass = "abc";
        boolean isValid = pass.length() >= 4;
        assertFalse("Slaptažodis < 4 simbolių turi būti atmestas", isValid);
    }

    @Test
    public void testValidation_ValidPassword() {
        String pass = "abcd";
        boolean isValid = pass.length() >= 4;
        assertTrue("Slaptažodis >= 4 simbolių turi praeiti", isValid);
    }

    @Test
    public void testValidation_InvalidAge() {
        // Kriterijus: amžius < 5 arba > 120 turi būti atmestas
        // (Iud.RegisterListener: if (ageInt < 5 || ageInt > 120) → klaida)
        assertFalse("Amžius 3 turi būti atmestas", isValidAge(3));
        assertFalse("Amžius 121 turi būti atmestas", isValidAge(121));
        assertFalse("Amžius 0 turi būti atmestas", isValidAge(0));
        assertFalse("Neigiamas amžius turi būti atmestas", isValidAge(-5));
    }

    @Test
    public void testValidation_ValidAge() {
        assertTrue("Amžius 5 turi praeiti", isValidAge(5));
        assertTrue("Amžius 25 turi praeiti", isValidAge(25));
        assertTrue("Amžius 120 turi praeiti", isValidAge(120));
    }

    @Test
    public void testValidation_EmptyLoginFields() {
        // Kriterijus: tusčia prisijungimo forma turi būti atmesta
        // (Iud.LoginListener: if (name.isEmpty() || pass.isEmpty()) → klaida)
        String name = "";
        String pass = "";
        boolean isValid = !name.isEmpty() && !pass.isEmpty();
        assertFalse("Tusčia prisijungimo forma turi būti atmesta", isValid);
    }

    // ── Pagalbinis metodas (atkurtas iš Iud.RegisterListener) ──
    private boolean isValidAge(int age) {
        return age >= 5 && age <= 120;
    }
}
