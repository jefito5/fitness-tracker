package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import gui.MealIUD.InsertMealListener.InsertExerciseListener;
import impl.ExerciseDB;
import impl.MealDB;
import impl.UserDB;
import models.Exercise;
import models.Meal;
import models.User;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

public class MealIUD {

	private JFrame frame;
	private JTextField txtmealname;
	private JTextField txtcalorieG;
	private JTextField txtexercise;
	private JTextField txtcalorieB;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
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
		frame.setBounds(100, 100, 650, 480);
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
		
		JLabel lblCaloriegram = new JLabel("Calorie/gram:");
		lblCaloriegram.setFont(new Font("Verdana", Font.PLAIN, 15));
		lblCaloriegram.setBounds(10, 373, 120, 23);
		frame.getContentPane().add(lblCaloriegram);
		
		txtcalorieG = new JTextField();
		txtcalorieG.setColumns(10);
		txtcalorieG.setBounds(140, 366, 156, 30);
		frame.getContentPane().add(txtcalorieG);
		
		JButton btnInsert = new JButton("INSERT MEAL");
		btnInsert.setBounds(76, 407, 120, 23);
		frame.getContentPane().add(btnInsert);
		btnInsert.addActionListener(new InsertMealListener());
		
		JLabel lblAddExercise = new JLabel("ADD EXERCISES:");
		lblAddExercise.setFont(new Font("Verdana", Font.PLAIN, 18));
		lblAddExercise.setBounds(322, 284, 176, 30);
		frame.getContentPane().add(lblAddExercise);
		
		JLabel lblExerciseName = new JLabel("Exercise Name:");
		lblExerciseName.setFont(new Font("Verdana", Font.PLAIN, 15));
		lblExerciseName.setBounds(322, 327, 120, 23);
		frame.getContentPane().add(lblExerciseName);
		
		txtexercise = new JTextField();
		txtexercise.setColumns(10);
		txtexercise.setBounds(468, 325, 156, 30);
		frame.getContentPane().add(txtexercise);
		
		JLabel lblCalorieBurnmin = new JLabel("Calorie Burn/min:");
		lblCalorieBurnmin.setFont(new Font("Verdana", Font.PLAIN, 15));
		lblCalorieBurnmin.setBounds(322, 373, 131, 23);
		frame.getContentPane().add(lblCalorieBurnmin);
		
		JButton btnInsertExercise = new JButton("INSERT EXERCISE");
		btnInsertExercise.setBounds(403, 407, 139, 23);
		frame.getContentPane().add(btnInsertExercise);
		btnInsertExercise.addActionListener(new InsertExerciseListener());
		
		txtcalorieB = new JTextField();
		txtcalorieB.setColumns(10);
		txtcalorieB.setBounds(468, 366, 156, 30);
		
		frame.getContentPane().add(txtcalorieB);
		
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
	
		
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(110, 138, 131, 20);
		frame.getContentPane().add(textField_3);
		textField_3.setText(genders);
		
		
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
			/*int asd=Integer.parseInt(textField.getText());
			new trackProgress(asd);*/
			//System.out.println(textField.getText());
			frame.setVisible(false);
			
		}
		
	}
	class UpdateProfileListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			UserDB udb=new UserDB();
			int text_3=Integer.parseInt(textField.getText());
			User u=udb.getById(text_3); 
			u.setName(textField_1.getText());
			u.setGender(textField_3.getText());
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
			Meal m = new Meal();
			m.setMealName(txtmealname.getText());
			int text_2=Integer.parseInt(txtcalorieG.getText());
			m.setCaloriesPerGram(text_2);
			
			MealDB udb=new MealDB();
			udb.insert(m);
			//int rowUpdate= udb.insert(m);
			//if(rowUpdate>0){
			JOptionPane.showMessageDialog(null, "Meal Added!");	
			
				txtmealname.setText("");
				txtcalorieG.setText("");
			/*}
			else{
				JOptionPane.showMessageDialog(null, "Failed to Add Meal!!");
			}*/
			}
			}
			catch(NumberFormatException eee){
				JOptionPane.showConfirmDialog
				(null, "Please enter numeric value in calorie", "Naughty", JOptionPane.CANCEL_OPTION);
			}
			
		}
		}
	
	class InsertExerciseListener implements ActionListener{
//here is validations for numeric input in calorie so check it out and use on other classes too
		@Override
		public void actionPerformed(ActionEvent e) {
			
			try{
			if(txtexercise.getText().isEmpty() || txtcalorieB.getText().isEmpty()){
				JOptionPane.showMessageDialog(null, "The fields can not be empty!! ");
			}
			else{
			Exercise ee = new Exercise();
			ee.setExerciseName(txtexercise.getText());
			int text_2=Integer.parseInt(txtcalorieB.getText());
			ee.setCalorieburn(text_2);
			
			ExerciseDB udb=new ExerciseDB();
			udb.insert(ee);
			/*int rowUpdate= udb.insertExercise(ee);
			if(rowUpdate>0){
			JOptionPane.showMessageDialog(null, "Exercise Added!");	
				txtexercise.setText("");
				txtcalorieB.setText("");
			}
			else{
				JOptionPane.showMessageDialog(null, "Failed to Add Exercise!!");
			}*/
			}
			}
			catch(NumberFormatException eee){
				JOptionPane.showConfirmDialog(null,
			"Please enter numeric value in calorie", "Naughty", JOptionPane.CANCEL_OPTION);
			}
		}
		}
}
