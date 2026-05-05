package components;

import theme.UITheme;
import javax.swing.JPasswordField;
import javax.swing.BorderFactory;
import javax.swing.border.CompoundBorder;

public class StyledPasswordField extends JPasswordField {

    public StyledPasswordField(int columns) {
        super(columns);
        setFont(UITheme.FONT_REGULAR);
        setForeground(UITheme.TEXT_MAIN);
        setBackground(UITheme.SURFACE);
        setCaretColor(UITheme.PRIMARY);
        
        
        setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER, 1),
            BorderFactory.createEmptyBorder(4, UITheme.PADDING_SMALL, 4, UITheme.PADDING_SMALL)
        ));
    }
}