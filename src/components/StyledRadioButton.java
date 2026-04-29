package components;

import theme.UITheme;
import javax.swing.JRadioButton;
import javax.swing.Icon;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Cursor;

public class StyledRadioButton extends JRadioButton {

    public StyledRadioButton(String text) {
        super(text);
        setFont(UITheme.FONT_REGULAR);
        setForeground(UITheme.TEXT_MAIN);
        setOpaque(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        setIcon(new CustomRadioIcon(false));
        setSelectedIcon(new CustomRadioIcon(true));
    }

    
    private class CustomRadioIcon implements Icon {
        private boolean isSelected;
        private int size = 20;

        public CustomRadioIcon(boolean isSelected) {
            this.isSelected = isSelected;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

            g2.setStroke(new java.awt.BasicStroke(1.5f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND));

            double centerX = x + size / 2.0;
            double centerY = y + size / 2.0; 

            double outerRadius = 7.0;
            double innerRadius = 3.5; 

            java.awt.geom.Ellipse2D.Double outerRing = new java.awt.geom.Ellipse2D.Double(
                    centerX - outerRadius, centerY - outerRadius, outerRadius * 2, outerRadius * 2
            );

            if (isSelected) {
                g2.setColor(UITheme.PRIMARY);
            } else {
                g2.setColor(UITheme.BORDER);
            }
            g2.draw(outerRing); 

            if (isSelected) {
                java.awt.geom.Ellipse2D.Double innerDot = new java.awt.geom.Ellipse2D.Double(
                        centerX - innerRadius, centerY - innerRadius, innerRadius * 2, innerRadius * 2
                );
                g2.fill(innerDot); 
            }

            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size; 
        }
    }
}