package gui;

import gui.components.CardPanel;
import gui.components.RoundedButton;
import gui.components.StyledTextField;
import gui.components.TagChip;
import gui.components.UITheme;
import impl.ExerciseDB;
import impl.ExerciseLogDB;
import impl.PresetExerciseDB;
import models.DailyExerciseLog;
import models.Exercise;
import models.PresetExercise;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * UI-6: Redesigned Exercise Browser.
 *
 * Two-pane layout — searchable / filterable exercise list on the left,
 * detail &amp; quick-log panel on the right. Preset and custom exercises are
 * shown together with a tag chip distinguishing them.
 *
 * FT-85: Workout type selection screen (Cardio / Strength).
 *  - Cardio:   pick exercise, enter duration → calories auto-calculated
 *              Formula: calories = MET * bodyWeightKg * (durationMin / 60)
 *  - Strength: pick exercise (filterable by muscle), enter reps + weight
 */
public class ExerciseIUD {

    private static final String FILTER_ALL      = "All";
    private static final String FILTER_CARDIO   = "Cardio";
    private static final String FILTER_STRENGTH = "Strength";

    /** Unified row model for the exercise list — wraps a preset OR a custom exercise. */
    private static final class Entry {
        final String name;
        final String type;        // "Cardio" / "Strength"
        final String muscleGroup;
        final double met;
        final boolean preset;
        final PresetExercise presetRef; // null if custom

        Entry(PresetExercise pe) {
            this.name        = pe.getExerciseName();
            this.type        = pe.getWorkoutType();
            this.muscleGroup = pe.getMuscleGroup();
            this.met         = pe.getMet();
            this.preset      = true;
            this.presetRef   = pe;
        }

        Entry(Exercise ex) {
            this.name        = ex.getExerciseName();
            this.type        = ex.getWorkoutType() == null ? "Cardio" : ex.getWorkoutType();
            this.muscleGroup = ex.getMuscleGroup();
            this.met         = 0;
            this.preset      = false;
            this.presetRef   = null;
        }
    }

    // ── Inputs ──────────────────────────────────────────────────────────
    private final int userId;
    private final double userWeightKg;

    // ── Top-level ───────────────────────────────────────────────────────
    private JFrame frame;

    // ── Left pane ───────────────────────────────────────────────────────
    private StyledTextField searchField;
    private RoundedButton chipAll, chipCardio, chipStrength;
    private String activeFilter = FILTER_ALL;
    private DefaultListModel<Entry> listModel;
    private JList<Entry> exerciseList;
    private List<Entry> allEntries = new ArrayList<>();

    // ── Right pane (detail / log) ───────────────────────────────────────
    private CardPanel detailCard;
    private JLabel selectedName;
    private JPanel chipsRow;
    private JLabel metaLabel;          // e.g. "MET 8.0  •  Body weight 70 kg"
    private JPanel formContainer;      // CardLayout: cardio | strength | empty
    private CardLayout formLayout;

    // Cardio inputs
    private StyledTextField txtDuration;
    private JLabel lblCalcResult;

    // Strength inputs
    private StyledTextField txtReps;
    private StyledTextField txtWeight;

    private RoundedButton btnInsert;
    private JLabel statusLabel;        // inline feedback (replaces popups)

    public ExerciseIUD(int userId, double userWeightKg) {
        this.userId = userId;
        this.userWeightKg = userWeightKg > 0 ? userWeightKg : 70.0;
        initialize();
    }

    private void initialize() {
        new PresetExerciseDB().seedIfEmpty();

        frame = new JFrame("Exercises");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setMinimumSize(new Dimension(850, 560));
        frame.setSize(960, 620);
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BACKGROUND);
        root.setBorder(UITheme.padding(UITheme.SPACE_LG));
        frame.setContentPane(root);

        root.add(buildHeader(), BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildLeftPane(), buildRightPane());
        split.setDividerLocation(330);
        split.setContinuousLayout(true);
        split.setBorder(null);
        split.setOpaque(false);
        split.setBackground(UITheme.BACKGROUND);
        root.add(split, BorderLayout.CENTER);

        loadEntries();
        applyFilter();

