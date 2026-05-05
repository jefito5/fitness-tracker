package gui.components;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JTextField;
import javax.swing.border.AbstractBorder;

/**
 * Text field with a rounded border, soft padding, focus highlight, and
 * an optional placeholder string shown when empty &amp; unfocused.
 *
 * Part of the UI redesign component library (Jira: UI-2).
 */
public class StyledTextField extends JTextField {

    private boolean focused = false;
    private String placeholder;

    public StyledTextField() {
        this("");
    }

    public StyledTextField(String placeholder) {
        super();
        this.placeholder = placeholder == null ? "" : placeholder;
        setFont(UITheme.FONT_BODY);
        setForeground(UITheme.TEXT_PRIMARY);
        setBackground(UITheme.SURFACE);
        setOpaque(false);
        setBorder(new RoundedFieldBorder());
        setCaretColor(UITheme.PRIMARY);

        addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { focused = true;  repaint(); }
            @Override public void focusLost  (FocusEvent e) { focused = false; repaint(); }
        });
    }

    public void setPlaceholder(String p) {
        this.placeholder = p == null ? "" : p;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), UITheme.RADIUS_SM, UITheme.RADIUS_SM);
        g2.dispose();
        super.paintComponent(g);

        if (getText().isEmpty() && !focused && !placeholder.isEmpty()) {
            Graphics2D g3 = (Graphics2D) g.create();
            g3.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g3.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g3.setColor(UITheme.TEXT_MUTED);
            g3.setFont(getFont());
            Insets ins = getInsets();
            FontMetrics fm = g3.getFontMetrics();
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g3.drawString(placeholder, ins.left, y);
            g3.dispose();
        }
    }

    private class RoundedFieldBorder extends AbstractBorder {
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(focused ? UITheme.BORDER_FOCUS : UITheme.BORDER);
            g2.setStroke(new BasicStroke(focused ? 1.6f : 1.0f));
            g2.drawRoundRect(x, y, w - 1, h - 1, UITheme.RADIUS_SM, UITheme.RADIUS_SM);
            g2.dispose();
        }

        @Override public Insets getBorderInsets(Component c) {
            return new Insets(8, 12, 8, 12);
        }
    }
}
