package gui;

import database.ConnectionFactory;
import impl.MealLogDB;
import impl.UserDB;
import models.User;

import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CalorieCalcPanel {

    private JFrame frame;
    private final int userId;
    private JComboBox<String> activityCombo;
    private JLabel lblTdee, lblConsumed, lblBalance, lblWeek, lblMonth, lblSummary;

    public CalorieCalcPanel(int userId) {
        this.userId = userId;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Calorie Deficit/Surplus Calculator");
        frame.setBounds(150, 150, 440, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        UserDB udb = new UserDB();
        User u = udb.getById(userId);

        // Latest weight for this user
        double weight = 0;
        try {
            PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(
                "SELECT WeightM FROM weights WHERE UserId=? AND WeightM > 0 ORDER BY Date DESC LIMIT 1");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) weight = rs.getDouble(1);
        } catch (Exception ignored) {}

        int y = 10;
        lbl(u.getName() + "  |  Age: " + u.getAge() + "  |  " + u.getGender(), 10, y, 400);
        y += 22;
        lbl("Height: " + (u.getHeight() > 0 ? (int) u.getHeight() + " cm" : "N/A (set in profile)")
            + "  |  Weight: " + (weight > 0 ? String.format("%.1f kg", weight) : "N/A (log weight first)"), 10, y, 400);
        y += 30;

        lbl("Activity Level:", 10, y, 120);
        activityCombo = new JComboBox<>(new String[]{"Sedentary", "Light (1-3 days/wk)", "Moderate (3-5 days/wk)", "Active (6-7 days/wk)"});
        activityCombo.setBounds(130, y - 2, 280, 25);
        frame.getContentPane().add(activityCombo);
        y += 35;

        JButton btnCalc = new JButton("CALCULATE");
        btnCalc.setFont(new Font("Verdana", Font.BOLD, 12));
        btnCalc.setBounds(10, y, 150, 28);
        frame.getContentPane().add(btnCalc);
        y += 45;

        JSeparator sep = new JSeparator();
        sep.setBounds(10, y, 400, 2);
        frame.getContentPane().add(sep);
        y += 10;

        lblTdee     = resultRow("TDEE (daily need):", y); y += 26;
        lblConsumed = resultRow("Consumed today:", y);    y += 26;
        lblBalance  = resultRow("Balance:", y);           y += 26;
        lblWeek     = resultRow("Projected 7 days:", y);  y += 26;
        lblMonth    = resultRow("Projected 30 days:", y); y += 30;

        lblSummary = new JLabel(" ");
        lblSummary.setFont(new Font("Verdana", Font.ITALIC, 11));
        lblSummary.setBounds(10, y, 400, 18);
        frame.getContentPane().add(lblSummary);

        double finalWeight = weight;
        btnCalc.addActionListener(e -> calculate(u, finalWeight));

        frame.setVisible(true);
    }

    private void lbl(String text, int x, int y, int w) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Verdana", Font.PLAIN, 11));
        l.setBounds(x, y, w, 18);
        frame.getContentPane().add(l);
    }

    private JLabel resultRow(String key, int y) {
        JLabel k = new JLabel(key);
        k.setFont(new Font("Verdana", Font.PLAIN, 11));
        k.setBounds(10, y, 170, 20);
        frame.getContentPane().add(k);
        JLabel v = new JLabel("—");
        v.setFont(new Font("Verdana", Font.BOLD, 12));
        v.setBounds(185, y, 230, 20);
        frame.getContentPane().add(v);
        return v;
    }

    private void calculate(User u, double weightKg) {
        double height = u.getHeight();
        int age = u.getAge();
        String gender = u.getGender() == null ? "male" : u.getGender().toLowerCase();

        if (height <= 0 || weightKg <= 0 || age <= 0) {
            JOptionPane.showMessageDialog(frame,
                "Please update your Height in your profile and log your Weight first.");
            return;
        }

        // Mifflin-St Jeor BMR
        double bmr = "female".equals(gender)
            ? 10 * weightKg + 6.25 * height - 5 * age - 161
            : 10 * weightKg + 6.25 * height - 5 * age + 5;

        double[] multipliers = {1.2, 1.375, 1.55, 1.725};
        double tdee = bmr * multipliers[activityCombo.getSelectedIndex()];

        double consumed = new MealLogDB().getTodayCalories(userId);
        double balance = consumed - tdee;
        // 7700 kcal ≈ 1 kg of body fat
        double weekKg  = (balance * 7)  / 7700.0;
        double monthKg = (balance * 30) / 7700.0;

        lblTdee.setText(String.format("%.0f kcal", tdee));
        lblConsumed.setText(String.format("%.0f kcal", consumed));

        String dir = balance >= 0 ? "surplus" : "deficit";
        lblBalance.setForeground(balance >= 0 ? new Color(180, 100, 0) : new Color(0, 130, 0));
        lblBalance.setText(String.format("%+.0f kcal  (%s)", balance, dir));

        lblWeek.setForeground(weekKg >= 0 ? new Color(180, 100, 0) : new Color(0, 130, 0));
        lblWeek.setText(String.format("%+.2f kg", weekKg));

        lblMonth.setForeground(monthKg >= 0 ? new Color(180, 100, 0) : new Color(0, 130, 0));
        lblMonth.setText(String.format("%+.2f kg", monthKg));

        String trend = monthKg < 0 ? "lose" : "gain";
        lblSummary.setText(String.format("At this rate you will %s %.2f kg in a month.", trend, Math.abs(monthKg)));
        lblSummary.setForeground(monthKg < 0 ? new Color(0, 130, 0) : new Color(180, 100, 0));
    }
}
