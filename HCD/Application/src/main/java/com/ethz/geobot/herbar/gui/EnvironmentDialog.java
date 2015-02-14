/*
 * Copyright (c) 2011.
 *
 * Nutzung und Rechte
 *
 * Die Applikation eBot wurde für Studierende der ETH Zürich entwickelt. Sie  steht
 * allen   an   Hochschulen  oder   Fachhochschulen   eingeschriebenen Studierenden
 * (auch  ausserhalb  der  ETH  Zürich)  für  nichtkommerzielle  Zwecke  im Studium
 * kostenlos zur Verfügung. Nichtstudierende Privatpersonen, die die Applikation zu
 * ihrer  persünlichen  Weiterbildung  nutzen  müchten,  werden  gebeten,  für  die
 * nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.ü zu bezahlen.
 *
 * Postkonto
 *
 * Unterricht, 85-761469-0, Vermerk "eBot"
 * IBAN 59 0900 0000 8576  1469 0; BIC POFICHBEXXX
 *
 * Jede andere Nutzung der Applikation  ist vorher mit dem Projektleiter  (Matthias
 * Baltisberger, Email:  balti@ethz.ch) abzusprechen  und mit  einer entsprechenden
 * Vereinbarung zu regeln. Die  Applikation wird ohne jegliche  Garantien bezüglich
 * Nutzungsansprüchen zur Verfügung gestellt.
 */

/*
 * Created by JFormDesigner on Fri Mar 04 10:03:51 CET 2011
 */
package com.ethz.geobot.herbar.gui;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.uif.util.SystemUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.text.html.HTMLDocument;

/**
 * @author Daniel Frey
 */
public class EnvironmentDialog extends JDialog
{
    private static final String PREFS_KEY = "accepted";

    public EnvironmentDialog( final Frame owner )
    {
        super( owner );
        initComponents();
        initCustomize();
        initCopyright();
    }

    private void initCopyright()
    {
        final boolean accepted = getPreferences().getBoolean( PREFS_KEY, false );
        tabbedPane1.setEnabledAt( 1, accepted );
        okButton.setEnabled( accepted );
        acceptButton.setEnabled( !accepted );
    }

    private void initCustomize()
    {
        getRootPane().setDefaultButton( okButton );
        setText( textPane, "<h3>Nutzung und Rechte</h3><p> Die Applikation eBot wurde f\u00fcr Studierende der ETH Z\u00fcrich entwickelt. Sie steht allen an Hochschulen oder Fachhochschulen eingeschriebenen Studierenden (auch ausserhalb der ETH Z\u00fcrich) f\u00fcr nichtkommerzielle Zwecke im Studium kostenlos zur Verf\u00fcgung. Nichtstudierende Privatpersonen, die die Applikation zu ihrer pers\u00f6nlichen Weiterbildung nutzen m\u00f6chten, werden gebeten, f\u00fcr die nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.- zu bezahlen.</p><p><b>Postkonto: Matthias Baltisberger, 45-681486-7<br>IBAN: CH91 0900 0000 4568 1486 7</b><p>Jede andere Nutzung der Applikation ist vorher mit dem Projektleiter (Matthias Baltisberger, Email: balti@ethz.ch) abzusprechen und mit einer entsprechenden Vereinbarung zu regeln. Die Applikation wird ohne jegliche Garantien bez\u00fcglich Nutzungsanspr\u00fcchen zur Verf\u00fcgung gestellt." );
        setText( textPane2, "Im <b>Themenbereich Systematik</b> stehen insgesamt 609 Taxa auf dem Art-Level zur Verf\u00fcgung (4 Flechten, 5 Moose, 18 Farne, 12 Gymnospermen und 570 Angiospermen). Dieser Gesamtstoff kann je nach Lernziel \u00fcber verschiedene Stofflisten eingeschr\u00e4nkt werden:" );
        setText( textPane3, "Liste 60 enth\u00e4lt 60 Arten; diese sind Pr\u00fcfungsstoff f\u00fcr die Semesterpr\u00fcfung (Wahlfach) von Studierenden in Agrar-, Lebensmittel- und Umweltnaturwissenschaften an der ETH Z\u00fcrich (Prof. Dr. Adrian Leuchtmann)." );
        setText( textPane4, "Liste 200 enth\u00e4lt 200 Arten; diese sind Pr\u00fcfungsstoff (Teil Artenkenntnis) f\u00fcr Studierende der Biologie und Pharmazie an der ETH Z\u00fcrich. Die Liste 200 ist auch identisch mit der Z\u00fcrcher Liste f\u00fcr die Zertifizierungsstufe 200." );
        setText( textPane5, "Liste 400 enth\u00e4lt die f\u00fcr die Zertifizierungsstufe 400 verlangten Arten." );
        setText( textPane6, "Liste 600 enth\u00e4lt die f\u00fcr die Zertifizierungsstufe 600 verlangten Arten." );
        setText( textPane7, "Alle Taxa ist die vollst\u00e4ndige Liste mit allen 609 Taxa in eBot." );
        demoRadio.setEnabled( SystemUtils.IS_OS_WINDOWS );
        demoText.setEnabled( SystemUtils.IS_OS_WINDOWS );
    }

