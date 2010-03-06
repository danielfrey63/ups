package ch.jfactory.component.wizard;

import ch.jfactory.application.presentation.Constants;
import ch.jfactory.bean.ExtendedBeanInfo;
import ch.jfactory.component.ComponentFactory;
import ch.jfactory.component.EditItem;
import ch.jfactory.component.JMultiLineLabel;
import ch.jfactory.component.TextEditItem;
import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.Strings;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.lang.reflect.Method;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Superclass for all wizard panes. All wizard panes must be inherit from this class. It enhance JPanel with some wizard
 * related features.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 */
public abstract class WizardPane extends JPanel
{
    /**
     * logging
     */
    private static final Logger LOGGER = LoggerFactory.getLogger( WizardPane.class );

    /**
     * the wizard model should be initialize in init
     */
    private WizardModel model;

    /**
     * name of the pane
     */
    private final String name;

    /**
     * map of properties
     */
    ExtendedBeanInfo ebi;

    protected String prefix;

    /**
     * This constructor initialize a pane with a given name.
     *
     * @param name the name of the pane
     */
    public WizardPane( final String name )
    {
        super();
        this.name = name;
        prefix = "WIZARD." + getName().toUpperCase();
        initGui();
    }

    private void initGui()
    {
        final JLabel picture = createWizardPicture( prefix + ".ICON" );
        final JPanel display = createDisplayPanel( prefix );

        final int gap = Constants.GAP_BETWEEN_GROUP;

        display.setBorder( BorderFactory.createCompoundBorder( BorderFactory.createCompoundBorder( BorderFactory.createEmptyBorder( 0, gap, 0, 0 ),
                BorderFactory.createEtchedBorder() ),
                BorderFactory.createEmptyBorder( gap, gap, gap, gap ) ) );

        setLayout( new BorderLayout() );
        add( picture, BorderLayout.WEST );
        add( display, BorderLayout.CENTER );
    }

    /**
     * The default approach taken to fill the right hand side of the wizard pane includes a text area in the upper part
     * (see {@link #createTextPanel(String)}) and an edit area in the lower part (see {@link
     * #createEditPanel(String)}).<p> Overwrite this method if you decide to take another approach. Even if you decide
     * to overwrite this method, it is quaranteed that the panel is properly bordered by a edged border with the correct
     * insets.
     *
     * @param prefix the prefix to the key to search for the text, icons, tooltips
     * @return panel containing the right hand side of the wizard pane
     */
    protected JPanel createDisplayPanel( final String prefix )
    {
        final JPanel text = createTextPanel( prefix );
        final JPanel edit = createEditPanel( prefix );

        final JPanel panel = new JPanel( new GridBagLayout() );
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = -1;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        if ( text != null )
        {
            gbc.gridy += 1;
            gbc.weighty = 0.0;
            panel.add( text, gbc );
        }

        if ( edit != null )
        {
            gbc.gridy += 1;
            gbc.weighty = 0.0;
            panel.add( edit, gbc );
        }

        gbc.gridy += 1;
        gbc.weighty = 1.0;
        panel.add( new JPanel(), gbc );

        return panel;
    }

    /**
     * Creates a default wizard right hand side with a text, a title, an edit field and an edit button.
     *
     * @param prefix       the key prefix used to access the string
     * @param buttonAction the action added to the button
     * @return panel containing the component
     */
    protected JPanel createDefaultDisplayArea( final String prefix, final ActionListener buttonAction )
    {
        final JPanel text = createTextPanel( prefix );
        final JPanel edit = createDefaultEdit( prefix, buttonAction );
        return createDefaultDislayArea( text, edit );
    }

    /**
     * Creates a default wizard right hand side with a text, a title, an edit field and an edit button.
     *
     * @param prefix          the key prefix used to access the string
     * @param editFieldAction the action added to the edit field
     * @param focus           the focus listener added to the edit field
     * @return panel containing the component
     */
    protected JPanel createDefaultDisplayArea( final String prefix, final ActionListener editFieldAction, final FocusListener focus )
    {
        final JPanel text = createTextPanel( prefix );
        final JPanel edit = createDefaultTextEdit( prefix, editFieldAction, focus );
        return createDefaultDislayArea( text, edit );
    }

    private JPanel createDefaultDislayArea( final JPanel text, final JPanel edit )
    {
        final JPanel panel = new JPanel( new GridBagLayout() );
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        panel.add( text, gbc );
        gbc.gridy += 1;
        gbc.weighty = 0.0;
        panel.add( edit, gbc );
        gbc.gridy += 1;
        gbc.weighty = 1.0;
        panel.add( new JPanel(), gbc );

        return panel;
    }