        frame.setVisible(true);
    }

    // ════════════════════════════════════════════════════════════════════
    //  HEADER
    // ════════════════════════════════════════════════════════════════════
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(UITheme.padding(0, 0, UITheme.SPACE_LG, 0));

        JPanel titleBlock = new JPanel();
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.setOpaque(false);

        JLabel title = new JLabel("Exercises");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Browse, filter, and log your workouts");
        subtitle.setFont(UITheme.FONT_CAPTION);
        subtitle.setForeground(UITheme.TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setBorder(UITheme.padding(2, 0, 0, 0));

        titleBlock.add(title);
        titleBlock.add(subtitle);
        header.add(titleBlock, BorderLayout.WEST);

        JPanel chips = new JPanel(new FlowLayout(FlowLayout.RIGHT, UITheme.SPACE_SM, 0));
        chips.setOpaque(false);
        chipAll      = filterChip(FILTER_ALL);
        chipCardio   = filterChip(FILTER_CARDIO);
        chipStrength = filterChip(FILTER_STRENGTH);
        chips.add(chipAll);
        chips.add(chipCardio);
        chips.add(chipStrength);
        header.add(chips, BorderLayout.EAST);

        return header;
    }

    private RoundedButton filterChip(String label) {
        RoundedButton b = new RoundedButton(label, RoundedButton.Variant.OUTLINE);
        b.setBorder(UITheme.padding(6, 14));
        if (label.equals(activeFilter)) b.setVariant(RoundedButton.Variant.PRIMARY);
        b.addActionListener(e -> {
            activeFilter = label;
            chipAll.setVariant(label.equals(FILTER_ALL)         ? RoundedButton.Variant.PRIMARY : RoundedButton.Variant.OUTLINE);
            chipCardio.setVariant(label.equals(FILTER_CARDIO)   ? RoundedButton.Variant.PRIMARY : RoundedButton.Variant.OUTLINE);
            chipStrength.setVariant(label.equals(FILTER_STRENGTH)? RoundedButton.Variant.PRIMARY : RoundedButton.Variant.OUTLINE);
            applyFilter();
        });
        return b;
    }

    // ════════════════════════════════════════════════════════════════════
    //  LEFT PANE — search + list
    // ════════════════════════════════════════════════════════════════════
    private JComponent buildLeftPane() {
        CardPanel left = new CardPanel(true);
        left.setLayout(new BorderLayout(0, UITheme.SPACE_MD));
        left.setBorder(UITheme.padding(UITheme.SPACE_LG));

        searchField = new StyledTextField("Search exercises…");
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate (DocumentEvent e) { applyFilter(); }
            public void removeUpdate (DocumentEvent e) { applyFilter(); }
            public void changedUpdate(DocumentEvent e) { applyFilter(); }
        });
        left.add(searchField, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        exerciseList = new JList<>(listModel);
        exerciseList.setCellRenderer(new EntryRenderer());
        exerciseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        exerciseList.setBackground(UITheme.SURFACE);
        exerciseList.setFixedCellHeight(58);
        exerciseList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) updateDetailPane(exerciseList.getSelectedValue());
        });

        JScrollPane scroll = new JScrollPane(exerciseList);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        scroll.getViewport().setBackground(UITheme.SURFACE);
        left.add(scroll, BorderLayout.CENTER);

        return left;
    }

    // ════════════════════════════════════════════════════════════════════
    //  RIGHT PANE — detail + quick-log form
    // ════════════════════════════════════════════════════════════════════
    private JComponent buildRightPane() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(UITheme.padding(0, UITheme.SPACE_LG, 0, 0));

        detailCard = new CardPanel(true);
        detailCard.setLayout(new BoxLayout(detailCard, BoxLayout.Y_AXIS));
        detailCard.setBorder(UITheme.padding(UITheme.SPACE_XL));

        selectedName = new JLabel("Select an exercise");
        selectedName.setFont(UITheme.FONT_DISPLAY);
        selectedName.setForeground(UITheme.TEXT_PRIMARY);
        selectedName.setAlignmentX(Component.LEFT_ALIGNMENT);

        chipsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, UITheme.SPACE_SM, 0));
        chipsRow.setOpaque(false);
        chipsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        chipsRow.setBorder(UITheme.padding(UITheme.SPACE_SM, 0, 0, 0));

        metaLabel = new JLabel(" ");
        metaLabel.setFont(UITheme.FONT_CAPTION);
        metaLabel.setForeground(UITheme.TEXT_SECONDARY);
        metaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        metaLabel.setBorder(UITheme.padding(UITheme.SPACE_MD, 0, 0, 0));

        JLabel formHeading = new JLabel("Log this exercise");
        formHeading.setFont(UITheme.FONT_HEADING);
        formHeading.setForeground(UITheme.TEXT_PRIMARY);
        formHeading.setAlignmentX(Component.LEFT_ALIGNMENT);
        formHeading.setBorder(UITheme.padding(UITheme.SPACE_XL, 0, UITheme.SPACE_MD, 0));

        formLayout = new CardLayout();
        formContainer = new JPanel(formLayout);
        formContainer.setOpaque(false);
        formContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        formContainer.add(buildEmptyForm(),    "empty");
        formContainer.add(buildCardioForm(),   "cardio");
        formContainer.add(buildStrengthForm(), "strength");
        formContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        statusLabel = new JLabel(" ");
        statusLabel.setFont(UITheme.FONT_CAPTION);
        statusLabel.setForeground(UITheme.TEXT_MUTED);
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statusLabel.setBorder(UITheme.padding(UITheme.SPACE_MD, 0, UITheme.SPACE_SM, 0));

        btnInsert = new RoundedButton("INSERT EXERCISE", RoundedButton.Variant.PRIMARY);
        btnInsert.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnInsert.setEnabled(false);
        btnInsert.addActionListener(e -> doInsert());

        detailCard.add(selectedName);
        detailCard.add(chipsRow);
        detailCard.add(metaLabel);
        detailCard.add(formHeading);
        detailCard.add(formContainer);
        detailCard.add(statusLabel);
        detailCard.add(btnInsert);
        detailCard.add(Box.createVerticalGlue());

        wrapper.add(detailCard, BorderLayout.CENTER);
        return wrapper;
    }

    private JComponent buildEmptyForm() {
        JLabel hint = new JLabel("← Pick an exercise from the list to log it.");
        hint.setFont(UITheme.FONT_BODY);
        hint.setForeground(UITheme.TEXT_MUTED);
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(hint, BorderLayout.NORTH);
        return p;
    }

    private JComponent buildCardioForm() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(UITheme.SPACE_XS, 0, UITheme.SPACE_XS, UITheme.SPACE_MD);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0; c.weightx = 0;
        p.add(fieldLabel("Duration (min)"), c);

        c.gridx = 1; c.weightx = 1;
        txtDuration = new StyledTextField("e.g. 30");
        txtDuration.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { recalcCardio(); }
        });
        p.add(txtDuration, c);

        c.gridx = 0; c.gridy = 1; c.weightx = 0;
        p.add(fieldLabel("Estimated burn"), c);

        c.gridx = 1; c.weightx = 1;
        lblCalcResult = new JLabel("≈ — kcal");
        lblCalcResult.setFont(UITheme.FONT_BODY_BOLD);
        lblCalcResult.setForeground(UITheme.SUCCESS);
        p.add(lblCalcResult, c);

        c.gridx = 0; c.gridy = 2; c.gridwidth = 2; c.weightx = 1;
        JLabel formula = new JLabel("Formula: MET × body weight × time");
        formula.setFont(UITheme.FONT_CAPTION);
        formula.setForeground(UITheme.TEXT_MUTED);
        p.add(formula, c);

        return p;
    }

    private JComponent buildStrengthForm() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(UITheme.SPACE_XS, 0, UITheme.SPACE_XS, UITheme.SPACE_MD);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0; c.weightx = 0;
        p.add(fieldLabel("Reps"), c);
        c.gridx = 1; c.weightx = 1;
        txtReps = new StyledTextField("e.g. 10");
        p.add(txtReps, c);

        c.gridx = 0; c.gridy = 1; c.weightx = 0;
        p.add(fieldLabel("Weight (kg)"), c);
        c.gridx = 1; c.weightx = 1;
        txtWeight = new StyledTextField("e.g. 60");
        p.add(txtWeight, c);

        c.gridx = 0; c.gridy = 2; c.gridwidth = 2; c.weightx = 1;
        JLabel hint = new JLabel("Tip: add one entry per set (3 sets = 3 entries)");
        hint.setFont(UITheme.FONT_CAPTION);
        hint.setForeground(UITheme.TEXT_MUTED);
        p.add(hint, c);

        return p;
    }

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UITheme.FONT_SUBHEADING);
        l.setForeground(UITheme.TEXT_SECONDARY);
        l.setPreferredSize(new Dimension(140, 28));
        return l;
    }

    // ════════════════════════════════════════════════════════════════════
    //  DATA — load + filter
    // ════════════════════════════════════════════════════════════════════
    private void loadEntries() {
        allEntries.clear();
        PresetExerciseDB pdb = new PresetExerciseDB();
        for (PresetExercise pe : pdb.getByType(FILTER_CARDIO))   allEntries.add(new Entry(pe));
        for (PresetExercise pe : pdb.getByType(FILTER_STRENGTH)) allEntries.add(new Entry(pe));
        for (Exercise ex : new ExerciseDB().getExercise())       allEntries.add(new Entry(ex));
    }

    private void applyFilter() {
        if (listModel == null) return;
        String q = searchField == null ? "" : searchField.getText().trim().toLowerCase();
        listModel.clear();
        for (Entry e : allEntries) {
            boolean typeOk = activeFilter.equals(FILTER_ALL) || activeFilter.equalsIgnoreCase(e.type);
            boolean searchOk = q.isEmpty()
                    || e.name.toLowerCase().contains(q)
                    || (e.muscleGroup != null && e.muscleGroup.toLowerCase().contains(q));
            if (typeOk && searchOk) listModel.addElement(e);
        }
    }

    // ════════════════════════════════════════════════════════════════════
    //  SELECTION → detail pane
    // ════════════════════════════════════════════════════════════════════
    private void updateDetailPane(Entry e) {
        chipsRow.removeAll();
        statusLabel.setText(" ");

        if (e == null) {
            selectedName.setText("Select an exercise");
            metaLabel.setText(" ");
            formLayout.show(formContainer, "empty");
            btnInsert.setEnabled(false);
        } else {
            selectedName.setText(e.name);
            chipsRow.add(TagChip.forType(e.type));
            TagChip muscleChip = TagChip.muscle(e.muscleGroup);
            if (muscleChip != null) chipsRow.add(muscleChip);
            chipsRow.add(e.preset ? TagChip.preset() : TagChip.custom());

            if (FILTER_CARDIO.equalsIgnoreCase(e.type)) {
                metaLabel.setText(String.format(
                        "MET %.1f  •  Body weight %d kg  (update in profile)",
                        e.met > 0 ? e.met : 5.0, (int) userWeightKg));
                formLayout.show(formContainer, "cardio");
                if (txtDuration != null) txtDuration.setText("");
                if (lblCalcResult != null) lblCalcResult.setText("≈ — kcal");
            } else {
                metaLabel.setText("Strength training — log per set");
                formLayout.show(formContainer, "strength");
                if (txtReps != null)   txtReps.setText("");
                if (txtWeight != null) txtWeight.setText("");
            }
            btnInsert.setEnabled(true);
        }
        chipsRow.revalidate();
        chipsRow.repaint();
    }

    private void recalcCardio() {
        Entry sel = exerciseList.getSelectedValue();
        if (sel == null || !FILTER_CARDIO.equalsIgnoreCase(sel.type)) return;
        try {
            double minutes = Double.parseDouble(txtDuration.getText().trim());
            double met = sel.met > 0 ? sel.met : 5.0;
            double kcal = met * userWeightKg * (minutes / 60.0);
            lblCalcResult.setText(String.format("≈ %.0f kcal", kcal));
            lblCalcResult.setForeground(UITheme.SUCCESS);
        } catch (NumberFormatException ex) {
            lblCalcResult.setText("≈ — kcal");
            lblCalcResult.setForeground(UITheme.TEXT_MUTED);
        }
    }

    // ════════════════════════════════════════════════════════════════════
    //  INSERT
    // ════════════════════════════════════════════════════════════════════
    private void doInsert() {
        Entry sel = exerciseList.getSelectedValue();
        if (sel == null) {
            setStatus("Select an exercise first.", UITheme.DANGER);
            return;
        }

        try {
            Exercise ex = new Exercise();
            double durationMinutes = 0;

            if (FILTER_CARDIO.equalsIgnoreCase(sel.type)) {
                String dur = txtDuration.getText().trim();
                if (dur.isEmpty()) { setStatus("Please enter duration.", UITheme.DANGER); return; }
                durationMinutes = Double.parseDouble(dur);
                if (durationMinutes <= 0) { setStatus("Duration must be positive.", UITheme.DANGER); return; }

                double met = sel.met > 0 ? sel.met : 5.0;
                double totalKcal = met * userWeightKg * (durationMinutes / 60.0);

                ex.setExerciseName(sel.name);
                ex.setWorkoutType(FILTER_CARDIO);
                ex.setCalorieburn(totalKcal / Math.max(durationMinutes, 1));
                ex.setReps(0);
                ex.setWeightUsed(0);
                ex.setMuscleGroup(sel.muscleGroup == null ? "General" : sel.muscleGroup);

            } else {
                String repsStr = txtReps.getText().trim();
                String wStr    = txtWeight.getText().trim();
                if (repsStr.isEmpty() || wStr.isEmpty()) {
                    setStatus("Please enter both reps and weight.", UITheme.DANGER);
                    return;
                }
                int reps = Integer.parseInt(repsStr);
                double weightKg = Double.parseDouble(wStr);
                if (reps <= 0 || weightKg < 0) {
                    setStatus("Reps must be positive and weight non-negative.", UITheme.DANGER);
                    return;
                }

                ex.setExerciseName(sel.name);
                ex.setWorkoutType(FILTER_STRENGTH);
                ex.setCalorieburn(0);
                ex.setReps(reps);
                ex.setWeightUsed(weightKg);
                ex.setMuscleGroup(sel.muscleGroup == null ? "" : sel.muscleGroup);
            }

            int newId = new ExerciseDB().insertExercise(ex);

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

            setStatus("Added: " + sel.name, UITheme.SUCCESS);
            if (txtDuration != null) txtDuration.setText("");
            if (txtReps     != null) txtReps.setText("");
            if (txtWeight   != null) txtWeight.setText("");
            if (lblCalcResult != null) lblCalcResult.setText("≈ — kcal");

            // Refresh the list so any newly-saved custom exercise appears immediately.
            loadEntries();
            applyFilter();

        } catch (NumberFormatException ex) {
            setStatus("Please enter numeric values.", UITheme.DANGER);
        }
    }

    private void setStatus(String text, Color color) {
        statusLabel.setText(text);
        statusLabel.setForeground(color);
    }

    // ════════════════════════════════════════════════════════════════════
    //  Custom list cell renderer — name + tag chips per row
    // ════════════════════════════════════════════════════════════════════
    private static class EntryRenderer extends JPanel implements ListCellRenderer<Entry> {
        private final JLabel name = new JLabel();
        private final JPanel chips = new JPanel(new FlowLayout(FlowLayout.LEFT, UITheme.SPACE_XS, 0));

        EntryRenderer() {
            setLayout(new BorderLayout());
            setBorder(UITheme.padding(UITheme.SPACE_SM, UITheme.SPACE_MD));
            chips.setOpaque(false);
            name.setFont(UITheme.FONT_BODY_BOLD);

            JPanel text = new JPanel();
            text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
            text.setOpaque(false);
            name.setAlignmentX(Component.LEFT_ALIGNMENT);
            chips.setAlignmentX(Component.LEFT_ALIGNMENT);
            text.add(name);
            text.add(chips);
            add(text, BorderLayout.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Entry> list, Entry e,
                                                      int index, boolean selected, boolean focus) {
            name.setText(e.name);
            chips.removeAll();
            chips.add(TagChip.forType(e.type));
            TagChip m = TagChip.muscle(e.muscleGroup);
            if (m != null) chips.add(m);
            chips.add(e.preset ? TagChip.preset() : TagChip.custom());

            if (selected) {
                setBackground(new Color(UITheme.PRIMARY.getRed(), UITheme.PRIMARY.getGreen(), UITheme.PRIMARY.getBlue(), 28));
                setOpaque(true);
                name.setForeground(UITheme.TEXT_PRIMARY);
            } else {
                setBackground(UITheme.SURFACE);
                setOpaque(true);
                name.setForeground(UITheme.TEXT_PRIMARY);
            }
            return this;
        }
    }
}
