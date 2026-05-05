package gui;

import impl.MealDB;
import impl.MealLogDB;
import impl.UserDB;
import models.DailyMealLog;
import models.Meal;
import models.User;
import services.FoodImporter;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.util.List;

/**
 * NutriTrack — Main Dashboard (MealIUD replacement).
 *
 * Layout
 * ──────
 *  ┌─ TOP NAV BAR (persistent) ───────────────────────────────────┐
 *  │  🥗 NutriTrack   Welcome, {name}   [User ID pill]  [Log Out] │
 *  ├─ CONTENT (CardLayout) ───────────────────────────────────────┤
 *  │  DASHBOARD card:                                             │
 *  │   ┌ DAILY SUMMARY STRIP ─────────────────────────────────┐  │
 *  │   │  Consumed  ·  Burned  ·  Net  ·  Goal progress bar   │  │
 *  │   └──────────────────────────────────────────────────────┘  │
 *  │   ┌ NAVIGATION GRID (StatCards) ────────────────────────┐   │
 *  │   │  [📋 Log Meal] [📊 Progress] [📈 Analyse]           │   │
 *  │   │  [🎯 Macros]   [⚖ BMI]       [🔥 Calorie Calc]      │   │
 *  │   │  [🏋 Exercise] [👤 Profile]                          │   │
 *  │   └──────────────────────────────────────────────────────┘   │
 *  │  ADD MEAL card  (inline form, reachable from nav)            │
 *  │  PROFILE card   (inline form, reachable from nav)            │
 *  └───────────────────────────────────────────────────────────────┘
 *
 * Design tokens shared with Iud.java.
 */
public class MealIUD {

    // ── Design tokens (mirror Iud) ─────────────────────────────
    private static final Color BG         = new Color(0x0D1117);
    private static final Color SURFACE    = new Color(0x161B22);
    private static final Color SURFACE_HI = new Color(0x21262D);
    private static final Color ACCENT     = new Color(0x00D4AA);
    private static final Color TEXT_MAIN  = new Color(0xE6EDF3);
    private static final Color TEXT_MUTED = new Color(0x8B949E);
    private static final Color DANGER     = new Color(0xFF6B6B);
    private static final Color SUCCESS    = new Color(0x3FB950);
    private static final Color WARNING    = new Color(0xF0A500);
    private static final Color BORDER_COL = new Color(0x30363D);

