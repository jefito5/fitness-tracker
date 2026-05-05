package gui;

import gui.components.CardPanel;
import gui.components.RoundedButton;
import gui.components.StatCard;
import gui.components.StyledTextField;
import gui.components.UITheme;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import impl.ExerciseLogDB;
import impl.FoodDB;
import impl.MealDB;
import impl.MealLogDB;
import impl.UserDB;
import impl.WaistDB;
import impl.WeightDB;
import models.DailyMealLog;
import models.Food;
import models.Meal;
import models.User;
import models.Waist;
import models.Weight;

/**
 * UI-5: Redesigned Daily Log / Track Progress screen.
 *
 * Card-based layout with section-separated meal &amp; exercise logs, a sticky
 * date selector header, colour-coded calorie totals against the user's daily
 * goal, and inline Add/Update/Delete actions per section.
 *
 * Behaviour, field names, and inner listener classes are unchanged from the
 * previous implementation so the data flow remains identical.
 */
public class trackProgress {

    private JFrame trackFrame;

    // Weight inputs
    private JTextField txtmorningW;
    private JTextField txtEveningW;
    private JTextField txtMorningWa;
    private JTextField txtEveningWa;

    // Tables
    private JTable table;
    private JTable table2;

    // Meal entry inputs
    private JTextField txtmealName;
    private JTextField txtmealcalorie;
    private JTextField txtintake;
    private JTextField mealsID;
    private JTextField exerciseID;
    private JTextField txtuserid;

    private JButton btnDeleteE;
    private JComboBox<String> dateComboBox;
    private JLabel lblSelectedDate;
    private String currentViewDate;
    private int get;

    // Header summary widgets (replace old labels)
    private StatCard cardCalories;
    private StatCard cardProtein;
    private StatCard cardCarbs;
    private StatCard cardFat;

    // Inline status feedback per section
    private JLabel mealStatus;
    private JLabel exerciseStatus;
    private JLabel weightStatus;

    // User's daily calorie goal — used to colour-code the calorie card.
    private double dailyCalorieGoal = 2000;

    // Tracks macros from the food-search dialog so InsertDailymealListener can save them
    private double selectedProtein = 0;
    private double selectedCarbs   = 0;
    private double selectedFat     = 0;

    // Legacy macro labels kept as no-op references so refreshMacroSummary still works.
    private JLabel lblProteinVal;
    private JLabel lblCarbsVal;
    private JLabel lblFatVal;

    public trackProgress(int gets) {
        get = gets;
        currentViewDate = String.valueOf(LocalDate.now());
        loadCalorieGoal();
        initialize();
        Show_Meals_In_JTable();
        Show_Exercise_In_JTable();
        Show_MealLog_In_JTable();
        refreshMacroSummary();
    }

    private void loadCalorieGoal() {
        try {
            User u = new UserDB().getById(get);
            if (u != null && u.getCalorieGoal() > 0) dailyCalorieGoal = u.getCalorieGoal();
        } catch (Exception ignored) {}
    }

    // ════════════════════════════════════════════════════════════════════
    //  LAYOUT
    // ════════════════════════════════════════════════════════════════════
    private void initialize() {
        trackFrame = new JFrame("Daily Record");
        trackFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        trackFrame.setMinimumSize(new Dimension(1100, 720));
        trackFrame.setSize(1180, 780);
        trackFrame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(UITheme.SPACE_LG, UITheme.SPACE_LG));
        root.setBackground(UITheme.BACKGROUND);
        root.setBorder(UITheme.padding(UITheme.SPACE_LG));
        trackFrame.setContentPane(root);

        root.add(buildHeader(), BorderLayout.NORTH);

        JPanel body = new JPanel(new GridLayout(1, 2, UITheme.SPACE_LG, 0));
        body.setOpaque(false);
        body.add(buildMealsCard());

        JPanel rightCol = new JPanel(new BorderLayout(0, UITheme.SPACE_LG));
        rightCol.setOpaque(false);
        rightCol.add(buildExercisesCard(), BorderLayout.CENTER);
        rightCol.add(buildWeightCard(),    BorderLayout.SOUTH);
        body.add(rightCol);

        root.add(body, BorderLayout.CENTER);

        // Hidden state fields preserved for listener compatibility
        mealsID    = new JTextField();
        exerciseID = new JTextField();
        txtuserid  = new JTextField(String.valueOf(get));
        // Legacy-label refs (point at the StatCard captions so old code paths still no-op safely)
        lblProteinVal = new JLabel();
        lblCarbsVal   = new JLabel();
        lblFatVal     = new JLabel();
        // Legacy waist fields kept (functionality not yet wired in original code either)
        txtMorningWa = new JTextField();
        txtEveningWa = new JTextField();

