package components;

import theme.UITheme;
import javax.swing.JLabel;

public class SectionHeader extends JLabel {

    public SectionHeader(String text) {
        super(text);
        setFont(UITheme.FONT_HEADER);
        setForeground(UITheme.TEXT_MAIN);
    }
}