    /**
     * The default text display is a simple text block component which is taking its text from the global string
     * resource. The key to search for the text is generated by using the prefix given and appending an extension.
     * Assume <code>PREFIX</code> to be the prefix, then the key looks like this: <code>PREFIX.TEXT</code><p> The text
     * is displayed in a multiline field with the panels background color, but without scrollers.<p> Overwrite this
     * method if you want to have another text display than the default. This will change all default text displays.
     *
     * @param prefix
     * @return panel
     */
    protected JPanel createTextPanel( final String prefix )
    {
        final JMultiLineLabel text = new JMultiLineLabel( Strings.getString( prefix + ".TEXT" ) );
        final JPanel textPanel = new JPanel( new BorderLayout() );
        textPanel.add( text, BorderLayout.CENTER );
        return textPanel;
    }

    /**
     * The default edit area just returns the edit constructed by {@link #createDefaultEdit(String, ActionListener)}.<p>
     * Overwrite this method to setup a customized edit area. If you return null, this area will be excluded properly.
     *
     * @return JPanel containing the edit region
     */
    protected JPanel createEditPanel( final String prefix )
    {
        return createDefaultEdit( prefix, getEditAction() );
    }

    /**
     * Returns a default edit item containing a title in bold, a field displaying the current status and a button to
     * invoke an action to edit the current status. The string key is composed by the prefix <code>WIZARD</code>, the
     * name of the wizard in uppercase, i.e. <code>NAME</code> and postfix <code>CHOOSE</code>, each part separated by a
     * dot: <code>WIZARD.NAME.CHOOSE</code>.<p> If you overwrite this method, you will exchange all edit items for the
     * default behaviour of the wizard.<p> See documentatino of {@link EditItem#EditItem(String, ActionListener)} to see
     * what further extensions are appended to this key to get the final strings.
     *
     * @return edit item
     */
    protected EditItem createDefaultEdit( final String prefix, final ActionListener editAction )
    {
        return new EditItem( prefix, editAction );
    }

    protected TextEditItem createDefaultTextEdit( final String prefix, final ActionListener editAction, final FocusListener focus )
    {
        return new TextEditItem( prefix, editAction, focus );
    }

    /**
     * return the name of the pane.
     *
     * @return name of the pane
     */
    public String getName()
    {
        return name;
    }

    public String toString()
    {
        return name;
    }

    public WizardModel getWizardModel()
    {
        return model;
    }

    /**
     * Is invoked by the wizard if it enter this pane. Overwrite this method if you want to take actions uppon
     * activation.
     */
    public void activate()
    {
    }

    /**
     * Is invoked by the wizard if it leaves this pane. Overwrite this method if you want to take actions uppon
     * activation.
     */
    public void passivate()
    {
    }

    /**
     * Is called before any activation or deactivation of the pane.
     *
     * @return whether it is ok to switch to the next pane
     */
    public boolean isNextOk()
    {
        return true;
    }

    /**
     * Is called before any deactivation of the pane.
     *
     * @return whether it is ok to switch cancel the dialog
     */
    public boolean isCancelOk()
    {
        return true;
    }

    /**
     * Is called before any activation or deactivation of the pane is made.
     *
     * @return whether it is ok to switch to the previous pane
     */
    public boolean isPreviousOk()
    {
        return true;
    }

    /**
     * Overwrite this method to get the simples solution to an editable item by clicking on a button.
     *
     * @return ActionListener to invoke when button is clicked
     */
    protected ActionListener getEditAction()
    {
        return null;
    }

    /**
     * is invoked by the wizard to register change listener. This method is invoked by the init method.
     *
     * @param model refernce to the model
     */
    public void registerPropertyChangeListener( final WizardModel model )
    {
    }

    /**
     * This method should be overwritten to set the standard values.
     */
    public void initDefaultValues()
    {
    }

    /**
     * return a property value.
     *
     * @param name the name of the property
     * @return reference to the property value
     */
    public Object getProperty( final String name )
    {
        Method method = null;
        try
        {
            method = ebi.getPropertyDescriptor( name ).getReadMethod();
            return method.invoke( model );
        }
        catch ( Exception ex )
        {
            final String msg = "Cannot get property \"" + name + "\" for WizardModel. Method invokation \"" + method +
                    "\" not successful.";
            LOGGER.error( msg, ex );
            throw new IllegalStateException( msg );
        }
    }

