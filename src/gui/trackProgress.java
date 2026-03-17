package gui;

import javax.swing.JFrame;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import impl.ExerciseDB;
import impl.ExerciseLogDB;
import impl.MealDB;
import impl.MealLogDB;
import impl.WaistDB;
import impl.WeightDB;
import models.DailyExerciseLog;
import models.DailyMealLog;
import models.Exercise;
import models.Meal;
import models.Waist;
import models.Weight;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.Color;
import java.util.List;

public class trackProgress {

<<<<<<< NaujasE
    private JFrame trackFrame;
    private JTextField txtmorningW;
    private JTextField txtEveningW;
    private JTextField txtMorningWa;
    private JTextField txtEveningWa;
    private JTable table, table2;
    private JTextField txtmealName;
    private JTextField txtmealcalorie;
    private JTextField txtintake;
    private JTextField mealsID;
    private JTextField exerciseID;
    private JButton btnDeleteE;
    private JTextField txtuserid;
    private int get;

    public trackProgress(int gets) {
        get = gets;
        initialize();
        Show_Meals_In_JTable();
        Show_Exercise_In_JTable();
    }

    private void initialize() {
        trackFrame = new JFrame();
        trackFrame.setTitle("Daily Record");
        trackFrame.setBounds(100, 100, 664, 651);
        trackFrame.setLocation(380, 10);
        trackFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        trackFrame.getContentPane().setLayout(null);

        JButton btnBack = new JButton("BACK");
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                trackFrame.dispose();
            }
        });
        btnBack.setBounds(10, 11, 89, 23);
        trackFrame.getContentPane().add(btnBack);

        JLabel lblTrackYourDaily = new JLabel("TRACK YOUR DAILY PROGRESS");
        lblTrackYourDaily.setFont(new Font("Verdana", Font.BOLD, 22));
        lblTrackYourDaily.setBounds(130, 11, 392, 47);
        trackFrame.getContentPane().add(lblTrackYourDaily);

        JLabel lblNewLabel = new JLabel("Your Weight(in KGs):");
        lblNewLabel.setFont(new Font("Verdana", Font.BOLD, 15));
        lblNewLabel.setBounds(30, 125, 177, 32);
        trackFrame.getContentPane().add(lblNewLabel);

        JLabel lblMorning = new JLabel("Morning:");
        lblMorning.setFont(new Font("Verdana", Font.PLAIN, 16));
        lblMorning.setBounds(40, 156, 81, 23);
        trackFrame.getContentPane().add(lblMorning);

        txtmorningW = new JTextField();
        txtmorningW.setBounds(110, 159, 122, 23);
        trackFrame.getContentPane().add(txtmorningW);
        txtmorningW.setColumns(10);

        JLabel lblEvening = new JLabel("Evening:");
        lblEvening.setFont(new Font("Verdana", Font.PLAIN, 16));
        lblEvening.setBounds(40, 190, 81, 23);
        trackFrame.getContentPane().add(lblEvening);

        txtEveningW = new JTextField();
        txtEveningW.setColumns(10);
        txtEveningW.setBounds(110, 190, 122, 23);
        trackFrame.getContentPane().add(txtEveningW);

        JButton btnAdd = new JButton("ADD");
        btnAdd.setBounds(234, 159, 65, 23);
        trackFrame.getContentPane().add(btnAdd);
        btnAdd.addActionListener(new InsertWeightListener());

        JButton btnEvening = new JButton("ADD");
        btnEvening.setBounds(234, 190, 65, 23);
        trackFrame.getContentPane().add(btnEvening);
        btnEvening.addActionListener(new UpdateWeightListener());

        JLabel lblYourWaist = new JLabel("Your Waist(in centimeters):");
        lblYourWaist.setFont(new Font("Verdana", Font.BOLD, 15));
        lblYourWaist.setBounds(309, 125, 196, 32);
        trackFrame.getContentPane().add(lblYourWaist);

        JLabel label = new JLabel("Morning:");
        label.setFont(new Font("Verdana", Font.PLAIN, 16));
        label.setBounds(319, 156, 81, 23);
        trackFrame.getContentPane().add(label);

        txtMorningWa = new JTextField();
        txtMorningWa.setColumns(10);
        txtMorningWa.setBounds(393, 159, 122, 23);
        trackFrame.getContentPane().add(txtMorningWa);

        JLabel label_1 = new JLabel("Evening:");
        label_1.setFont(new Font("Verdana", Font.PLAIN, 16));
        label_1.setBounds(319, 190, 81, 23);
        trackFrame.getContentPane().add(label_1);

        txtEveningWa = new JTextField();
        txtEveningWa.setColumns(10);
        txtEveningWa.setBounds(393, 193, 122, 23);
        trackFrame.getContentPane().add(txtEveningWa);

        JButton btnWaistM = new JButton("ADD");
        btnWaistM.setBounds(520, 159, 65, 23);
        trackFrame.getContentPane().add(btnWaistM);
        btnWaistM.addActionListener(new InsertWaistListener());

        JButton btnWaistE = new JButton("ADD");
        btnWaistE.setBounds(520, 193, 65, 23);
        trackFrame.getContentPane().add(btnWaistE);
        btnWaistE.addActionListener(new UpdateWaistListener());

        JLabel lblAddYourMeals = new JLabel("Add your Meals for Today!!");
        lblAddYourMeals.setFont(new Font("Tahoma", Font.PLAIN, 17));
        lblAddYourMeals.setBounds(10, 234, 212, 23);
        trackFrame.getContentPane().add(lblAddYourMeals);

        JScrollPane mealTable = new JScrollPane();
        mealTable.setBounds(10, 268, 310, 186);
        trackFrame.getContentPane().add(mealTable);

        table = new JTable();
        mealTable.setViewportView(table);
