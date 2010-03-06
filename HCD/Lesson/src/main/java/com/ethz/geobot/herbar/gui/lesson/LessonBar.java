/*
 * Herbar CD-ROM version 2
 *
 * AboutBox.java
 *
 * Created on 2. Mai 2002
 * Created by thomas
 */
package com.ethz.geobot.herbar.gui.lesson;

import ch.jfactory.application.presentation.Constants;
import ch.jfactory.application.view.border.BevelDirection;
import ch.jfactory.application.view.border.ThinBevelBorder;
import ch.jfactory.application.view.dialog.ListDialog;
import ch.jfactory.application.view.status.StatusBar;
import ch.jfactory.component.ComponentFactory;
import ch.jfactory.component.ObjectPopup;
import ch.jfactory.lang.ToStringComparator;
import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.gui.TaxonNameInterrogator;
import com.ethz.geobot.herbar.gui.tax.TaxTreeDialog;
import com.ethz.geobot.herbar.gui.util.HerbarTheme;
import com.ethz.geobot.herbar.gui.util.IteratorControlEvent;
import com.ethz.geobot.herbar.gui.util.IteratorControlListener;
import com.ethz.geobot.herbar.gui.util.IteratorControlPanel;
import com.ethz.geobot.herbar.modeapi.HerbarContext;
import com.ethz.geobot.herbar.modeapi.SimpleTaxStateModel;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:56 $
 */
public class LessonBar extends JPanel implements ActionListener, IteratorControlListener, PropertyChangeListener
{
    private static final Logger LOG = LoggerFactory.getLogger( LessonBar.class );

    private static final int POPUP_THRESHOLD = 8;

    private static final String ASKER = "asker";

    private static final String BREAD = "bread";

    private JButton listButton;

    private JButton scopeButton;

    private JButton levelButton;

    private JButton orderButton;

    private JButton focusButton;

    private JButton listLabel;

    private JButton scopeLabel;

    private JButton levelLabel;

    private JButton orderLabel;

    private JButton focusLabel;

    private BreadCrumb bread;

    private final TaxStateModel taxStateModel;

    private final JFrame parent;

    private FocusPopup focusPopup;

    private LevelPopup levelPopup;

    private IteratorControlPanel taxonControl;

    protected TaxonNameInterrogator asker;

    protected CardLayout cards;

    protected JPanel switcher;

    private final HerbarContext herbarContext;

    private ListPopup listPopup;

    private final StatusBar statusBar;

    public LessonBar( final JFrame parent, final HerbarContext herbarModel, final TaxStateModel taxStateModel )
    {
        // init object-vars
        this.parent = parent;
        this.taxStateModel = taxStateModel;
        this.herbarContext = herbarModel;
        this.statusBar = herbarModel.getHerbarGUIManager().getStatusBar();

        final JToolBar toolBar = createToolBar();
        final JPanel breadCrumb = createBreadCrumb( taxStateModel );

        createStatusComponents();

        setLayout( new BorderLayout( 0, 0 ) );
        add( toolBar, BorderLayout.NORTH );
        add( breadCrumb, BorderLayout.CENTER );

        // Datadriven
        final Taxon focusTaxon = taxStateModel.getFocus();
        bread.setTaxFocus( focusTaxon );
        asker.setTaxFocus( focusTaxon );
        scopeLabel.setText( taxStateModel.getScope().getName() );
        levelLabel.setText( taxStateModel.getLevel().getName() );
        taxonControl.setCursor( taxStateModel.getTaxList() );
        setOrder();

        // Register Listener
        taxStateModel.addPropertyChangeListener( this );
        taxonControl.addIteratorControlListener( this );
    }

    private JPanel createBreadCrumb( final TaxStateModel model )
    {
        // Bread crumb and species name interrogation
        bread = new BreadCrumb( model );
        bread.setBackground( HerbarTheme.getBackground2() );

        asker = new TaxonNameInterrogator( new TaxonNameInterrogator.TransientInterrogatorModel() );
        asker.setBackground( HerbarTheme.getBackground2() );

        cards = new CardLayout();
        switcher = new JPanel( cards );
        switcher.add( bread, BREAD );
        switcher.add( asker, ASKER );
        switcher.setBackground( HerbarTheme.getBackground2() );
        switcher.setBorder( new EmptyBorder( 0, Constants.GAP_WITHIN_GROUP, 0, Constants.GAP_WITHIN_GROUP ) );

        // Panel common to focus button and breadcrumb/interrogation
        final JPanel lower = new JPanel( new BorderLayout() );
        lower.setBackground( HerbarTheme.getBackground2() );
        final int gap = Constants.GAP_BETWEEN_GROUP;
        switcher.setBorder( new CompoundBorder( new ThinBevelBorder( BevelDirection.RAISED ), new EmptyBorder( gap, gap, gap, gap ) ) );
        lower.add( switcher, BorderLayout.CENTER );

        return switcher;
    }

