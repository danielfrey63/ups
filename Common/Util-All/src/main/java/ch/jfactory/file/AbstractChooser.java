/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.file;

import ch.jfactory.application.presentation.WindowUtils;
import ch.jfactory.application.view.dialog.I15nHeaderPanel;
import ch.jfactory.resource.Strings;
import com.jgoodies.uif.AbstractDialog;
import com.jgoodies.uif.util.SystemUtils;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.5 $ $Date: 2006/04/18 11:14:34 $
 */
public abstract class AbstractChooser extends AbstractDialog
{
    /** The key base string to access resource strings. */
    protected String base;

    /** The frame title of the error dialog displayed when implementing {@link #checkFiles(File[])}. */
    protected String keyErrorTitle;

    /** The text of the error dialog displayed when implementing {@link #checkFiles(File[])}. */
    protected String keyErrorText;

    /** The panel displaying the help text. */
    private I15nHeaderPanel headerPanel;

    /** The file chooser. */
    private JFileChooser chooser;

    /** The base key for retrieving the resource strings. */

    private FileFilter filter;

    private File directory;

    private int selectionMode = JFileChooser.FILES_ONLY;

    private int type = JFileChooser.SAVE_DIALOG;

    public AbstractChooser( final JFrame owner, final int selectionMode, final String base, final String initialDir, final int type )
    {
        this( owner, selectionMode, base, new File( initialDir ), type );
    }

    public AbstractChooser( final JFrame owner, final int selectionMode, final String base, final File initialDir, final int type )
    {
        super( owner );
        init( null, selectionMode, initialDir, base, type );
    }

    /**
     * Creates a new SaveChooser.
     *
     * <p/>
     *
     * See {@link #AbstractChooser(JFrame, FileFilter, String, File, int)}.
     *
     * @param filter     the file filter
     * @param base       the base string to retrieve system properties from
     * @param initialDir the initial directory
     * @param type
     */
    public AbstractChooser( final JFrame owner, final FileFilter filter, final String base, final String initialDir, final int type )
    {
        this( owner, filter, base, new File( initialDir ), type );
    }

    /**
     * Creates a new SaveChooser. The strings used in the chooser are composed using based on resource strings. The base resource key is extended for the required strings as follows:
     *
     * <ul>
     *
     * <li><code>.frame.TITLE</code> for the frames title</li>
     *
     * <li><code>.frame.TEXT1</code> for a bold text displayed above the chooser in the frame</li>
     *
     * <li><code>.frame.TEXT2</code> for a text displayed above the chooser in the frame</li>
     *
     * <li><code>.error.title</code> for the dialogs title when the file is not found</li>
     *
     * <li><code>.error.text</code> for the message when the file is not found</li>
     *
     * </ul>
     *
     * If you pass in a ExtentionFileFilter for the filter argument, then matching and completing of file names is done automatically.
     *
     * @param owner      the parent frame
     * @param filter     the file filter
     * @param base       the base string to retrieve system properties from
     * @param initialDir the initial directory
     * @param type
     */
    public AbstractChooser( final JFrame owner, final FileFilter filter, final String base, final File initialDir, final int type )
    {
        super( owner );
        init( filter, JFileChooser.FILES_ONLY, initialDir, base, type );
    }

    private void init( final FileFilter filter, final int selectionMode, final File initialDir, final String base, final int type )
    {
        this.filter = filter;
        this.setDirectory( initialDir );
        this.base = base;
        this.type = type;
        this.selectionMode = selectionMode;

        keyErrorTitle = base + ".error.title";
        keyErrorText = base + ".error.text";

        initLayout();
    }

    /**
     * Wrapper to retrieve the selected file.
     *
     * @return the selected file
     */
    public File[] getSelectedFiles()
    {
        File[] files = getChooser().getSelectedFiles();
        if ( files == null || files.length == 0 )
        {
            files = new File[]{getChooser().getSelectedFile()};
        }
        if ( !hasBeenCanceled() )
        {
            int c = 0;
            for ( int i = 0; i < files.length; i++ )
            {
                final File file = files[i];
                if ( file != null )
                {
                    files[c++] = cleanUpFileName( cleanUpFileName0( file ) );
                }
            }
            final File[] clean = new File[c];
            System.arraycopy( files, 0, clean, 0, c );
            files = clean;
        }
        else
        {
            files = new File[0];
        }
        return files;
    }

