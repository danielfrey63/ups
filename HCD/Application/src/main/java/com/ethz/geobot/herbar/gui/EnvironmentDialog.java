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
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.text.html.HTMLDocument;

/** @author Daniel Frey */
public class EnvironmentDialog extends JDialog
{
    private static final String PREFS_KEY = "accepted";

    public EnvironmentDialog( Frame owner )
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
        acceptButton.setEnabled( !accepted );
    }

    private void initCustomize()
    {
        getRootPane().setDefaultButton( okButton );
        textPane.setContentType( "text/html" );
        final Font font = UIManager.getFont( "Label.font" );
        final String bodyRule = "body { font-family: " + font.getFamily() + "; font-size: " + font.getSize() + "pt; }";
        ( (HTMLDocument) textPane.getDocument() ).getStyleSheet().addRule( bodyRule );
        textPane.setText( "<h3>Nutzung und Rechte</h3><p> Die Applikation eBot wurde für Studierende der ETH Zürich entwickelt. Sie steht allen an Hochschulen oder Fachhochschulen eingeschriebenen Studierenden (auch ausserhalb der ETH Zürich) für nichtkommerzielle Zwecke im Studium kostenlos zur Verfügung. Nichtstudierende Privatpersonen, die die Applikation zu ihrer persönlichen Weiterbildung nutzen möchten, werden gebeten, für die nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.– zu bezahlen.</p><p><b>Postkonto: Unterricht, 85-761469-0, Vermerk \"eBot\"<br>IBAN: 59 0900 0000 8576 1469 0<br>BIC: POFICHBEXXX<br></b><p>Jede andere Nutzung der Applikation ist vorher mit dem Projektleiter (Matthias Baltisberger, Email: balti@ethz.ch) abzusprechen und mit einer entsprechenden Vereinbarung zu regeln. Die Applikation wird ohne jegliche Garantien bezüglich Nutzungsansprüchen zur Verfügung gestellt." );
    }

    private Preferences getPreferences()
    {
        return Preferences.userNodeForPackage( getClass() ).node( "copyright" );
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

    private void acceptButtonActionPerformed()
    {
        getPreferences().putBoolean( PREFS_KEY, true );
        initCopyright();
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
        scientificRadio = new JRadioButton();
        germanRadio = new JRadioButton();
        dendroRadio = new JRadioButton();
        JTextArea textField3 = new JTextArea();
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
                    textField4.setText( "Hier best\u00e4tigen Sie das Copyright und w\u00e4hlen den Themebereich" );
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
                                "3*(default, $lgap), default, $ugap, default, $lgap, fill:default:grow, $lgap, default" ) );

                        //---- textField1 ----
                        textField1.setText( "Sie haben zwei Themenbereiche (Systematik und Dendrologie) zur Auswahl, mit denen Sie die Lernumgebung starten k\u00f6nnen. Bitte w\u00e4hlen Sie den gew\u00fcnschten Bereich aus und pr\u00e4zisieren Sie gegebenenfalls die Details (Wissenschaftlich oder Deutsch)." );
                        textField1.setLineWrap( true );
                        textField1.setWrapStyleWord( true );
                        textField1.setOpaque( false );
                        textField1.setEditable( false );
                        panel2.add( textField1, cc.xywh( 1, 1, 3, 1 ) );

                        //---- systematicRadio ----
                        systematicRadio.setSelected( true );
                        systematicRadio.addActionListener( new ActionListener()
                        {
                            public void actionPerformed( ActionEvent e )
                            {
                                setChoice1();
                            }
                        } );
                        panel2.add( systematicRadio, cc.xy( 1, 3 ) );

                        //---- textField2 ----
                        textField2.setText( "Systematik: W\u00e4hlen Sie diese Option, falls Sie Ihre Systematikkenntnisse von Pflanzen erweitern oder auffrischen wollen. Sie haben zwei M\u00f6glichkeiten, wie die Taxonnamen gehandhabt werden:" );
                        textField2.setLineWrap( true );
                        textField2.setWrapStyleWord( true );
                        textField2.setOpaque( false );
                        textField2.setEditable( false );
                        panel2.add( textField2, cc.xy( 3, 3 ) );

                        //---- scientificRadio ----
                        scientificRadio.setText( "Wissenschaftliche Namen" );
                        scientificRadio.setSelected( true );
                        panel2.add( scientificRadio, cc.xy( 3, 5 ) );

                        //---- germanRadio ----
                        germanRadio.setText( "Deutsche Namen" );
                        panel2.add( germanRadio, cc.xy( 3, 7 ) );

                        //---- dendroRadio ----
                        dendroRadio.addActionListener( new ActionListener()
                        {
                            public void actionPerformed( ActionEvent e )
                            {
                                setChoice1();
                            }
                        } );
                        panel2.add( dendroRadio, cc.xy( 1, 9 ) );

                        //---- textField3 ----
                        textField3.setText( "Dendrologie: Hier erhalten Sie speziellen Einblick in die Dendrologie mit Fokus auf Artkenntnis." );
                        textField3.setLineWrap( true );
                        textField3.setWrapStyleWord( true );
                        textField3.setOpaque( false );
                        textField3.setEditable( false );
                        panel2.add( textField3, cc.xy( 3, 9 ) );

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
                                public void actionPerformed( ActionEvent e )
                                {
                                    okButtonActionPerformed();
                                }
                            } );
                            buttonBar2.add( okButton, cc.xy( 2, 1 ) );
                        }
                        panel2.add( buttonBar2, cc.xywh( 1, 13, 3, 1 ) );
                    }
                    tabbedPane1.addTab( "Auswahl", panel2 );

                }
                contentPanel.add( tabbedPane1, cc.xy( 2, 3 ) );
            }
            dialogPane.add( contentPanel, BorderLayout.CENTER );
        }
        contentPane.add( dialogPane, BorderLayout.CENTER );
        setSize( 565, 615 );
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

    private JTextArea textField4;

    private JTabbedPane tabbedPane1;

    private JPanel panel3;

    private JTextPane textPane;

    private JButton acceptButton;

    private JTextArea textField1;

    public JRadioButton systematicRadio;

    public JRadioButton scientificRadio;

    public JRadioButton germanRadio;

    public JRadioButton dendroRadio;

    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
