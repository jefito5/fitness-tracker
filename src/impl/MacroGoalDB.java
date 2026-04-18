package impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import database.ConnectionFactory;
import models.MacroGoal;

public class MacroGoalDB {

    private Connection conn;

    public MacroGoalDB() {
        conn = ConnectionFactory.getConnection();
        createTableIfNeeded();
    }

    private void createTableIfNeeded() {
        String sql = "CREATE TABLE IF NOT EXISTS macro_goals (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userId INTEGER NOT NULL, " +
                "profileName TEXT NOT NULL, " +
                "carbPercent REAL NOT NULL, " +
                "proteinPercent REAL NOT NULL, " +
                "fatPercent REAL NOT NULL, " +
                "isActive INTEGER DEFAULT 0)";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            // Also create calorie goal table
            stmt.execute("CREATE TABLE IF NOT EXISTS user_calorie_goal (" +
                    "userId INTEGER PRIMARY KEY, dailyCalories REAL DEFAULT 2000)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getCalorieGoal(int userId) {
        String sql = "SELECT dailyCalories FROM user_calorie_goal WHERE userId=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("dailyCalories");
        } catch (SQLException e) { e.printStackTrace(); }
        return 2000;
    }

    public void saveCalorieGoal(int userId, double calories) {
        String sql = "INSERT OR REPLACE INTO user_calorie_goal(userId, dailyCalories) VALUES(?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setDouble(2, calories);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /** Insert a new profile. Returns generated id or 0 on failure. */
    public int insert(MacroGoal g) {
        String sql = "INSERT INTO macro_goals(userId,profileName,carbPercent,proteinPercent,fatPercent,isActive) VALUES(?,?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, g.getUserId());
            ps.setString(2, g.getProfileName());
            ps.setDouble(3, g.getCarbPercent());
            ps.setDouble(4, g.getProteinPercent());
            ps.setDouble(5, g.getFatPercent());
            ps.setInt(6, g.isActive() ? 1 : 0);
            if (ps.executeUpdate() > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /** Update an existing profile (matched by id). */
    public int update(MacroGoal g) {
        String sql = "UPDATE macro_goals SET profileName=?,carbPercent=?,proteinPercent=?,fatPercent=?,isActive=? WHERE id=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, g.getProfileName());
            ps.setDouble(2, g.getCarbPercent());
            ps.setDouble(3, g.getProteinPercent());
            ps.setDouble(4, g.getFatPercent());
            ps.setInt(5, g.isActive() ? 1 : 0);
            ps.setInt(6, g.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(int id) {
        String sql = "DELETE FROM macro_goals WHERE id=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /** Set one profile as active for the user (deactivates all others first). */
    public void setActive(int userId, int profileId) {
        try {
            PreparedStatement ps1 = conn.prepareStatement("UPDATE macro_goals SET isActive=0 WHERE userId=?");
            ps1.setInt(1, userId);
            ps1.executeUpdate();

            PreparedStatement ps2 = conn.prepareStatement("UPDATE macro_goals SET isActive=1 WHERE id=? AND userId=?");
            ps2.setInt(1, profileId);
            ps2.setInt(2, userId);
            ps2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Returns all profiles for this user, active one first. */
    public ArrayList<MacroGoal> getByUser(int userId) {
        ArrayList<MacroGoal> list = new ArrayList<>();
        String sql = "SELECT * FROM macro_goals WHERE userId=? ORDER BY isActive DESC, id ASC";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MacroGoal g = new MacroGoal();
                g.setId(rs.getInt("id"));
                g.setUserId(rs.getInt("userId"));
                g.setProfileName(rs.getString("profileName"));
                g.setCarbPercent(rs.getDouble("carbPercent"));
                g.setProteinPercent(rs.getDouble("proteinPercent"));
                g.setFatPercent(rs.getDouble("fatPercent"));
                g.setActive(rs.getInt("isActive") == 1);
                list.add(g);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** Returns the currently active profile for the user, or null if none. */
    public MacroGoal getActive(int userId) {
        String sql = "SELECT * FROM macro_goals WHERE userId=? AND isActive=1 LIMIT 1";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                MacroGoal g = new MacroGoal();
                g.setId(rs.getInt("id"));
                g.setUserId(rs.getInt("userId"));
                g.setProfileName(rs.getString("profileName"));
                g.setCarbPercent(rs.getDouble("carbPercent"));
                g.setProteinPercent(rs.getDouble("proteinPercent"));
                g.setFatPercent(rs.getDouble("fatPercent"));
                g.setActive(true);
                return g;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}