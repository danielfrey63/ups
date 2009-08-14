/* ====================================================================
 *  Copyright 2004-2005 www.xmatrix.ch
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */

package ch.jfactory.application;

import java.util.LinkedList;

/**
 * TODO: Remove this class from CVS of the old directory (E:/Daten/UPS/UST/src/java/ch/xmatrix/ups/ust/main).
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2006/03/14 21:27:56 $
 */
public class WorkQueue
{

    private final LinkedList queue;

    private boolean run = true;

    public WorkQueue(final int nThreads)
    {
        queue = new LinkedList();

        final PoolWorker[] threads = new PoolWorker[nThreads];

        for (int i = 0; i < nThreads; i++)
        {
            threads[i] = new PoolWorker();
            threads[i].start();
            threads[i].setPriority(Thread.MIN_PRIORITY);
        }
    }

    public void execute(final Runnable r)
    {
        synchronized (queue)
        {
            queue.addLast(r);
            queue.notify();
        }
    }

    public void unexecute(final Runnable r)
    {
        synchronized (queue)
        {
            queue.remove(r);
            queue.notify();
        }
    }

    public void destroy()
    {
        run = false;
    }

    private class PoolWorker extends Thread
    {

        public void run()
        {
            Runnable r;

            while (run)
            {
                synchronized (queue)
                {
                    while (queue.isEmpty())
                    {
                        try
                        {
                            queue.wait();
                        }
                        catch (InterruptedException ignored)
                        {
                        }
                    }

                    r = (Runnable) queue.removeFirst();
                }

                // If we don't catch RuntimeException, the pool could leak threads
                try
                {
                    r.run();
                }
                catch (RuntimeException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
