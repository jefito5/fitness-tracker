package gui;

import impl.ExerciseDB;
import impl.ExerciseLogDB;
import impl.PresetExerciseDB;
import models.DailyExerciseLog;
import models.Exercise;
import models.PresetExercise;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * FT-85: Workout type selection screen (Cardio / Strength).
 *
 * Strength: pick exercise from preset list (grouped by muscle), enter reps + weight
 * Cardio:   pick exercise from preset list, enter duration → calories auto-calculated
 *           Formula: calories = MET * bodyWeightKg * (durationMin / 60)
 */
public class ExerciseIUD {

    private JFrame frame;
    private int userId;
    private double userWeightKg;

    // ── Shared ───────────────────────────────────────────────────────────
    private JRadioButton rdoCardio;
    private JRadioButton rdoStrength;

    // ── Cardio ───────────────────────────────────────────────────────────
    private JPanel cardioPanel;
    private JComboBox<PresetExercise> cmbCardio;
    private JTextField txtDuration;
    private JLabel lblCalcResult;

    // ── Strength ─────────────────────────────────────────────────────────
    private JPanel strengthPanel;
    private JComboBox<String> cmbMuscleGroup;
    private JComboBox<PresetExercise> cmbStrength;
    private JTextField txtReps;
    private JTextField txtWeight;

    public ExerciseIUD(int userId, double userWeightKg) {
        this.userId = userId;
        this.userWeightKg = userWeightKg > 0 ? userWeightKg : 70.0;
        initialize();
    }

