package ch.jfactory.component.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * @author Nobuo Tamemasa, $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:54:58 $
 */
public class SortColumnRenderer extends JLabel implements TableCellRenderer
{
    private final TableCellRenderer tableCellRenderer;

    private final SortState sortState;

    public SortColumnRenderer( final TableCellRenderer tableCellRenderer, final SortState sortState )
    {
        this.tableCellRenderer = tableCellRenderer;
        this.sortState = sortState;
    }

    public Component getTableCellRendererComponent( final JTable table, final Object value, final boolean isSelected,
                                                    final boolean hasFocus, final int row, final int column )
    {
        final Component c = tableCellRenderer.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
        if ( c instanceof JLabel )
        {
            final JLabel l = (JLabel) c;
            final int modelColumn = table.convertColumnIndexToModel( column );
            l.setHorizontalTextPosition( JLabel.LEFT );
            l.setIcon( getHeaderRendererIcon( modelColumn, l.getFont().getSize() ) );
        }
        return c;
    }

    protected Icon getHeaderRendererIcon( final int column, final int size )
    {
        final Arrow icon;
        if ( sortState.getColumn() == column && sortState.getState() != SortState.SORT_NONE )
        {
            icon = new Arrow( sortState.getState() == SortState.SORT_DESCENDING, size, 0 );
            return icon;
        }
        else
        {
            icon = null;
        }
        return icon;
    }

    private static class Arrow implements Icon
    {
        private final boolean descending;

        private final int size;

        private final int priority;

        public Arrow( final boolean descending, final int size, final int priority )
        {
            this.descending = descending;
            this.size = size;
            this.priority = priority;
        }

        public void paintIcon( final Component c, final Graphics g, final int x, int y )
        {
            final Color color = c == null ? Color.GRAY : c.getBackground();
            // In a compound sort, make each succesive triangle 20%
            // smaller than the previous one.
            final int dx = (int) ( size / 2 * Math.pow( 0.8, priority ) );
            final int dy = descending ? dx : -dx;
            // Align icon (roughly) with font baseline.
            y = y + 5 * size / 6 + ( descending ? -dy : 0 );
            final int shift = descending ? 1 : -1;
            g.translate( x, y );

            // Right diagonal.
            g.setColor( color.darker() );
            g.drawLine( dx / 2, dy, 0, 0 );
            g.drawLine( dx / 2, dy + shift, 0, shift );

            // Left diagonal.
            g.setColor( color.brighter() );
            g.drawLine( dx / 2, dy, dx, 0 );
            g.drawLine( dx / 2, dy + shift, dx, shift );

            // Horizontal line.
            if ( descending )
            {
                g.setColor( color.darker().darker() );
            }
            else
            {
                g.setColor( color.brighter().brighter() );
            }
            g.drawLine( dx, 0, 0, 0 );

            g.setColor( color );
            g.translate( -x, -y );
        }

        public int getIconWidth()
        {
            return size;
        }

        public int getIconHeight()
        {
            return size;
        }
    }
}



