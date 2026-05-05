package gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * Compact pill-shaped label for tags (workout type, muscle group, preset/custom flag, etc.).
 *
 * Part of the UI redesign component library (Jira: UI-2).
 */
public class TagChip extends JLabel {

    private final Color bg;

    public TagChip(String text, Color bg, Color fg) {
        super(text == null ? "" : text);
        this.bg = bg;
        setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 11));
        setForeground(fg);
        setOpaque(false);
        setBorder(UITheme.padding(3, 10));
        setHorizontalAlignment(SwingConstants.CENTER);
    }

    public static TagChip cardio()   { return new TagChip("CARDIO",   UITheme.TAG_CARDIO_BG,   UITheme.TAG_CARDIO_FG); }
    public static TagChip strength() { return new TagChip("STRENGTH", UITheme.TAG_STRENGTH_BG, UITheme.TAG_STRENGTH_FG); }
    public static TagChip preset()   { return new TagChip("PRESET",   UITheme.TAG_PRESET_BG,   UITheme.TAG_PRESET_FG); }
    public static TagChip custom()   { return new TagChip("CUSTOM",   UITheme.TAG_CUSTOM_BG,   UITheme.TAG_CUSTOM_FG); }

    /** Convenience: chip for a muscle group (capitalised display). Returns null for blank input. */
    public static TagChip muscle(String muscleGroup) {
        if (muscleGroup == null || muscleGroup.isBlank()) return null;
        String label = muscleGroup.substring(0, 1).toUpperCase() + muscleGroup.substring(1).toLowerCase();
        return new TagChip(label, UITheme.TAG_MUSCLE_BG, UITheme.TAG_MUSCLE_FG);
    }

    public static TagChip neutral(String text) {
        return new TagChip(text, UITheme.TAG_NEUTRAL_BG, UITheme.TAG_NEUTRAL_FG);
    }

    /** Chip styled by workout-type string ("Cardio" / "Strength"). */
    public static TagChip forType(String workoutType) {
        if (workoutType != null && workoutType.equalsIgnoreCase("Strength")) return strength();
        return cardio();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(bg);
        int r = getHeight();
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), r, r);
        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        return new Dimension(d.width, Math.max(d.height, 22));
    }
}
