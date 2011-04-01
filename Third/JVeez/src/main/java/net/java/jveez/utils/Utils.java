/*
 * This project is made available under the terms of the BSD license, more information can be found at : http://www.opensource.org/licenses/bsd-license.html
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * - Neither the name of the java.net website nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright (c) 2004, The JVeez Project Team.
 * All rights reserved.
 */
package net.java.jveez.utils;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileSystemView;
import net.java.jveez.cache.ImageStore;
import net.java.jveez.cache.ThumbnailStore;
import org.apache.log4j.Logger;

public class Utils
{
    private static final Logger LOG = Logger.getLogger( Utils.class );

    public static final CustomUncaughtExceptionHandler UNCAUGHT_EXCEPTION_HANDLER = new CustomUncaughtExceptionHandler();

    private static final ExecutorService executor = Executors.newFixedThreadPool( 1, newPriorityThreadFactory( Thread.NORM_PRIORITY ) );

    public static Icon loadIcon( final String resourceName )
    {
        final URL url = Utils.class.getClassLoader().getResource( resourceName );
        return new ImageIcon( url );
    }

    public static BufferedImage loadImage( final String resourceName )
    {
        final URL url = Utils.class.getClassLoader().getResource( resourceName );
        try
        {
            return ImageIO.read( url );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            return null;
        }
    }

    public static void executeAsyncIfDisptachThread( final Runnable runnable )
    {
        if ( SwingUtilities.isEventDispatchThread() )
        {
            executor.execute( runnable );
        }
        else
        {
            runnable.run();
        }
    }

    public static void executeAsync( final Runnable runnable )
    {
        executor.execute( runnable );
    }

    public static void executeWithDisptachThread( final Runnable runnable )
    {
        if ( SwingUtilities.isEventDispatchThread() )
        {
            runnable.run();
        }
        else
        {
            SwingUtilities.invokeLater( runnable );
        }
    }

    public static boolean isSupportedImage( final File file )
    {
        final String fileName = file.getName().toLowerCase();
        return fileName.endsWith( ".jpg" ) ||
                fileName.endsWith( ".jpeg" ) ||
                fileName.endsWith( ".gif" ) ||
                fileName.endsWith( ".png" ) ||
                fileName.endsWith( ".bmp" );
    }

    public static boolean isSupportedExifImage( final File file )
    {
        final String fileName = file.getName().toLowerCase();
        return fileName.endsWith( ".jpg" ) ||
                fileName.endsWith( ".jpeg" );
    }

    public static Icon getSystemIconForFile( final File file )
    {
        return FileSystemView.getFileSystemView().getSystemIcon( file );
    }

    public static Rectangle fitToParent( final Rectangle parent, final Rectangle child, final Rectangle target )
    {
        final float scale = Math.min( (float) parent.width / (float) child.width, (float) parent.height / (float) child.height );
        target.width = (int) ( scale * child.width );
        target.height = (int) ( scale * child.height );
        target.x = ( parent.width - target.width ) / 2;
        target.y = ( parent.height - target.height ) / 2;
        return target;
    }

    public static int countOccurencesOf( final String s, final char c )
    {
        if ( s == null || s.length() == 0 )
        {
            return 0;
        }

        final int len = s.length();
        int occurences = 0;
        for ( int i = 0; i < len; i++ )
        {
            if ( s.charAt( i ) == c )
            {
                occurences++;
            }
        }

        return occurences;
    }

    public static ThreadFactory newPriorityThreadFactory( final int priority )
    {
        return new PriorityThreadFactory( priority );
    }

    public static void freeMemory( final boolean hard )
    {
        LOG.debug( "freeMemory(" + hard + ")" );

        ImageStore.getInstance().invalidateCache();
        if ( hard )
        {
            ThumbnailStore.getInstance().invalidateCache();
        }
        System.runFinalization();
        System.gc();
    }

    private static class PriorityThreadFactory implements ThreadFactory
    {
        private final int priority;

        public PriorityThreadFactory( final int priority )
        {
            this.priority = priority;
        }

        public Thread newThread( final Runnable r )
        {
            final Thread t = Executors.defaultThreadFactory().newThread( r );
            t.setPriority( priority );
            t.setUncaughtExceptionHandler( UNCAUGHT_EXCEPTION_HANDLER );
            return t;
        }
    }

    private static class CustomUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler
    {
        public void uncaughtException( final Thread t, final Throwable e )
        {
            LOG.error( "Thread " + t.getName() + " has thrown an unexpected exception !", e );
        }
    }
}
