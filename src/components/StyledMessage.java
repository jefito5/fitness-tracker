package components;

import theme.UITheme;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class StyledMessage {

    
    public static void show(String title, String message) {
        JDialog dialog = new JDialog();
        dialog.setTitle(title);
        dialog.setModal(true);
        dialog.setSize(350, 180);
        dialog.setLocationRelativeTo(null);
        dialog.getContentPane().setBackground(UITheme.SURFACE);
        dialog.setLayout(new BorderLayout());

        
        JLabel lblMessage = new JLabel(message, SwingConstants.CENTER);
        lblMessage.setFont(UITheme.FONT_REGULAR);
        lblMessage.setForeground(UITheme.TEXT_MAIN);
        lblMessage.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        dialog.add(lblMessage, BorderLayout.CENTER);

        
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(UITheme.SURFACE);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        
        RoundedButton btnOk = new RoundedButton("OK");
        btnOk.setPreferredSize(new Dimension(100, 35));
        btnOk.addActionListener(e -> dialog.dispose());
        btnPanel.add(btnOk);
        
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}