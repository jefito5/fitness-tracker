package gui;

import gui.components.CardPanel;
import gui.components.RoundedButton;
import gui.components.StyledTextField;
import gui.components.UITheme;
import impl.MacroGoalDB;
import impl.MealLogDB;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import models.MacroGoal;

/**
 * UI-9: Redesigned Macro Ratio Goals panel.
 *
 * Sectioned card layout with:
 *  - Active profile name shown prominently in the header
 *  - Daily calorie goal section
 *  - Profile selector (set active / delete)
 *  - Quick-preset buttons
 *  - Create / Edit profile with three-column %% inputs and live sum validation
 *  - Theme-coloured progress rings with bold percentage labels
 *
 * FT-120: Nutrition Goals by Macronutrient Ratios.
 */
public class MacroGoalsPanel {

    private JFrame frame;
    private final int userId;
    private double dailyCalorieGoal;

    // Profile management
    private JComboBox<String> profileCombo;
    private ArrayList<MacroGoal> profiles;
    private MacroGoal activeProfile;

    // Inputs
    private StyledTextField txtProfileName;
    private StyledTextField txtCarb;
    private StyledTextField txtProtein;
    private StyledTextField txtFat;
    private StyledTextField txtCalGoal;
    private JLabel lblSum;

    // Header summary
    private JLabel lblActiveProfileHeader;

    // Ring panel
    private RingPanel ringPanel;
    private boolean showPercent = false;

    public MacroGoalsPanel(int userId) {
        this.userId = userId;
        loadCalorieGoal();
        initialize();
        loadProfiles();
        refreshRings();
    }

    /** Reads calorie goal from DB (stored as a special profile). */
    private void loadCalorieGoal() {
        MacroGoalDB db = new MacroGoalDB();
        dailyCalorieGoal = db.getCalorieGoal(userId);
        if (dailyCalorieGoal <= 0) dailyCalorieGoal = 2000;
    }

    private void initialize() {
        frame = new JFrame("Macro Ratio Goals");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setMinimumSize(new Dimension(960, 720));
        frame.setSize(1040, 780);
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(0, UITheme.SPACE_LG));
        root.setBackground(UITheme.BACKGROUND);
        root.setBorder(UITheme.padding(UITheme.SPACE_LG));
        frame.setContentPane(root);

        root.add(buildHeader(), BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);

        JPanel topRow = new JPanel(new GridLayout(1, 2, UITheme.SPACE_LG, 0));
        topRow.setOpaque(false);
        topRow.add(buildCalorieGoalCard());
        topRow.add(buildProfileSelectorCard());
        body.add(topRow);

        body.add(Box.createVerticalStrut(UITheme.SPACE_LG));
        body.add(buildPresetCard());

        body.add(Box.createVerticalStrut(UITheme.SPACE_LG));
        body.add(buildCreateEditCard());

        body.add(Box.createVerticalStrut(UITheme.SPACE_LG));
        body.add(buildRingsCard());

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        root.add(scroll, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    // ════════════════════════════════════════════════════════════════════
    //  HEADER — title + active profile + calorie goal summary
    // ════════════════════════════════════════════════════════════════════
    private JComponent buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titleBlock = new JPanel();
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.setOpaque(false);

        JLabel title = new JLabel("Macro Ratio Goals");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblActiveProfileHeader = new JLabel("No active profile");
        lblActiveProfileHeader.setFont(UITheme.FONT_BODY_BOLD);
        lblActiveProfileHeader.setForeground(UITheme.PRIMARY);
        lblActiveProfileHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblActiveProfileHeader.setBorder(UITheme.padding(2, 0, 0, 0));

        titleBlock.add(title);
        titleBlock.add(lblActiveProfileHeader);
        header.add(titleBlock, BorderLayout.WEST);
        return header;
    }

