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
package com.ethz.geobot.herbar.gui.picture;

import ch.jfactory.cache.ImageCache;
import ch.jfactory.cache.ImageCacheException;
import ch.jfactory.cache.ImageRetrieveException;
import ch.jfactory.component.Dialogs;
import ch.jfactory.image.PictureDetailPanel;
import ch.jfactory.resource.PictureCache;
import com.ethz.geobot.herbar.model.PictureTheme;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JTabbedPane;
import org.apache.log4j.Logger;

import static ch.jfactory.resource.ImageLocator.PICT_LOCATOR;
import static java.lang.System.currentTimeMillis;

public class TabbedPictureDetailPanel extends JTabbedPane
{
    /** This class logger. */
    private static final Logger LOG = Logger.getLogger( TabbedPictureDetailPanel.class );

    private final PictureCache cache;

    private final ArrayList<PictureTheme> list = new ArrayList<PictureTheme>();

    public TabbedPictureDetailPanel()
    {
        this.cache = new PictureCache( new PictureCache.CachingExceptionHandler()
        {
            private long lastSameCause = 0;

            private ImageCacheException last;

            public void handleCachingException( final Throwable e )
            {
                if ( e instanceof ImageCacheException && !( e instanceof ImageRetrieveException ) )
                {
                    final ImageCacheException imageCacheException = (ImageCacheException) e;
                    final String message;
                    final Throwable cause = e.getCause();
                    if ( cause instanceof IOException )
                    {
                        message = cause.getMessage();
                    }
                    else
                    {
                        message = e.getMessage();
                    }
                    if ( e.equals( last ) )
                    {
                        final long delta = currentTimeMillis() - lastSameCause;
                        if ( delta < 2000 )
                        {
                            LOG.warn( "skipped exception feedback as last same exception occurred only " + delta + "ms before" );
                        }
                        else
                        {
                            displayDialog( message, imageCacheException.getFreeSpace(), imageCacheException.getImageCache() );
                        }
                        lastSameCause = currentTimeMillis();
                    }
                    else
                    {
                        displayDialog( message, imageCacheException.getFreeSpace(), imageCacheException.getImageCache() );
                        last = imageCacheException;
                    }
                }
                else
                {
                    Dialogs.showErrorMessage( TabbedPictureDetailPanel.this, "Ein unerwarteter Fehler ist beim Zwischenspeichern von Bildern aufgetreten", e.getMessage() );
                    LOG.error( "unexpected exception", e );
                }
            }

            private void displayDialog( final String message, final long freeSpace, final ImageCache cache )
            {
                if ( freeSpace < 1024 * 100 )
                {
                    final ErrorHandlingDialog dialog = new ErrorHandlingDialog();
                    dialog.setLocationRelativeTo( null );
                    dialog.setVisible( true );
                    if ( dialog.getDoNothing().isSelected() )
                    {
                        LOG.info( "user has chosen to do nothing" );
                    }
                    else if ( dialog.getDeletePercentageCheckBox().isSelected() )
                    {
                        LOG.info( "user has chosen to invalidate the cache" );
                        try
                        {
                            PICT_LOCATOR.invalidateCache( cache );
                        }
                        catch ( final ImageCacheException e )
                        {
                            LOG.error( "can not invalidate caches", e );
                        }
                    }
                    else if ( dialog.getRunInMemoryCheckBox().isSelected() )
                    {
                        LOG.info( "user has chosen to run in memory only" );
                        PICT_LOCATOR.disableCache( cache );
                    }
                }
                else
                {
                    Dialogs.showErrorMessage( TabbedPictureDetailPanel.this, "Fehler beim Zwischenspeichern von Bildern", message );
                }
            }
        } );
    }

    public PictureDetailPanel getDetail( final int i )
    {
        return (PictureDetailPanel) getComponentAt( i );
    }

    public PictureDetailPanel addTab( final PictureTheme theme )
    {
        LOG.debug( "initializing picture details panel for " + theme + " with cache " + cache );
        final PictureDetailPanel panel = new PictureDetailPanel( cache );
        this.add( panel, theme.getName() );
        list.add( theme );
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

    public void setEnabled( final int t, final boolean b )
    {
        final PictureTheme theme = getObjectAt( t );
        final String color = b ? "000000" : "999999";
        this.setTitleAt( t, "<html><body><font color='#" + color + "'>" + theme.getName() + "</font></body></html>" );
    }

    public PictureDetailPanel getThemePanel( final PictureTheme t )
    {
        return getDetail( getObjectIndex( t ) );
    }
}
