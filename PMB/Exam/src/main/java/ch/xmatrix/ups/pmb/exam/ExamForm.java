/*
 * Created by JFormDesigner on Tue Apr 24 15:50:04 CEST 2007
 */
package ch.xmatrix.ups.pmb.exam;

import ch.jfactory.application.view.status.StatusBar;
import ch.jfactory.component.ClockPanel;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import net.java.jveez.ui.thumbnails.ThumbnailList;
import net.java.jveez.ui.viewer.ViewerPanel;

/** @author Daniel Frey */
public abstract class ExamForm extends JFrame
{
    private ClockPanel.CountDownProvider targetProvider;

    protected boolean exam = false;

    public ExamForm() throws HeadlessException
    {
        exam = ExamForm.class.getResource( "/test/test.xml" ) != null;

        if ( exam )
        {
            targetProvider = new ClockPanel.CountDownProvider( 30 * 60 * 1000, new ActionListener()
            {
                public void actionPerformed( final ActionEvent e )
                {
                    time.stop();
                    setVisible( false );
                    targetProvider.reset();
                    new StartDialog( ExamForm.this ).setVisible( true );
                    setVisible( true );
                    time.start();
                }
            } );

            addComponentListener( new ComponentAdapter()
            {
                @Override
                public void componentShown( final ComponentEvent e )
                {
                    time.start();
                    removeComponentListener( this );
                }
            } );
        }
        initComponents();
        if ( !exam )
        {
            time.start();
        }
    }

    protected abstract void doSaveOtherPosition();

    protected abstract void doSaveHabitusPosition();

    protected abstract void doQuit();

    protected void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        menuBar = new JMenuBar();
        JMenu menu1 = new JMenu();
        JMenuItem menuItem1 = new JMenuItem();
        JMenuItem menuItem3 = new JMenuItem();
        JMenuItem menuItem4 = new JMenuItem();
        JPanel panel1 = new JPanel();
        listPanel = new JPanel();
        thumbnailScroller = new JScrollPane();
        thumbnailList = new ThumbnailList();
        if ( exam )
        {
            time = new ClockPanel( new ClockPanel.CombinedFormatter( 2 * 60 * 1000 ), targetProvider, 1000 );
        }
        else
        {
            time = new ClockPanel();
        }
        imageLeft = new ViewerPanel();
        JPanel panel2 = new JPanel();
        navigationScroller = new JScrollPane();
        navigation = new JTree();
        imageRight = new ViewerPanel();
        statusBar = new StatusBar();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setUndecorated(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                doQuit();
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== menuBar ========
        {
            menuBar.setVisible(false);

            //======== menu1 ========
            {
                menu1.setText("Datei");
                menu1.setMnemonic('D');

                //---- menuItem1 ----
                menuItem1.setText("Habitus Bildposition speichern");
                menuItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_MASK));
                menuItem1.setMnemonic('H');
                menuItem1.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        doSaveHabitusPosition();
                    }
                });
                menu1.add(menuItem1);

                //---- menuItem3 ----
                menuItem3.setText("Andere Bildposition speichern");
                menuItem3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
                menuItem3.setMnemonic('A');
                menuItem3.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        doSaveOtherPosition();
                    }
                });
                menu1.add(menuItem3);
                menu1.addSeparator();

                //---- menuItem4 ----
                menuItem4.setText("Beenden");
                menuItem4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_MASK));
                menuItem4.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        doQuit();
                    }
                });
                menu1.add(menuItem4);
            }
            menuBar.add(menu1);
        }
        setJMenuBar(menuBar);

        //======== panel1 ========
        {
            panel1.setBorder(new EmptyBorder(5, 5, 5, 5));
            panel1.setBackground(new Color(51, 51, 51));
            panel1.setLayout(new FormLayout(
                "default:grow, $lcgap, [100dlu,default], $lcgap, default:grow",
                "fill:136px, $lgap, fill:default:grow, $lgap, default"));
            ((FormLayout)panel1.getLayout()).setColumnGroups(new int[][] {{1, 5}});

            //======== listPanel ========
            {
                listPanel.setBackground(Color.gray);
                listPanel.setLayout(new FormLayout(
                    "pref:grow, default:grow",
                    "default:grow"));

                //======== thumbnailScroller ========
                {
                    thumbnailScroller.setOpaque(false);
                    thumbnailScroller.setBorder(null);
                    thumbnailScroller.setBackground(Color.gray);

                    //---- thumbnailList ----
                    thumbnailList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    thumbnailList.setBackground(Color.gray);
                    thumbnailList.setOpaque(false);
                    thumbnailScroller.setViewportView(thumbnailList);
                }
                listPanel.add(thumbnailScroller, cc.xy(1, 1));

                //---- time ----
                time.setForeground(Color.orange);
                time.setBackground(Color.gray);
                time.setFont(new Font("SansSerif", Font.BOLD, 20));
                time.setFocusable(false);
                listPanel.add(time, cc.xy(2, 1));
            }
            panel1.add(listPanel, cc.xywh(1, 1, 5, 1));

            //---- imageLeft ----
            imageLeft.setOpaque(false);
            imageLeft.setBackground(Color.gray);
            panel1.add(imageLeft, cc.xy(1, 3));

            //======== panel2 ========
            {
                panel2.setOpaque(false);
                panel2.setBackground(new Color(51, 51, 51));
                panel2.setLayout(new FormLayout(
                    "default:grow",
                    "default"));

                //======== navigationScroller ========
                {
                    navigationScroller.setOpaque(false);
                    navigationScroller.setBorder(null);

                    //---- navigation ----
                    navigation.setBackground(new Color(51, 51, 51));
                    navigation.setForeground(new Color(204, 204, 204));
                    navigation.setRootVisible(false);
                    navigation.setShowsRootHandles(true);
                    navigation.setOpaque(false);
                    navigationScroller.setViewportView(navigation);
                }
                panel2.add(navigationScroller, cc.xy(1, 1));
            }
            panel1.add(panel2, cc.xy(3, 3));

            //---- imageRight ----
            imageRight.setOpaque(false);
            imageRight.setBackground(Color.gray);
            panel1.add(imageRight, cc.xy(5, 3));
            panel1.add(statusBar, cc.xywh(1, 5, 5, 1));
        }
        contentPane.add(panel1, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    protected JMenuBar menuBar;
    protected JPanel listPanel;
    protected JScrollPane thumbnailScroller;
    protected ThumbnailList thumbnailList;
    protected ClockPanel time;
    protected ViewerPanel imageLeft;
    protected JScrollPane navigationScroller;
    protected JTree navigation;
    protected ViewerPanel imageRight;
    protected StatusBar statusBar;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
