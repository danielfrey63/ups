/*
 * ====================================================================
 *  Copyright 2004-2006 www.xmatrix.ch
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */

package ch.xmatrix.ups.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

/**
 * Delegates space key press and mouse clicks to a common handler method {@link #handleSelection(javax.swing.tree.TreePath)
 * handleSelection(TreePath)}.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2006/04/25 11:08:11 $
 */
public abstract class TreeCheckboxController extends MouseAdapter implements KeyListener
{

    /** The tree to hand the events for. */
    private final JTree tree;

    /** The delegate to implement a key listener. */
    private final KeyListener keyDelegate = new CheckBoxKeyController();

    /**
     * Initializes this object.
     *
     * @param tree the JTree to act uppon
     */
    public TreeCheckboxController(final JTree tree)
    {
        this.tree = tree;
    }

    //--- Interface

    /**
     * Implement to act uppon key and mouse events in the tree.
     *
     * @param path the path the action was emitted.
     */
    protected abstract void handleSelection(TreePath path);

    //--- Utilities

    /**
     * Returns the tree.
     *
     * @return the tree
     */
    protected JTree getTree()
    {
        return tree;
    }

    //--- Implementation

    /**
     * Handles the selection of tree nodes by simple translating the klick point to a tree path and delegating to
     * handleSelection(TreePath).
     *
     * @param e the mouse event
     */
    public void mouseClicked(final MouseEvent e)
    {
        final TreePath path = tree.getPathForLocation(e.getX(), e.getY());
        handleSelection(path);
    }

    /**
     * Simply delegates to the internal controller (CheckBoxKeyController).
     *
     * @param e the key event
     */
    public void keyPressed(final KeyEvent e)
    {
        keyDelegate.keyPressed(e);
    }

    /**
     * Simply delegates to the internal controller (CheckBoxKeyController).
     *
     * @param e the key event
     */
    public void keyReleased(final KeyEvent e)
    {
        keyDelegate.keyReleased(e);
    }

    /**
     * Simply delegates to the internal controller (CheckBoxKeyController).
     *
     * @param e the key event
     */
    public void keyTyped(final KeyEvent e)
    {
        keyDelegate.keyTyped(e);
    }

    /** Key adapter to handle key pressed events and filter spaces. */
    private final class CheckBoxKeyController extends KeyAdapter
    {

        /**
         * Listens for spaces and delegates to handleSelection(TreePath).
         *
         * @param e the key event
         */
        public void keyPressed(final KeyEvent e)
        {
            if (e.getKeyChar() == ' ')
            {
                handleSelection(tree.getSelectionPath());
                e.consume();
            }
        }
    }
}