        refreshDateComboBox();
        trackFrame.setVisible(true);
    }

    // ── HEADER ──────────────────────────────────────────────────────────
    private JComponent buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        // Top row: title + history controls
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);

        JPanel titleBlock = new JPanel();
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.setOpaque(false);

        JLabel title = new JLabel("Daily Record");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblSelectedDate = new JLabel("Viewing: " + currentViewDate);
        lblSelectedDate.setFont(UITheme.FONT_CAPTION);
        lblSelectedDate.setForeground(UITheme.TEXT_SECONDARY);
        lblSelectedDate.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblSelectedDate.setBorder(UITheme.padding(2, 0, 0, 0));

        titleBlock.add(title);
        titleBlock.add(lblSelectedDate);
        topRow.add(titleBlock, BorderLayout.WEST);

        // History controls (Back, history dropdown, Show, Today)
        JPanel hist = new JPanel(new FlowLayout(FlowLayout.RIGHT, UITheme.SPACE_SM, 0));
        hist.setOpaque(false);

        JLabel lblHistory = new JLabel("History:");
        lblHistory.setFont(UITheme.FONT_BODY_BOLD);
        lblHistory.setForeground(UITheme.TEXT_SECONDARY);
        hist.add(lblHistory);

        dateComboBox = new JComboBox<>();
        dateComboBox.setFont(UITheme.FONT_BODY);
        dateComboBox.setPreferredSize(new Dimension(160, 30));
        hist.add(dateComboBox);

        RoundedButton btnLoadHistory = new RoundedButton("Show", RoundedButton.Variant.OUTLINE);
        btnLoadHistory.setBorder(UITheme.padding(6, 14));
        btnLoadHistory.addActionListener(e -> {
            String selected = (String) dateComboBox.getSelectedItem();
            if (selected != null && !selected.isEmpty()) {
                currentViewDate = selected;
                lblSelectedDate.setText("Viewing: " + currentViewDate);
                Show_Exercise_In_JTable();
                Show_MealLog_In_JTable();
                refreshMacroSummary();
            }
        });
        hist.add(btnLoadHistory);

        RoundedButton btnTodayBtn = new RoundedButton("Today", RoundedButton.Variant.PRIMARY);
        btnTodayBtn.setBorder(UITheme.padding(6, 14));
        btnTodayBtn.addActionListener(e -> {
            currentViewDate = String.valueOf(LocalDate.now());
            lblSelectedDate.setText("Viewing: " + currentViewDate);
            refreshDateComboBox();
            Show_Exercise_In_JTable();
            Show_MealLog_In_JTable();
            refreshMacroSummary();
        });
        hist.add(btnTodayBtn);

        RoundedButton btnBack = new RoundedButton("Back", RoundedButton.Variant.SECONDARY);
        btnBack.setBorder(UITheme.padding(6, 14));
        btnBack.addActionListener(e -> trackFrame.dispose());
        hist.add(btnBack);

        topRow.add(hist, BorderLayout.EAST);
        header.add(topRow, BorderLayout.NORTH);

        // Bottom row: 4 StatCards
        JPanel cards = new JPanel(new GridLayout(1, 4, UITheme.SPACE_MD, 0));
        cards.setOpaque(false);
        cards.setBorder(UITheme.padding(UITheme.SPACE_LG, 0, 0, 0));

        cardCalories = new StatCard("Calories");
        cardProtein  = new StatCard("Protein");
        cardCarbs    = new StatCard("Carbs");
        cardFat      = new StatCard("Fat");
        cards.add(cardCalories);
        cards.add(cardProtein);
        cards.add(cardCarbs);
        cards.add(cardFat);

        header.add(cards, BorderLayout.CENTER);
        return header;
    }

    // ── MEALS CARD ──────────────────────────────────────────────────────
    private JComponent buildMealsCard() {
        CardPanel card = new CardPanel(true);
        card.setLayout(new BorderLayout(0, UITheme.SPACE_MD));
        card.setBorder(UITheme.padding(UITheme.SPACE_LG));

        card.add(sectionHeader("Meals", "Today's food intake"), BorderLayout.NORTH);

        // Table
        table = new JTable();
        table.setModel(new DefaultTableModel(new Object[][]{},
                new String[]{"Meal ID", "Meals Name", "Kcal/100g", "Total kcal"}));
        styleTable(table);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) { UsersMouseClicked(evt); }
        });
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        tableScroll.getViewport().setBackground(UITheme.SURFACE);
        card.add(tableScroll, BorderLayout.CENTER);

        // South: form + actions + status
        JPanel south = new JPanel();
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
        south.setOpaque(false);
        south.setBorder(UITheme.padding(UITheme.SPACE_MD, 0, 0, 0));

        JLabel addHeading = new JLabel("Add a meal");
        addHeading.setFont(UITheme.FONT_HEADING);
        addHeading.setForeground(UITheme.TEXT_PRIMARY);
        addHeading.setAlignmentX(Component.LEFT_ALIGNMENT);
        addHeading.setBorder(UITheme.padding(0, 0, UITheme.SPACE_SM, 0));
        south.add(addHeading);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setAlignmentX(Component.LEFT_ALIGNMENT);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(UITheme.SPACE_XS, 0, UITheme.SPACE_XS, UITheme.SPACE_SM);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0; c.weightx = 0;
        form.add(fieldLabel("Meal name"), c);
        c.gridx = 1; c.weightx = 1;
        txtmealName = new StyledTextField("e.g. Chicken breast");
        form.add(txtmealName, c);
        c.gridx = 2; c.weightx = 0;
        RoundedButton btnSearchDB = new RoundedButton("Search DB", RoundedButton.Variant.OUTLINE);
        btnSearchDB.setBorder(UITheme.padding(6, 14));
        btnSearchDB.addActionListener(e -> openFoodSearchDialog());
        form.add(btnSearchDB, c);

        c.gridx = 0; c.gridy = 1; c.weightx = 0;
        form.add(fieldLabel("Calories / 100g"), c);
        c.gridx = 1; c.gridwidth = 2; c.weightx = 1;
        txtmealcalorie = new StyledTextField("e.g. 165");
        form.add(txtmealcalorie, c);
        c.gridwidth = 1;

        c.gridx = 0; c.gridy = 2; c.weightx = 0;
        form.add(fieldLabel("Amount (g)"), c);
        c.gridx = 1; c.gridwidth = 2; c.weightx = 1;
        txtintake = new StyledTextField("e.g. 200");
        form.add(txtintake, c);
        c.gridwidth = 1;

        south.add(form);

        // Actions row
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, UITheme.SPACE_SM, 0));
        actions.setOpaque(false);
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);
        actions.setBorder(UITheme.padding(UITheme.SPACE_SM, 0, 0, 0));

        RoundedButton btnInsert = new RoundedButton("Insert",  RoundedButton.Variant.PRIMARY);
        RoundedButton btnUpdate = new RoundedButton("Update",  RoundedButton.Variant.OUTLINE);
        RoundedButton btnDelete = new RoundedButton("Delete",  RoundedButton.Variant.SECONDARY);
        btnInsert.addActionListener(new InsertDailymealListener());
        btnUpdate.addActionListener(new UpdateMealListener());
        btnDelete.addActionListener(new DeleteMealListener());
        actions.add(btnInsert);
        actions.add(btnUpdate);
        actions.add(btnDelete);
        south.add(actions);

        mealStatus = new JLabel(" ");
        mealStatus.setFont(UITheme.FONT_CAPTION);
        mealStatus.setForeground(UITheme.TEXT_MUTED);
        mealStatus.setAlignmentX(Component.LEFT_ALIGNMENT);
        mealStatus.setBorder(UITheme.padding(UITheme.SPACE_SM, 0, 0, 0));
        south.add(mealStatus);

        card.add(south, BorderLayout.SOUTH);

        // Food-importer status: kept inline as a quiet caption.
        FoodDB fdb = new FoodDB();
        if (fdb.isEmpty()) {
            new services.FoodImporter(mealStatus).execute();
        }

        return card;
    }

    // ── EXERCISES CARD ──────────────────────────────────────────────────
    private JComponent buildExercisesCard() {
        CardPanel card = new CardPanel(true);
        card.setLayout(new BorderLayout(0, UITheme.SPACE_MD));
        card.setBorder(UITheme.padding(UITheme.SPACE_LG));

        card.add(sectionHeader("Exercises", "Logged workouts for the day"), BorderLayout.NORTH);

        table2 = new JTable();
        table2.setModel(new DefaultTableModel(new Object[][]{},
                new String[]{"Log ID", "Exercise Name", "Muscle Group", "Info", "Calories"}));
        styleTable(table2);
        table2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) { UsersMouseClicked1(evt); }
        });
        JScrollPane scroll = new JScrollPane(table2);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        scroll.getViewport().setBackground(UITheme.SURFACE);
        card.add(scroll, BorderLayout.CENTER);

        JPanel south = new JPanel();
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
        south.setOpaque(false);
        south.setBorder(UITheme.padding(UITheme.SPACE_MD, 0, 0, 0));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, UITheme.SPACE_SM, 0));
        actions.setOpaque(false);
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnDeleteE = new RoundedButton("Delete selected", RoundedButton.Variant.SECONDARY);
        btnDeleteE.addActionListener(new DeleteExerciseListener());
        actions.add(btnDeleteE);
        south.add(actions);

        exerciseStatus = new JLabel(" ");
        exerciseStatus.setFont(UITheme.FONT_CAPTION);
        exerciseStatus.setForeground(UITheme.TEXT_MUTED);
        exerciseStatus.setAlignmentX(Component.LEFT_ALIGNMENT);
        exerciseStatus.setBorder(UITheme.padding(UITheme.SPACE_SM, 0, 0, 0));
        south.add(exerciseStatus);

        card.add(south, BorderLayout.SOUTH);
        return card;
    }

    // ── WEIGHT CARD ─────────────────────────────────────────────────────
    private JComponent buildWeightCard() {
        CardPanel card = new CardPanel(true);
        card.setLayout(new BorderLayout(0, UITheme.SPACE_MD));
        card.setBorder(UITheme.padding(UITheme.SPACE_LG));

        card.add(sectionHeader("Weight", "Track morning &amp; evening (kg)"), BorderLayout.NORTH);

        JPanel body = new JPanel(new GridBagLayout());
        body.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(UITheme.SPACE_XS, 0, UITheme.SPACE_XS, UITheme.SPACE_SM);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0;
        body.add(fieldLabel("Morning"), c);
        c.gridx = 1; c.weightx = 1;
        txtmorningW = new StyledTextField("kg");
        body.add(txtmorningW, c);
        c.gridx = 2; c.weightx = 0;
        RoundedButton btnAddM = new RoundedButton("Add", RoundedButton.Variant.PRIMARY);
        btnAddM.setBorder(UITheme.padding(6, 14));
        btnAddM.addActionListener(new InsertWeightListener());
        body.add(btnAddM, c);

        c.gridx = 0; c.gridy = 1; c.weightx = 0;
        body.add(fieldLabel("Evening"), c);
        c.gridx = 1; c.weightx = 1;
        txtEveningW = new StyledTextField("kg");
        body.add(txtEveningW, c);
        c.gridx = 2; c.weightx = 0;
        RoundedButton btnAddE = new RoundedButton("Add", RoundedButton.Variant.PRIMARY);
        btnAddE.setBorder(UITheme.padding(6, 14));
        btnAddE.addActionListener(new UpdateWeightListener());
        body.add(btnAddE, c);

        card.add(body, BorderLayout.CENTER);

        weightStatus = new JLabel(" ");
        weightStatus.setFont(UITheme.FONT_CAPTION);
        weightStatus.setForeground(UITheme.TEXT_MUTED);
        weightStatus.setBorder(UITheme.padding(UITheme.SPACE_SM, 0, 0, 0));
        card.add(weightStatus, BorderLayout.SOUTH);

        return card;
    }

    // ── Helpers ─────────────────────────────────────────────────────────
    private JComponent sectionHeader(String title, String subtitle) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        JLabel t = new JLabel(title);
        t.setFont(UITheme.FONT_HEADING);
        t.setForeground(UITheme.TEXT_PRIMARY);
        t.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel s = new JLabel(subtitle);
        s.setFont(UITheme.FONT_CAPTION);
        s.setForeground(UITheme.TEXT_MUTED);
        s.setAlignmentX(Component.LEFT_ALIGNMENT);
        s.setBorder(UITheme.padding(2, 0, UITheme.SPACE_SM, 0));

        p.add(t);
        p.add(s);
        return p;
    }

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UITheme.FONT_SUBHEADING);
        l.setForeground(UITheme.TEXT_SECONDARY);
        l.setPreferredSize(new Dimension(120, 28));
        return l;
    }

    private void styleTable(JTable t) {
        t.setFont(UITheme.FONT_BODY);
        t.setRowHeight(26);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setBackground(UITheme.SURFACE);
        t.setSelectionBackground(new Color(UITheme.PRIMARY.getRed(), UITheme.PRIMARY.getGreen(), UITheme.PRIMARY.getBlue(), 40));
        t.setSelectionForeground(UITheme.TEXT_PRIMARY);
        t.setForeground(UITheme.TEXT_PRIMARY);
        t.setGridColor(UITheme.BORDER);

        DefaultTableCellRenderer alt = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean sel,
                                                           boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, value, sel, focus, row, col);
                if (!sel) c.setBackground(row % 2 == 0 ? UITheme.SURFACE : UITheme.SURFACE_ALT);
                setBorder(UITheme.padding(2, 8));
                return c;
            }
        };
        for (int i = 0; i < t.getColumnCount(); i++) {
            t.getColumnModel().getColumn(i).setCellRenderer(alt);
        }

        JTableHeader h = t.getTableHeader();
        h.setFont(UITheme.FONT_SUBHEADING);
        h.setBackground(UITheme.SURFACE_ALT);
        h.setForeground(UITheme.TEXT_SECONDARY);
        h.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER));
        h.setReorderingAllowed(false);
    }

    private void setMealStatus(String text, Color color) {
        if (mealStatus != null) {
            mealStatus.setText(text);
            mealStatus.setForeground(color);
        }
    }
    private void setExerciseStatus(String text, Color color) {
        if (exerciseStatus != null) {
            exerciseStatus.setText(text);
            exerciseStatus.setForeground(color);
        }
    }
    private void setWeightStatus(String text, Color color) {
        if (weightStatus != null) {
            weightStatus.setText(text);
            weightStatus.setForeground(color);
        }
    }

    // ════════════════════════════════════════════════════════════════════
    //  Food-search dialog (unchanged behaviour, lightly restyled)
    // ════════════════════════════════════════════════════════════════════
    private void openFoodSearchDialog() {
        JDialog searchDialog = new JDialog(trackFrame, "Search Food Database", true);
        searchDialog.setSize(560, 420);
        searchDialog.setLocationRelativeTo(trackFrame);
        searchDialog.getContentPane().setLayout(new BorderLayout(UITheme.SPACE_MD, UITheme.SPACE_MD));
        ((JComponent) searchDialog.getContentPane()).setBorder(UITheme.padding(UITheme.SPACE_LG));
        searchDialog.getContentPane().setBackground(UITheme.BACKGROUND);

        JPanel topPanel = new JPanel(new BorderLayout(UITheme.SPACE_SM, 0));
        topPanel.setOpaque(false);
        JLabel lbl = new JLabel("Search:");
        lbl.setFont(UITheme.FONT_BODY_BOLD);
        topPanel.add(lbl, BorderLayout.WEST);
        StyledTextField searchField = new StyledTextField("Type to filter…");
        topPanel.add(searchField, BorderLayout.CENTER);
        searchDialog.getContentPane().add(topPanel, BorderLayout.NORTH);

        String[] cols = {"Name", "Calories", "Protein", "Carbs", "Fat"};
        DefaultTableModel searchModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable searchTable = new JTable(searchModel);
        styleTable(searchTable);
        JScrollPane sp = new JScrollPane(searchTable);
        sp.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        searchDialog.getContentPane().add(sp, BorderLayout.CENTER);

        FoodDB fdb = new FoodDB();
        List<Food> allFoods = fdb.getAll();
        for (Food f : allFoods) {
            searchModel.addRow(new Object[]{f.getName(), f.getCalories(), f.getProtein(), f.getCarbs(), f.getFat()});
        }

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(searchModel);
        searchTable.setRowSorter(sorter);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { filter(); }
            public void removeUpdate(DocumentEvent e)  { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
            private void filter() {
                String text = searchField.getText();
                sorter.setRowFilter(text.trim().isEmpty() ? null : RowFilter.regexFilter("(?i)" + text));
            }
        });

        RoundedButton btnSelect = new RoundedButton("Select", RoundedButton.Variant.PRIMARY);
        btnSelect.addActionListener(e -> {
            int row = searchTable.getSelectedRow();
            if (row >= 0) {
                int modelRow = searchTable.convertRowIndexToModel(row);
                String name = searchModel.getValueAt(modelRow, 0).toString();
                String cal  = searchModel.getValueAt(modelRow, 1).toString();
                txtmealName.setText(name);
                txtmealcalorie.setText(cal);
                try { selectedProtein = Double.parseDouble(searchModel.getValueAt(modelRow, 2).toString()); } catch (Exception ex) { selectedProtein = 0; }
                try { selectedCarbs   = Double.parseDouble(searchModel.getValueAt(modelRow, 3).toString()); } catch (Exception ex) { selectedCarbs   = 0; }
                try { selectedFat     = Double.parseDouble(searchModel.getValueAt(modelRow, 4).toString()); } catch (Exception ex) { selectedFat     = 0; }
                searchDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(searchDialog, "Please select a food item.");
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        bottomPanel.add(btnSelect);
        searchDialog.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        searchDialog.setVisible(true);
    }

    // ════════════════════════════════════════════════════════════════════
    //  Data → views (preserved behaviour)
    // ════════════════════════════════════════════════════════════════════
    public void Show_Meals_In_JTable() {
        MealLogDB logDB = new MealLogDB();
        ArrayList<Object[]> logs = logDB.getMealLogsByDate(get, currentViewDate);

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (Object[] log : logs) {
            // log: [0]=mealID, [1]=MealName, [2]=CaloriePerGram, [3]=totalCalorieIntake
            model.addRow(new Object[]{log[0], log[1], log[2], String.format("%.1f", (double) log[3])});
        }
    }

    private void UsersMouseClicked(java.awt.event.MouseEvent evt) {
        int i = table.getSelectedRow();
        TableModel model = table.getModel();
        mealsID.setText(model.getValueAt(i, 0).toString());
        txtmealName.setText(model.getValueAt(i, 1).toString());
        txtmealcalorie.setText(model.getValueAt(i, 2).toString());
    }

    public void Show_Exercise_In_JTable() {
        ExerciseLogDB logDB = new ExerciseLogDB();
        ArrayList<Object[]> logs;
        if (currentViewDate != null && !currentViewDate.isEmpty()) {
            logs = logDB.getLogsByDate(get, currentViewDate);
        } else {
            logs = logDB.getTodayLogs(get);
        }
        DefaultTableModel model = (DefaultTableModel) table2.getModel();
        model.setRowCount(0);
        for (Object[] lr : logs) {
            String type = (String) lr[2];
            String muscleGroup = lr[7] != null ? (String) lr[7] : "General";
            if (muscleGroup.isEmpty()) muscleGroup = "General";
            String info;
            String calories;
            if ("Strength".equals(type)) {
                int reps = (int) lr[4];
                int kg   = (int) ((double) lr[5]);
                info = reps + " reps @ " + kg + " kg";
                calories = "-";
            } else {
                int mins = (int) ((double) lr[6]);
                info = mins + " min";
                calories = String.format("%.0f kcal", (double) lr[3]);
            }
            model.addRow(new Object[]{lr[0], lr[1], muscleGroup, info, calories});
        }
    }

    private void refreshDateComboBox() {
        if (dateComboBox == null) return;
        ExerciseLogDB logDB = new ExerciseLogDB();
        ArrayList<String> dates = logDB.getAllLogDates(get);
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        String today = String.valueOf(LocalDate.now());
        if (!dates.contains(today)) dates.add(0, today);
        for (String d : dates) model.addElement(d);
        dateComboBox.setModel(model);
        dateComboBox.setSelectedItem(currentViewDate);
    }

    public void Show_MealLog_In_JTable() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        Show_Meals_In_JTable();
        refreshMacroSummary();
    }

    /**
     * Refreshes the four header StatCards: Calories (with colour-coded
     * progress vs daily goal), Protein, Carbs, Fat.
     */
    public void refreshMacroSummary() {
        if (cardCalories == null) return; // guard against early calls
        MealLogDB mdb = new MealLogDB();
        double[] macros = mdb.getMacroSummary(get, currentViewDate);

        // Sum total calories for the day from the meal-log table.
        double totalCals = 0;
        ArrayList<Object[]> logs = mdb.getMealLogsByDate(get, currentViewDate);
        for (Object[] log : logs) totalCals += (double) log[3];

        cardCalories.setValue(String.format("%.0f / %.0f kcal", totalCals, dailyCalorieGoal));
        double pct = dailyCalorieGoal > 0 ? (totalCals / dailyCalorieGoal) * 100 : 0;
        cardCalories.setCaption(String.format("%.0f%% of daily goal", pct));

        // Colour: green ≤ 90%, amber 90–110%, red > 110%
        Color calColour;
        if (pct <= 90)       calColour = UITheme.SUCCESS;
        else if (pct <= 110) calColour = UITheme.WARNING;
        else                 calColour = UITheme.DANGER;
        cardCalories.setValueColor(calColour);

        cardProtein.setValue(String.format("%.1f g", macros[0]));
        cardProtein.setValueColor(UITheme.SUCCESS);
        cardProtein.setCaption("Today's intake");

        cardCarbs.setValue(String.format("%.1f g", macros[1]));
        cardCarbs.setValueColor(UITheme.WARNING);
        cardCarbs.setCaption("Today's intake");

        cardFat.setValue(String.format("%.1f g", macros[2]));
        cardFat.setValueColor(UITheme.DANGER);
        cardFat.setCaption("Today's intake");
    }

    private int selectedExerciseId = -1;

    private void UsersMouseClicked1(java.awt.event.MouseEvent evt) {
        int i = table2.getSelectedRow();
        if (i < 0) return;
        TableModel model = table2.getModel();
        selectedExerciseId = Integer.parseInt(model.getValueAt(i, 0).toString());
        exerciseID.setText(String.valueOf(selectedExerciseId));
    }

    // ════════════════════════════════════════════════════════════════════
    //  Listeners — preserved verbatim, except popup messages routed to
    //  the inline status label where convenient.
    // ════════════════════════════════════════════════════════════════════
    class UpdateMealListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (mealsID.getText().isEmpty() || txtmealName.getText().isEmpty() || txtmealcalorie.getText().isEmpty()) {
                    setMealStatus("Select a meal in the table to update.", UITheme.DANGER);
                } else {
                    MealDB udb = new MealDB();
                    int meals_id = Integer.parseInt(mealsID.getText());
                    Meal m = udb.getById(meals_id);
                    m.setMealName(txtmealName.getText());
                    int meals_calorie = Integer.parseInt(txtmealcalorie.getText());
                    m.setCaloriesPerGram(meals_calorie);
                    int rowUpdate = udb.updateMeal(m);
                    if (rowUpdate > 0) {
                        setMealStatus("Meal updated.", UITheme.SUCCESS);
                        ((DefaultTableModel) table.getModel()).setRowCount(0);
                        Show_Meals_In_JTable();
                        mealsID.setText("");
                        txtmealName.setText("");
                        txtmealcalorie.setText("");
                    } else {
                        setMealStatus("Failed to update meal.", UITheme.DANGER);
                    }
                }
            } catch (NumberFormatException eee) {
                setMealStatus("Please enter numeric values.", UITheme.DANGER);
            }
        }
    }

    class DeleteMealListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (mealsID.getText().isEmpty() || txtmealName.getText().isEmpty() || txtmealcalorie.getText().isEmpty()) {
                setMealStatus("Select a meal in the table to delete.", UITheme.DANGER);
            } else {
                MealDB udb = new MealDB();
                int meals_id = Integer.parseInt(mealsID.getText());
                Meal m = udb.getById(meals_id);
                int rowUpdate = udb.deleteMeal(m);
                if (rowUpdate > 0) {
                    setMealStatus("Meal deleted.", UITheme.SUCCESS);
                    ((DefaultTableModel) table.getModel()).setRowCount(0);
                    Show_Meals_In_JTable();
                    mealsID.setText("");
                    txtmealName.setText("");
                    txtmealcalorie.setText("");
                    refreshMacroSummary();
                } else {
                    setMealStatus("Failed to delete meal.", UITheme.DANGER);
                }
            }
        }
    }

    class UpdateExerciseListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) { /* not used */ }
    }

    class DeleteExerciseListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedExerciseId < 0) {
                setExerciseStatus("Select an exercise to delete.", UITheme.DANGER);
            } else {
                ExerciseLogDB logDB = new ExerciseLogDB();
                int rowUpdate = logDB.deleteTodayLog(selectedExerciseId);
                if (rowUpdate > 0) {
                    setExerciseStatus("Removed from today's log.", UITheme.SUCCESS);
                    Show_Exercise_In_JTable();
                    selectedExerciseId = -1;
                    exerciseID.setText("");
                } else {
                    setExerciseStatus("Failed to delete.", UITheme.DANGER);
                }
            }
        }
    }

    class InsertWeightListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (txtmorningW.getText().isEmpty()) {
                    setWeightStatus("Please enter morning weight.", UITheme.DANGER);
                } else {
                    Weight w = new Weight();
                    double mrng_wt = Double.parseDouble(txtmorningW.getText());
                    w.setWeightM(mrng_wt);
                    int user_id = Integer.parseInt(txtuserid.getText());
                    w.setUserId(user_id);
                    WeightDB udb = new WeightDB();
                    int rowUpdate = udb.insertWeight(w);
                    if (rowUpdate > 0) {
                        setWeightStatus("Weight added.", UITheme.SUCCESS);
                        txtmorningW.setText("");
                    } else {
                        setWeightStatus("Failed to add weight.", UITheme.DANGER);
                    }
                }
            } catch (NumberFormatException eee) {
                setWeightStatus("Please enter a numeric value.", UITheme.DANGER);
            }
        }
    }

    class UpdateWeightListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (txtEveningW.getText().isEmpty()) {
                    setWeightStatus("Please enter evening weight.", UITheme.DANGER);
                } else {
                    WeightDB udb = new WeightDB();
                    Weight w = new Weight();
                    double weight_ev = Double.parseDouble(txtEveningW.getText());
                    w.setWeightE(weight_ev);
                    ArrayList<Double> got = udb.getList();
                    double mWt = got.get(0);
                    double averageW = (weight_ev + mWt) / 2;
                    w.setAverage(averageW);
                    int user_id = Integer.parseInt(txtuserid.getText());
                    w.setUserId(user_id);
                    int rowUpdate = udb.updateWeight(w);
                    if (rowUpdate > 0) {
                        setWeightStatus("Evening weight saved (avg " + String.format("%.1f", averageW) + " kg).", UITheme.SUCCESS);
                        txtEveningW.setText("");
                    } else {
                        setWeightStatus("Failed to add weight.", UITheme.DANGER);
                    }
                }
            } catch (NumberFormatException ee) {
                setWeightStatus("Please enter a numeric value.", UITheme.DANGER);
            }
        }
    }

    class InsertWaistListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (txtMorningWa.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter morning waist");
                } else {
                    Waist ww = new Waist();
                    double mrng_wst = Double.parseDouble(txtMorningWa.getText());
                    ww.setWaistM(mrng_wst);
                    int user_id = Integer.parseInt(txtuserid.getText());
                    ww.setUserId(user_id);
                    WaistDB udb = new WaistDB();
                    int rowUpdate = udb.insertWaist(ww);
                    if (rowUpdate > 0) {
                        JOptionPane.showMessageDialog(null, "Waist Added!");
                        txtMorningWa.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to Add Waist!!");
                    }
                }
            } catch (NumberFormatException ee) {
                JOptionPane.showConfirmDialog(null, "Please enter numeric value", "Naughty", JOptionPane.CANCEL_OPTION);
            }
        }
    }

    class UpdateWaistListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (txtEveningWa.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter evening waist");
                } else {
                    WaistDB udb = new WaistDB();
                    Waist ww = new Waist();
                    double waist_ev = Double.parseDouble(txtEveningWa.getText());
                    ww.setWaistE(waist_ev);
                    ArrayList<Double> gots = udb.getLists();
                    double mWt = gots.get(0);
                    double averageW = (waist_ev + mWt) / 2;
                    ww.setAverage(averageW);
                    int user_id = Integer.parseInt(txtuserid.getText());
                    ww.setUserId(user_id);
                    int rowUpdate = udb.updateWaist(ww);
                    if (rowUpdate > 0) {
                        JOptionPane.showMessageDialog(null, "Waist Added");
                        txtEveningWa.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed add Waist");
                    }
                }
            } catch (NumberFormatException ee) {
                JOptionPane.showConfirmDialog(null, "Please enter numeric value", "Naughty", JOptionPane.CANCEL_OPTION);
            }
        }
    }

    class InsertDailymealListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (txtmealcalorie.getText().isEmpty() || txtintake.getText().isEmpty() || txtmealName.getText().isEmpty()) {
                    setMealStatus("All meal fields are required.", UITheme.DANGER);
                } else {
                    double calorieIntake = Double.parseDouble(txtmealcalorie.getText());
                    double mealAmount    = Double.parseDouble(txtintake.getText());
                    double totalCalorie  = (calorieIntake / 100.0) * mealAmount;

                    Meal m = new Meal();
                    m.setMealName(txtmealName.getText());
                    m.setCaloriesPerGram(calorieIntake);
                    m.setProteinPer100g(selectedProtein);
                    m.setCarbsPer100g(selectedCarbs);
                    m.setFatPer100g(selectedFat);
                    MealDB mdb = new MealDB();
                    int generatedMealId = mdb.insertMeal(m);

                    DailyMealLog dml = new DailyMealLog();
                    dml.setTotalCalorieIntake(totalCalorie);
                    int user_id = Integer.parseInt(txtuserid.getText());
                    dml.setUserId(user_id);
                    dml.setMealId(generatedMealId);

                    MealLogDB udb = new MealLogDB();
                    int rowUpdate = udb.insertDailyLog(dml);

                    if (rowUpdate > 0) {
                        setMealStatus(String.format("Logged: %.1f kcal added.", totalCalorie), UITheme.SUCCESS);
                        txtmealcalorie.setText("");
                        txtintake.setText("");
                        txtmealName.setText("");
                        selectedProtein = 0;
                        selectedCarbs   = 0;
                        selectedFat     = 0;
                        Show_MealLog_In_JTable();
                    } else {
                        setMealStatus("Failed to add log.", UITheme.DANGER);
                    }
                }
            } catch (NumberFormatException ee) {
                setMealStatus("Please enter numeric values.", UITheme.DANGER);
            }
        }
    }
}
