/*
 * Created by JFormDesigner on Fri Feb 18 12:07:13 CET 2011
 */

package com.ethz.geobot.herbar.gui.picture;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextPane;
import javax.swing.UIManager;

/** @author Daniel Frey */
public class ErrorHandlingDialog extends JDialog
{
    public ErrorHandlingDialog()
    {
        initComponents();
        getRootPane().setDefaultButton( okButton );
    }

    private void okActionPerformed()
    {
        setVisible( false );
        dispose();
    }

    public JRadioButton getDeletePercentageCheckBox()
    {
        return deletePercentageCheckBox;
    }

    public JRadioButton getRunInMemoryCheckBox()
    {
        return runInMemoryCheckBox;
    }

    public JRadioButton getDoNothing()
    {
        return doNothing;
    }

    private void deletePercentageCheckBoxItemStateChanged()
    {
        // TODO add your code here
    }

    private void runInMemoryCheckBoxItemStateChanged()
    {
        // TODO add your code here
    }

    private void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        JPanel panel4 = new JPanel();
        JPanel panel3 = new JPanel();
        JLabel label1 = new JLabel();
        JTextPane textPane3 = new JTextPane();
        doNothing = new JRadioButton();
        JTextPane textPane4 = new JTextPane();
        runInMemoryCheckBox = new JRadioButton();
        JTextPane textPane1 = new JTextPane();
        deletePercentageCheckBox = new JRadioButton();
        JTextPane textPane2 = new JTextPane();
        JPanel panel1 = new JPanel();
        okButton = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setModal( true );
        setTitle( "Fehler beim Zwischenspeichern" );
        Container contentPane = getContentPane();
        contentPane.setLayout( new FormLayout(
                "default:grow",
                "fill:default:grow" ) );

        //======== panel4 ========
        {
            panel4.setBorder( Borders.createEmptyBorder( "10dlu, 10dlu, 10dlu, 10dlu" ) );
            panel4.setPreferredSize( new Dimension( 500, 300 ) );
            panel4.setLayout( new FormLayout(
                    "default",
                    "fill:default:grow" ) );

            //======== panel3 ========
            {
                panel3.setLayout( new FormLayout(
                        "2*(default, $lcgap), default",
                        "4*(default, $lgap), fill:default:grow, $lgap, default" ) );

                //---- label1 ----
                label1.setIcon( UIManager.getIcon( "OptionPane.errorIcon" ) );
                panel3.add( label1, cc.xy( 1, 1 ) );

                //---- textPane3 ----
                textPane3.setEditable( false );
                textPane3.setText( "Die Systempartition, auf der die Bilder vom Internet zwischengespeichert werden, scheint voll zu sein. Sie haben mehrere M\u00f6glichkeiten:" );
                textPane3.setOpaque( false );
                textPane3.setFocusable( false );
                panel3.add( textPane3, cc.xywh( 3, 1, 3, 1 ) );

                //---- doNothing ----
                doNothing.setSelected( true );
                doNothing.addItemListener( new ItemListener()
                {
                    public void itemStateChanged( ItemEvent e )
                    {
                        deletePercentageCheckBoxItemStateChanged();
                    }
                } );
                panel3.add( doNothing, cc.xy( 3, 3 ) );

                //---- textPane4 ----
                textPane4.setEditable( false );
                textPane4.setText( "Sie k\u00f6nnen diese Option w\u00e4hlen und selbst daf\u00fcr sorgen, dass mehr Platz f\u00fcr den lokalen Zwischenspeicher auf der Systempartition zur Verf\u00fcgung gestellt wird. Falls nicht, wird diese Fehlermeldung ziemlich sicher bald wieder erscheinen." );
                textPane4.setOpaque( false );
                textPane4.setFocusable( false );
                panel3.add( textPane4, cc.xy( 5, 3 ) );

                //---- runInMemoryCheckBox ----
                runInMemoryCheckBox.addItemListener( new ItemListener()
                {
                    public void itemStateChanged( ItemEvent e )
                    {
                        runInMemoryCheckBoxItemStateChanged();
                    }
                } );
                panel3.add( runInMemoryCheckBox, cc.xy( 3, 5 ) );

                //---- textPane1 ----
                textPane1.setEditable( false );
                textPane1.setText( "Sie k\u00f6nnen ohne lokalen Zwischenspeicher weiterarbeiten. Bilder werden damit immer wieder aus dem Internet heruntergeladen und nicht lokal zwischengespeichert. Gilt bis zum n\u00e4chsten Neustart des Programms." );
                textPane1.setOpaque( false );
                textPane1.setFocusable( false );
                panel3.add( textPane1, cc.xy( 5, 5 ) );

                //---- deletePercentageCheckBox ----
                deletePercentageCheckBox.addItemListener( new ItemListener()
                {
                    public void itemStateChanged( ItemEvent e )
                    {
                        deletePercentageCheckBoxItemStateChanged();
                    }
                } );
                panel3.add( deletePercentageCheckBox, cc.xy( 3, 7 ) );

                //---- textPane2 ----
                textPane2.setEditable( false );
                textPane2.setText( "Sie k\u00f6nnen den lokalen Zwischenspeicher l\u00f6schen lassen. Die Bilder werde dann wieder automatisch aus dem Internet heruntergeladen und der lokale Zwischenspeicher wird langsam wieder gef\u00fcllt. Es wird eine Zeit gehen, bis er wieder ans Limit st\u00f6sst und diese Fehlermeldung erneut erscheint." );
                textPane2.setOpaque( false );
                textPane2.setFocusable( false );
                panel3.add( textPane2, cc.xy( 5, 7 ) );

                //======== panel1 ========
                {
                    panel1.setLayout( new FormLayout(
                            "default:grow, $lcgap, [50dlu,default]",
                            "default" ) );

                    //---- okButton ----
                    okButton.setText( "OK" );
                    okButton.addActionListener( new ActionListener()
                    {
                        public void actionPerformed( ActionEvent e )
                        {
                            okActionPerformed();
                        }
                    } );
                    panel1.add( okButton, cc.xy( 3, 1 ) );
                }
                panel3.add( panel1, cc.xywh( 1, 11, 5, 1 ) );
            }
            panel4.add( panel3, cc.xy( 1, 1 ) );
        }
        contentPane.add( panel4, cc.xy( 1, 1 ) );
        pack();
        setLocationRelativeTo( getOwner() );

        //---- buttonGroup1 ----
        ButtonGroup buttonGroup1 = new ButtonGroup();
        buttonGroup1.add( doNothing );
        buttonGroup1.add( runInMemoryCheckBox );
        buttonGroup1.add( deletePercentageCheckBox );
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JRadioButton doNothing;

    private JRadioButton runInMemoryCheckBox;

    private JRadioButton deletePercentageCheckBox;

    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
