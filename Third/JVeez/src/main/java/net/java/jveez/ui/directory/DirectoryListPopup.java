package net.java.jveez.ui.directory;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.util.Collection;
import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.java.jveez.JVeez;
import net.java.jveez.ui.widgets.BlurPanel;
import net.java.jveez.vfs.Directory;
import org.apache.log4j.Logger;

public class DirectoryListPopup extends BlurPanel {

    /** This class logger. */
    private static final Logger LOG = Logger.getLogger(DirectoryListPopup.class);

    private DirectoryListModel listModel = new DirectoryListModel();
    private JList directoryList = new JList(listModel);

    private DirectoryBrowser directoryBrowser;
    private Popup popup;

    private AWTEventListener awtEventListener = new AWTEventListener() {
        public void eventDispatched(AWTEvent event) {
            if (event.getID() == MouseEvent.MOUSE_PRESSED) {
                Object source = event.getSource();
                if (!(source instanceof Component)
                        || DirectoryListPopup.this != SwingUtilities.getAncestorOfClass(DirectoryListPopup.class, (Component) source)) {
                    LOG.warn("CLICKED OUTSIDE !!");
                    hide();
                }
            }
        }
    };

    public DirectoryListPopup(DirectoryBrowser directoryBrowser) {
        this.directoryBrowser = directoryBrowser;

        JScrollPane scrollPane = new JScrollPane(directoryList);
        scrollPane.setBorder(BorderFactory.createEtchedBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.setPreferredSize(new Dimension(300, 200));

        directoryList.setCellRenderer(new DirectoryListCellRenderer());
        directoryList.setOpaque(false);
        directoryList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }

                Directory directory = (Directory) directoryList.getSelectedValue();
                if (directory != null) {
                    DirectoryListPopup.this.directoryBrowser.setCurrentDirectory(directory);
                }
                hide();
            }
        });

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
    }

//  public boolean isOptimizedDrawingEnabled()
//  {
//    return false;
//  }

    public synchronized void show(Collection<? extends Directory> directories, int x, int y) {
        hide();

        setBlurSource(JVeez.getMainFrame().getContentPane());
//    setBlurSource( directoryList );

        listModel.setContent(directories);
        if (popup == null) {
            popup = PopupFactory.getSharedInstance().getPopup(JVeez.getMainFrame(), this, x, y);
            popup.show();
        }

        Toolkit.getDefaultToolkit().addAWTEventListener(awtEventListener, AWTEvent.MOUSE_EVENT_MASK);
    }

    public synchronized void hide() {
        Toolkit.getDefaultToolkit().removeAWTEventListener(awtEventListener);
        if (popup != null) {
            popup.hide();
            popup = null;
        }
    }
}
