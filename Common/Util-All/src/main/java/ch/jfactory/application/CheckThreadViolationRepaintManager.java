/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.application;

import javax.swing.JComponent;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;

/**
 * TODO: document
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2007/09/27 10:41:22 $
 */
public class CheckThreadViolationRepaintManager extends RepaintManager
{
    // it is recommended to pass the complete check
    private boolean completeCheck = true;

    public boolean isCompleteCheck()
    {
        return completeCheck;
    }

    public void setCompleteCheck( final boolean completeCheck )
    {
        this.completeCheck = completeCheck;
    }

    public synchronized void addInvalidComponent( final JComponent component )
    {
        checkThreadViolations( component );
        super.addInvalidComponent( component );
    }

    public void addDirtyRegion( final JComponent component, final int x, final int y, final int w, final int h )
    {
        checkThreadViolations( component );
        super.addDirtyRegion( component, x, y, w, h );
    }

    private void checkThreadViolations( final JComponent c )
    {
        if ( !SwingUtilities.isEventDispatchThread() && ( completeCheck || c.isShowing() ) )
        {
            final Exception exception = new Exception();
            boolean repaint = false;
            boolean fromSwing = false;
            final StackTraceElement[] stackTrace = exception.getStackTrace();
            for ( final StackTraceElement st : stackTrace )
            {
                if ( repaint && st.getClassName().startsWith( "javax.swing." ) )
                {
                    fromSwing = true;
                }
                if ( "repaint".equals( st.getMethodName() ) )
                {
                    repaint = true;
                }
            }
            if ( repaint && !fromSwing )
            {
                //no problems here, since repaint() is thread safe
                return;
            }
            exception.printStackTrace();
        }
    }
}
