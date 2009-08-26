/*
 * Created by JFormDesigner on Tue Apr 10 10:02:30 CEST 2007
 */

package ch.xmatrix.ups.pmb.input;

import ch.jfactory.application.view.status.StatusBar;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;
import com.jgoodies.uif_lite.component.UIFSplitPane;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import net.java.jveez.ui.thumbnails.ThumbnailList;
import net.java.jveez.ui.viewer.ViewerPanel;

/**
 * @author Daniel Frey
 */
public abstract class EntryForm extends JFrame {

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JMenuItem menuItem4;
    protected JTree treeOverview;
    protected JList listOverview;
    protected JList listSpecies;
    private JToolBar toolBar1;
    private JButton button1;
    protected JTree treeSpecies;
    protected JTabbedPane imagesTab;
    protected JTextField fieldName;
    protected JButton renameButton;
    protected JPanel moveButtonsPanel;
    protected ThumbnailList imagesPanel;
    private JPanel panel4;
    private JButton button2;
    protected ViewerPanel imagePanel;
    protected StatusBar statusBar;
    protected JMenuItem selectAll;
    protected JMenuItem copyToClipboard;
    private JCheckBoxMenuItem menuItem3;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public EntryForm() {
        initComponents();
    }

    protected abstract void doShowPrefs();

    protected abstract void doQuit();

    protected abstract void doRename();

    protected abstract void doDelete();

    protected abstract void doCollapseSpeciesTree();

    protected abstract void doExpandSpeciesTree();

    protected abstract void doSelectAll();

    protected abstract void doCopyPath();

    protected abstract void doSwitchRecursiveOverviewList();

    protected abstract void doRemix();

    protected abstract void doSetBigger(ActionEvent e);

    protected abstract void doToBackup();

    protected abstract void doSavePositionAndZoom();

    protected abstract void doClearCache();

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        final JMenuBar menuBar1 = new JMenuBar();
        final JMenu menu1 = new JMenu();
        final JMenuItem menuItem2 = new JMenuItem();
        menuItem4 = new JMenuItem();
        final JMenuItem menuItem1 = new JMenuItem();
        final JPanel panel6 = new JPanel();
        final UIFSplitPane sp2 = new UIFSplitPane();
        final JTabbedPane tabbedPane2 = new JTabbedPane();
        final JPanel panel1 = new JPanel();
        final JToolBar toolBar2 = new JToolBar();
        final JButton button3 = new JButton();
        final JButton button4 = new JButton();
        final JToggleButton toggleButton1 = new JToggleButton();
        final JScrollPane scrollPane1 = new JScrollPane();
        treeOverview = new JTree();
        final JScrollPane scrollPane2 = new JScrollPane();
        listOverview = new JList();
        final JPanel panel5 = new JPanel();
        final JScrollPane scrollPane4 = new JScrollPane();
        listSpecies = new JList();
        toolBar1 = new JToolBar();
        button1 = new JButton();
        final JScrollPane scrollPane3 = new JScrollPane();
        treeSpecies = new JTree();
        final JPanel panel3 = new JPanel();
        imagesTab = new JTabbedPane();
        final JPanel panel8 = new JPanel();
        fieldName = new JTextField();
        renameButton = new JButton();
        moveButtonsPanel = new JPanel();
        final JScrollPane imagesScroll = new JScrollPane();
        imagesPanel = new ThumbnailList();
        final JPanel panel2 = new JPanel();
        panel4 = new JPanel();
        button2 = new JButton();
        imagePanel = new ViewerPanel();
        statusBar = new StatusBar();
        final JPopupMenu popupMenu1 = new JPopupMenu();
        selectAll = new JMenuItem();
        copyToClipboard = new JMenuItem();
        menuItem3 = new JCheckBoxMenuItem();
        final CellConstraints cc = new CellConstraints();

        //======== this ========
        setTitle("Bilder Eingabe");
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        final Container contentPane = getContentPane();
        contentPane.setLayout(new FormLayout(
        	"default:grow",
        	"fill:default:grow"));