    // ── CARD: Daily calorie goal ────────────────────────────────────────
    private JComponent buildCalorieGoalCard() {
        CardPanel card = new CardPanel(true);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(UITheme.padding(UITheme.SPACE_LG));

        card.add(sectionHeader("Daily calorie goal", "Used to translate ratios into target grams"));
        card.add(Box.createVerticalStrut(UITheme.SPACE_MD));

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, UITheme.SPACE_SM, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtCalGoal = new StyledTextField("kcal");
        txtCalGoal.setText(String.valueOf((int) dailyCalorieGoal));
        txtCalGoal.setPreferredSize(new Dimension(120, 32));

        RoundedButton btnSave = new RoundedButton("Save", RoundedButton.Variant.PRIMARY);
        btnSave.setBorder(UITheme.padding(7, 16));
        btnSave.addActionListener(e -> {
            try {
                dailyCalorieGoal = Double.parseDouble(txtCalGoal.getText().trim());
                MacroGoalDB db = new MacroGoalDB();
                db.saveCalorieGoal(userId, dailyCalorieGoal);
                refreshRings();
                updateActiveHeader();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Enter a valid number.");
            }
        });

        row.add(txtCalGoal);
        row.add(btnSave);
        card.add(row);
        return card;
    }

    // ── CARD: Profile selector ──────────────────────────────────────────
    private JComponent buildProfileSelectorCard() {
        CardPanel card = new CardPanel(true);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(UITheme.padding(UITheme.SPACE_LG));

        card.add(sectionHeader("Profiles", "Switch between or remove saved ratio profiles"));
        card.add(Box.createVerticalStrut(UITheme.SPACE_MD));

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, UITheme.SPACE_SM, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        profileCombo = new JComboBox<>();
        profileCombo.setFont(UITheme.FONT_BODY);
        profileCombo.setPreferredSize(new Dimension(220, 32));
        row.add(profileCombo);

        RoundedButton btnActivate = new RoundedButton("Set Active", RoundedButton.Variant.PRIMARY);
        btnActivate.setBorder(UITheme.padding(7, 16));
        btnActivate.addActionListener(e -> {
            int idx = profileCombo.getSelectedIndex();
            if (idx >= 0 && idx < profiles.size()) {
                MacroGoal selected = profiles.get(idx);
                MacroGoalDB db = new MacroGoalDB();
                db.setActive(userId, selected.getId());
                loadProfiles();
                refreshRings();
            }
        });

        RoundedButton btnDelete = new RoundedButton("Delete", RoundedButton.Variant.SECONDARY);
        btnDelete.setBorder(UITheme.padding(7, 16));
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

        row.add(btnActivate);
        row.add(btnDelete);
        card.add(row);
        return card;
    }

    // ── CARD: Quick presets ─────────────────────────────────────────────
    private JComponent buildPresetCard() {
        CardPanel card = new CardPanel(true);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(UITheme.padding(UITheme.SPACE_LG));

        card.add(sectionHeader("Quick presets", "Tap to fill the form below"));
        card.add(Box.createVerticalStrut(UITheme.SPACE_MD));

        JPanel grid = new JPanel(new GridLayout(2, 3, UITheme.SPACE_SM, UITheme.SPACE_SM));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[][] presets = {
                {"Balanced (50/25/25)",     "50", "25", "25"},
                {"Keto (5/30/65)",          "5",  "30", "65"},
                {"High-Carb (60/20/20)",    "60", "20", "20"},
                {"High-Protein (30/45/25)", "30", "45", "25"},
                {"Cutting (40/40/20)",      "40", "40", "20"},
                {"Bulking (55/25/20)",      "55", "25", "20"},
        };

        for (String[] p : presets) {
            RoundedButton btn = new RoundedButton(p[0], RoundedButton.Variant.OUTLINE);
            btn.setBorder(UITheme.padding(8, 12));
            final String name = p[0].split(" ")[0];
            final double c  = Double.parseDouble(p[1]);
            final double pr = Double.parseDouble(p[2]);
            final double f  = Double.parseDouble(p[3]);
            btn.addActionListener(e -> {
                txtProfileName.setText(name);
                txtCarb.setText(String.valueOf((int) c));
                txtProtein.setText(String.valueOf((int) pr));
                txtFat.setText(String.valueOf((int) f));
                updateSum();
            });
            grid.add(btn);
        }

        card.add(grid);
        return card;
    }

