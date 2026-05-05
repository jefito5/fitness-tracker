package components;

import theme.UITheme;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class CardPanel extends JPanel {

    public CardPanel() {
        setOpaque(false);
        setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(UITheme.SURFACE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), UITheme.RADIUS_LARGE, UITheme.RADIUS_LARGE);
        
        g2.setColor(UITheme.BORDER);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, UITheme.RADIUS_LARGE, UITheme.RADIUS_LARGE);
        
        g2.dispose();
        super.paintComponent(g);
    }
}