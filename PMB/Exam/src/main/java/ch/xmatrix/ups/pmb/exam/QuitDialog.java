/*
 * Created by JFormDesigner on Fri Feb 24 16:24:02 CET 2012
 */

package ch.xmatrix.ups.pmb.exam;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 * @author Daniel Frey
 */
public class QuitDialog extends JDialog {

    public boolean hit = false;

    public QuitDialog(Frame owner) {
        super(owner);
        initComponents();
        initCustomizedComponents();
    }

    private void initCustomizedComponents()
    {
        getRootPane().setDefaultButton( okButton );
        textField2.requestFocus();
        textField2.addCaretListener( new CaretListener()
        {
            public void caretUpdate( final CaretEvent e )
            {
                okButton.setEnabled( textField2.getPassword().length > 0 );
            }
        } );
        okButton.addActionListener( new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                if ( new String( textField2.getPassword() ).equals( "Alles wird gut" ) )
                {
                    hit = true;
                    setVisible( false );
                }
                else
                {
                    hit = false;
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        } );
        cancelButton.addActionListener( new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                hit = false;
                setVisible( false );
            }
        } );
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        textField1 = new JTextArea();
        buttonBar = new JPanel();
        textField2 = new JPasswordField();
        cancelButton = new JButton();
        okButton = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setModal(true);
        setResizable(false);
        setBackground(Color.black);
        setTitle("Ende dieses Pr\u00fcfungsteils");
        setUndecorated(true);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new CompoundBorder(
                new LineBorder(new Color(204, 0, 0), 2),
                Borders.DIALOG_BORDER));
            dialogPane.setBackground(new Color(51, 51, 51));
            dialogPane.setPreferredSize(new Dimension(300, 200));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setOpaque(false);
                contentPanel.setLayout(new FormLayout(
                    "default:grow",
                    "fill:default:grow"));

                //---- textField1 ----
                textField1.setEditable(false);
                textField1.setText("Bitte geben Sie das Passwort zum Beenden des Programms ein:");
                textField1.setWrapStyleWord(true);
                textField1.setFont(new Font("SansSerif", Font.BOLD, 12));
                textField1.setForeground(Color.orange);
                textField1.setOpaque(false);
                textField1.setBackground(Color.black);
                textField1.setLineWrap(true);
                contentPanel.add(textField1, cc.xy(1, 1));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
                buttonBar.setOpaque(false);
                buttonBar.setLayout(new FormLayout(
                    "default:grow, $ugap, $lcgap, default, $lcgap, $button",
                    "default, $ugap, pref"));
                ((FormLayout)buttonBar.getLayout()).setColumnGroups(new int[][] {{4, 6}});
                buttonBar.add(textField2, cc.xywh(1, 1, 6, 1));

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                cancelButton.setForeground(Color.orange);
                cancelButton.setBackground(new Color(51, 51, 51));
                buttonBar.add(cancelButton, cc.xy(4, 3));

                //---- okButton ----
                okButton.setText("OK");
                okButton.setForeground(Color.orange);
                okButton.setBackground(new Color(51, 51, 51));
                okButton.setEnabled(false);
                buttonBar.add(okButton, cc.xy(6, 3));
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
    private JTextArea textField1;
    private JPanel buttonBar;
    private JPasswordField textField2;
    private JButton cancelButton;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
