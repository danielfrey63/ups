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

import ch.jfactory.component.Dialogs;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * A JFileChooser embeded in a JDialog with a help text in the north. To use this class overwrite the method {@link #save(File)}. Optionally you may overwrite {@link #cleanUpFileName(File)} for checks or change a file name.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.4 $
 */
public class SaveChooser extends AbstractChooser
{
    /** {@inheritDoc} */
    public SaveChooser( final FileFilter filter, final String base, final File initialDir )
    {
        super( null, filter, base, initialDir, JFileChooser.SAVE_DIALOG );
    }

    /** {@inheritDoc} */
    public SaveChooser( final FileFilter filter, final String base, final String initialDir )
    {
        super( null, filter, base, initialDir, JFileChooser.SAVE_DIALOG );
    }

    /** {@inheritDoc} */
    public SaveChooser( final int selectionMode, final String base, final File initialDir )
    {
        super( null, selectionMode, base, initialDir, JFileChooser.SAVE_DIALOG );
    }

    /** {@inheritDoc} */
    public SaveChooser( final int selectionMode, final String base, final String initialDir )
    {
        super( null, selectionMode, base, initialDir, JFileChooser.SAVE_DIALOG );
    }

    /**
     * Makes sure the file is only overwritten upon permission of the user.
     *
     * @param files the files to check
     * @return whether saving is ok
     */
    protected boolean checkFiles( final File[] files )
    {
        for ( final File file : files )
        {
            if ( file.exists() && !file.isDirectory() && getChooser().getFileSelectionMode() == JFileChooser.FILES_ONLY )
            {
                return Dialogs.showQuestionMessageOk( getChooser(), getErrorTitle(), getErrorText( file ) ) == Dialogs.OK;
            }
        }
        return true;
    }

    /**
     * Redirector to save.
     *
     * @param files
     */
    protected void execute( final File[] files )
    {
        for ( final File file : files )
        {
            save( file );
        }
    }

    /**
     * Overwrite this method if you want to doublecheck or even change the file name before any checking for overwriting is done.
     *
     * @param file the original file choosen
     * @return the cleaned up file
     */
    protected File cleanUpFileName( final File file )
    {
        return file;
    }

    /**
     * Overwrite this method to invoke an action to save the file after it has been checked for overwriting and name cleanup.
     *
     * @param file
     */
    protected void save( final File file )
    {
    }
}
