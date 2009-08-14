/*
 * Copyright x-matrix Switzerland (c) 2002
 *
 * StatusBar.java
 *
 * Created on 10. Juli 2002, 17:13
 * Created by Daniel Frey
 */
package ch.jfactory.application.view.status;

import com.jgoodies.looks.windows.WindowsLookAndFeel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JSplitPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * Makes a simple status bar to display i.e. at the bottom of a frame. Initially, a default message display area is
 * added at the leftmost position of the status bar. The text of this default message display area may be set with
 * {@link #setText(String)}.
 *
 * @author $Author: daniel_frey $
 * @version $Date: 2008/01/06 10:16:23 $ $Revision: 1.4 $
 */
public class StatusBar extends JPanel {

    private static final String LOGO = "\u00A9 " + Calendar.getInstance().get(Calendar.YEAR) + " www.xmatrix.ch";

    private final JLabel messageStatusItem;
    private final List<Message> history = new ArrayList<Message>();
    private final JPopupMenu historyPopup = new JPopupMenu();

    private GridBagConstraints gbc = new GridBagConstraints();
    private List<JComponent> components = new ArrayList<JComponent>();
    private List<StatusPanel> statusPanels = new ArrayList<StatusPanel>();

    public StatusBar() {
        setItemBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
        setLayout(new GridBagLayout());
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        messageStatusItem = new JLabel(LOGO);
        addStatusComponent(new StatusPanel(messageStatusItem));
        gbc.weightx = 0;
        installHistoryPopup();
    }

    private void installHistoryPopup() {
        final JList list = new JList();
        list.setCellRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
                final JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                final Message message = (Message) value;
                if (message.getType() == Message.Type.WARN) {
                    label.setForeground(Color.red);
                }
                label.setText(message.getText());
                return label;
            }
        });
        list.setBorder(new EmptyBorder(0, 0, 0, 0));
        final JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(new EmptyBorder(0, 0, 0, 0));
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        historyPopup.setLayout(new BorderLayout());
        historyPopup.add(scroll, BorderLayout.CENTER);
        historyPopup.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseExited(final MouseEvent e) {
                final Point point = SwingUtilities.convertMouseEvent((Component) e.getSource(), e, null).getPoint();
                final Rectangle popupRect = SwingUtilities.convertRectangle(historyPopup.getParent(), historyPopup.getBounds(), null);
                final Rectangle statusRect = SwingUtilities.convertRectangle(getParent(), getBounds(), null);
                historyPopup.setVisible(statusRect.contains(point) || popupRect.contains(point));
            }
        });
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseExited(final MouseEvent e) {
                final Point point = SwingUtilities.convertMouseEvent((Component) e.getSource(), e, null).getPoint();
                final Rectangle popupRect = SwingUtilities.convertRectangle(historyPopup.getParent(), historyPopup.getBounds(), null);
                final Rectangle statusRect = SwingUtilities.convertRectangle(getParent(), getBounds(), null);
                historyPopup.setVisible(statusRect.contains(point) || popupRect.contains(point));
            }

            @Override
            public void mouseClicked(final MouseEvent e) {
                list.setListData(history.toArray());
                final Dimension preferredSize = historyPopup.getPreferredSize();
                historyPopup.setPreferredSize(new Dimension(messageStatusItem.getWidth(), preferredSize.height));
                final Point point = messageStatusItem.getLocation();
                point.translate(0, -preferredSize.height);
                historyPopup.setLocation(point);
                historyPopup.show(StatusBar.this, point.x, point.y);
            }
        });
    }

    /**
     * Addd the new component and makes sure it looks the same like a status item.
     *
     * @param component component to add
     */
    public void addStatusComponent(final JComponent component) {
        gbc.gridx += 1;
        final StatusPanel statusPanel;
        if (StatusPanel.class.isAssignableFrom(component.getClass())) {
            statusPanel = (StatusPanel) component;
        }
        else {
            statusPanel = new StatusPanel(component);
        }
        components.add(component);
        statusPanels.add(statusPanel);
        add(statusPanel, gbc);
    }

    public void addStatusComponent(final JComponent component, final int index) {
        final List<JComponent> componentsCopy = new ArrayList<JComponent>(components);
        final List<StatusPanel> statusPanelsCopy = new ArrayList<StatusPanel>(statusPanels);
        for (int i = 1; i < components.size(); i++) {
            remove(statusPanels.get(i));
            components.remove(i);
            statusPanels.remove(i);
        }
        componentsCopy.add(index, component);
        statusPanelsCopy.add(index, new StatusPanel(component));
        gbc.gridx = 1;
        components = componentsCopy;
        statusPanels = statusPanelsCopy;
        for (int i = 1; i < components.size(); i++) {
            add(statusPanels.get(i), gbc);
            gbc.gridx += 1;
        }
    }

    /**
     * Sets the text of default message part of the status bar.
     *
     * @param text the new text to display
     */
    public void setText(final String text) {
        if (history.size() == 0 || !(history.get(0).getText().equals(text) && history.get(0).getType() == Message.Type.INFO)) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    messageStatusItem.setForeground(UIManager.getColor("Field.foreground"));
                    messageStatusItem.setText("".equals(text) ? LOGO : text);
                }
            });
            if (!"".equals(text)) {
                history.add(0, new SimpleMessage(text, Message.Type.INFO));
            }
        }
    }

    public void setWarning(final String text) {
        if (history.size() == 0 || !(history.get(0).getText().equals(text) && history.get(0).getType() == Message.Type.WARN)) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    messageStatusItem.setForeground(Color.red);
                    messageStatusItem.setText(text);
                }
            });
            if (!"".equals(text)) {
                history.add(0, new SimpleMessage(text, Message.Type.WARN));
            }
        }
    }

    /**
     * The border to use for all <code>StatusPanel</code>s.
     *
     * @param border the border to propagate
     */
    public void setItemBorder(final Border border) {
        for (int i = 0; i < components.size(); i++) {
            final JComponent component = components.get(i);
            component.setBorder(border);
        }
    }

    /**
     * Removes the component previously added and shifts the trailing components one to the left.
     *
     * @param component the component to be removed
     */
    public void removeStatusComponent(final JComponent component) {
        boolean found = false;
        final List<JComponent> tempComponents = new ArrayList<JComponent>(components);
        final List<StatusPanel> tempStatusPanels = new ArrayList<StatusPanel>(statusPanels);
        for (int i = 0; i < tempComponents.size(); i++) {
            final JComponent comp = tempComponents.get(i);
            if (found || comp == component) {
                remove(tempStatusPanels.get(i));
                if (comp != component) {
                    add(tempStatusPanels.get(i), gbc);
                }
                else {
                    components.remove(i);
                    statusPanels.remove(i);
                    found = true;
                }
            }
            gbc.gridx = i;
        }
    }

    @Override
    public Dimension getPreferredSize() {
        final int h = super.getPreferredSize().height;
        final Container parent = getTopLevelAncestor();
        final int w = parent.getSize().width;
        return new Dimension(w, h);
    }

    public static void main(final String[] args) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new WindowsLookAndFeel());
        final javax.swing.JButton remove = new javax.swing.JButton("Remove");
        final javax.swing.JButton add = new javax.swing.JButton("Add");
        final javax.swing.JButton message = new javax.swing.JButton("Message");
        final javax.swing.JPanel panel = new JPanel(new java.awt.FlowLayout());

        final javax.swing.JFrame f = new javax.swing.JFrame();
        final javax.swing.JTextField text = new javax.swing.JTextField(20);
        final StatusBar status = new StatusBar();
        final List<JLabel> list = new ArrayList<JLabel>();

        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(new JMenu("Datei"));
        f.setJMenuBar(menuBar);

        final JSplitPane splitPane = new JSplitPane();
        splitPane.setRightComponent(panel);
        f.getContentPane().add(splitPane, java.awt.BorderLayout.CENTER);
        panel.add(add);
        panel.add(remove);
        panel.add(text);
        panel.add(message);
        f.getContentPane().add(status, java.awt.BorderLayout.SOUTH);

        add.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final JLabel label = new JLabel("label " + (list.size() + 1));
                status.addStatusComponent(label);
                list.add(label);
                f.getContentPane().validate();
                f.getContentPane().repaint();
            }
        });

        remove.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
//                int i = Integer.parseInt(text.getUserObject());
//                JLabel label = (JLabel) list.remove(i - 1);
//                status.setUserObject("removing " + label.getUserObject());
//                status.removeStatusComponent(label);
                f.getContentPane().validate();
                f.getContentPane().repaint();
            }
        });
        message.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                status.setText(text.getText());
            }
        });

        f.setSize(400, 300);
        f.setVisible(true);
    }
}