    private static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD,   20);
    private static final Font FONT_BODY    = new Font("Segoe UI", Font.PLAIN,  13);
    private static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN,  11);
    private static final Font FONT_BTN     = new Font("Segoe UI", Font.BOLD,   13);
    private static final Font FONT_NAV     = new Font("Segoe UI", Font.BOLD,   11);
    private static final Font FONT_CARD_ICON  = new Font("Segoe UI Emoji", Font.PLAIN, 28);
    private static final Font FONT_STAT_VAL   = new Font("Segoe UI", Font.BOLD,   22);
    private static final Font FONT_STAT_LABEL = new Font("Segoe UI", Font.PLAIN,  11);

    // ── User state ─────────────────────────────────────────────
    private final int    ids;
    private final String names;
    private final int    ages;
    private final String genders;
    private final String passwords;

    // ── Swing state ────────────────────────────────────────────
    private JFrame      frame;
    private CardLayout  cardLayout;
    private JPanel      contentArea;

    // Summary strip labels (refreshed each time dashboard is shown)
    private JLabel lblConsumed, lblBurned, lblNet, lblGoal;
    private JProgressBar progressBar;

    // Add-Meal form fields
    private JTextField txtMealName, txtCalPer100g, txtWeight;
    private JTextField txtProtein, txtCarbs, txtFat;
    private JLabel     mealFeedback;

    // Profile form fields
    private JTextField    tfName, tfAge, tfHeight, tfCalGoal;
    private JPasswordField tfPassword;
    private JComboBox<String> cbGender;
    private JLabel profileFeedback;

    // ── Constructor ────────────────────────────────────────────
    public MealIUD(int id, String name, int age, String gender, String password) {
        ids       = id;
        names     = name;
        ages      = age;
        genders   = gender;
        passwords = password;
        initialize();
    }

    // ── Frame setup ────────────────────────────────────────────
    private void initialize() {
        frame = new JFrame("NutriTrack — Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(980, 680);
        frame.setMinimumSize(new Dimension(860, 600));
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(BG);
        frame.getContentPane().setLayout(new BorderLayout());

        frame.getContentPane().add(buildTopNav(),     BorderLayout.NORTH);
        frame.getContentPane().add(buildContent(),    BorderLayout.CENTER);

        refreshSummary();
        frame.setVisible(true);
    }

    // ══════════════════════════════════════════════════════════════
    //  TOP NAV BAR  (persistent — visible from every card)
    // ══════════════════════════════════════════════════════════════
    private JPanel buildTopNav() {
        JPanel nav = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(SURFACE);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        nav.setOpaque(false);
        nav.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_COL),
            new EmptyBorder(10, 20, 10, 20)
        ));

        // Left: logo + app name
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);
        JLabel logo = new JLabel("🥗");
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        JLabel appName = new JLabel("NutriTrack");
        appName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        appName.setForeground(ACCENT);
        left.add(logo);
        left.add(appName);

        // Centre: quick nav links
        JPanel centre = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        centre.setOpaque(false);
        String[][] navItems = {
            {"🏠", "Home"     },
            {"📋", "Log Meal" },
            {"📊", "Progress" },
            {"👤", "Profile"  }
        };
        String[] cards = {"dashboard", "addMeal", "addMeal", "profile"};
        // Progress opens external window; the others are inline cards
        for (int i = 0; i < navItems.length; i++) {
            final String card  = cards[i];
            final boolean isProgress = navItems[i][1].equals("Progress");
            JButton b = makeNavLink(navItems[i][0] + " " + navItems[i][1]);
            b.addActionListener(e -> {
                if (isProgress) {
                    new trackProgress(ids);
                } else {
                    showCard(card);
                }
            });
            centre.add(b);
            if (i < navItems.length - 1) {
                JLabel sep = new JLabel("|");
                sep.setForeground(BORDER_COL);
                sep.setFont(FONT_SMALL);
                centre.add(sep);
            }
        }

        // Right: welcome + logout
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);
        JLabel welcome = new JLabel("Welcome, " + names);
        welcome.setFont(FONT_BODY);
        welcome.setForeground(TEXT_MUTED);

        JButton btnLogOut = makeAccentButton("Log Out", SURFACE_HI, TEXT_MUTED);
        btnLogOut.addActionListener(e -> { new Iud(); frame.dispose(); });

        right.add(welcome);
        right.add(btnLogOut);

        nav.add(left,   BorderLayout.WEST);
        nav.add(centre, BorderLayout.CENTER);
        nav.add(right,  BorderLayout.EAST);
        return nav;
    }

    // ══════════════════════════════════════════════════════════════
    //  CONTENT AREA  (CardLayout: dashboard / addMeal / profile)
    // ══════════════════════════════════════════════════════════════
    private JPanel buildContent() {
        cardLayout  = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(BG);

        contentArea.add(buildDashboardCard(), "dashboard");
        contentArea.add(buildAddMealCard(),   "addMeal");
        contentArea.add(buildProfileCard(),   "profile");

        return contentArea;
    }

    private void showCard(String name) {
        cardLayout.show(contentArea, name);
        if (name.equals("dashboard")) refreshSummary();
    }

    // ══════════════════════════════════════════════════════════════
    //  DASHBOARD CARD
    // ══════════════════════════════════════════════════════════════
    private JPanel buildDashboardCard() {
        JPanel root = new JPanel();
        root.setBackground(BG);
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(new EmptyBorder(24, 32, 24, 32));

        // Section header
        JLabel headerLbl = new JLabel("Dashboard");
        headerLbl.setFont(FONT_HEADING);
        headerLbl.setForeground(TEXT_MAIN);
        headerLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(headerLbl);
        root.add(Box.createVerticalStrut(4));

        JLabel dateLbl = new JLabel(LocalDate.now().toString());
        dateLbl.setFont(FONT_SMALL);
        dateLbl.setForeground(TEXT_MUTED);
        dateLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(dateLbl);
        root.add(Box.createVerticalStrut(20));

        // ── Daily Summary Strip ──────────────────────────────────
        root.add(buildSummaryStrip());
        root.add(Box.createVerticalStrut(28));

        // ── Navigation Grid ──────────────────────────────────────
        JLabel navHeader = new JLabel("Features");
        navHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
        navHeader.setForeground(TEXT_MUTED);
        navHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(navHeader);
        root.add(Box.createVerticalStrut(12));
        root.add(buildNavGrid());

        return root;
    }

    // ── Daily Summary Strip ─────────────────────────────────────
    private JPanel buildSummaryStrip() {
        JPanel strip = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(SURFACE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.setColor(BORDER_COL);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 16, 16));
                g2.dispose();
            }
        };
        strip.setOpaque(false);
        strip.setLayout(new GridLayout(1, 4, 0, 0));
        strip.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        strip.setAlignmentX(Component.LEFT_ALIGNMENT);
        strip.setBorder(new EmptyBorder(16, 0, 16, 0));

        // Consumed
        lblConsumed = new JLabel("—");
        lblConsumed.setFont(FONT_STAT_VAL);
        lblConsumed.setForeground(ACCENT);
        strip.add(buildSummaryCell("🍽 Consumed", lblConsumed, "kcal today", false));

        // Burned
        lblBurned = new JLabel("—");
        lblBurned.setFont(FONT_STAT_VAL);
        lblBurned.setForeground(WARNING);
        strip.add(buildSummaryCell("🔥 Burned", lblBurned, "kcal exercises", false));

        // Net
        lblNet = new JLabel("—");
        lblNet.setFont(FONT_STAT_VAL);
        lblNet.setForeground(TEXT_MAIN);
        strip.add(buildSummaryCell("📊 Net", lblNet, "kcal balance", false));

        // Goal progress
        JPanel goalCell = new JPanel();
        goalCell.setOpaque(false);
        goalCell.setLayout(new BoxLayout(goalCell, BoxLayout.Y_AXIS));
        goalCell.setBorder(new EmptyBorder(0, 24, 0, 24));

        JLabel goalIcon = new JLabel("🎯 Goal Progress");
        goalIcon.setFont(FONT_STAT_LABEL);
        goalIcon.setForeground(TEXT_MUTED);
        goalIcon.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblGoal = new JLabel("—  /  — kcal");
        lblGoal.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblGoal.setForeground(TEXT_MAIN);
        lblGoal.setAlignmentX(Component.LEFT_ALIGNMENT);

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(false);
        progressBar.setBackground(SURFACE_HI);
        progressBar.setForeground(ACCENT);
        progressBar.setBorderPainted(false);
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 8));
        progressBar.setAlignmentX(Component.LEFT_ALIGNMENT);

        goalCell.add(goalIcon);
        goalCell.add(Box.createVerticalStrut(8));
        goalCell.add(lblGoal);
        goalCell.add(Box.createVerticalStrut(10));
        goalCell.add(progressBar);
        strip.add(goalCell);

        return strip;
    }

    private JPanel buildSummaryCell(String title, JLabel valueLabel, String unit, boolean last) {
        JPanel cell = new JPanel();
        cell.setOpaque(false);
        cell.setLayout(new BoxLayout(cell, BoxLayout.Y_AXIS));
        cell.setBorder(new CompoundBorder(
            last ? BorderFactory.createEmptyBorder()
                 : new MatteBorder(0, 0, 0, 1, BORDER_COL),
            new EmptyBorder(0, 24, 0, 24)
        ));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(FONT_STAT_LABEL);
        titleLbl.setForeground(TEXT_MUTED);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel unitLbl = new JLabel(unit);
        unitLbl.setFont(FONT_SMALL);
        unitLbl.setForeground(TEXT_MUTED);
        unitLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        cell.add(titleLbl);
        cell.add(Box.createVerticalStrut(6));
        cell.add(valueLabel);
        cell.add(Box.createVerticalStrut(2));
        cell.add(unitLbl);
        return cell;
    }

    /** Pull today's totals from DB and refresh strip labels. */
    private void refreshSummary() {
        try {
            MealLogDB mldb = new MealLogDB();
            double consumed = mldb.getTodayCalories(ids);

            double burned = 0;
            try {
                impl.ExerciseLogDB edb = new impl.ExerciseLogDB();
                //burned = edb.getTodayBurnedCalories(ids);
            } catch (Exception ignored) {}

            double net = consumed - burned;

            UserDB udb = new UserDB();
            User u = udb.getById(ids);
            int goal = (u != null && u.getCalorieGoal() > 0) ? u.getCalorieGoal() : 2000;

            lblConsumed.setText(String.format("%.0f", consumed));
            lblBurned.setText(String.format("%.0f", burned));

            lblNet.setText(String.format("%.0f", net));
            lblNet.setForeground(net > goal ? DANGER : ACCENT);

            lblGoal.setText(String.format("%.0f  /  %d kcal", consumed, goal));
            int pct = Math.min(100, (int) (consumed * 100.0 / goal));
            progressBar.setValue(pct);
            progressBar.setForeground(pct > 100 ? DANGER : pct > 80 ? WARNING : ACCENT);
        } catch (Exception e) {
            // DB not available during design-time preview — leave defaults
        }
    }

    // ── Navigation Grid of StatCards ────────────────────────────
    private JPanel buildNavGrid() {
        JPanel grid = new JPanel(new GridLayout(0, 3, 14, 14));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Each entry: icon, label, action
        addStatCard(grid, "📋", "Log Meal",       "Add a new meal entry",       () -> showCard("addMeal"));
        addStatCard(grid, "📊", "Daily Progress",  "View & update today's log",  () -> new trackProgress(ids));
        addStatCard(grid, "📈", "Analyse Trends",  "Charts & period analysis",   () -> new PeriodSelect(ids));
        addStatCard(grid, "🎯", "Macro Goals",     "Set ratio targets",          () -> new MacroGoalsPanel(ids));
        addStatCard(grid, "⚖",  "BMI Check",       "Calculate your BMI",         () -> new BmiPanel(ids));
        addStatCard(grid, "🔥", "Calorie Calc",    "Deficit / Surplus calculator",() -> new CalorieCalcPanel(ids));
        addStatCard(grid, "🏋", "Exercises",       "Browse & log exercises",     () -> new ExerciseIUD(ids, 70.0));
        addStatCard(grid, "👤", "Profile",         "Edit your information",      () -> showCard("profile"));

        return grid;
    }

    private void addStatCard(JPanel grid, String icon, String label, String desc, Runnable action) {
        JPanel card = new JPanel() {
            private boolean hover = false;
            {
                setOpaque(false);
                setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                setBorder(new EmptyBorder(18, 18, 18, 18));
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hover = true;  repaint(); }
                    public void mouseExited (MouseEvent e) { hover = false; repaint(); }
                    public void mouseClicked(MouseEvent e) { action.run(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hover ? SURFACE_HI : SURFACE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 14, 14));
                g2.setColor(hover ? ACCENT : BORDER_COL);
                g2.setStroke(new BasicStroke(hover ? 1.5f : 1f));
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 14, 14));
                g2.dispose();
            }
        };

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(FONT_CARD_ICON);
        iconLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nameLbl = new JLabel(label);
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLbl.setForeground(TEXT_MAIN);
        nameLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLbl = new JLabel(desc);
        descLbl.setFont(FONT_SMALL);
        descLbl.setForeground(TEXT_MUTED);
        descLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(iconLbl);
        card.add(Box.createVerticalStrut(8));
        card.add(nameLbl);
        card.add(Box.createVerticalStrut(3));
        card.add(descLbl);

        grid.add(card);
    }

    // ══════════════════════════════════════════════════════════════
    //  ADD MEAL CARD
    // ══════════════════════════════════════════════════════════════
    private JPanel buildAddMealCard() {
        JPanel root = new JPanel();
        root.setBackground(BG);
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(new EmptyBorder(28, 40, 28, 40));

        addCardHeader(root, "📋 Log a Meal", "Record a new food item and its macros");

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.setMaximumSize(new Dimension(560, Integer.MAX_VALUE));

        GridBagConstraints lc = new GridBagConstraints();
        lc.anchor = GridBagConstraints.WEST;
        lc.insets = new Insets(6, 0, 2, 16);
        lc.gridx  = 0;

        GridBagConstraints fc = new GridBagConstraints();
        fc.fill   = GridBagConstraints.HORIZONTAL;
        fc.weightx= 1.0;
        fc.insets = new Insets(6, 0, 2, 0);
        fc.gridx  = 1;

        txtMealName  = addFormRow(form, lc, fc, 0, "Meal Name");
        txtCalPer100g= addFormRow(form, lc, fc, 1, "Calories / 100g");
        txtWeight    = addFormRow(form, lc, fc, 2, "Weight (g)");
        txtProtein   = addFormRow(form, lc, fc, 3, "Protein g/100g  (optional)");
        txtCarbs     = addFormRow(form, lc, fc, 4, "Carbs g/100g  (optional)");
        txtFat       = addFormRow(form, lc, fc, 5, "Fat g/100g  (optional)");

        root.add(form);
        root.add(Box.createVerticalStrut(12));

        mealFeedback = makeFeedbackLabel(" ");
        root.add(mealFeedback);
        root.add(Box.createVerticalStrut(16));

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        btns.setOpaque(false);
        btns.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnInsert = makeAccentButton("Insert Meal", ACCENT, BG);
        btnInsert.addActionListener(new InsertMealListener());
        btns.add(btnInsert);

        JButton btnBack = makeAccentButton("← Back", SURFACE_HI, TEXT_MUTED);
        btnBack.addActionListener(e -> showCard("dashboard"));
        btns.add(btnBack);

        root.add(btns);
        return root;
    }

    private JTextField addFormRow(JPanel form, GridBagConstraints lc, GridBagConstraints fc, int row, String label) {
        lc.gridy = row; fc.gridy = row;
        JLabel lbl = new JLabel(label);
        lbl.setFont(FONT_SMALL);
        lbl.setForeground(TEXT_MUTED);
        form.add(lbl, lc);

        JTextField tf = new JTextField();
        styleTextField(tf);
        form.add(tf, fc);
        return tf;
    }

    // ══════════════════════════════════════════════════════════════
    //  PROFILE CARD
    // ══════════════════════════════════════════════════════════════
    private JPanel buildProfileCard() {
        JPanel root = new JPanel();
        root.setBackground(BG);
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(new EmptyBorder(28, 40, 28, 40));

        addCardHeader(root, "👤 Your Profile", "Update your personal information");

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));

        GridBagConstraints lc = new GridBagConstraints();
        lc.anchor = GridBagConstraints.WEST;
        lc.insets = new Insets(6, 0, 2, 16);
        lc.gridx  = 0;

        GridBagConstraints fc = new GridBagConstraints();
        fc.fill   = GridBagConstraints.HORIZONTAL;
        fc.weightx= 1.0;
        fc.insets = new Insets(6, 0, 2, 0);
        fc.gridx  = 1;

        // Name
        lc.gridy = 0; fc.gridy = 0;
        form.add(profileLabel("Name"), lc);
        tfName = new JTextField(names);
        styleTextField(tfName);
        form.add(tfName, fc);

        // Age
        lc.gridy = 1; fc.gridy = 1;
        form.add(profileLabel("Age"), lc);
        tfAge = new JTextField(String.valueOf(ages));
        styleTextField(tfAge);
        form.add(tfAge, fc);

        // Gender
        lc.gridy = 2; fc.gridy = 2;
        form.add(profileLabel("Gender"), lc);
        cbGender = new JComboBox<>(new String[]{"male", "female"});
        cbGender.setSelectedItem(genders);
        cbGender.setBackground(SURFACE_HI);
        cbGender.setForeground(TEXT_MAIN);
        cbGender.setFont(FONT_BODY);
        cbGender.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        form.add(cbGender, fc);

        // Password
        lc.gridy = 3; fc.gridy = 3;
        form.add(profileLabel("Password"), lc);
        tfPassword = new JPasswordField(passwords);
        styleTextField(tfPassword);
        form.add(tfPassword, fc);

        // Height
        lc.gridy = 4; fc.gridy = 4;
        form.add(profileLabel("Height (cm)"), lc);
        tfHeight = new JTextField();
        styleTextField(tfHeight);
        form.add(tfHeight, fc);

        // Calorie goal
        lc.gridy = 5; fc.gridy = 5;
        form.add(profileLabel("Calorie Goal (kcal)"), lc);
        tfCalGoal = new JTextField();
        styleTextField(tfCalGoal);
        form.add(tfCalGoal, fc);

        // Pre-fill height / goal from DB
        try {
            User uH = new UserDB().getById(ids);
            if (uH != null) {
                if (uH.getHeight() > 0) tfHeight.setText(String.valueOf((int) uH.getHeight()));
                tfCalGoal.setText(String.valueOf(uH.getCalorieGoal()));
            }
        } catch (Exception ignored) {}

        root.add(form);
        root.add(Box.createVerticalStrut(12));

        profileFeedback = makeFeedbackLabel(" ");
        root.add(profileFeedback);
        root.add(Box.createVerticalStrut(16));

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        btns.setOpaque(false);
        btns.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnSave = makeAccentButton("Save Changes", ACCENT, BG);
        btnSave.addActionListener(new UpdateProfileListener());
        btns.add(btnSave);

        JButton btnBack = makeAccentButton("← Back", SURFACE_HI, TEXT_MUTED);
        btnBack.addActionListener(e -> showCard("dashboard"));
        btns.add(btnBack);

        root.add(btns);
        return root;
    }

    // ══════════════════════════════════════════════════════════════
    //  SHARED HELPERS
    // ══════════════════════════════════════════════════════════════
    private void addCardHeader(JPanel p, String title, String subtitle) {
        JLabel h = new JLabel(title);
        h.setFont(FONT_HEADING);
        h.setForeground(TEXT_MAIN);
        h.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(h);

        JLabel sub = new JLabel(subtitle);
        sub.setFont(FONT_SMALL);
        sub.setForeground(TEXT_MUTED);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(sub);
        p.add(Box.createVerticalStrut(24));
    }

    private JLabel profileLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_SMALL);
        l.setForeground(TEXT_MUTED);
        return l;
    }

    private JLabel makeFeedbackLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_SMALL);
        l.setForeground(DANGER);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private void styleTextField(JComponent f) {
        f.setBackground(SURFACE_HI);
        f.setForeground(TEXT_MAIN);
        f.setFont(FONT_BODY);
        f.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COL, 1, true),
            new EmptyBorder(7, 12, 7, 12)
        ));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (f instanceof JTextField)    ((JTextField)    f).setCaretColor(ACCENT);
        if (f instanceof JPasswordField)((JPasswordField) f).setCaretColor(ACCENT);
    }

    private JButton makeAccentButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? bg.brighter() : bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(fg);
        btn.setFont(FONT_BTN);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(9, 22, 9, 22));
        return btn;
    }

    private JButton makeNavLink(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_NAV);
        btn.setForeground(TEXT_MUTED);
        btn.setBackground(BG);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setForeground(ACCENT); }
            public void mouseExited (MouseEvent e) { btn.setForeground(TEXT_MUTED); }
        });
        return btn;
    }

    // ══════════════════════════════════════════════════════════════
    //  ACTION LISTENERS
    // ══════════════════════════════════════════════════════════════

    class InsertMealListener implements ActionListener {
        @Override public void actionPerformed(ActionEvent e) {
            mealFeedback.setForeground(DANGER);
            try {
                if (txtMealName.getText().isEmpty() || txtCalPer100g.getText().isEmpty()) {
                    mealFeedback.setText("Meal name and calories/100g are required.");
                    return;
                }
                if (txtWeight.getText().isEmpty()) {
                    mealFeedback.setText("Please enter weight in grams.");
                    return;
                }
                double grams        = Double.parseDouble(txtWeight.getText());
                double kcalPer100g  = Double.parseDouble(txtCalPer100g.getText());
                double totalKcal    = kcalPer100g * grams / 100.0;
                double protein      = txtProtein.getText().isEmpty() ? 0 : Double.parseDouble(txtProtein.getText());
                double carbs        = txtCarbs.getText().isEmpty()   ? 0 : Double.parseDouble(txtCarbs.getText());
                double fat          = txtFat.getText().isEmpty()     ? 0 : Double.parseDouble(txtFat.getText());

                Meal m = new Meal();
                m.setMealName(txtMealName.getText());
                m.setCaloriesPerGram(kcalPer100g);
                m.setProteinPer100g(protein);
                m.setCarbsPer100g(carbs);
                m.setFatPer100g(fat);
                int mealId = new MealDB().insertMeal(m);

                impl.FoodDB fdb = new impl.FoodDB();
                fdb.insertIfNew(new models.Food(txtMealName.getText(), kcalPer100g, protein, carbs, fat));
                FoodImporter.appendIfNew(txtMealName.getText(), kcalPer100g, protein, carbs, fat);

                DailyMealLog dml = new DailyMealLog();
                dml.setMealId(mealId);
                dml.setUserId(ids);
                dml.setTotalCalorieIntake(totalKcal);
                new MealLogDB().insertDailyLog(dml);

                mealFeedback.setForeground(SUCCESS);
                mealFeedback.setText(String.format("✓  %s — %.0fg = %.1f kcal logged!",
                    txtMealName.getText(), grams, totalKcal));

                txtMealName.setText(""); txtCalPer100g.setText(""); txtWeight.setText("");
                txtProtein.setText(""); txtCarbs.setText(""); txtFat.setText("");

                // Refresh summary in background
                SwingUtilities.invokeLater(() -> refreshSummary());

            } catch (NumberFormatException ex) {
                mealFeedback.setText("Numeric values required for calories, weight, and macros.");
            }
        }
    }

    class UpdateProfileListener implements ActionListener {
        @Override public void actionPerformed(ActionEvent e) {
            profileFeedback.setForeground(DANGER);
            try {
                UserDB udb = new UserDB();
                User u = udb.getById(ids);
                u.setName(tfName.getText().trim());
                u.setGender((String) cbGender.getSelectedItem());
                u.setAge(Integer.parseInt(tfAge.getText().trim()));
                u.setPassword(new String(tfPassword.getPassword()));
                if (!tfHeight.getText().trim().isEmpty())
                    u.setHeight(Double.parseDouble(tfHeight.getText().trim()));
                if (!tfCalGoal.getText().trim().isEmpty())
                    u.setCalorieGoal(Integer.parseInt(tfCalGoal.getText().trim()));
                udb.update(u);

                profileFeedback.setForeground(SUCCESS);
                profileFeedback.setText("✓  Profile saved successfully.");
                SwingUtilities.invokeLater(() -> refreshSummary());
            } catch (NumberFormatException ex) {
                profileFeedback.setText("Age, height, and calorie goal must be numbers.");
            }
        }
    }
}