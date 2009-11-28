/* ====================================================================
 *  Copyright 2004-2005 www.xmatrix.ch
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.jfactory.component.table;

import ch.jfactory.lang.ArrayUtils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 * Builds a SortedTable and encapsulates if necessary the given TableModel into a SortableTableModel.
 *
 * @author Daniel Frey
 * @version $Revision: 1.4 $ $Date: 2007/09/27 10:41:47 $
 */
public class SortedTable extends JTable
{
    public static final Direction DIRECTION_UP = new Direction( "up" );

    public static final Direction DIRECTION_DOWN = new Direction( "down" );

    private final TableModelListener revalidator = new TableModelListener()
    {
        public void tableChanged( final TableModelEvent e )
        {
            revalidate();
        }
    };

    private final Map<KeyStroke, KeyListener> upListenerMap = new HashMap<KeyStroke, KeyListener>();

    private final Map<KeyStroke, KeyListener> downListenerMap = new HashMap<KeyStroke, KeyListener>();

    private SortState sortState;

    public SortedTable()
    {
        this( new DefaultTableModel() );
    }

    public SortedTable( final TableModel model )
    {
        super( model instanceof SortableTableModel ? model : new SortableTableModel( model ) );
        final SortableTableModel sortableTableModel = (SortableTableModel) getModel();
        sortState = sortableTableModel.getSortState();
        final JTableHeader header = getTableHeader();
        header.setDefaultRenderer( new SortColumnRenderer( header.getDefaultRenderer() ) );
        header.addMouseListener( new HeaderListener() );
    }

    public void setModel( final TableModel model )
    {
        final TableModel old = getModel();
        if ( old != null )
        {
            old.removeTableModelListener( revalidator );
        }
        super.setModel( model instanceof SortableTableModel ? model : new SortableTableModel( model ) );
        model.addTableModelListener( revalidator );
        final SortableTableModel sortableTableModel = (SortableTableModel) getModel();
        sortState = sortableTableModel.getSortState();
    }

    /**
     * Register a key stroke for {@link #DIRECTION_UP} or {@link #DIRECTION_DOWN}.
     *
     * @param direction the direction for which to link the key stroke
     * @param stroke    the key stroke to link
     */
    public void addKeyStroke( final Direction direction, final KeyStroke stroke )
    {
        final KeyListener listener;
        if ( direction == DIRECTION_UP )
        {
            listener = new UpKeyListener( stroke );
            upListenerMap.put( stroke, listener );
            addKeyListener( listener );
        }
        else if ( direction == DIRECTION_DOWN )
        {
            listener = new DownKeyListener( stroke );
            downListenerMap.put( stroke, listener );
            addKeyListener( listener );
        }
    }

    public void removeKeyStroke( final SortState.State direction, final KeyStroke stroke )
    {
        assert direction != null : "direction argument may not be null";
        if ( direction == SortState.SORT_ASCENDING )
        {
            removeKeyListener( upListenerMap.get( stroke ) );
        }
        else if ( direction == SortState.SORT_DESCENDING )
        {
            removeKeyListener( downListenerMap.get( stroke ) );
        }
    }

    public boolean isAtTop()
    {
        final int[] rows = getSelectedRows();
        return ArrayUtils.contains( rows, 0 );
    }

    public boolean isAtBottom()
    {
        final int[] rows = getSelectedRows();
        return ArrayUtils.contains( rows, getRowCount() - 1 );
    }

    public SortableTableModel getSortableModel()
    {
        return (SortableTableModel) getModel();
    }

    private class HeaderListener extends MouseAdapter
    {
        public void mouseClicked( final MouseEvent e )
        {
            final JTableHeader header = getTableHeader();
            if ( header.getTable().isEditing() )
            {
                header.getTable().getCellEditor().stopCellEditing();
            }
            final int col = header.columnAtPoint( e.getPoint() );
            final int sortColumn = header.getTable().convertColumnIndexToModel( col );
            if ( e.isShiftDown() )
            {
                sortState.setDecreasedDirective( sortColumn );
            }
            else
            {
                sortState.setIncreasedDirective( sortColumn );
            }
        }
    }

    private class UpKeyListener extends KeyAdapter
    {
        private final KeyStroke stroke;

        public UpKeyListener( final KeyStroke stroke )
        {
            this.stroke = stroke;
        }

        public void keyPressed( final KeyEvent e )
        {
            if ( KeyStroke.getKeyStroke( e.getKeyCode(), e.getModifiers() ) == stroke )
            {
                final SortableTableModel sortableTableModel = (SortableTableModel) getModel();
                final int[] selected = sortableTableModel.moveUp( getSelectedRows() );
                TableUtils.setSelection( selected, getSelectionModel() );
            }
        }
    }

    private class DownKeyListener extends KeyAdapter
    {
        private final KeyStroke stroke;

        public DownKeyListener( final KeyStroke stroke )
        {
            this.stroke = stroke;
        }

        public void keyPressed( final KeyEvent e )
        {
            if ( KeyStroke.getKeyStroke( e.getKeyCode(), e.getModifiers() ) == stroke )
            {
                final SortableTableModel searchableModel = (SortableTableModel) getModel();
                final int[] selected = searchableModel.moveDown( getSelectedRows() );
                TableUtils.setSelection( selected, getSelectionModel() );
            }
        }
    }

    /**
     * @author Nobuo Tamemasa, $Author: daniel_frey $
     * @version $Revision: 1.4 $ $Date: 2007/09/27 10:41:47 $
     */
    private class SortColumnRenderer extends JLabel implements TableCellRenderer
    {
        private final TableCellRenderer tableCellRenderer;

        public SortColumnRenderer( final TableCellRenderer tableCellRenderer )
        {
            this.tableCellRenderer = tableCellRenderer;
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

    }

    public static class Direction
    {
        private final String direction;

        public Direction( final String direction )
        {
            this.direction = direction;
        }

        public String toString()
        {
            return direction;
        }
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
