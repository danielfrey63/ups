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
package com.ethz.geobot.herbar.gui.lesson;

import ch.jfactory.application.view.border.BevelDirection;
import ch.jfactory.application.view.border.ThinBevelBorder;
import ch.jfactory.application.view.builder.Builder;
import ch.jfactory.component.ComponentFactory;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.Mode;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.FOCUS;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.SUB_MODUS;
import com.ethz.geobot.herbar.model.Taxon;
import java.awt.BorderLayout;
import static java.awt.BorderLayout.CENTER;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import static javax.swing.SwingConstants.HORIZONTAL;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays a text field and an enter button to put guesses of the focused species name. The guesses are appended to
 * the end of this panel.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class TaxonNameInterrogatorBuilder implements Builder
{
    /**
     * This class' logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger( TaxonNameInterrogatorBuilder.class );

    /**
     * Toolbar to display all crumbs.
     */
    private final JToolBar toolBar;

    /**
     * The panel to return from the builder containing the toolbar and the filler (toolbar).
     */
    private final JPanel panel;

    /**
     * The component to show dialogs on.
     */
    private final JFrame parent;

    /**
     * Model holding the mode states.
     */
    private final TaxStateModel taxStateModel;

    /**
     * List of bread crumb panels.
     */
    private List<TaxonNamePanel> taxonNamePanels;

    public TaxonNameInterrogatorBuilder( JFrame parent, TaxStateModel taxStateModel )
    {
        this.parent = parent;
        this.taxStateModel = taxStateModel;

        toolBar = new JToolBar( HORIZONTAL );
        toolBar.setBorder( new CompoundBorder( new ThinBevelBorder( BevelDirection.RAISED ), new EmptyBorder( 0, 0, 1, 0 ) ) );
        toolBar.setFloatable( false );
        toolBar.setRollover( true );
        toolBar.setFocusable( false );

        panel = new JPanel( new BorderLayout() );
        panel.add( toolBar, CENTER );

        setListeners();
    }

    public JComponent getPanel()
    {
        return panel;
    }

    public void setListeners()
    {
        taxStateModel.addPropertyChangeListener( FOCUS.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent evt )
            {
                setTaxFocus();
            }
        } );
        taxStateModel.addPropertyChangeListener( SUB_MODUS.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent evt )
            {
                setTaxFocus();
                final Mode subMode = (Mode) evt.getNewValue();
                for ( final TaxonNamePanel taxonNamePanel : taxonNamePanels )
                {
                    taxonNamePanel.setSubMode( subMode );
                }
            }
        } );
    }

    public void setTaxFocus()
    {
        taxonNamePanels = getTaxonNamePanels();
        toolBar.removeAll();
        toolBar.add( ComponentFactory.createSeparator( 0, 3, 0, 0 ) );
        for ( int i = 0; i < taxonNamePanels.size(); i++ )
        {
            toolBar.add( taxonNamePanels.get( taxonNamePanels.size() - 1 - i ) );
            if ( i < taxonNamePanels.size() - 1 )
            {
                toolBar.add( ComponentFactory.createArrowSeparator( 0, 7, 0, 7 ) );
            }
        }

        toolBar.repaint();
        toolBar.invalidate();
    }

    private List<TaxonNamePanel> getTaxonNamePanels()
    {
        final Mode subMode = taxStateModel.getMode();
        final List<TaxonNamePanel> list = new ArrayList<TaxonNamePanel>();
        final Map<Taxon, Mode> subModes = taxStateModel.getSubModes();
        for ( final Taxon taxon : subModes.keySet() )
        {
            final TaxonNamePanel taxonNamePanel = new TaxonNamePanel( this.parent, taxStateModel, taxon, subMode, list );
            list.add( taxonNamePanel );
        }
        Collections.reverse( list );
        return list;
    }
}
