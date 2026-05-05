package components;

import theme.UITheme;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class TagChip extends JLabel {

    public TagChip(String text) {
        super(text);
        setFont(UITheme.FONT_SMALL);
        setForeground(UITheme.PRIMARY);
        setHorizontalAlignment(CENTER);
        setOpaque(false);
        setBorder(new EmptyBorder(4, 10, 4, 10));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(new java.awt.Color(UITheme.PRIMARY.getRed(), UITheme.PRIMARY.getGreen(), UITheme.PRIMARY.getBlue(), 30));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), UITheme.RADIUS_LARGE, UITheme.RADIUS_LARGE);
        
        g2.dispose();
        super.paintComponent(g);
    }
}