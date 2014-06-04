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
import ch.jfactory.component.ComponentFactory;
import ch.jfactory.component.ObjectPopup;
import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.gui.util.IteratorControlEvent;
import com.ethz.geobot.herbar.gui.util.IteratorControlListener;
import com.ethz.geobot.herbar.gui.util.IteratorControlPanel;
import com.ethz.geobot.herbar.modeapi.HerbarContext;
import com.ethz.geobot.herbar.model.HerbarModel;
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
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.SubMode;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.SubMode.Abfragen;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.SubMode.Lernen;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.Focus;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.List;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.Model;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.Ordered;
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

    private JButton listButton;

    private JButton orderButton;

    private final TaxStateModel taxStateModel;

    private IteratorControlPanel taxonControl;

    private final HerbarContext herbarContext;

    private ListPopUp listPopUp;

    private final TaxonNameInterrogatorBuilder queryBuilder;

    public LessonBar( final HerbarContext herbarModel, final TaxStateModel taxStateModel )
    {
        this.taxStateModel = taxStateModel;
        this.herbarContext = herbarModel;
        queryBuilder = new TaxonNameInterrogatorBuilder( herbarContext.getHerbarGUIManager().getParentFrame(), taxStateModel );

        final JPanel filler = new JPanel();
        filler.setBorder( new CompoundBorder( new ThinBevelBorder( BevelDirection.RAISED ), new EmptyBorder( 0, 0, 1, 0 ) ) );
        setLayout( new BorderLayout() );
        add( createToolBar(), WEST );
        add( filler, CENTER );

        // Data driven
        taxonControl.setCursor( taxStateModel.getTaxList() );
        setOrder();

        // Register Listener
        taxonControl.addIteratorControlListener( this );
    }

    private JToolBar createToolBar()
    {
        final String taxonCursorPrefix = Strings.getString( LessonMode.class, "BUTTON.NAVIGATION.PREFIX" );
        taxonControl = new IteratorControlPanel( taxonCursorPrefix );

        listButton = ComponentFactory.createButton( LessonMode.class, "BUTTON.LIST", this );
        orderButton = ComponentFactory.createButton( LessonMode.class, "BUTTON.ORDER", this );

        final JToolBar toolBar = new JToolBar();
        toolBar.setBorder( new CompoundBorder( new ThinBevelBorder( BevelDirection.RAISED ), new EmptyBorder( 0, 0, 1, 0 ) ) );
        toolBar.setFloatable( false );
        toolBar.setRollover( true );
        toolBar.setFocusable( false );
        toolBar.add( listButton );
        toolBar.add( ComponentFactory.createBarSeparator( 0, 3, 0, 3 ) );
        toolBar.add( orderButton );
        toolBar.add( ComponentFactory.createBarSeparator( 0, 3, 0, 3 ) );
        toolBar.add( taxonControl.getPrevButton() );
        toolBar.add( taxonControl.getDisplay() );
        toolBar.add( taxonControl.getNextButton() );
        toolBar.add( ComponentFactory.createBarSeparator( 0, 3, 0, 3 ) );
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
        taxStateModel.addPropertyChangeListener( Focus.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent evt )
            {
                final Taxon focus = (Taxon) evt.getNewValue();
                taxonControl.getIteratorCursor().setCurrent( focus );
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
                taxStateModel.setGlobalSubMode( newSubMode );
            }
        } );
        taxStateModel.addPropertyChangeListener( SubModus.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent evt )
            {
                final boolean isQuery = evt.getNewValue() == Abfragen;
                final SubMode subMode = taxStateModel.getSubMode( taxStateModel.getFocus().getName() );
                final JComponent queryPanel = queryBuilder.getPanel();
                if ( isQuery && subMode != null )
                {
                    add( queryPanel, BorderLayout.SOUTH );
                }
                else
                {
                    remove( queryPanel );
                }
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
            else if ( source == orderButton )
            {
                taxStateModel.setOrdered( !taxStateModel.isOrdered() );
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
