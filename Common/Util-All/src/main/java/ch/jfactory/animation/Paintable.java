/* ====================================================================
 *  Copyright 2004-2005 www.xmatrix.ch
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */

package ch.jfactory.animation;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract paintable to be used with {@link AnimationQueue}.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
public abstract class Paintable implements Runnable
{
    /**
     * The list of {@link ch.jfactory.animation.StopListener}s that will be notified upon termination of this painting
     * thread.
     */
    private final List stopListeners = new ArrayList();

    /**
     * The associated {@link AnimationQueue}.
     */
    private transient AnimationQueue animation;

    /**
     * The painting thread.
     */
    private transient Thread runner;

    /**
     * The state of the painting thread. If set to false, the thread will stop.
     */
    private transient boolean run;

    /**
     * Register animation queue.
     *
     * @param animation the queue to register
     */
    public void setAnimationQueue( final AnimationQueue animation )
    {
        this.animation = animation;
    }

    /**
     * Start background repainting thread.
     */
    public void start()
    {
        run = true;

        if ( runner == null )
        {
            runner = new Thread( this );
            runner.setPriority( Thread.MAX_PRIORITY );
            runner.start();
        }
    }

    /**
     * Stop background repainting thread.
     */
    public void stop()
    {
        run = false;

        for ( final Object stopListener1 : stopListeners )
        {
            final StopListener stopListener = (StopListener) stopListener1;
            stopListener.stopPerformed();
        }
    }

    /**
     * Register listener that is notified upon stop of a paintable.
     *
     * @param stopListener the listener to notify
     */
    public void addStopListener( final StopListener stopListener )
    {
        stopListeners.add( stopListener );
    }

    /**
     * Implement this method to do the actual painting stuff.
     *
     * @param g the graphics object
     */
    public abstract void paint( Graphics2D g );

    /**
     * Returns the animation queue associated with this paintable.
     *
     * @return the animation queue
     */
    protected AnimationQueue getAnimation()
    {
        return animation;
    }

    /**
     * Returns the painting thread associated.
     *
     * @return the painting thread
     */
    protected Thread getRunner()
    {
        return runner;
    }

    /**
     * <p>Returns the running state of the paining thread. If false, the thread will die or has died.</p> <p>To set the
     * running state to false, use {@link #stop}, to set it to true {@link #start}.
     *
     * @return the running state
     */
    protected boolean isRun()
    {
        return run;
    }
}
