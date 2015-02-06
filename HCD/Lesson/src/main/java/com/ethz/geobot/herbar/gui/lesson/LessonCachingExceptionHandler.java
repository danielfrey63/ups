package com.ethz.geobot.herbar.gui.lesson;

import ch.jfactory.application.view.dialog.I15nComponentDialog;
import ch.jfactory.application.view.dialog.ListDialog;
import ch.jfactory.cache.ImageCache;
import ch.jfactory.cache.ImageCacheException;
import ch.jfactory.cache.ImageRetrieveException;
import ch.jfactory.component.Dialogs;
import static ch.jfactory.resource.ImageLocator.PICT_LOCATOR;
import com.ethz.geobot.herbar.gui.picture.ErrorHandlingDialog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import org.apache.log4j.Logger;

class LessonCachingExceptionHandler implements PictureCache.CachingExceptionHandler
{
    private static final Logger LOG = Logger.getLogger( LessonCachingExceptionHandler.class );

    private final List<String> exceptions = new ArrayList<String>();

    private JFrame parent;

    private long freeSpace = -1L;

    public LessonCachingExceptionHandler( final JFrame parent )
    {
        this.parent = parent;
    }

    public void handleCachingException( final Throwable e )
    {
        if ( e instanceof ImageCacheException )
        {
            final Throwable cause = e.getCause();
            exceptions.add( cause instanceof IOException ? cause.getMessage() : e.getMessage() );
            freeSpace = ((ImageCacheException) e).getFreeSpace();
        }
        else if ( e instanceof ImageRetrieveException )
        {
            exceptions.add( e.getMessage() );
        }
        else
        {
            Dialogs.showErrorMessage( null, "Ein unerwarteter Fehler ist beim Zwischenspeichern von Bildern aufgetreten", e.getMessage() );
            LOG.error( "unexpected exception", e );
        }
    }

    public void displayDialog( final ImageCache cache )
    {
        if ( freeSpace < 1024 * 100 && freeSpace >= 0 )
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
                catch ( final ImageRetrieveException e )
                {
                    LOG.error( "can not invalidate cache", e );
                }
            }
            else if ( dialog.getRunInMemoryCheckBox().isSelected() )
            {
                LOG.info( "user has chosen to run in memory only" );
                PICT_LOCATOR.disableCache( cache );
            }
        }
        else if (exceptions.size() > 0)
        {
            final ListDialog<String> dialog = new ListDialog<String>( parent, "DOWNLOAD.ERROR", exceptions.toArray( new String[exceptions.size()] ) )
            {
                @Override
                protected boolean isApplyShowing()
                {
                    return false;
                }
            };
            dialog.setSize( 600, 600 );
            dialog.setLocationRelativeTo( null );
            dialog.setVisible( true );
        }
    }
}
