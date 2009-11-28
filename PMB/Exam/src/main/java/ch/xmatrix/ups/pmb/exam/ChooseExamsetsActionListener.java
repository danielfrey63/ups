package ch.xmatrix.ups.pmb.exam;

import ch.jfactory.file.OpenChooser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.apache.log4j.Logger;

/**
 * Handles the user selection and load of a exam sets file.
 *
 * @author Daniel Frey 18.06.2008 00:16:54
 */
public class ChooseExamsetsActionListener implements ActionListener
{
    /**
     * This class logger.
     */
    private static final Logger LOG = Logger.getLogger( ChooseExamsetsActionListener.class );

    /**
     * The text field model to update.
     */
    private final Document document;

    /**
     * The initial directory to open.
     */
    private final File initialDirectory;

    public ChooseExamsetsActionListener( final Document document, final String initialDirectory )
    {
        this.document = document;
        this.initialDirectory = new File( initialDirectory );
    }

    /**
     * Gets the user selection. If one file is selected, loads it, otherwise does nothing.
     *
     * @param event not used
     */
    public void actionPerformed( final ActionEvent event )
    {
        final File[] files = getUserSelectedFiles();
        if ( files != null && files.length == 1 )
        {
            loadFile( files[0] );
        }
    }

    /**
     * Loads the file into the students list model.
     *
     * @param file the file to load
     */
    private void loadFile( final File file )
    {
        try
        {
            document.remove( 0, document.getLength() );
            document.insertString( 0, file.getAbsolutePath(), null );
        }
        catch ( BadLocationException e )
        {
            LOG.error( "wrong document model updates", e );
        }
    }

    /**
     * Shows the file selection dialog and returns the files selected.
     *
     * @return the files selected
     */
    private File[] getUserSelectedFiles()
    {
        final ExamsetFileFilter filter = new ExamsetFileFilter();
        final OpenChooser chooser = new OpenChooser( filter, "pmb.open.examsets", System.getProperty( "user.dir" ) );
        try
        {
            final String text = document.getText( 0, document.getLength() );
            LOG.info( "settings directory of chooser to " + text );
            chooser.setDirectory( new File( text ) );
        }
        catch ( BadLocationException e )
        {
            LOG.warn( "could not retrieve documents text [0, " + document.getLength() + "]" );
        }
        chooser.setModal( true );
        chooser.setDirectory( initialDirectory );
        chooser.getChooser().setMultiSelectionEnabled( false );
        chooser.open();
        return chooser.getSelectedFiles();
    }
}
