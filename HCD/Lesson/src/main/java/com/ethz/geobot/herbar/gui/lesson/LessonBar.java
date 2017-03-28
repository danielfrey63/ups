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
import ch.jfactory.resource.Strings;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.EditState.USE;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.Mode;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.Mode.ABFRAGEN;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.Mode.LERNEN;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.*;
import com.ethz.geobot.herbar.gui.util.IteratorControlEvent;
import com.ethz.geobot.herbar.gui.util.IteratorControlListener;
import com.ethz.geobot.herbar.gui.util.IteratorControlPanel;
import com.ethz.geobot.herbar.modeapi.HerbarContext;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Taxon;
import java.awt.BorderLayout;
import static java.awt.BorderLayout.*;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:56 $
 */
public class LessonBar extends JPanel
{
    private final TaxStateModel taxStateModel;

    private final HerbarContext herbarContext;

    private JButton listButton;

    private JButton orderButton;

    private JPanel editAndQueryPanel;

    private IteratorControlPanel taxonControl;

    private ListPopUp listPopUp;

    private TaxonNameInterrogatorBuilder queryBuilder;

    private JButton subModusToggle;

    public LessonBar( final HerbarContext herbarModel, final TaxStateModel taxStateModel )
    {
        this.taxStateModel = taxStateModel;
        this.herbarContext = herbarModel;

        initUi();
        initListeners();
        setScrollerState( taxStateModel.getFocus() );
    }

    private void initUi()
    {
        queryBuilder = new TaxonNameInterrogatorBuilder( herbarContext.getHerbarGUIManager().getParentFrame(), taxStateModel );

        setLayout( new BorderLayout() );
        add( createToolBar(), NORTH );
        add( createEditAndQueryPanel(), SOUTH );
    }

    private JPanel createEditAndQueryPanel()
    {
        editAndQueryPanel = new JPanel( new BorderLayout() );
        editAndQueryPanel.add( new EditBarBuilder( herbarContext, taxStateModel ).getPanel(), WEST );
        return editAndQueryPanel;
    }

    private JToolBar createToolBar()
    {
        taxonControl = createTaxonControl();
        listButton = createListButton();
        orderButton = createOrderButton();
        subModusToggle = createModeSwitcher();

        setListButtonText();

        final JToolBar toolBar = new JToolBar();
        toolBar.setBorder( new CompoundBorder( new ThinBevelBorder( BevelDirection.RAISED ), new EmptyBorder( 0, 0, 1, 0 ) ) );
        toolBar.setFloatable( false );
        toolBar.setRollover( true );
        toolBar.setFocusable( false );
        toolBar.add( listButton );
        toolBar.add( ComponentFactory.createBarSeparator( 0, 3, 0, 3 ) );
        toolBar.add( orderButton );
        toolBar.add( ComponentFactory.createBarSeparator( 0, 3, 0, 3 ) );
        toolBar.add( taxonControl.getFirstButton() );
        toolBar.add( taxonControl.getPrevButton() );
        toolBar.add( taxonControl.getDisplay() );
        toolBar.add( taxonControl.getNextButton() );
        toolBar.add( taxonControl.getLastButton() );
        toolBar.add( ComponentFactory.createBarSeparator( 0, 3, 0, 3 ) );
        toolBar.add( subModusToggle );
        toolBar.add( ComponentFactory.createBarSeparator( 0, 3, 0, 3 ) );
        listButton.setEnabled( herbarContext.getModels().size() != 0 );
        return toolBar;
    }

    private IteratorControlPanel createTaxonControl()
    {
        final IteratorControlPanel panel = new IteratorControlPanel( Strings.getString( LessonMode.class, "BUTTON.NAVIGATION.PREFIX" ) );
        panel.setList( taxStateModel.getTaxList() );
        panel.addIteratorControlListener( new IteratorControlListener()
        {
            @Override
            public void itemChange( IteratorControlEvent e )
            {
                taxStateModel.setFocus( (Taxon) e.getCurrentObject() );
            }
        } );
        return panel;
    }