=======
	private JFrame trackFrame;
	private JTextField txtmorningW;
	private JTextField txtEveningW;
	private JTextField txtMorningWa;
	private JTextField txtEveningWa;
	private JTable table,table2;
	private JTextField txtmealName;
	private JTextField txtmealcalorie;
	private JTextField txtintake;
	private JTextField mealsID;
	private JTextField exerciseID;
	private JButton btnDeleteE;
	private JTextField txtuserid;
	private JComboBox<String> dateComboBox;
	private JLabel lblSelectedDate;
	private String currentViewDate;
	private int get;

	public trackProgress(int gets) {
		get=gets;
		currentViewDate = String.valueOf(LocalDate.now());
		initialize();
		Show_Meals_In_JTable();
		Show_Exercise_In_JTable();		
	}

	private void initialize() {
		trackFrame = new JFrame();
		trackFrame.setTitle("Daily Record");
		trackFrame.setBounds(100, 100, 664, 651);
		trackFrame.setLocation(380, 10);
		trackFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		trackFrame.getContentPane().setLayout(null);
		
		JButton btnBack = new JButton("BACK");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//new MealIUD(get, null, get, null, null);
				trackFrame.dispose();
			}
		});
		btnBack.setBounds(10, 11, 89, 23);
		trackFrame.getContentPane().add(btnBack);
		
		JLabel lblTrackYourDaily = new JLabel("TRACK YOUR DAILY PROGRESS");
		lblTrackYourDaily.setFont(new Font("Verdana", Font.BOLD, 22));
		lblTrackYourDaily.setBounds(130, 11, 392, 47);
		trackFrame.getContentPane().add(lblTrackYourDaily);
		
		JLabel lblNewLabel = new JLabel("Your Weight(in KGs):");
		lblNewLabel.setFont(new Font("Verdana", Font.BOLD, 15));
		lblNewLabel.setBounds(30, 125, 177, 32);
		trackFrame.getContentPane().add(lblNewLabel);
		
		JLabel lblMorning = new JLabel("Morning:");
		lblMorning.setFont(new Font("Verdana", Font.PLAIN, 16));
		lblMorning.setBounds(40, 156, 81, 23);
		trackFrame.getContentPane().add(lblMorning);
		
		txtmorningW = new JTextField();
		txtmorningW.setBounds(110, 159, 122, 23);
		trackFrame.getContentPane().add(txtmorningW);
		txtmorningW.setColumns(10);
		
		JLabel lblEvening = new JLabel("Evening:");
		lblEvening.setFont(new Font("Verdana", Font.PLAIN, 16));
		lblEvening.setBounds(40, 190, 81, 23);
		trackFrame.getContentPane().add(lblEvening);
		
		txtEveningW = new JTextField();
		txtEveningW.setColumns(10);
		txtEveningW.setBounds(110, 190, 122, 23);
		trackFrame.getContentPane().add(txtEveningW);
		
		JButton btnAdd = new JButton("ADD");
		btnAdd.setBounds(234, 159, 65, 23);
		trackFrame.getContentPane().add(btnAdd);
		btnAdd.addActionListener(new InsertWeightListener());
		
		JButton btnEvening = new JButton("ADD");
		btnEvening.setBounds(234, 190, 65, 23);
		trackFrame.getContentPane().add(btnEvening);
		
		btnEvening.addActionListener(new UpdateWeightListener());
		
		JLabel lblYourWaist = new JLabel("Your Waist(in centimeters):");
		lblYourWaist.setFont(new Font("Verdana", Font.BOLD, 15));
		lblYourWaist.setBounds(309, 125, 196, 32);
		trackFrame.getContentPane().add(lblYourWaist);
		
		JLabel label = new JLabel("Morning:");
		label.setFont(new Font("Verdana", Font.PLAIN, 16));
		label.setBounds(319, 156, 81, 23);
		trackFrame.getContentPane().add(label);
		
		txtMorningWa = new JTextField();
		txtMorningWa.setColumns(10);
		txtMorningWa.setBounds(393, 159, 122, 23);
		trackFrame.getContentPane().add(txtMorningWa);
		
		JLabel label_1 = new JLabel("Evening:");
		label_1.setFont(new Font("Verdana", Font.PLAIN, 16));
		label_1.setBounds(319, 190, 81, 23);
		trackFrame.getContentPane().add(label_1);
		
		txtEveningWa = new JTextField();
		txtEveningWa.setColumns(10);
		txtEveningWa.setBounds(393, 193, 122, 23);
		trackFrame.getContentPane().add(txtEveningWa);
		
		JButton btnWaistM = new JButton("ADD");
		btnWaistM.setBounds(520, 159, 65, 23);
		trackFrame.getContentPane().add(btnWaistM);
		btnWaistM.addActionListener(new InsertWaistListener());
		
		JButton btnWaistE = new JButton("ADD");
		btnWaistE.setBounds(520, 193, 65, 23);
		trackFrame.getContentPane().add(btnWaistE);
		btnWaistE.addActionListener(new UpdateWaistListener());
		
		JLabel lblAddYourMeals = new JLabel("Add your Meals for Today!!");
		lblAddYourMeals.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblAddYourMeals.setBounds(10, 234, 212, 23);
		trackFrame.getContentPane().add(lblAddYourMeals);
		
		JScrollPane mealTable = new JScrollPane();
		mealTable.setBounds(10, 268, 310, 186);
		trackFrame.getContentPane().add(mealTable);
		
		table = new JTable();
		mealTable.setViewportView(table);
		