    private void initialize() {
        new PresetExerciseDB().seedIfEmpty();

        frame = new JFrame("Add Exercise");
        frame.setBounds(100, 100, 500, 420);
        frame.setLocation(420, 120);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        // ── Title ─────────────────────────────────────────────────────────
        JLabel lblTitle = new JLabel("Add Exercise");
        lblTitle.setFont(new Font("Verdana", Font.BOLD, 20));
        lblTitle.setBounds(20, 15, 250, 30);
        frame.getContentPane().add(lblTitle);

        // ── Workout type radio buttons ────────────────────────────────────
        JLabel lblType = new JLabel("Workout Type:");
        lblType.setFont(new Font("Verdana", Font.PLAIN, 14));
        lblType.setBounds(20, 60, 120, 25);
        frame.getContentPane().add(lblType);

        rdoCardio = new JRadioButton("Cardio");
        rdoCardio.setFont(new Font("Verdana", Font.PLAIN, 14));
        rdoCardio.setBounds(150, 59, 90, 25);
        rdoCardio.setSelected(true);
        frame.getContentPane().add(rdoCardio);

        rdoStrength = new JRadioButton("Strength");
        rdoStrength.setFont(new Font("Verdana", Font.PLAIN, 14));
        rdoStrength.setBounds(245, 59, 100, 25);
        frame.getContentPane().add(rdoStrength);

        ButtonGroup grp = new ButtonGroup();
        grp.add(rdoCardio);
        grp.add(rdoStrength);

        JSeparator sep = new JSeparator();
        sep.setBounds(20, 92, 445, 2);
        frame.getContentPane().add(sep);

        // ══════════════════════════════════════════════════════════════════
        // ── CARDIO PANEL ─────────────────────────────────────────────────
        // ══════════════════════════════════════════════════════════════════
        cardioPanel = new JPanel(null);
        cardioPanel.setBounds(15, 100, 460, 220);
        cardioPanel.setOpaque(false);
        frame.getContentPane().add(cardioPanel);

        JLabel lblExercise = new JLabel("Exercise:");
        lblExercise.setFont(new Font("Verdana", Font.PLAIN, 13));
        lblExercise.setBounds(5, 10, 80, 25);
        cardioPanel.add(lblExercise);

        ArrayList<PresetExercise> cardioList = new PresetExerciseDB().getByType("Cardio");
        cmbCardio = new JComboBox<>(cardioList.toArray(new PresetExercise[0]));
        cmbCardio.setFont(new Font("Verdana", Font.PLAIN, 13));
        cmbCardio.setBounds(90, 8, 360, 28);
        cardioPanel.add(cmbCardio);

        JLabel lblDuration = new JLabel("Duration (min):");
        lblDuration.setFont(new Font("Verdana", Font.PLAIN, 13));
        lblDuration.setBounds(5, 52, 130, 25);
        cardioPanel.add(lblDuration);

        txtDuration = new JTextField();
        txtDuration.setFont(new Font("Verdana", Font.PLAIN, 13));
        txtDuration.setBounds(140, 50, 100, 28);
        cardioPanel.add(txtDuration);

        lblCalcResult = new JLabel("≈ — kcal");
        lblCalcResult.setFont(new Font("Verdana", Font.BOLD, 14));
        lblCalcResult.setForeground(new Color(0, 130, 0));
        lblCalcResult.setBounds(255, 52, 200, 25);
        cardioPanel.add(lblCalcResult);

        JLabel lblFormula = new JLabel("Formula: MET × body weight × time");
        lblFormula.setFont(new Font("Verdana", Font.ITALIC, 11));
        lblFormula.setForeground(Color.GRAY);
        lblFormula.setBounds(5, 88, 350, 18);
        cardioPanel.add(lblFormula);

        JLabel lblBodyW = new JLabel("Your weight: " + (int) userWeightKg + " kg  (update in profile)");
        lblBodyW.setFont(new Font("Verdana", Font.PLAIN, 11));
        lblBodyW.setForeground(Color.GRAY);
        lblBodyW.setBounds(5, 106, 400, 18);
        cardioPanel.add(lblBodyW);

        // Live calorie update
        txtDuration.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { recalcCardio(); }
        });
        cmbCardio.addActionListener(e -> recalcCardio());

        // ══════════════════════════════════════════════════════════════════
        // ── STRENGTH PANEL ───────────────────────────────────────────────
        // ══════════════════════════════════════════════════════════════════
        strengthPanel = new JPanel(null);
        strengthPanel.setBounds(15, 100, 460, 220);
        strengthPanel.setOpaque(false);
        strengthPanel.setVisible(false);
        frame.getContentPane().add(strengthPanel);

        JLabel lblMuscle = new JLabel("Muscle Group:");
        lblMuscle.setFont(new Font("Verdana", Font.PLAIN, 13));
        lblMuscle.setBounds(5, 10, 115, 25);
        strengthPanel.add(lblMuscle);

        String[] muscleGroups = {"All", "chest", "lats", "middle back", "lower back",
                "shoulders", "traps", "biceps", "triceps",
                "quadriceps", "hamstrings", "glutes", "calves", "abdominals"};
        cmbMuscleGroup = new JComboBox<>(muscleGroups);
        cmbMuscleGroup.setFont(new Font("Verdana", Font.PLAIN, 13));
        cmbMuscleGroup.setBounds(125, 8, 180, 28);
        strengthPanel.add(cmbMuscleGroup);

        JLabel lblEx = new JLabel("Exercise:");
        lblEx.setFont(new Font("Verdana", Font.PLAIN, 13));
        lblEx.setBounds(5, 48, 80, 25);
        strengthPanel.add(lblEx);

        ArrayList<PresetExercise> strengthList = new PresetExerciseDB().getByType("Strength");
        cmbStrength = new JComboBox<>(strengthList.toArray(new PresetExercise[0]));
        cmbStrength.setFont(new Font("Verdana", Font.PLAIN, 13));
        cmbStrength.setBounds(90, 46, 360, 28);
        strengthPanel.add(cmbStrength);

        JLabel lblReps = new JLabel("Reps:");
        lblReps.setFont(new Font("Verdana", Font.PLAIN, 13));
        lblReps.setBounds(5, 90, 60, 25);
        strengthPanel.add(lblReps);

        txtReps = new JTextField();
        txtReps.setFont(new Font("Verdana", Font.PLAIN, 13));
        txtReps.setBounds(68, 88, 80, 28);
        strengthPanel.add(txtReps);

        JLabel lblWeight = new JLabel("Weight (kg):");
        lblWeight.setFont(new Font("Verdana", Font.PLAIN, 13));
        lblWeight.setBounds(165, 90, 100, 25);
        strengthPanel.add(lblWeight);

        txtWeight = new JTextField();
        txtWeight.setFont(new Font("Verdana", Font.PLAIN, 13));
        txtWeight.setBounds(268, 88, 80, 28);
        strengthPanel.add(txtWeight);

        JLabel lblSetsHint = new JLabel("Tip: add one entry per set (e.g. 3 sets = 3 entries)");
        lblSetsHint.setFont(new Font("Verdana", Font.ITALIC, 11));
        lblSetsHint.setForeground(Color.GRAY);
        lblSetsHint.setBounds(5, 126, 430, 18);
        strengthPanel.add(lblSetsHint);

        // Filter by muscle group
        cmbMuscleGroup.addActionListener(e -> filterStrengthByMuscle());

        // ── Radio toggle ─────────────────────────────────────────────────
        rdoCardio.addActionListener(e -> {
            cardioPanel.setVisible(true);
            strengthPanel.setVisible(false);
        });
        rdoStrength.addActionListener(e -> {
            cardioPanel.setVisible(false);
            strengthPanel.setVisible(true);
        });

        // ── INSERT button ────────────────────────────────────────────────
        JButton btnInsert = new JButton("INSERT EXERCISE");
        btnInsert.setFont(new Font("Verdana", Font.BOLD, 14));
        btnInsert.setBounds(130, 340, 220, 35);
        btnInsert.addActionListener(new InsertListener());
        frame.getContentPane().add(btnInsert);

        frame.setVisible(true);
    }

    /** Filters strength combo box by selected muscle group */
    private void filterStrengthByMuscle() {
        String selected = (String) cmbMuscleGroup.getSelectedItem();
        ArrayList<PresetExercise> all = new PresetExerciseDB().getByType("Strength");
        cmbStrength.removeAllItems();
        for (PresetExercise pe : all) {
            if ("All".equals(selected) || pe.getMuscleGroup().equalsIgnoreCase(selected)) {
                cmbStrength.addItem(pe);
            }
        }
    }

    /** Recalculates and displays cardio calories live */
    private void recalcCardio() {
        try {
            PresetExercise sel = (PresetExercise) cmbCardio.getSelectedItem();
            double minutes = Double.parseDouble(txtDuration.getText().trim());
            double met = sel != null ? sel.getMet() : 5.0;
            double kcal = met * userWeightKg * (minutes / 60.0);
            lblCalcResult.setText(String.format("≈ %.0f kcal", kcal));
        } catch (NumberFormatException ex) {
            lblCalcResult.setText("≈ — kcal");
        }
    }

    class InsertListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Exercise ex = new Exercise();
                double durationMinutes = 0;

                if (rdoCardio.isSelected()) {
                    if (txtDuration.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Please enter duration!");
                        return;
                    }
                    PresetExercise sel = (PresetExercise) cmbCardio.getSelectedItem();
                    durationMinutes = Double.parseDouble(txtDuration.getText().trim());
                    double met = sel != null ? sel.getMet() : 5.0;
                    double totalKcal = met * userWeightKg * (durationMinutes / 60.0);

                    ex.setExerciseName(sel != null ? sel.getExerciseName() : "Cardio");
                    ex.setWorkoutType("Cardio");
                    ex.setCalorieburn(totalKcal / Math.max(durationMinutes, 1));
                    ex.setReps(0);
                    ex.setWeightUsed(0);
                    ex.setMuscleGroup("General");

                } else {
                    if (txtReps.getText().trim().isEmpty() || txtWeight.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Please enter reps and weight!");
                        return;
                    }
                    PresetExercise sel = (PresetExercise) cmbStrength.getSelectedItem();
                    int reps = Integer.parseInt(txtReps.getText().trim());
                    double weightKg = Double.parseDouble(txtWeight.getText().trim());

                    ex.setExerciseName(sel != null ? sel.getExerciseName() : "Strength");
                    ex.setWorkoutType("Strength");
                    ex.setCalorieburn(0);
                    ex.setReps(reps);
                    ex.setWeightUsed(weightKg);
                    String chosenMuscle = (String) cmbMuscleGroup.getSelectedItem();
                    ex.setMuscleGroup("All".equals(chosenMuscle) ? (sel != null ? sel.getMuscleGroup() : "") : chosenMuscle);
                }

                int newId = new ExerciseDB().insertExercise(ex);

                // Iš karto įrašyti į dienos log'ą
                if (newId > 0) {
                    ExerciseLogDB logDB = new ExerciseLogDB();
                    logDB.ensureDurationColumn();
                    DailyExerciseLog log = new DailyExerciseLog();
                    log.setExerciseId(newId);
                    log.setUserId(userId);
                    double calPerMin = ex.getCalorieburn();
                    log.setTotalCalorieBurn(calPerMin * Math.max(durationMinutes, 1));
                    log.setDurationMinutes(durationMinutes);
                    logDB.insertDailyLog(log);
                }

                JOptionPane.showMessageDialog(frame, "Exercise Added!");
                txtDuration.setText("");
                txtReps.setText("");
                txtWeight.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter numeric values!");
            }
        }
    }
}