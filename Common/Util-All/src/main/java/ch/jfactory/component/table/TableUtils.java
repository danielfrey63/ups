/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.component.table;

import ch.jfactory.color.ColorUtils;
import ch.jfactory.lang.ArrayUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import mseries.Calendar.MDateChanger;
import mseries.Calendar.MDefaultPullDownConstraints;
import mseries.ui.MDateEntryField;
import org.apache.commons.collections.keyvalue.MultiKey;
import org.apache.commons.lang.RandomStringUtils;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.4 $ $Date: 2007/09/27 10:41:47 $
 */
public class TableUtils
{
    /**
     * Returns the selection of the table selection model as an array of ints.
     *
     * @param selectionModel the selection model of the table
     * @return an array of ints
     */
    public static int[] getSelection( final ListSelectionModel selectionModel )
    {
        int[] ints = new int[0];
        for ( int i = selectionModel.getMinSelectionIndex(); i <= selectionModel.getMaxSelectionIndex(); i++ )
        {
            if ( selectionModel.isSelectedIndex( i ) )
            {
                ints = ArrayUtils.insert( ints, i, ints.length );
            }
        }
        return ints;
    }

    /**
     * Set selection to a given array of indices.
     *
     * @param ints           array of indices.
     * @param selectionModel the selection model
     */
    public static void setSelection( final int[] ints, final ListSelectionModel selectionModel )
    {
        for ( int i = 0; i < ints.length; i++ )
        {
            final int pos = ints[i];
            if ( i == 0 )
            {
                selectionModel.setSelectionInterval( pos, pos );
            }
            else
            {
                selectionModel.addSelectionInterval( pos, pos );
            }
        }
    }

    /**
     * Returns an identifiable collection of selections for later reuse to restore selection using {@link #setSelection(Map, JTable)}.
     *
     * @param table
     * @return a map for
     */
    public static Map<MultiKey, Integer> getSelection( final JTable table )
    {
        final int[] selection = getSelection( table.getSelectionModel() );
        final Map<MultiKey, Integer> result = new HashMap<MultiKey, Integer>();
        for ( final int r : selection )
        {
            result.put( getMultiKey( table, r ), r );
        }
        return result;
    }

    /**
     * Adds the specified saved selection to the table selections.
     *
     * @param selection the selections to add
     * @param table     the table to add the selections to
     */
    public static void setSelection( final Map<MultiKey, Integer> selection, final JTable table )
    {
        final Map<MultiKey, Integer> rows = new HashMap<MultiKey, Integer>();
        for ( int r = 0; r < table.getRowCount(); r++ )
        {
            rows.put( getMultiKey( table, r ), r );
        }
        table.clearSelection();
        final Collection<MultiKey> selectionKeys = selection.keySet();
        for ( final MultiKey key : selectionKeys )
        {
            if ( rows.containsKey( key ) )
            {
                final int row = rows.get( key );
                table.addRowSelectionInterval( row, row );
            }
        }
    }

    private static MultiKey getMultiKey( final JTable table, final int r )
    {
        final int columns = table.getColumnCount();
        final Object[] keys = new Object[columns];
        for ( int c = 0; c < columns; c++ )
        {
            keys[c] = table.getValueAt( r, c );
        }
        return new MultiKey( keys );
    }

    /** Note: Make sure to override TableModel#getColumnClass to use the renderers. */
    public static class CalendarCellRenderer extends DefaultTableCellRenderer
    {
        private final String pattern;

        public CalendarCellRenderer( final String pattern )
        {
            this.pattern = pattern;
        }

        public Component getTableCellRendererComponent( final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column )
        {
            final JLabel label = (JLabel) super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
            final Calendar cal;
            if ( value instanceof Date )
            {
                cal = Calendar.getInstance();
                cal.setTime( (Date) value );
            }
            else if ( value instanceof Calendar )
            {
                cal = (Calendar) value;
            }
            else
            {
                label.setText( "" );
                return label;
//                throw new IllegalArgumentException("value (" + (value == null ? "null" : value.getClass().getName()) + ") not a Calendar or Date");
            }
            final Format format = new SimpleDateFormat( pattern );
            label.setText( cal == null ? "0" : format.format( cal.getTime() ) );
            return label;
        }
    }

