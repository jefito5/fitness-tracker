package gui;

import database.ConnectionFactory;
import gui.components.CardPanel;
import gui.components.RoundedButton;
import gui.components.StatCard;
import gui.components.StyledTextField;
import gui.components.UITheme;
import impl.MealLogDB;
import impl.UserDB;
import models.User;

import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * UI-8: Redesigned Calorie Deficit / Surplus Calculator.
 *
 * Three sectioned steps with results displayed inline (no JOptionPane popups):
 *   1  Activity     — activity-level combo
 *   2  Goal         — segmented control: Deficit / Maintenance / Surplus
 *   3  Results      — StatCards: TDEE, Target, Consumed, Balance, 30-day projection
 *
 * BMR formula: Mifflin-St Jeor.
 * 7700 kcal ≈ 1 kg of body fat.
 */
public class CalorieCalcPanel {

    private enum Goal {
        DEFICIT("Deficit",     -500, "Lose weight"),
        MAINTAIN("Maintenance",  0,  "Maintain weight"),
        SURPLUS("Surplus",     +300, "Gain weight");

        final String label;
        final int    kcalAdjust;
        final String caption;
        Goal(String l, int adj, String c) { label = l; kcalAdjust = adj; caption = c; }
    }

    private final int userId;
    private JFrame frame;

    // Step 1
    private JComboBox<String> activityCombo;

    // Step 2
    private RoundedButton goalDeficit, goalMaintain, goalSurplus;
    private Goal selectedGoal = Goal.MAINTAIN;
    private StyledTextField customDeficitField; // optional override (positive number = kcal/day adjustment)

    // Inline error feedback (replaces JOptionPane)
    private JLabel inlineError;

    // Step 3 — result cards
    private StatCard cardTdee;
    private StatCard cardTarget;
    private StatCard cardConsumed;
    private StatCard cardBalance;
    private StatCard cardProjection;
    private JLabel summaryLabel;

    public CalorieCalcPanel(int userId) {
        this.userId = userId;
        initialize();
    }

    private void initialize() {
        UserDB udb = new UserDB();
        User u = udb.getById(userId);
        double weight = latestWeight();

        frame = new JFrame("Calorie Deficit / Surplus Calculator");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setMinimumSize(new Dimension(720, 640));
        frame.setSize(820, 720);
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BACKGROUND);
        root.setBorder(UITheme.padding(UITheme.SPACE_LG));
        frame.setContentPane(root);

        root.add(buildHeader(u, weight), BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);
        body.setBorder(UITheme.padding(UITheme.SPACE_LG, 0, 0, 0));

        body.add(buildStep1Card());
        body.add(Box.createVerticalStrut(UITheme.SPACE_LG));
        body.add(buildStep2Card());
        body.add(Box.createVerticalStrut(UITheme.SPACE_LG));
        body.add(buildStep3Card());

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        root.add(scroll, BorderLayout.CENTER);

        // Initial action wires
        goalDeficit.addActionListener(e -> setGoal(Goal.DEFICIT));
        goalMaintain.addActionListener(e -> setGoal(Goal.MAINTAIN));
        goalSurplus.addActionListener(e -> setGoal(Goal.SURPLUS));

