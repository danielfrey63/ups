package net.java.jveez.ui.directory;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import net.java.jveez.vfs.Directory;

public class DirectoryListCellRenderer extends DefaultListCellRenderer {

    private Color normalColor;
    private Color hiddenColor;

    public DirectoryListCellRenderer() {
        normalColor = getForeground();
        hiddenColor = Color.BLUE;
        setOpaque(false);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Directory) {
            Directory directory = (Directory) value;
            label.setIcon(directory.getIcon());
            label.setText(directory.getName());
            label.setForeground(directory.isHidden() ? hiddenColor : normalColor);
        }
        else {
            label.setText("[NULL]");
        }

        return label;
    }
}
