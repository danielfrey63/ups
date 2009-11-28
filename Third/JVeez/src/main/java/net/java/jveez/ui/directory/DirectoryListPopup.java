package net.java.jveez.ui.directory;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.util.Collection;
import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.java.jveez.JVeez;
import net.java.jveez.ui.widgets.BlurPanel;
import net.java.jveez.vfs.Directory;
import org.apache.log4j.Logger;

public class DirectoryListPopup extends BlurPanel
{
    /**
     * This class logger.
     */
    private static final Logger LOG = Logger.getLogger( DirectoryListPopup.class );

    private final DirectoryListModel listModel = new DirectoryListModel();

    private final JList directoryList = new JList( listModel );

    private final DirectoryBrowser directoryBrowser;

    private Popup popup;

    private final AWTEventListener awtEventListener = new AWTEventListener()
    {
        public void eventDispatched( final AWTEvent event )
        {
            if ( event.getID() == MouseEvent.MOUSE_PRESSED )
            {
                final Object source = event.getSource();
                if ( !( source instanceof Component )
                        || DirectoryListPopup.this != SwingUtilities.getAncestorOfClass( DirectoryListPopup.class, (Component) source ) )
                {
                    LOG.warn( "CLICKED OUTSIDE !!" );
                    setVisible(false);
                }
            }
        }
    };

    public DirectoryListPopup( final DirectoryBrowser directoryBrowser )
    {
        this.directoryBrowser = directoryBrowser;

        final JScrollPane scrollPane = new JScrollPane( directoryList );
        scrollPane.setBorder( BorderFactory.createEtchedBorder() );
        scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
        scrollPane.getViewport().setOpaque( false );
        scrollPane.setOpaque( false );
        scrollPane.setPreferredSize( new Dimension( 300, 200 ) );

        directoryList.setCellRenderer( new DirectoryListCellRenderer() );
        directoryList.setOpaque( false );
        directoryList.getSelectionModel().addListSelectionListener( new ListSelectionListener()
        {
            public void valueChanged( final ListSelectionEvent e )
            {
                if ( e.getValueIsAdjusting() )
                {
                    return;
                }

                final Directory directory = (Directory) directoryList.getSelectedValue();
                if ( directory != null )
                {
                    DirectoryListPopup.this.directoryBrowser.setCurrentDirectory( directory );
                }
                setVisible(false);
            }
        } );

        setLayout( new BorderLayout() );
        add( scrollPane, BorderLayout.CENTER );
    }

    public synchronized void show( final Collection<? extends Directory> directories, final int x, final int y )
    {
        setVisible(false);

        setBlurSource( JVeez.getMainFrame().getContentPane() );

        listModel.setContent( directories );
        if ( popup == null )
        {
            popup = PopupFactory.getSharedInstance().getPopup( JVeez.getMainFrame(), this, x, y );
            popup.show();
        }

        Toolkit.getDefaultToolkit().addAWTEventListener( awtEventListener, AWTEvent.MOUSE_EVENT_MASK );
    }

    public synchronized void setVisible( final boolean visible )
    {
        super.setVisible( visible );
        if ( !visible )
        {
            Toolkit.getDefaultToolkit().removeAWTEventListener( awtEventListener );
            if ( popup != null )
            {
                popup.hide();
                popup = null;
            }
        }
    }
}
