package gui;

import impl.UserDB;
import models.User;
import theme.UITheme;
import components.RoundedButton;
import components.StyledTextField;
import components.StyledPasswordField;
import components.SectionHeader;
import components.StyledMessage;
import components.StyledComboBox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Iud {

    private JFrame frame;
    private StyledTextField textField; // Registracijos vardas
    private StyledComboBox<String> textField_1; // Lytis
    private StyledTextField textField_2; // Amžius
    private StyledTextField textField_4; // Prisijungimo vardas
    private StyledTextField textField_height; // Ūgis
    private StyledPasswordField passwordField; // Registracijos slaptažodis
    private StyledPasswordField passwordField_1; // Prisijungimo slaptažodis

    public Iud() {
        initialize();   
    }

    private void initialize() {
        frame = new JFrame("Registration & Login Panel");
        frame.setBounds(100, 100, 600, 400);
        frame.setLocation(430, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.getContentPane().setBackground(UITheme.BACKGROUND);

        // --- REGISTRATION SECTION (Dešinė pusė) ---
        SectionHeader lblRegistration = new SectionHeader("REGISTRATION");
        lblRegistration.setBounds(318, 11, 222, 30);
        frame.getContentPane().add(lblRegistration);

        JLabel lblName = new JLabel("Name:");
        lblName.setFont(UITheme.FONT_REGULAR);
        lblName.setForeground(UITheme.TEXT_MAIN);
        lblName.setBounds(318, 55, 50, 20);
        frame.getContentPane().add(lblName);

        textField = new StyledTextField(10);
        textField.setBounds(400, 50, 140, 30);
        frame.getContentPane().add(textField);

        JLabel lblGender = new JLabel("Gender:");
        lblGender.setFont(UITheme.FONT_REGULAR);
        lblGender.setForeground(UITheme.TEXT_MAIN);
        lblGender.setBounds(318, 95, 60, 20);
        frame.getContentPane().add(lblGender);

        textField_1 = new StyledComboBox<>(new String[]{"male", "female"});
        textField_1.setBounds(400, 90, 140, 30);
        frame.getContentPane().add(textField_1);

        JLabel lblAge = new JLabel("Age:");
        lblAge.setFont(UITheme.FONT_REGULAR);
        lblAge.setForeground(UITheme.TEXT_MAIN);
        lblAge.setBounds(318, 135, 46, 20);
        frame.getContentPane().add(lblAge);

        textField_2 = new StyledTextField(10);
        textField_2.setBounds(400, 130, 140, 30);
        frame.getContentPane().add(textField_2);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(UITheme.FONT_REGULAR);
        lblPassword.setForeground(UITheme.TEXT_MAIN);
        lblPassword.setBounds(318, 175, 80, 20);
        frame.getContentPane().add(lblPassword);

        passwordField = new StyledPasswordField(10);
        passwordField.setBounds(400, 170, 140, 30);
        frame.getContentPane().add(passwordField);

        JLabel lblHeight = new JLabel("Height (cm):");
        lblHeight.setFont(UITheme.FONT_REGULAR);
        lblHeight.setForeground(UITheme.TEXT_MAIN);
        lblHeight.setBounds(318, 215, 80, 20);
        frame.getContentPane().add(lblHeight);

        textField_height = new StyledTextField(10);
        textField_height.setBounds(400, 210, 140, 30);
        frame.getContentPane().add(textField_height);

        RoundedButton btnInsert = new RoundedButton("Register");
        btnInsert.setBounds(400, 260, 140, 35);
        frame.getContentPane().add(btnInsert);
        btnInsert.addActionListener(new InsertListener());


        // --- LOGIN SECTION (Kairė pusė) ---
        SectionHeader lblLogin = new SectionHeader("LOGIN");
        lblLogin.setBounds(24, 11, 150, 30);
        frame.getContentPane().add(lblLogin);

        JLabel lblName_1 = new JLabel("Name:");
        lblName_1.setFont(UITheme.FONT_REGULAR);
        lblName_1.setForeground(UITheme.TEXT_MAIN);
        lblName_1.setBounds(24, 55, 60, 20);
        frame.getContentPane().add(lblName_1);

        textField_4 = new StyledTextField(10);
        textField_4.setBounds(100, 50, 140, 30);
        frame.getContentPane().add(textField_4);

        JLabel lblPassword_1 = new JLabel("Password:");
        lblPassword_1.setFont(UITheme.FONT_REGULAR);
        lblPassword_1.setForeground(UITheme.TEXT_MAIN);
        lblPassword_1.setBounds(24, 95, 80, 20);
        frame.getContentPane().add(lblPassword_1);

        passwordField_1 = new StyledPasswordField(10);
        passwordField_1.setBounds(100, 90, 140, 30);
        frame.getContentPane().add(passwordField_1);

        RoundedButton btnLogin = new RoundedButton("Login");
        btnLogin.setBounds(100, 140, 140, 35);
        frame.getContentPane().add(btnLogin);
        btnLogin.addActionListener(new loginCheckListener());

        JLabel lblNewHerePlease = new JLabel("New here? Please register first!");
        lblNewHerePlease.setFont(UITheme.FONT_SMALL);
        lblNewHerePlease.setForeground(UITheme.TEXT_MUTED);
        lblNewHerePlease.setBounds(24, 250, 250, 20);
        frame.getContentPane().add(lblNewHerePlease);
        
        frame.setVisible(true);
    }
           
    // PRISIJUNGIMO LOGIKA
    class loginCheckListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(textField_4.getText().isEmpty() || new String(passwordField_1.getPassword()).isEmpty()){
                StyledMessage.show("Warning", "The fields can not be empty!!");
            }
            else{
                UserDB udb = new UserDB();
                User u = udb.getByName(textField_4.getText());
                
                try{
                    if(u != null && u.getName().equals(textField_4.getText()) && 
                       u.getPassword().equals(new String(passwordField_1.getPassword()))){
                        
                        StyledMessage.show("Success", "Login Successful!!");
                        new MealIUD(u.getId(), u.getName(), u.getAge(), u.getGender(), u.getPassword());
                        frame.setVisible(false);
                    }
                    else {
                        StyledMessage.show("Error", "Login Failed! Check name or password.");
                    }   
                }
                catch(Exception ee){
                    StyledMessage.show("Error", "User does not exist.");
                }
            }   
        }
    }
    
    // REGISTRACIJOS LOGIKA
    class InsertListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                if(textField.getText().isEmpty() ||
                   textField_2.getText().isEmpty() || new String(passwordField.getPassword()).isEmpty()){
                    StyledMessage.show("Warning", "All fields are required!");
                }
                else{
                    User u = new User();
                    u.setName(textField.getText());
                    u.setAge(Integer.parseInt(textField_2.getText()));
                    u.setGender((String) textField_1.getSelectedItem());
                    u.setPassword(new String(passwordField.getPassword()));
                    
                    try {
                        String hStr = textField_height.getText().trim();
                        if (!hStr.isEmpty()) u.setHeight(Double.parseDouble(hStr));
                    } catch (NumberFormatException ignored) {}
                    
                    UserDB udb = new UserDB();
                    udb.insert(u);
                    
                    StyledMessage.show("Success", "Account created! You can now login.");
                        
                    textField.setText("");
                    textField_1.setSelectedIndex(0);
                    textField_2.setText("");
                    passwordField.setText("");
                    textField_height.setText("");
                }
            }
            catch(NumberFormatException eee){
                StyledMessage.show("Error", "Please enter numeric values for age and height.");
            }
        }
    }
}