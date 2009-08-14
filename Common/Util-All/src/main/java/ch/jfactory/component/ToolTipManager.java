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
package ch.jfactory.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.CellRendererPane;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JToolTip;
import javax.swing.JTree;
import javax.swing.JWindow;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputAdapter;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

/**
 * <pre>
 * TLToolTipManager.java
 * ToolTipManager for both J<b>T</b>ree and J<b>L</b>ist.
 * Usage:
 *  new TLToolTipManager(JTree) or new TLToolTipManager(JList);
 *  Note - there is no need to keep reference to TLToolTipManager objects.
 * </pre>
 *
 * @author Andrei Kouznetsov
 * @version 1.3
 */
public class ToolTipManager
{

    public static final int UNDEFINED_ROW = -1;

    int lastRow = -1;

    TipWindow window;

    JComponent owner;

    boolean showFullTip;

    JToolTip tip = new JToolTip();

    Rectangle visibleRect = new Rectangle();

    Rectangle rowBounds = new Rectangle();

    Border lb = new LineBorder(Color.gray);

    /**
     * create new TLToolTipManager for supplied JTree
     *
     * @param tree JTree
     */
    public ToolTipManager(final JTree tree)
    {
        this(tree, false);
    }

    /**
     * create new TLToolTipManager for supplied JTree
     *
     * @param tree        JTree
     * @param showFullTip if true then full tooltip shown, otherwise only missed part shown (this is preferred way)
     */
    public ToolTipManager(final JTree tree, final boolean showFullTip)
    {
        this.owner = tree;
        this.showFullTip = showFullTip;

        final MouseHandler tmh = new MouseHandler();
        this.owner.addMouseListener(tmh);
        this.owner.addMouseMotionListener(tmh);

        final TipMouseHandler tmh2 = new TipMouseHandler();
        this.tip.addMouseListener(tmh2);
        this.tip.addMouseMotionListener(tmh2);
        owner.addComponentListener(new OwnerListener());
    }

    /**
     * create new TLToolTipManager for supplied JList
     *
     * @param list JList
     */
    public ToolTipManager(final JList list)
    {
        this(list, false);
    }

    /**
     * create new TLToolTipManager for supplied JList
     *
     * @param list        JList
     * @param showFullTip if true then full tooltip shown, otherwise only missed part shown (this is preferred way)
     */
    public ToolTipManager(final JList list, final boolean showFullTip)
    {
        this.owner = list;
        this.showFullTip = showFullTip;

        final MouseHandler tmh = new MouseHandler();
        this.owner.addMouseListener(tmh);
        this.owner.addMouseMotionListener(tmh);

        final TipMouseHandler tmh2 = new TipMouseHandler();
        this.tip.addMouseListener(tmh2);
        this.tip.addMouseMotionListener(tmh2);
        owner.addComponentListener(new OwnerListener());
    }

    /**
     * set lastRow to UNDEFINED_ROW
     *
     * @see #UNDEFINED_ROW
     */
    private void resetRow()
    {
        lastRow = UNDEFINED_ROW;
    }

    /**
     * Get TipWindow. If TipWindow is null then new one is created.
     *
     * @return JWindow
     */
    protected JWindow getTipWindow()
    {
        if (this.window == null)
        {
            final Window w = SwingUtilities.getWindowAncestor(owner);
            this.window = new TipWindow(w);
        }
        return this.window;
    }

    /** TipWindow. Used for showing tooltip. */
    static class TipWindow extends JWindow
    {

        BufferedImage bi;

        CellRendererPane pane;

        int xOffset;

        public TipWindow(final Window owner)
        {
            super(owner);
            pane = new CellRendererPane();
//            getContentPane().add(pane);
        }

        public void setXOffset(final int offset)
        {
            this.xOffset = offset;
            if (bi != null)
            {
                setSize(bi.getWidth(), bi.getHeight());
            }
        }

        void setImage(final BufferedImage bi)
        {
            this.bi = bi;
            if (bi != null)
            {
                setSize(bi.getWidth(), bi.getHeight());
            }
            repaint();
        }

        public void paint(final Graphics g)
        {
            g.setColor(Color.white);
            g.fillRect(0, 0, getWidth(), getHeight());
            if (bi != null)
            {
                g.drawImage(bi, 0 - (bi.getWidth() - xOffset), 0, null);
            }
        }
    }

