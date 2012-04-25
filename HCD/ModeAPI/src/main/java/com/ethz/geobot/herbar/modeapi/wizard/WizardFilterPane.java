/*
 * Copyright (c) 2011.
 *
 * Nutzung und Rechte
 *
 * Die Applikation eBot wurde f�r Studierende der ETH Z�rich entwickelt. Sie  steht
 * allen   an   Hochschulen  oder   Fachhochschulen   eingeschriebenen Studierenden
 * (auch  ausserhalb  der  ETH  Z�rich)  f�r  nichtkommerzielle  Zwecke  im Studium
 * kostenlos zur Verf�gung. Nichtstudierende Privatpersonen, die die Applikation zu
 * ihrer  pers�nlichen  Weiterbildung  nutzen  m�chten,  werden  gebeten,  f�r  die
 * nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.� zu bezahlen.
 *
 * Postkonto
 *
 * Unterricht, 85-761469-0, Vermerk "eBot"
 * IBAN 59 0900 0000 8576  1469 0; BIC POFICHBEXXX
 *
 * Jede andere Nutzung der Applikation  ist vorher mit dem Projektleiter  (Matthias
 * Baltisberger, Email:  balti@ethz.ch) abzusprechen  und mit  einer entsprechenden
 * Vereinbarung zu regeln. Die  Applikation wird ohne jegliche  Garantien bez�glich
 * Nutzungsanspr�chen zur Verf�gung gestellt.
 */
package com.ethz.geobot.herbar.modeapi.wizard;

import ch.jfactory.application.presentation.Constants;
import ch.jfactory.component.Dialogs;
import ch.jfactory.component.EditItem;
import ch.jfactory.lang.ArrayUtils;
import ch.jfactory.lang.ToStringComparator;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.modeapi.HerbarContext;
import com.ethz.geobot.herbar.modeapi.wizard.filter.FilterWizardModel;
import com.ethz.geobot.herbar.modeapi.wizard.filter.WizardFilterBasePane;
import com.ethz.geobot.herbar.modeapi.wizard.filter.WizardFilterDefinitionPane;
import com.ethz.geobot.herbar.modeapi.wizard.filter.WizardFilterNamePane;
import com.ethz.geobot.herbar.modeapi.wizard.filter.WizardFilterPreviewPane;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.filter.FilterModel;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WizardPane to display Filter selection
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:11 $
 */
public class WizardFilterPane extends WizardPane
{
    private static final Logger LOG = LoggerFactory.getLogger( WizardFilterPane.class );

    private static final boolean DEBUG = LOG.isDebugEnabled();

    /** name of the pane */
    public static final String NAME = "lesson.filter";

    private static final String INTRO_NAME = "filter.intro";

    private JList list;

    private EditItem current;

    private final String modelPropertyName;

    private MiniListModel listModel;

    public WizardFilterPane( final String modelPropertyName )
    {
        super( NAME, new String[]{modelPropertyName} );
        this.modelPropertyName = modelPropertyName;
    }

    public JPanel createDisplayPanel( final String prefix )
    {
        final JPanel text = createTextPanel( prefix );

        final JLabel title = new JLabel( Strings.getString( prefix + ".TITLE.TEXT" ) );
        title.setFont( title.getFont().deriveFont( Font.BOLD ) );

        // the buttons need list to be ready
        list = new JList();
        list.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );

        final JButton add = createAddButton();
        final JButton minus = createDeleteButton();
        final JButton edit = createEditButton();
        final JButton copy = createActivateButton();

        current = new EditItem( prefix + ".BUTTON" );
        copy.setHorizontalTextPosition( JButton.LEFT );
        copy.setFocusPainted( false );

        final JPanel panel = new JPanel( new GridBagLayout() );
        final int smallGap = Constants.GAP_WITHIN_TOGGLES;
        final int bigGap = Constants.GAP_BETWEEN_GROUP;

        final GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.0;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add( text, gbc );

        gbc.gridy += 1;
        panel.add( current, gbc );

        gbc.gridx = 0;
        gbc.gridy += 1;
        gbc.weightx = 0.0;
        gbc.insets = new Insets( bigGap, 0, bigGap, 0 );
        panel.add( title, gbc );

        gbc.gridx = 0;
        gbc.gridy += 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets( 0, 0, smallGap, smallGap );
        panel.add( add, gbc );

        gbc.gridx = 1;
        panel.add( minus, gbc );

        gbc.gridx = 2;
        panel.add( edit, gbc );

        gbc.gridx = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets( 0, 0, 0, 0 );
        panel.add( copy, gbc );

        gbc.gridx = 0;
        gbc.gridy += 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add( new JScrollPane( list ), gbc );

