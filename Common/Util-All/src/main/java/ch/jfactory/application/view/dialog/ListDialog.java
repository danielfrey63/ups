/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.application.view.dialog;

import ch.jfactory.application.presentation.WindowUtils;
import ch.jfactory.component.list.DefaultJList;
import ch.jfactory.resource.Strings;
import com.jidesoft.swing.SearchableUtils;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple dialog that displays a text and a list.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.11 $ $Date: 2007/09/27 10:41:35 $
 */
public class ListDialog<T> extends I15nComponentDialog implements ListSelectionListener, DocumentListener
{
    private static final Logger LOGGER = LoggerFactory.getLogger( ListDialog.class );

    private T[] selectedData;

    private T[] allData;

    private DefaultJList<T> list;

    /**
     * Constructs a new list dialog based as a child of the dialog given. A title is displayed, for which the string is taken from a string resource {@link Strings} with a key like <code>PREFIX.TITLE</code>, where <code>PREFIX</code> is the prefix argument given. A text multi-line label is displayed above the list where the text is retrieved by the key <code>PREFIX.TEXT</code>.
     *
     * @param parent   the parent dialog to center this dialog on
     * @param prefix   the key prefix to use
     * @param listData the data to put into the list
     */
    public ListDialog( final Dialog parent, final String prefix, final T[] listData )
    {
        super( parent, prefix );
        init( listData );
    }

    /**
     * Constructs a new list dialog based as a child of the frame given. A title is displayed, for which the string is taken from a string resource {@link Strings} with a key like <code>PREFIX.TITLE</code>, where <code>PREFIX</code> is the prefix argument given. A text multi-line label is displayed above the list where the text is retrieved by the key <code>PREFIX.TEXT</code>.
     *
     * @param parent   the parent dialog to center this dialog on
     * @param prefix   the key prefix to use
     * @param listData the data to put into the list
     */
    public ListDialog( final Frame parent, final String prefix, final T[] listData )
    {
        super( parent, prefix );
        init( listData );
    }

    private void init( final T[] listData )
    {
        // Make sure to work on a copy of the original data
        allData = copyIntoNewArray( listData, listData.length );
        list.setListData( allData );
        list.requestFocus();
    }

    @SuppressWarnings("unchecked")
    private T[] copyIntoNewArray( final T[] listData, final int length )
    {
        final List<T> temp = Arrays.asList( listData );
        return new ArrayList<T>( temp ).toArray( (T[]) Array.newInstance( temp.iterator().next().getClass(), length ) );
    }

    /**
     * Returns the data selected. The type of the object array is <code>Object[]</code>.
     *
     * @return the data selected upon close of the dialog, if the apply button was pressed, otherwise an empty array.
     */
    public T[] getSelectedData()
    {
        return selectedData;
    }

    /**
     * Returns the data selected. The type of the object array is the type given.
     *
     * @param type the type of the array to which the data should be copied
     * @return the data selected upon close of the dialog, if the apply button was pressed, otherwise an empty array.
     */
    public T[] getSelectedData( final T[] type )
    {
        return new ArrayList<T>( Arrays.asList( selectedData ) ).toArray( type );
    }

    /**
     * Sets the renderer for the list displayed.
     *
     * @param renderer the ListCellRenderer
     */
    public void setListCellRenderer( final ListCellRenderer<? super T> renderer )
    {
        list.setCellRenderer( renderer );
    }

    /**
     * Set which selection mode you want.
     *
     * @param selectionMode selection mode from {@link ListSelectionModel} might be {@link ListSelectionModel#SINGLE_SELECTION} or {@link ListSelectionModel#SINGLE_INTERVAL_SELECTION} or {@link ListSelectionModel#MULTIPLE_INTERVAL_SELECTION}
     */
    public void setSelectionMode( final int selectionMode )
    {
        list.setSelectionMode( selectionMode );
    }

    /**
     * Sets the preselected data.
     *
     * @param selected the objects to select in the list
     */
    public void setSelectedData( final T[] selected )
    {
        list.setSelectedValues( selected );
        if ( selected != null && selected.length > 0 )
        {
            list.ensureIndexIsVisible( Arrays.binarySearch( allData, selected[0] ) );
        }
    }

    // I15nComponentDialog

    protected JComponent createComponentPanel()
    {
        list = new DefaultJList<T>();
        list.addListSelectionListener( this );
        list.addMouseListener( new MouseAdapter()
        {
            public void mouseClicked( final MouseEvent e )
            {
                if ( e.getClickCount() == 2 )
                {
                    apply();
                }
            }
        } );
        list.setVisibleRowCount( 3 );

        SearchableUtils.installSearchable( list );

        return new JScrollPane( list );
    }

    @SuppressWarnings("unchecked")
    protected void onApply()
    {
        final List<T> objects = list.getSelectedValuesList();
        selectedData = (T[]) objects.toArray();
    }

    @SuppressWarnings("unchecked")
    protected void onCancel()
    {
        final List<T> objects = Arrays.asList( allData );
        selectedData = (T[]) objects.toArray();
//        selectedData = (T[]) Array.newInstance( objects.iterator().next().getClass(), 0 );
    }

    // ListSelectionListener

    public void valueChanged( final ListSelectionEvent e )
    {
        final JList list = (JList) e.getSource();
        final List selection = list.getSelectedValuesList();
        enableApply( selection != null && selection.size() > 0 );
    }

    // DocumentListener

    public void insertUpdate( final DocumentEvent e )
    {
        changedUpdate( e );
    }

    public void removeUpdate( final DocumentEvent e )
    {
        changedUpdate( e );
    }

    @SuppressWarnings("unchecked")
    public void changedUpdate( final DocumentEvent e )
    {
        final Document d = e.getDocument();
        String str;
        try
        {
            str = d.getText( 0, d.getLength() ).toUpperCase();
            final List<T> currentObjects = new ArrayList<T>();
            for ( final T o : allData )
            {
                if ( o.toString().toUpperCase().contains( str ) )
                {
                    currentObjects.add( o );
                }
            }
            list.setListData( (T[]) currentObjects.toArray() );
        }
        catch ( Exception x )
        {
            LOGGER.error( "Error getting text", x );
        }
    }

    public static void main( final String[] args )
    {
        final ResourceBundle bundle = new ListResourceBundle()
        {
            protected Object[][] getContents()
            {
                return new String[][]{
                        {"test.TITLE", "Öffne UST Datei"},
                        {"test.TEXT1", "Öffne UST Datei"},
                        {"test.TEXT2", "Das ist ein wirklich langer Text, der testen soll, ob das mit dem Zeilenumbruch klappt. Das ist ein wirklich langer Text, der testen soll, ob das mit dem Zeilenumbruch klappt. Das ist ein wirklich langer Text, der testen soll, ob das mit dem Zeilenumbruch klappt."},
                        {"test.SYMBOL", "file.png"},
                        {"BUTTON.OK.TEXT", "OK"},
                        {"BUTTON.CANCEL.TEXT", "Abbrechen"}
                };
            }
        };
        Strings.setResourceBundle( bundle );
        final ArrayList<String> list = new ArrayList<String>();
        for ( int i = 0; i < 100; i++ )
        {
            list.add( "Eintrag " + i );
        }
        final String[] objects = list.toArray( new String[0] );
        final ListDialog<String> dialog = new ListDialog<String>( (JFrame) null, "test", objects );
        dialog.setSize( 400, 700 );
        WindowUtils.centerOnScreen( dialog );
        dialog.setVisible( true );
    }
}
