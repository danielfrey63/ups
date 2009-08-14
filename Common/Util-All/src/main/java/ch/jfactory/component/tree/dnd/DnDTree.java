/* ====================================================================
 *  Copyright 2004 www.jfactory.ch
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.
 * ====================================================================
 */
package ch.jfactory.component.tree.dnd;

import ch.jfactory.color.ColorUtils;
import ch.jfactory.component.tree.MutableTreeModel;
import ch.jfactory.component.tree.TreeExpandedRestorer;
import ch.jfactory.image.ArrowImage;
import ch.jfactory.image.ImageUtils;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.SystemColor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.Autoscroll;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceAdapter;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.Timer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * Implementation for a basic drag and drop tree. The tree supports dragging within itself. It doesn't support copying
 * of nodes.
 *
 * <p/>The implementation is based on an article of Andrew J. Armstrong published in <a
 * href="http://www.javaworld.com/javaworld/javatips/jw-javatip114.html">JavaWorld</a>. However, different aspects have
 * been improved and the code has been refactored. Find the original comment at the end of this documentation.
 *
 * <p/>This implementation of a drag and drop tree is based on five drag use cases. All cases are based on a virtual
 * subdivision of a node into three different areas:
 *
 * <p/><img src="DnDTree-0.png"/>
 *
 * <p/>The five use cases are as follows:
 *
 * <p/><code>1:</code>
 *
 * <p/><img src="DnDTree-1.png"/>
 *
 * <p/>The node (X) is dragged to another node (A). If the drag position is over the middle half of the node, then the
 * whole node is marked to indicate that the dragged node (X) will be droped into the marked one (A).
 *
 * <p/><code>2:</code>
 *
 * <p/><img src="DnDTree-2.png"/>
 *
 * <p/>The node (X) is dragged between two sister nodes (A and B). The drag position may be in the lower quart of the
 * upper (A) or the upper quart of the lower sister node (B). In this case, the node is inserted between the two sister
 * nodes.
 *
 * <p/><code>3:</code>
 *
 * <p/><img src="DnDTree-3.png"/>
 *
 * <p/>The node (X) is dragged to the end of a series of children (A). If the drag posistion is located in the upper
 * nodes (A) lower quart, the node is inserted as last child of these childrens parent. The arrow indicates at what
 * level it will be inserted.
 *
 * <p/><code>4:</code>
 *
 * <p/><img src="DnDTree-4.png"/>
 *
 * <p/>The node (X) is dragged to the end of a series of children (A). If the drag position is located in the lower
 * nodes (B) upper quart, the node is inserted as an adjacent sister to the childrens (A) parent. That's the same as to
 * insert it as a preceding sister to the node at the drag position. Note that the arrow is located at the begining of
 * node at the drag position, not at the childrens begining to indicate at what level it will be inserted. If node B is
 * the root node, pointing to the upper quart has no effect. Instead the tree reacts as if the mouse were over the
 * middle half of the node. If node A is the last node in the tree, make sure to have some white space left to be able
 * to drag beyound node A.
 *
 * <p/><code>5:</code>
 *
 * <p/><img src="DnDTree-5.png"/>
 *
 * <p/>The node (X) is dragged to the first position of a series of children and inserted at this position. The drag
 * position may be located in the upper quart of the first child (B) or lower quart of the parent node (A).
 *
 * <p/>Todo: There are still some detail use cases that do not work 100 percent correctly: (a) if a node is at the last
 * position with a parent, draging down does not always allow a hierarchial 'stepping up'.
 *
 * <p/>Original comment of Andrew J. Armstrong: "Demonstrates how to display a 'drag image' when using drag and drop on
 * those platforms whose JVMs do not support it natively (eg Win32). Some of the code was borrowed by the original
 * author from the book: Java Swing, by Robert Eckstein, Marc Loy & Dave Wood, Paperback - 1221 pages 1 Ed edition
 * (September 1998), O'Reilly & Associates, ISBN 156592455X. The <a href="http://www.oreilly.com/catalog/jswing/chapter/dnd.beta.pdf">
 * relevant chapter</a> can be found online."
 *
 * @author Daniel Frey
 */
public class DnDTree extends JTree implements Autoscroll
{

    private static final boolean DEBUG = false;

    /** The margin at which the scrolling starts. */
    private static final int AUTOSCROLL_MARGIN = 30;

