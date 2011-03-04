/*
 * Created by JFormDesigner on Fri Mar 04 10:03:51 CET 2011
 */

package com.ethz.geobot.herbar.gui;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

/** @author Daniel Frey */
public class EnvironmentDialog extends JDialog
{
    public EnvironmentDialog( Frame owner )
    {
        super( owner );
        initComponents();
        getRootPane().setDefaultButton( okButton );
    }

    private void setChoice1()
    {
        scientificRadio.setEnabled( systematicRadio.isSelected() );
        germanRadio.setEnabled( systematicRadio.isSelected() );
    }

    public boolean ok = false;

    private void okButtonActionPerformed()
    {
        ok = true;
        this.dispose();
    }

    private void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        JPanel panel1 = new JPanel();
        textField1 = new JTextArea();
        JPanel panel2 = new JPanel();
        systematicRadio = new JRadioButton();
        JTextArea textField2 = new JTextArea();
        scientificRadio = new JRadioButton();
        germanRadio = new JRadioButton();
        dendroRadio = new JRadioButton();
        JTextArea textField3 = new JTextArea();
        JPanel buttonBar = new JPanel();
        okButton = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setTitle( "Auswahl des Themenbereiches" );
        setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );
        setAlwaysOnTop( true );
        setModal( true );
        Container contentPane = getContentPane();
        contentPane.setLayout( new BorderLayout() );

        //======== dialogPane ========
        {
            dialogPane.setLayout( new BorderLayout() );

            //======== contentPanel ========
            {
                contentPanel.setLayout( new FormLayout(
                        "7dlu, default:grow, 7dlu",
                        "default, 7dlu, fill:default:grow, default, 7dlu" ) );

                //======== panel1 ========
                {
                    panel1.setBackground( Color.white );
                    panel1.setBorder( Borders.DIALOG_BORDER );
                    panel1.setLayout( new FormLayout(
                            "default:grow",
                            "default" ) );

                    //---- textField1 ----
                    textField1.setText( "Sie haben zwei Themenbereiche (Systematik und Dendrologie) zur Auswahl, mit denen Sie die Lernumgebung starten k\u00f6nnen. Bitte w\u00e4hlen Sie den gew\u00fcnschten Bereich aus und pr\u00e4zisieren Sie gegebenenfalls die Details (Wissenschaftlich oder Deutsch)." );
                    textField1.setLineWrap( true );
                    textField1.setWrapStyleWord( true );
                    textField1.setOpaque( false );
                    textField1.setEditable( false );
                    panel1.add( textField1, cc.xy( 1, 1 ) );
                }
                contentPanel.add( panel1, cc.xywh( 1, 1, 3, 1 ) );

                //======== panel2 ========
                {
                    panel2.setLayout( new FormLayout(
                            "10dlu, $lcgap, default:grow",
                            "2*(default, $lgap), default, $ugap, default" ) );

                    //---- systematicRadio ----
                    systematicRadio.setSelected( true );
                    systematicRadio.addActionListener( new ActionListener()
                    {
                        public void actionPerformed( ActionEvent e )
                        {
                            setChoice1();
                        }
                    } );
                    panel2.add( systematicRadio, cc.xy( 1, 1 ) );

                    //---- textField2 ----
                    textField2.setText( "Systematik: W\u00e4hlen Sie diese Option, falls Sie Ihre Systematikkenntnisse von Pflanzen erweitern oder auffrischen wollen. Sie haben zwei M\u00f6glichkeiten, wie die Taxonnamen gehandhabt werden:" );
                    textField2.setLineWrap( true );
                    textField2.setWrapStyleWord( true );
                    textField2.setOpaque( false );
                    textField2.setEditable( false );
                    panel2.add( textField2, cc.xy( 3, 1 ) );

                    //---- scientificRadio ----
                    scientificRadio.setText( "Wissenschaftliche Namen" );
                    scientificRadio.setSelected( true );
                    panel2.add( scientificRadio, cc.xy( 3, 3 ) );

                    //---- germanRadio ----
                    germanRadio.setText( "Deutsche Namen" );
                    panel2.add( germanRadio, cc.xy( 3, 5 ) );

                    //---- dendroRadio ----
                    dendroRadio.addActionListener( new ActionListener()
                    {
                        public void actionPerformed( ActionEvent e )
                        {
                            setChoice1();
                        }
                    } );
                    panel2.add( dendroRadio, cc.xy( 1, 7 ) );

                    //---- textField3 ----
                    textField3.setText( "Dendrologie: Hier erhalten Sie speziellen Einblick in die Dendrologie mit Fokus auf Artkenntnis." );
                    textField3.setLineWrap( true );
                    textField3.setWrapStyleWord( true );
                    textField3.setOpaque( false );
                    textField3.setEditable( false );
                    panel2.add( textField3, cc.xy( 3, 7 ) );
                }
                contentPanel.add( panel2, cc.xy( 2, 3 ) );

                //======== buttonBar ========
                {
                    buttonBar.setBorder( Borders.BUTTON_BAR_GAP_BORDER );
                    buttonBar.setLayout( new FormLayout(
                            "$glue, $button",
                            "pref" ) );

                    //---- okButton ----
                    okButton.setText( "OK" );
                    okButton.addActionListener( new ActionListener()
                    {
                        public void actionPerformed( ActionEvent e )
                        {
                            okButtonActionPerformed();
                        }
                    } );
                    buttonBar.add( okButton, cc.xy( 2, 1 ) );
                }
                contentPanel.add( buttonBar, cc.xy( 2, 4 ) );
            }
            dialogPane.add( contentPanel, BorderLayout.CENTER );
        }
        contentPane.add( dialogPane, BorderLayout.CENTER );
        setSize( 535, 375 );
        setLocationRelativeTo( null );

        //---- buttonGroup1 ----
        ButtonGroup buttonGroup1 = new ButtonGroup();
        buttonGroup1.add( systematicRadio );
        buttonGroup1.add( dendroRadio );

        //---- buttonGroup2 ----
        ButtonGroup buttonGroup2 = new ButtonGroup();
        buttonGroup2.add( scientificRadio );
        buttonGroup2.add( germanRadio );
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;

    private JPanel contentPanel;

    private JTextArea textField1;

    public JRadioButton systematicRadio;

    public JRadioButton scientificRadio;

    public JRadioButton germanRadio;

    public JRadioButton dendroRadio;

    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