>>>>>>> main
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{},
            new String[]{"Meal ID", "Meals Name", "Calorie Per Gram"}
        ));
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                UsersMouseClicked(evt);
            }
        });

        JLabel lblAddYourExercise = new JLabel("Today's Exercises");
        lblAddYourExercise.setFont(new Font("Tahoma", Font.PLAIN, 17));
        lblAddYourExercise.setBounds(329, 234, 233, 23);
        trackFrame.getContentPane().add(lblAddYourExercise);

        JScrollPane exercise = new JScrollPane();
        exercise.setBounds(330, 268, 308, 186);
        trackFrame.getContentPane().add(exercise);

        table2 = new JTable();
        exercise.setViewportView(table2);

        // PAKEITIMAS: pridėtas "Muscle Group" stulpelis
        table2.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{},
            new String[]{"Log ID", "Exercise Name", "Muscle Group", "Info", "Calories"}
        ));
        table2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                UsersMouseClicked1(evt);
            }
        });

        JLabel lblKnowYourProgress = new JLabel("Know Your Progress!!");
        lblKnowYourProgress.setForeground(Color.RED);
        lblKnowYourProgress.setFont(new Font("Tahoma", Font.PLAIN, 17));
        lblKnowYourProgress.setBounds(460, 55, 178, 23);
        trackFrame.getContentPane().add(lblKnowYourProgress);

        JButton btnAnalyse = new JButton("ANALYSE");
        btnAnalyse.setFont(new Font("Verdana", Font.PLAIN, 15));
        btnAnalyse.setBounds(511, 83, 110, 32);
        trackFrame.getContentPane().add(btnAnalyse);

        txtmealName = new JTextField();
        txtmealName.setBounds(147, 465, 164, 23);
        trackFrame.getContentPane().add(txtmealName);
        txtmealName.setColumns(10);

        txtmealcalorie = new JTextField();
        txtmealcalorie.setColumns(10);
        txtmealcalorie.setBounds(147, 499, 164, 23);
        trackFrame.getContentPane().add(txtmealcalorie);

        txtintake = new JTextField();
        txtintake.setColumns(10);
        txtintake.setBounds(147, 533, 164, 23);
        trackFrame.getContentPane().add(txtintake);

        mealsID = new JTextField();
        mealsID.setBounds(220, 238, 45, 20);
        trackFrame.getContentPane().add(mealsID);
        mealsID.setColumns(10);

        exerciseID = new JTextField();
        exerciseID.setBounds(275, 238, 45, 20);
        trackFrame.getContentPane().add(exerciseID);
        exerciseID.setColumns(10);

        JLabel lblMealName = new JLabel("Meal Name:");
        lblMealName.setFont(new Font("Verdana", Font.PLAIN, 15));
        lblMealName.setBounds(30, 469, 107, 14);
        trackFrame.getContentPane().add(lblMealName);

        JLabel lblCaloriegram = new JLabel("Calorie/Gram:");
        lblCaloriegram.setFont(new Font("Verdana", Font.PLAIN, 15));
        lblCaloriegram.setBounds(30, 503, 107, 14);
        trackFrame.getContentPane().add(lblCaloriegram);

        JLabel lblAmount = new JLabel("Amount(gm):");
        lblAmount.setFont(new Font("Verdana", Font.PLAIN, 15));
        lblAmount.setBounds(30, 537, 107, 19);
        trackFrame.getContentPane().add(lblAmount);

        JButton btnInsert = new JButton("Insert");
        btnInsert.setBounds(20, 580, 89, 23);
        trackFrame.getContentPane().add(btnInsert);
        btnInsert.addActionListener(new InsertDailymealListener());

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBounds(114, 580, 93, 23);
        trackFrame.getContentPane().add(btnUpdate);
        btnUpdate.addActionListener(new UpdateMealListener());

        JButton btnDelete = new JButton("Delete");
        btnDelete.setBounds(210, 580, 89, 23);
        trackFrame.getContentPane().add(btnDelete);
        btnDelete.addActionListener(new DeleteMealListener());

        btnDeleteE = new JButton("Delete");
        btnDeleteE.setBounds(330, 462, 100, 28);
        trackFrame.getContentPane().add(btnDeleteE);
        btnDeleteE.addActionListener(new DeleteExerciseListener());

        ButtonGroup bg = new ButtonGroup();

        txtuserid = new JTextField();
        txtuserid.setBounds(13, 45, 86, 20);
        trackFrame.getContentPane().add(txtuserid);
        txtuserid.setColumns(10);
        txtuserid.setText(String.valueOf(get));

        btnAnalyse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int asd = Integer.parseInt(txtuserid.getText());
                new PeriodSelect(asd);
                trackFrame.setVisible(false);
            }
        });

        trackFrame.setVisible(true);
    }

    public void Show_Meals_In_JTable() {
        MealDB udb = new MealDB();
        List<Meal> meals = udb.getAll();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        Object[] row = new Object[4];
        for (int i = 0; i < meals.size(); i++) {
            row[0] = meals.get(i).getId();
            row[1] = meals.get(i).getName();
            row[2] = meals.get(i).getCaloriesPerGram();
            model.addRow(row);
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
        ArrayList<Object[]> logs = logDB.getTodayLogs(get);
        DefaultTableModel model = (DefaultTableModel) table2.getModel();
        model.setRowCount(0);

        for (Object[] lr : logs) {
            // lr: [0]=logId, [1]=name, [2]=type, [3]=totalKcal,
            //     [4]=reps, [5]=weightKg, [6]=durationMin, [7]=muscleGroup
            String type = (String) lr[2];
            String muscleGroup = lr[7] != null ? (String) lr[7] : "General";
            String info;
            String calories;

            if ("Strength".equals(type)) {
                int reps = (int) lr[4];
                int kg = (int) ((double) lr[5]);
                info = reps + " reps @ " + kg + " kg";
                calories = "-";
            } else {
                int mins = (int) ((double) lr[6]);
                info = mins + " min";
                calories = String.format("%.0f kcal", (double) lr[3]);
            }

            // PAKEITIMAS: pridėtas muscleGroup stulpelis
            model.addRow(new Object[]{lr[0], lr[1], muscleGroup, info, calories});
        }
    }

    private int selectedExerciseId = -1;

    private void UsersMouseClicked1(java.awt.event.MouseEvent evt) {
        int i = table2.getSelectedRow();
        if (i < 0) return;
        TableModel model = table2.getModel();
        selectedExerciseId = Integer.parseInt(model.getValueAt(i, 0).toString());
        exerciseID.setText(String.valueOf(selectedExerciseId));
    }

    class UpdateMealListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (mealsID.getText().isEmpty() || txtmealName.getText().isEmpty() || txtmealcalorie.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "To Update please select the meal above!!");
                } else {
                    MealDB udb = new MealDB();
                    int meals_id = Integer.parseInt(mealsID.getText());
                    Meal m = udb.getById(meals_id);
                    m.setMealName(txtmealName.getText());
                    m.setCaloriesPerGram(Integer.parseInt(txtmealcalorie.getText()));
                    int rowUpdate = udb.updateMeal(m);
                    if (rowUpdate > 0) {
                        JOptionPane.showMessageDialog(null, "Meal Updated");
                        DefaultTableModel model = (DefaultTableModel) table.getModel();
                        model.setRowCount(0);
                        Show_Meals_In_JTable();
                        mealsID.setText(""); txtmealName.setText(""); txtmealcalorie.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to update meal");
                    }
                }
            } catch (NumberFormatException eee) {
                JOptionPane.showConfirmDialog(null, "Please enter numeric value", "Naughty", JOptionPane.CANCEL_OPTION);
            }
        }
    }

    class DeleteMealListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (mealsID.getText().isEmpty() || txtmealName.getText().isEmpty() || txtmealcalorie.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "To Delete please select the meal above!!");
            } else {
                MealDB udb = new MealDB();
                int meals_id = Integer.parseInt(mealsID.getText());
                Meal m = udb.getById(meals_id);
                int rowUpdate = udb.deleteMeal(m);
                if (rowUpdate > 0) {
                    JOptionPane.showMessageDialog(null, "Meal Deleted");
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    model.setRowCount(0);
                    Show_Meals_In_JTable();
                    mealsID.setText(""); txtmealName.setText(""); txtmealcalorie.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to delete Meal");
                }
            }
        }
    }

    class UpdateExerciseListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) { /* nenaudojama */ }
    }

    class DeleteExerciseListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedExerciseId < 0) {
                JOptionPane.showMessageDialog(null, "Please select exercise to delete!!");
            } else {
                ExerciseLogDB logDB = new ExerciseLogDB();
                int rowUpdate = logDB.deleteTodayLog(selectedExerciseId);
                if (rowUpdate > 0) {
                    JOptionPane.showMessageDialog(null, "Exercise removed from today's log");
                    Show_Exercise_In_JTable();
                    selectedExerciseId = -1;
                    exerciseID.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to delete");
                }
            }
        }
    }

    class InsertWeightListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (txtmorningW.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter weight!!");
                } else {
                    Weight w = new Weight();
                    w.setWeightM(Double.parseDouble(txtmorningW.getText()));
                    w.setUserId(Integer.parseInt(txtuserid.getText()));
                    WeightDB udb = new WeightDB();
                    int rowUpdate = udb.insertWeight(w);
                    if (rowUpdate > 0) {
                        JOptionPane.showMessageDialog(null, "Weight Added!");
                        txtmorningW.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to Add Weight!!");
                    }
                }
            } catch (NumberFormatException eee) {
                JOptionPane.showConfirmDialog(null, "Please enter numeric value", "Naughty", JOptionPane.CANCEL_OPTION);
            }
        }
    }

    class UpdateWeightListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (txtEveningW.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter evening weight!!");
                } else {
                    WeightDB udb = new WeightDB();
                    Weight w = new Weight();
                    double weight_ev = Double.parseDouble(txtEveningW.getText());
                    w.setWeightE(weight_ev);
                    ArrayList<Double> got = udb.getList();
                    double mWt = got.get(0);
                    w.setAverage((weight_ev + mWt) / 2);
                    w.setUserId(Integer.parseInt(txtuserid.getText()));
                    int rowUpdate = udb.updateWeight(w);
                    if (rowUpdate > 0) {
                        JOptionPane.showMessageDialog(null, "Weight Added");
                        txtEveningW.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed add Weight");
                    }
                }
            } catch (NumberFormatException ee) {
                JOptionPane.showConfirmDialog(null, "Please enter numeric value", "Naughty", JOptionPane.CANCEL_OPTION);
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
                    ww.setWaistM(Double.parseDouble(txtMorningWa.getText()));
                    ww.setUserId(Integer.parseInt(txtuserid.getText()));
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
                    ww.setAverage((waist_ev + mWt) / 2);
                    ww.setUserId(Integer.parseInt(txtuserid.getText()));
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
                if (mealsID.getText().isEmpty() || txtmealcalorie.getText().isEmpty()
                        || txtintake.getText().isEmpty() || txtmealName.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "The fields can not be empty");
                } else {
                    DailyMealLog dml = new DailyMealLog();
                    double calorieIntake = Double.parseDouble(txtmealcalorie.getText());
                    double mealAmount = Double.parseDouble(txtintake.getText());
                    dml.setTotalCalorieIntake(calorieIntake * mealAmount);
                    dml.setUserId(Integer.parseInt(txtuserid.getText()));
                    dml.setMealId(Integer.parseInt(mealsID.getText()));
                    MealLogDB udb = new MealLogDB();
                    int rowUpdate = udb.insertDailyLog(dml);
                    if (rowUpdate > 0) {
                        JOptionPane.showMessageDialog(null, "Your Meal Log Added!");
                        txtmealcalorie.setText(""); txtintake.setText(""); txtmealName.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to Add Log!!");
                    }
                }
