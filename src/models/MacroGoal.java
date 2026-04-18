package models;
 
public class MacroGoal {
    private int id;
    private int userId;
    private String profileName;
    private double carbPercent;
    private double proteinPercent;
    private double fatPercent;
    private boolean isActive;
 
    public MacroGoal() {}
 
    public MacroGoal(int userId, String profileName, double carbPercent, double proteinPercent, double fatPercent) {
        this.userId = userId;
        this.profileName = profileName;
        this.carbPercent = carbPercent;
        this.proteinPercent = proteinPercent;
        this.fatPercent = fatPercent;
    }
 
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
 
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
 
    public String getProfileName() { return profileName; }
    public void setProfileName(String profileName) { this.profileName = profileName; }
 
    public double getCarbPercent() { return carbPercent; }
    public void setCarbPercent(double carbPercent) { this.carbPercent = carbPercent; }
 
    public double getProteinPercent() { return proteinPercent; }
    public void setProteinPercent(double proteinPercent) { this.proteinPercent = proteinPercent; }
 
    public double getFatPercent() { return fatPercent; }
    public void setFatPercent(double fatPercent) { this.fatPercent = fatPercent; }
 
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
 
    /** Calculate target grams for this macro given a daily calorie goal.
     *  Protein & Carbs = 4 kcal/g, Fat = 9 kcal/g */
    public double getProteinGrams(double dailyCalories) {
        return (dailyCalories * proteinPercent / 100.0) / 4.0;
    }
 
    public double getCarbGrams(double dailyCalories) {
        return (dailyCalories * carbPercent / 100.0) / 4.0;
    }
 
    public double getFatGrams(double dailyCalories) {
        return (dailyCalories * fatPercent / 100.0) / 9.0;
    }
}