-- SQLite

-- !!!!!!!!!!!!!! APACIOJE APRASYTA KAIP ISTRINTI ELEMENTA IS LENTELES !!!!!!!!!!!!!!

-- ExerciseDB
CREATE TABLE IF NOT EXISTS exercise (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    ExerciseName TEXT NOT NULL,
    CalorieburnPerMin INTEGER
);
-- ExerciseLogDB
CREATE TABLE IF NOT EXISTS DailyExerciseLog (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    totalCalorieBurn INTEGER,
    exerciseID INTEGER,
    userId INTEGER,
    Date TEXT
);
-- MealDB
CREATE TABLE IF NOT EXISTS meals (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    MealName TEXT NOT NULL,
    CaloriePerGram REAL
);
-- MealLogDB
CREATE TABLE IF NOT EXISTS DailyMealLog (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    totalCalorieIntake INTEGER,
    mealID INTEGER,
    userId INTEGER,
    Date TEXT
);
-- UserDB
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    gender TEXT,
    age INTEGER,
    password TEXT NOT NULL
);
-- WaistDB
CREATE TABLE IF NOT EXISTS waists (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    WaistM REAL,
    WaistE REAL,
    UserId INTEGER,
    Date TEXT,
    average REAL
);
-- WeightDB
CREATE TABLE IF NOT EXISTS weights (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    WeightM REAL,
    WeightE REAL,
    UserId INTEGER,
    Date TEXT,
    average REAL
);

-- norint istrinti kazka is SQL lenteles... kazkoki irasa
-- DELETE FROM (pavadinimas pvz.: meals) WHERE id IN (irasykit id su skliaustais pvz.: (2, 3, 4);
-- pavyzdine eilute: DELETE FROM meals WHERE id IN (1);
-- SVARBU !!! parasius eilute su pelyte ja pazymeti ir paspauti RUN SELECTED QUERY !!!