    /**
     * Get row for given location. For JTree it calls JTree#getRowForLocation, for JList - JList#locationToIndex
     *
     * @param comp JTree or JList
     * @param p    Point
     * @return row for given point
     * @see javax.swing.JTree#getRowForLocation
     * @see javax.swing.JList#locationToIndex
     */
    private int getRow(final JComponent comp, final Point p)
    {
        if (comp instanceof JTree)
        {
            return ((JTree) comp).getRowForLocation(p.x, p.y);
        }
        else if (comp instanceof JList)
        {
            return ((JList) comp).locationToIndex(p);
        }
        throw new RuntimeException("JComponent shouls be JList or JTree");
    }

    /**
     * Get bounds for given row.<br> If comp is instance of JTree then bounds are determined by call to
     * JTree#getRowBounds.<br> If comp is instance of JList then bounds are determined by call to
     * JList#getCellBounds<br>
     *
     * @param comp JTree or JList
     * @param row  row number
     * @return bounds of given row (Rectangle)
     * @see javax.swing.JTree#getRowBounds
     * @see javax.swing.JList#getCellBounds
     */
    private Rectangle getRowBounds(final JComponent comp, final int row)
    {
        if (comp instanceof JTree)
        {
            return ((JTree) comp).getRowBounds(row);
        }
        else if (comp instanceof JList)
        {
            return ((JList) comp).getCellBounds(row, row);
        }
        throw new RuntimeException("JComponent shouls be JList or JTree");
    }

    /**
     * get start of label's text. <br> Should the ComponentOrientation be checked here?
     *
     * @param label the label
     * @return start of text
     */
    static int getLabelStart(final JLabel label)
    {
        final Icon currentI = label.getIcon();
        if (currentI != null && label.getText() != null)
        {
            return currentI.getIconWidth() + Math.max(0, label.getIconTextGap() - 4);
        }
        return 0;
    }

    /** MouseHandler.<br> MouseListener for TLToolTipManager. */
    class MouseHandler extends MouseInputAdapter
    {

        public void mousePressed(final MouseEvent e)
        {
            resetRow();
            mouseMoved(e);
        }

        public void mouseMoved(final MouseEvent e)
        {
            final JComponent component = (JComponent) e.getSource();

            final Point p = e.getPoint();

            final int row = getRow(component, p);

            if (row == -1)
            {
                resetRow();
                hideTipWindow();
                return;
            }

            final Rectangle rowBounds = getRowBounds(component, row);

            if (!rowBounds.contains(p))
            {
                hideTipWindow();
                resetRow();
                return;
            }

            final Rectangle vr = computeVisibleRect();

            if (vr.contains(rowBounds))
            {
                lastRow = row;
                hideTipWindow();
                return;
            }

            // row does not changed
            if (row == lastRow)
            {
                return;
            }

            lastRow = row;
            hideTipWindow();

            final Component label = getRenderer(component, row, rowBounds);

            if (vr.contains(rowBounds))
            {
                hideTipWindow();
                return;
            }

            createImage(rowBounds, label, component);

            final Point screenLocation = new Point(rowBounds.x, rowBounds.y);
            SwingUtilities.convertPointToScreen(screenLocation, component);

            getTipWindow().getContentPane().add(tip);

            if (showFullTip)
            {
                window.setXOffset(rowBounds.width);
                window.setLocation(screenLocation.x, screenLocation.y);
            }
            else
            {
                final int offset = (rowBounds.width + rowBounds.x) - (vr.width + vr.x);
                window.setXOffset(offset);
                window.setBounds(screenLocation.x + rowBounds.width - offset, screenLocation.y, offset, rowBounds.height);
            }
            window.setVisible(true);
        }