<<<<<<< NaujasE
            } catch (NumberFormatException ee) {
                JOptionPane.showConfirmDialog(null, "Please enter numeric value", "Durnas ar kas? praso skaiciaus", JOptionPane.CANCEL_OPTION);
            }
        }
    }
=======
            });
				
		JLabel lblKnowYourProgress = new JLabel("Know Your Progress!!");
		lblKnowYourProgress.setForeground(Color.RED);
		lblKnowYourProgress.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblKnowYourProgress.setBounds(460, 55, 178, 23);
		trackFrame.getContentPane().add(lblKnowYourProgress);
		
		JButton btnAnalyse = new JButton("ANALYSE");

		btnAnalyse.setFont(new Font("Verdana", Font.PLAIN, 15));
		btnAnalyse.setBounds(511, 83, 110, 32);
		trackFrame.getContentPane().add(btnAnalyse);
		
		
		txtmealName = new JTextField();
		txtmealName.setBounds(147, 465, 164, 23);
		trackFrame.getContentPane().add(txtmealName);
		txtmealName.setColumns(10);
		
		txtmealcalorie = new JTextField();
		txtmealcalorie.setColumns(10);
		txtmealcalorie.setBounds(147, 499, 164, 23);
		trackFrame.getContentPane().add(txtmealcalorie);
		
		txtintake = new JTextField();
		txtintake.setColumns(10);
		txtintake.setBounds(147, 533, 164, 23);
		trackFrame.getContentPane().add(txtintake);
		
		mealsID = new JTextField();
		mealsID.setBounds(220, 238, 45, 20);
		trackFrame.getContentPane().add(mealsID);
		mealsID.setColumns(10);
		
		exerciseID = new JTextField();
		exerciseID.setBounds(275, 238, 45, 20);
		trackFrame.getContentPane().add(exerciseID);
		exerciseID.setColumns(10);
		
		JLabel lblMealName = new JLabel("Meal Name:");
		lblMealName.setFont(new Font("Verdana", Font.PLAIN, 15));
		lblMealName.setBounds(30, 469, 107, 14);
		trackFrame.getContentPane().add(lblMealName);
		
		JLabel lblCaloriegram = new JLabel("Calorie/Gram:");
		lblCaloriegram.setFont(new Font("Verdana", Font.PLAIN, 15));
		lblCaloriegram.setBounds(30, 503, 107, 14);
		trackFrame.getContentPane().add(lblCaloriegram);
		
		JLabel lblAmount = new JLabel("Amount(gm):");
		lblAmount.setFont(new Font("Verdana", Font.PLAIN, 15));
		lblAmount.setBounds(30, 537, 107, 19);
		trackFrame.getContentPane().add(lblAmount);
		
		JButton btnInsert = new JButton("Insert");
		btnInsert.setBounds(20, 580, 89, 23);
		trackFrame.getContentPane().add(btnInsert);
		btnInsert.addActionListener(new InsertDailymealListener());
		
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBounds(114, 580, 93, 23);
		trackFrame.getContentPane().add(btnUpdate);
		btnUpdate.addActionListener(new UpdateMealListener());
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.setBounds(210, 580, 89, 23);
		trackFrame.getContentPane().add(btnDelete);
		btnDelete.addActionListener(new DeleteMealListener());
		
		btnDeleteE = new JButton("Delete");
		btnDeleteE.setBounds(330, 462, 100, 28);
		trackFrame.getContentPane().add(btnDeleteE);
		btnDeleteE.addActionListener(new DeleteExerciseListener());

		ButtonGroup bg=new ButtonGroup();
		
		txtuserid = new JTextField();
		txtuserid.setBounds(13, 45, 86, 20);
		trackFrame.getContentPane().add(txtuserid);
		txtuserid.setColumns(10);
		String iss=String.valueOf(get);
		txtuserid.setText(iss);
		
		btnAnalyse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int asd=Integer.parseInt(txtuserid.getText());
				new PeriodSelect(asd);
				//System.out.println(textField.getText());
				trackFrame.setVisible(false);
			}
		});

		// ── ISTORIJOS DATOS PASIRINKIMAS ──────────────────────────────────────
		JLabel lblHistory = new JLabel("Istorija:");
		lblHistory.setFont(new Font("Verdana", Font.BOLD, 13));
		lblHistory.setBounds(330, 462, 80, 20);
		trackFrame.getContentPane().add(lblHistory);

		dateComboBox = new JComboBox<>();
		dateComboBox.setBounds(330, 488, 160, 25);
		trackFrame.getContentPane().add(dateComboBox);

		JButton btnLoadHistory = new JButton("Rodyti");
		btnLoadHistory.setBounds(498, 488, 80, 25);
		trackFrame.getContentPane().add(btnLoadHistory);

		JButton btnTodayBtn = new JButton("Šiandien");
		btnTodayBtn.setBounds(330, 520, 110, 25);
		trackFrame.getContentPane().add(btnTodayBtn);

		lblSelectedDate = new JLabel("Rodoma: " + currentViewDate);
		lblSelectedDate.setFont(new Font("Verdana", Font.ITALIC, 11));
		lblSelectedDate.setForeground(Color.BLUE);
		lblSelectedDate.setBounds(330, 550, 200, 20);
		trackFrame.getContentPane().add(lblSelectedDate);

		// Užpildyti datos sąrašą
		refreshDateComboBox();

		btnLoadHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selected = (String) dateComboBox.getSelectedItem();
				if (selected != null && !selected.isEmpty()) {
					currentViewDate = selected;
					lblSelectedDate.setText("Rodoma: " + currentViewDate);
					Show_Exercise_In_JTable();
				}
			}
		});

		btnTodayBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentViewDate = String.valueOf(LocalDate.now());
				lblSelectedDate.setText("Rodoma: " + currentViewDate);
				refreshDateComboBox();
				Show_Exercise_In_JTable();
			}
		});
		// ─────────────────────────────────────────────────────────────────────
		
		trackFrame.setVisible(true);
	}
	   public void Show_Meals_In_JTable()
	   {
		   
		   MealDB udb=new MealDB();
	       List<Meal> meals = udb.getAll();
	    	   
	       DefaultTableModel model = (DefaultTableModel)table.getModel();
	       Object[] row = new Object[4];
	       for(int i = 0; i < meals.size(); i++)
	       {
	           row[0]=meals.get(i).getId();
	          
	           row[1] = meals.get(i).getName();
	           row[2] = meals.get(i).getCaloriesPerGram();
	           
	           
	           model.addRow(row);
	       }
	    }
	
	   private void UsersMouseClicked(java.awt.event.MouseEvent evt) {                                                  
	    
	        int i = table.getSelectedRow();

	        TableModel model = table.getModel();
	        
	        
	        mealsID.setText(model.getValueAt(i,0).toString());

	        txtmealName.setText(model.getValueAt(i,1).toString());

	        txtmealcalorie.setText(model.getValueAt(i,2).toString());

	        
	    } 
	   
	   public void Show_Exercise_In_JTable()
	   {
		   ExerciseLogDB logDB = new ExerciseLogDB();
		   java.util.ArrayList<Object[]> logs;
		   // Rodyti pasirinktą datą (arba šiandien, jei nenustatyta)
		   if (currentViewDate != null && !currentViewDate.isEmpty()) {
			   logs = logDB.getLogsByDate(get, currentViewDate);
		   } else {
			   logs = logDB.getTodayLogs(get);
		   }
	       DefaultTableModel model = (DefaultTableModel)table2.getModel();
	       model.setRowCount(0);
	       for (Object[] lr : logs) {
	           // lr: [0]=logId, [1]=name, [2]=type, [3]=totalKcal, [4]=reps, [5]=weightKg, [6]=durationMin
	           String type = (String) lr[2];
	           String info;
	           String calories;
	           if ("Strength".equals(type)) {
	               int reps = (int) lr[4];
	               int kg   = (int)((double) lr[5]);
	               info     = reps + " reps @ " + kg + " kg";
	               calories = "-";
	           } else {
	               int mins = (int)((double) lr[6]);
	               info     = mins + " min";
	               calories = String.format("%.0f kcal", (double) lr[3]);
	           }
	           model.addRow(new Object[]{lr[0], lr[1], info, calories});
	       }
	    }

	   /** Atnaujina datos pasirinkimo sąrašą iš duomenų bazės */
	   private void refreshDateComboBox() {
		   if (dateComboBox == null) return;
		   ExerciseLogDB logDB = new ExerciseLogDB();
		   java.util.ArrayList<String> dates = logDB.getAllLogDates(get);
		   DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
		   // Visada pridėti šiandieną viršuje
		   String today = String.valueOf(LocalDate.now());
		   if (!dates.contains(today)) dates.add(0, today);
		   for (String d : dates) model.addElement(d);
		   dateComboBox.setModel(model);
		   // Pasirinkti esamą datą
		   dateComboBox.setSelectedItem(currentViewDate);
	   }
	
	   private int selectedExerciseId = -1;

	   private void UsersMouseClicked1(java.awt.event.MouseEvent evt) {
	        int i = table2.getSelectedRow();
	        if (i < 0) return;
	        TableModel model = table2.getModel();
	        selectedExerciseId = Integer.parseInt(model.getValueAt(i, 0).toString());
	        exerciseID.setText(String.valueOf(selectedExerciseId));
	    } 
	   
	   class UpdateMealListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try{
			if(mealsID.getText().isEmpty() || 
			txtmealName.getText().isEmpty() || txtmealcalorie.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "To Update please select the meal above!!");
			}
			else{
			MealDB udb=new MealDB();
			int meals_id=Integer.parseInt(mealsID.getText());
			Meal m=udb.getById(meals_id);
			//System.out.println(meals_id);
			//System.out.println(txtmealName.getText());
			m.setMealName(txtmealName.getText());
			
			int meals_calorie=Integer.parseInt(txtmealcalorie.getText());
			m.setCaloriesPerGram(meals_calorie);
			
			int rowUpdate=udb.updateMeal(m);
			if(rowUpdate>0){
				JOptionPane.showMessageDialog(null, "Meal Updated");
				DefaultTableModel model = (DefaultTableModel)table.getModel();
		         model.setRowCount(0);
		         Show_Meals_In_JTable();
		         mealsID.setText("");
		         txtmealName.setText("");
		         txtmealcalorie.setText("");
		         
			}
			else{
				JOptionPane.showMessageDialog(null, "Failed to update meal");
			}	
			}
			}
			catch(NumberFormatException eee){
				JOptionPane.showConfirmDialog(null, 
				"Please enter numeric value", "Naughty", JOptionPane.CANCEL_OPTION);
			}
		}
	 }
	   
		class DeleteMealListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				if(mealsID.getText().isEmpty() || 
				txtmealName.getText().isEmpty() || txtmealcalorie.getText().isEmpty()){
				JOptionPane.showMessageDialog(null, "To Delete please select the meal above!!");
				}
				else{
				MealDB udb=new MealDB();
				int meals_id=Integer.parseInt(mealsID.getText());
				Meal m=udb.getById(meals_id); 
				int rowUpdate= udb.deleteMeal(m);
				if(rowUpdate>0){
					JOptionPane.showMessageDialog(null, "Meal Deleted");
					DefaultTableModel model = (DefaultTableModel)table.getModel();
			         model.setRowCount(0);
			         Show_Meals_In_JTable();
					mealsID.setText("");
					txtmealName.setText("");
					txtmealcalorie.setText("");
					
				}
				else{
					JOptionPane.showMessageDialog(null, "Failed to delete Meal");
				}
				}	
			}
		}
		
		   class UpdateExerciseListener implements ActionListener {
				@Override
				public void actionPerformed(ActionEvent e) { /* nenaudojama */ }
			   }
			   
		class DeleteExerciseListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(selectedExerciseId < 0){
				JOptionPane.showMessageDialog(null, "Please select exercise to delete!!");
			}
			else{
			ExerciseLogDB logDB = new ExerciseLogDB();
			int rowUpdate = logDB.deleteTodayLog(selectedExerciseId);
			if(rowUpdate > 0){
			JOptionPane.showMessageDialog(null, "Exercise removed from today's log");
			Show_Exercise_In_JTable();
			selectedExerciseId = -1;
			exerciseID.setText("");
			}
			else{
			JOptionPane.showMessageDialog(null, "Failed to delete");
			}
			}
			}		
				}
				
