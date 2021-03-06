/*
 * Copyright (c) 2011.
 *
 * Nutzung und Rechte
 *
 * Die Applikation eBot wurde f\u00fcr Studierende der ETH Z\u00fcrich entwickelt. Sie  steht
 * allen   an   Hochschulen  oder   Fachhochschulen   eingeschriebenen Studierenden
 * (auch  ausserhalb  der  ETH  Z\u00fcrich)  f\u00fcr  nichtkommerzielle  Zwecke  im Studium
 * kostenlos zur Verf\u00fcgung. Nichtstudierende Privatpersonen, die die Applikation zu
 * ihrer  pers\u00fcnlichen  Weiterbildung  nutzen  m\u00fcchten,  werden  gebeten,  f\u00fcr  die
 * nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.\u00fc zu bezahlen.
 *
 * Postkonto
 *
 * Unterricht, 85-761469-0, Vermerk "eBot"
 * IBAN 59 0900 0000 8576  1469 0; BIC POFICHBEXXX
 *
 * Jede andere Nutzung der Applikation  ist vorher mit dem Projektleiter  (Matthias
 * Baltisberger, Email:  balti@ethz.ch) abzusprechen  und mit  einer entsprechenden
 * Vereinbarung zu regeln. Die  Applikation wird ohne jegliche  Garantien bez\u00fcglich
 * Nutzungsanspr\u00fcchen zur Verf\u00fcgung gestellt.
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
import net.infonode.util.Alignment;

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
        setText( textPane2, "Im <b>Themenbereich Systematik</b> stehen insgesamt 632 Taxa auf dem Art-Level zur Verf\u00fcgung (1 Pilz, 4 Flechten, 5 Moose, 19 Farne, 12 Gymnospermen, 591 Angiospermen). Dieser Gesamtstoff kann je nach Lernziel \u00fcber verschiedene Stofflisten eingeschr\u00e4nkt werden:" );
        setText( textPane3, "Liste �200 CH� enth�lt jene 200 Arten, die von der Zertifizierungskommission f�r das Zertifikat 200 festgelegt wurden." );
        setText( textPane4, "Liste �200 Leuchtmann� enth�lt 200 Arten; diese sind Pr�fungsstoff (Teil Artenkenntnis) f�r Studierende der Biologie, Pharmazie und Umweltnaturwissenschaften an der ETH Z�rich (4. Semester, Prof. Dr. Adrian Leuchtmann)." +
                "" );
        setText( textPane5, "Die Liste �200 ZH� ist die Z�rcher Liste f�r das Zertifikat Stufe 200; diese Liste �200 ZH� unterscheidet sich von der Liste �200 CH� in 29 Arten." );
        setText( textPane6, "Weitere Listen enthalten die f�r die Zertifizierungsstufen verlangten Arten: Liste �400� die Arten f�r das Zertifikat Stufe 400 resp. Liste �600� die Arten f�r das Zertifikat Stufe 600 (Arten f�r das Zertifikat Stufe 200 siehe Listen �200 CH� und �200 ZH�). Die Listen �400�200 ZH�, �600�200 ZH� und �600�400� dienen dem stufenweisen Lernen (Einschr�nkung auf die bei den Stufen hinzukommenden Arten)." );
        setText( textPane7, "Liste �600 Zeiger� umfasst jene Arten der Liste �600�, die f�r die Feuchtezahl (F), die Reaktionszahl (R, pH-Wert des Bodens) und die N�hrstoffzahl (N) extreme Zeigerwerte aufweisen. F1: sehr trocken, F1.5: trocken, F4.5: nass, F5: �berschwemmt bzw. unter Wasser; R1: stark sauer, R5: basisch; N1: sehr n�hrstoffarm, N5: sehr n�hrstoffreich bis �berd�ngt.\n" +
                "Bitte beachten Sie: Arten mit Zeigerwerten 2 oder 4 k�nnen ebenfalls gute Zeigerarten sein; diese Arten werden in eBot als �m�ssige Zeiger� bezeichnet.\n" );
        setText( textPane8, "Liste �PHARMBIO� enth�lt 79 Arten; diese sind Pr�fungsstoff in der Veranstaltung �Pharmazeutische Biologie� (Prof. Dr. Karl�Heinz Altmann)." );
        setText( textPane9, "Liste �Alle� ist die vollst�ndige Liste mit allen 632 Taxa in eBot." );
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
        return Preferences.userRoot().node( "ebot" ).node( "copyright" );
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
        // Generated using JFormDesigner Evaluation license - Daniel Frey
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
        label6 = new JLabel();
        textPane8 = new JTextPane();
        label7 = new JLabel();
        textPane9 = new JTextPane();
        JPanel buttonBar2 = new JPanel();
        okButton = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setTitle("Auswahl des Themenbereiches");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setModal(true);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {

            // JFormDesigner evaluation mark
            dialogPane.setBorder(new javax.swing.border.CompoundBorder(
                new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                    "", javax.swing.border.TitledBorder.CENTER,
                    javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                    java.awt.Color.red), dialogPane.getBorder())); dialogPane.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new FormLayout(
                    "7dlu, default:grow, 7dlu",
                    "default, 7dlu, fill:default:grow, 7dlu"));

                //======== panel1 ========
                {
                    panel1.setBackground(Color.white);
                    panel1.setBorder(Borders.DIALOG_BORDER);
                    panel1.setLayout(new FormLayout(
                        "default:grow",
                        "default"));

                    //---- textField4 ----
                    textField4.setText("Hier best\u00e4tigen Sie das Copyright und w\u00e4hlen den Themenbereich");
                    textField4.setLineWrap(true);
                    textField4.setWrapStyleWord(true);
                    textField4.setOpaque(false);
                    textField4.setEditable(false);
                    panel1.add(textField4, cc.xy(1, 1));
                }
                contentPanel.add(panel1, cc.xywh(1, 1, 3, 1));

                //======== tabbedPane1 ========
                {

                    //======== panel3 ========
                    {
                        panel3.setBorder(Borders.DIALOG_BORDER);
                        panel3.setLayout(new FormLayout(
                            "default:grow",
                            "fill:default:grow, $lgap, default"));

                        //---- textPane ----
                        textPane.setOpaque(false);
                        textPane.setEditable(false);
                        panel3.add(textPane, cc.xy(1, 1));

                        //======== buttonBar ========
                        {
                            buttonBar.setBorder(Borders.createEmptyBorder("0dlu, 0dlu, 0dlu, 0dlu"));
                            buttonBar.setLayout(new FormLayout(
                                "$glue, $button",
                                "pref"));

                            //---- acceptButton ----
                            acceptButton.setText("Akzeptieren");
                            acceptButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    acceptButtonActionPerformed();
                                }
                            });
                            buttonBar.add(acceptButton, cc.xy(2, 1));
                        }
                        panel3.add(buttonBar, cc.xy(1, 3));
                    }
                    tabbedPane1.addTab("Copyright", panel3);

                    //======== panel2 ========
                    {
                        panel2.setBorder(Borders.DIALOG_BORDER);
                        panel2.setLayout(new FormLayout(
                            "10dlu, $lcgap, default:grow",
                            "4*(default, $lgap), 6*(top:default, $lgap), default, $lgap, fill:default:grow, $lgap, default"));

                        //---- textField1 ----
                        textField1.setText("Sie haben zwei Themenbereiche (Systematik und Dendrologie) zur Auswahl, mit denen Sie die Lernumgebung starten k\u00f6nnen. Bitte w\u00e4hlen Sie den gew\u00fcnschten Bereich aus.");
                        textField1.setLineWrap(true);
                        textField1.setWrapStyleWord(true);
                        textField1.setOpaque(false);
                        textField1.setEditable(false);
                        panel2.add(textField1, cc.xywh(1, 1, 3, 1));

                        //---- systematicRadio ----
                        systematicRadio.setSelected(true);
                        systematicRadio.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                setChoice1();
                            }
                        });
                        panel2.add(systematicRadio, cc.xy(1, 3));

                        //---- textField2 ----
                        textField2.setText("Systematik: W\u00e4hlen Sie diese Option, falls Sie Ihre wissenschaftlichen Systematikkenntnisse von Pflanzen erweitern oder auffrischen wollen.");
                        textField2.setLineWrap(true);
                        textField2.setWrapStyleWord(true);
                        textField2.setOpaque(false);
                        textField2.setEditable(false);
                        panel2.add(textField2, cc.xy(3, 3));

                        //---- dendroRadio ----
                        dendroRadio.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                setChoice1();
                            }
                        });
                        panel2.add(dendroRadio, cc.xy(1, 5));

                        //---- textField3 ----
                        textField3.setText("Dendrologie: Hier erhalten Sie speziellen Einblick in die Dendrologie mit Fokus auf Artkenntnis.");
                        textField3.setLineWrap(true);
                        textField3.setWrapStyleWord(true);
                        textField3.setOpaque(false);
                        textField3.setEditable(false);
                        panel2.add(textField3, cc.xy(3, 5));

                        //---- textPane2 ----
                        textPane2.setOpaque(false);
                        textPane2.setEditable(false);
                        panel2.add(textPane2, cc.xywh(1, 7, 3, 1));

                        //---- label1 ----
                        label1.setText("-");
                        label1.setHorizontalAlignment(SwingConstants.RIGHT);
                        panel2.add(label1, cc.xy(1, 9));

                        //---- textPane3 ----
                        textPane3.setOpaque(false);
                        textPane3.setEditable(false);
                        panel2.add(textPane3, cc.xy(3, 9));

                        //---- label2 ----
                        label2.setText("-");
                        label2.setHorizontalAlignment(SwingConstants.RIGHT);
                        panel2.add(label2, cc.xy(1, 11));

                        //---- textPane4 ----
                        textPane4.setOpaque(false);
                        textPane4.setEditable(false);
                        panel2.add(textPane4, cc.xy(3, 11));

                        //---- label3 ----
                        label3.setText("-");
                        label3.setHorizontalAlignment(SwingConstants.RIGHT);
                        panel2.add(label3, cc.xy(1, 13));

                        //---- textPane5 ----
                        textPane5.setOpaque(false);
                        textPane5.setEditable(false);
                        panel2.add(textPane5, cc.xy(3, 13));

                        //---- label4 ----
                        label4.setText("-");
                        label4.setHorizontalAlignment(SwingConstants.RIGHT);
                        panel2.add(label4, cc.xy(1, 15));

                        //---- textPane6 ----
                        textPane6.setOpaque(false);
                        textPane6.setEditable(false);
                        panel2.add(textPane6, cc.xy(3, 15));

                        //---- label5 ----
                        label5.setText("-");
                        label5.setHorizontalAlignment(SwingConstants.RIGHT);
                        panel2.add(label5, cc.xy(1, 17));

                        //---- textPane7 ----
                        textPane7.setOpaque(false);
                        textPane7.setEditable(false);
                        panel2.add(textPane7, cc.xy(3, 17));

                        //---- label6 ----
                        label6.setText("-");
                        label6.setHorizontalAlignment(SwingConstants.RIGHT);
                        panel2.add(label6, cc.xy(1, 19));

                        //---- textPane8 ----
                        textPane8.setOpaque(false);
                        textPane8.setEditable(false);
                        panel2.add(textPane8, cc.xy(3, 19));

                        //---- label7 ----
                        label7.setText("-");
                        label7.setHorizontalAlignment(SwingConstants.RIGHT);
                        panel2.add(label7, cc.xy(1, 21));

                        //---- textPane9 ----
                        textPane9.setOpaque(false);
                        textPane9.setEditable(false);
                        panel2.add(textPane9, cc.xy(3, 21));

                        //======== buttonBar2 ========
                        {
                            buttonBar2.setBorder(Borders.createEmptyBorder("0dlu, 0dlu, 0dlu, 0dlu"));
                            buttonBar2.setLayout(new FormLayout(
                                "$glue, $button",
                                "pref"));

                            //---- okButton ----
                            okButton.setText("OK");
                            okButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    okButtonActionPerformed();
                                }
                            });
                            buttonBar2.add(okButton, cc.xy(2, 1));
                        }
                        panel2.add(buttonBar2, cc.xywh(1, 25, 3, 1));
                    }
                    tabbedPane1.addTab("Auswahl", panel2);
                }
                contentPanel.add(tabbedPane1, cc.xy(2, 3));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        setSize(635, 750);
        setLocationRelativeTo(null);

        //---- buttonGroup1 ----
        ButtonGroup buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(systematicRadio);
        buttonGroup1.add(dendroRadio);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Daniel Frey
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
    private JLabel label6;
    private JTextPane textPane8;
    private JLabel label7;
    private JTextPane textPane9;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