    /** Note: Make sure to override TableModel#getColumnClass to use the renderers. */
    public static class BooleanCellRenderer extends DefaultTableCellRenderer
    {
        private final Icon trueIcon;

        private final Icon falseIcon;

        public BooleanCellRenderer( final Icon trueIcon, final Icon falseIcon )
        {
            this.trueIcon = trueIcon;
            this.falseIcon = falseIcon;
        }

        public Component getTableCellRendererComponent( final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column )
        {
            final JLabel label = (JLabel) super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
            final Boolean bool = (Boolean) value;
            label.setText( "" );
            if ( bool )
            {
                label.setIcon( trueIcon );
            }
            else
            {
                label.setIcon( falseIcon );
            }
            return label;
        }
    }

    public static class StringEditor extends AbstractCellEditor implements TableCellEditor
    {
        public StringEditor( final Color background )
        {
            super( background );
        }

        public StringEditor()
        {
        }

        public Object getCellEditorValue()
        {
            return editor.getText();
        }

        public Component getTableCellEditorComponent( final JTable table, final Object value, final boolean isSelected, final int row, final int column )
        {
            final String string = (String) value;
            editor.setText( string );
            return editor;
        }

        public boolean isCellEditable( final EventObject evt )
        {
            if ( evt instanceof MouseEvent )
            {
                return ( (MouseEvent) evt ).getClickCount() >= 2;
            }
            return true;
        }
    }

    public static class CalendarEditor extends AbstractCellEditor implements TableCellEditor
    {
        private final MDateEntryField editor = new MDateEntryField( DateFormat.getDateInstance( DateFormat.MEDIUM ) );

        public CalendarEditor()
        {
            this( ColorUtils.fade( Color.red, 0.9 ) );
        }

        public CalendarEditor( final Color background )
        {
            final MDefaultPullDownConstraints c = new MDefaultPullDownConstraints();
            c.firstDay = Calendar.MONDAY;
            c.changerStyle = MDateChanger.BUTTON;
            editor.setConstraints( c );
            editor.setBorder( new EmptyBorder( 0, 1, 0, 1 ) );
            editor.getDisplay().setBackground( background );
        }

        public Object getCellEditorValue()
        {
            return editor.getText();
        }

        public Component getTableCellEditorComponent( final JTable table, final Object value, final boolean isSelected, final int row, final int column )
        {
            try
            {
                final Calendar calendar = (Calendar) value;
                if ( calendar == null )
                {
                    editor.setValue( null );
                }
                else
                {
                    editor.setValue( calendar.getTime() );
                }
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
            return editor;
        }

        public boolean isCellEditable( final EventObject evt )
        {
            if ( evt instanceof MouseEvent )
            {
                return ( (MouseEvent) evt ).getClickCount() >= 2;
            }
            return true;
        }
    }

    public static void main( final String[] args )
    {
        final int size = 100;
        final Object[][] data = new Object[size][];
        for ( int i = 0; i < size; i++ )
        {
            data[i] = new String[]{
                    RandomStringUtils.randomAlphabetic( 5 ),
                    RandomStringUtils.randomAlphabetic( 5 ),
                    RandomStringUtils.randomAlphabetic( 5 )
            };
        }
        final DefaultTableModel model = new DefaultTableModel(
                data,
                new String[]{"a", "b", "c"}
        );
        final JTable table = new JTable( model );
        final JButton button = new JButton( "Selection" );
        button.addActionListener( new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                final Map<MultiKey, Integer> selection = getSelection( table );
                table.clearSelection();
                setSelection( selection, table );
            }
        } );
        final JFrame f = new JFrame();
        f.add( new JScrollPane( table ) );
        f.add( button, BorderLayout.SOUTH );
        f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        f.setSize( 200, 200 );
        f.setVisible( true );
    }
}
