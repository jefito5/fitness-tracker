package components;

import theme.UITheme;
import javax.swing.JLabel;
import java.awt.Font;

public class InfoLabel extends JLabel {

    public InfoLabel(String text) {
        super(text);
        setFont(new Font(UITheme.FONT_SMALL.getName(), Font.ITALIC, UITheme.FONT_SMALL.getSize()));
        setForeground(UITheme.TEXT_MUTED);
    }
}