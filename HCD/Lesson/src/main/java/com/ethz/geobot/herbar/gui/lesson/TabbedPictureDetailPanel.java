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

import com.ethz.geobot.herbar.gui.picture.PictureCache;
import com.ethz.geobot.herbar.model.PictureTheme;
import java.util.ArrayList;
import javax.swing.JTabbedPane;
import org.apache.log4j.Logger;

public class TabbedPictureDetailPanel extends JTabbedPane
{
    /** This class logger. */
    private static final Logger LOG = Logger.getLogger( TabbedPictureDetailPanel.class );

    private final PictureCache cache;

    private final ArrayList<PictureTheme> list = new ArrayList<PictureTheme>();

    public TabbedPictureDetailPanel( final PictureCache cache )
    {
        this.cache = cache;
    }

    public PictureDetailPanel getDetail( final int i )
    {
        return (PictureDetailPanel) getComponentAt( i );
    }

    public PictureDetailPanel addTab( final PictureTheme theme )
    {
        LOG.debug( "initializing picture details panel for " + theme + " with cache " + cache );
        final PictureDetailPanel panel = new PictureDetailPanel( cache );
        list.add( theme );
        this.add( panel, theme.getName() );
        return panel;
    }

    private PictureTheme getObjectAt( final int i )
    {
        return list.get( i );
    }

    private int getObjectIndex( final PictureTheme theme )
    {
        for ( int i = 0; i < list.size(); i++ )
        {
            if ( theme.equals( getObjectAt( i ) ) )
            {
                return i;
            }
        }
        return -1;
    }

    public void clearAll()
    {
        for ( int i = 0; i < list.size(); i++ )
        {
            getDetail( i ).clear();
        }
    }

    public void setEnabled( final int index, final boolean b )
    {
        final PictureTheme theme = getObjectAt( index );
        final String color = b ? "000000" : "999999";
        final String title;
        if ( index == 0 )
        {
            title = "<html><body><font color='#" + color + "'><b>" + theme.getName() + "</b></font></body></html>";
        }
        else
        {
            title = "<html><body><font color='#" + color + "'>" + theme.getName() + "</font></body></html>";
        }
        setTitleAt( index, title );
    }

    public PictureDetailPanel getThemePanel( final PictureTheme t )
    {
        return getDetail( getObjectIndex( t ) );
    }

    @Override
    public void setSelectedIndex( int newIndex )
    {
        final int oldIndex = getSelectedIndex();
        if ( oldIndex >= 0 && oldIndex < list.size() )
        {
            final String oldName = getObjectAt( oldIndex ).getName();
            final String oldTitle = getTitleAt( oldIndex );
            final String oldDeselectedTitle = oldTitle.replace( "<b>" + oldName + "</b>", oldName );
            setTitleAt( oldIndex, oldDeselectedTitle );
        }
        super.setSelectedIndex( newIndex );
        if ( newIndex >= 0 && newIndex < list.size() )
        {
            final String newName = getObjectAt( newIndex ).getName();
            final String newTitle = getTitleAt( newIndex );
            final String newSelectedTitle;
            if ( newName != null && newName.equals( newTitle ) )
            {
                newSelectedTitle = "<html><body><font color='#000000'><b>" + newName + "</b></font></body></html>";
            }
            else
            {
                newSelectedTitle = newTitle.replace( newName, "<b>" + newName + "</b>" );
            }
            setTitleAt( newIndex, newSelectedTitle );
        }
    }
}
