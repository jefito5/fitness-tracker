package gui.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

/**
 * Styled button with rounded corners, hover &amp; press states, and three variants:
 * PRIMARY (filled), SECONDARY (filled, neutral), and OUTLINE (stroked, primary text).
 *
 * Part of the UI redesign component library (Jira: UI-2).
 */
public class RoundedButton extends JButton {

    public enum Variant { PRIMARY, SECONDARY, OUTLINE }

    private Variant variant;
    private boolean hover   = false;
    private boolean pressed = false;

    public RoundedButton(String text) {
        this(text, Variant.PRIMARY);
    }

    public RoundedButton(String text, Variant variant) {
        super(text);
        this.variant = variant;
        setFont(UITheme.FONT_BUTTON);
        setBorder(UITheme.padding(10, 18));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setForeground(textColour());

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { hover = true;  repaint(); }
            @Override public void mouseExited(MouseEvent e)  { hover = false; pressed = false; repaint(); }
            @Override public void mousePressed(MouseEvent e) { pressed = true;  repaint(); }
            @Override public void mouseReleased(MouseEvent e){ pressed = false; repaint(); }
        });
    }

    /** Switch variant at runtime (e.g. for active/inactive filter chips). */
    public void setVariant(Variant v) {
        this.variant = v;
        setForeground(textColour());
        repaint();
    }

    public Variant getVariant() { return variant; }

    private Color textColour() {
        switch (variant) {
            case OUTLINE:   return UITheme.PRIMARY;
            case SECONDARY: return UITheme.ON_PRIMARY;
            case PRIMARY:
            default:        return UITheme.ON_PRIMARY;
        }
    }

    private Color bgColour() {
        switch (variant) {
            case OUTLINE:
                if (pressed) return new Color(UITheme.PRIMARY.getRed(), UITheme.PRIMARY.getGreen(), UITheme.PRIMARY.getBlue(), 60);
                if (hover)   return new Color(UITheme.PRIMARY.getRed(), UITheme.PRIMARY.getGreen(), UITheme.PRIMARY.getBlue(), 30);
                return new Color(0, 0, 0, 0);
            case SECONDARY:
                return (hover || pressed) ? UITheme.SECONDARY_HOVER : UITheme.SECONDARY;
            case PRIMARY:
            default:
                if (pressed) return UITheme.PRIMARY_PRESSED;
                if (hover)   return UITheme.PRIMARY_HOVER;
                return UITheme.PRIMARY;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int r = UITheme.RADIUS_MD;
        g2.setColor(bgColour());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), r, r);
        if (variant == Variant.OUTLINE) {
            g2.setColor(UITheme.PRIMARY);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, r, r);
        }
        g2.dispose();
        super.paintComponent(g);
    }
}
