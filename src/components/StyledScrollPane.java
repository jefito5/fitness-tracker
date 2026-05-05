package components;

import theme.UITheme;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import java.awt.Component;

public class StyledScrollPane extends JScrollPane {

    public StyledScrollPane(Component view) {
        super(view);
        
        setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
        getViewport().setBackground(UITheme.SURFACE);
    }
}