    // ── CARD: Create / edit profile (3-column percentages + live sum) ───
    private JComponent buildCreateEditCard() {
        CardPanel card = new CardPanel(true);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(UITheme.padding(UITheme.SPACE_LG));

        card.add(sectionHeader("Create / edit profile", "Carb + Protein + Fat must total 100%"));
        card.add(Box.createVerticalStrut(UITheme.SPACE_MD));

        JPanel nameRow = new JPanel(new FlowLayout(FlowLayout.LEFT, UITheme.SPACE_SM, 0));
        nameRow.setOpaque(false);
        nameRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        nameRow.add(fieldLabel("Profile name"));
        txtProfileName = new StyledTextField("e.g. My Cut");
        txtProfileName.setPreferredSize(new Dimension(240, 32));
        nameRow.add(txtProfileName);
        card.add(nameRow);

        card.add(Box.createVerticalStrut(UITheme.SPACE_MD));

        // Three-column percentage row
        JPanel pctRow = new JPanel(new GridLayout(1, 3, UITheme.SPACE_LG, 0));
        pctRow.setOpaque(false);
        pctRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtCarb    = new StyledTextField("%");
        txtProtein = new StyledTextField("%");
        txtFat     = new StyledTextField("%");
        txtCarb.setText("50");
        txtProtein.setText("25");
        txtFat.setText("25");

        pctRow.add(percentColumn("Carbs %",   RingPanel.MACRO_CARBS,   txtCarb));
        pctRow.add(percentColumn("Protein %", RingPanel.MACRO_PROTEIN, txtProtein));
        pctRow.add(percentColumn("Fat %",     RingPanel.MACRO_FAT,     txtFat));
        card.add(pctRow);

        card.add(Box.createVerticalStrut(UITheme.SPACE_SM));

        lblSum = new JLabel("Sum: 100% ✓");
        lblSum.setFont(UITheme.FONT_BODY_BOLD);
        lblSum.setForeground(UITheme.SUCCESS);
        lblSum.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblSum);

        // Live sum updates via DocumentListener (works for typing + paste)
        DocumentListener dl = new DocumentListener() {
            public void insertUpdate (DocumentEvent e) { updateSum(); }
            public void removeUpdate (DocumentEvent e) { updateSum(); }
            public void changedUpdate(DocumentEvent e) { updateSum(); }
        };
        txtCarb.getDocument().addDocumentListener(dl);
        txtProtein.getDocument().addDocumentListener(dl);
        txtFat.getDocument().addDocumentListener(dl);

