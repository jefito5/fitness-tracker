package components;

import theme.UITheme;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class RoundedButton extends JButton {

    public RoundedButton(String text) {
        super(text);
        setFont(UITheme.FONT_REGULAR);
        setForeground(UITheme.ON_PRIMARY);
        setBackground(UITheme.PRIMARY);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(new EmptyBorder(UITheme.PADDING_SMALL, UITheme.PADDING_MEDIUM, UITheme.PADDING_SMALL, UITheme.PADDING_MEDIUM));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (getModel().isPressed()) {
            g2.setColor(getBackground().darker());
        } else {
            g2.setColor(getBackground());
        }
        
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), UITheme.RADIUS_MEDIUM, UITheme.RADIUS_MEDIUM);
        g2.dispose();
        super.paintComponent(g);
    }
}