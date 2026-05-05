package gui;

import impl.UserDB;
import java.awt.*;
import javax.swing.*;
import models.User;

import theme.UITheme;
import components.SectionHeader;
import components.StyledTextField;
import components.CardPanel;
import components.RoundedButton;
import components.InfoLabel;


public class BmiPanel {

    private JFrame frame;
    private int userId;

    private JLabel lblBmiValue;
    private JLabel lblBmiCategory;
    private JLabel lblWeightUsed;
    private JLabel lblHeightUsed;
    private JLabel lblPrevBmi;
    private StyledTextField txtHeight;

    public BmiPanel(int userId) {
        this.userId = userId;
        initialize();
        refreshBmi();
    }

    private void initialize() {
        frame = new JFrame("BMI & Body Metrics");
        frame.setBounds(100, 100, 460, 420);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        
        frame.getContentPane().setBackground(UITheme.BACKGROUND);

        SectionHeader lblTitle = new SectionHeader("BMI & Body Metrics");
        lblTitle.setBounds(20, 10, 300, 28);
        frame.getContentPane().add(lblTitle);

        JLabel lblHLabel = new JLabel("Your Height (cm):");
        lblHLabel.setFont(UITheme.FONT_REGULAR);
        lblHLabel.setForeground(UITheme.TEXT_MAIN);
        lblHLabel.setBounds(20, 50, 140, 20);
        frame.getContentPane().add(lblHLabel);

        UserDB udb = new UserDB();
        User u = udb.getById(userId);
        double currentHeight = (u != null) ? u.getHeight() : 0;

        txtHeight = new StyledTextField(10);
        txtHeight.setText(currentHeight > 0 ? String.valueOf((int) currentHeight) : "");
        txtHeight.setBounds(150, 45, 70, 30);
        frame.getContentPane().add(txtHeight);

        CardPanel card = new CardPanel();
        card.setBounds(20, 90, 410, 210);
        frame.getContentPane().add(card);

        JLabel lblBmiTitle = new JLabel("Your BMI");
        lblBmiTitle.setFont(UITheme.FONT_SUBTITLE);
        lblBmiTitle.setForeground(UITheme.TEXT_MAIN);
        lblBmiTitle.setBounds(15, 10, 200, 20);
        card.add(lblBmiTitle);

        lblBmiValue = new JLabel("-");
        lblBmiValue.setFont(new Font(UITheme.FONT_HEADER.getName(), Font.BOLD, 46));
        lblBmiValue.setForeground(UITheme.PRIMARY);
        lblBmiValue.setBounds(15, 35, 200, 65);
        card.add(lblBmiValue);

        lblBmiCategory = new JLabel("No data yet");
        lblBmiCategory.setFont(UITheme.FONT_SUBTITLE);
        lblBmiCategory.setBounds(15, 105, 250, 22);
        card.add(lblBmiCategory);

        lblWeightUsed = new JLabel("Weight: -");
        lblWeightUsed.setFont(UITheme.FONT_SMALL);
        lblWeightUsed.setForeground(UITheme.TEXT_MUTED);
        lblWeightUsed.setBounds(15, 133, 200, 18);
        card.add(lblWeightUsed);

        lblHeightUsed = new JLabel("Height: -");
        lblHeightUsed.setFont(UITheme.FONT_SMALL);
        lblHeightUsed.setForeground(UITheme.TEXT_MUTED);
        lblHeightUsed.setBounds(15, 153, 200, 18);
        card.add(lblHeightUsed);

        lblPrevBmi = new JLabel("Previous: -");
        lblPrevBmi.setFont(new Font(UITheme.FONT_SMALL.getName(), Font.ITALIC, UITheme.FONT_SMALL.getSize()));
        lblPrevBmi.setForeground(UITheme.TEXT_MUTED);
        lblPrevBmi.setBounds(15, 173, 340, 18);
        card.add(lblPrevBmi);

        BmiScale scale = new BmiScale();
        scale.setBounds(270, 10, 130, 190);
        card.add(scale);

        InfoLabel lblInfo = new InfoLabel("BMI = weight(kg) / height(m)^2");
        lblInfo.setBounds(20, 312, 300, 16);
        frame.getContentPane().add(lblInfo);

        RoundedButton btnRefresh = new RoundedButton("Refresh");
        btnRefresh.setBounds(20, 335, 100, 35);
        frame.getContentPane().add(btnRefresh);
        btnRefresh.addActionListener(e -> refreshBmi());

        frame.setVisible(true);
    }

