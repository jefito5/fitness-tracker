package gui;
import javax.swing.JFrame;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.time.LocalDate;
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
	private JTextField txtexercise;
	private JTextField txtcalorieburn;
	private JTextField txtduration;
	private JButton btnDeleteE;
	private JTextField txtuserid;
	private int get;

	public trackProgress(int gets) {
		get=gets;
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
		
        table.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                   "Meal ID", "Meals Name", "Calorie Per Gram"
                }
            ));
            table.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    UsersMouseClicked(evt);
                }
            });
		
		
		
		JLabel lblAddYourExercise = new JLabel("Add your Exercises for Today!!");
		lblAddYourExercise.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblAddYourExercise.setBounds(329, 234, 233, 23);
		trackFrame.getContentPane().add(lblAddYourExercise);
		
	JScrollPane exercise = new JScrollPane();
		exercise.setBounds(330, 268, 308, 186);
		trackFrame.getContentPane().add(exercise);
		
		table2=new JTable();
		exercise.setViewportView(table2);
		
        table2.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                   "Exercise ID", "Exercise Name", "Calorie Burn/min"
                }
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
		
		JLabel lblExerciseName = new JLabel("Exercise Name:");
		lblExerciseName.setFont(new Font("Verdana", Font.PLAIN, 15));
		lblExerciseName.setBounds(340, 469, 122, 14);
		trackFrame.getContentPane().add(lblExerciseName);
		
		JLabel lblCalorieBurnmin = new JLabel("Calorie Burn/min:");
		lblCalorieBurnmin.setFont(new Font("Verdana", Font.PLAIN, 15));
		lblCalorieBurnmin.setBounds(340, 503, 131, 14);
		trackFrame.getContentPane().add(lblCalorieBurnmin);
		
		JLabel lblDuration = new JLabel("Duration(min):");
		lblDuration.setFont(new Font("Verdana", Font.PLAIN, 15));
		lblDuration.setBounds(340, 537, 131, 14);
		trackFrame.getContentPane().add(lblDuration);
		
		txtexercise = new JTextField();
		txtexercise.setColumns(10);
		txtexercise.setBounds(457, 465, 164, 23);
		trackFrame.getContentPane().add(txtexercise);
		
		txtcalorieburn = new JTextField();
		txtcalorieburn.setColumns(10);
		txtcalorieburn.setBounds(477, 500, 144, 23);
		trackFrame.getContentPane().add(txtcalorieburn);
		
		txtduration = new JTextField();
		txtduration.setColumns(10);
		txtduration.setBounds(457, 534, 164, 23);
		trackFrame.getContentPane().add(txtduration);
		
		JButton btnInsertE = new JButton("Insert");
		btnInsertE.setBounds(345, 580, 84, 23);
		trackFrame.getContentPane().add(btnInsertE);
        btnInsertE.addActionListener(new InsertDailyExerciseListener());
		
		JButton btnUpdateE = new JButton("Update");
		//btnUpdateE.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnUpdateE.setBounds(439, 580, 89, 23);
		trackFrame.getContentPane().add(btnUpdateE);
		btnUpdateE.addActionListener(new UpdateExerciseListener());
		
		btnDeleteE = new JButton("Delete");
		btnDeleteE.setBounds(538, 580, 89, 23);
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
		   
		   ExerciseDB udb=new ExerciseDB();
	       ArrayList<Exercise> exercises = udb.getExercise();
