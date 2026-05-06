package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

import impl.ExerciseLogDB;
import impl.MealLogDB;
import impl.UserDB;
import impl.WeightDB;
import models.User;
import models.Weight;

import theme.UITheme;
import components.CardPanel;
import components.RoundedButton;
import components.SectionHeader;
import components.StyledRadioButton;

public class PeriodSelect {

    private JFrame frame;
    private JComboBox<Integer> comboBox;    // from year
    private JComboBox<Integer> comboBox_1;  // from month
    private JComboBox<Integer> comboBox_2;  // from day
    private JComboBox<Integer> comboBox_3;  // to year
    private JComboBox<Integer> comboBox_4;  // to month
    private JComboBox<Integer> comboBox_5;  // to day

    private StyledRadioButton rdbtnNewRadioButton;
    private StyledRadioButton rdbtnActive;
    private StyledRadioButton rdbtnModeratelyActive;

    private int getID;

    // Result labels
    private JLabel label;         // calorie intake necessary
    private JLabel lblNewLabel;   // weight loss/gain (period)
    private JLabel label_1;       // weight loss/gain (start to end)
    private JLabel label_2;       // total calorie intake
    private JLabel label_4;       // total calorie burned
    private JLabel label_6;       // calorie difference

    public PeriodSelect(int IDD) {
        getID = IDD;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Analyse Trends");
        frame.setSize(600, 760);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(UITheme.BACKGROUND);
        frame.getContentPane().setLayout(null);

        // ── Header bar ────────────────────────────────────────────────
        JPanel headerPanel = new JPanel(null);
        headerPanel.setBounds(0, 0, 600, 64);
        headerPanel.setBackground(UITheme.SURFACE);
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER));
        frame.getContentPane().add(headerPanel);

        RoundedButton btnBack = new RoundedButton("← Back");
        btnBack.setBounds(14, 14, 100, 36);
        btnBack.addActionListener(e -> frame.dispose());
        headerPanel.add(btnBack);

        SectionHeader lblTitle = new SectionHeader("Analyse Trends");
        lblTitle.setBounds(130, 16, 280, 32);
        headerPanel.add(lblTitle);

        // ── Activity level card ───────────────────────────────────────
        CardPanel activityCard = new CardPanel();
        activityCard.setBounds(18, 78, 564, 100);
        activityCard.setLayout(null);
        frame.getContentPane().add(activityCard);

        JLabel lblActivity = new JLabel("Activity Level Today");
        lblActivity.setFont(UITheme.FONT_SUBTITLE);
        lblActivity.setForeground(UITheme.TEXT_MAIN);
        lblActivity.setBounds(16, 12, 220, 24);
        activityCard.add(lblActivity);

        rdbtnNewRadioButton = new StyledRadioButton("Inactive");
        rdbtnNewRadioButton.setBounds(16, 48, 110, 32);
        rdbtnNewRadioButton.setBackground(UITheme.SURFACE);
        activityCard.add(rdbtnNewRadioButton);

        rdbtnModeratelyActive = new StyledRadioButton("Moderately Active");
        rdbtnModeratelyActive.setBounds(132, 48, 170, 32);
        rdbtnModeratelyActive.setBackground(UITheme.SURFACE);
        activityCard.add(rdbtnModeratelyActive);

        rdbtnActive = new StyledRadioButton("Active");
        rdbtnActive.setBounds(310, 48, 100, 32);
        rdbtnActive.setBackground(UITheme.SURFACE);
        activityCard.add(rdbtnActive);

        RoundedButton btnViewChart = new RoundedButton("View Chart");
        btnViewChart.setBounds(446, 46, 110, 34);
        activityCard.add(btnViewChart);

        ButtonGroup bg = new ButtonGroup();
        bg.add(rdbtnNewRadioButton);
        bg.add(rdbtnModeratelyActive);
        bg.add(rdbtnActive);

        // ── Period selection card ─────────────────────────────────────
        CardPanel periodCard = new CardPanel();
        periodCard.setBounds(18, 192, 564, 148);
        periodCard.setLayout(null);
        frame.getContentPane().add(periodCard);

        JLabel lblSelectPeriod = new JLabel("Select Period");
        lblSelectPeriod.setFont(UITheme.FONT_SUBTITLE);
        lblSelectPeriod.setForeground(UITheme.TEXT_MAIN);
        lblSelectPeriod.setBounds(16, 12, 200, 24);
        periodCard.add(lblSelectPeriod);

        // FROM label
        JLabel lblFrom = new JLabel("FROM");
        lblFrom.setFont(new Font(UITheme.FONT_SMALL.getName(), Font.BOLD, 11));
        lblFrom.setForeground(UITheme.TEXT_MUTED);
        lblFrom.setBounds(16, 48, 70, 16);
        periodCard.add(lblFrom);

        comboBox = createStyledComboBox();
        for (int i = 2024; i <= 2032; i++) comboBox.addItem(i);
        comboBox.setBounds(16, 68, 80, 34);
        periodCard.add(comboBox);

        JLabel lYear = tinyLabel("Year");
        lYear.setBounds(16, 104, 80, 14);
        periodCard.add(lYear);

        comboBox_1 = createStyledComboBox();
        for (int i = 1; i <= 12; i++) comboBox_1.addItem(i);
        comboBox_1.setBounds(104, 68, 72, 34);
        periodCard.add(comboBox_1);

        JLabel lMonth = tinyLabel("Month");
        lMonth.setBounds(104, 104, 72, 14);
        periodCard.add(lMonth);

        comboBox_2 = createStyledComboBox();
        for (int i = 1; i <= 31; i++) comboBox_2.addItem(i);
        comboBox_2.setBounds(184, 68, 68, 34);
        periodCard.add(comboBox_2);

        JLabel lDay = tinyLabel("Day");
        lDay.setBounds(184, 104, 68, 14);
        periodCard.add(lDay);

        // Arrow separator
        JLabel arrow = new JLabel("→");
        arrow.setFont(new Font("SansSerif", Font.PLAIN, 22));
        arrow.setForeground(UITheme.TEXT_MUTED);
        arrow.setBounds(260, 72, 34, 28);
        periodCard.add(arrow);

        // TO label
        JLabel lblTo = new JLabel("TO");
        lblTo.setFont(new Font(UITheme.FONT_SMALL.getName(), Font.BOLD, 11));
        lblTo.setForeground(UITheme.TEXT_MUTED);
        lblTo.setBounds(302, 48, 70, 16);
        periodCard.add(lblTo);

        comboBox_3 = createStyledComboBox();
        for (int i = 2024; i <= 2032; i++) comboBox_3.addItem(i);
        comboBox_3.setBounds(302, 68, 80, 34);
        periodCard.add(comboBox_3);

        JLabel lYear2 = tinyLabel("Year");
        lYear2.setBounds(302, 104, 80, 14);
        periodCard.add(lYear2);

        comboBox_4 = createStyledComboBox();
        for (int i = 1; i <= 12; i++) comboBox_4.addItem(i);
        comboBox_4.setBounds(390, 68, 72, 34);
        periodCard.add(comboBox_4);

        JLabel lMonth2 = tinyLabel("Month");
        lMonth2.setBounds(390, 104, 72, 14);
        periodCard.add(lMonth2);

        comboBox_5 = createStyledComboBox();
        for (int i = 1; i <= 31; i++) comboBox_5.addItem(i);
        comboBox_5.setBounds(470, 68, 68, 34);
        periodCard.add(comboBox_5);

        JLabel lDay2 = tinyLabel("Day");
        lDay2.setBounds(470, 104, 68, 14);
        periodCard.add(lDay2);

        // Proceed button
        RoundedButton btnProceed = new RoundedButton("Proceed");
        btnProceed.setBounds(18, 354, 564, 44);
        frame.getContentPane().add(btnProceed);

        // ── Results card ──────────────────────────────────────────────
        CardPanel resultsCard = new CardPanel();
        resultsCard.setBounds(18, 412, 564, 300);
        resultsCard.setLayout(null);
        frame.getContentPane().add(resultsCard);

        JLabel lblReport = new JLabel("Progress Report");
        lblReport.setFont(UITheme.FONT_SUBTITLE);
        lblReport.setForeground(UITheme.TEXT_MAIN);
        lblReport.setBounds(16, 14, 280, 26);
        resultsCard.add(lblReport);

        // Calorie intake necessary row
        JLabel lblCalTitle = new JLabel("Daily Calorie Target");
        lblCalTitle.setFont(UITheme.FONT_SMALL);
        lblCalTitle.setForeground(UITheme.TEXT_MUTED);
        lblCalTitle.setBounds(16, 52, 220, 20);
        resultsCard.add(lblCalTitle);

        label = new JLabel("—");
        label.setFont(UITheme.FONT_SUBTITLE);
        label.setForeground(UITheme.PRIMARY);
        label.setBounds(16, 74, 260, 26);
        resultsCard.add(label);

        // Divider
        JSeparator sep = new JSeparator();
        sep.setBounds(16, 112, 532, 1);
        sep.setForeground(UITheme.BORDER);
        resultsCard.add(sep);

        // Left column – Weight (x=16, width=250)
        JLabel lblWeightSummary = new JLabel("Weight Summary");
        lblWeightSummary.setFont(new Font(UITheme.FONT_SMALL.getName(), Font.BOLD, 13));
        lblWeightSummary.setForeground(UITheme.TEXT_MUTED);
        lblWeightSummary.setBounds(16, 124, 220, 20);
        resultsCard.add(lblWeightSummary);

        JLabel lblPeriodLbl = new JLabel("Selected Period");
        lblPeriodLbl.setFont(UITheme.FONT_SMALL);
        lblPeriodLbl.setForeground(UITheme.TEXT_MUTED);
        lblPeriodLbl.setBounds(16, 154, 160, 18);
        resultsCard.add(lblPeriodLbl);

        lblNewLabel = new JLabel("—");
        lblNewLabel.setFont(new Font(UITheme.FONT_REGULAR.getName(), Font.BOLD, 15));
        lblNewLabel.setForeground(UITheme.TEXT_MAIN);
        lblNewLabel.setBounds(16, 174, 220, 24);
        resultsCard.add(lblNewLabel);

        JLabel lblTotalLbl = new JLabel("All Time");
        lblTotalLbl.setFont(UITheme.FONT_SMALL);
        lblTotalLbl.setForeground(UITheme.TEXT_MUTED);
        lblTotalLbl.setBounds(16, 210, 160, 18);
        resultsCard.add(lblTotalLbl);

        label_1 = new JLabel("—");
        label_1.setFont(new Font(UITheme.FONT_REGULAR.getName(), Font.BOLD, 15));
        label_1.setForeground(UITheme.TEXT_MAIN);
        label_1.setBounds(16, 230, 220, 24);
        resultsCard.add(label_1);

        // Vertical divider
        JSeparator vSep = new JSeparator(SwingConstants.VERTICAL);
        vSep.setBounds(282, 120, 1, 148);
        vSep.setForeground(UITheme.BORDER);
        resultsCard.add(vSep);

        // Right column – Calories (x=298)
        JLabel lblCalInfo = new JLabel("Calorie Info (Today)");
        lblCalInfo.setFont(new Font(UITheme.FONT_SMALL.getName(), Font.BOLD, 13));
        lblCalInfo.setForeground(UITheme.TEXT_MUTED);
        lblCalInfo.setBounds(298, 124, 240, 20);
        resultsCard.add(lblCalInfo);

        JLabel lblIntakeLbl = new JLabel("Intake");
        lblIntakeLbl.setFont(UITheme.FONT_SMALL);
        lblIntakeLbl.setForeground(UITheme.TEXT_MUTED);
        lblIntakeLbl.setBounds(298, 154, 120, 18);
        resultsCard.add(lblIntakeLbl);

        label_2 = new JLabel("—");
        label_2.setFont(new Font(UITheme.FONT_REGULAR.getName(), Font.BOLD, 15));
        label_2.setForeground(UITheme.TEXT_MAIN);
        label_2.setBounds(298, 174, 240, 24);
        resultsCard.add(label_2);

        JLabel lblBurnedLbl = new JLabel("Burned");
        lblBurnedLbl.setFont(UITheme.FONT_SMALL);
        lblBurnedLbl.setForeground(UITheme.TEXT_MUTED);
        lblBurnedLbl.setBounds(298, 210, 120, 18);
        resultsCard.add(lblBurnedLbl);

        label_4 = new JLabel("—");
        label_4.setFont(new Font(UITheme.FONT_REGULAR.getName(), Font.BOLD, 15));
        label_4.setForeground(UITheme.TEXT_MAIN);
        label_4.setBounds(298, 230, 240, 24);
        resultsCard.add(label_4);

        JLabel lblNetLbl = new JLabel("Net Balance");
        lblNetLbl.setFont(UITheme.FONT_SMALL);
        lblNetLbl.setForeground(UITheme.TEXT_MUTED);
        lblNetLbl.setBounds(16, 266, 160, 18);
        resultsCard.add(lblNetLbl);

        label_6 = new JLabel("—");
        label_6.setFont(new Font(UITheme.FONT_SUBTITLE.getName(), Font.BOLD, 16));
        label_6.setForeground(UITheme.PRIMARY);
        label_6.setBounds(16, 286, 260, 26);
        resultsCard.add(label_6);

        // ── Wire up listeners ─────────────────────────────────────────
        btnViewChart.addActionListener(e -> {
            new TrendLine(getID);
            frame.setVisible(false);
        });

        btnProceed.addActionListener(e -> {
            calorieInformations();
            activityLevelSelection();
            try {
                int fromYear  = (int) comboBox.getSelectedItem();
                int fromMonth = (int) comboBox_1.getSelectedItem();
                int fromDay   = (int) comboBox_2.getSelectedItem();
                int toYear    = (int) comboBox_3.getSelectedItem();
                int toMonth   = (int) comboBox_4.getSelectedItem();
                int toDay     = (int) comboBox_5.getSelectedItem();

                String fromDate = String.format("%04d-%02d-%02d", fromYear, fromMonth, fromDay);
                String toDate   = String.format("%04d-%02d-%02d", toYear,   toMonth,   toDay);

                if (fromDate.compareTo(toDate) > 0) {
                    JOptionPane.showMessageDialog(frame,
                        "FROM date must be before or equal to TO date.",
                        "Invalid Range", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Weight wt = new Weight();
                wt.setPeriod1(fromDate);
                wt.setPeriod2(toDate);
                wt.setUserId(getID);

                WeightDB wdb = new WeightDB();
                ArrayList<Double> averages = wdb.getWeight(wt);

                if (averages == null || averages.isEmpty()) {
                    lblNewLabel.setText("No data in range");
                } else {
                    ArrayList<Double> valid = new ArrayList<>();
                    for (double v : averages) { if (v > 0) valid.add(v); }
                    if (valid.isEmpty()) {
                        lblNewLabel.setText("No valid data");
                    } else {
                        double periodChange = valid.get(0) - valid.get(valid.size() - 1);
                        lblNewLabel.setText(String.format("%.2f kg", periodChange));
                        lblNewLabel.setForeground(periodChange >= 0 ? UITheme.SUCCESS : UITheme.ERROR);
                    }
                }

                ArrayList<Double> allEntries = wdb.getStartEndForUser(getID);
                if (allEntries == null || allEntries.isEmpty()) {
                    label_1.setText("No data available");
                } else {
                    double totalChange = allEntries.get(0) - allEntries.get(allEntries.size() - 1);
                    label_1.setText(String.format("%.2f kg", totalChange));
                    label_1.setForeground(totalChange >= 0 ? UITheme.SUCCESS : UITheme.ERROR);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame,
                    "Could not calculate weight change. Please ensure a valid date range is selected.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }

    // ── Helpers ──────────────────────────────────────────────────────────

    private JComboBox<Integer> createStyledComboBox() {
        JComboBox<Integer> cb = new JComboBox<>();
        cb.setFont(UITheme.FONT_REGULAR);
        cb.setForeground(UITheme.TEXT_MAIN);
        cb.setBackground(UITheme.SURFACE);
        cb.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER, 1),
            BorderFactory.createEmptyBorder(2, 4, 2, 4)
        ));
        cb.setFocusable(false);
        return cb;
    }

    private JLabel tinyLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font(UITheme.FONT_SMALL.getName(), Font.PLAIN, 11));
        l.setForeground(UITheme.TEXT_MUTED);
        return l;
    }

    // ── Business logic (unchanged) ────────────────────────────────────────

    public void activityLevelSelection() {
        UserDB udb = new UserDB();
        User u = udb.getById(getID);
        WeightDB wdb = new WeightDB();
        ArrayList<Double> gets = wdb.getAverages();

        if (gets == null || gets.isEmpty()) {
            label.setText("No weight data found");
            return;
        }

        if (!rdbtnNewRadioButton.isSelected() && !rdbtnModeratelyActive.isSelected() && !rdbtnActive.isSelected()) {
            JOptionPane.showMessageDialog(null, "Please select your activity level");
            return;
        }

        double weight = gets.get(0);
        double cal;
        boolean isMale = u != null && u.getGender().equalsIgnoreCase("male");

        if (rdbtnNewRadioButton.isSelected()) {
            cal = isMale ? 5 * weight : 4 * weight;
        } else if (rdbtnModeratelyActive.isSelected()) {
            cal = isMale ? 6 * weight : 5 * weight;
        } else {
            cal = isMale ? 7.5 * weight : 6 * weight;
        }

        label.setText(String.format("%.0f kcal", cal));
    }

    public void calorieInformations() {
        try {
            MealLogDB mdb = new MealLogDB();
            User u = new User();
            u.setId(getID);

            ArrayList<Double> aa = mdb.getSum();
            double totalCalorieSum = 0;
            for (Double d : aa) totalCalorieSum += d;
            label_2.setText(String.format("%.0f kcal", totalCalorieSum));

            ExerciseLogDB edb = new ExerciseLogDB();
            ArrayList<Double> aea = edb.getburnSum();
            double totalCalorieBurn = 0;
            for (Double dd : aea) totalCalorieBurn += dd;
            label_4.setText(String.format("%.0f kcal", totalCalorieBurn));

            double diff = totalCalorieSum - totalCalorieBurn;
            label_6.setText(String.format("%.0f kcal", diff));
            label_6.setForeground(diff >= 0 ? UITheme.SUCCESS : UITheme.ERROR);

        } catch (Exception ee) {
            JOptionPane.showMessageDialog(null, "No meal or exercise data found for today.");
        }
    }
}