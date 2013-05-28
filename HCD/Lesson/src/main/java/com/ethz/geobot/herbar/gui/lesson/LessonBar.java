/*
 * Copyright (c) 2011.
 *
 * Nutzung und Rechte
 *
 * Die Applikation eBot wurde für Studierende der ETH Zürich entwickelt. Sie  steht
 * allen   an   Hochschulen  oder   Fachhochschulen   eingeschriebenen Studierenden
 * (auch  ausserhalb  der  ETH  Zürich)  für  nichtkommerzielle  Zwecke  im Studium
 * kostenlos zur Verfügung. Nichtstudierende Privatpersonen, die die Applikation zu
 * ihrer  persönlichen  Weiterbildung  nutzen  möchten,  werden  gebeten,  für  die
 * nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.– zu bezahlen.
 *
 * Postkonto
 *
 * Unterricht, 85-761469-0, Vermerk "eBot"
 * IBAN 59 0900 0000 8576  1469 0; BIC POFICHBEXXX
 *
 * Jede andere Nutzung der Applikation  ist vorher mit dem Projektleiter  (Matthias
 * Baltisberger, Email:  balti@ethz.ch) abzusprechen  und mit  einer entsprechenden
 * Vereinbarung zu regeln. Die  Applikation wird ohne jegliche  Garantien bezüglich
 * Nutzungsansprüchen zur Verfügung gestellt.
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

    private static final int POP_UP_THRESHOLD = 8;

    private static final String ASKING_PANEL = "askingPanel";

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

    private FocusPopUp focusPopUp;

    private LevelPopUp levelPopUp;

    private IteratorControlPanel taxonControl;

    protected TaxonNameInterrogator askingPanel;

    protected CardLayout cards;

    protected JPanel switcher;

    private final HerbarContext herbarContext;

    private ListPopUp listPopUp;

    private final StatusBar statusBar;

    public LessonBar( final JFrame parent, final HerbarContext herbarModel, final TaxStateModel taxStateModel )
    {
        // init object-vars
        this.parent = parent;
        this.taxStateModel = taxStateModel;
        this.herbarContext = herbarModel;
        this.statusBar = herbarModel.getHerbarGUIManager().getStatusBar();

        final JToolBar toolBar = createToolBar();
        final JPanel breadCrumb = createBreadCrumb();

        createStatusComponents();

        setLayout( new BorderLayout( 0, 0 ) );
        add( toolBar, BorderLayout.NORTH );
        add( breadCrumb, BorderLayout.CENTER );

        // Data driven
        final Taxon focusTaxon = taxStateModel.getFocus();
        bread.setTaxFocus( focusTaxon );
        askingPanel.setTaxFocus( focusTaxon );
        listLabel.setText( taxStateModel.getModel().toString() );
        scopeLabel.setText( taxStateModel.getScope().getName() );
        levelLabel.setText( taxStateModel.getLevel().getName() );
        focusLabel.setText( taxStateModel.getFocus().getName() );
        taxonControl.setCursor( taxStateModel.getTaxList() );
        setOrder();

        // Register Listener
        taxStateModel.addPropertyChangeListener( this );
        taxonControl.addIteratorControlListener( this );
    }

    private JPanel createBreadCrumb()
    {
        // Bread crumb and species name interrogation
        bread = new BreadCrumb();

        askingPanel = new TaxonNameInterrogator( new TaxonNameInterrogator.TransientInterrogatorModel() );

        cards = new CardLayout();
        switcher = new JPanel( cards );
        switcher.add( bread, BREAD );
        switcher.add( askingPanel, ASKING_PANEL );
        switcher.setBorder( new EmptyBorder( 0, Constants.GAP_WITHIN_GROUP, 0, Constants.GAP_WITHIN_GROUP ) );

        // Panel common to focus button and breadcrumb/interrogation
        final JPanel lower = new JPanel( new BorderLayout() );
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

        listButton.setEnabled( herbarContext.getModels().size() != 0 );

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
                getLevelPopUp().showPopUp( (Component) source );
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
        getListPopUp().showPopUp( component );
    }

    public void changeScope()
    {
        final TaxTreeDialog fd = new TaxTreeDialog( parent, taxStateModel.getModel().getRootTaxon() );
        fd.setSize( 600, 800 );
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
        Arrays.sort( copy, new ToStringComparator<Taxon>() );
        if ( copy.length > POP_UP_THRESHOLD )
        {
            final ListDialog<Taxon> dialog = new ListDialog<Taxon>( parent, "DIALOG.FOCUS", copy );
            dialog.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
            dialog.setSize( 400, 800 );
            dialog.setLocationRelativeTo( getTopLevelAncestor() );
            dialog.setVisible( true );
            if ( dialog.isAccepted() )
            {
                taxStateModel.setFocus( dialog.getSelectedData()[0] );
            }
        }
        else
        {
            getFocusPopUp().showPopUp( component );
        }
    }

    public FocusPopUp getFocusPopUp()
    {
        if ( focusPopUp == null )
        {
            focusPopUp = new FocusPopUp( taxStateModel );
        }
        focusPopUp.setObjects( taxStateModel.getTaxList() );
        return focusPopUp;
    }

    public LevelPopUp getLevelPopUp()
    {
        if ( levelPopUp == null )
        {
            levelPopUp = new LevelPopUp();
        }
        levelPopUp.setObjects( taxStateModel.getModel().getRootTaxon().getSubLevels() );
        return levelPopUp;
    }

    public ListPopUp getListPopUp()
    {
        if ( listPopUp == null )
        {
            listPopUp = new ListPopUp();
        }
        return listPopUp;
    }

    public void itemChange( final IteratorControlEvent evt )
    {
        taxStateModel.setFocus( (Taxon) evt.getCurrentObject() );
    }

    public void propertyChange( final PropertyChangeEvent evt )
    {
        final Taxon focus = taxStateModel.getFocus();
        bread.setTaxFocus( focus );
        askingPanel.setTaxFocus( focus );
        if ( evt.getPropertyName().equals( TaxStateModel.TAX_LIST ) )
        {
            taxonControl.setCursor( taxStateModel.getTaxList() );
        }
        scopeLabel.setText( taxStateModel.getScope().getName() );
        levelLabel.setText( taxStateModel.getLevel().getName() );
        if ( evt.getPropertyName().equals( SimpleTaxStateModel.FOCUS ) )
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
        LOG.info( "switching to ask sub mode" );
        cards.show( switcher, ASKING_PANEL );
        statusBar.removeStatusComponent( focusLabel );
    }

    public void switchToLessonMode()
    {
        LOG.info( "switching to lesson sub mode" );
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

    public static class FocusPopUp extends ObjectPopup<Taxon>
    {
        final TaxStateModel taxStateModel;

        public FocusPopUp( final TaxStateModel taxStateModel )
        {
            super( taxStateModel.getTaxList() );
            this.taxStateModel = taxStateModel;
        }

        public void showPopUp( final Component component )
        {
            showPopUp( component, taxStateModel.getFocus() );
        }

        public void itemSelected( final Taxon obj )
        {
            taxStateModel.setFocus( obj );
        }
    }

    public class LevelPopUp extends ObjectPopup<Level>
    {
        public LevelPopUp()
        {
            super( taxStateModel.getModel().getRootTaxon().getSubLevels() );
        }

        public void showPopUp( final Component component )
        {
            showPopUp( component, taxStateModel.getScope().getSubLevels(), taxStateModel.getLevel() );
        }

        public void itemSelected( final Level obj )
        {
            taxStateModel.setLevel( obj );
        }
    }

    public class ListPopUp extends ObjectPopup<HerbarModel>
    {
        public ListPopUp()
        {
            super( herbarContext.getModels().toArray( new HerbarModel[herbarContext.getModels().size()] ) );
        }

        public void showPopUp( final Component component )
        {
            showPopUp( component, taxStateModel.getModel() );
        }

        public void itemSelected( final HerbarModel obj )
        {
            herbarContext.setCurrentModel( obj );
            taxStateModel.setModel( obj );
        }
    }
}
