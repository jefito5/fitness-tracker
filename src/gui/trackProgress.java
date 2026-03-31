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
import java.sql.Date;
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
import impl.UserDB;
import impl.WaistDB;
import impl.WeightDB;
import models.DailyExerciseLog;
import models.DailyMealLog;
import models.Exercise;
import models.Meal;
import models.User;
import models.Waist;
import models.Weight;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.Color;
import java.time.LocalDate;
import java.util.List;


public class trackProgress {

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
		Show_MealLog_In_JTable();
	}

	private void initialize() {
		trackFrame = new JFrame();
		trackFrame.setTitle("Daily Record");
		trackFrame.setBounds(100, 100, 800, 720);
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
		
		// ── TODO: not yet implemented ──
		// JLabel lblYourWaist = new JLabel("Your Waist(in centimeters):");
		// lblYourWaist.setFont(new Font("Verdana", Font.BOLD, 15));
		// lblYourWaist.setBounds(309, 125, 196, 32);
		// trackFrame.getContentPane().add(lblYourWaist);
		// 
		// JLabel label = new JLabel("Morning:");
		// label.setFont(new Font("Verdana", Font.PLAIN, 16));
		// label.setBounds(319, 156, 81, 23);
		// trackFrame.getContentPane().add(label);
		// 
		// txtMorningWa = new JTextField();
		// txtMorningWa.setColumns(10);
		// txtMorningWa.setBounds(393, 159, 122, 23);
		// trackFrame.getContentPane().add(txtMorningWa);
		// 
		// JLabel label_1 = new JLabel("Evening:");
		// label_1.setFont(new Font("Verdana", Font.PLAIN, 16));
		// label_1.setBounds(319, 190, 81, 23);
		// trackFrame.getContentPane().add(label_1);
		// 
		// txtEveningWa = new JTextField();
		// txtEveningWa.setColumns(10);
		// txtEveningWa.setBounds(393, 193, 122, 23);
		// trackFrame.getContentPane().add(txtEveningWa);
		// 
		// JButton btnWaistM = new JButton("ADD");
		// btnWaistM.setBounds(520, 159, 65, 23);
		// trackFrame.getContentPane().add(btnWaistM);
		// btnWaistM.addActionListener(new InsertWaistListener());
		// 
		// JButton btnWaistE = new JButton("ADD");
		// btnWaistE.setBounds(520, 193, 65, 23);
		// trackFrame.getContentPane().add(btnWaistE);
		// btnWaistE.addActionListener(new UpdateWaistListener());
		
		JLabel lblAddYourMeals = new JLabel("Add your Meals for Today!!");
		lblAddYourMeals.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblAddYourMeals.setBounds(10, 234, 212, 23);
		trackFrame.getContentPane().add(lblAddYourMeals);
		
		JScrollPane mealTable = new JScrollPane();
		mealTable.setBounds(10, 268, 310, 186);
		trackFrame.getContentPane().add(mealTable);
		
		table = new JTable();
		mealTable.setViewportView(table);
		
        table.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                   "Meal ID", "Meals Name", "Kcal/100g", "Total kcal"
                }
            ));
            table.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    UsersMouseClicked(evt);
                }
            });
		
		
		
		JLabel lblAddYourExercise = new JLabel("Today's Exercises");
		lblAddYourExercise.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblAddYourExercise.setBounds(380, 234, 233, 23);
		trackFrame.getContentPane().add(lblAddYourExercise);
		
	JScrollPane exercise = new JScrollPane();
		exercise.setBounds(380, 268, 308, 186);
		trackFrame.getContentPane().add(exercise);
		
		table2=new JTable();
		exercise.setViewportView(table2);
		
        table2.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                   "Log ID", "Exercise Name", "Muscle Group", "Info", "Calories"
                }
            ));
            table2.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    UsersMouseClicked1(evt);
                }
            });
				
		// ── TODO: not yet implemented ──
		// JLabel lblKnowYourProgress = new JLabel("Know Your Progress!!");
		// lblKnowYourProgress.setForeground(Color.RED);
		// lblKnowYourProgress.setFont(new Font("Tahoma", Font.PLAIN, 17));
		// lblKnowYourProgress.setBounds(460, 55, 178, 23);
		// trackFrame.getContentPane().add(lblKnowYourProgress);
		// 
		// JButton btnAnalyse = new JButton("ANALYSE");
		// 
		// btnAnalyse.setFont(new Font("Verdana", Font.PLAIN, 15));
		// btnAnalyse.setBounds(511, 83, 110, 32);
		// trackFrame.getContentPane().add(btnAnalyse);
		
		
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
		mealsID.setColumns(10);
		// mealsID - used internally, not shown to user
		
		exerciseID = new JTextField();
		exerciseID.setColumns(10);
		// exerciseID - used internally, not shown to user
		
		JLabel lblMealName = new JLabel("Meal Name:");
		lblMealName.setFont(new Font("Verdana", Font.PLAIN, 15));
		lblMealName.setBounds(30, 469, 107, 14);
		trackFrame.getContentPane().add(lblMealName);
		
		JLabel lblCaloriegram = new JLabel("Calories/100g:");
		lblCaloriegram.setFont(new Font("Verdana", Font.PLAIN, 15));
		lblCaloriegram.setBounds(30, 503, 130, 20);
		trackFrame.getContentPane().add(lblCaloriegram);
		
		JLabel lblAmount = new JLabel("Amount (g):");
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
		btnDeleteE.setBounds(380, 462, 100, 28);
		trackFrame.getContentPane().add(btnDeleteE);
		btnDeleteE.addActionListener(new DeleteExerciseListener());

		ButtonGroup bg=new ButtonGroup();
		
		txtuserid = new JTextField();
		txtuserid.setColumns(10);
		txtuserid.setText(String.valueOf(get));
		// txtuserid - used internally, not shown to user
		
		// btnAnalyse.addActionListener(...); // TODO: not yet implemented

		// ── HISTORY DATE SELECTOR ───────────────────────────────────────────
		JLabel lblHistory = new JLabel("History:");
		lblHistory.setFont(new Font("Verdana", Font.BOLD, 13));
		lblHistory.setBounds(490, 462, 80, 20);
		trackFrame.getContentPane().add(lblHistory);

		dateComboBox = new JComboBox<>();
		dateComboBox.setBounds(380, 488, 160, 25);
		trackFrame.getContentPane().add(dateComboBox);

		JButton btnLoadHistory = new JButton("Show");
		btnLoadHistory.setBounds(548, 488, 80, 25);
		trackFrame.getContentPane().add(btnLoadHistory);

		JButton btnTodayBtn = new JButton("Today");
		btnTodayBtn.setBounds(380, 520, 110, 25);
		trackFrame.getContentPane().add(btnTodayBtn);

		lblSelectedDate = new JLabel("Viewing: " + currentViewDate);
		lblSelectedDate.setFont(new Font("Verdana", Font.ITALIC, 11));
		lblSelectedDate.setForeground(Color.BLUE);
		lblSelectedDate.setBounds(380, 550, 200, 20);
		trackFrame.getContentPane().add(lblSelectedDate);

		// Populate date list
		refreshDateComboBox();

		btnLoadHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selected = (String) dateComboBox.getSelectedItem();
				if (selected != null && !selected.isEmpty()) {
					currentViewDate = selected;
					lblSelectedDate.setText("Viewing: " + currentViewDate);
					Show_Exercise_In_JTable();
					Show_MealLog_In_JTable();
				}
			}
		});

		btnTodayBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentViewDate = String.valueOf(LocalDate.now());
				lblSelectedDate.setText("Viewing: " + currentViewDate);
				refreshDateComboBox();
				Show_Exercise_In_JTable();
				Show_MealLog_In_JTable();
			}
		});
		// ─────────────────────────────────────────────────────────────────────
		
		trackFrame.setVisible(true);
	}
	   public void Show_Meals_In_JTable()
	   {
		   // Rodyti tik tos dienos maisto log'us — ta pati logika kaip pratimų
		   MealLogDB logDB = new MealLogDB();
		   java.util.ArrayList<Object[]> logs = logDB.getMealLogsByDate(get, currentViewDate);

	       DefaultTableModel model = (DefaultTableModel)table.getModel();
	       model.setRowCount(0);
	       for (Object[] log : logs) {
			   // log: [0]=mealID, [1]=MealName, [2]=CaloriePerGram, [3]=totalCalorieIntake
			   model.addRow(new Object[]{log[0], log[1], log[2], String.format("%.1f", (double)log[3])});
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
		   // Show selected date (or today if not set)
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
	           String muscleGroup = lr[7] != null ? (String) lr[7] : "General";
	           if (muscleGroup.isEmpty()) muscleGroup = "General";
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
	           model.addRow(new Object[]{lr[0], lr[1], muscleGroup, info, calories});
	       }
	    }

	   /** Refreshes the date dropdown from the database */
	   private void refreshDateComboBox() {
		   if (dateComboBox == null) return;
		   ExerciseLogDB logDB = new ExerciseLogDB();
		   java.util.ArrayList<String> dates = logDB.getAllLogDates(get);
		   DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
		   // Always add today at the top
		   String today = String.valueOf(LocalDate.now());
		   if (!dates.contains(today)) dates.add(0, today);
		   for (String d : dates) model.addElement(d);
		   dateComboBox.setModel(model);
		   // Select the currently viewed date
		   dateComboBox.setSelectedItem(currentViewDate);
	   }
	
	   public void Show_MealLog_In_JTable() {
		   DefaultTableModel model = (DefaultTableModel)table.getModel();
		   model.setRowCount(0);
		   Show_Meals_In_JTable();
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
				public void actionPerformed(ActionEvent e) { /* not used */ }
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
						// Daily meal log entry			   
						class InsertDailymealListener implements ActionListener{

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try{
                                    // Ištaisyta: Nebetikriname mealsID.getText().isEmpty()
                                    if(txtmealcalorie.getText().isEmpty() || txtintake.getText().isEmpty() || txtmealName.getText().isEmpty()){
                                        JOptionPane.showMessageDialog(null, "The fields can not be empty");
                                    }
                                    else{
                                        double calorieIntake=Double.parseDouble(txtmealcalorie.getText());
                                        double mealAmount=Double.parseDouble(txtintake.getText());
                                        double totalCalorie=(calorieIntake / 100.0) * mealAmount; 
                                        
                                        // 1. Sukuriame naują maistą Meals lentelėje ir gauname jo ID
                                        Meal m = new Meal();
                                        m.setMealName(txtmealName.getText());
                                        m.setCaloriesPerGram(calorieIntake);
                                        MealDB mdb = new MealDB();
                                        int generatedMealId = mdb.insertMeal(m);

                                        // 2. Išsaugome suvartojimo istoriją (Log'ą) naudodami NAUJĄ ID
                                        DailyMealLog dml = new DailyMealLog();
                                        dml.setTotalCalorieIntake(totalCalorie);
                                        int user_id=Integer.parseInt(txtuserid.getText());
                                        dml.setUserId(user_id);
                                        dml.setMealId(generatedMealId); // Štai čia priskiriamas naujasis ID!
                                        
                                        MealLogDB udb=new MealLogDB();
                                        int rowUpdate= udb.insertDailyLog(dml);
                                        
                                        if(rowUpdate>0){
                                            JOptionPane.showMessageDialog(null,
                                                "Your Meal Log Added!\nTotal calories: " + String.format("%.1f", totalCalorie) + " kcal");

                                            txtmealcalorie.setText("");
                                            txtintake.setText("");
                                            txtmealName.setText("");
                                            Show_MealLog_In_JTable();
                                        }
                                        else{
                                            JOptionPane.showMessageDialog(null, "Failed to Add Log!!");
                                        }
                                    }
                                }
                                catch(NumberFormatException ee){
                                    JOptionPane.showConfirmDialog(null, "Please enter numeric value",
                                "Invalid input", JOptionPane.CANCEL_OPTION);
                                }
                            }
                        }
}