        card.add(Box.createVerticalStrut(UITheme.SPACE_MD));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, UITheme.SPACE_SM, 0));
        actions.setOpaque(false);
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedButton btnSave = new RoundedButton("Save Profile", RoundedButton.Variant.PRIMARY);
        btnSave.addActionListener(e -> saveProfile());
        actions.add(btnSave);

        RoundedButton btnToggle = new RoundedButton("Toggle g / %", RoundedButton.Variant.OUTLINE);
        btnToggle.addActionListener(e -> {
            showPercent = !showPercent;
            refreshRings();
        });
        actions.add(btnToggle);

        card.add(actions);
        return card;
    }

    private JComponent percentColumn(String label, Color accent, StyledTextField field) {
        JPanel col = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setOpaque(false);

        JLabel l = new JLabel(label);
        l.setFont(UITheme.FONT_SUBHEADING);
        l.setForeground(accent);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        col.add(l);
        col.add(Box.createVerticalStrut(UITheme.SPACE_XS));
        col.add(field);
        return col;
    }

    // ── CARD: Rings ─────────────────────────────────────────────────────
    private JComponent buildRingsCard() {
        CardPanel card = new CardPanel(true);
        card.setLayout(new BorderLayout(0, UITheme.SPACE_MD));
        card.setBorder(UITheme.padding(UITheme.SPACE_LG));

        card.add(sectionHeader("Today's progress", "Coloured rings show consumed vs target per macro"), BorderLayout.NORTH);

        ringPanel = new RingPanel();
        ringPanel.setPreferredSize(new Dimension(900, 360));
        ringPanel.setOpaque(false);
        card.add(ringPanel, BorderLayout.CENTER);

        return card;
    }

    // ════════════════════════════════════════════════════════════════════
    //  Helpers
    // ════════════════════════════════════════════════════════════════════
    private JComponent sectionHeader(String title, String subtitle) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel t = new JLabel(title);
        t.setFont(UITheme.FONT_HEADING);
        t.setForeground(UITheme.TEXT_PRIMARY);
        t.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel s = new JLabel(subtitle);
        s.setFont(UITheme.FONT_CAPTION);
        s.setForeground(UITheme.TEXT_MUTED);
        s.setAlignmentX(Component.LEFT_ALIGNMENT);
        s.setBorder(UITheme.padding(2, 0, 0, 0));

        p.add(t);
        p.add(s);
        return p;
    }

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UITheme.FONT_SUBHEADING);
        l.setForeground(UITheme.TEXT_SECONDARY);
        l.setPreferredSize(new Dimension(120, 32));
        return l;
    }

    private void updateSum() {
        try {
            double c = Double.parseDouble(txtCarb.getText().trim());
            double p = Double.parseDouble(txtProtein.getText().trim());
            double f = Double.parseDouble(txtFat.getText().trim());
            double sum = c + p + f;
            boolean valid = Math.abs(sum - 100) < 0.01;
            lblSum.setText(String.format("Sum: %.0f%% %s", sum, valid ? "✓" : ""));
            lblSum.setForeground(valid ? UITheme.SUCCESS : UITheme.DANGER);
        } catch (NumberFormatException ex) {
            lblSum.setText("Sum: ?");
            lblSum.setForeground(UITheme.DANGER);
        }
    }

    private void saveProfile() {
        String name = txtProfileName.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter a profile name.");
            return;
        }
        try {
            double c = Double.parseDouble(txtCarb.getText().trim());
            double p = Double.parseDouble(txtProtein.getText().trim());
            double f = Double.parseDouble(txtFat.getText().trim());
            if (Math.abs(c + p + f - 100) > 0.5) {
                JOptionPane.showMessageDialog(frame, "Percentages must sum to 100%!\nCurrent sum: " + (c + p + f));
                return;
            }
            MacroGoal g = new MacroGoal(userId, name, c, p, f);
            MacroGoalDB db = new MacroGoalDB();
            db.insert(g);
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
        updateActiveHeader();
    }

    private void updateActiveHeader() {
        if (activeProfile != null) {
            lblActiveProfileHeader.setText(String.format(
                    "Active: %s  •  %.0f / %.0f / %.0f  •  Daily goal: %d kcal",
                    activeProfile.getProfileName(),
                    activeProfile.getCarbPercent(),
                    activeProfile.getProteinPercent(),
                    activeProfile.getFatPercent(),
                    (int) dailyCalorieGoal));
            lblActiveProfileHeader.setForeground(UITheme.PRIMARY);
        } else {
            lblActiveProfileHeader.setText("No active profile — pick a preset or create one below.");
            lblActiveProfileHeader.setForeground(UITheme.TEXT_MUTED);
        }
    }

    private void refreshRings() {
        MealLogDB mdb = new MealLogDB();
        double[] consumed = mdb.getMacroSummary(userId, String.valueOf(LocalDate.now()));
        ringPanel.update(activeProfile, consumed, dailyCalorieGoal, showPercent);
        ringPanel.repaint();
        updateActiveHeader();
    }

    // ════════════════════════════════════════════════════════════════════
    //  RING PANEL — themed redesign
    // ════════════════════════════════════════════════════════════════════
    static class RingPanel extends JPanel {

        // Macro accent colours — pulled from UITheme for palette coherence.
        static final Color MACRO_CARBS   = UITheme.INFO;     // blue
        static final Color MACRO_PROTEIN = UITheme.SUCCESS;  // green
        static final Color MACRO_FAT     = UITheme.WARNING;  // amber

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
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            if (profile == null) {
                g2.setFont(UITheme.FONT_BODY);
                g2.setColor(UITheme.TEXT_MUTED);
                String msg = "Set an active profile to see progress rings.";
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2, getHeight() / 2);
                return;
            }

            String[] labels  = {"Carbs", "Protein", "Fat"};
            Color[]  accents = {MACRO_CARBS, MACRO_PROTEIN, MACRO_FAT};
            double[] targets = {
                    profile.getCarbGrams(dailyCal),
                    profile.getProteinGrams(dailyCal),
                    profile.getFatGrams(dailyCal)
            };

            int ringSize = 160;
            int stroke   = 20;
            int gap      = 60;
            int totalW   = 3 * ringSize + 2 * gap;
            int startX   = Math.max(20, (getWidth() - totalW - 240) / 2); // leave room for legend on right
            int centerY  = Math.max(ringSize / 2 + 30, getHeight() / 2 - 20);

            for (int i = 0; i < 3; i++) {
                int cx = startX + i * (ringSize + gap) + ringSize / 2;
                int cy = centerY;

                double ratio = targets[i] > 0 ? Math.min(consumed[i] / targets[i], 1.0) : 0;
                int arc = (int) Math.round(ratio * 360);

                // Background ring
                g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
                g2.setColor(UITheme.BORDER);
                g2.drawOval(cx - ringSize / 2, cy - ringSize / 2, ringSize, ringSize);

                // Progress arc — thresholded against the macro accent
                Color ringColor;
                if (ratio >= 0.9)      ringColor = UITheme.SUCCESS;
                else if (ratio >= 0.7) ringColor = UITheme.WARNING;
                else                   ringColor = accents[i];
                g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.setColor(ringColor);
                g2.drawArc(cx - ringSize / 2, cy - ringSize / 2, ringSize, ringSize, 90, -arc);

                // Centre value — big &amp; bold (acceptance criterion: bold percentage labels)
                String centreText = showPercent
                        ? String.format("%.0f%%", ratio * 100)
                        : String.format("%.0fg", consumed[i]);
                g2.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 24));
                g2.setColor(UITheme.TEXT_PRIMARY);
                FontMetrics fmBig = g2.getFontMetrics();
                g2.drawString(centreText, cx - fmBig.stringWidth(centreText) / 2, cy + 8);

                // Sub-label inside ring (always shows the alternate format)
                String sub = showPercent
                        ? String.format("%.0fg", consumed[i])
                        : String.format("%.0f%%", ratio * 100);
                g2.setFont(UITheme.FONT_CAPTION);
                g2.setColor(UITheme.TEXT_MUTED);
                FontMetrics fmSub = g2.getFontMetrics();
                g2.drawString(sub, cx - fmSub.stringWidth(sub) / 2, cy + 26);

                // Macro label below ring
                g2.setFont(UITheme.FONT_HEADING);
                g2.setColor(accents[i]);
                FontMetrics fmLbl = g2.getFontMetrics();
                g2.drawString(labels[i], cx - fmLbl.stringWidth(labels[i]) / 2, cy + ringSize / 2 + 26);

                // Target / remaining caption
                g2.setFont(UITheme.FONT_CAPTION);
                g2.setColor(UITheme.TEXT_SECONDARY);
                String targetStr = showPercent
                        ? String.format("target %.0f%%",
                            i == 0 ? profile.getCarbPercent() :
                            i == 1 ? profile.getProteinPercent() : profile.getFatPercent())
                        : String.format("target %.0fg", targets[i]);
                FontMetrics fmCap = g2.getFontMetrics();
                g2.drawString(targetStr, cx - fmCap.stringWidth(targetStr) / 2, cy + ringSize / 2 + 44);

                double remaining = targets[i] - consumed[i];
                String remStr = remaining > 0
                        ? String.format("%.0fg left", remaining)
                        : String.format("%.0fg over", -remaining);
                g2.setColor(remaining > 0 ? UITheme.TEXT_SECONDARY : UITheme.DANGER);
                g2.drawString(remStr, cx - fmCap.stringWidth(remStr) / 2, cy + ringSize / 2 + 60);
            }

            // Pie chart legend on the right
            drawPieChart(g2, profile, startX + totalW + 30, centerY - ringSize / 2 + 10);
        }

        private void drawPieChart(Graphics2D g2, MacroGoal p, int x, int y) {
            int size = 130;
            Color[] pc = {MACRO_CARBS, MACRO_PROTEIN, MACRO_FAT};
            double[] pcts = {p.getCarbPercent(), p.getProteinPercent(), p.getFatPercent()};
            String[] names = {"Carbs", "Protein", "Fat"};

            // Title
            g2.setFont(UITheme.FONT_SUBHEADING);
            g2.setColor(UITheme.TEXT_SECONDARY);
            g2.drawString("Target split", x, y - 6);

            int start = 0;
            for (int i = 0; i < 3; i++) {
                int arc = (int) Math.round(pcts[i] / 100.0 * 360);
                g2.setColor(pc[i]);
                g2.fillArc(x, y, size, size, start, arc);
                start += arc;
            }

            // Donut hole for a cleaner look
            g2.setColor(UITheme.SURFACE);
            int hole = size / 2;
            g2.fillOval(x + (size - hole) / 2, y + (size - hole) / 2, hole, hole);

            // Legend
            g2.setFont(UITheme.FONT_CAPTION);
            for (int i = 0; i < 3; i++) {
                int ly = y + size + 14 + i * 20;
                g2.setColor(pc[i]);
                g2.fillRoundRect(x, ly - 10, 14, 14, 4, 4);
                g2.setColor(UITheme.TEXT_PRIMARY);
                g2.drawString(String.format("%s — %.0f%%", names[i], pcts[i]), x + 22, ly + 1);
            }
        }
    }
}