    /** The 'drag image' */
    protected BufferedImage ghost;

    /** The path being dragged */
    protected TreePath sourcePath;

    /** Where, in the drag image, the mouse was clicked */
    protected Point offset = new Point();

    /** The notfiable tree model to use. */
    private MutableTreeModel mutable;

    /** The drag source handler. */
    private final DragSourceHandler dragSourceHandler = new DragSourceHandler();

    /** The drag gesture recognizer. */
    private final DragGestureHandler dragGestureHandler = new DragGestureHandler();

    /**
     * Instantiates a drag and drop tree.
     *
     * @param mutable   the NotifiableTreeModel
     * @param validator the DnDValidatorUpdater to validate the tree actions. May not be null.
     */
    public DnDTree(final MutableTreeModel mutable, final DnDValidatorUpdater validator)
    {
        super((TreeModel) mutable);
        setModel((TreeModel) mutable);
        // Make this JTree a drag source
        final DragSource dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, new DragGestureHandler());
        // Also, make this JTree a drag target
        final DropTarget dropTarget = new DropTarget(this, new DropTargetHandler(validator));
        dropTarget.setDefaultActions(DnDConstants.ACTION_MOVE);
    }

    // Overrides

    /**
     * Sets the tree model.
     *
     * @param model the TreeModel to set. Must be a NotifiableTreeModel.
     * @throws IllegalArgumentException if the model is not an instance of NotifiableTreeModel.
     */
    public void setModel(final TreeModel model)
    {
        if (model instanceof MutableTreeModel)
        {
            final MutableTreeModel notifiable = (MutableTreeModel) model;
            this.mutable = notifiable;
            super.setModel((TreeModel) notifiable);
        }
        else
        {
            throw new IllegalArgumentException("model must be an instance of NotifiableTreeModel");
        }
    }

    public void paintComponent(final java.awt.Graphics g)
    {
        super.paintComponent(g);
        if (DEBUG)
        {
            final Rectangle raOuter = getBounds();
            final Rectangle raInner = getParent().getBounds();
            g.setColor(Color.red);
            g.drawRect(-raOuter.x + AUTOSCROLL_MARGIN, -raOuter.y + AUTOSCROLL_MARGIN,
                    raInner.width - (2 * AUTOSCROLL_MARGIN), raInner.height - (2 * AUTOSCROLL_MARGIN));
        }
    }

    // Autoscroll interface

    /** Calculate the insets for the JTree, not the viewport the tree is in. This makes it a bit messy. */
    public Insets getAutoscrollInsets()
    {
        final Rectangle raOuter = getBounds();
        final Rectangle raInner = getParent().getBounds();
        return new Insets(raInner.y - raOuter.y + AUTOSCROLL_MARGIN,
                raInner.x - raOuter.x + AUTOSCROLL_MARGIN,
                raOuter.height - raInner.height - raInner.y + raOuter.y + AUTOSCROLL_MARGIN,
                raOuter.width - raInner.width - raInner.x + raOuter.x + AUTOSCROLL_MARGIN);
    }

    /** Ok, we’ve been told to scroll because the mouse cursor is in our scroll zone. */
    public void autoscroll(final Point pt)
    {
        // Figure out which row we’re on.
        final int actualRow = getClosestRowForLocation(pt.x, pt.y);
        // If we are not on a row then ignore this autoscroll request
        if (actualRow < 0)
        {
            return;
        }
        final Rectangle raOuter = getBounds();
        // Now decide if the row is at the top of the screen or at the bottom. We do this to make the previous row (or
        // the next row) visible as appropriate. If we’re at the absolute top or bottom, just return the first or last
        // row respectively. Is row at top of screen?
        final int nextRow = (pt.y + raOuter.y <= AUTOSCROLL_MARGIN) ?
                // Yes, scroll up one row
                (actualRow <= 0 ? 0 : actualRow - 1) :
                // No, scroll down one row
                (actualRow < getRowCount() - 1 ? actualRow + 1 : actualRow);
        scrollRowToVisible(nextRow);
    }

    // More helpers...

    protected boolean isRootPath(final TreePath path)
    {
        return isRootVisible() && getRowForPath(path) == 0;
    }

    /**
     * Checks for the different conditions for which transfers are allowed. The ghost image is drawn here, and a cue
     * line is drawn between nodes to indicate the drop location. Movements to the left or right are catched to display
     * an appropriate icon and trigger scaling down and up of the nodes Level. <p> <p/> A timer is used to automatically
     * open and close nodes.
     *
     * @author $Author: daniel_frey $
     * @version $Revision: 1.6 $ $Date: 2006/04/15 23:03:21 $
     */
    private class DropTargetHandler implements DropTargetListener
    {

        // Fields...
        private Color colorCueBox = ColorUtils.alpha(SystemColor.controlShadow, 0.25);

        private Color colorCueLine = ColorUtils.alpha(SystemColor.controlShadow, 0.75);

        private BufferedImage imgLeft = new ArrowImage(15, 15, ArrowImage.ArrowDirection.ARROW_LEFT);

        private BufferedImage imgRight = new ArrowImage(15, 15, ArrowImage.ArrowDirection.ARROW_RIGHT);

        private Rectangle2D raCueLine = new java.awt.geom.Rectangle2D.Float();

        private CueLine cueLine = new CueLine(7);

        private Rectangle2D cueBox = new Rectangle2D.Float();

        private Rectangle2D raGhost = new java.awt.geom.Rectangle2D.Float();

        private Timer timerHover;

        private DnDValidatorUpdater tm;

        /** Cumulative left/right mouse movement */
        protected Point ptLast = new Point();

        protected int nLeftRight = 0;

        protected int nShift = 0;

        protected TreePath pathLast = null;

        // Constructor...

        public DropTargetHandler(final DnDValidatorUpdater validator)
        {
            tm = validator;

            // Set up a hover timer, so that a node will be automatically expanded or collapsed if the user lingers on
            // it for more than a short time
            timerHover = new Timer(2000, new ActionListener()
            {
                public void actionPerformed(final ActionEvent e)
                {
                    // ResetPresentationModel left/right movement trend
                    nLeftRight = 0;
                    // Do nothing if we are hovering over the root node
                    if (isRootPath(pathLast))
                    {
                        return;
                    }
                    if (isExpanded(pathLast))
                    {
                        collapsePath(pathLast);
                    }
                    else
                    {
                        expandPath(pathLast);
                    }
                }
            });
            // Set timer to one-shot mode
            timerHover.setRepeats(false);
        }

        // Helpers...

        public boolean isDragAcceptable(final DropTargetDragEvent e)
        {
            // Only accept COPY or MOVE gestures (ie LINK is not supported)
            if ((e.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) == 0)
            {
                return false;
            }

            // Only accept this particular flavor
            if (!e.isDataFlavorSupported(TransferableTreePaths.TREEPATH_FLAVOR))
            {
                return false;
            }

            // Check for valid target
            final Point location = e.getLocation();
            return tm.isMoveAllowed(sourcePath, getDropPath(location), getDropIndex(location));
        }

        public boolean isDropAcceptable(final DropTargetDropEvent e)
        {
            // Only accept COPY or MOVE gestures (ie LINK is not supported)
            if ((e.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) == 0)
            {
                return false;
            }

            // Only accept this particular flavor
            if (!e.isDataFlavorSupported(TransferableTreePaths.TREEPATH_FLAVOR))
            {
                return false;
            }

            // Check for valid target
            final Point location = e.getLocation();
            return tm.isMoveAllowed(sourcePath, getDropPath(location), getDropIndex(location));
        }

        // DropTargetListener interface

        /**
         * Here we determie whether the drag should go.
         *
         * @param e the event
         */
        public void dragEnter(final DropTargetDragEvent e)
        {
            if (!isDragAcceptable(e))
            {
                e.rejectDrag();
            }
            else
            {
                e.acceptDrag(e.getDropAction());
            }
        }

        /**
         * Here the ghost image and cue lines are erased.
         *
         * @param e the event
         */
        public void dragExit(final DropTargetEvent e)
        {
            if (!DragSource.isDragImageSupported())
            {
                repaint(raGhost.getBounds());
            }
        }

        /**
         * This is where the ghost image is drawn.
         *
         * @param e the event
         */
        public void dragOver(final DropTargetDragEvent e)
        {
            // Even if the mouse is not moving, this method is still invoked 10 times per second
            final Point pt = e.getLocation();
            if (pt.equals(ptLast))
            {
                return;
            }

            // Try to determine whether the user is flicking the cursor right or left.
            // Switching direction resets flicking.
            final int nDeltaLeftRight = pt.x - ptLast.x;
            if ((nLeftRight > 0 && nDeltaLeftRight < 0) || (nLeftRight < 0 && nDeltaLeftRight > 0))
            {
                nLeftRight = 0;
            }
            nLeftRight += nDeltaLeftRight;
            ptLast = pt;

            final Graphics2D g2 = (Graphics2D) getGraphics();
            final Map map = new HashMap();
            map.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            map.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            final RenderingHints hints = new RenderingHints(map);
            g2.setRenderingHints(hints);

            // If a drag image is not supported by the platform, then draw my own drag image
            if (!DragSource.isDragImageSupported())
            {
                // Rub out the last ghost image and cue line
                paintImmediately(raGhost.getBounds());
                // And remember where we are about to draw the new ghost imagep
                raGhost.setRect(pt.x - offset.x, pt.y - offset.y, ghost.getWidth(), ghost.getHeight());
                g2.drawImage(ghost, AffineTransform.getTranslateInstance(raGhost.getX(), raGhost.getY()), null);
            }
            // Just rub out the last cue line
            else
            {
                paintImmediately(raCueLine.getBounds());
            }

            // In any case draw (over the ghost image if necessary) either a cue line indicating where a drop will occur
            // or a band indicating that it will be dropped into the item.
            final Rectangle bounds = getPathBounds(getClosestPathForLocation(pt.x, pt.y));
            final boolean isLowerQuart = isLowerQuart(pt);

            final Shape insertionMarker;
            if (isMiddleHalf(pt))
            {
                insertionMarker = cueBox;
                cueBox.setRect(0, bounds.y, getWidth(), bounds.height);
                g2.setColor(colorCueBox);
            }
            else
            {
                final int x;
                final int y;
                if (isBeyondLast(pt))
                {
                    final Rectangle parentBounds = getPathBounds(getParent(pt));
                    x = parentBounds.x;
                    y = bounds.y + bounds.height;
                }
                else if (isUpperQuart(pt))
                {
                    x = bounds.x;
                    y = bounds.y;
                }
                else if (isLowerQuart && isParentToNext(pt))
                {
                    final Rectangle nextBounds = getPathBounds(getNext(pt));
                    x = nextBounds.x;
                    y = nextBounds.y;
                }
                else if (isLowerQuart && (isLastSibling(pt) || isSiblingToNext(pt)))
                {
                    x = bounds.x;
                    y = bounds.y + bounds.height;
                }
                else
                {
                    throw new IllegalStateException("unknown state in drag gesture");
                }
                insertionMarker = cueLine;
                cueLine.setCue(x);
                cueLine.setY(y);
                cueLine.setWidth(getWidth());
                g2.setColor(colorCueLine);
            }

            if (tm.isMoveAllowed(sourcePath, getDropPath(pt), getDropIndex(pt)))
            {
                g2.fill(insertionMarker);
            }

            final TreePath touched = getClosestPathForLocation(pt.x, pt.y);
            if (touched != pathLast)
            {
                // We've moved up or down, so reset left/right movement trend
                nLeftRight = 0;
                pathLast = touched;
                timerHover.restart();
            }

            // Now superimpose the left/right movement indicator only if the source path is the same.
            if ((tm.isRightShiftAllowed(sourcePath) || tm.isLeftShiftAllowed(sourcePath)) && sourcePath.equals(touched))
            {
                if (nLeftRight > 20 && tm.isRightShiftAllowed(sourcePath))
                {
                    g2.drawImage(imgRight, AffineTransform.getTranslateInstance(pt.x - offset.x, pt.y - offset.y), null);
                    nShift = +1;
                }
                else if (nLeftRight < -20 && tm.isLeftShiftAllowed(sourcePath))
                {
                    g2.drawImage(imgLeft, AffineTransform.getTranslateInstance(pt.x - offset.x, pt.y - offset.y), null);
                    nShift = -1;
                }
            }
            else
            {
                nShift = 0;
            }

            // And include the cue line in the area to be rubbed out next time
            final Rectangle insertionMarkerBounds = insertionMarker.getBounds();
            insertionMarkerBounds.grow(0, 1);
            raGhost = raGhost.createUnion(insertionMarkerBounds);

            // Do this if you want to prohibit dropping onto the drag source
            if (!isDragAcceptable(e))
            {
                e.rejectDrag();
            }
            else
            {
                e.acceptDrag(e.getDropAction());
            }
        }

        /**
         * Note: Make sure the tree is using each TreeNode only once. Generation of arbitrary TreeNodes may lead to
         * insertions at wrong position or IndexOutOfBoundsException with an index of -1, as evaluation of the insertion
         * point is based on reference equality.
         */
        public void drop(final DropTargetDropEvent e)
        {

            // Prevent hover timer from doing an unwanted expandPath or collapsePath
            timerHover.stop();

            boolean completeOk = true;

            if (!isDropAcceptable(e))
            {
                e.rejectDrop();
            }
            else
            {
                e.acceptDrop(e.getDropAction());
                completeOk = handleDrop(e);
            }

            e.dropComplete(completeOk);
        }

        public void dropActionChanged(final DropTargetDragEvent e)
        {
            if (!isDragAcceptable(e))
            {
                e.rejectDrag();
            }
            else
            {
                e.acceptDrag(e.getDropAction());
            }
        }

        private TreePath getNext(final Point pt)
        {
            final TreePath path = getClosestPathForLocation(pt.x, pt.y);
            final int row = getRowForPath(path);
            return getPathForRow(row + 1);
        }

        private TreePath getParent(final Point pt)
        {
            final TreePath path = getClosestPathForLocation(pt.x, pt.y);
            return path.getParentPath();
        }

        /**
         * Returns whether the closest node at this point is a parent to the next node.
         *
         * @param pt the location to search for the node
         * @return whether parent to next node
         */
        private boolean isParentToNext(final Point pt)
        {
            final TreePath touchedPath = getClosestPathForLocation(pt.x, pt.y);
            final Object touchedNode = touchedPath.getLastPathComponent();
            final int count = getModel().getChildCount(touchedNode);
            final boolean isParentToNext;
            if (count > 0)
            {
                final Object firstChildNode = getModel().getChild(touchedNode, 0);
                final TreePath firstChild = touchedPath.pathByAddingChild(firstChildNode);
                isParentToNext = firstChild.equals(getNext(pt));
            }
            else
            {
                isParentToNext = false;
            }
            return isParentToNext;
        }

        /**
         * Returns whether the mouse location is beyond the last node in the tree.
         *
         * @param pt the mouse location
         * @return whether beyond last node
         */
        private boolean isBeyondLast(final Point pt)
        {
            final TreePath touched = getClosestPathForLocation(pt.x, pt.y);
            final TreePath parent = touched.getParentPath();
            final TreePath root = (isRootVisible() ? getPathForRow(0) : getPathForRow(0).getParentPath());
            final boolean beyondLast;
            if (touched.equals(root) || (parent != null && parent.equals(root)))
            {
                beyondLast = false;
            }
            else
            {
                final Rectangle bounds = getPathBounds(touched);
                beyondLast = bounds.y + bounds.height < pt.y;
            }
            return beyondLast;
        }

        /**
         * Returns whether the nearest node of the points location is a last children within its siblings.
         *
         * @param pt the location to search for the node
         * @return whether last sibling
         */
        private boolean isLastSibling(final Point pt)
        {
            final TreePath touched = getClosestPathForLocation(pt.x, pt.y);
            final TreePath parent = touched.getParentPath();
            final boolean isLastSibling;
            if (parent == null)
            {
                isLastSibling = false;
            }
            else
            {
                final Object parentNode = parent.getLastPathComponent();
                final TreePath touchedPath = getClosestPathForLocation(pt.x, pt.y);
                final Object touchedNode = touchedPath.getLastPathComponent();
                final int touchedIndex = getModel().getIndexOfChild(parentNode, touchedNode);
                final int numberOfSiblings = getModel().getChildCount(parentNode);
                isLastSibling = touchedIndex + 1 == numberOfSiblings;
            }
            return isLastSibling;
        }

        /**
         * Returns whether the nearest node of the points location has a following sibling.
         *
         * @param pt the location to search for the node
         * @return whether there is a following sibling
         */
        private boolean isSiblingToNext(final Point pt)
        {
            final TreePath touched = getClosestPathForLocation(pt.x, pt.y);
            final TreePath parent = touched.getParentPath();
            final TreePath next = getNext(pt);
            return (parent != null && next != null && parent.equals(next.getParentPath()));
        }

        /**
         * Returns whether the point is in the lower quart of the nearest node at the points locations.
         *
         * @param pt the location to search for the node
         * @return whether in the lower quart.
         */
        private boolean isLowerQuart(final Point pt)
        {
            final Rectangle bounds = getPathBounds(getClosestPathForLocation(pt.x, pt.y));
            return pt.y >= bounds.y + bounds.height * 2 / 3;
        }

        /**
         * Returns whether the point is in the upper quart of the nearest node at the points location.
         *
         * @param pt the location to search for the node
         * @return whether in the upper quart
         */
        private boolean isUpperQuart(final Point pt)
        {
            final Rectangle bounds = getPathBounds(getClosestPathForLocation(pt.x, pt.y));
            return pt.y <= bounds.y + bounds.height / 3;
        }

        /**
         * Returns whether the point lies in the middle half of the node under it.
         *
         * @param pt the location to search for the node
         * @return whether in the middle half
         */
        private boolean isMiddleHalf(final Point pt)
        {
            final TreePath path = getClosestPathForLocation(pt.x, pt.y);
            final TreePath root;
            if (isRootVisible())
            {
                root = getPathForRow(0);
            }
            else
            {
                root = null;
            }
            return ((path.equals(root) && !isLowerQuart(pt)) || !(isLowerQuart(pt) || isUpperQuart(pt)));
        }

        private TreePath getDropPath(final Point pt)
        {
            final TreePath path = getClosestPathForLocation(pt.x, pt.y);
            final boolean isLowerQuart = isLowerQuart(pt);
            final TreePath dropPath;
            if (isBeyondLast(pt))
            {
                dropPath = path.getParentPath().getParentPath();
            }
            else if (isMiddleHalf(pt) || (isLowerQuart && isParentToNext(pt)))
            {
                dropPath = path;
            }
            else if (isUpperQuart(pt) || isLowerQuart && (isLastSibling(pt) || isSiblingToNext(pt)))
            {
                dropPath = path.getParentPath();
            }
            else
            {
                throw new IllegalStateException("drag gesture with invalid state");
            }
            return dropPath;
        }

        private int getDropIndex(final Point pt)
        {
            final TreePath path = getClosestPathForLocation(pt.x, pt.y);
            final Object node = path.getLastPathComponent();
            final boolean isLowerQuart = isLowerQuart(pt);
            final int dropIndex;
            if (isMiddleHalf(pt))
            {
                dropIndex = getModel().getChildCount(node);
            }
            else if (isBeyondLast(pt))
            {
                final TreePath grandParent = path.getParentPath().getParentPath();
                dropIndex = getModel().getChildCount(grandParent.getLastPathComponent());
            }
            else if (isUpperQuart(pt))
            {
                final Object parent = path.getParentPath().getLastPathComponent();
                dropIndex = getModel().getIndexOfChild(parent, node);
            }
            else if (isLowerQuart && isParentToNext(pt))
            {
                dropIndex = 0;
            }
            else if (isLowerQuart && (isLastSibling(pt) || isSiblingToNext(pt)))
            {
                final Object parent = path.getParentPath().getLastPathComponent();
                dropIndex = getModel().getIndexOfChild(parent, node) + 1;
            }
            else
            {
                dropIndex = 0;
            }
            return dropIndex;
        }

        private boolean handleDrop(final DropTargetDropEvent e)
        {

            boolean completeOk = false;

            // keep source path for reselection after everthing is done.
            //TreePath pathNewChild = sourcePath;
            TreePath[] selectionPaths = null;

            final JTree jt = (JTree) ((DropTarget) e.getSource()).getComponent();
            final TreeExpandedRestorer ter = new TreeExpandedRestorer(jt);
            ter.save();

            final Transferable transferable = e.getTransferable();
            final DataFlavor[] flavors = transferable.getTransferDataFlavors();

            for (int i = 0; i < flavors.length && !completeOk; i++)
            {
                final DataFlavor flavor = flavors[i];
                if (flavor.isMimeTypeEqual(DataFlavor.javaJVMLocalObjectMimeType))
                {
                    try
                    {

                        final TreeModel model = jt.getModel();
                        final Point location = e.getLocation();

                        final TreePath targetPath = getDropPath(location);
                        final Object newParent = targetPath.getLastPathComponent();
                        final int iClosest = getDropIndex(location);

                        final TreePath[] pathMissiles = (TreePath[]) transferable.getTransferData(flavor);
                        selectionPaths = new TreePath[pathMissiles.length];

                        for (int j = pathMissiles.length - 1; j >= 0; j--)
                        {

                            final TreePath pathMissile = pathMissiles[j];
                            final Object missile = pathMissile.getLastPathComponent();

                            // Remember for selection
                            selectionPaths[j] = targetPath.pathByAddingChild(missile);

                            // Calculate insertion point. If the target is before the insertion point then it
                            // will be removed. This has to be considered when calculating the insertion point.
                            final int iMissile = model.getIndexOfChild(newParent, missile);
                            final int insertionIndex = iClosest - (iClosest > iMissile && iMissile >= 0 ? 1 : 0);

                            handleOneDrag(pathMissile, targetPath, insertionIndex, ter, missile);
                        }

                        // No need to check remaining flavors
                        completeOk = true;
                    }
                    catch (UnsupportedFlavorException ufe)
                    {
                        System.err.println(ufe);
                    }
                    catch (IOException ioe)
                    {
                        System.err.println(ioe);
                    }
                }
            }
            if (completeOk)
            {
                ter.restore();

                // Mark this as the selected path in the tree
                if (selectionPaths != null)
                {
                    setSelectionPaths(selectionPaths);
                }
            }
            return completeOk;
        }

        private void handleOneDrag(final TreePath pathMissile, final TreePath targetPath,
                                   final int insertionIndex, final TreeExpandedRestorer ter, final Object missile)
        {

            if (nShift == 0)
            {
                // Node has been moved up or down in the tree.

                // Update model. So the node has to be removed from the old parent node and added to
                // the new parent.
                mutable.removeFromParent(pathMissile);
                mutable.insertInto(pathMissile, targetPath, insertionIndex);
                ter.update(missile, pathMissile.getParentPath(), targetPath);
            }
            else if (nShift > 0)
            {
                tm.doRightShift(pathMissile);
            }
            else if (nShift < 0)
            {
                tm.doLeftShift(pathMissile);
            }
        }
    }

    private class DragSourceHandler extends DragSourceAdapter
    {

        public void dragDropEnd(final DragSourceDropEvent e)
        {
            if (e.getDropSuccess())
            {
                final int nAction = e.getDropAction();
                if (nAction == DnDConstants.ACTION_MOVE)
                {
                    sourcePath = null;
                }
            }
        }
    }

    private class DragGestureHandler implements DragGestureListener
    {

        public void dragGestureRecognized(final DragGestureEvent e)
        {
            final Point dragOrigin = e.getDragOrigin();
            final TreePath path = getPathForLocation(dragOrigin.x, dragOrigin.y);
            if (path == null)
            {
                return;
            }
            // Ignore user trying to drag the root node
            if (isRootPath(path))
            {
                return;
            }

            // Look whether the draged path is part of the selected paths. If yes, drag all selected paths, otherwise
            // only the path nearest.
            final TreePath[] paths = getSelectionPaths();

            // Work out the offset of the drag point from the TreePath bounding rectangle origin
            final Rectangle raPath = getPathBounds(path);
            raPath.width -= 1;
            offset.setLocation(dragOrigin.x - raPath.x, dragOrigin.y - raPath.y);

            // Get the cell renderer (which is a JLabel) for the path being dragged
            final BufferedImage[] ghostImages = new BufferedImage[paths.length];
            for (int i = 0; i < paths.length; i++)
            {
                final TreePath treePath = paths[i];
                final Object value = treePath.getLastPathComponent();
                final JComponent comp = (JComponent) getCellRenderer().getTreeCellRendererComponent(
                        DnDTree.this, value, false, isExpanded(treePath), getModel().isLeaf(value), 0, false);
                comp.setSize((int) raPath.getWidth(), (int) raPath.getHeight());
                // Make sure to collect images, not components as components are changed when passing in the next loop.
                ghostImages[i] = ImageUtils.getGhostImage(comp);
            }
            ghost = ImageUtils.getGhostImage(ghostImages);

            // Select this path in the tree
            //setSelectionPath(path);

            // Wrap the path being transferred into a Transferable object
            final Transferable transferable = new TransferableTreePaths(paths);

            // Remember the path being dragged (because if it is being moved, we will have to delete it later)
            sourcePath = path;

            // We pass our drag image just in case it IS supported by the platform
            e.startDrag(null, ghost, new Point(5, 5), transferable, dragSourceHandler);
        }
    }

    /**
     * Represents a shape that looks like an arrow pointing to a bar.
     *
     * <pre>
     *           2  4--5
     *           |\ |  |
     *   0------ 1 \|  |
     *              3  6--7
     *             12  9--8
     *  15----- 14 /|  |
     *           |/ |  |
     *          13  11-19
     * </pre>
     */
    private class CueLine implements Shape
    {

        private final Polygon polygon = new Polygon(new int[16], new int[16], 16);

        private int cue;

        private int thickness;

        private int y;

        private int width;

        private Rectangle bounds;

        public CueLine(final int thickness)
        {
            setCue(5);
            setThickness(thickness);
        }

        public void setThickness(final int thickness)
        {
            this.thickness = thickness;
            recalculate();
        }

        public void setY(final int y)
        {
            this.y = y;
            recalculate();
        }

        public void setCue(final int cue)
        {
            this.cue = cue;
            recalculate();
        }

        private void recalculate()
        {
            final int half = thickness - thickness / 2;

            polygon.ypoints[0] = y - half;
            polygon.ypoints[1] = y - half;
            polygon.ypoints[2] = y - thickness;
            polygon.ypoints[3] = y - 1;
            polygon.ypoints[4] = y - thickness;
            polygon.ypoints[5] = y - thickness;
            polygon.ypoints[6] = y - 1;
            polygon.ypoints[7] = y - 1;
            polygon.ypoints[8] = y + 1;
            polygon.ypoints[9] = y + 1;
            polygon.ypoints[10] = y + thickness;
            polygon.ypoints[11] = y + thickness;
            polygon.ypoints[12] = y + 1;
            polygon.ypoints[13] = y + thickness;
            polygon.ypoints[14] = y + half;
            polygon.ypoints[15] = y + half;

            polygon.xpoints[0] = 0;
            polygon.xpoints[1] = cue - half - thickness;
            polygon.xpoints[2] = cue - half - thickness;
            polygon.xpoints[3] = cue - half;
            polygon.xpoints[4] = cue - half;
            polygon.xpoints[5] = cue;
            polygon.xpoints[6] = cue;
            polygon.xpoints[7] = width;
            polygon.xpoints[8] = width;
            polygon.xpoints[9] = cue;
            polygon.xpoints[10] = cue;
            polygon.xpoints[11] = cue - half;
            polygon.xpoints[12] = cue - half;
            polygon.xpoints[13] = cue - half - thickness;
            polygon.xpoints[14] = cue - half - thickness;
            polygon.xpoints[15] = 0;

            calculateBounds();
        }

        private void calculateBounds()
        {
            int xMin = Integer.MAX_VALUE;
            int yMin = Integer.MAX_VALUE;
            int xMax = Integer.MIN_VALUE;
            int yMax = Integer.MIN_VALUE;

            for (int i = 0; i < polygon.npoints; i++)
            {
                final int x = polygon.xpoints[i];
                xMin = Math.min(xMin, x);
                xMax = Math.max(xMax, x);
                final int y = polygon.ypoints[i];
                yMin = Math.min(yMin, y);
                yMax = Math.max(yMax, y);
            }
            bounds = new Rectangle(xMin, yMin, xMax - xMin, yMax - yMin);
        }

        public boolean contains(final double x, final double y)
        {
            return polygon.contains(x, y);
        }

        public boolean contains(final double x, final double y, final double w, final double h)
        {
            return polygon.contains(x, y, w, h);
        }

        public boolean intersects(final double x, final double y, final double w, final double h)
        {
            return polygon.intersects(x, y, w, h);
        }

        public Rectangle getBounds()
        {
            return bounds;
        }

        public boolean contains(final Point2D p)
        {
            return polygon.contains(p);
        }

        public Rectangle2D getBounds2D()
        {
            return polygon.getBounds2D();
        }

        public boolean contains(final Rectangle2D r)
        {
            return polygon.contains(r);
        }

        public boolean intersects(final Rectangle2D r)
        {
            return polygon.intersects(r);
        }

        public PathIterator getPathIterator(final AffineTransform at)
        {
            return polygon.getPathIterator(at);
        }

        public PathIterator getPathIterator(final AffineTransform at, final double flatness)
        {
            return polygon.getPathIterator(at, flatness);
        }

        public String toString()
        {
            return getClass().getName() + "[y=" + y + ",cue=" + cue + "]";
        }

        public void setWidth(final int width)
        {
            this.width = width;
        }
    }
}