        private Component getRenderer(final JComponent comp, final int row, final Rectangle rowBounds)
        {
            if (comp instanceof JTree)
            {
                final JTree tree = (JTree) comp;
                final TreePath tp = tree.getPathForRow(row);
                final Object value = tp.getLastPathComponent();
                final boolean isLeaf = tree.getModel().isLeaf(value);

                final boolean isSelected = tree.isRowSelected(row);
                final TreeCellRenderer renderer = tree.getCellRenderer();

                return renderer.getTreeCellRendererComponent(tree, value, isSelected, !tree.isCollapsed(row), isLeaf, row, false);
            }
            else if (comp instanceof JList)
            {
                final JList list = (JList) comp;
                final boolean isSelected = list.isSelectedIndex(row);
                final ListCellRenderer renderer = list.getCellRenderer();

                final Object value = list.getModel().getElementAt(row);
                final String tipText = String.valueOf(value);

                tip.setTipText(tipText);

//                FontMetrics fm = tip.getFontMetrics(tip.getFont());
//                int strlen = fm.stringWidth(tipText);

                final Component label = renderer.getListCellRendererComponent(
                        list, value, row, isSelected, false);

//                int labelStart = getLabelStart(label);

//                rowBounds.x += labelStart;
                rowBounds.width = label.getPreferredSize().width;//strlen + 20;
                return label;
            }
            throw new RuntimeException("JComponent shouls be JList or JTree");
        }

        /**
         * Create buffer image.<br>
         *
         * @param rowBounds row bounds
         * @param label     renderer component
         * @param cont      Container (JTree or JList)
         * @see #getRenderer
         */
        private void createImage(final Rectangle rowBounds, final Component label, final Container cont)
        {
            final int w = rowBounds.width;
            final int h = rowBounds.height;
            BufferedImage bi = window.bi;
            if (w > 0 && h > 0)
            {
                window.getContentPane().add(label);
                window.validate();
                if (bi == null || bi.getWidth() < w || bi.getHeight() != h)
                {
                    /*BufferedImage */
                    bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                }
            }
            final Graphics2D g = bi.createGraphics();
            g.setColor(label.getBackground());
            g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
            lb.paintBorder(label, g, bi.getWidth() - w, 0, w, h);
            g.setClip(1, 1, bi.getWidth() - 2, bi.getHeight() - 2);
            window.pane.paintComponent(g, label, cont, bi.getWidth() - w, 0, w, h, true);
            g.dispose();

            window.setImage(bi);
        }

        public void mouseExited(final MouseEvent e)
        {
            final Rectangle vr = computeVisibleRect();
            if (vr.contains(e.getPoint()))
            {
                return;
            }
            hideTipWindow();
        }
    }

    /**
     * Optimized computing of visible rectangle (without creating new Rectangle Object every time)
     *
     * @return visible rectangle
     */
    protected Rectangle computeVisibleRect()
    {
        owner.computeVisibleRect(visibleRect);
        return visibleRect;
    }

    /** hide TipWindow */
    void hideTipWindow()
    {
        getTipWindow().setVisible(false);
    }

    /** MouseListener for TLToolTipManager */
    protected class TipMouseHandler extends MouseInputAdapter
    {

        public void mouseExited(final MouseEvent e)
        {
            hideTipWindow();
            resetRow();
        }

        public void mousePressed(final MouseEvent e)
        {
            resetRow();
            final Point p = e.getPoint();
            final Point nps = new Point(p);

            SwingUtilities.convertPointToScreen(nps, tip);
            final Point np = new Point(nps);

            SwingUtilities.convertPointFromScreen(np, owner);

            hideTipWindow();

            final MouseEvent ne = new MouseEvent(owner, e.getID(), e.getWhen(), e.getModifiers(),
                    np.x, np.y, e.getClickCount(), e.isPopupTrigger()
            );
            computeVisibleRect();
            if (visibleRect.contains(np))
            {
                owner.dispatchEvent(ne);
            }
        }

        public void mouseMoved(final MouseEvent e)
        {
            final Point p = e.getPoint();
            Rectangle vr = computeVisibleRect();
            final Point rp = new Point(vr.x, vr.y);
            SwingUtilities.convertPointToScreen(rp, owner);
            SwingUtilities.convertPointToScreen(p, tip);
            vr = new Rectangle(rp.x, rp.y, vr.width, vr.height);
            if (!vr.contains(p))
            {
                hideTipWindow();
                resetRow();
            }
        }
    }

    /** OwnerListener hides TipWindow if Component is resized, moved or made invisible. */
    private class OwnerListener extends ComponentAdapter
    {

        public void componentResized(final ComponentEvent e)
        {
            hideTipWindow();
        }

        public void componentMoved(final ComponentEvent e)
        {
            hideTipWindow();
        }

        public void componentHidden(final ComponentEvent e)
        {
            hideTipWindow();
        }
    }
}
