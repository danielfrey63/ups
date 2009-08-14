/*
 * Created by JFormDesigner on Fri May 04 00:11:18 CEST 2007
 */

package ch.xmatrix.ups.uec.session.commands;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/** @author Daniel Frey */
public abstract class Upload extends JDialog
{

    protected Upload() throws HeadlessException
    {
        initComponents();
    }

    protected abstract void doOk();

    protected abstract void doSave();

    protected abstract void doCancel();

    private void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        final DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        fieldServer = new JTextField();
        fieldRemoteDir = new JTextField();
        goodiesFormsSeparator1 = compFactory.createSeparator("Keystore");
        goodiesFormsSeparator2 = compFactory.createSeparator("Server");
        fieldKeystorePass = new JPasswordField();
        label5 = new JLabel();
        label1 = new JLabel();
        goodiesFormsSeparator3 = compFactory.createSeparator("Pr\u00fcfungen");
        label8 = new JLabel();
        label2 = new JLabel();
        fieldUser = new JTextField();
        label3 = new JLabel();
        fieldServerPass = new JPasswordField();
        label4 = new JLabel();
        scrollPane1 = new JScrollPane();
        listExaminfos = new JList();
        fieldKeystore = new JTextField();
        fieldAlias = new JTextField();
        label7 = new JLabel();
        label6 = new JLabel();
        buttonBar = new JPanel();
        saveButton = new JButton();
        okButton = new JButton();
        cancelButton = new JButton();
        final CellConstraints cc = new CellConstraints();

        //======== this ========
        setTitle("Vorgaben Upload");
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(Borders.DIALOG_BORDER);
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new FormLayout(
                        new ColumnSpec[]{
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC
                        },
                        new RowSpec[]{
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW),
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC
                        }));
                contentPanel.add(fieldServer, cc.xywh(3, 15, 3, 1));
                contentPanel.add(fieldRemoteDir, cc.xywh(3, 21, 3, 1));
                contentPanel.add(goodiesFormsSeparator1, cc.xywh(1, 5, 5, 1));
                contentPanel.add(goodiesFormsSeparator2, cc.xywh(1, 13, 5, 1));
                contentPanel.add(fieldKeystorePass, cc.xywh(3, 11, 3, 1));

                //---- label5 ----
                label5.setText("Name/IP:");
                contentPanel.add(label5, cc.xy(1, 15));

                //---- label1 ----
                label1.setText("Datei:");
                contentPanel.add(label1, cc.xy(1, 7));
                contentPanel.add(goodiesFormsSeparator3, cc.xywh(1, 1, 5, 1));

                //---- label8 ----
                label8.setText("Auswahl:");
                contentPanel.add(label8, cc.xy(1, 3));

                //---- label2 ----
                label2.setText("Alias:");
                contentPanel.add(label2, cc.xy(1, 9));
                contentPanel.add(fieldUser, cc.xywh(3, 17, 3, 1));

                //---- label3 ----
                label3.setText("Passwort:");
                contentPanel.add(label3, cc.xy(1, 19));
                contentPanel.add(fieldServerPass, cc.xywh(3, 19, 3, 1));

                //---- label4 ----
                label4.setText("User:");
                contentPanel.add(label4, cc.xy(1, 17));

                //======== scrollPane1 ========
                {

                    //---- listExaminfos ----
                    listExaminfos.setToolTipText("Verf\u00fcgbare Pr\u00fcfungssessions-Konfigurationen, wobei hier nur fixierte angezeigt werden.");
                    scrollPane1.setViewportView(listExaminfos);
                }
                contentPanel.add(scrollPane1, cc.xywh(3, 3, 3, 1));
                contentPanel.add(fieldKeystore, cc.xywh(3, 7, 3, 1));
                contentPanel.add(fieldAlias, cc.xywh(3, 9, 3, 1));

                //---- label7 ----
                label7.setText("Passwort:");
                contentPanel.add(label7, cc.xy(1, 11));

                //---- label6 ----
                label6.setText("Verzeichnis:");
                contentPanel.add(label6, cc.xy(1, 21));
            }
            dialogPane.add(contentPanel, BorderLayout.NORTH);

            //======== buttonBar ========
            {
                buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
                buttonBar.setLayout(new FormLayout(
                        new ColumnSpec[]{
                                FormFactory.GLUE_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.RELATED_GAP_COLSPEC,
                                FormFactory.BUTTON_COLSPEC,
                                FormFactory.RELATED_GAP_COLSPEC,
                                FormFactory.BUTTON_COLSPEC
                        },
                        RowSpec.decodeSpecs("pref")));
                ((FormLayout) buttonBar.getLayout()).setColumnGroups(new int[][]{{2, 4, 6}});

                //---- saveButton ----
                saveButton.setText("Speichern");
                saveButton.setToolTipText("Speichert die Eintr\u00e4ge f\u00fcr \"Keystore\" und \"Server\"");
                saveButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(final ActionEvent e)
                    {
                        doSave();
                    }
                });
                buttonBar.add(saveButton, cc.xy(2, 1));

                //---- okButton ----
                okButton.setText("OK");
                okButton.setEnabled(false);
                okButton.setToolTipText("\"Speichert die Eintr\u00e4ge f\u00fcr \"Keystore\" und \"Server\" und l\u00e4dt die Pr\u00fcfungssessionen hoch");
                okButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(final ActionEvent e)
                    {
                        doOk();
                    }
                });
                buttonBar.add(okButton, cc.xy(4, 1));

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                cancelButton.setToolTipText("Beendet den Dialog ohne etwas zu speichern");
                cancelButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(final ActionEvent e)
                    {
                        doCancel();
                    }
                });
                buttonBar.add(cancelButton, cc.xy(6, 1));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;

    private JPanel contentPanel;

    protected JTextField fieldServer;

    protected JTextField fieldRemoteDir;

    private JComponent goodiesFormsSeparator1;

    private JComponent goodiesFormsSeparator2;

    protected JPasswordField fieldKeystorePass;

    private JLabel label5;

    private JLabel label1;

    private JComponent goodiesFormsSeparator3;

    private JLabel label8;

    private JLabel label2;

    protected JTextField fieldUser;

    private JLabel label3;

    protected JPasswordField fieldServerPass;

    private JLabel label4;

    private JScrollPane scrollPane1;

    protected JList listExaminfos;

    protected JTextField fieldKeystore;

    protected JTextField fieldAlias;

    private JLabel label7;

    private JLabel label6;

    private JPanel buttonBar;

    protected JButton saveButton;

    protected JButton okButton;

    protected JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
