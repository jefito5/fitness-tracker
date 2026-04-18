package gui;

import impl.UserDB;
import impl.WeightDB;
import models.User;
import models.Weight;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * FT-122: BMI Calculation & Body Metrics
 * Displays BMI based on user's height + latest logged weight.
 * Launched as a small panel/dialog from MealIUD or trackProgress.
 */
public class BmiPanel {

    private JFrame frame;
    private int userId;

    private JLabel lblBmiValue;
    private JLabel lblBmiCategory;
    private JLabel lblBmiColor;
    private JLabel lblWeightUsed;
    private JLabel lblHeightUsed;
    private JLabel lblPrevBmi;
    private JTextField txtHeight;

    public BmiPanel(int userId) {
        this.userId = userId;
        initialize();
        refreshBmi();
    }

    private void initialize() {
        frame = new JFrame("BMI & Body Metrics");
        frame.setBounds(100, 100, 420, 380);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblTitle = new JLabel("BMI & Body Metrics");
        lblTitle.setFont(new Font("Verdana", Font.BOLD, 16));
        lblTitle.setBounds(20, 10, 300, 28);
        frame.getContentPane().add(lblTitle);

        // ── Height update section ──────────────────────────────────────────
        JLabel lblHLabel = new JLabel("Your Height (cm):");
        lblHLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
        lblHLabel.setBounds(20, 50, 140, 20);
        frame.getContentPane().add(lblHLabel);

        UserDB udb = new UserDB();
        User u = udb.getById(userId);
        double currentHeight = (u != null) ? u.getHeight() : 0;

        txtHeight = new JTextField(currentHeight > 0 ? String.valueOf((int) currentHeight) : "");
        txtHeight.setBounds(165, 48, 80, 22);
        frame.getContentPane().add(txtHeight);

        JButton btnSaveHeight = new JButton("Update");
        btnSaveHeight.setBounds(255, 48, 80, 22);
        frame.getContentPane().add(btnSaveHeight);
        btnSaveHeight.addActionListener(e -> saveHeight());

        // ── BMI result card ────────────────────────────────────────────────
        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        card.setBackground(new Color(245, 245, 245));
        card.setBounds(20, 85, 365, 200);
        frame.getContentPane().add(card);

        JLabel lblBmiTitle = new JLabel("Your BMI");
        lblBmiTitle.setFont(new Font("Verdana", Font.BOLD, 13));
        lblBmiTitle.setBounds(10, 8, 200, 20);
        card.add(lblBmiTitle);

        lblBmiValue = new JLabel("—");
        lblBmiValue.setFont(new Font("Verdana", Font.BOLD, 46));
        lblBmiValue.setForeground(new Color(52, 152, 219));
        lblBmiValue.setBounds(10, 35, 200, 65);
        card.add(lblBmiValue);

        lblBmiCategory = new JLabel("No data yet");
        lblBmiCategory.setFont(new Font("Verdana", Font.BOLD, 15));
        lblBmiCategory.setBounds(10, 100, 250, 22);
        card.add(lblBmiCategory);

        lblWeightUsed = new JLabel("Weight: —");
        lblWeightUsed.setFont(new Font("Verdana", Font.PLAIN, 11));
        lblWeightUsed.setForeground(Color.DARK_GRAY);
        lblWeightUsed.setBounds(10, 128, 170, 18);
        card.add(lblWeightUsed);

        lblHeightUsed = new JLabel("Height: —");
        lblHeightUsed.setFont(new Font("Verdana", Font.PLAIN, 11));
        lblHeightUsed.setForeground(Color.DARK_GRAY);
        lblHeightUsed.setBounds(10, 148, 170, 18);
        card.add(lblHeightUsed);

        lblPrevBmi = new JLabel("Previous: —");
        lblPrevBmi.setFont(new Font("Verdana", Font.ITALIC, 11));
        lblPrevBmi.setForeground(Color.GRAY);
        lblPrevBmi.setBounds(10, 168, 340, 18);
        card.add(lblPrevBmi);

        // BMI scale diagram on the right side of the card
        BmiScale scale = new BmiScale();
        scale.setBounds(230, 10, 125, 175);
        card.add(scale);

        // ── Info label ─────────────────────────────────────────────────────
        JLabel lblInfo = new JLabel("BMI = weight(kg) ÷ height(m)²  |  Log weight first on Track screen.");
        lblInfo.setFont(new Font("Verdana", Font.ITALIC, 10));
        lblInfo.setForeground(Color.GRAY);
        lblInfo.setBounds(20, 295, 380, 16);
        frame.getContentPane().add(lblInfo);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setBounds(20, 315, 90, 24);
        frame.getContentPane().add(btnRefresh);
        btnRefresh.addActionListener(e -> refreshBmi());

        frame.setVisible(true);
    }

