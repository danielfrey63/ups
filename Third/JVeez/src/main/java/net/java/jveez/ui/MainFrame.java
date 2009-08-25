/*
 * This project is made available under the terms of the BSD license, more information can be found at : http://www.opensource.org/licenses/bsd-license.html
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * - Neither the name of the java.net website nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright (c) 2004, The JVeez Project Team.
 * All rights reserved.
 */

package net.java.jveez.ui;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import net.java.jveez.config.ConfigurationManager;
import net.java.jveez.ui.fstree.FileSystemTree;
import net.java.jveez.ui.fstree.LazyDirectoryNode;
import net.java.jveez.ui.thumbnails.ThumbnailList;
import net.java.jveez.ui.thumbnails.ThumbnailPanel;
import net.java.jveez.ui.viewer.ViewerPanel;
import net.java.jveez.utils.BuildInfo;

public class MainFrame extends JFrame {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3257572814801547829L;

    private final FileSystemTree fileSystemTree = new FileSystemTree();
    private final JFrame viewerFrame = new JFrame("ViewerFrame");
    private final ThumbnailList thumbnailList = new ThumbnailList();
    private final ViewerPanel viewerPanel = new ViewerPanel(thumbnailList);
    private final ThumbnailPanel thumbnailPanel = new ThumbnailPanel(thumbnailList);
    private final StatusBar statusBar = new StatusBar();

    public MainFrame() throws HeadlessException {
        super();
        setupComponents();
        layoutComponents();
    }

    private void layoutComponents() {
        viewerFrame.add(viewerPanel);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setRightComponent(thumbnailPanel);
        splitPane.setLeftComponent(new JScrollPane(fileSystemTree));
        splitPane.setDividerLocation(200);

        getContentPane().add(splitPane, BorderLayout.CENTER);
        getContentPane().add(statusBar, BorderLayout.SOUTH);

        setBounds(ConfigurationManager.getInstance().getMainFrameConfiguration().getBounds());
        setVisible(true);
    }

    private void setupComponents() {
        setTitle(BuildInfo.TITLE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                quit();
            }
        });
        fileSystemTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        fileSystemTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                TreePath path = e.getPath();
                if (path != null) {
                    LazyDirectoryNode selectedNode = (LazyDirectoryNode) path.getLastPathComponent();
                    thumbnailPanel.setCurrentDirectory(selectedNode != null ? selectedNode.getDirectory() : null);
                }
                else {
                    thumbnailPanel.setCurrentDirectory(null);
                }
            }
        });

//    // setup menu bar
//    JMenu helpMenu = new JMenu("Help");
//    JMenuItem aboutMenuItem = new JMenuItem("About");
//    aboutMenuItem.addActionListener(new ActionListener()
//    {
//      public void actionPerformed(ActionEvent e)
//      {
//        AboutDialog dialog = new AboutDialog();
//        dialog.setVisible(true);
//      }
//    });
//    helpMenu.add(aboutMenuItem);
//    menuBar.add(helpMenu);
//    this.setJMenuBar(menuBar);
    }

    public StatusBar getStatusBar() {
        return statusBar;
    }

    // private methods

    private void quit() {
        boolean promptBeforeQuit = ConfigurationManager.getInstance().getSystemConfiguration().isConfirmBeforeQuit();
        if (promptBeforeQuit) {
            String title = "Quit JVeez?";
            String message = "Are you sure you want to quit JVeez?";
            String checkBoxText = "Do not show this message again";

            JOptionPane optionPane = new JOptionPane(message, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
            JPanel checkBoxPane = new JPanel();
            JCheckBox checkBox = new JCheckBox(checkBoxText);
            checkBoxPane.add(checkBox);
            optionPane.add(checkBoxPane, 1);
            JDialog dialog = optionPane.createDialog(this, title);
            dialog.setVisible(true);

            Object val = optionPane.getValue();
            int rc = JOptionPane.CLOSED_OPTION;
            if (val == null || !(val instanceof Integer)) {
                rc = JOptionPane.CLOSED_OPTION;
            }
            else {
                rc = ((Integer) val).intValue();
            }

            if (rc != JOptionPane.YES_OPTION) {
                return;
            }
            else if (checkBox.isSelected())
            // Only remember the checkbox if they say yes
            {
                ConfigurationManager.getInstance().getSystemConfiguration().setConfirmBeforeQuit(false);
            }
        }

        ConfigurationManager.getInstance().getMainFrameConfiguration().setBounds(getBounds());
        ConfigurationManager.getInstance().flushPendingChanges();
        System.exit(0);
    }
}