    private void refreshBmi() {
        UserDB udb = new UserDB();
        User u = udb.getById(userId);
        if (u == null) return;

        double heightCm = u.getHeight();

        if (heightCm <= 0) {
            lblBmiCategory.setText("Enter your height above");
            lblBmiCategory.setForeground(UITheme.TEXT_MUTED);
            lblBmiValue.setText("-");
            return;
        }

        double latestWeight = 0;
        double prevWeight = 0;
        try {
            java.sql.Connection conn = database.ConnectionFactory.getConnection();
            java.sql.PreparedStatement ps = conn.prepareStatement(
                "SELECT WeightM FROM weights WHERE UserId=? AND WeightM > 0 ORDER BY Date DESC LIMIT 2");
            ps.setInt(1, userId);
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) latestWeight = rs.getDouble("WeightM");
            if (rs.next()) prevWeight = rs.getDouble("WeightM");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (latestWeight <= 0) {
            lblBmiCategory.setText("Log your weight first");
            lblBmiCategory.setForeground(UITheme.TEXT_MUTED);
            lblBmiValue.setText("-");
            lblHeightUsed.setText("Height: " + (int) heightCm + " cm");
            return;
        }

        double heightM = heightCm / 100.0;
        double bmi = latestWeight / (heightM * heightM);

        lblBmiValue.setText(String.format("%.1f", bmi));
        lblWeightUsed.setText("Weight: " + String.format("%.1f", latestWeight) + " kg");
        lblHeightUsed.setText("Height: " + (int) heightCm + " cm");

        String category;
        Color catColor;
        if (bmi < 18.5) {
            category = "Underweight";
            catColor = UITheme.PRIMARY;
        } else if (bmi < 25.0) {
            category = "Normal weight";
            catColor = UITheme.SUCCESS;
        } else if (bmi < 30.0) {
            category = "Overweight";
            catColor = new Color(243, 156, 18);
        } else {
            category = "Obese";
            catColor = UITheme.ERROR;
        }
        lblBmiCategory.setText(category);
        lblBmiCategory.setForeground(catColor);
        lblBmiValue.setForeground(catColor);

        if (prevWeight > 0) {
            double prevBmi = prevWeight / (heightM * heightM);
            double diff = bmi - prevBmi;
            String direction = diff > 0 ? "up" : (diff < 0 ? "down" : "same");
            lblPrevBmi.setText(String.format("Previous BMI: %.1f  (%s %.2f)", prevBmi, direction, Math.abs(diff)));
            lblPrevBmi.setForeground(diff < 0 && bmi >= 18.5 ? UITheme.SUCCESS : UITheme.TEXT_MUTED);
        } else {
            lblPrevBmi.setText("Previous: not enough data");
        }
    }

    static class BmiScale extends JPanel {
        
        public BmiScale() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = 22, x = 10;
            int totalH = getHeight() - 20;
            
            Color[] colors = {
                UITheme.ERROR,                  
                new Color(243, 156, 18),
                UITheme.SUCCESS,                
                UITheme.PRIMARY                 
            };
            String[] labels = {"Obese >=30", "Over 25", "Normal 18.5", "Under"};
            int bandH = totalH / 4;

            for (int i = 0; i < 4; i++) {
                g2.setColor(colors[i]);
                g2.fillRoundRect(x, i * bandH + 5, w, bandH - 2, 6, 6);
                g2.setColor(UITheme.TEXT_MAIN);
                g2.setFont(UITheme.FONT_SMALL);
                g2.drawString(labels[i], x + w + 8, i * bandH + 18 + (bandH/4));
            }
        }
    }
}