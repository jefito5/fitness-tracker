CREATE TABLE IF NOT EXISTS exercise (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    ExerciseName TEXT NOT NULL,
    CalorieburnPerMin REAL,
    workoutType TEXT DEFAULT 'Cardio',
    reps INTEGER DEFAULT 0,
    weightUsed REAL DEFAULT 0,
    muscleGroup TEXT DEFAULT ''
);
CREATE TABLE IF NOT EXISTS preset_exercises (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    ExerciseName TEXT NOT NULL,
    workoutType TEXT NOT NULL,
    muscleGroup TEXT,
    met REAL DEFAULT 0
);
CREATE TABLE IF NOT EXISTS DailyExerciseLog (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    totalCalorieBurn INTEGER,
    exerciseID INTEGER,
    userId INTEGER,
    Date TEXT
);
CREATE TABLE IF NOT EXISTS meals (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    MealName TEXT NOT NULL,
    CaloriePerGram REAL
);
CREATE TABLE IF NOT EXISTS DailyMealLog (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    totalCalorieIntake INTEGER,
    mealID INTEGER,
    userId INTEGER,
    Date TEXT
);
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    gender TEXT,
    age INTEGER,
    password TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS waists (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    WaistM REAL,
    WaistE REAL,
    UserId INTEGER,
    Date TEXT,
    average REAL
);
CREATE TABLE IF NOT EXISTS weights (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    WeightM REAL,
    WeightE REAL,
    UserId INTEGER,
    Date TEXT,
    average REAL
);
CREATE TABLE IF NOT EXISTS foods (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT,
    calories_per_100g REAL,
    protein_per_100g REAL,
    carbs_per_100g REAL,
    fat_per_100g REAL
);