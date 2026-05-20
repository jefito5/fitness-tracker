# Fitness Tracker

Darbalaukio programa, padedanti kasdien sekti mitybą, fizinius pratimus ir kūno svorį.
Vartotojas fiksuoja suvartotus patiekalus ir treniruotes — programa automatiškai
skaičiuoja kalorijas, stebi makroelementus ir generuoja svorio kitimo diagramas.

---

## Turinys

- [Komanda](#komanda)
- [Techninė užduotis](#techninė-užduotis)
- [Technologijos](#technologijos)
- [Diegimas ir paleidimas](#diegimas-ir-paleidimas)
- [Naudojimas](#naudojimas)
- [Testavimas](#testavimas)

---

## Komanda
Ant įtampos

| Vardas | GitHub |
|--------|--------|
|Benas   |https://github.com/jefito5     
|Justinas|https://github.com/MrJuztin        
|Eitvydas|https://github.com/qqvis        
|Gustas  |https://github.com/GustasMA      
|Kajus   |https://github.com/ntpqj

---

## Techninė užduotis

Sukurti darbalaukio programą (Java Swing), kuri leistų vartotojui kasdien sekti
mitybą, fizinius pratimus ir kūno svorį — visa tai vienoje vietoje, su automatiniais
skaičiavimais ir vizualine pažangos analize.

### Funkciniai reikalavimai

**Vartotojų valdymas**
- Vartotojas gali susikurti paskyrą nurodydamas vardą, amžių, lytį, ūgį ir dienos kalorijų tikslą
- Prisijungimas atliekamas vartotojo vardu ir slaptažodžiu
- Viename įrenginyje gali egzistuoti kelios atskiros paskyros

**Mitybos sekimas**
- Vartotojas registruoja suvartotus patiekalus nurodydamas gramus ir kalorijų kiekį per 100 g
- Kiekvienas maisto įrašas turi makroelementų duomenis — baltymus, angliavandenis ir riebalus
- Integruota maisto produktų paieška su iš anksto užpildytu katalogu
- Dienos maisto žurnalas rodo suvartotų kalorijų sumą ir makroelementų kiekius

**Fizinio aktyvumo sekimas**
- Treniruotės registruojamos pasirenkant tipą: kardio arba jėgos
- Kardio treniruotei nurodoma trukmė minutėmis; sudegintos kalorijos apskaičiuojamos automatiškai pagal pratimo intensyvumo koeficientą ir vartotojo svorį
- Jėgos treniruotei nurodomas pakartojimų skaičius ir naudojamas svoris
- Prie kiekvieno pratimo rodoma informacija apie aktyvuojamas raumenų grupes

**Kūno matavimai**
- Kūno svoris fiksuojamas du kartus per dieną — ryte ir vakare
- Programa automatiškai apskaičiuoja dienos svorio vidurkį

**Analizė ir ataskaitos**
- Pagrindinis ekranas rodo dienos kalorijų suvestinę: suvartota, sudeginta, balansas, progresas iki tikslo
- Galima peržiūrėti bet kurios ankstesnės dienos žurnalą
- Svorio kitimo diagrama su tendencijų linija pasirinktam laikotarpiui
- Kalorijų skaičiuoklė su rekomenduojamu dienos kalorijų kiekiu ir svorio pokyčio prognoze
- KMI skaičiuoklė su kategorija ir palyginimu su ankstesne reikšme

**Makroelementų tikslai**
- Makroelementų tikslai nustatomi procentais ir saugomi kaip pavadintuosiasi profiliai
- Paruošti šablonai: Balanced, Keto, High-Carb, High-Protein, Cutting, Bulking
- Dienos progresas vaizduojamas žiedinėmis diagramomis

### Nefunkciniai reikalavimai

- Programa veikia Windows operacinėje sistemoje kaip darbalaukio aplikacija
- Visi duomenys saugomi vietinėje duomenų bazėje — interneto ryšys nereikalingas
- Duomenų bazės lentelės sukuriamos automatiškai paleidus programą pirmą kartą
- Programa tikrina vartotojo įvedimus ir rodo klaidos pranešimus neteisingų duomenų atveju
- Architektūra grindžiama objektinio programavimo principais ir Factory projektavimo šablonu

---

## Technologijos

| Technologija    | Paskirtis                     | Pagrindimas                                                                         |
|-----------------|-------------------------------|-------------------------------------------------------------------------------------|
| Java (JDK 17+)  | Pagrindinė programavimo kalba | Platforma nepriklausoma, objektinio programavimo palaikymas, plati ekosistema.      |
| Java Swing      | Grafinė vartotojo sąsaja      | Integruotas Java sprendimas be papildomų priklausomybių.                            |
| JDBC            | Duomenų bazės jungtis         | Standartinis Java API duomenų bazių valdymui; leidžia lengvai keisti DB variklį.   |
| JFreeChart      | Diagramų generavimas          | Brandus įrankis, palaikantis linijines diagramas ir tendencijų linijas.             |
| Factory Pattern | Projektavimo šablonas         | Užtikrina išplečiamumą kuriant skirtingų tipų objektus.                             |
| SQLite          | Duomenų bazė                  | Lengva, serverio nereikalaujanti duomenų bazė, tinkanti vieno vartotojo programai. |
| JUnit 4 / 5     | Testavimas                    | Automatizuoti testai duomenų bazės operacijoms, skaičiavimams ir validavimui.      |

---

## Diegimas ir paleidimas

**Reikalavimai:** Java 17 arba naujesnė versija.

Patikrinti įdiegtą versiją:

```bash
java -version
```

Jei Java neįdiegta: https://adoptium.net

**Paleidimas:**

1. Atsisiųsti projektą:

```bash
git clone https://github.com/jefito5/fitness-tracker.git
```

2. Atidaryti projektą IDE (IntelliJ IDEA arba Eclipse)
3. Paleisti `Main.java`

Duomenų bazė sukuriama automatiškai pirmą kartą paleidus — jokio papildomo diegimo nereikia.

---

## Naudojimas

**Paskyros sukūrimas**

Pirmą kartą paleidus programą rodomas prisijungimo langas. Pasirinkti "Register" ir užpildyti
formą: vardą, slaptažodį, amžių, lytį ir ūgį. Kalorijų tikslas neprivalomas — jį galima
nustatyti vėliau.

**Maisto registravimas**

Paspaudus "Log Meal" atidaroma įvedimo forma. Galima įvesti patiekalo duomenis rankiniu būdu
arba naudotis paieška — pasirinkus produktą iš sąrašo, visi laukai užpildomi automatiškai.

**Treniruočių registravimas**

Paspaudus "Exercises" pasirenkamas treniruotės tipas. Kardio treniruotei nurodoma trukmė
minutėmis — sudegintos kalorijos apskaičiuojamos automatiškai. Jėgos treniruotei įvedamas
pakartojimų skaičius ir naudojamas svoris.

**Svorio matavimas**

Skydelyje "Daily Progress" įvedami ryto ir vakaro svorio matavimai. Dienos vidurkis
apskaičiuojamas automatiškai.

**Istorijos peržiūra**

"Daily Progress" lange datos išskleidžiamajame sąraše matomos tik dienos su įrašais.
Pasirinkus datą ir paspaudus "Show", rodomi tos dienos duomenys.

**Svorio kitimo diagrama**

Skydelyje "Analyse Trends" pasirenkamas laikotarpis ir generuojama diagrama su tendencijų linija.

**Kalorijų skaičiuoklė**

Skydelyje "Calorie Calc" pasirenkamas aktyvumo lygis ir tikslas. Programa apskaičiuoja
rekomenduojamą dienos kalorijų kiekį ir prognozuoja svorio pokytį per savaitę bei mėnesį.

**KMI skaičiuoklė**

Skydelyje "BMI Check" rodomas kūno masės indeksas su kategorija ir palyginimu su ankstesne reikšme.

**Makroelementų tikslai**

Skydelyje "Macro Goals" nustatomi makroelementų tikslai procentais. Galima išsaugoti kelis
profilius ir perjunginėti tarp jų arba naudotis paruoštais šablonais.

---

## Testavimas

Projektas patikrintas 12 automatinių testų klasių — iš viso 92 testai.

**Rezultatas: 92 / 92 testai praeina.**

Testai apima: vartotojų registraciją ir prisijungimą, maisto ir pratimų duomenų bazių
operacijas, svorio matavimus, KMI ir TDEE skaičiavimų tikslumą, makroelementų profilių
valdymą, datos filtravimą žurnale bei pilnus integracinius scenarijus nuo įvedimo formos
iki suvestinės.

Detalūs testų rezultatai su kiekvieno testo aprašymu: [testu rezultatai.docx](testo rezultatai.docx)

Testų paleidimas:

```bash
java -cp lib/*:out org.junit.runner.JUnitCore \
  unitTests.UserAuthTest unitTests.UserDBTest unitTests.MealDBTest \
  unitTests.MealLogDBTest unitTests.ExerciseDBTest unitTests.Exerciselogdbtest \
  unitTests.MetricsDBTest unitTests.BmiCalorieCalcTest unitTests.ValidationUtilsTest \
  unitTests.DateFilteringIntegrationTest unitTests.Mealentryintegrationtest \
  Test.MacroGoalsPanelTest
```




## Notes
This project is developed as part of a university software engineering course.
