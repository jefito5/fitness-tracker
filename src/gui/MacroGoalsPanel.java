package gui;

import impl.MacroGoalDB;
import impl.MealLogDB;
import models.MacroGoal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * FT-120: Nutrition Goals by Macronutrient Ratios
 * Shows macro ratio profiles, lets user create/edit/switch profiles,
 * and draws color-coded progress rings for today's intake vs targets.
 */
public class MacroGoalsPanel {

    private JFrame frame;
    private int userId;
    private double dailyCalorieGoal;

    // Profile management
    private JComboBox<String> profileCombo;
    private ArrayList<MacroGoal> profiles;
    private MacroGoal activeProfile;

    // Input fields
    private JTextField txtProfileName;
    private JTextField txtCarb;
    private JTextField txtProtein;
    private JTextField txtFat;
    private JLabel lblSum;

    // Display labels
    private JLabel lblActiveProfile;
    private JLabel lblCalGoal;

    // Ring panel
    private RingPanel ringPanel;

    // Toggle
    private boolean showPercent = false;

    public MacroGoalsPanel(int userId) {
        this.userId = userId;
        loadCalorieGoal();
        initialize();
        loadProfiles();
        refreshRings();
    }

    /** Reads calorie goal from DB (stored as a special profile named "__calorie_goal__"). */
    private void loadCalorieGoal() {
        MacroGoalDB db = new MacroGoalDB();
        dailyCalorieGoal = db.getCalorieGoal(userId);
        if (dailyCalorieGoal <= 0) dailyCalorieGoal = 2000; // sensible default
    }

