package components;

import theme.UITheme;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;

public class StatCard extends CardPanel {

    private JLabel lblTitle;
    private JLabel lblValue;

    public StatCard(String title, String value) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(UITheme.PADDING_MEDIUM, UITheme.PADDING_MEDIUM, UITheme.PADDING_MEDIUM, UITheme.PADDING_MEDIUM));

        lblTitle = new JLabel(title);
        lblTitle.setFont(UITheme.FONT_REGULAR);
        lblTitle.setForeground(UITheme.TEXT_MUTED);
        lblTitle.setAlignmentX(CENTER_ALIGNMENT);

        lblValue = new JLabel(value);
        lblValue.setFont(UITheme.FONT_HEADER);
        lblValue.setForeground(UITheme.PRIMARY);
        lblValue.setAlignmentX(CENTER_ALIGNMENT);

        add(lblTitle);
        add(javax.swing.Box.createVerticalStrut(UITheme.PADDING_SMALL));
        add(lblValue);
    }

    public void setValue(String value) {
        lblValue.setText(value);
    }
}