//	       for (Meal mm:meals)
//	    	   System.out.println(mm.getName());   
	       DefaultTableModel model = (DefaultTableModel)table2.getModel();
	       Object[] row = new Object[4];
	       for(int i = 0; i < exercises.size(); i++)
	       {
	           row[0]=exercises.get(i).getId();
	          
	           row[1] = exercises.get(i).getExerciseName();
	           row[2] = exercises.get(i).getCalorieburn();
	           
	           model.addRow(row);
	       }
	    }
	
	   private void UsersMouseClicked1(java.awt.event.MouseEvent evt) {                                                  
	    
	        int i = table2.getSelectedRow();

	        TableModel model = table2.getModel();
	   
	        exerciseID.setText(model.getValueAt(i,0).toString());

	        txtexercise.setText(model.getValueAt(i,1).toString());

	        txtcalorieburn.setText(model.getValueAt(i,2).toString());
  
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
		
		   class UpdateExerciseListener implements ActionListener{

				@Override
				public void actionPerformed(ActionEvent e) {
					try{
					if(exerciseID.getText().isEmpty() || 
					txtexercise.getText().isEmpty() || txtcalorieburn.getText().isEmpty()){
						JOptionPane.showMessageDialog
					(null, "Please Select exercise above to update!!");
					}
					else{
					ExerciseDB udb=new ExerciseDB();
					int exercise_id=Integer.parseInt(exerciseID.getText());
					Exercise ee=udb.getById(exercise_id);
					ee.setExerciseName(txtexercise.getText());
					
					int exercise_burn=Integer.parseInt(txtcalorieburn.getText());
					ee.setCalorieburn(exercise_burn);
					
					int rowUpdate=udb.updateExercise(ee);
					if(rowUpdate>0){
						JOptionPane.showMessageDialog(null, "Exercise Updated");
						DefaultTableModel model = (DefaultTableModel)table2.getModel();
				         model.setRowCount(0);
				         Show_Exercise_In_JTable();
				         exerciseID.setText("");
				         txtexercise.setText("");
				         txtcalorieburn.setText("");
				         
					}
					else{
						JOptionPane.showMessageDialog(null, "Failed to update Exercise");
					}
					}
					}
					catch(NumberFormatException ee){
						JOptionPane.showConfirmDialog(null,
					"Please enter numeric value", "Naughty", JOptionPane.CANCEL_OPTION);
				}
				}   
			   }
			   
		class DeleteExerciseListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(exerciseID.getText().isEmpty() ||
			txtexercise.getText().isEmpty() || txtcalorieburn.getText().isEmpty()){
				JOptionPane.showMessageDialog(null, "Please select exercise above to delete!!");
			}
			else{
			ExerciseDB udb=new ExerciseDB();
			int exercise_id=Integer.parseInt(exerciseID.getText());
			Exercise ee=udb.getById(exercise_id); 
			int rowUpdate= udb.deleteExercise(ee);
			if(rowUpdate>0){
			JOptionPane.showMessageDialog(null, "Exercise Deleted");
			DefaultTableModel model = (DefaultTableModel)table2.getModel();
		    model.setRowCount(0);
		    Show_Exercise_In_JTable();
			exerciseID.setText("");
			txtexercise.setText("");
			txtcalorieburn.setText("");
				
			}
			else{
			JOptionPane.showMessageDialog(null, "Failed to delete Exercise");
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
								"Naughty", JOptionPane.CANCEL_OPTION);
								}
								
								
							}
							}
			//code for daily dairy of exercise				
						class InsertDailyExerciseListener implements ActionListener{

							@Override
							public void actionPerformed(ActionEvent e) {
								try{
								if(txtcalorieburn.getText().isEmpty() || txtduration.getText().isEmpty()
									|| exerciseID.getText().isEmpty()
										|| txtexercise.getText().isEmpty()){
									JOptionPane.showMessageDialog(null, "The fields can not be empty");
								}
								else{
								DailyExerciseLog del = new DailyExerciseLog();
								double calorieBurn=Double.parseDouble(txtcalorieburn.getText());
								double exerciseDuration=Double.parseDouble(txtduration.getText());
								double totalCalorie=calorieBurn*exerciseDuration;
								
								del.setTotalCalorieBurn(totalCalorie);
								int user_id=Integer.parseInt(txtuserid.getText());
								del.setUserId(user_id);
								int exercise_id=Integer.parseInt(exerciseID.getText());
								del.setExerciseId(exercise_id);
								
								ExerciseLogDB udb=new ExerciseLogDB();
								int rowUpdate= udb.insertDailyLog(del);
								
								if(rowUpdate>0){
								JOptionPane.showMessageDialog(null, "Your Exercise Log Added!");	
								
									txtcalorieburn.setText("");
									txtduration.setText("");
									txtexercise.setText("");
									
								}
								else{
									JOptionPane.showMessageDialog(null, "Failed to Add Log!!");
								}
								}
								}
								catch(NumberFormatException ee){
									JOptionPane.showConfirmDialog(null, "Please enter numeric value",
									"Naughty", JOptionPane.CANCEL_OPTION);
								}
								
							}
						}
}
						