    /**
     * set a property value
     *
     * @param name  name of the property
     * @param value value of the property
     */
    public void setProperty( final String name, final Object value )
    {
        Method method = null;
        try
        {
            waitCursor( true );
            method = ebi.getPropertyDescriptor( name ).getWriteMethod();
            method.invoke( model, value );
        }
        catch ( Exception ex )
        {
            final String msg = "Cannot set property \"" + name + "\" for WizardModel. Method invokation \"" + method +
                    "\" with argument of " + value + " not successful.";
            LOGGER.error( msg, ex );
            throw new IllegalStateException( msg );
        }
        finally
        {
            waitCursor( false );
        }
    }

    /**
     * is invoked by the wizard if it initialize the pane
     *
     * @param model the wizard model
     */
    final public void init( final WizardModel model )
    {
        this.model = model;
        registerPropertyChangeListener( model );
        initDefaultValues();
    }

    public void waitCursor( final boolean visible )
    {
        final JDialog dlg = (JDialog) this.getTopLevelAncestor();
        if ( dlg != null )
        {
            dlg.getGlassPane().setVisible( visible );
        }
    }

    protected JLabel createWizardPicture( final String resourceKey )
    {
        final JLabel wizardPicture = new JLabel();
        wizardPicture.setIcon( ImageLocator.getIcon( Strings.getString( resourceKey ) ) );
        wizardPicture.setBorder( BorderFactory.createEmptyBorder( 0, 0, 1, 0 ) );
        return wizardPicture;
    }

    /**
     * Create an intro component displaying text gotten from a string resource with the key <code>PREFIX.TEXT</code>,
     * where <code>PREFIX</code> is the value of the argument given.<p> The component may i.e. be used to create the
     * display area of the wizard.
     *
     * @param prefix the prefix used to access the key in the string resource
     * @return JScrollPane displaying the text
     */
    protected JScrollPane createIntroArea( final String prefix )
    {
        final String text = Strings.getString( prefix + ".TEXT" );
        final JTextPane textDisplay = new JTextPane();
//        textDisplay.setBackground(HerbarTheme.getBackground2());
        textDisplay.setContentType( "text/html" );
        textDisplay.setText( text );
        textDisplay.setEditable( false );
        textDisplay.setHighlighter( null );
        textDisplay.setFocusable( false );

        final SimpleAttributeSet attributes = new SimpleAttributeSet();
//        StyleConstants.setFontSize(attributes, HerbarTheme.FONTSIZE);
//        StyleConstants.setFontFamily(attributes, HerbarTheme.FONTFACE);
        StyleConstants.setAlignment( attributes, StyleConstants.ALIGN_LEFT );
        final StyledDocument doc = textDisplay.getStyledDocument();
        doc.setParagraphAttributes( 0, doc.getLength(), attributes, false );

        final JScrollPane scrollPane = new JScrollPane( textDisplay );
        scrollPane.setPreferredSize( new Dimension( 10, 250 ) );
        return scrollPane;
    }

    /**
     * Creates a component displaying a string with the default font but in bold. The string is loaded from the default
     * resource property file by using the key <code>PREFIX.TITLE.TEXT</code>, where <code>PREFIX</code> is the argument
     * given. Gaps are inserted before and after the title component.
     *
     * @param prefix used to prefix the key
     */
    protected JPanel createDefaultTitlePanel( final String prefix )
    {
        final JLabel label = new JLabel( Strings.getString( prefix + ".TITLE.TEXT" ) );
        label.setFont( label.getFont().deriveFont( Font.BOLD ) );
        final JPanel panel = new JPanel( new BorderLayout() );
        panel.add( label, BorderLayout.CENTER );
        panel.setBorder( new EmptyBorder( Constants.GAP_BETWEEN_GROUP, 0, Constants.GAP_BETWEEN_GROUP, 0 ) );
        return panel;
    }

    protected JPanel createSimpleDisplayPanel( final String prefix, final JPanel edit )
    {
        final JPanel panel = new JPanel( new GridBagLayout() );
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add( createTextPanel( prefix ), gbc );
        gbc.gridy += 1;
        panel.add( edit, gbc );
        gbc.gridy += 1;
        gbc.weighty = 1.0;
        panel.add( new JPanel(), gbc );
        return panel;
    }

    protected JButton createListEditButton( final String prefix, final ActionListener actionListener )
    {
        final JButton button = ComponentFactory.createButton( prefix, actionListener );
        button.setFocusable( false );
        button.setBorder( BorderFactory.createEmptyBorder() );
        button.setSize( button.getIcon().getIconHeight(), button.getIcon().getIconWidth() );
        return button;
    }
}