class InsertWeightListener implements ActionListener{

					@Override
					public void actionPerformed(ActionEvent e) {
						try{
						if(txtmorningW.getText().isEmpty()){
							JOptionPane.showMessageDialog(null, "Please enter weight!!");
						}
						else{
						Weight w = new Weight();
						double mrng_wt=Double.parseDouble(txtmorningW.getText());
						w.setWeightM(mrng_wt);
						int user_id=Integer.parseInt(txtuserid.getText());
						w.setUserId(user_id);
						
						WeightDB udb=new WeightDB();
						int rowUpdate= udb.insertWeight(w);
						
						if(rowUpdate>0){
						JOptionPane.showMessageDialog(null, " Weight Added!");	
						LocalDate today=LocalDate.now();
							
							txtmorningW.setText("");
							
							
						}
						else{
							JOptionPane.showMessageDialog(null, "Failed to Add Weight!!");
						}
						}
						}
						catch(NumberFormatException eee){
							JOptionPane.showConfirmDialog(null, 
						"Please enter numeric value", "Naughty", JOptionPane.CANCEL_OPTION);
						}
						
					}
					}
			
				   class UpdateWeightListener implements ActionListener{

						@Override
						public void actionPerformed(ActionEvent e) {
							try{
							if(txtEveningW.getText().isEmpty()){
								JOptionPane.showMessageDialog(null, "Please enter evening weight!!");
							}
							else{
							WeightDB udb=new WeightDB();
							
							Weight w=new Weight();
							
							double weight_ev=Double.parseDouble(txtEveningW.getText());
							w.setWeightE(weight_ev);
							ArrayList<Double> got =udb.getList();
							double mWt=got.get(0);
							double averageW=(weight_ev+mWt)/2;
							w.setAverage(averageW);
							//i have change here
							int user_id=Integer.parseInt(txtuserid.getText());
							w.setUserId(user_id);
							
							
							int rowUpdate=udb.updateWeight(w);
							if(rowUpdate>0){
								JOptionPane.showMessageDialog(null, "Weight Added");
						         
						         txtEveningW.setText("");
						         
							}
							else{
								JOptionPane.showMessageDialog(null, "Failed add Weight");
							}
							}
							}
							catch(NumberFormatException ee){
								JOptionPane.showConfirmDialog
								(null, "Please enter numeric value", "Naughty", JOptionPane.CANCEL_OPTION);
							}
							
							
						}
						   
					   }
					class InsertWaistListener implements ActionListener{

						@Override
						public void actionPerformed(ActionEvent e) {
							try{
							if(txtMorningWa.getText().isEmpty()){
								JOptionPane.showMessageDialog(null, "Please enter morning waist");
							}
							else{
							Waist ww = new Waist();
							double mrng_wst=Double.parseDouble(txtMorningWa.getText());
							ww.setWaistM(mrng_wst);
							int user_id=Integer.parseInt(txtuserid.getText());
							ww.setUserId(user_id);
							
							
							
							WaistDB udb=new WaistDB();
							int rowUpdate= udb.insertWaist(ww);
							
							if(rowUpdate>0){
							JOptionPane.showMessageDialog(null, "Waist Added!");	
							
								txtMorningWa.setText("");
								
							}
							else{
								JOptionPane.showMessageDialog(null, "Failed to Add Waist!!");
							}
							}
							}
							catch(NumberFormatException ee){
								JOptionPane.showConfirmDialog(null,
							"Please enter numeric value", "Naughty", JOptionPane.CANCEL_OPTION);
							}
							
						}
						}
				
						class UpdateWaistListener implements ActionListener{

						@Override
						public void actionPerformed(ActionEvent e) {
							try{
							if(txtEveningWa.getText().isEmpty()){
								JOptionPane.showMessageDialog(null, 
								"Please enter evening waist");
							}
							else{
							WaistDB udb=new WaistDB();
								
								Waist ww=new Waist();
								double waist_ev=Double.parseDouble(txtEveningWa.getText());
								ww.setWaistE(waist_ev);
								
								ArrayList<Double> gots =udb.getLists();
								double mWt=gots.get(0);
								double averageW=(waist_ev+mWt)/2;
								ww.setAverage(averageW);
								int user_id=Integer.parseInt(txtuserid.getText());
								ww.setUserId(user_id);
								
								int rowUpdate=udb.updateWaist(ww);
								if(rowUpdate>0){
									JOptionPane.showMessageDialog(null, "Waist Added");
							         
							         txtEveningWa.setText("");
							         
								}
								else{
									JOptionPane.showMessageDialog(null, "Failed add Waist");
								}
							}
							}
							catch(NumberFormatException ee){
								JOptionPane.showConfirmDialog(null,
						    "Please enter numeric value", "Naughty", JOptionPane.CANCEL_OPTION);
							}
								
								
								
							}
							   
						   }
						//code for daily dairy of meal			   
						class InsertDailymealListener implements ActionListener{

							@Override
							public void actionPerformed(ActionEvent e) {
								try{
								if(mealsID.getText().isEmpty() || txtmealcalorie.getText().isEmpty() 
								|| txtintake.getText().isEmpty()
										|| txtmealName.getText().isEmpty()){
									JOptionPane.showMessageDialog(null, "The fields can not be empty");
								}
								else{
								DailyMealLog dml = new DailyMealLog();
								double calorieIntake=Double.parseDouble(txtmealcalorie.getText());
								double mealAmount=Double.parseDouble(txtintake.getText());
								double totalCalorie=calorieIntake*mealAmount;
								
								dml.setTotalCalorieIntake(totalCalorie);
								int user_id=Integer.parseInt(txtuserid.getText());
								dml.setUserId(user_id);
								int meal_id=Integer.parseInt(mealsID.getText());
								dml.setMealId(meal_id);
								
								MealLogDB udb=new MealLogDB();
								int rowUpdate= udb.insertDailyLog(dml);
								//JOptionPane.showMessageDialog(null, rowUpdate);
								
								if(rowUpdate>0){
								JOptionPane.showMessageDialog(null, "Your Meal Log Added!");	
								
									txtmealcalorie.setText("");
									txtintake.setText("");
									txtmealName.setText("");
									
								}
								else{
									JOptionPane.showMessageDialog(null, "Failed to Add Log!!");
								}
								}
								}
								catch(NumberFormatException ee){
									JOptionPane.showConfirmDialog(null, "Please enter numeric value",
								"Durnas ar kas? praso skaiciaus", JOptionPane.CANCEL_OPTION);
								}
								
								
							}
							}
>>>>>>> main
}