    private void saveHeight() {
        try {
            double h = Double.parseDouble(txtHeight.getText().trim());
            if (h < 50 || h > 280) {
                JOptionPane.showMessageDialog(frame, "Please enter a realistic height (50–280 cm).");
                return;
            }
            UserDB udb = new UserDB();
            User u = udb.getById(userId);
            if (u != null) {
                u.setHeight(h);
                udb.update(u);
                JOptionPane.showMessageDialog(frame, "Height updated!");
                refreshBmi();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid number.");
        }
    }

    private void refreshBmi() {
        UserDB udb = new UserDB();
        User u = udb.getById(userId);
        if (u == null) return;

        double heightCm = u.getHeight();

        // Get latest weight from DB
        WeightDB wdb = new WeightDB();
        ArrayList<Double> weights = wdb.getStartEndForUser(userId);

        if (heightCm <= 0) {
            lblBmiCategory.setText("Enter your height above");
            lblBmiCategory.setForeground(Color.GRAY);
            lblBmiValue.setText("—");
            return;
        }

        if (weights.isEmpty()) {
            lblBmiCategory.setText("Log your weight first");
            lblBmiCategory.setForeground(Color.GRAY);
            lblBmiValue.setText("—");
            lblHeightUsed.setText("Height: " + (int) heightCm + " cm");
            return;
        }

        double latestWeight = weights.get(weights.size() - 1);
        double heightM = heightCm / 100.0;
        double bmi = latestWeight / (heightM * heightM);

        lblBmiValue.setText(String.format("%.1f", bmi));
        lblWeightUsed.setText("Weight: " + String.format("%.1f", latestWeight) + " kg");
        lblHeightUsed.setText("Height: " + (int) heightCm + " cm");

        // Category + color
        String category;
        Color catColor;
        if (bmi < 18.5) {
            category = "Underweight";
            catColor = new Color(52, 152, 219); // blue
        } else if (bmi < 25.0) {
            category = "Normal weight";
            catColor = new Color(39, 174, 96);  // green
        } else if (bmi < 30.0) {
            category = "Overweight";
            catColor = new Color(243, 156, 18); // orange
        } else {
            category = "Obese";
            catColor = new Color(231, 76, 60);  // red
        }
        lblBmiCategory.setText(category);
        lblBmiCategory.setForeground(catColor);
        lblBmiValue.setForeground(catColor);

        // Previous BMI comparison
        if (weights.size() >= 2) {
            double prevWeight = weights.get(weights.size() - 2);
            double prevBmi = prevWeight / (heightM * heightM);
            double diff = bmi - prevBmi;
            String arrow = diff > 0 ? "▲" : (diff < 0 ? "▼" : "=");
            lblPrevBmi.setText(String.format("Previous BMI: %.1f  %s %.2f", prevBmi, arrow, Math.abs(diff)));
            lblPrevBmi.setForeground(diff < 0 && bmi >= 18.5 ? new Color(39, 174, 96) : Color.GRAY);
        } else {
            lblPrevBmi.setText("Previous: not enough data");
        }
    }

    /** Simple BMI category scale drawn as colored bands. */
    static class BmiScale extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = 20, x = 10;
            int totalH = getHeight() - 40;

            // 4 bands: Underweight, Normal, Overweight, Obese
            Color[] colors = {
                new Color(52, 152, 219),
                new Color(39, 174, 96),
                new Color(243, 156, 18),
                new Color(231, 76, 60)
            };
            String[] labels = {"Obese\n≥30", "Over\n25", "Normal\n18.5", "Under"};
            int bandH = totalH / 4;

            for (int i = 0; i < 4; i++) {
                g2.setColor(colors[3 - i]);
                g2.fillRect(x, i * bandH + 10, w, bandH);
                g2.setColor(Color.DARK_GRAY);
                g2.setFont(new Font("Tahoma", Font.PLAIN, 9));
                String[] parts = labels[i].split("\n");
                for (int j = 0; j < parts.length; j++) {
                    g2.drawString(parts[j], x + w + 4, i * bandH + 20 + j * 11);
                }
            }
        }
    }
}