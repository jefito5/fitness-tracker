package components;

import theme.UITheme;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.Dimension;

public class StyledTable extends JTable {

    public StyledTable(DefaultTableModel model) {
        super(model);
        setFont(UITheme.FONT_REGULAR);
        setForeground(UITheme.TEXT_MAIN);
        setRowHeight(30);
        
        setGridColor(UITheme.BORDER);
        setShowVerticalLines(false);

        setSelectionBackground(UITheme.PRIMARY);
        setSelectionForeground(UITheme.ON_PRIMARY);

        JTableHeader header = getTableHeader();
        header.setFont(UITheme.FONT_SUBTITLE);
        header.setBackground(UITheme.SURFACE);
        header.setForeground(UITheme.TEXT_MAIN);
        header.setPreferredSize(new Dimension(100, 35));
    }
}