    private void initialize() {
        frame = new JFrame("Macro Ratio Goals");
        frame.setBounds(100, 100, 950, 720);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        // ── Title ─────────────────────────────────────────────────────────
        JLabel lblTitle = new JLabel("Macronutrient Ratio Goals");
        lblTitle.setFont(new Font("Verdana", Font.BOLD, 18));
        lblTitle.setBounds(20, 10, 400, 30);
        frame.getContentPane().add(lblTitle);

        // ── Daily calorie goal input ───────────────────────────────────────
        JLabel lblGoalLabel = new JLabel("Daily Calorie Goal (kcal):");
        lblGoalLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
        lblGoalLabel.setBounds(20, 50, 180, 20);
        frame.getContentPane().add(lblGoalLabel);

        JTextField txtCalGoal = new JTextField(String.valueOf((int) dailyCalorieGoal));
        txtCalGoal.setBounds(205, 48, 80, 22);
        frame.getContentPane().add(txtCalGoal);

        JButton btnSaveCalGoal = new JButton("Save");
        btnSaveCalGoal.setBounds(295, 48, 60, 22);
        frame.getContentPane().add(btnSaveCalGoal);
        btnSaveCalGoal.addActionListener(e -> {
            try {
                dailyCalorieGoal = Double.parseDouble(txtCalGoal.getText().trim());
                MacroGoalDB db = new MacroGoalDB();
                db.saveCalorieGoal(userId, dailyCalorieGoal);
                refreshRings();
                JOptionPane.showMessageDialog(frame, "Calorie goal saved!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Enter a valid number.");
            }
        });

        lblCalGoal = new JLabel("Goal: " + (int) dailyCalorieGoal + " kcal");
        lblCalGoal.setFont(new Font("Verdana", Font.ITALIC, 11));
        lblCalGoal.setForeground(Color.DARK_GRAY);
        lblCalGoal.setBounds(365, 50, 150, 20);
        frame.getContentPane().add(lblCalGoal);

        // ── Profile selector ──────────────────────────────────────────────
        JLabel lblSelectProfile = new JLabel("Active Profile:");
        lblSelectProfile.setFont(new Font("Verdana", Font.BOLD, 12));
        lblSelectProfile.setBounds(20, 82, 110, 20);
        frame.getContentPane().add(lblSelectProfile);

        profileCombo = new JComboBox<>();
        profileCombo.setBounds(135, 80, 200, 24);
        frame.getContentPane().add(profileCombo);

        JButton btnActivate = new JButton("Set Active");
        btnActivate.setBounds(345, 80, 100, 24);
        frame.getContentPane().add(btnActivate);

        JButton btnDelete = new JButton("Delete");
        btnDelete.setBounds(455, 80, 80, 24);
        frame.getContentPane().add(btnDelete);

        lblActiveProfile = new JLabel("No active profile");
        lblActiveProfile.setFont(new Font("Verdana", Font.ITALIC, 11));
        lblActiveProfile.setForeground(new Color(0, 100, 0));
        lblActiveProfile.setBounds(20, 108, 350, 18);
        frame.getContentPane().add(lblActiveProfile);

        btnActivate.addActionListener(e -> {
            int idx = profileCombo.getSelectedIndex();
            if (idx >= 0 && idx < profiles.size()) {
                MacroGoal selected = profiles.get(idx);
                MacroGoalDB db = new MacroGoalDB();
                db.setActive(userId, selected.getId());
                loadProfiles();
                refreshRings();
                JOptionPane.showMessageDialog(frame, "Profile \"" + selected.getProfileName() + "\" is now active!");
            }
        });

        btnDelete.addActionListener(e -> {
            int idx = profileCombo.getSelectedIndex();
            if (idx >= 0 && idx < profiles.size()) {
                MacroGoal sel = profiles.get(idx);
                int confirm = JOptionPane.showConfirmDialog(frame,
                        "Delete profile \"" + sel.getProfileName() + "\"?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    new MacroGoalDB().delete(sel.getId());
                    loadProfiles();
                    refreshRings();
                }
            }
        });

        // ── Preset templates ───────────────────────────────────────────────
        JLabel lblPresets = new JLabel("Quick presets:");
        lblPresets.setFont(new Font("Verdana", Font.BOLD, 12));
        lblPresets.setBounds(20, 135, 110, 20);
        frame.getContentPane().add(lblPresets);

        String[][] presets = {
            {"Balanced (50/25/25)",    "50","25","25"},
            {"Keto (5/30/65)",         "5","30","65"},
            {"High-Carb (60/20/20)",   "60","20","20"},
            {"High-Protein (30/45/25)","30","45","25"},
            {"Cutting (40/40/20)",     "40","40","20"},
            {"Bulking (55/25/20)",     "55","25","20"},
        };

        int px = 20;
        for (String[] p : presets) {
            JButton btn = new JButton(p[0]);
            btn.setFont(new Font("Tahoma", Font.PLAIN, 10));
            btn.setBounds(px, 158, 160, 22);
            frame.getContentPane().add(btn);
            final String name = p[0].split(" ")[0];
            final double c = Double.parseDouble(p[1]);
            final double pr = Double.parseDouble(p[2]);
            final double f = Double.parseDouble(p[3]);
            btn.addActionListener(e -> {
                txtProfileName.setText(name);
                txtCarb.setText(String.valueOf((int)c));
                txtProtein.setText(String.valueOf((int)pr));
                txtFat.setText(String.valueOf((int)f));
                updateSum();
            });
            px += 165;
            if (px > 640) px = 20; // wrap if needed
        }

        // ── Custom profile creation ────────────────────────────────────────
        JLabel lblCreate = new JLabel("Create / Edit Profile:");
        lblCreate.setFont(new Font("Verdana", Font.BOLD, 12));
        lblCreate.setBounds(20, 192, 200, 20);
        frame.getContentPane().add(lblCreate);

        JLabel lblN = new JLabel("Name:");
        lblN.setBounds(20, 218, 50, 20);
        frame.getContentPane().add(lblN);
        txtProfileName = new JTextField();
        txtProfileName.setBounds(75, 216, 140, 22);
        frame.getContentPane().add(txtProfileName);

        JLabel lblC = new JLabel("Carbs %:");
        lblC.setBounds(225, 218, 65, 20);
        frame.getContentPane().add(lblC);
        txtCarb = new JTextField("50");
        txtCarb.setBounds(292, 216, 50, 22);
        frame.getContentPane().add(txtCarb);

        JLabel lblP = new JLabel("Protein %:");
        lblP.setBounds(355, 218, 72, 20);
        frame.getContentPane().add(lblP);
        txtProtein = new JTextField("25");
        txtProtein.setBounds(430, 216, 50, 22);
        frame.getContentPane().add(txtProtein);

        JLabel lblF = new JLabel("Fat %:");
        lblF.setBounds(492, 218, 45, 20);
        frame.getContentPane().add(lblF);
        txtFat = new JTextField("25");
        txtFat.setBounds(538, 216, 50, 22);
        frame.getContentPane().add(txtFat);

        lblSum = new JLabel("Sum: 100%");
        lblSum.setFont(new Font("Verdana", Font.ITALIC, 11));
        lblSum.setForeground(new Color(0, 128, 0));
        lblSum.setBounds(600, 218, 100, 22);
        frame.getContentPane().add(lblSum);

        // Update sum live
        KeyAdapter ka = new KeyAdapter() {
            public void keyReleased(KeyEvent e) { updateSum(); }
        };
        txtCarb.addKeyListener(ka);
        txtProtein.addKeyListener(ka);
        txtFat.addKeyListener(ka);

        JButton btnSaveProfile = new JButton("Save Profile");
        btnSaveProfile.setBounds(20, 248, 130, 26);
        frame.getContentPane().add(btnSaveProfile);
        btnSaveProfile.addActionListener(e -> saveProfile());

        // ── Toggle view button ─────────────────────────────────────────────
        JButton btnToggle = new JButton("Toggle g / %");
        btnToggle.setBounds(165, 248, 130, 26);
        frame.getContentPane().add(btnToggle);
        btnToggle.addActionListener(e -> {
            showPercent = !showPercent;
            refreshRings();
        });

        // ── Ring panel ─────────────────────────────────────────────────────
        ringPanel = new RingPanel();
        ringPanel.setBounds(20, 285, 900, 400);
        frame.getContentPane().add(ringPanel);

        frame.setVisible(true);
    }