        return panel;
    }

    private JButton createActivateButton()
    {
        final ActionListener action = new ActionListener()
        {
            public void actionPerformed( final ActionEvent event )
            {
                final HerbarModel model = (HerbarModel) list.getSelectedValue();
                setModel( model );
                activate();
            }
        };
        final JButton button = createListEditButton( prefix + ".COPY.BUTTON", action );
        list.getSelectionModel().addListSelectionListener( new ListSelectionListener()
        {
            public void valueChanged( final ListSelectionEvent e )
            {
                final Object selectedValue = list.getSelectedValue();
                button.setEnabled( selectedValue != null && selectedValue != current.getUserObject() );
            }
        } );
        button.setEnabled( false );
        return button;
    }

    private JButton createEditButton()
    {
        final ActionListener action = new ActionListener()
        {
            public void actionPerformed( final ActionEvent event )
            {
                invokeEditFilterWizard();
            }
        };
        final JButton button = createListEditButton( prefix + ".EDIT.BUTTON", action );
        list.getSelectionModel().addListSelectionListener( new ListSelectionListener()
        {
            public void valueChanged( final ListSelectionEvent e )
            {
                final Set mutableModels = getWizardModel().getHerbarContext().getChangeableModels();
                button.setEnabled( ArrayUtils.contains( mutableModels.toArray(), list.getSelectedValue() ) );
            }
        } );
        button.setEnabled( false );
        return button;
    }

    private JButton createDeleteButton()
    {
        final ActionListener action = new ActionListener()
        {
            public void actionPerformed( final ActionEvent event )
            {
                if ( !isVisible() )
                {
                    return;
                }
                final String title = Strings.getString( "WIZARD.LESSON.FILTER.DELETE.TITLE" );
                final String text = Strings.getString( "WIZARD.LESSON.FILTER.DELETE.TEXT" );
                if ( Dialogs.CANCEL != Dialogs.showQuestionMessageOk( WizardFilterPane.this, title, text ) )
                {
                    final HerbarModel model = (HerbarModel) list.getSelectedValue();
                    listModel.deleteElement( model );
                    final HerbarContext herbarContext = getWizardModel().getHerbarContext();
                    if ( herbarContext.getCurrentModel() == model )
                    {
                        final String defaultModel = Strings.getString( "FILTER_NAME.ALL_TAXA" );
                        setModel( herbarContext.getModel( defaultModel ) );
                    }
                }
            }
        };
        final JButton button = createListEditButton( prefix + ".DELETE.BUTTON", action );
        list.getSelectionModel().addListSelectionListener( new ListSelectionListener()
        {
            public void valueChanged( final ListSelectionEvent e )
            {
                final Set mutableModels = getWizardModel().getHerbarContext().getChangeableModels();
                button.setEnabled( ArrayUtils.contains( mutableModels.toArray(), list.getSelectedValue() ) );
            }
        } );
        button.setEnabled( false );
        return button;
    }

    private JButton createAddButton()
    {
        final ActionListener action = new ActionListener()
        {
            public void actionPerformed( final ActionEvent event )
            {
                if ( !isVisible() )
                {
                    return;
                }
                invokeAddFilterWizard();
            }
        };
        return createListEditButton( prefix + ".ADD.BUTTON", action );
    }

    private void invokeAddFilterWizard()
    {
        final WizardPane[] panes = new WizardPane[]{
                new WizardIntroPane( INTRO_NAME ),
                new WizardFilterNamePane( FilterWizardModel.FILTER_NAME ),
                new WizardFilterBasePane( FilterWizardModel.FILTER_BASE ),
                new WizardFilterDefinitionPane( FilterWizardModel.FILTER_MODEL ),
                new WizardFilterPreviewPane( FilterWizardModel.FILTER_MODEL )
        };
        final HerbarContext herbarContext = getWizardModel().getHerbarContext();
        final NameValidator validator = new FilterModelNameValidator( herbarContext );
        final String filterName = validator.getInitialName();
        final HerbarModel base = herbarContext.getCurrentModel();
        final FilterModel model = new FilterModel( base, filterName, false);
        final String title = Strings.getString( "WIZARD.FILTER.TITLE" );
        final FilterWizardModel wizardModel = new FilterWizardModel( herbarContext, panes, model, validator, title );
        final Wizard dlg = new Wizard( wizardModel );
        final boolean accepted = dlg.show( (JDialog) getTopLevelAncestor(), 700, 388 );

        final FilterModel filterModel = wizardModel.getFilterModel();
        if ( accepted && filterModel != null )
        {
            if ( DEBUG )
            {
                LOG.debug( "store filter model: " + filterModel.getName() );
            }
            listModel.addElement( filterModel );
        }
    }

    private void invokeEditFilterWizard()
    {
        final WizardPane[] panes = new WizardPane[]{
                new WizardIntroPane( INTRO_NAME ),
                new WizardFilterNamePane( FilterWizardModel.FILTER_NAME ),
                new WizardFilterBasePane( FilterWizardModel.FILTER_BASE ),
                new WizardFilterDefinitionPane( FilterWizardModel.FILTER_MODEL ),
                new WizardFilterPreviewPane( FilterWizardModel.FILTER_MODEL )
        };
        final HerbarContext herbarContext = getWizardModel().getHerbarContext();
        final NameValidator validator = new FilterModelNameValidator( herbarContext );
        final FilterModel model = (FilterModel) list.getSelectedValue();
        validator.setInitialName( model.getName() );
        final String title = Strings.getString( "WIZARD.FILTER.TITLE" );
        final FilterWizardModel wizardModel = new FilterWizardModel( herbarContext, panes, model, validator, title );
        final Wizard dlg = new Wizard( wizardModel );
        final boolean accepted = dlg.show( (JDialog) getTopLevelAncestor(), 700, 388 );

        if ( accepted )
        {
            getWizardModel().getHerbarContext().saveModel( model );
        }
    }

    public void registerPropertyChangeListener( final WizardModel model )
    {
        model.addPropertyChangeListener( modelPropertyName, new PropertyChangeListener()
        {
            public void propertyChange( final PropertyChangeEvent event )
            {
                final HerbarModel model = (HerbarModel) event.getNewValue();
                current.setUserObject( model );
            }
        } );
    }

    public void activate()
    {
        listModel = new MiniListModel( getWizardModel() );
        list.setModel( listModel );
        current.setUserObject( ( (LessonWizardModel) getWizardModel() ).getModel() );
    }

    private void setModel( final HerbarModel model )
    {
        setProperty( modelPropertyName, model );
    }

    public void initDefaultValues()
    {
    }

    public static class MiniListModel extends AbstractListModel
    {
        private final WizardModel wizardModel;

        public MiniListModel( final WizardModel wizardModel )
        {
            this.wizardModel = wizardModel;
        }

        public int getSize()
        {
            final Set listData = wizardModel.getHerbarContext().getModels();
            return listData.size();
        }

        public Object getElementAt( final int i )
        {
            final Set<HerbarModel> models = wizardModel.getHerbarContext().getModels();
            final HerbarModel[] listData = models.toArray( new HerbarModel[models.size()] );
            Arrays.sort( listData, new ToStringComparator<HerbarModel>() );
            return listData[i];
        }

        public void deleteElement( final HerbarModel model )
        {
            final Set<HerbarModel> models = wizardModel.getHerbarContext().getModels();
            final HerbarModel[] listData = models.toArray( new HerbarModel[models.size()] );
            Arrays.sort( listData, new ToStringComparator<HerbarModel>() );
            final List<HerbarModel> list = new ArrayList<HerbarModel>( Arrays.asList( listData ) );

            wizardModel.getHerbarContext().removeModel( model );
            final int index = list.indexOf( model );
            fireIntervalRemoved( this, index, index );
        }

        public void addElement( final HerbarModel model )
        {
            wizardModel.getHerbarContext().saveModel( model );

            final Set<HerbarModel> models = wizardModel.getHerbarContext().getModels();
            final HerbarModel[] listData = models.toArray( new HerbarModel[models.size()] );
            Arrays.sort( listData, new ToStringComparator<HerbarModel>() );
            final List<HerbarModel> list = new ArrayList<HerbarModel>( Arrays.asList( listData ) );
            final int index = list.indexOf( model );
            fireIntervalAdded( this, index, index );
        }
    }

    public static class FilterModelNameValidator implements NameValidator
    {
        private final HerbarContext context;

        private String initialName;

        public FilterModelNameValidator( final HerbarContext context )
        {
            this.context = context;
            this.initialName = getValidName();
        }

        public String getValidName()
        {
            String prefix = Strings.getString( "WIZARD.FILTER.NAME.DEFAULT.PREFIX" );
            prefix = ( prefix.equals( "" ) ? "New" : prefix );
            String pattern = Strings.getString( "WIZARD.FILTER.NAME.DEFAULT.POST_FIX" );
            pattern = ( pattern.equals( "" ) ? "000" : pattern );
            int postFix = 1;
            String newName;
            final NumberFormat format = new DecimalFormat( pattern );
            while ( !isValidName( newName = prefix + format.format( postFix ) ) )
            {
                postFix++;
            }
            return newName;
        }

        public boolean isValidName( final String newName )
        {
            boolean found = false;
            final Set modelNames = context.getModelNames();
            for ( final Object modelName : modelNames )
            {
                final String name = (String) modelName;
                if ( !name.equals( initialName ) && name.equals( newName ) )
                {
                    found = true;
                    break;
                }
            }
            return !found && !newName.equals( "" );
        }

        public String getInitialName()
        {
            return initialName;
        }

        public void setInitialName( final String initialName )
        {
            this.initialName = initialName;
        }
    }
}
