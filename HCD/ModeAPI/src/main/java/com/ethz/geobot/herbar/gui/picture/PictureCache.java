/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package com.ethz.geobot.herbar.gui.picture;

import ch.jfactory.cache.FileImageCache;
import ch.jfactory.cache.ImageCache;
import ch.jfactory.cache.NestedImageCache;
import ch.jfactory.cache.UrlImageCache;
import ch.jfactory.resource.CachedImage;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
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
import javax.swing.SwingWorker;
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

    public static final String FINISHED = "FINISHED";

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

    private boolean wasError = false;

    /**
     * PictureCache taking images from directory
     */
    public PictureCache( final String name, final CachingExceptionHandler handler, final ImageCache locator )
    {
        this.locator = locator;
        cachingThread = new CacheImageThread( name, handler );
        cachingThread.execute();
    }

    public String getName()
    {
        return cachingThread.getName();
    }

    /**
     * Clears all entries from caching queue.
     */
    synchronized public void clearCachingList()
    {
        synchronized ( queue )
        {
            queue.clear();
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
            LOG.trace( "adding cached image \"" + name + "\" to cache" );
            image = new CachedImage( locator, name );
            cache.put( name, image );
        }
        return image;
    }

    /**
     * Registers the image in the queue if not loaded already. Allows for small prioritization by indicating that the image has to be put at the first position.
     *
     * @param name   name of the image
     * @param thumb  whether it is a thumbnail
     * @param first  insert at the start of the list
     * @param resume resume caching thread after queueing images
     */
    public void queueImage( final String name, final boolean thumb, final boolean first, boolean resume )
    {
        internalCacheImage( name, thumb, first );
        if ( queue.size() > 0 && resume )
        {
            LOG.debug( "resuming " + cachingThread.getName() + " after queueing image" );
            cachingThread.setResumed();
        }
    }

    /**
     * Registers the image in the queue if not loaded already. Allows for small prioritization by indicating that the image has to be put at the first position.
     *
     * @param names  names of the image
     * @param thumb  whether it is a thumbnail
     * @param first  insert at the start of the list
     * @param resume resume caching thread after queueing images
     */
    public void queueImages( final String[] names, final boolean thumb, final boolean first, boolean resume )
    {
        for ( final String name : names )
        {
            internalCacheImage( name, thumb, first );
        }
        if ( queue.size() > 0 && resume )
        {
            LOG.debug( "resuming " + cachingThread.getName() + " after queueing images" );
            cachingThread.setResumed();
        }
    }

    private void internalCacheImage( String name, boolean thumb, boolean first )
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
                        LOG.trace( "added cached image " + name + " to last position in the queue " + queue );
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

    public void removePropertyChangeListener( final String type, final PropertyChangeListener listener )
    {
        propertyChangeSupport.removePropertyChangeListener( type, listener );
    }

    public void suspend()
    {
        if ( cachingThread.isResumed() )
        {
            cachingThread.setSuspended();
        }
    }

    public void resume()
    {
        if ( cachingThread.isSuspended() )
        {
            cachingThread.setResumed();
        }
    }

    public void stop()
    {
        LOG.info( "stopping " + cachingThread.getName() );
        cachingThread.setRunning( false );
    }

    /**
     * Internal class used to processing the caching image list.
     */
    public class CacheImageThread extends SwingWorker<String, Float>
    {
        private final CachingExceptionHandler handler;

        private final String name;

        private boolean nameSet = false;

        private boolean suspended = true;

        private boolean running = true;

        public CacheImageThread( final String name, final CachingExceptionHandler handler )
        {
            this.name = name;
            this.handler = handler;
            LOG.debug( "initialized " + name + " with suspend=" + suspended );
        }

        public String getName()
        {
            return name;
        }

        public String doInBackground()
        {
            try
            {
                if ( !nameSet )
                {
                    Thread.currentThread().setName( name );
                    nameSet = true;
                }
                while ( running )
                {
                    String name = queue.peek();
                    try
                    {
                        if ( name == null )
                        {
                            suspended = true;
                        }
                        synchronized ( this )
                        {
                            while ( suspended )
                            {
                                LOG.debug( getName() + " suspended by flag" );
                                propertyChangeSupport.firePropertyChange( WAITING, false, true );
                                wait();
                                LOG.debug( getName() + " resumed by notify" + (suspended ? " AND STILL SUSPENDED!" : "") );
                                propertyChangeSupport.firePropertyChange( RESUME, false, true );
                                // Make sure to peek on the queue again as in the meantime it might has been filled
                                name = queue.peek();
                            }
                        }
                        if ( name == null )
                        {
                            propertyChangeSupport.firePropertyChange( FINISHED, false, true );
                        }
                        else
                        {
                            final CachedImage img = getCachedImage( name );
                            LOG.trace( "loading cached image \"" + name + "\"" );
                            if ( !img.isLoaded( false ) )
                            {
                                img.loadImage();
                                LOG.trace( "loaded cached image \"" + name + "\"" );
                            }
                            else
                            {
                                LOG.trace( "cached image \"" + name + "\" loading or loaded already" );
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
                propertyChangeSupport.firePropertyChange( FINISHED, false, true );
            }
            catch ( InterruptedException ex )
            {
                suspended = true;
                LOG.info( getName() + " stopped" );
            }
            return "done";
        }

        public void setRunning( final boolean running )
        {
            this.running = running;
        }

        // Lesson learned: notify needs to be in the runnable!
        public synchronized void setResumed()
        {
            if ( suspended )
            {
                suspended = false;
                notify();
            }
        }

        public boolean isResumed()
        {
            return !suspended;
        }

        public void setSuspended()
        {
            suspended = true;
        }

        public boolean isSuspended()
        {
            return suspended;
        }

        private void removeFromQueue( final String name )
        {
            synchronized ( queue )
            {
                try
                {
                    queue.remove( name );
                    //propertyChangeSupport.firePropertyChange( WAITING, false, queue.size() == 0 );
                    LOG.trace( "popped image " + name + " after successful loading from queue " + queue );
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
        private static PictureCache front;
        private static PictureCache back;

        public static void main( String[] args )
        {
            front = new PictureCache( "Manual-Thread", new CachingExceptionHandler()
            {
                @Override
                public void handleCachingException( Throwable e )
                {
                    e.printStackTrace();
                }
            }, new NestedImageCache( new ImageCache[0],
                    new FileImageCache( "C:/Users/Daniel/.hcd2/sc/cache/", "jpg" ),
                    new UrlImageCache( "http://balti.ethz.ch/hcd/images-6.0/systematic/", "jpg" ) ) );
            back = new PictureCache( "Background-Thread", new CachingExceptionHandler()
            {
                @Override
                public void handleCachingException( Throwable e )
                {
                    e.printStackTrace();
                }
            }, new NestedImageCache( new ImageCache[0],
                    new FileImageCache( "C:/Users/Daniel/.hcd2/sc/cache/", "jpg" ),
                    new UrlImageCache( "http://balti.ethz.ch/hcd/images-6.0/systematic/", "jpg" ) ) );

            front.addPropertyChangeListener( RESUME, new PropertyChangeListener()
            {
                @Override
                public void propertyChange( PropertyChangeEvent evt )
                {
                    LOG.debug( "suspending " + back.getName() + " by RESUME of " + front.getName() );
                    back.suspend();
                }
            } );
            front.addPropertyChangeListener( WAITING, new PropertyChangeListener()
            {
                @Override
                public void propertyChange( PropertyChangeEvent evt )
                {
                    LOG.debug( "resuming " + back.getName() + " by WAITING of " + front.getName() );
                    back.resume();
                }
            } );
            front.addPropertyChangeListener( FINISHED, new PropertyChangeListener()
            {
                @Override
                public void propertyChange( PropertyChangeEvent evt )
                {
                    LOG.debug( "resuming " + back.getName() + " by FINISHED of " + front.getName() );
                    back.resume();
                }
            } );

            final JFrame frame = new JFrame();
            final BoxLayout layout = new BoxLayout( frame.getContentPane(), BoxLayout.Y_AXIS );
            frame.getContentPane().setLayout( layout );

            frame.add( createLoadButton() );
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
                    front.stop();
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
                    LOG.debug( "resuming " + front.getName() + " after pushing \"Resume\" button" );
                    front.resume();
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
                    front.suspend();
                }
            } );
            return button;
        }

        private static JButton createLoadButton()
        {
            final JButton button = new JButton( "Load" );
            button.addActionListener( new ActionListener()
            {
                @Override
                public void actionPerformed( ActionEvent e )
                {
                    try
                    {
                        loadFiles( "C:/Users/Daniel/Desktop/list.txt", front, back );
                        loadFiles( "C:/Users/Daniel/Desktop/list2.txt", back, front );
                    }
                    catch ( IOException ex )
                    {
                        ex.printStackTrace();
                    }
                }

                private void loadFiles( final String filename, final PictureCache cache, final PictureCache other ) throws IOException
                {
                    final FileReader fileReader = new FileReader( filename );
                    final BufferedReader bufferedReader = new BufferedReader( fileReader );
                    final ArrayList<String> lines = new ArrayList<String>();
                    String line;
                    while ( (line = bufferedReader.readLine()) != null )
                    {
                        lines.add( line );
                    }
                    bufferedReader.close();
                    LOG.info( "queueing " + lines.size() + " files for " + cache.getName() );
                    cache.queueImages( lines.toArray( new String[lines.size()] ), false, false, other.getStatus() > 0 );
                    LOG.info( "queued " + lines.size() + " files for " + cache.getName() );
                }
            } );
            return button;
        }
    }
}