        frame.setVisible(true);
    }

    // ════════════════════════════════════════════════════════════════════
    //  HEADER
    // ════════════════════════════════════════════════════════════════════
    private JComponent buildHeader(User u, double weight) {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titleBlock = new JPanel();
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.setOpaque(false);

        JLabel title = new JLabel("Calorie Calculator");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        String heightStr = u.getHeight() > 0 ? ((int) u.getHeight()) + " cm" : "Height N/A";
        String weightStr = weight > 0 ? String.format("%.1f kg", weight) : "Weight N/A";
        JLabel sub = new JLabel(String.format("%s  •  %d yrs  •  %s  •  %s  •  %s",
                u.getName(), u.getAge(), u.getGender(), heightStr, weightStr));
        sub.setFont(UITheme.FONT_CAPTION);
        sub.setForeground(UITheme.TEXT_SECONDARY);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        sub.setBorder(UITheme.padding(2, 0, 0, 0));

        titleBlock.add(title);
        titleBlock.add(sub);
        header.add(titleBlock, BorderLayout.WEST);
        return header;
    }

    // ════════════════════════════════════════════════════════════════════
    //  STEP 1 — Activity
    // ════════════════════════════════════════════════════════════════════
    private JComponent buildStep1Card() {
        CardPanel card = new CardPanel(true);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(UITheme.padding(UITheme.SPACE_XL));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        card.add(stepHeader("1", "Activity level", "How active are you on a typical day?"));
        card.add(Box.createVerticalStrut(UITheme.SPACE_MD));

        activityCombo = new JComboBox<>(new String[]{
                "Sedentary (little to no exercise)",
                "Light (1-3 days/week)",
                "Moderate (3-5 days/week)",
                "Active (6-7 days/week)"
        });
        activityCombo.setFont(UITheme.FONT_BODY);
        activityCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        activityCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(activityCombo);

        return card;
    }

    // ════════════════════════════════════════════════════════════════════
    //  STEP 2 — Goal
    // ════════════════════════════════════════════════════════════════════
    private JComponent buildStep2Card() {
        CardPanel card = new CardPanel(true);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(UITheme.padding(UITheme.SPACE_XL));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        card.add(stepHeader("2", "Goal", "Pick a target — we'll show you a daily calorie goal."));
        card.add(Box.createVerticalStrut(UITheme.SPACE_MD));

        JPanel segmented = new JPanel(new FlowLayout(FlowLayout.LEFT, UITheme.SPACE_SM, 0));
        segmented.setOpaque(false);
        segmented.setAlignmentX(Component.LEFT_ALIGNMENT);
        goalDeficit  = goalChip(Goal.DEFICIT);
        goalMaintain = goalChip(Goal.MAINTAIN);
        goalSurplus  = goalChip(Goal.SURPLUS);
        segmented.add(goalDeficit);
        segmented.add(goalMaintain);
        segmented.add(goalSurplus);
        card.add(segmented);

        card.add(Box.createVerticalStrut(UITheme.SPACE_MD));

        JPanel customRow = new JPanel(new FlowLayout(FlowLayout.LEFT, UITheme.SPACE_SM, 0));
        customRow.setOpaque(false);
        customRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel customLbl = new JLabel("Custom daily adjustment (kcal, optional):");
        customLbl.setFont(UITheme.FONT_CAPTION);
        customLbl.setForeground(UITheme.TEXT_SECONDARY);
        customDeficitField = new StyledTextField("e.g. -500 or 300");
        customDeficitField.setPreferredSize(new Dimension(180, 32));
        customRow.add(customLbl);
        customRow.add(customDeficitField);
        card.add(customRow);

        return card;
    }

    private RoundedButton goalChip(Goal g) {
        RoundedButton b = new RoundedButton(g.label,
                g == selectedGoal ? RoundedButton.Variant.PRIMARY : RoundedButton.Variant.OUTLINE);
        b.setBorder(UITheme.padding(8, 18));
        return b;
    }

    private void setGoal(Goal g) {
        selectedGoal = g;
        goalDeficit.setVariant(g == Goal.DEFICIT  ? RoundedButton.Variant.PRIMARY : RoundedButton.Variant.OUTLINE);
        goalMaintain.setVariant(g == Goal.MAINTAIN ? RoundedButton.Variant.PRIMARY : RoundedButton.Variant.OUTLINE);
        goalSurplus.setVariant(g == Goal.SURPLUS  ? RoundedButton.Variant.PRIMARY : RoundedButton.Variant.OUTLINE);
    }

    // ════════════════════════════════════════════════════════════════════
    //  STEP 3 — Results
    // ════════════════════════════════════════════════════════════════════
    private JComponent buildStep3Card() {
        CardPanel card = new CardPanel(true);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(UITheme.padding(UITheme.SPACE_XL));

        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setOpaque(false);
        headerRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerRow.add(stepHeader("3", "Results", "Calculated from the inputs above."), BorderLayout.WEST);

        RoundedButton btnCalc = new RoundedButton("CALCULATE", RoundedButton.Variant.PRIMARY);
        btnCalc.addActionListener(e -> calculate());
        headerRow.add(btnCalc, BorderLayout.EAST);
        card.add(headerRow);

        inlineError = new JLabel(" ");
        inlineError.setFont(UITheme.FONT_CAPTION);
        inlineError.setForeground(UITheme.DANGER);
        inlineError.setAlignmentX(Component.LEFT_ALIGNMENT);
        inlineError.setBorder(UITheme.padding(UITheme.SPACE_SM, 0, 0, 0));
        card.add(inlineError);

        card.add(Box.createVerticalStrut(UITheme.SPACE_MD));

        JPanel grid = new JPanel(new GridLayout(2, 3, UITheme.SPACE_MD, UITheme.SPACE_MD));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardTdee       = new StatCard("TDEE (daily need)");
        cardTarget     = new StatCard("Target calories");
        cardConsumed   = new StatCard("Consumed today");
        cardBalance    = new StatCard("Balance vs target");
        cardProjection = new StatCard("30-day projection");
        StatCard cardWeekly = new StatCard("7-day projection");
        grid.add(cardTdee);
        grid.add(cardTarget);
        grid.add(cardConsumed);
        grid.add(cardBalance);
        grid.add(cardWeekly);
        grid.add(cardProjection);
        // Keep a reference to weekly so calculate() can update it
        this.weeklyCardRef = cardWeekly;

        card.add(grid);

        summaryLabel = new JLabel(" ");
        summaryLabel.setFont(UITheme.FONT_BODY);
        summaryLabel.setForeground(UITheme.TEXT_SECONDARY);
        summaryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        summaryLabel.setBorder(UITheme.padding(UITheme.SPACE_LG, 0, 0, 0));
        card.add(summaryLabel);

        return card;
    }

    private StatCard weeklyCardRef;

    // ════════════════════════════════════════════════════════════════════
    //  Helper — step header (number badge + title + caption)
    // ════════════════════════════════════════════════════════════════════
    private JComponent stepHeader(String num, String title, String caption) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, UITheme.SPACE_MD, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel badge = new JLabel(num) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UITheme.PRIMARY);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setPreferredSize(new Dimension(28, 28));
        badge.setHorizontalAlignment(SwingConstants.CENTER);
        badge.setVerticalAlignment(SwingConstants.CENTER);
        badge.setForeground(UITheme.ON_PRIMARY);
        badge.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 13));
        badge.setOpaque(false);

        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setOpaque(false);

        JLabel t = new JLabel(title);
        t.setFont(UITheme.FONT_HEADING);
        t.setForeground(UITheme.TEXT_PRIMARY);
        t.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel c = new JLabel(caption);
        c.setFont(UITheme.FONT_CAPTION);
        c.setForeground(UITheme.TEXT_MUTED);
        c.setAlignmentX(Component.LEFT_ALIGNMENT);

        text.add(t);
        text.add(c);

        row.add(badge);
        row.add(text);
        return row;
    }

    // ════════════════════════════════════════════════════════════════════
    //  Calculation
    // ════════════════════════════════════════════════════════════════════
    private void calculate() {
        inlineError.setText(" ");
        UserDB udb = new UserDB();
        User u = udb.getById(userId);
        double weight = latestWeight();

        double height = u.getHeight();
        int age = u.getAge();
        String gender = u.getGender() == null ? "male" : u.getGender().toLowerCase();

        if (height <= 0 || weight <= 0 || age <= 0) {
            inlineError.setText("Please update your Height in your profile and log your Weight first.");
            return;
        }

        // Mifflin-St Jeor BMR
        double bmr = "female".equals(gender)
                ? 10 * weight + 6.25 * height - 5 * age - 161
                : 10 * weight + 6.25 * height - 5 * age + 5;

        double[] multipliers = {1.2, 1.375, 1.55, 1.725};
        double tdee = bmr * multipliers[activityCombo.getSelectedIndex()];

        // Resolve goal adjustment — custom field overrides preset goal if valid
        int adjust = selectedGoal.kcalAdjust;
        String customRaw = customDeficitField.getText().trim();
        if (!customRaw.isEmpty()) {
            try { adjust = Integer.parseInt(customRaw); }
            catch (NumberFormatException ignored) {
                inlineError.setText("Custom adjustment must be a whole number (e.g. -500).");
                return;
            }
        }

        double target   = tdee + adjust;
        double consumed = new MealLogDB().getTodayCalories(userId);
        double balance  = consumed - target;
        double weekKg   = (balance * 7)  / 7700.0;
        double monthKg  = (balance * 30) / 7700.0;

        cardTdee.setValue(String.format("%.0f kcal", tdee));
        cardTdee.setCaption("Maintenance daily need");

        cardTarget.setValue(String.format("%.0f kcal", target));
        cardTarget.setCaption(selectedGoal.label + " (" + (adjust >= 0 ? "+" : "") + adjust + " kcal)");
        cardTarget.setValueColor(UITheme.PRIMARY);

        cardConsumed.setValue(String.format("%.0f kcal", consumed));
        cardConsumed.setCaption("Logged for today");

        Color balanceColor = balance >= 0 ? UITheme.WARNING : UITheme.SUCCESS;
        cardBalance.setValue(String.format("%+.0f kcal", balance));
        cardBalance.setValueColor(balanceColor);
        cardBalance.setCaption(balance >= 0 ? "Above target" : "Below target");

        Color weeklyColor = weekKg >= 0 ? UITheme.WARNING : UITheme.SUCCESS;
        weeklyCardRef.setValue(String.format("%+.2f kg", weekKg));
        weeklyCardRef.setValueColor(weeklyColor);
        weeklyCardRef.setCaption("At current rate");

        Color monthColor = monthKg >= 0 ? UITheme.WARNING : UITheme.SUCCESS;
        cardProjection.setValue(String.format("%+.2f kg", monthKg));
        cardProjection.setValueColor(monthColor);
        cardProjection.setCaption("At current rate");

        String trend = monthKg < 0 ? "lose" : "gain";
        summaryLabel.setText(String.format(
                "At this rate you will %s %.2f kg in a month against your %s target.",
                trend, Math.abs(monthKg), selectedGoal.label.toLowerCase()));
        summaryLabel.setForeground(monthKg < 0 ? UITheme.SUCCESS : UITheme.WARNING);
    }

    // ════════════════════════════════════════════════════════════════════
    //  DB helpers
    // ════════════════════════════════════════════════════════════════════
    private double latestWeight() {
        try {
            PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(
                    "SELECT WeightM FROM weights WHERE UserId=? AND WeightM > 0 ORDER BY Date DESC LIMIT 1");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (Exception ignored) {}
        return 0;
    }
}
