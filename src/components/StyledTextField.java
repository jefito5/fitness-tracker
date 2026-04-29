package components;

import theme.UITheme;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.border.CompoundBorder;

public class StyledTextField extends JTextField {

    public StyledTextField(int columns) {
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