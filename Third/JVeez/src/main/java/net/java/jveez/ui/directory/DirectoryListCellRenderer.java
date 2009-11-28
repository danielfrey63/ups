package net.java.jveez.ui.directory;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import net.java.jveez.vfs.Directory;

public class DirectoryListCellRenderer extends DefaultListCellRenderer
{
    private final Color normalColor;

    private final Color hiddenColor;

    public DirectoryListCellRenderer()
    {
        normalColor = getForeground();
        hiddenColor = Color.BLUE;
        setOpaque( false );
    }

    public Component getListCellRendererComponent( final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus )
    {
        final JLabel label = (JLabel) super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
        if ( value instanceof Directory )
        {
            final Directory directory = (Directory) value;
            label.setIcon( directory.getIcon() );
            label.setText( directory.getName() );
            label.setForeground( directory.isHidden() ? hiddenColor : normalColor );
        }
        else
        {
            label.setText( "[NULL]" );
        }

        return label;
    }
}
