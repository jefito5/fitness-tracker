package components;

import theme.UITheme;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;

public class StyledComboBox<E> extends JComboBox<E> {

    public StyledComboBox(E[] items) {
        super(items);
        setFont(UITheme.FONT_REGULAR);
        setForeground(UITheme.TEXT_MAIN);
        setBackground(UITheme.SURFACE);
        setFocusable(false);

        setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER, 1),
            BorderFactory.createEmptyBorder(2, 4, 2, 4)
        ));

        setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton("▼");
                button.setFont(new Font("SansSerif", Font.PLAIN, 10));
                button.setBackground(UITheme.SURFACE);
                button.setForeground(UITheme.TEXT_MUTED);
                button.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 8));
                button.setFocusPainted(false);
                button.setContentAreaFilled(false);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                return button;
            }

            @Override
            protected ComboPopup createPopup() {
                BasicComboPopup popup = new BasicComboPopup(comboBox) {
                    @Override
                    protected JScrollPane createScroller() {
                        JScrollPane sp = new JScrollPane(list,
                                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                        
                        sp.getVerticalScrollBar().setUI(new ModernScrollBarUI());
                        sp.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0)); 
                        
                        sp.setBorder(BorderFactory.createEmptyBorder());
                        return sp;
                    }
                };
                popup.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1)); 
                return popup;
            }
        });

        setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                label.setBorder(new EmptyBorder(6, 8, 6, 8)); 
                
                if (isSelected) {
                    label.setBackground(UITheme.PRIMARY);
                    label.setForeground(UITheme.ON_PRIMARY);
                } else {
                    label.setBackground(UITheme.SURFACE);
                    label.setForeground(UITheme.TEXT_MAIN);
                }
                return label;
            }
        });
    }

    private static class ModernScrollBarUI extends BasicScrollBarUI {
        
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = Color.LIGHT_GRAY;
            this.trackColor = UITheme.SURFACE; 
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }


        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            g.setColor(trackColor);
            g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) return;
            
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumbColor);
            
            
            g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2, thumbBounds.width - 4, thumbBounds.height - 4, 8, 8);
            g2.dispose();
        }
    }
}