package ch.jfactory.application.view.dialog;

import ch.jfactory.action.ActionUtils;
import ch.jfactory.application.presentation.Constants;
import ch.jfactory.component.ComponentFactory;
import ch.jfactory.resource.Strings;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.Sizes;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import org.apache.log4j.Logger;

/**
 * Implements default behaviour to display a component like TaxTree or LevelSelectionList. The only methods that you
 * have to implement are {@link I15nComponentDialog#onApply()}, {@link I15nComponentDialog#onCancel()} and {@link
 * I15nComponentDialog#createComponentPanel()}. The prefix is used to get the text from resources by calling the {@link
 * Strings#getString(String)} method. The resource keys you should use are (PREFIX is replaced):
 * <ul><li><code>PREFIX.TITLE</code>: the text in the title of the window</li> <li><code>PREFIX.TEXT1</code>: the text
 * given in the white plase on the top of the dialog</li> <li><code>PREFIX.TEXT2</code>: the text placed immediatly
 * before your component</li> </ul> This component also relies on the {@link ch.jfactory.component.ComponentFactory} to
 * create buttons. See there for additional keys to put into the string resource.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.5 $ $Date: 2006/03/22 15:05:10 $
 */
abstract public class I15nComponentDialog extends JDialog
{
    private static final Logger LOGGER = Logger.getLogger( I15nComponentDialog.class );

    protected JButton apply;

    protected JButton cancel;

    private boolean accepted = false;

    public I15nComponentDialog( final Frame parent, final String prefix )
    {
        super( parent, Strings.getString( prefix + ".TITLE" ), true );
        dialogInit( prefix );
    }

    public I15nComponentDialog( final Dialog parent, final String prefix )
    {
        super( parent, Strings.getString( prefix + ".TITLE" ), true );
        dialogInit( prefix );
    }

    protected void dialogInit( final String prefix )
    {
        setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );

        final JPanel titlePanel = new I15nHeaderPanel( prefix );

        final JPanel buttonPanel = createDefaultButtonPanel();

        // make sure to init button before call to overwritable createComponentPanel, as this may hide them.
        // init component panel
        final JComponent componentPanel = createComponentPanel();
        componentPanel.setBorder( BorderFactory.createCompoundBorder( BorderFactory.createEtchedBorder(),
                Borders.createEmptyBorder( Sizes.DLUY8, Sizes.DLUX8, Sizes.DLUY8, Sizes.DLUX8 ) ) );

        final JPanel panel = new JPanel( new BorderLayout() );
        panel.setBorder( Borders.createEmptyBorder( Sizes.DLUY8, Sizes.DLUX8, Sizes.DLUY8, Sizes.DLUX8 ) );
        panel.add( componentPanel, BorderLayout.CENTER );
        panel.add( buttonPanel, BorderLayout.SOUTH );

        getContentPane().setLayout( new BorderLayout() );
        getContentPane().add( titlePanel, BorderLayout.NORTH );
        getContentPane().add( panel, BorderLayout.CENTER );

        addWindowListener( new WindowAdapter()
        {
            public void windowClosing( final WindowEvent e )
            {
                onCancel();
            }
        } );

        if ( isApplyShowing() )
        {
            getRootPane().setDefaultButton( apply );
        }
        if ( isCancelShowing() )
        {
            ActionUtils.registerEscapeKey( cancel );
        }
    }

    private JPanel createDefaultButtonPanel()
    {
        apply = createOkButton();
        cancel = createCancelButton();

        final FormLayout layout = new FormLayout( "p:g, max(50dlu;p), 8dlu, max(50dlu;p)", "4dlu, p" );
        layout.setColumnGroups( new int[][]{{2, 4}} );
        final JPanel panel = new JPanel( layout );

        panel.setBorder( BorderFactory.createEmptyBorder( Constants.GAP_BETWEEN_GROUP, 0, 0, 0 ) );
        final CellConstraints cc = new CellConstraints();
        if ( isApplyShowing() )
        {
            panel.add( apply, cc.xy( 2, 2 ) );
        }
        if ( isCancelShowing() )
        {
            panel.add( cancel, cc.xy( 4, 2 ) );
        }

        return panel;
    }

    private JButton createOkButton()
    {
        final ActionListener action = new ActionListener()
        {
            public void actionPerformed( final ActionEvent l )
            {
                apply();
            }
        };
        final JButton button = ComponentFactory.createButton( "BUTTON.OK", action );
        if ( isCancelShowing() )
        {
            button.setEnabled( false );
        }
        return button;
    }

    public void apply()
    {
        final Cursor saved = getCursor();
        try
        {
            setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );
            accepted = true;
            boolean dispose = true;
            try
            {
                onApply();
            }
            catch ( ComponentDialogException e )
            {
                dispose = false;
            }
            if ( dispose )
            {
                dispose();
            }
        }
        catch ( RuntimeException e )
        {
            dispose();
            LOGGER.error( "error when confirming dialog", e );
            throw e;
        }
        finally
        {
            setCursor( saved );
        }
    }

    private JButton createCancelButton()
    {
        final ActionListener action = new ActionListener()
        {
            public void actionPerformed( final ActionEvent l )
            {
                onCancel();
                dispose();
            }
        };
        return ComponentFactory.createButton( "BUTTON.CANCEL", action );
    }

    /**
     * Tells the caller if the ok button was pressed.
     *
     * @return true if the selection is accepted
     */
    public boolean isAccepted()
    {
        return accepted;
    }

    /**
     * This method enables or disables the apply button.
     *
     * @param value true for apply enabled otherwise false
     */
    protected void enableApply( final boolean value )
    {
        apply.setEnabled( value );
    }

    protected boolean isApplyShowing()
    {
        return true;
    }

    protected boolean isCancelShowing()
    {
        return true;
    }

    /**
     * This method should implement the behaviour on apply. Throw a {@link I15nComponentDialog.ComponentDialogException}
     * if you want to let the dialog stay open.
     */
    protected abstract void onApply() throws I15nComponentDialog.ComponentDialogException;

    /**
     * This method should implement the behaviour on cancel.
     */
    protected abstract void onCancel();

    /**
     * This method should implement the presentation of the component. This method is called after the Button-Panel is
     * initialized.
     */
    protected abstract JComponent createComponentPanel();

    /**
     * Exception to throw when a component dialog has to stay open uppon apply.
     */
    public class ComponentDialogException extends Exception
    {
        public ComponentDialogException()
        {
        }

        public ComponentDialogException( final String message )
        {
            super( message );
        }

        public ComponentDialogException( final String message, final Throwable cause )
        {
            super( message, cause );
        }

        public ComponentDialogException( final Throwable cause )
        {
            super( cause );
        }
    }
}
