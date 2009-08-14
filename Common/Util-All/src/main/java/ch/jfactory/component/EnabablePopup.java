/*
 * Copyright x-matrix Switzerland (c) 2002
 *
 * EnabablePopup.java
 *
 * Created on 8. July 2002, 12:44
 * Created by Daniel Frey
 */
package ch.jfactory.component;

import ch.jfactory.resource.Strings;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.apache.commons.lang.ArrayUtils;

/**
 * This combination of a JPopupMenu and a JButton adds enabability to the items in the popup menu. The popup is
 * displayed at the correct item over the button, so selection is made easier/shorter. <p> <p/> To construct the
 * component, you need to deliver some objects which are displayed in the popup by using their toString() method. An
 * additional icon may be delivered to display on the button. <p> <p/> The items in the popup are en-/disabable. To
 * enable a set of objects, use the <code>setEnabled()</code> method. <p> <p/> The following options are on the todo
 * list: <ul> <li> Pass an array of icons to display with the popup items</li> <li> Choose whether checkboxes are used
 * for the popup items</li> </ul>
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 * @see #setEnabled(Object[])
 */
public class EnabablePopup extends JButton implements ActionListener
{

    private static final String DEFAULT = Strings.getString("ROLE_NULL.ENTRY");

    private SimpleAction[] actions;

    private JPopupMenu popup;

    private Object selection;

    /**
     * The first item, if there, is used to set the text of the button, otherwise a default is used.
     *
     * @param items the items to display in the popup
     * @param icon  thie icon to place on the button
     */
    public EnabablePopup(final Object[] items, final Icon icon)
    {
        JMenuItem jmi = null;
        this.setIcon(icon);
        this.setHorizontalTextPosition(JButton.LEFT);
        this.setHorizontalAlignment(JButton.LEFT);
        popup = new JPopupMenu();
        actions = new SimpleAction[items.length];
        this.setText(DEFAULT);
        int maxWidth = (int) this.getPreferredSize().getWidth();
        for (int i = 0; i < items.length; i++)
        {
            jmi = popup.add(new SimpleAction(items[i]));
            actions[i] = (SimpleAction) jmi.getAction();
            this.setText(items[i].toString());
            final int pWidth = (int) this.getPreferredSize().getWidth();
            maxWidth = (pWidth > maxWidth ? pWidth : maxWidth);
        }
        if (items.length > 0)
        {
            this.setText(items[0].toString());
            selection = items[0];
        }
        else
        {
            this.setText(DEFAULT);
            selection = null;
        }
        final int height = (int) this.getPreferredSize().getHeight();
        this.setPreferredSize(new Dimension(maxWidth, height));
        this.addActionListener(this);
    }

    /**
     * Enables all popup item objects in the array, disables all popup item objects not in the array.
     *
     * @param toEnable the objects to enable. Make sure not to pass null objects. So toEnable should not contain
     *                 <code>null</code> elements.
     */
    public void setEnabled(final Object[] toEnable)
    {
        for (SimpleAction action1 : actions)
        {
            action1.setEnabled(ArrayUtils.contains(toEnable, action1.getObject()));
        }
        if (!ArrayUtils.contains(toEnable, selection) && toEnable.length > 0)
        {
            selection = toEnable[0];
            this.setText(selection.toString());
        }
        else if (toEnable.length == 0)
        {
            selection = null;
            this.setText(DEFAULT);
        }
    }

    /**
     * Returns the currently selection Object of the popup.
     *
     * @return the Object currently selection
     */
    public Object getSelection()
    {
        return this.selection;
    }

    /**
     * Sets the selection.
     *
     * @param selected The selection to set
     */
    public void setSelection(final Object selected)
    {
        this.selection = selected;
    }

    /**
     * Sets the width of the popup and displays the popup at the selection item. The size of the popup is set to the
     * width of the button. The size is ajusted each time, so changes of the layout manager are taken into account.
     *
     * @param actionEvent the event object
     */
    public void actionPerformed(final ActionEvent actionEvent)
    {
        // display the popup at the selected object by finding the component
        // selected in the popup. There might be a small difference between
        // the buttons and the components height, so I equalize this as well in
        // order to display the popups component in the vertical middle of the
        // button.
        int yPos = 0;
        for (int i = popup.getComponentCount() - 1; i >= 0; i--)
        {
            final JMenuItem jmi = (JMenuItem) popup.getComponent(i);
            final SimpleAction testaction = (SimpleAction) jmi.getAction();
            if (selection == testaction.getObject())
            {
                yPos = jmi.getBounds().y;
                if (jmi.getHeight() > 0)
                {
                    yPos += (jmi.getHeight() - getHeight()) / 2;
                }
            }
        }
        popup.show(this, 0, -yPos);
    }

    class SimpleAction extends AbstractAction
    {

        private Object object;

        public SimpleAction(final Object item)
        {
            this(item, null);
        }

        public SimpleAction(final Object item, final Icon icon)
        {
            super(item.toString(), icon);
            object = item;
        }

        public Object getObject()
        {
            return object;
        }

        public void actionPerformed(final ActionEvent ae)
        {
            EnabablePopup.this.setSelection(object);
            EnabablePopup.this.setText(object.toString());
        }
    }

}

// $Log: EnabablePopup.java,v $
// Revision 1.1  2005/06/16 06:28:57  daniel_frey
// Completely merged and finished for UST version 2.0-20050616
//
// Revision 1.3  2004/08/30 21:46:58  daniel_frey
// Lot of refactorings
//
// Revision 1.2  2004/06/16 21:18:32  daniel_frey
// Extracted package from xmatrix to jfactory
//
// Revision 1.1  2004/04/19 10:31:21  daniel_frey
// Replaced top level package com by ch
//
// Revision 1.10  2003/04/11 12:28:28  daniel_frey
// - Included popup for current list in lesson panel
//
// Revision 1.9  2003/01/23 10:54:01  daniel_frey
// - Optimized imports
//
// Revision 1.8  2002/09/25 22:59:59  daniel_frey
// - Changes ER to reflect PICTURETEXT as node, not as role
//
// Revision 1.7  2002/09/20 14:01:45  Dani
// - Automatic width adaptation
//
// Revision 1.6  2002/08/27 15:11:57  Dani
// - Moved IsIn from Utils to ArrayUtils
//
// Revision 1.5  2002/08/05 12:48:05  Dani
// - Synchronisation wihtout changes
//
// Revision 1.4  2002/08/05 12:29:23  Dani
// - Reformatted
// - Externalized strings
// - Refactored and encabsulated variable names
//
// Revision 1.3  2002/08/02 00:42:21  Dani
// Optimized import statements
//
// Revision 1.2  2002/07/08 11:14:30  Dani
// CVS keyword $Source: /repository/HerbarCD/Version2.1/xmatrix/src/java/ch/jfactory/component/EnabablePopup.java,v $ added
//
// Revision 1.1  2002/07/08 10:44:56  Dani
// rename package components to component
//
// Revision 1.2  2002/07/05 08:54:11  Dani
// using now ArraysUtils
//
// Revision 1.1  2002/07/05 08:34:47  Dani
// initial
//
