/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.typemapper;

import ch.jfactory.application.view.dialog.I15nComponentDialog;
import ch.jfactory.file.FileUtils;
import ch.jfactory.image.ImagesPanel;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.value.BufferedValueModel;
import com.jgoodies.binding.value.Trigger;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2007/09/27 10:41:22 $
 */
public class IconsAction extends ActionCommand
{
    private static final FileUtils.StringFilter FILTER = new Filter();

    private I15nComponentDialog dialog;

    private final BufferedValueModel model;

    private ImagesPanel panel;

    private JScrollPane pane;

    private final Trigger trigger;

    public IconsAction( final CommandManager commandManager, final PresentationModel model )
    {
        super( commandManager, Commands.COMMANDID_EDITICONS );
        trigger = new Trigger();
        this.model = new BufferedValueModel( model.getBufferedModel( TypeMapping.PROPERTYNAME_ICON ), trigger );
    }

    protected void handleExecute()
    {
        try
        {
            if ( dialog == null )
            {
                final Collection icons = FileUtils.getFilesFromClasspath( FILTER );
                panel = ImagesPanel.createImagesPanel( icons, 30, 30, true );
                pane = new JScrollPane( panel );
                pane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
                panel.setScroller( pane );
                dialog = new IconsDialog( panel );
            }
            dialog.setVisible( true );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    private static class Filter implements FileUtils.StringFilter
    {
        private static final String[] extentions = new String[]{".jpg", ".gif", ".png", ".bmp"};

        public boolean accept( final String file )
        {
            boolean result = new File( file ).isDirectory();
            for ( final String extention : extentions )
            {
                result |= file.endsWith( extention );
            }
            final ImageIcon icon = new ImageIcon( file );
            return result && icon.getIconHeight() == icon.getIconHeight();
        }
    }

    private class IconsDialog extends I15nComponentDialog
    {
        public IconsDialog( final ImagesPanel panel )
        {
            super( (JFrame) null, "type.icons" );
            panel.getModel().addPropertyChangeListener( ImagesPanel.SelectionModel.PROPERTYNAME_SELECTED, new Handler() );
        }

        protected void onApply() throws ComponentDialogException
        {
            model.setValue( panel.getModel().getSelected().getIconPath() );
            trigger.triggerCommit();
        }

        protected void onCancel()
        {
            trigger.triggerFlush();
        }

        protected JComponent createComponentPanel()
        {
            return pane;
        }

        private class Handler implements PropertyChangeListener
        {
            public void propertyChange( final PropertyChangeEvent evt )
            {
                enableApply( evt.getNewValue() != null );
            }
        }
    }
}
