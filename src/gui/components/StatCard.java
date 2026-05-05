package gui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

/**
 * Card showing a single metric: label + large value + optional caption.
 *
 * Part of the UI redesign component library (Jira: UI-2).
 */
public class StatCard extends CardPanel {

    private final JLabel labelLbl;
    private final JLabel valueLbl;
    private final JLabel captionLbl;

    public StatCard(String label) {
        super(true);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(UITheme.padding(UITheme.SPACE_LG));

        labelLbl = new JLabel(label == null ? "" : label.toUpperCase());
        labelLbl.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 11));
        labelLbl.setForeground(UITheme.TEXT_SECONDARY);
        labelLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        valueLbl = new JLabel("—");
        valueLbl.setFont(UITheme.FONT_STAT_VALUE);
        valueLbl.setForeground(UITheme.TEXT_PRIMARY);
        valueLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        valueLbl.setBorder(UITheme.padding(UITheme.SPACE_XS, 0, 0, 0));

        captionLbl = new JLabel(" ");
        captionLbl.setFont(UITheme.FONT_CAPTION);
        captionLbl.setForeground(UITheme.TEXT_MUTED);
        captionLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        captionLbl.setBorder(UITheme.padding(UITheme.SPACE_XS, 0, 0, 0));

        add(labelLbl);
        add(valueLbl);
        add(captionLbl);
    }

    public void setValue(String v) {
        valueLbl.setText(v == null ? "—" : v);
    }

    public void setValueColor(Color c) {
        valueLbl.setForeground(c);
    }

    public void setCaption(String c) {
        captionLbl.setText(c == null || c.isEmpty() ? " " : c);
    }

    public void setCaptionColor(Color c) {
        captionLbl.setForeground(c);
    }
}
