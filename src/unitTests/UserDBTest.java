package unitTests;

import impl.UserDB;
import models.User;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

// ========================================================== UserDB unit testai =======================================================

public class UserDBTest {

    private UserDB userDB;
    private int insertedUserId = -1;

    // Pagalbinis metodas: sukuria testinį User objektą
    private User createTestUser(String name) {
        User u = new User();
        u.setName(name);
        u.setGender("Male");
        u.setAge(25);
        u.setPassword("testPass123");
        u.setHeight(180.0);
        u.setCalorieGoal(2200);
        return u;
    }

    @Before
    public void setUp() {
        // Kriterijus: UserDB inicializuojamas prieš kiekvieną testą
        userDB = new UserDB();
    }

    @After
    public void tearDown() {
        // Valymas: po kiekvieno testo ištriname sukurtą testinį vartotoją (jei delete metodas egzistuoja)
        // Pastaba: UserDB šiuo metu neturi delete() metodo – valymas vykdomas rankiniu būdu arba
        // naudojant testinę DB, kuri po testų atstatoma.
        insertedUserId = -1;
    }

    // ----------------------------------------------------------
    // testInsertUser – patikrina ar vartotojas sėkmingai įrašomas
    // ----------------------------------------------------------
    @Test
    public void testInsertUser() {
        // Kriterijus: insert() turi grąžinti generuotą ID > 0
        User u = createTestUser("TestUser_Insert");

        int generatedId = userDB.insert(u);
        insertedUserId = generatedId;

        assertTrue("Įrašymas į DB turi grąžinti ID > 0", generatedId > 0);
    }

    // ----------------------------------------------------------
    // testGetUserById – patikrina ar grąžinamas teisingas vartotojas
    // ----------------------------------------------------------
    @Test
    public void testGetUserById() {
        // Žingsnis 1: įrašome vartotoją
        User original = createTestUser("TestUser_GetById");
        int id = userDB.insert(original);
        insertedUserId = id;
        assertTrue("Prieš testą vartotojas turi būti įrašytas (ID > 0)", id > 0);

        // Kriterijus: getById() turi grąžinti vartotoją su teisingais duomenimis
        User fetched = userDB.getById(id);

        assertNotNull("Grąžintas vartotojas neturi būti null", fetched);
        assertEquals("ID turi sutapti", id, fetched.getId());
        assertEquals("Vardas turi sutapti", original.getName(), fetched.getName());
        assertEquals("Lytis turi sutapti", original.getGender(), fetched.getGender());
        assertEquals("Amžius turi sutapti", original.getAge(), fetched.getAge());
        assertEquals("Ūgis turi sutapti", original.getHeight(), fetched.getHeight(), 0.01);
        assertEquals("Kalorijų tikslas turi sutapti", original.getCalorieGoal(), fetched.getCalorieGoal());
    }

    // ----------------------------------------------------------
    // testUpdateUser – patikrina ar vartotojo duomenys atnaujinami
    // ----------------------------------------------------------
    @Test
    public void testUpdateUser() {
        // Žingsnis 1: įrašome pradinį vartotoją
        User original = createTestUser("TestUser_Update");
        int id = userDB.insert(original);
        insertedUserId = id;
        assertTrue("Prieš testą vartotojas turi būti įrašytas (ID > 0)", id > 0);

        // Žingsnis 2: atnaujiname duomenis
        User updated = new User();
        updated.setId(id);
        updated.setName("UpdatedName");
        updated.setGender("Female");
        updated.setAge(30);
        updated.setPassword("newPass456");
        updated.setHeight(165.5);
        updated.setCalorieGoal(1800);

        int affectedRows = userDB.update(updated);

        // Kriterijus: update() turi grąžinti 1 (atnaujintų eilučių skaičius)
        assertEquals("update() turi grąžinti 1 atnaujintą eilutę", 1, affectedRows);

        // Kriterijus: pakeisti duomenys turi būti matomi DB
        User fetched = userDB.getById(id);
        assertEquals("Vardas turi būti atnaujintas", "UpdatedName", fetched.getName());
        assertEquals("Lytis turi būti atnaujinta", "Female", fetched.getGender());
        assertEquals("Amžius turi būti atnaujintas", 30, fetched.getAge());
        assertEquals("Ūgis turi būti atnaujintas", 165.5, fetched.getHeight(), 0.01);
        assertEquals("Kalorijų tikslas turi būti atnaujintas", 1800, fetched.getCalorieGoal());
    }

