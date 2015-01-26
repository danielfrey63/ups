package com.ethz.geobot.herbar.gui.lesson;

import ch.jfactory.cache.ImageCache;
import ch.jfactory.cache.ImageCacheException;
import ch.jfactory.cache.ImageRetrieveException;
import ch.jfactory.component.Dialogs;
import static ch.jfactory.resource.ImageLocator.PICT_LOCATOR;
import com.ethz.geobot.herbar.gui.picture.ErrorHandlingDialog;
import java.io.IOException;
import static java.lang.System.currentTimeMillis;
import javax.swing.JComponent;
import org.apache.log4j.Logger;

/**
* Created by Daniel on 25.01.2015.
*/
class LessonCachingExceptionHandler implements PictureCache.CachingExceptionHandler
{
    private static final Logger LOG = Logger.getLogger( LessonCachingExceptionHandler.class );

    private long lastSameCause = 0;

    private ImageCacheException last;
    private JComponent parent;

    LessonCachingExceptionHandler( final JComponent parent )
    {
        this.parent = parent;
    }

    public void handleCachingException( final Throwable e )
    {
        if ( e instanceof ImageCacheException && !(e instanceof ImageRetrieveException) )
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
            Dialogs.showErrorMessage( parent, "Ein unerwarteter Fehler ist beim Zwischenspeichern von Bildern aufgetreten", e.getMessage() );
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
            Dialogs.showErrorMessage( parent, "Fehler beim Zwischenspeichern von Bildern", message );
        }
    }
}
