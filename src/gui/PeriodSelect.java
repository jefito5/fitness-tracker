package gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import impl.ExerciseLogDB;
import impl.MealLogDB;
import impl.UserDB;
import impl.WeightDB;
import models.User;
import models.Weight;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class PeriodSelect {

	private JFrame frame;
	private JComboBox comboBox;
	private JComboBox comboBox_1;
	private JComboBox comboBox_2;
	private JComboBox comboBox_3;
	private JComboBox comboBox_4;
	private JComboBox comboBox_5;
	private JRadioButton rdbtnNewRadioButton;
	private JRadioButton rdbtnActive;
	private JRadioButton rdbtnModeratelyActive;
	private JTextField txtUserid;
	private JLabel lblYear;
	private JLabel lblMonths;
	private JLabel lblDay;
	private JLabel lblYear_1;
	private JLabel lblMonth;
	private JLabel lblDay_1;
	private JLabel label;
	private JLabel lblNewLabel;
	private JLabel label_1;
	private JLabel label_2;
	private JLabel label_4;
	private JLabel label_6;
	private int getID;
	
	public PeriodSelect(int IDD) {
		getID=IDD;
		initialize();
	}
	private void initialize() {
		frame = new JFrame("Analyse");
		frame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 12));
		frame.setSize(499, 580);
		frame.setLocation(500,100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblWeNeedFollowing = new JLabel("KNOW PROGRESS");
		lblWeNeedFollowing.setFont(new Font("Verdana", Font.PLAIN, 20));
		lblWeNeedFollowing.setBounds(133, 11, 201, 31);
		frame.getContentPane().add(lblWeNeedFollowing);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		btnBack.setFont(new Font("Verdana", Font.PLAIN, 15));
		btnBack.setBounds(10, 11, 89, 21);
		frame.getContentPane().add(btnBack);
		
		JLabel lblWhatsYourActivity = new JLabel("What is Your Activity Level Today?");
		lblWhatsYourActivity.setFont(new Font("Verdana", Font.PLAIN, 16));
		lblWhatsYourActivity.setBounds(10, 53, 295, 21);
		frame.getContentPane().add(lblWhatsYourActivity);
		
		 rdbtnNewRadioButton = new JRadioButton("Inactive");
		rdbtnNewRadioButton.setBounds(10, 72, 71, 23);
		frame.getContentPane().add(rdbtnNewRadioButton);
		
		 rdbtnModeratelyActive = new JRadioButton("Moderately Active");
		rdbtnModeratelyActive.setBounds(83, 72, 132, 23);
		frame.getContentPane().add(rdbtnModeratelyActive);
		
		 rdbtnActive = new JRadioButton("Active");
		rdbtnActive.setBounds(214, 72, 89, 23);
		frame.getContentPane().add(rdbtnActive);
		
		ButtonGroup bg=new ButtonGroup();
		bg.add(rdbtnNewRadioButton);
		bg.add(rdbtnActive);
		bg.add(rdbtnModeratelyActive);
		
		JButton btnProceed = new JButton("PROCEED");
		btnProceed.setBounds(186, 207, 89, 23);
		frame.getContentPane().add(btnProceed);
	
		JLabel lblFrom = new JLabel("FROM:");
		lblFrom.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblFrom.setBounds(10, 135, 59, 21);
		frame.getContentPane().add(lblFrom);

		 comboBox = new JComboBox();
		comboBox.setBounds(10, 167, 59, 20);
		frame.getContentPane().add(comboBox);
		for(int i=2026;i<=2032;i++){
		comboBox.addItem(i);
		}
					
		 comboBox_1 = new JComboBox();
		comboBox_1.setBounds(79, 167, 59, 20);
		frame.getContentPane().add(comboBox_1);
		for(int i=1;i<=12;i++){
			comboBox_1.addItem(i);
		}
	
		 comboBox_2 = new JComboBox();
		comboBox_2.setBounds(148, 167, 59, 20);
		frame.getContentPane().add(comboBox_2);
		for(int i=1;i<=31;i++){
			comboBox_2.addItem(i);
		}
			
		JLabel lblTo = new JLabel("TO:");
		lblTo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTo.setBounds(280, 138, 46, 14);
		frame.getContentPane().add(lblTo);
		
		 comboBox_3 = new JComboBox();
		comboBox_3.setBounds(280, 167, 59, 20);
		frame.getContentPane().add(comboBox_3);
		for(int i=2026;i<=2032;i++){
			comboBox_3.addItem(i);
		}
		
		 comboBox_4 = new JComboBox();
		comboBox_4.setBounds(349, 167, 57, 20);
		frame.getContentPane().add(comboBox_4);
		for(int i=1;i<=12;i++){
			comboBox_4.addItem(i);
		}
		
		 comboBox_5 = new JComboBox();
		comboBox_5.setBounds(416, 167, 57, 20);
		frame.getContentPane().add(comboBox_5);
		for(int i=1;i<=31;i++){
			comboBox_5.addItem(i);
		}
		
		txtUserid = new JTextField();
		txtUserid.setBounds(387, 11, 86, 20);
		frame.getContentPane().add(txtUserid);
		txtUserid.setColumns(10);
		String fgf=String.valueOf(getID);
		txtUserid.setText(fgf);
		
		lblYear = new JLabel("Year:");
		lblYear.setBounds(10, 153, 46, 14);
		frame.getContentPane().add(lblYear);
		
		lblMonths = new JLabel("Month:");
		lblMonths.setBounds(79, 153, 46, 14);
		frame.getContentPane().add(lblMonths);
		
		lblDay = new JLabel("Day:");
		lblDay.setBounds(148, 153, 46, 14);
		frame.getContentPane().add(lblDay);
		
		lblYear_1 = new JLabel("Year:");
		lblYear_1.setBounds(280, 153, 46, 14);
		frame.getContentPane().add(lblYear_1);
		
		lblMonth = new JLabel("Month:");
		lblMonth.setBounds(349, 153, 46, 14);
		frame.getContentPane().add(lblMonth);
		
		lblDay_1 = new JLabel("Day:");
		lblDay_1.setBounds(416, 153, 46, 14);
		frame.getContentPane().add(lblDay_1);
		
		JButton btnViewChart = new JButton("VIEW CHART");
		btnViewChart.setBounds(339, 72, 107, 23);
		frame.getContentPane().add(btnViewChart);
		
		JLabel lblSelectPeriod = new JLabel("SELECT PERIOD:");
		lblSelectPeriod.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblSelectPeriod.setBounds(10, 102, 141, 25);
		frame.getContentPane().add(lblSelectPeriod);
		
		JLabel lblYourProgressReport = new JLabel("YOUR PROGRESS REPORT");
		lblYourProgressReport.setFont(new Font("Verdana", Font.BOLD, 16));
		lblYourProgressReport.setBounds(102, 257, 241, 31);
		frame.getContentPane().add(lblYourProgressReport);
		
		JLabel lblTotalCalorieIntake = new JLabel("Total Calorie Intake Necessary For today:");
		lblTotalCalorieIntake.setFont(new Font("Verdana", Font.PLAIN, 13));
		lblTotalCalorieIntake.setBounds(10, 299, 278, 21);
		frame.getContentPane().add(lblTotalCalorieIntake);
		
		 label = new JLabel("?");
		label.setFont(new Font("Verdana", Font.BOLD, 13));
		label.setBounds(294, 299, 71, 21);
		frame.getContentPane().add(label);
		
		JLabel lblWeightSummary = new JLabel("Weight Summary:");
		lblWeightSummary.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblWeightSummary.setBounds(10, 342, 150, 31);
		frame.getContentPane().add(lblWeightSummary);
		
		JLabel lblWeightLossgainselectedPeriod = new JLabel("Weight LOSS/GAIN(SELECTED PERIOD):");
		lblWeightLossgainselectedPeriod.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblWeightLossgainselectedPeriod.setBounds(10, 384, 205, 21);
		frame.getContentPane().add(lblWeightLossgainselectedPeriod);
		
		lblNewLabel = new JLabel("?");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel.setBounds(32, 404, 175, 21);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblWeightLossgainstartTo = new JLabel("Weight LOSS/GAIN(START TO END):");
		lblWeightLossgainstartTo.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblWeightLossgainstartTo.setBounds(10, 436, 192, 21);
		frame.getContentPane().add(lblWeightLossgainstartTo);
		
		label_1 = new JLabel("?");
		label_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		label_1.setBounds(32, 460, 175, 21);
		frame.getContentPane().add(label_1);
		
		JLabel lblCalorieInformation = new JLabel("Calorie Information(For Today):");
		lblCalorieInformation.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblCalorieInformation.setBounds(227, 342, 246, 31);
		frame.getContentPane().add(lblCalorieInformation);
		
		JLabel lblTotalCalorieIntake_1 = new JLabel("Total Calorie Intake:");
		lblTotalCalorieIntake_1.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblTotalCalorieIntake_1.setBounds(227, 384, 107, 21);
		frame.getContentPane().add(lblTotalCalorieIntake_1);
		
		 label_2 = new JLabel("?");
		label_2.setFont(new Font("Tahoma", Font.BOLD, 13));
		label_2.setBounds(346, 384, 127, 21);
		frame.getContentPane().add(label_2);
		
		JLabel lblTotalCalorieBurned = new JLabel("Total Calorie Burned:");
		lblTotalCalorieBurned.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblTotalCalorieBurned.setBounds(227, 409, 107, 21);
		frame.getContentPane().add(lblTotalCalorieBurned);
		
		 label_4 = new JLabel("?");
		label_4.setFont(new Font("Tahoma", Font.BOLD, 13));
		label_4.setBounds(346, 408, 127, 21);
		frame.getContentPane().add(label_4);
		
		JLabel lblCalorieDifference = new JLabel("Calorie Difference:");
		lblCalorieDifference.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblCalorieDifference.setBounds(227, 436, 107, 21);
		frame.getContentPane().add(lblCalorieDifference);
		
		 label_6 = new JLabel("?");
		label_6.setFont(new Font("Tahoma", Font.BOLD, 13));
		label_6.setBounds(346, 435, 127, 21);
		frame.getContentPane().add(label_6);
		
		JLabel lblSeeYourProgress = new JLabel("See Your Progress Chart");
		lblSeeYourProgress.setFont(new Font("Verdana", Font.PLAIN, 13));
		lblSeeYourProgress.setBounds(309, 53, 164, 20);
		frame.getContentPane().add(lblSeeYourProgress);

		frame.setVisible(true);
		btnViewChart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int asd=Integer.parseInt(txtUserid.getText());
				new TrendLine(asd);
				frame.setVisible(false);
				
			}
		});
		
		
		btnProceed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calorieInformations();
				activityLevelSelection();
				try {
					int fromYear  = (int) comboBox.getSelectedItem();
					int fromMonth = (int) comboBox_1.getSelectedItem();
					int fromDay   = (int) comboBox_2.getSelectedItem();
					int toYear    = (int) comboBox_3.getSelectedItem();
					int toMonth   = (int) comboBox_4.getSelectedItem();
					int toDay     = (int) comboBox_5.getSelectedItem();

					// Build zero-padded ISO dates so SQLite date comparison works correctly
					String fromDate = String.format("%04d-%02d-%02d", fromYear, fromMonth, fromDay);
					String toDate   = String.format("%04d-%02d-%02d", toYear,   toMonth,   toDay);

					// Validate: FROM must not be after TO
					if (fromDate.compareTo(toDate) > 0) {
						JOptionPane.showMessageDialog(null,
							"Invalid date range: FROM date must be before or equal to TO date.",
							"Invalid Range", JOptionPane.WARNING_MESSAGE);
						return;
					}

					int uID = Integer.parseInt(txtUserid.getText());
					Weight wt = new Weight();
					wt.setPeriod1(fromDate);
					wt.setPeriod2(toDate);
					wt.setUserId(uID);

					WeightDB wdb = new WeightDB();

					// Weight change for the selected period
					ArrayList<Double> averages = wdb.getWeight(wt);

					if (averages == null || averages.isEmpty()) {
						lblNewLabel.setText("No data in range");
					} else {
						// Filter out zero/unset averages
						ArrayList<Double> valid = new ArrayList<>();
						for (double v : averages) { if (v > 0) valid.add(v); }

						if (valid.isEmpty()) {
							lblNewLabel.setText("No valid data in range");
						} else {
							double periodChange = valid.get(0) - valid.get(valid.size() - 1);
							lblNewLabel.setText(String.format("%.2f kg", periodChange));
						}
					}

					// Total weight change from very first to most recent entry (scoped to this user)
					ArrayList<Double> allEntries = wdb.getStartEndForUser(uID);

					if (allEntries == null || allEntries.isEmpty()) {
						label_1.setText("No data available");
					} else {
						double totalChange = allEntries.get(0) - allEntries.get(allEntries.size() - 1);
						label_1.setText(String.format("%.2f kg", totalChange));
					}

					frame.setSize(499, 580);

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null,
						"Could not calculate weight change. Please ensure a valid date range is selected.",
						"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
	}

	public void activityLevelSelection(){
		
		UserDB udb=new UserDB();
		int user_id=Integer.parseInt(txtUserid.getText());
		User u=udb.getById(user_id);
		WeightDB wdb=new WeightDB();
		//u.setId(user_id);
		ArrayList<Double> gets=wdb.getAverages();

		if (gets == null || gets.isEmpty()) {
			label.setText("No weight data for today");
			return;
		}

		if(rdbtnNewRadioButton.
		isSelected()==false && rdbtnModeratelyActive.isSelected()==false && 
		rdbtnActive.isSelected()==false ){
			JOptionPane.showMessageDialog(null, "Please select your activity level");
		}
		else{
		if(rdbtnNewRadioButton.isSelected()){
		if(u.getGender().equals("male")){
			double nessCalI=5*gets.get(0);
			//System.out.println(nessCalI);
			String inactive=String.valueOf(nessCalI);
			label.setText(inactive);
		}
		else{
			double nessCalI=4*gets.get(0);	
			String inactive=String.valueOf(nessCalI);
			label.setText(inactive);
		}
		}
		else if(rdbtnModeratelyActive.isSelected()){
			if(u.getGender().equals("male")){
				double nessCalM=6*gets.get(0);	
				String Mactive=String.valueOf(nessCalM);
				label.setText(Mactive);
			}
			else{
				double nessCalM=5*gets.get(0);	
				String Mactive=String.valueOf(nessCalM);
				label.setText(Mactive);
			}
		}
		else if(rdbtnActive.isSelected()){
			if(u.getGender().equals("male")){
				double nessCalA=7.5*gets.get(0);
				String active=String.valueOf(nessCalA);
				label.setText(active);
			}
			else{
				double nessCalA=6*gets.get(0);	
				String active=String.valueOf(nessCalA);
				label.setText(active);
				}
		}
		}	
		}
	
	public void calorieInformations(){
		try{
		MealLogDB mdb=new MealLogDB();
		User u =new User();
		int iss=Integer.parseInt(txtUserid.getText());
		u.setId(iss);
		//calculating total calorie intake for day
		ArrayList<Double> aa=mdb.getSum();
		double totalCalorieSum=0;
		//System.out.println("value is"+aa.get(0));
		for(Double d:aa)
			totalCalorieSum+=d;
		String intakes=String.valueOf(totalCalorieSum);
		label_2.setText(intakes);
		
		//calculating total calorie burn for day
		ExerciseLogDB edb=new ExerciseLogDB();
		ArrayList<Double> aea=edb.getburnSum();
		double totalCalorieBurn=0;
		for(Double dd:aea)
			totalCalorieBurn+=dd;
		
		String burned=String.valueOf(totalCalorieBurn);
		label_4.setText(burned);
		
		//calculating calorie difference
		double CalorieDifference=totalCalorieSum-totalCalorieBurn;
		String difference=String.valueOf(CalorieDifference);
		label_6.setText(difference);
		}
		catch(Exception ee){
			JOptionPane.showMessageDialog(null, "It seems you have not taken any meals or exercises");
		}
	}
	}