        //======== menuBar1 ========
        {

        	//======== menu1 ========
        	{
        		menu1.setText("Datei");
        		menu1.setMnemonic('D');

        		//---- menuItem2 ----
        		menuItem2.setText("Einstellungen...");
        		menuItem2.setMnemonic('E');
        		menuItem2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK));
        		menuItem2.addActionListener(new ActionListener() {
        			public void actionPerformed(final ActionEvent e) {
        				doShowPrefs();
        			}
        		});
        		menu1.add(menuItem2);

        		//---- menuItem4 ----
        		menuItem4.setText("Cache leeren");
        		menuItem4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK));
        		menuItem4.addActionListener(new ActionListener() {
        			public void actionPerformed(final ActionEvent e) {
        				doClearCache();
        			}
        		});
        		menu1.add(menuItem4);

        		//---- menuItem1 ----
        		menuItem1.setText("Beenden");
        		menuItem1.setMnemonic('B');
        		menuItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_MASK));
        		menuItem1.addActionListener(new ActionListener() {
        			public void actionPerformed(final ActionEvent e) {
        				doQuit();
        			}
        		});
        		menu1.add(menuItem1);
        	}
        	menuBar1.add(menu1);
        }
        setJMenuBar(menuBar1);

        //======== panel6 ========
        {
        	panel6.setBorder(Borders.TABBED_DIALOG_BORDER);
        	panel6.setLayout(new BorderLayout());

        	//======== sp2 ========
        	{
        		sp2.setBorder(BorderFactory.createEmptyBorder());

        		//======== tabbedPane2 ========
        		{

        			//======== panel1 ========
        			{
        				panel1.setLayout(new FormLayout(
        					ColumnSpec.decodeSpecs("default:grow"),
        					new RowSpec[] {
        						FormFactory.DEFAULT_ROWSPEC,
        						FormFactory.DEFAULT_ROWSPEC,
        						FormFactory.LINE_GAP_ROWSPEC,
        						new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
        					}));
        				((FormLayout)panel1.getLayout()).setRowGroups(new int[][] {{2, 4}});

        				//======== toolBar2 ========
        				{

        					//---- button3 ----
        					button3.setIcon(new ImageIcon(getClass().getResource("/16x16/fill/tree_expand.png")));
        					button3.setToolTipText("Expandiert alle Knoten");
        					button3.addActionListener(new ActionListener() {
        						public void actionPerformed(final ActionEvent e) {
        							doExpandSpeciesTree();
        						}
        					});
        					toolBar2.add(button3);

        					//---- button4 ----
        					button4.setIcon(new ImageIcon(getClass().getResource("/16x16/fill/tree_collapse.png")));
        					button4.setToolTipText("Kollabiert alle Knoten");
        					button4.addActionListener(new ActionListener() {
        						public void actionPerformed(final ActionEvent e) {
        							doCollapseSpeciesTree();
        						}
        					});
        					toolBar2.add(button4);
        					toolBar2.addSeparator();

        					//---- toggleButton1 ----
        					toggleButton1.setSelectedIcon(new ImageIcon(getClass().getResource("/16x16/fill/go2_up.png")));
        					toggleButton1.setIcon(new ImageIcon(getClass().getResource("/16x16/fill/go2_down.png")));
        					toggleButton1.setToolTipText("Rekursiv alle Bilder anzeigen");
        					toggleButton1.addActionListener(new ActionListener() {
        						public void actionPerformed(final ActionEvent e) {
        							doSwitchRecursiveOverviewList();
        						}
        					});
        					toolBar2.add(toggleButton1);
        				}
        				panel1.add(toolBar2, cc.xy(1, 1));

        				//======== scrollPane1 ========
        				{

        					//---- treeOverview ----
        					treeOverview.setBorder(null);
        					treeOverview.setRootVisible(false);
        					treeOverview.setModel(new DefaultTreeModel(
        						new DefaultMutableTreeNode("<keine Daten vorhanden>") {
        							{
        							}
        						}));
        					treeOverview.setMinimumSize(new Dimension(200, 200));
        					treeOverview.setShowsRootHandles(true);
        					scrollPane1.setViewportView(treeOverview);
        				}
        				panel1.add(scrollPane1, cc.xy(1, 2));

        				//======== scrollPane2 ========
        				{

        					//---- listOverview ----
        					listOverview.setBorder(null);
        					listOverview.setMinimumSize(new Dimension(200, 200));
        					scrollPane2.setViewportView(listOverview);
        				}
        				panel1.add(scrollPane2, cc.xy(1, 4));
        			}
        			tabbedPane2.addTab("\u00dcbersicht", panel1);


        			//======== panel5 ========
        			{
        				panel5.setLayout(new FormLayout(
        					ColumnSpec.decodeSpecs("default:grow"),
        					new RowSpec[] {
        						new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
        						FormFactory.LINE_GAP_ROWSPEC,
        						FormFactory.DEFAULT_ROWSPEC,
        						new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
        					}));
        				((FormLayout)panel5.getLayout()).setRowGroups(new int[][] {{1, 4}});

        				//======== scrollPane4 ========
        				{

        					//---- listSpecies ----
        					listSpecies.setBorder(null);
        					listSpecies.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        					listSpecies.setModel(new AbstractListModel() {
        						String[] values = {
        							" "
        						};
        						public int getSize() { return values.length; }
        						public Object getElementAt(final int i) { return values[i]; }
        					});
        					scrollPane4.setViewportView(listSpecies);
        				}
        				panel5.add(scrollPane4, cc.xy(1, 1));

        				//======== toolBar1 ========
        				{

        					//---- button1 ----
        					button1.setToolTipText("Remix");
        					button1.setIcon(new ImageIcon(getClass().getResource("/16x16/fill/go1_right.png")));
        					button1.addActionListener(new ActionListener() {
        						public void actionPerformed(final ActionEvent e) {
        							doRemix();
        						}
        					});
        					toolBar1.add(button1);
        				}
        				panel5.add(toolBar1, cc.xy(1, 3));

        				//======== scrollPane3 ========
        				{

        					//---- treeSpecies ----
        					treeSpecies.setBorder(null);
        					treeSpecies.setRootVisible(false);
        					treeSpecies.setModel(new DefaultTreeModel(
        						new DefaultMutableTreeNode("<keine Daten vorhanden>") {
        							{
        							}
        						}));
        					treeSpecies.setShowsRootHandles(true);
        					scrollPane3.setViewportView(treeSpecies);
        				}
        				panel5.add(scrollPane3, cc.xy(1, 4));
        			}
        			tabbedPane2.addTab("Arten", panel5);

        		}
        		sp2.setLeftComponent(tabbedPane2);

        		//======== panel3 ========
        		{
        			panel3.setLayout(new FormLayout(
        				new ColumnSpec[] {
        					new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
        					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
        					FormFactory.DEFAULT_COLSPEC,
        					FormFactory.UNRELATED_GAP_COLSPEC,
        					FormFactory.DEFAULT_COLSPEC
        				},
        				RowSpec.decodeSpecs("fill:default:grow")));

        			//======== imagesTab ========
        			{

        				//======== panel8 ========
        				{
        					panel8.setLayout(new FormLayout(
        						new ColumnSpec[] {
        							new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
        							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
        							FormFactory.DEFAULT_COLSPEC,
        							FormFactory.UNRELATED_GAP_COLSPEC,
        							FormFactory.DEFAULT_COLSPEC
        						},
        						new RowSpec[] {
        							FormFactory.DEFAULT_ROWSPEC,
        							FormFactory.LINE_GAP_ROWSPEC,
        							new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
        						}));
        					panel8.add(fieldName, cc.xy(1, 1));

        					//---- renameButton ----
        					renameButton.setText("Umbenennen");
        					renameButton.setEnabled(false);
        					renameButton.addActionListener(new ActionListener() {
        						public void actionPerformed(final ActionEvent e) {
        							doRename();
        						}
        					});
        					panel8.add(renameButton, cc.xy(3, 1));

        					//======== moveButtonsPanel ========
        					{
        						moveButtonsPanel.setLayout(new FlowLayout());
        					}
        					panel8.add(moveButtonsPanel, cc.xy(5, 1));

        					//======== imagesScroll ========
        					{

        						//---- imagesPanel ----
        						imagesPanel.setBorder(null);
        						imagesPanel.setComponentPopupMenu(popupMenu1);
        						imagesScroll.setViewportView(imagesPanel);
        					}
        					panel8.add(imagesScroll, cc.xywh(1, 3, 5, 1));
        				}
        				imagesTab.addTab("Bildersammlung", panel8);


        				//======== panel2 ========
        				{
        					panel2.setLayout(new FormLayout(
        						ColumnSpec.decodeSpecs("default:grow"),
        						new RowSpec[] {
        							FormFactory.DEFAULT_ROWSPEC,
        							FormFactory.RELATED_GAP_ROWSPEC,
        							new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
        						}));

        					//======== panel4 ========
        					{
        						panel4.setLayout(new FormLayout(
        							new ColumnSpec[] {
        								new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
        								FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
        								FormFactory.DEFAULT_COLSPEC
        							},
        							RowSpec.decodeSpecs("default")));

        						//---- button2 ----
        						button2.setText("Position und Zoom speichern");
        						button2.addActionListener(new ActionListener() {
        							public void actionPerformed(final ActionEvent e) {
        								doSavePositionAndZoom();
        							}
        						});
        						panel4.add(button2, cc.xy(3, 1));
        					}
        					panel2.add(panel4, cc.xy(1, 1));

        					//---- imagePanel ----
        					imagePanel.setBorder(new CompoundBorder(
        						Borders.createEmptyBorder("0dlu, 0dlu, 0dlu, 0dlu"),
        						new CompoundBorder(
        							LineBorder.createBlackLineBorder(),
        							Borders.createEmptyBorder("1dlu, 1dlu, 1dlu, 1dlu"))));
        					panel2.add(imagePanel, cc.xy(1, 3));
        				}
        				imagesTab.addTab("Einzelbild", panel2);

        			}
        			panel3.add(imagesTab, cc.xywh(1, 1, 5, 1));
        		}
        		sp2.setRightComponent(panel3);
        	}
        	panel6.add(sp2, BorderLayout.CENTER);
        	panel6.add(statusBar, BorderLayout.SOUTH);
        }
        contentPane.add(panel6, cc.xy(1, 1));
        pack();
        setLocationRelativeTo(getOwner());

        //======== popupMenu1 ========
        {

        	//---- selectAll ----
        	selectAll.setText("Gleichnamige Ausw\u00e4hlen");
        	selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.ALT_MASK));
        	selectAll.addActionListener(new ActionListener() {
        		public void actionPerformed(final ActionEvent e) {
        			doSelectAll();
        		}
        	});
        	popupMenu1.add(selectAll);

        	//---- copyToClipboard ----
        	copyToClipboard.setText("Pfad in die Zwischenablage kopieren");
        	copyToClipboard.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK));
        	copyToClipboard.addActionListener(new ActionListener() {
        		public void actionPerformed(final ActionEvent e) {
        			doCopyPath();
        		}
        	});
        	popupMenu1.add(copyToClipboard);
        	popupMenu1.addSeparator();

        	//---- menuItem3 ----
        	menuItem3.setText("Miniaturen gross");
        	menuItem3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, KeyEvent.CTRL_MASK));
        	menuItem3.addActionListener(new ActionListener() {
        		public void actionPerformed(final ActionEvent e) {
        			doSetBigger(e);
        		}
        	});
        	popupMenu1.add(menuItem3);
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

}
