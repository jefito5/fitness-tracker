package gui;

import impl.UserDB;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import models.User;
public class Iud {

	private JFrame frame;
	private JTextField textField;
	private JComboBox<String> textField_1;
	private JTextField textField_2;
	private JTextField textField_4;
	private JTextField textField_height; // NEW: height field
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;

	public Iud() {
		initialize();	
	}

	private void initialize() {
		frame = new JFrame("Registration panel");
		frame.setBounds(100, 100, 585, 300);
		frame.setLocation(430, 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(400, 43, 130, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(318, 46, 50, 14);
		frame.getContentPane().add(lblName);
		
		textField_1 = new JComboBox<>(new String[]{"male", "female"});
		textField_1.setBounds(400, 74, 130, 20);
		frame.getContentPane().add(textField_1);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(400, 105, 130, 20);
		frame.getContentPane().add(textField_2);
		
		JLabel lblGender = new JLabel("Gender:");
		lblGender.setBounds(318, 77, 46, 14);
		frame.getContentPane().add(lblGender);
		
		JLabel lblAge = new JLabel("Age:");
		lblAge.setBounds(318, 108, 46, 14);
		frame.getContentPane().add(lblAge);
		
		JButton btnInsert = new JButton("Register");
		btnInsert.setBounds(403, 190, 89, 23);
		frame.getContentPane().add(btnInsert);
		btnInsert.addActionListener(new InsertListener());
		
		JLabel lblRegistration = new JLabel("REGISTRATION");
		lblRegistration.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblRegistration.setBounds(362, 11, 130, 21);
		frame.getContentPane().add(lblRegistration);
		
		JLabel lblLogin = new JLabel("LOGIN");
		lblLogin.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblLogin.setBounds(88, 11, 64, 21);
		frame.getContentPane().add(lblLogin);
		
		textField_4 = new JTextField();
		textField_4.setBounds(98, 49, 130, 20);
		frame.getContentPane().add(textField_4);
		textField_4.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(318, 139, 68, 14);
		frame.getContentPane().add(lblPassword);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(400, 136, 130, 20);
		frame.getContentPane().add(passwordField);

		JLabel lblHeight = new JLabel("Height (cm):");
		lblHeight.setBounds(318, 163, 80, 14);
		frame.getContentPane().add(lblHeight);

		textField_height = new JTextField();
		textField_height.setBounds(400, 160, 130, 20);
		frame.getContentPane().add(textField_height);
		textField_height.setColumns(10);
		
		passwordField_1 = new JPasswordField();
		passwordField_1.setBounds(98, 77, 130, 20);
		frame.getContentPane().add(passwordField_1);
		
		JLabel lblName_1 = new JLabel("Name:");
		lblName_1.setBounds(24, 52, 46, 14);
		frame.getContentPane().add(lblName_1);
		
		JLabel lblPassword_1 = new JLabel("Password:");
		lblPassword_1.setBounds(24, 80, 64, 14);
		frame.getContentPane().add(lblPassword_1);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setBounds(115, 104, 89, 23);
		frame.getContentPane().add(btnLogin);
		btnLogin.addActionListener(new loginCheckListener());
		
		JLabel lblNewHerePlease = new JLabel("New here? Please register first!");
		lblNewHerePlease.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewHerePlease.setBounds(24, 179, 204, 14);
		frame.getContentPane().add(lblNewHerePlease);
		frame.setVisible(true);
	}
		   
	class loginCheckListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {

		if(textField_4.getText().isEmpty() || passwordField_1.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "The field can not be empty!!");
		}
		else{
		UserDB udb = new UserDB();
		User u=udb.getByName(textField_4.getText());
		
		try{
		 if(u.getName().equals(textField_4.getText()) && 
				 u.getPassword().equals(passwordField_1.getText())){
			JOptionPane.showMessageDialog(null, "Login Successfull!!");
			new MealIUD(u.getId(),u.getName(),u.getAge(),u.getGender(),u.getPassword());
			frame.setVisible(false);
		}
		else{
			JOptionPane.showMessageDialog(null, "Login Failed!!");
		}	
		}
		catch(Exception ee){
			JOptionPane.showMessageDialog(null, "There is no such name exits");
			//ee.printStackTrace();
		}
	}	
	}
	}
	
	class InsertListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try{
			if(textField.getText().isEmpty() ||
			   textField_2.getText().isEmpty() || passwordField.getText().isEmpty()){
				JOptionPane.showMessageDialog(null, "The fields can not be empty!!");
			}
			else{
			User u = new User();
			u.setName(textField.getText());
			int text_2=Integer.parseInt(textField_2.getText());
			u.setAge(text_2);
			u.setGender((String) textField_1.getSelectedItem());
			u.setPassword(passwordField.getText());
			try {
				String hStr = textField_height.getText().trim();
				if (!hStr.isEmpty()) u.setHeight(Double.parseDouble(hStr));
			} catch (NumberFormatException ignored) {}
			UserDB udb=new UserDB();
            udb.insert(u);
			//int rowUpdate= udb.insert(u);
			//if(rowUpdate>0){
			JOptionPane.showMessageDialog(null, "User Registered!");
				
				textField.setText("");
				textField_1.setSelectedIndex(0);
				textField_2.setText("");
				passwordField.setText("");
				textField_height.setText("");
			/*}
			else{
				JOptionPane.showMessageDialog(null, "Failed to register User!!");
			}*/
			}
			}
			catch(NumberFormatException eee){
				JOptionPane.showConfirmDialog(null, 
				"Please enter numeric value in age", "Naughty", JOptionPane.CANCEL_OPTION);
			}
		}
		}

}	