    private JToolBar createToolBar()
    {
        final String taxonCursorPrefix = Strings.getString( LessonMode.class, "BUTTON.NAVIGATION.PREFIX" );
        taxonControl = new IteratorControlPanel( taxonCursorPrefix );
        taxonControl.setAlignment( SwingConstants.RIGHT );

        listButton = ComponentFactory.createButton( LessonMode.class, "BUTTON.LIST", this );
        scopeButton = ComponentFactory.createButton( LessonMode.class, "BUTTON.SCOPE", this );
        levelButton = ComponentFactory.createButton( LessonMode.class, "BUTTON.LEVEL", this );
        orderButton = ComponentFactory.createButton( LessonMode.class, "BUTTON.ORDER", this );
        focusButton = ComponentFactory.createButton( LessonMode.class, "BUTTON.FOCUS", this );

        final JToolBar toolBar = new JToolBar();
        toolBar.setBorder( new ThinBevelBorder( BevelDirection.RAISED ) );
        toolBar.setFloatable( false );
        toolBar.setRollover( true );
        toolBar.setFocusable( false );
        toolBar.add( listButton );
        toolBar.add( ComponentFactory.createSeparator() );
        toolBar.add( scopeButton );
        toolBar.add( levelButton );
        toolBar.add( orderButton );
        toolBar.add( focusButton );
        toolBar.add( ComponentFactory.createSeparator() );
        toolBar.add( taxonControl.getPrevButton() );
        toolBar.add( taxonControl.getNextButton() );
        toolBar.add( ComponentFactory.createSeparator() );

        return toolBar;
    }

    private void createStatusComponents()
    {
        listLabel = ComponentFactory.createLabelButton( LessonMode.class, "LABEL.LIST", this );
        scopeLabel = ComponentFactory.createLabelButton( LessonMode.class, "LABEL.SCOPE", this );
        levelLabel = ComponentFactory.createLabelButton( LessonMode.class, "LABEL.LEVEL", this );
        orderLabel = ComponentFactory.createLabelButton( LessonMode.class, "LABEL.ORDER", this );
        focusLabel = ComponentFactory.createLabelButton( LessonMode.class, "LABEL.FOCUS", this );
    }

    public void actionPerformed( final ActionEvent e )
    {
        try
        {
            final Object source = e.getSource();
            if ( source == listButton || source == listLabel )
            {
                changeList( (Component) source );
            }
            else if ( source == scopeButton || source == scopeLabel )
            {
                changeScope();
            }
            else if ( source == levelButton || source == levelLabel )
            {
                getLevelPopup().showPopup( (Component) source );
            }
            else if ( source == orderButton || source == orderLabel )
            {
                taxStateModel.setSortedList( !taxStateModel.isSortedList() );
            }
            else if ( source == focusButton || source == focusLabel )
            {
                changeFocus( (Component) source );
            }
        }
        catch ( Exception x )
        {
            LOG.error( "actionPerformed() throws an exception: ", x );
        }
    }

    private void changeList( final Component component )
    {
        getListPopup().showPopup( component );
    }

    public void changeScope()
    {
        final TaxTreeDialog fd = new TaxTreeDialog( parent, taxStateModel.getModel().getRootTaxon() );
        fd.setSize( 400, 400 );
        fd.setLocationRelativeTo( getTopLevelAncestor() );
        fd.setVisible( true );
        if ( fd.isAccepted() )
        {
            final Taxon tax = fd.getSelectedTaxon();
            if ( tax != null )
            {
                taxStateModel.setScope( tax );
            }
        }
    }

    public void changeFocus( final Component component )
    {
        final Taxon[] list = taxStateModel.getTaxList();
        final int length = list.length;
        final Taxon[] copy = new Taxon[length];
        System.arraycopy( list, 0, copy, 0, length );
        Arrays.sort( copy, new ToStringComparator() );
        if ( copy.length > POPUP_THRESHOLD )
        {
            final ListDialog dialog = new ListDialog( parent, "DIALOG.FOCUS", copy );
            dialog.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
            dialog.setSize( 300, 300 );
            dialog.setLocationRelativeTo( getTopLevelAncestor() );
            dialog.setVisible( true );
            if ( dialog.isAccepted() )
            {
                taxStateModel.setFocus( (Taxon) dialog.getSelectedData()[0] );
            }
        }
        else
        {
            getFocusPopup().showPopup( component );
        }
    }

