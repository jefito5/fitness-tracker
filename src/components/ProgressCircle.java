package components;

import theme.UITheme;
import javax.swing.JPanel;
import java.awt.*;

public class ProgressCircle extends JPanel {

    private int percentage = 0;
    private Color arcColor;

    public ProgressCircle(Color arcColor) {
        this.arcColor = arcColor;
        setOpaque(false);
        setPreferredSize(new Dimension(120, 120));
    }

    public void setPercentage(int percentage) {
        this.percentage = Math.max(0, Math.min(100, percentage));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int strokeWidth = 14;
        int size = Math.min(getWidth(), getHeight()) - strokeWidth;
        int x = (getWidth() - size) / 2;
        int y = (getHeight() - size) / 2;

        g2.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(UITheme.BORDER);
        g2.drawArc(x, y, size, size, 0, 360);

        g2.setColor(arcColor);
        int angle = (int) (360 * (percentage / 100.0));
        g2.drawArc(x, y, size, size, 90, -angle);

        g2.setColor(UITheme.TEXT_MAIN);
        g2.setFont(UITheme.FONT_HEADER);
        String text = percentage + "%";
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();
        g2.drawString(text, (getWidth() - textWidth) / 2, (getHeight() + textHeight) / 2 - 5);

        g2.dispose();
    }
}