    private void updateSum() {
        try {
            double c = Double.parseDouble(txtCarb.getText().trim());
            double p = Double.parseDouble(txtProtein.getText().trim());
            double f = Double.parseDouble(txtFat.getText().trim());
            double sum = c + p + f;
            lblSum.setText(String.format("Sum: %.0f%%", sum));
            lblSum.setForeground(Math.abs(sum - 100) < 0.01 ? new Color(0, 128, 0) : Color.RED);
        } catch (NumberFormatException ex) {
            lblSum.setText("Sum: ?");
            lblSum.setForeground(Color.RED);
        }
    }

    private void saveProfile() {
        String name = txtProfileName.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter a profile name.");
            return;
        }
        try {
            double c  = Double.parseDouble(txtCarb.getText().trim());
            double p  = Double.parseDouble(txtProtein.getText().trim());
            double f  = Double.parseDouble(txtFat.getText().trim());
            if (Math.abs(c + p + f - 100) > 0.5) {
                JOptionPane.showMessageDialog(frame, "Percentages must sum to 100%!\nCurrent sum: " + (c+p+f));
                return;
            }
            MacroGoal g = new MacroGoal(userId, name, c, p, f);
            MacroGoalDB db = new MacroGoalDB();
            db.insert(g);
            JOptionPane.showMessageDialog(frame, "Profile \"" + name + "\" saved!");
            txtProfileName.setText("");
            loadProfiles();
            refreshRings();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter valid numbers for all percentages.");
        }
    }

    private void loadProfiles() {
        MacroGoalDB db = new MacroGoalDB();
        profiles = db.getByUser(userId);
        profileCombo.removeAllItems();
        activeProfile = null;
        for (MacroGoal g : profiles) {
            profileCombo.addItem(g.getProfileName() + (g.isActive() ? " ✓" : ""));
            if (g.isActive()) activeProfile = g;
        }
        if (activeProfile != null) {
            lblActiveProfile.setText("Active: " + activeProfile.getProfileName() +
                    String.format("  (Carbs %.0f%% / Protein %.0f%% / Fat %.0f%%)",
                            activeProfile.getCarbPercent(),
                            activeProfile.getProteinPercent(),
                            activeProfile.getFatPercent()));
        } else {
            lblActiveProfile.setText("No active profile — create one below or pick a preset.");
        }
    }

    private void refreshRings() {
        MealLogDB mdb = new MealLogDB();
        double[] consumed = mdb.getMacroSummary(userId, String.valueOf(LocalDate.now()));
        ringPanel.update(activeProfile, consumed, dailyCalorieGoal, showPercent);
        ringPanel.repaint();
    }

    // ─── Inner class: ring drawing ─────────────────────────────────────────
    static class RingPanel extends JPanel {

        private MacroGoal profile;
        private double[] consumed = {0, 0, 0};
        private double dailyCal = 2000;
        private boolean showPercent = false;

        void update(MacroGoal p, double[] c, double cal, boolean pct) {
            this.profile = p;
            this.consumed = c;
            this.dailyCal = cal;
            this.showPercent = pct;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (profile == null) {
                g2.setFont(new Font("Verdana", Font.ITALIC, 14));
                g2.setColor(Color.GRAY);
                g2.drawString("Set an active profile to see progress rings.", 60, getHeight() / 2);
                return;
            }

            String[] labels   = {"Carbs", "Protein", "Fat"};
            Color[]  colors   = {new Color(52, 152, 219), new Color(46, 204, 113), new Color(231, 76, 60)};
            double[] targets  = {
                profile.getCarbGrams(dailyCal),
                profile.getProteinGrams(dailyCal),
                profile.getFatGrams(dailyCal)
            };

            int ringSize = 150;
            int stroke   = 18;
            int startX   = 30;
            int centerY  = getHeight() / 2 - 20;

            for (int i = 0; i < 3; i++) {
                int cx = startX + i * (ringSize + 50) + ringSize / 2;
                int cy = centerY;

                double ratio = targets[i] > 0 ? Math.min(consumed[i] / targets[i], 1.0) : 0;
                int arc = (int) Math.round(ratio * 360);

                // Determine ring color based on progress
                Color ringColor;
                if (ratio >= 0.9)       ringColor = new Color(39, 174, 96);   // green
                else if (ratio >= 0.7)  ringColor = new Color(243, 156, 18);  // yellow
                else                    ringColor = new Color(231, 76, 60);    // red

                // Background ring
                g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
                g2.setColor(new Color(220, 220, 220));
                g2.drawOval(cx - ringSize / 2, cy - ringSize / 2, ringSize, ringSize);

                // Progress arc
                g2.setColor(ringColor);
                g2.drawArc(cx - ringSize / 2, cy - ringSize / 2, ringSize, ringSize, 90, -arc);

                // Centre text
                g2.setFont(new Font("Verdana", Font.BOLD, 13));
                g2.setColor(Color.DARK_GRAY);
                String centreText;
                if (showPercent) {
                    centreText = String.format("%.0f%%", ratio * 100);
                } else {
                    centreText = String.format("%.0fg", consumed[i]);
                }
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(centreText, cx - fm.stringWidth(centreText) / 2, cy + 5);

                // Label below ring
                g2.setFont(new Font("Verdana", Font.BOLD, 12));
                g2.setColor(colors[i]);
                g2.drawString(labels[i], cx - fm.stringWidth(labels[i]) / 2, cy + ringSize / 2 + 22);

                // Target line
                g2.setFont(new Font("Verdana", Font.PLAIN, 11));
                g2.setColor(Color.GRAY);
                String targetStr = showPercent
                        ? String.format("target: %.0f%%", profile == null ? 0 :
                            i == 0 ? profile.getCarbPercent() :
                            i == 1 ? profile.getProteinPercent() : profile.getFatPercent())
                        : String.format("target: %.0fg", targets[i]);
                g2.drawString(targetStr, cx - fm.stringWidth(targetStr) / 2, cy + ringSize / 2 + 38);

                // Remaining
                double remaining = targets[i] - consumed[i];
                String remStr = remaining > 0
                        ? String.format("%.0fg left", remaining)
                        : String.format("+%.0fg over", -remaining);
                g2.setColor(remaining > 0 ? Color.DARK_GRAY : new Color(231, 76, 60));
                g2.drawString(remStr, cx - fm.stringWidth(remStr) / 2, cy + ringSize / 2 + 53);
            }

            // Pie chart (simple) on the right
            drawPieChart(g2, profile, startX + 3 * (ringSize + 50) + 30, centerY - 60);
        }

        private void drawPieChart(Graphics2D g2, MacroGoal p, int x, int y) {
            int size = 130;
            Color[] pc = {new Color(52, 152, 219), new Color(46, 204, 113), new Color(231, 76, 60)};
            double[] pcts = {p.getCarbPercent(), p.getProteinPercent(), p.getFatPercent()};
            String[] names = {"Carbs", "Protein", "Fat"};

            int start = 0;
            for (int i = 0; i < 3; i++) {
                int arc = (int) Math.round(pcts[i] / 100.0 * 360);
                g2.setColor(pc[i]);
                g2.fillArc(x, y, size, size, start, arc);
                start += arc;
            }

            // Legend
            g2.setFont(new Font("Verdana", Font.PLAIN, 11));
            for (int i = 0; i < 3; i++) {
                g2.setColor(pc[i]);
                g2.fillRect(x, y + size + 10 + i * 18, 12, 12);
                g2.setColor(Color.DARK_GRAY);
                g2.drawString(String.format("%s: %.0f%%", names[i], pcts[i]), x + 18, y + size + 21 + i * 18);
            }

            g2.setFont(new Font("Verdana", Font.BOLD, 11));
            g2.setColor(Color.DARK_GRAY);
            g2.drawString("Macro Split", x + 15, y - 5);
        }
    }
}