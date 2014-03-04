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

import ch.jfactory.application.view.border.BevelDirection;
import ch.jfactory.application.view.border.ThinBevelBorder;
import ch.jfactory.application.view.dialog.ListDialog;
import ch.jfactory.component.ComponentFactory;
import ch.jfactory.component.ObjectPopup;
import ch.jfactory.lang.ToStringComparator;
import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.gui.tax.TaxTreeDialog;
import com.ethz.geobot.herbar.gui.util.IteratorControlEvent;
import com.ethz.geobot.herbar.gui.util.IteratorControlListener;
import com.ethz.geobot.herbar.gui.util.IteratorControlPanel;
import com.ethz.geobot.herbar.modeapi.HerbarContext;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import java.awt.BorderLayout;
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
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.SubMode;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.SubMode.Abfragen;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.SubMode.Lernen;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.Focus;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.Level;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.List;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.Model;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.Ordered;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.Scope;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.SubModus;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.WEST;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:56 $
 */
public class LessonBar extends JPanel implements ActionListener, IteratorControlListener
{
    private static final Logger LOG = LoggerFactory.getLogger( LessonBar.class );

    private static final int POP_UP_THRESHOLD = 8;

    private JButton listButton;

    private JButton scopeButton;

    private JButton levelButton;

    private JButton orderButton;

    private JButton focusButton;

    private final TaxStateModel taxStateModel;

    private final JFrame parent;

    private FocusPopUp focusPopUp;

    private LevelPopUp levelPopUp;

    private IteratorControlPanel taxonControl;

    protected TaxonNameInterrogatorBuilder breadCrumbBuilder;

    private final HerbarContext herbarContext;

    private ListPopUp listPopUp;

    public LessonBar( final JFrame parent, final HerbarContext herbarModel, final TaxStateModel taxStateModel )
    {
        this.parent = parent;
        this.taxStateModel = taxStateModel;
        this.herbarContext = herbarModel;

        breadCrumbBuilder = new TaxonNameInterrogatorBuilder( parent, taxStateModel );

        setLayout( new BorderLayout() );
        add( createToolBar(), WEST );
        add( breadCrumbBuilder.getPanel(), CENTER );

        // Data driven
        final Taxon focusTaxon = taxStateModel.getFocus();
        breadCrumbBuilder.setTaxFocus( focusTaxon );
        taxonControl.setCursor( taxStateModel.getTaxList() );
        setOrder();
        levelButton.setText( taxStateModel.getLevel().getName() );

        // Register Listener
        taxonControl.addIteratorControlListener( this );
    }

    private JToolBar createToolBar()
    {
        final String taxonCursorPrefix = Strings.getString( LessonMode.class, "BUTTON.NAVIGATION.PREFIX" );
        taxonControl = new IteratorControlPanel( taxonCursorPrefix );

        listButton = ComponentFactory.createButton( LessonMode.class, "BUTTON.LIST", this );
        scopeButton = ComponentFactory.createButton( LessonMode.class, "BUTTON.SCOPE", this );
        levelButton = ComponentFactory.createButton( LessonMode.class, "BUTTON.LEVEL", this );
        orderButton = ComponentFactory.createButton( LessonMode.class, "BUTTON.ORDER", this );
        focusButton = ComponentFactory.createButton( LessonMode.class, "BUTTON.FOCUS", this );

        final JToolBar toolBar = new JToolBar();
        toolBar.setBorder( new CompoundBorder( new ThinBevelBorder( BevelDirection.RAISED ), new EmptyBorder( 0, 0, 1, 0 ) ) );
        toolBar.setFloatable( false );
        toolBar.setRollover( true );
        toolBar.setFocusable( false );
        toolBar.add( listButton );
        toolBar.add( ComponentFactory.createBarSeparator( 0, 3, 0, 3 ) );
        toolBar.add( scopeButton );
        toolBar.add( ComponentFactory.createBarSeparator( 0, 3, 0, 3 ) );
        toolBar.add( levelButton );
        toolBar.add( ComponentFactory.createBarSeparator( 0, 3, 0, 3 ) );
        toolBar.add( orderButton );
        toolBar.add( ComponentFactory.createBarSeparator( 0, 3, 0, 3 ) );
        toolBar.add( taxonControl.getPrevButton() );
        toolBar.add( taxonControl.getDisplay() );
        toolBar.add( taxonControl.getNextButton() );
        toolBar.add( ComponentFactory.createBarSeparator( 0, 3, 0, 3 ) );
        toolBar.add( focusButton );
        toolBar.add( ComponentFactory.createBarSeparator( 0, 3, 0, 12 ) );
        toolBar.add( createSubModusSwitcher() );

        listButton.setEnabled( herbarContext.getModels().size() != 0 );

        return toolBar;
    }

