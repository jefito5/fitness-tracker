package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import impl.MealDB;
import impl.MealLogDB;
import models.DailyMealLog;
import services.FoodImporter;
import impl.UserDB;
import models.Meal;
import models.User;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

public class MealIUD {

	private JFrame frame;
	private JTextField txtmealname;
	private JTextField txtcalorieG;
	private JTextField txtweight;
	private JTextField txtprotein;
	private JTextField txtcarbs;
	private JTextField txtfat;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JComboBox<String> textField_3;
	private JPasswordField passwordField;
	private int ids;
	private String names;
	private int ages;
	private String genders;
	private String passwords;

	
	public MealIUD(int id,String name,int age,String gender,String password) {
		ids=id;
		names=name;
		ages=age;
		genders=gender;
		passwords=password;
		//System.out.println(ids);
		initialize();
		
	}
	private void initialize() {
		frame = new JFrame();
		frame.setFont(new Font("Verdana", Font.PLAIN, 25));
		frame.setBounds(100, 100, 700, 620);
		frame.setLocation(400,100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Welcome,"+names);
		lblNewLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
		lblNewLabel.setBounds(10, 11, 272, 30);
		frame.getContentPane().add(lblNewLabel);
		
		JButton btnLogOut = new JButton("LOG OUT");
		btnLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			new Iud();
			frame.dispose();
			}
		});
		btnLogOut.setBounds(535, 11, 89, 23);
		frame.getContentPane().add(btnLogOut);
		
		JPanel panel = new JPanel();
		panel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panel.setBounds(322, 100, 293, 65);
		frame.getContentPane().add(panel);
		
		JLabel lblWhatHaveYou = new JLabel("Track Your Daily Progress");
		lblWhatHaveYou.setFont(new Font("Verdana", Font.BOLD, 18));
		panel.add(lblWhatHaveYou);
		
		JButton btnNewButton = new JButton("UPDATE LOG");
		panel.add(btnNewButton);
		btnNewButton.addActionListener(new OpenLogListener());
		
		// --- NAUJAS MYGTUKAS ANALIZEI IR DIAGRAMAI ---
        JButton btnAnalyse = new JButton("ANALYSE PROGRESS");
        btnAnalyse.setBounds(322, 175, 293, 30); // Dedamas po "Track Your Daily Progress" rėmeliu
        frame.getContentPane().add(btnAnalyse);
        
        btnAnalyse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TIESIOGIAI iškviečiame TrendLine (Diagramos) langą!
                new PeriodSelect(ids); 
            }
        });

        // FT-120: Macro Ratio Goals button
        JButton btnMacroGoals = new JButton("MACRO GOALS");
        btnMacroGoals.setBounds(322, 212, 140, 28);
        frame.getContentPane().add(btnMacroGoals);
        btnMacroGoals.addActionListener(e -> new MacroGoalsPanel(ids));

        // FT-122: BMI button
        JButton btnBmi = new JButton("BMI CHECK");
        btnBmi.setBounds(472, 212, 140, 28);
        frame.getContentPane().add(btnBmi);
        btnBmi.addActionListener(e -> new BmiPanel(ids));
        // ----------------------------------------------
		
		JLabel lblDoneSomethingNew = new JLabel("Have Something New?");
		lblDoneSomethingNew.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblDoneSomethingNew.setBounds(10, 243, 211, 30);
		frame.getContentPane().add(lblDoneSomethingNew);
		
		JLabel lblAddMeals = new JLabel("ADD MEALS:");
		lblAddMeals.setFont(new Font("Verdana", Font.PLAIN, 18));
		lblAddMeals.setBounds(20, 284, 130, 30);
		frame.getContentPane().add(lblAddMeals);
		
		txtmealname = new JTextField();
		txtmealname.setBounds(140, 325, 156, 30);
		frame.getContentPane().add(txtmealname);
		txtmealname.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Meal Name:");
		lblNewLabel_1.setFont(new Font("Verdana", Font.PLAIN, 15));
		lblNewLabel_1.setBounds(10, 332, 100, 23);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblCaloriegram = new JLabel("Calories/100g:");
		lblCaloriegram.setFont(new Font("Verdana", Font.PLAIN, 15));
		lblCaloriegram.setBounds(10, 373, 120, 23);
		frame.getContentPane().add(lblCaloriegram);
		
		txtcalorieG = new JTextField();
		txtcalorieG.setColumns(10);
		txtcalorieG.setBounds(140, 366, 156, 30);
		frame.getContentPane().add(txtcalorieG);
		
		JLabel lblWeight = new JLabel("Weight (g):");
		lblWeight.setFont(new Font("Verdana", Font.PLAIN, 15));
		lblWeight.setBounds(10, 407, 120, 23);
		frame.getContentPane().add(lblWeight);

		txtweight = new JTextField();
		txtweight.setColumns(10);
		txtweight.setBounds(140, 404, 156, 30);
		frame.getContentPane().add(txtweight);

		JLabel lblProtein = new JLabel("Protein (g/100g):");
		lblProtein.setFont(new Font("Verdana", Font.PLAIN, 13));
		lblProtein.setBounds(10, 440, 130, 23);
		frame.getContentPane().add(lblProtein);

		txtprotein = new JTextField();
		txtprotein.setColumns(10);
		txtprotein.setBounds(140, 438, 156, 25);
		frame.getContentPane().add(txtprotein);

		JLabel lblCarbs = new JLabel("Carbs (g/100g):");
		lblCarbs.setFont(new Font("Verdana", Font.PLAIN, 13));
		lblCarbs.setBounds(10, 468, 130, 23);
		frame.getContentPane().add(lblCarbs);

		txtcarbs = new JTextField();
		txtcarbs.setColumns(10);
		txtcarbs.setBounds(140, 466, 156, 25);
		frame.getContentPane().add(txtcarbs);

		JLabel lblFat = new JLabel("Fat (g/100g):");
		lblFat.setFont(new Font("Verdana", Font.PLAIN, 13));
		lblFat.setBounds(10, 496, 130, 23);
		frame.getContentPane().add(lblFat);

		txtfat = new JTextField();
		txtfat.setColumns(10);
		txtfat.setBounds(140, 494, 156, 25);
		frame.getContentPane().add(txtfat);

		JButton btnInsert = new JButton("INSERT MEAL");
		btnInsert.setBounds(76, 524, 120, 23);
		frame.getContentPane().add(btnInsert);
		btnInsert.addActionListener(new InsertMealListener());
		
		JLabel lblAddExercise = new JLabel("ADD EXERCISES:");
		lblAddExercise.setFont(new Font("Verdana", Font.PLAIN, 18));
		lblAddExercise.setBounds(322, 284, 176, 30);
		frame.getContentPane().add(lblAddExercise);

		JButton btnBrowseExercises = new JButton("Browse Exercises");
		btnBrowseExercises.setFont(new Font("Verdana", Font.PLAIN, 13));
		btnBrowseExercises.setBounds(322, 325, 180, 30);
		btnBrowseExercises.addActionListener(e -> new ExerciseIUD(ids, 70.0));
		frame.getContentPane().add(btnBrowseExercises);

		textField = new JTextField();
		textField.setBounds(292, 11, 86, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		String iddd=String.valueOf(ids);
		textField.setText(iddd);

		
		textField_1 = new JTextField();
		textField_1.setBounds(110, 78, 131, 20);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		textField_1.setText(names);
		
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(110, 107, 131, 20);
		frame.getContentPane().add(textField_2);
		String aggg=String.valueOf(ages);
		textField_2.setText(aggg);
	
		
		
		textField_3 = new JComboBox<>(new String[]{"male", "female"});
		textField_3.setBounds(110, 138, 131, 20);
		frame.getContentPane().add(textField_3);
		textField_3.setSelectedItem(genders);
		
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(20, 81, 46, 14);
		frame.getContentPane().add(lblName);
		
		JLabel lblAge = new JLabel("Age:");
		lblAge.setBounds(20, 110, 46, 14);
		frame.getContentPane().add(lblAge);
		
		JLabel lblGender = new JLabel("Gender:");
		lblGender.setBounds(20, 146, 46, 14);
		frame.getContentPane().add(lblGender);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(20, 172, 64, 14);
		frame.getContentPane().add(lblPassword);
		
		JButton btnUpdate = new JButton("UPDATE");
		btnUpdate.setBounds(72, 197, 89, 23);
		frame.getContentPane().add(btnUpdate);
		btnUpdate.addActionListener(new UpdateProfileListener());
		
		JLabel lblUpdateYourInformations = new JLabel("Update Your Informations:");
		lblUpdateYourInformations.setFont(new Font("Verdana", Font.PLAIN, 13));
		lblUpdateYourInformations.setBounds(20, 43, 221, 23);
		frame.getContentPane().add(lblUpdateYourInformations);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(110, 169, 131, 20);
		frame.getContentPane().add(passwordField);
		passwordField.setText(passwords);
		
		frame.setVisible(true);
	}
	
	class OpenLogListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			int asd=Integer.parseInt(textField.getText());
			new trackProgress(asd);
			System.out.println(textField.getText());

		}

	}
	class UpdateProfileListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			UserDB udb=new UserDB();
			int text_3=Integer.parseInt(textField.getText());
			User u=udb.getById(text_3); 
			u.setName(textField_1.getText());
			u.setGender((String) textField_3.getSelectedItem());
			int text_2=Integer.parseInt(textField_2.getText());
			u.setAge(text_2);
			u.setPassword(passwordField.getText());
			udb.update(u);
			/*int rowUpdate=udb.update(u);
			if(rowUpdate>0){
				JOptionPane.showMessageDialog(null, "User Updated");
			}
			else{
				JOptionPane.showMessageDialog(null, "Failed to update user");
			}	*/
			
		}
		
	}
	
	class InsertMealListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try{
			if(txtmealname.getText().isEmpty() || txtcalorieG.getText().isEmpty()){
				JOptionPane.showMessageDialog(null, "The fields cannot be empty!!");
			}
			else{
			if (txtweight.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Please enter weight in grams!");
				return;
			}
			double grams       = Double.parseDouble(txtweight.getText());
			double kcalPer100g = Double.parseDouble(txtcalorieG.getText());
			double totalKcal   = kcalPer100g * grams / 100.0;

			// Parse optional macro fields – default to 0 if left blank
			double protein = txtprotein.getText().isEmpty() ? 0 : Double.parseDouble(txtprotein.getText());
			double carbs   = txtcarbs.getText().isEmpty()   ? 0 : Double.parseDouble(txtcarbs.getText());
			double fat     = txtfat.getText().isEmpty()     ? 0 : Double.parseDouble(txtfat.getText());

			// 1. Save to meals catalogue
			Meal m = new Meal();
			m.setMealName(txtmealname.getText());
			m.setCaloriesPerGram(kcalPer100g);
			m.setProteinPer100g(protein);
			m.setCarbsPer100g(carbs);
			m.setFatPer100g(fat);
			MealDB udb = new MealDB();
			int mealId = udb.insertMeal(m);

			// Išsaugoti į foods DB ir foods_seed.csv, jei tokio pavadinimo dar nėra
			impl.FoodDB fdb = new impl.FoodDB();
			models.Food newFood = new models.Food(
				txtmealname.getText(), kcalPer100g, protein, carbs, fat);
			fdb.insertIfNew(newFood);
			FoodImporter.appendIfNew(
				txtmealname.getText(), kcalPer100g, protein, carbs, fat);

			// 2. Save to DailyMealLog
			DailyMealLog dml = new DailyMealLog();
			dml.setMealId(mealId);
			dml.setUserId(ids);
			dml.setTotalCalorieIntake(totalKcal);
			new MealLogDB().insertDailyLog(dml);

			JOptionPane.showMessageDialog(null,
				"Meal Added!\n" + txtmealname.getText() +
				"\n" + grams + "g = " + String.format("%.1f", totalKcal) + " kcal");

				txtmealname.setText("");
				txtcalorieG.setText("");
				txtweight.setText("");
				txtprotein.setText("");
				txtcarbs.setText("");
				txtfat.setText("");
			}
			}
			catch(NumberFormatException eee){
				JOptionPane.showConfirmDialog
				(null, "Please enter numeric value in calorie", "Naughty", JOptionPane.CANCEL_OPTION);
			}
			
		}
		}
	
}