    public FocusPopup getFocusPopup()
    {
        if ( focusPopup == null )
        {
            focusPopup = new FocusPopup();
        }
        focusPopup.setObjects( taxStateModel.getTaxList() );
        return focusPopup;
    }

    public LevelPopup getLevelPopup()
    {
        if ( levelPopup == null )
        {
            levelPopup = new LevelPopup();
        }
        levelPopup.setObjects( taxStateModel.getModel().getRootTaxon().getSubLevels() );
        return levelPopup;
    }

    public ListPopup getListPopup()
    {
        if ( listPopup == null )
        {
            listPopup = new ListPopup();
        }
        final Object[] models = herbarContext.getModels().toArray();
        Arrays.sort( models, new ToStringComparator() );
        listPopup.setObjects( models );
        return listPopup;
    }

    public void itemChange( final IteratorControlEvent evt )
    {
        taxStateModel.setFocus( (Taxon) evt.getCurrentObject() );
    }

    public void propertyChange( final PropertyChangeEvent evt )
    {
        final Taxon focus = taxStateModel.getFocus();
        bread.setTaxFocus( focus );
        asker.setTaxFocus( focus );
        if ( evt.getPropertyName() == TaxStateModel.TAXLIST )
        {
            taxonControl.setCursor( taxStateModel.getTaxList() );
        }
        scopeLabel.setText( taxStateModel.getScope().getName() );
        levelLabel.setText( taxStateModel.getLevel().getName() );
        if ( evt.getPropertyName() == SimpleTaxStateModel.FOCUS )
        {
            taxonControl.getIteratorCursor().setCurrent( focus );
        }
        final HerbarModel model = taxStateModel.getModel();
        listLabel.setText( model.toString() );
        focusLabel.setText( taxStateModel.getFocus().getName() );
        setOrder();
    }

    private void setOrder()
    {
        final boolean isSorted = taxStateModel.isSortedList();

        String orderIcon = "" + isSorted;
        orderIcon = Strings.getString( LessonMode.class, "BUTTON.ORDER.STATE.ICON", orderIcon );
        final ImageIcon icon = ImageLocator.getIcon( orderIcon );
        orderButton.setIcon( icon );

        final String orderText = ( isSorted ? "BUTTON.ORDER.STATE.SORTED.TEXT" : "BUTTON.ORDER.STATE.RANDOM.TEXT" );
        orderLabel.setText( Strings.getString( LessonMode.class, orderText ) );
    }

    public void switchToAskMode()
    {
        cards.show( switcher, ASKER );
        statusBar.removeStatusComponent( focusLabel );
    }

    public void switchToLessonMode()
    {
        cards.show( switcher, BREAD );
        statusBar.addStatusComponent( focusLabel, 5 );
    }

    public void activate()
    {
        statusBar.addStatusComponent( listLabel );
        statusBar.addStatusComponent( scopeLabel );
        statusBar.addStatusComponent( levelLabel );
        statusBar.addStatusComponent( orderLabel );
        statusBar.addStatusComponent( focusLabel );
        statusBar.addStatusComponent( taxonControl.getDisplay() );
    }

    public void deactivate()
    {
        statusBar.removeStatusComponent( taxonControl.getDisplay() );
        statusBar.removeStatusComponent( focusLabel );
        statusBar.removeStatusComponent( orderLabel );
        statusBar.removeStatusComponent( levelLabel );
        statusBar.removeStatusComponent( scopeLabel );
        statusBar.removeStatusComponent( listLabel );
    }

    public class FocusPopup extends ObjectPopup
    {
        public FocusPopup()
        {
            super( taxStateModel.getTaxList() );
        }

        public void showPopup( final Component component )
        {
            showPopup( component, taxStateModel.getFocus() );
        }

        public void itemSelected( final Object obj )
        {
            taxStateModel.setFocus( (Taxon) obj );
        }
    }

    public class LevelPopup extends ObjectPopup
    {
        public LevelPopup()
        {
            super( taxStateModel.getModel().getRootTaxon().getSubLevels() );
        }

        public void showPopup( final Component component )
        {
            showPopup( component, taxStateModel.getScope().getSubLevels(), taxStateModel.getLevel() );
        }

        public void itemSelected( final Object obj )
        {
            taxStateModel.setLevel( (Level) obj );
        }
    }

    public class ListPopup extends ObjectPopup
    {
        public ListPopup()
        {
            super( herbarContext.getModels().toArray() );
        }

        public void showPopup( final Component component )
        {
            showPopup( component, taxStateModel.getModel() );
        }

        public void itemSelected( final Object obj )
        {
            herbarContext.setCurrentModel( (HerbarModel) obj );
            taxStateModel.setModel( (HerbarModel) obj );
        }
    }
}