    private JButton createListButton()
    {
        return ComponentFactory.createButton( LessonMode.class, "BUTTON.LIST", new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                if ( listPopUp == null )
                {
                    listPopUp = new ListPopUp();
                }
                listPopUp.showPopUp( (Component) e.getSource() );
            }
        } );
    }

    private JButton createOrderButton()
    {
        final JButton button = ComponentFactory.createButton( LessonMode.class, "BUTTON.ORDER", new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                taxStateModel.setOrdered( !taxStateModel.isOrdered() );
            }
        } );
        setOrder( button );
        return button;
    }

    private JButton createModeSwitcher()
    {
        final JButton button = ComponentFactory.createButton( LessonMode.class, "BUTTON.SUBMODUS", new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                taxStateModel.setMode( taxStateModel.getMode() == LERNEN ? ABFRAGEN : LERNEN );
            }
        } );
        setMode( button, LERNEN );
        return button;
    }

    private void initListeners()
    {
        taxStateModel.addPropertyChangeListener( LIST.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent e )
            {
                setListButtonText();
            }
        } );
        taxStateModel.addPropertyChangeListener( RENAME.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent evt )
            {
                setListButtonText();
            }
        } );
        taxStateModel.addPropertyChangeListener( TAXA.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent e )
            {
                taxonControl.setList( taxStateModel.getTaxList() );
                setScrollerState( taxStateModel.getFocus() );
            }
        } );
        taxStateModel.addPropertyChangeListener( FOCUS.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent e )
            {
                setScrollerState( (Taxon) e.getNewValue() );
            }
        } );
        taxStateModel.addPropertyChangeListener( SUB_MODUS.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( final PropertyChangeEvent e )
            {
                setMode( subModusToggle, (Mode) e.getNewValue() );
            }
        } );
        taxStateModel.addPropertyChangeListener( ORDER.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent e )
            {
                setOrder( orderButton );
            }
        } );
        taxStateModel.addPropertyChangeListener( SUB_MODUS.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent e )
            {
                final boolean isQuery = e.getNewValue() == ABFRAGEN;
                final Mode subMode = taxStateModel.getSubMode( taxStateModel.getFocus() );
                final JComponent queryPanel = queryBuilder.getPanel();
                if ( isQuery && subMode != null )
                {
                    editAndQueryPanel.add( queryPanel, CENTER );
                    queryBuilder.requestFocus();
                    editAndQueryPanel.validate();
                    editAndQueryPanel.repaint();
                }
                else
                {
                    editAndQueryPanel.remove( queryPanel );
                    editAndQueryPanel.validate();
                    editAndQueryPanel.repaint();
                }
            }
        } );
        taxStateModel.addPropertyChangeListener( EDIT.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent e )
            {
                final boolean enable = e.getNewValue() == USE;
                orderButton.setEnabled( enable );
                taxonControl.setEnabled( enable );
                subModusToggle.setEnabled( enable );
            }
        } );
    }

    private void setScrollerState( final Taxon taxon )
    {
        taxonControl.updateControlState( taxon );
    }

    private void setListButtonText()
    {
        listButton.setText( Strings.getString( LessonMode.class, "BUTTON.LIST.TEXT", taxStateModel.getModel().toString() ) );
    }

    private void setMode( final JButton button, final Mode subMode )
    {
        button.setText( Strings.getString( LessonMode.class, "BUTTON.SUBMODUS.TEXT", subMode.toString() ) );
    }

    private void setOrder( final JButton button )
    {
        final String orderText = (taxStateModel.isOrdered() ? "BUTTON.ORDER.STATE.SORTED.TEXT" : "BUTTON.ORDER.STATE.RANDOM.TEXT");
        button.setText( Strings.getString( LessonMode.class, orderText ) );
    }

    public class ListPopUp extends ObjectPopup<HerbarModel>
    {
        public ListPopUp()
        {
            super( herbarContext.getModels().toArray( new HerbarModel[herbarContext.getModels().size()] ) );
        }

        public void showPopUp( final Component component )
        {
            setObjects( herbarContext.getModels().toArray( new HerbarModel[herbarContext.getModels().size()] ) );
            showPopUp( component, taxStateModel.getModel() );
        }

        public void itemSelected( final HerbarModel obj )
        {
            herbarContext.setCurrentModel( obj );
            taxStateModel.setModel( obj );
        }
    }
}