    private void setText( final JTextPane field, final String text )
    {
        field.setContentType( "text/html" );
        final Font font = UIManager.getFont( "Label.font" );
        final String bodyRule = "body { font-family: " + font.getFamily() + "; font-size: " + font.getSize() + "pt; }";
        ((HTMLDocument) field.getDocument()).getStyleSheet().addRule( bodyRule );
        field.setText( text );
    }

    private Preferences getPreferences()
    {
        return Preferences.userRoot().node( "eBot" ).node( "copyright" );
    }

    public boolean ok = false;

    private void okButtonActionPerformed()
    {
        ok = true;
        this.dispose();
    }

    private void acceptButtonActionPerformed()
    {
        getPreferences().putBoolean( PREFS_KEY, true );
        initCopyright();
    }

    private void setChoice1()
    {
        // TODO add your code here
    }

    private void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        JPanel panel1 = new JPanel();
        textField4 = new JTextArea();
        tabbedPane1 = new JTabbedPane();
        panel3 = new JPanel();
        textPane = new JTextPane();
        JPanel buttonBar = new JPanel();
        acceptButton = new JButton();
        JPanel panel2 = new JPanel();
        textField1 = new JTextArea();
        systematicRadio = new JRadioButton();
        JTextArea textField2 = new JTextArea();
        dendroRadio = new JRadioButton();
        JTextArea textField3 = new JTextArea();
        textField5 = new JTextArea();
        demoRadio = new JRadioButton();
        demoText = new JTextArea();
        textPane2 = new JTextPane();
        label1 = new JLabel();
        textPane3 = new JTextPane();
        label2 = new JLabel();
        textPane4 = new JTextPane();
        label3 = new JLabel();
        textPane5 = new JTextPane();
        label4 = new JLabel();
        textPane6 = new JTextPane();
        label5 = new JLabel();
        textPane7 = new JTextPane();
        JPanel buttonBar2 = new JPanel();
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
                        "default, 7dlu, fill:default:grow, 7dlu" ) );

                //======== panel1 ========
                {
                    panel1.setBackground( Color.white );
                    panel1.setBorder( Borders.DIALOG_BORDER );
                    panel1.setLayout( new FormLayout(
                            "default:grow",
                            "default" ) );

                    //---- textField4 ----
                    textField4.setText( "Hier best\u00e4tigen Sie das Copyright und w\u00e4hlen den Themenbereich" );
                    textField4.setLineWrap( true );
                    textField4.setWrapStyleWord( true );
                    textField4.setOpaque( false );
                    textField4.setEditable( false );
                    panel1.add( textField4, cc.xy( 1, 1 ) );
                }
                contentPanel.add( panel1, cc.xywh( 1, 1, 3, 1 ) );

                //======== tabbedPane1 ========
                {

                    //======== panel3 ========
                    {
                        panel3.setBorder( Borders.DIALOG_BORDER );
                        panel3.setLayout( new FormLayout(
                                "default:grow",
                                "fill:default:grow, $lgap, default" ) );

                        //---- textPane ----
                        textPane.setOpaque( false );
                        textPane.setEditable( false );
                        panel3.add( textPane, cc.xy( 1, 1 ) );

                        //======== buttonBar ========
                        {
                            buttonBar.setBorder( Borders.createEmptyBorder( "0dlu, 0dlu, 0dlu, 0dlu" ) );
                            buttonBar.setLayout( new FormLayout(
                                    "$glue, $button",
                                    "pref" ) );

                            //---- acceptButton ----
                            acceptButton.setText( "Akzeptieren" );
                            acceptButton.addActionListener( new ActionListener()
                            {
                                @Override
                                public void actionPerformed( ActionEvent e )
                                {
                                    acceptButtonActionPerformed();
                                }
                            } );
                            buttonBar.add( acceptButton, cc.xy( 2, 1 ) );
                        }
                        panel3.add( buttonBar, cc.xy( 1, 3 ) );
                    }
                    tabbedPane1.addTab( "Copyright", panel3 );

                    //======== panel2 ========
                    {
                        panel2.setBorder( Borders.DIALOG_BORDER );
                        panel2.setLayout( new FormLayout(
                                "10dlu, $lcgap, default:grow",
                                "6*(default, $lgap), 5*(top:default, $lgap), fill:default:grow, 2*($lgap, default)" ) );

                        //---- textField1 ----
                        textField1.setText( "Sie haben zwei Themenbereiche (Systematik und Dendrologie) zur Auswahl, mit denen Sie die Lernumgebung starten k\u00f6nnen. Bitte w\u00e4hlen Sie den gew\u00fcnschten Bereich aus." );
                        textField1.setLineWrap( true );
                        textField1.setWrapStyleWord( true );
                        textField1.setOpaque( false );
                        textField1.setEditable( false );
                        panel2.add( textField1, cc.xywh( 1, 1, 3, 1 ) );

                        //---- systematicRadio ----
                        systematicRadio.setSelected( true );
                        systematicRadio.addActionListener( new ActionListener()
                        {
                            @Override
                            public void actionPerformed( ActionEvent e )
                            {
                                setChoice1();
                            }
                        } );
                        panel2.add( systematicRadio, cc.xy( 1, 3 ) );

                        //---- textField2 ----
                        textField2.setText( "Systematik: W\u00e4hlen Sie diese Option, falls Sie Ihre wissenschaftlichen Systematikkenntnisse von Pflanzen erweitern oder auffrischen wollen." );
                        textField2.setLineWrap( true );
                        textField2.setWrapStyleWord( true );
                        textField2.setOpaque( false );
                        textField2.setEditable( false );
                        panel2.add( textField2, cc.xy( 3, 3 ) );

                        //---- dendroRadio ----
                        dendroRadio.addActionListener( new ActionListener()
                        {
                            @Override
                            public void actionPerformed( ActionEvent e )
                            {
                                setChoice1();
                            }
                        } );
                        panel2.add( dendroRadio, cc.xy( 1, 5 ) );

                        //---- textField3 ----
                        textField3.setText( "Dendrologie: Hier erhalten Sie speziellen Einblick in die Dendrologie mit Fokus auf Artkenntnis." );
                        textField3.setLineWrap( true );
                        textField3.setWrapStyleWord( true );
                        textField3.setOpaque( false );
                        textField3.setEditable( false );
                        panel2.add( textField3, cc.xy( 3, 5 ) );

                        //---- textField5 ----
                        textField5.setText( "Zudem kann man sich mit der an der ETH Z\u00fcrich in Pr\u00fcfungen verwendeten Applikation PMB vertraut machen (Pr\u00fcfungs-Demo)." );
                        textField5.setLineWrap( true );
                        textField5.setWrapStyleWord( true );
                        textField5.setOpaque( false );
                        textField5.setEditable( false );
                        panel2.add( textField5, cc.xywh( 1, 7, 3, 1 ) );

                        //---- demoRadio ----
                        demoRadio.addActionListener( new ActionListener()
                        {
                            @Override
                            public void actionPerformed( ActionEvent e )
                            {
                                setChoice1();
                            }
                        } );
                        panel2.add( demoRadio, cc.xy( 1, 9 ) );

                        //---- demoText ----
                        demoText.setText( "Pr\u00fcfungs-Demo (nur auf Windows)" );
                        demoText.setLineWrap( true );
                        demoText.setWrapStyleWord( true );
                        demoText.setOpaque( false );
                        demoText.setEditable( false );
                        panel2.add( demoText, cc.xy( 3, 9 ) );

                        //---- textPane2 ----
                        textPane2.setOpaque( false );
                        textPane2.setEditable( false );
                        panel2.add( textPane2, cc.xywh( 1, 11, 3, 1 ) );

                        //---- label1 ----
                        label1.setText( "-" );
                        label1.setHorizontalAlignment( SwingConstants.RIGHT );
                        panel2.add( label1, cc.xy( 1, 13 ) );

                        //---- textPane3 ----
                        textPane3.setOpaque( false );
                        textPane3.setEditable( false );
                        panel2.add( textPane3, cc.xy( 3, 13 ) );

                        //---- label2 ----
                        label2.setText( "-" );
                        label2.setHorizontalAlignment( SwingConstants.RIGHT );
                        panel2.add( label2, cc.xy( 1, 15 ) );

                        //---- textPane4 ----
                        textPane4.setOpaque( false );
                        textPane4.setEditable( false );
                        panel2.add( textPane4, cc.xy( 3, 15 ) );

                        //---- label3 ----
                        label3.setText( "-" );
                        label3.setHorizontalAlignment( SwingConstants.RIGHT );
                        panel2.add( label3, cc.xy( 1, 17 ) );

                        //---- textPane5 ----
                        textPane5.setOpaque( false );
                        textPane5.setEditable( false );
                        panel2.add( textPane5, cc.xy( 3, 17 ) );

                        //---- label4 ----
                        label4.setText( "-" );
                        label4.setHorizontalAlignment( SwingConstants.RIGHT );
                        panel2.add( label4, cc.xy( 1, 19 ) );

                        //---- textPane6 ----
                        textPane6.setOpaque( false );
                        textPane6.setEditable( false );
                        panel2.add( textPane6, cc.xy( 3, 19 ) );

                        //---- label5 ----
                        label5.setText( "-" );
                        label5.setHorizontalAlignment( SwingConstants.RIGHT );
                        panel2.add( label5, cc.xy( 1, 21 ) );

                        //---- textPane7 ----
                        textPane7.setOpaque( false );
                        textPane7.setEditable( false );
                        panel2.add( textPane7, cc.xy( 3, 21 ) );

                        //======== buttonBar2 ========
                        {
                            buttonBar2.setBorder( Borders.createEmptyBorder( "0dlu, 0dlu, 0dlu, 0dlu" ) );
                            buttonBar2.setLayout( new FormLayout(
                                    "$glue, $button",
                                    "pref" ) );

                            //---- okButton ----
                            okButton.setText( "OK" );
                            okButton.addActionListener( new ActionListener()
                            {
                                @Override
                                public void actionPerformed( ActionEvent e )
                                {
                                    okButtonActionPerformed();
                                }
                            } );
                            buttonBar2.add( okButton, cc.xy( 2, 1 ) );
                        }
                        panel2.add( buttonBar2, cc.xywh( 1, 25, 3, 1 ) );
                    }
                    tabbedPane1.addTab( "Auswahl", panel2 );
                }
                contentPanel.add( tabbedPane1, cc.xy( 2, 3 ) );
            }
            dialogPane.add( contentPanel, BorderLayout.CENTER );
        }
        contentPane.add( dialogPane, BorderLayout.CENTER );
        setSize( 615, 695 );
        setLocationRelativeTo( null );

        //---- buttonGroup1 ----
        ButtonGroup buttonGroup1 = new ButtonGroup();
        buttonGroup1.add( systematicRadio );
        buttonGroup1.add( dendroRadio );
        buttonGroup1.add( demoRadio );
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JTextArea textField4;
    private JTabbedPane tabbedPane1;
    private JPanel panel3;
    private JTextPane textPane;
    private JButton acceptButton;
    private JTextArea textField1;
    public JRadioButton systematicRadio;
    public JRadioButton dendroRadio;
    private JTextArea textField5;
    public JRadioButton demoRadio;
    private JTextArea demoText;
    private JTextPane textPane2;
    private JLabel label1;
    private JTextPane textPane3;
    private JLabel label2;
    private JTextPane textPane4;
    private JLabel label3;
    private JTextPane textPane5;
    private JLabel label4;
    private JTextPane textPane6;
    private JLabel label5;
    private JTextPane textPane7;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
