package gui.components;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 * Elevated container: white surface, rounded corners, subtle border &amp; soft shadow.
 *
 * Part of the UI redesign component library (Jira: UI-2).
 */
public class CardPanel extends JPanel {

    private final int radius;
    private final boolean shadow;

    public CardPanel() {
        this(true);
    }

    public CardPanel(boolean shadow) {
        this(shadow, UITheme.RADIUS_LG);
    }

    public CardPanel(boolean shadow, int radius) {
        this.shadow = shadow;
        this.radius = radius;
        setOpaque(false);
        setBackground(UITheme.SURFACE);
        setBorder(UITheme.padding(UITheme.SPACE_LG));
        setLayout(new BorderLayout());
    }

    @Override
    public void setLayout(LayoutManager mgr) {
        super.setLayout(mgr);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        int shadowOffset = shadow ? 4 : 0;

        if (shadow) {
            for (int i = 0; i < shadowOffset; i++) {
                int alpha = Math.max(0, 10 - i * 2);
                g2.setColor(new Color(15, 23, 42, alpha));
                g2.fillRoundRect(i, i + 1, w - i * 2, h - i * 2 - 2, radius, radius);
            }
        }

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, w - shadowOffset, h - shadowOffset, radius, radius);

        g2.setColor(UITheme.BORDER);
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(0, 0, w - shadowOffset - 1, h - shadowOffset - 1, radius, radius);
        g2.dispose();
        super.paintComponent(g);
    }
}