    /** Inits the common stuff. */
    protected void initLayout()
    {
        chooser = new JFileChooser( directory );
        if ( filter != null )
        {
            chooser.setFileFilter( filter );
        }
        if ( selectionMode != JFileChooser.FILES_ONLY )
        {
            chooser.setFileSelectionMode( selectionMode );
        }
        chooser.setDialogType( type );
        chooser.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );

        headerPanel = new I15nHeaderPanel( base + ".frame" );

        setResizable( true );
        addListener();
    }

    // Have to override this because it removes an incompatiblity to set the header panels preferred size corretly.
    // Basically this is a copy of Karstens code but without the pack statement.
    protected void build()
    {
        final JComponent content = buildContentPane();

        // Work around the JRE's gray rect problem.
        // TODO: Remove this if we require Java 6.
        if ( SystemUtils.IS_JAVA_1_4 || SystemUtils.IS_JAVA_5 )
        {
            setBackground( content.getBackground() );
        }

        resizeHook( content );
        setContentPane( content );
//        pack();
        setResizable();
        locateOnScreen();
        if ( getEscapeCancelsMode().enabled() )
        {
            setEscapeCancelsMode( getEscapeCancelsMode() );
        }
    }

    /**
     * Find out the header panels preferred size.
     *
     * @param b
     */
    public void setVisible( final boolean b )
    {
        if ( b )
        {
            final Dimension preferredSize = getChooser().getPreferredSize();
            getRootPane().setPreferredSize( new Dimension( preferredSize.width, 1200 ) );
            pack();
            final int preferredH = headerPanel.getPreferredSize().height;
            headerPanel.setPreferredSize( new Dimension( preferredSize.width, preferredH ) );
            getRootPane().setPreferredSize( null );
            pack();
            WindowUtils.centerOnScreen( this );
            super.setVisible( b );
        }
    }

    /**
     * Just adds the chooser to the panel.
     *
     * @return the chooser
     */
    protected JComponent buildContent()
    {
        return getChooser();
    }

    /**
     * Overwrites the buid header method and adds the help text.
     *
     * @return the help text panel
     */
    protected JComponent buildHeader()
    {
        return headerPanel;
    }

    public JFileChooser getChooser()
    {
        return chooser;
    }

    protected String getErrorTitle()
    {
        return Strings.getString( keyErrorTitle );
    }

    protected String getErrorText( final File file )
    {
        return Strings.getString( keyErrorText, file.getName() );
    }

    protected abstract File cleanUpFileName( File selectedFile );

    protected abstract boolean checkFiles( File[] files );

    protected abstract void execute( File[] files );

    /** Called when all files have been processed. Dummy implementation. */
    protected void doEnd()
    {
    }

    private File cleanUpFileName0( final File file )
    {
        final FileFilter filter = getChooser().getFileFilter();
        if ( filter instanceof ExtentionFileFilter && file != null )
        {
            // Find matching extention in which case the file is returned as is.
            final ExtentionFileFilter simpleFilter = (ExtentionFileFilter) filter;
            final String[] extentions = simpleFilter.getExtentions();
            for ( final String extention : extentions )
            {
                if ( file.getName().endsWith( extention ) )
                {
                    return file;
                }
            }

            // Complete incomlete files
            if ( extentions.length == 1 )
            {
                if ( !file.getName().endsWith( extentions[0] ) )
                {
                    return new File( file.getAbsoluteFile() + extentions[0] );
                }
            }
        }

        return file;
    }

    /** Wrapper to add an action listener */
    private void addListener()
    {
        getChooser().addActionListener( new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                final String command = e.getActionCommand();
                if ( command == JFileChooser.CANCEL_SELECTION )
                {
                    doCancel();
                    AbstractChooser.this.close();
                }
                else
                {
                    final File[] files = getSelectedFiles();
                    if ( !hasBeenCanceled() && files != null && checkFiles( files ) && command == JFileChooser.APPROVE_SELECTION )
                    {
                        AbstractChooser.this.close();
                        setCursor( getOwner(), Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );
                        execute( files );
                        setCursor( getOwner(), Cursor.getDefaultCursor() );
                    }
                    doEnd();
                }
            }
        } );
    }

    private void setCursor( final Window owner, final Cursor cursor )
    {
        SwingUtilities.invokeLater( new Runnable()
        {
            public void run()
            {
                owner.setCursor( cursor );
            }
        } );
    }

    public void setDirectory( final File directory )
    {
        this.directory = directory;
    }
}
