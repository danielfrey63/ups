/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package ch.jfactory.projecttime.entryeditor.builder;

import ch.jfactory.application.view.builder.ActionCommandPanelBuilder;
import ch.jfactory.projecttime.domain.api.IFEntry;
import ch.jfactory.projecttime.entryeditor.command.Commands;
import ch.jfactory.projecttime.entryeditor.command.EntryApply;
import ch.jfactory.projecttime.entryeditor.command.EntryReset;
import ch.jfactory.projecttime.entryeditor.command.EntrySum;
import ch.jfactory.projecttime.entryeditor.command.GregorianCalenderConverter;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * TODO: document
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2005/09/04 19:54:09 $
 */
public class EntryBuilder extends ActionCommandPanelBuilder
{
    private final EntryModel model;

    private final JTextField startField;

    private final JTextField endField;

    private final JTextField categoryField;

    private final JTextField nameField;

    private final JTextField durationField;

    public EntryBuilder( final EntryModel model )
    {
        this.model = model;
        // Make sure to initialize any buffered value models first, before any other listeners are registered.
        final PresentationModel presentationModel = model.getModel();
        startField = createAndBindGregorianCalendarField( presentationModel, IFEntry.PROPERTYNAME_START );
        endField = createAndBindGregorianCalendarField( presentationModel, IFEntry.PROPERTYNAME_END );
        categoryField = createAndBindTextField( presentationModel, IFEntry.PROPERTYNAME_TYPE );
        nameField = createAndBindTextField( presentationModel, IFEntry.PROPERTYNAME_NAME );
        durationField = createDurationField();
    }

    protected void initCommands()
    {
        final PresentationModel presentationModel = model.getModel();
        initCommand( new EntrySum( model, getCommandManager() ) );
        initCommand( new EntryApply( presentationModel, getCommandManager() ) );
        initCommand( new EntryReset( presentationModel, getCommandManager() ) );
    }

    protected void initListeners()
    {
        model.getModel().addPropertyChangeListener( PresentationModel.PROPERTYNAME_BEAN, new PropertyChangeListener()
        {
            public void propertyChange( final PropertyChangeEvent evt )
            {
                final boolean beanIsValid = ( model.getModel().getBean() != null );
                getCommand( Commands.SUM_COMMAND ).setEnabled( beanIsValid );
            }
        } );
        model.getModel().addPropertyChangeListener( PresentationModel.PROPERTYNAME_BUFFERING, new PropertyChangeListener()
        {
            public void propertyChange( final PropertyChangeEvent evt )
            {
                final boolean isBuffering = ( (Boolean) evt.getNewValue() ).booleanValue();
                getCommand( Commands.APPLY_COMMAND ).setEnabled( isBuffering );
                getCommand( Commands.RESET_COMMAND ).setEnabled( isBuffering );
            }
        } );
        model.addPropertyChangeListener( EntryModel.PROPERTYNAME_SUM, new PropertyChangeListener()
        {
            public void propertyChange( final PropertyChangeEvent evt )
            {
                durationField.setText( (String) evt.getNewValue() );
            }
        } );
        model.addPropertyChangeListener( PresentationModel.PROPERTYNAME_BEAN, new PropertyChangeListener()
        {
            public void propertyChange( final PropertyChangeEvent evt )
            {
                final IFEntry entry = (IFEntry) evt.getNewValue();
                model.getModel().setBean( entry );
                final boolean hasChildren = ( entry != null && entry.getChildren() != null );
                startField.setEnabled( !hasChildren );
                endField.setEnabled( !hasChildren );
            }
        } );
    }

    protected JComponent createMainPanel()
    {
        final JPanel main = new JPanel( new BorderLayout() );
        main.add( createFieldsPanel(), BorderLayout.CENTER );
        main.add( createToolbar(), BorderLayout.NORTH );
        return main;
    }

    private JComponent createToolbar()
    {
        return getCommandManager().getGroup( Commands.TOOLBAR_GROUP ).createToolBar( "toolbar" );
    }

    private JComponent createFieldsPanel()
    {
        final FormLayout layout = new FormLayout(
                "12dlu, r:p, 4dlu, p:g(1.0), 12dlu", "12dlu, p, 8dlu, p, 8dlu, p, 8dlu, p, 8dlu, p, 12dlu:g(1.0)" );
        layout.setRowGroups( new int[][]{{2, 4, 6, 8, 10}} );
        final CellConstraints cc = new CellConstraints();
        final JPanel fields = new JPanel( layout );
        fields.add( new JLabel( "Name:" ), cc.xy( 2, 2 ) );
        fields.add( nameField, cc.xy( 4, 2 ) );
        fields.add( new JLabel( "Kategorie:" ), cc.xy( 2, 4 ) );
        fields.add( categoryField, cc.xy( 4, 4 ) );
        fields.add( new JLabel( "Start:" ), cc.xy( 2, 6 ) );
        fields.add( startField, cc.xy( 4, 6 ) );
        fields.add( new JLabel( "Ende:" ), cc.xy( 2, 8 ) );
        fields.add( endField, cc.xy( 4, 8 ) );
        fields.add( new JLabel( "Dauer:" ), cc.xy( 2, 10 ) );
        fields.add( durationField, cc.xy( 4, 10 ) );
        return fields;
    }

    // UTILS

    private static JTextField createDurationField()
    {
        final JTextField field = new JTextField();
        field.setEditable( false );
        return field;
    }

    private static JTextField createAndBindTextField( final PresentationModel model, final String property )
    {
        final JTextField field = new JTextField();
        Bindings.bind( field, model.getBufferedModel( property ) );
        return field;
    }

    private static JTextField createAndBindGregorianCalendarField( final PresentationModel model, final String property )
    {
        final JTextField field = new JTextField();
        Bindings.bind( field, new GregorianCalenderConverter( model.getBufferedModel( property ) ) );
        return field;
    }
}
