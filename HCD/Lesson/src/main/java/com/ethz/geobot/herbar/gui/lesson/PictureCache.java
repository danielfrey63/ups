/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package com.ethz.geobot.herbar.gui.lesson;

import ch.jfactory.cache.FileImageCache;
import ch.jfactory.cache.ImageCache;
import ch.jfactory.cache.NestedImageCache;
import ch.jfactory.cache.UrlImageCache;
import ch.jfactory.resource.CachedImage;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used as a cache for images. It maintains a queue for pictures to cache and stores them in a HashMap with soft references.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class PictureCache
{
    public static final String WAITING = "WAITING";

    public static final String RESUME = "RESUME";

    /**
     * This class' logger.
     */
    private final static Logger LOG = LoggerFactory.getLogger( PictureCache.class.getName() );

    /**
     * Locator for images.
     */
    private final ImageCache locator;

    /**
     * Caching thread.
     */
    private final CacheImageThread cachingThread;

    /**
     * Hash map storing cached picture.
     */
    private final Map<String, CachedImage> cache = new HashMap<String, CachedImage>();

    /**
     * Task queue, contains images to be cached.
     */
    private final LinkedList<String> queue = new LinkedList<String>();

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport( PictureCache.this );

    private int maxSize = 0;

    private boolean suspend;

    private boolean run = true;

    private boolean wasError = false;

    /**
     * PictureCache taking images from directory
     */
    public PictureCache( final CachingExceptionHandler handler, final ImageCache locator )
    {
        this.locator = locator;
        cachingThread = new CacheImageThread( handler );
        cachingThread.setPriority( Thread.MIN_PRIORITY );
        cachingThread.start();
    }

    /**
     * Clears all entries from caching queue.
     */
    synchronized public void clearCachingList()
    {
        synchronized ( queue )
        {
            final boolean old = queue.size() == 0;
            queue.clear();
            propertyChangeSupport.firePropertyChange( WAITING, old, true );
            maxSize = 0;
        }
        LOG.debug( "caching list cleared" );
    }

    /**
     * Puts an image into the cache.
     *
     * @param name name of the image
     * @return a CachedImage-Object representing the image
     * @see CachedImage
     */
    public CachedImage getCachedImage( final String name )
    {
        CachedImage image = cache.get( name );
        if ( image == null )
        {
            LOG.debug( "adding cached image \"" + name + "\" to cache" );
            image = new CachedImage( locator, name );
            cache.put( name, image );
        }
        return image;
    }

    /**
     * Registers the image in the queue if not loaded already. Allows for small prioritization by indicating that the image has to be put at the first position.
     *
     * @param name  name of the image
     * @param thumb whether it is a thumbnail
     * @param first insert at the start of the list
     */
    public void cacheImage( final String name, final boolean thumb, final boolean first )
    {
        if ( !getCachedImage( name ).isLoaded( thumb ) )
        {
            LOG.trace( "cached image " + name + " not loaded yet" );
            final boolean isNew;
            final boolean old = queue.size() == 0;
            synchronized ( queue )
            {
                isNew = !queue.contains( name );
                if ( isNew )
                {
                    if ( first )
                    {
                        queue.addFirst( name );
                        LOG.debug( "added cached image " + name + " to first position in the queue " + queue );
                    }
                    else
                    {
                        queue.addLast( name );
                        LOG.debug( "added cached image " + name + " to last position in the queue " + queue );
                    }
                }
                else if ( first && !queue.getFirst().equals( name ) )
                {
                    queue.remove( name );
                    queue.addFirst( name );
                    LOG.debug( "moved cached image " + name + " to first position in the queue " + queue );
                }
                maxSize = Math.max( queue.size(), maxSize );
            }
            if ( isNew )
            {
                propertyChangeSupport.firePropertyChange( RESUME, old, true );
                synchronized ( cachingThread )
                {
                    cachingThread.notify();
                }
            }
        }
        else
        {
            LOG.trace( "image " + name + " loaded already" );
        }
    }

    /**
     * Registers the image in the queue if not loaded already. Allows for small prioritization by indicating that the image has to be put at the first position.
     *
     * @param names names of the image
     * @param thumb whether it is a thumbnail
     * @param first insert at the start of the list
     */
    public void cacheImages( final String[] names, final boolean thumb, final boolean first )
    {
        final boolean old = queue.size() == 0;
        for ( final String name : names )
        {
            if ( !getCachedImage( name ).isLoaded( thumb ) )
            {
                LOG.trace( "cached image " + name + " not loaded yet" );
                synchronized ( queue )
                {
                    if ( !queue.contains( name ) )
                    {
                        if ( first )
                        {
                            queue.addFirst( name );
                            LOG.debug( "added cached image " + name + " to first position in the queue " + queue );
                        }
                        else
                        {
                            queue.addLast( name );
                            LOG.debug( "added cached image " + name + " to last position in the queue " + queue );
                        }
                    }
                    else if ( first && !queue.getFirst().equals( name ) )
                    {
                        queue.remove( name );
                        queue.addFirst( name );
                        LOG.debug( "moved cached image " + name + " to first position in the queue " + queue );
                    }
                    maxSize = Math.max( queue.size(), maxSize );
                }
            }
            else
            {
                LOG.trace( "image " + name + " loaded already" );
            }
        }
        if ( queue.size() > 0 )
        {
            propertyChangeSupport.firePropertyChange( RESUME, old, true );
            synchronized ( this )
            {
                System.out.println( Thread.currentThread() );
                cachingThread.setResumed();
            }
        }
    }

    public double getStatus()
    {
        return (double) queue.size() / (double) maxSize;
    }

    public boolean hadError()
    {
        return wasError;
    }

    public void addPropertyChangeListener( final String reason, final PropertyChangeListener listener )
    {
        propertyChangeSupport.addPropertyChangeListener( reason, listener );
    }

    public interface CachingExceptionHandler
    {
        void handleCachingException( final Throwable e );
    }

    public void suspend()
    {
        cachingThread.setSuspended();
    }

    public void resume()
    {
        cachingThread.setResumed();
    }

    public void stop()
    {
        System.out.println( ">>> stopping..." );
        run = false;
        cachingThread.interrupt();
    }

    /**
     * Internal class used to processing the caching image list.
     */
    private class CacheImageThread extends Thread
    {
        private final CachingExceptionHandler handler;

        public CacheImageThread( final CachingExceptionHandler handler )
        {
            this.handler = handler;
            setName( "Caching-Image-Thread" );
        }

        public void run()
        {
            try
            {
                while ( run )
                {
                    final String name = queue.peek();
                    try
                    {
                        if ( name == null || suspend )
                        {
                            synchronized ( this )
                            {
                                LOG.debug( "caching thread waiting" );
                                wait();
                            }
                        }
                        else
                        {
                            final CachedImage img = getCachedImage( name );
                            LOG.debug( "loading cached image \"" + name + "\"" );
                            if ( !img.isLoaded( false ) )
                            {
                                img.loadImage();
                                LOG.debug( "loaded cached image \"" + name + "\"" );
                            }
                            else
                            {
                                LOG.debug( "cached image \"" + name + "\" loading or loaded already" );
                            }
                            // Important to remove the image from the queue also in loaded and exception case
                            removeFromQueue( name );
                        }
                    }
                    catch ( InterruptedException ex )
                    {
                        throw ex;
                    }
                    catch ( Throwable ex )
                    {
                        wasError = true;
                        // Important to remove the image from the queue also in loaded and exception case
                        removeFromQueue( name );
                        if ( handler != null )
                        {
                            handler.handleCachingException( ex );
                        }
                    }
                }
            }
            catch ( InterruptedException ex )
            {
                LOG.info( "caching thread dying" );
            }
        }

        // Lesson learned: notify needs to be in the runnable!
        public synchronized void setResumed()
        {
            suspend = false;
            notify();
        }

        public void setSuspended()
        {
            suspend = true;
        }

        private void removeFromQueue( final String name )
        {
            synchronized ( queue )
            {
                try
                {
                    queue.remove( name );
                    propertyChangeSupport.firePropertyChange( WAITING, false, queue.size() == 0 );
                    LOG.debug( "popped image " + name + " after successful loading from queue " + queue );
                }
                catch ( Exception e )
                {
                    // Ignore
                }
            }
        }
    }

    @Override
    public String toString()
    {
        return "PictureCache[" + locator + "]";
    }

    public static class Main
    {

        private static PictureCache cache;

        public static void main( String[] args )
        {
            cache = new PictureCache( new CachingExceptionHandler()
            {
                @Override
                public void handleCachingException( Throwable e )
                {
                    e.printStackTrace();
                }
            }, new NestedImageCache( new ImageCache[0],
                    new FileImageCache( "C:/Users/Daniel/.hcd2/sc/cache/", "jpg" ),
                    new UrlImageCache( "http://geobot2.ethz.ch/hcd/images-6.0/systematic/", "jpg" ) ) );

            final JFrame frame = new JFrame();
            final BoxLayout layout = new BoxLayout( frame.getContentPane(), BoxLayout.Y_AXIS );
            frame.getContentPane().setLayout( layout );

            final JTextField field = new JTextField( "C:/Users/Daniel/Desktop/list.txt" );
            frame.add( field );
            frame.add( createLoadButton( field ) );
            frame.add( createSuspendButton() );
            frame.add( createResumeButton() );
            frame.add( createStopButton() );

            frame.pack();
            frame.setLocationRelativeTo( null );
            frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
            frame.setVisible( true );
        }

        private static Component createStopButton()
        {
            final JButton button = new JButton( "Stop" );
            button.addActionListener( new ActionListener()
            {
                @Override
                public void actionPerformed( ActionEvent e )
                {
                    cache.stop();
                }
            } );
            return button;
        }

        private static JButton createResumeButton()
        {
            final JButton button = new JButton( "Resume" );
            button.addActionListener( new ActionListener()
            {
                @Override
                public void actionPerformed( ActionEvent e )
                {
                    cache.resume();
                }
            } );
            return button;
        }

        private static JButton createSuspendButton()
        {
            final JButton button = new JButton( "Suspend" );
            button.addActionListener( new ActionListener()
            {
                @Override
                public void actionPerformed( ActionEvent e )
                {
                    cache.suspend();
                }
            } );
            return button;
        }

        private static JButton createLoadButton( final JTextField field )
        {
            final JButton button = new JButton( "Load" );
            button.addActionListener( new ActionListener()
            {
                @Override
                public void actionPerformed( ActionEvent e )
                {
                    try
                    {
                        final String filename = field.getText();
                        final FileReader fileReader = new FileReader( filename );
                        final BufferedReader bufferedReader = new BufferedReader( fileReader );
                        final ArrayList<String> lines = new ArrayList<String>();
                        String line;
                        while ( (line = bufferedReader.readLine()) != null )
                        {
                            lines.add( line );
                        }
                        bufferedReader.close();
                        System.out.println( "loaded " + lines.size() + " pictures" );
                        cache.cacheImages( lines.toArray( new String[lines.size()] ), false, false );
                        System.out.println( "cached all images" );
                    }
                    catch ( IOException ex )
                    {
                        ex.printStackTrace();
                    }
                }
            } );
            return button;
        }
    }
}