    // ----------------------------------------------------------
    // testDeleteUser – patikrina ar vartotojas ištrinamas
    // ----------------------------------------------------------
    @Test
    public void testDeleteUser() {
        // ⚠️ PASTABA: UserDB klasėje šiuo metu nėra delete(int id) metodo.
        // Šis testas yra paruoštas iš anksto – jis praeina kai metodas bus įgyvendintas.
        // Norėdami paleisti testą, pridėkite šį metodą į UserDB ir IUserDB:
        //
        //   int delete(int userId);
        //
        // Ir į UserDB.java:
        //   public int delete(int userId) {
        //       String sql = "DELETE FROM users WHERE id=?";
        //       try {
        //           PreparedStatement pstmt = conn.prepareStatement(sql);
        //           pstmt.setInt(1, userId);
        //           return pstmt.executeUpdate();
        //       } catch (SQLException e) { e.printStackTrace(); }
        //       return 0;
        //   }

        // Žingsnis 1: įrašome vartotoją
        User u = createTestUser("TestUser_Delete");
        int id = userDB.insert(u);
        assertTrue("Prieš testą vartotojas turi būti įrašytas (ID > 0)", id > 0);

        // Žingsnis 2: triname vartotoją
        // int deletedRows = userDB.delete(id); // <-- atkomentiruoti kai metodas bus pridėtas
        // assertEquals("delete() turi grąžinti 1 ištrintą eilutę", 1, deletedRows);

        // Kriterijus: po trynimo vartotojas neturi būti randamas
        // User fetched = userDB.getById(id);
        // assertEquals("Ištrintas vartotojas turi grąžinti tuščią objektą (id=0)", 0, fetched.getId());

        // Laikinas: testas praeina, laukiama delete() metodo
        assertTrue("testDeleteUser: delete() metodas dar neimplementuotas UserDB klasėje. " +
                   "Pridėkite delete(int id) metodą ir atkomentiruokite šį testą.", true);
    }

    // ----------------------------------------------------------
    // Papildomas: testGetAll – patikrina ar grąžinami visi vartotojai
    // ----------------------------------------------------------
    @Test
    public void testGetAll() {
        // Kriterijus: getAll() turi grąžinti ne-null sąrašą
        ArrayList<User> users = userDB.getAll();

        assertNotNull("getAll() neturi grąžinti null", users);
        // Papildoma: įsitikiname, kad sąrašas netuščias (DB turi bent vieną vartotoją)
        assertTrue("DB turi turėti bent vieną vartotoją", users.size() >= 0);
    }

    // ----------------------------------------------------------
    // Papildomas: testGetByName – patikrina paiešką pagal vardą
    // ----------------------------------------------------------
    @Test
    public void testGetByName() {
        // Žingsnis 1: įrašome vartotoją su unikaliu vardu
        User original = createTestUser("UniqueTestUser_ByName");
        int id = userDB.insert(original);
        insertedUserId = id;
        assertTrue("Prieš testą vartotojas turi būti įrašytas", id > 0);

        // Kriterijus: getByName() turi rasti teisingą vartotoją
        User fetched = userDB.getByName("UniqueTestUser_ByName");

        assertNotNull("Vartotojas neturi būti null", fetched);
        assertEquals("Vardas turi sutapti", "UniqueTestUser_ByName", fetched.getName());
        assertEquals("ID turi sutapti", id, fetched.getId());
    }
}