    private JButton createSubModusSwitcher()
    {
        final SubMode[] subModi = SubMode.values();
        final JButton subModesToggle = new JButton( subModi[0].name() );
        subModesToggle.setFocusPainted( false );
        subModesToggle.setText( Abfragen.name() );
        subModesToggle.setPreferredSize( subModesToggle.getPreferredSize() );
        subModesToggle.setText( Lernen.name() );
        subModesToggle.setSize( subModesToggle.getPreferredSize() );
        taxStateModel.addPropertyChangeListener( Model.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent e )
            {
                listButton.setText( taxStateModel.getModel().toString() );
            }
        } );
        taxStateModel.addPropertyChangeListener( List.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent evt )
            {
                taxonControl.setCursor( taxStateModel.getTaxList() );
            }
        } );
        taxStateModel.addPropertyChangeListener( Scope.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent e )
            {
                scopeButton.setText( taxStateModel.getScope().getName() );
            }
        } );
        taxStateModel.addPropertyChangeListener( Level.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent e )
            {
                levelButton.setText( taxStateModel.getLevel().getName() );
            }
        } );
        taxStateModel.addPropertyChangeListener( Focus.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent evt )
            {
                final Taxon focus = (Taxon) evt.getNewValue();
                taxonControl.getIteratorCursor().setCurrent( focus );
                breadCrumbBuilder.setTaxFocus( focus );
            }
        } );
        taxStateModel.addPropertyChangeListener( SubModus.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( final PropertyChangeEvent evt )
            {
                //breadCrumbBuilder.setGlobalSubMode( (SubMode) evt.getNewValue() );
                subModesToggle.setText( taxStateModel.getGlobalSubMode().name() );
            }
        } );
        taxStateModel.addPropertyChangeListener( Ordered.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent evt )
            {
                setOrder();
            }
        } );
        subModesToggle.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                final int currentPos = Arrays.binarySearch( subModi, SubMode.valueOf( subModesToggle.getText() ) );
                final SubMode newSubMode = subModi[( ( currentPos + 1 ) % 2 )];
                breadCrumbBuilder.setGlobalSubMode( newSubMode );
                taxStateModel.setGlobalSubMode( newSubMode );
            }
        } );
        return subModesToggle;
    }


    public void actionPerformed( final ActionEvent e )
    {
        try
        {
            final Object source = e.getSource();
            if ( source == listButton )
            {
                changeList( (Component) source );
            }
            else if ( source == scopeButton )
            {
                changeScope();
            }
            else if ( source == levelButton )
            {
                getLevelPopUp().showPopUp( (Component) source );
            }
            else if ( source == orderButton )
            {
                taxStateModel.setOrdered( !taxStateModel.isOrdered() );
            }
            else if ( source == focusButton )
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

    private void setOrder()
    {
        final boolean isSorted = taxStateModel.isOrdered();

        String orderIcon = "" + isSorted;
        orderIcon = Strings.getString( LessonMode.class, "BUTTON.ORDER.STATE.ICON", orderIcon );
        final ImageIcon icon = ImageLocator.getIcon( orderIcon );
        orderButton.setIcon( icon );

        final String orderText = ( isSorted ? "BUTTON.ORDER.STATE.SORTED.TEXT" : "BUTTON.ORDER.STATE.RANDOM.TEXT" );
        orderButton.setText( Strings.getString( LessonMode.class, orderText ) );
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
