/*
 * Created by JFormDesigner on Thu Feb 23 13:58:56 CET 2012
 */

package ch.xmatrix.ups.pmb.exam;

import javax.swing.border.*;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/** @author Daniel Frey */
public class EndDialog extends JDialog
{
    public EndDialog( Frame owner )
    {
        super( owner );
        initComponents();
        initCustomization();
    }

    private void initCustomization()
    {
        setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );
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
                if ( new String( textField2.getPassword() ).equals( "F2013" ) )
                {
                    setVisible( false );
                }
                else
                {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        } );
    }

    public void setText(final String text)
    {
        textField2.setText( text );
    }

    private void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        textField1 = new JTextArea();
        buttonBar = new JPanel();
        textField2 = new JPasswordField();
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
                new LineBorder(new Color(255, 255, 51), 2),
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
                textField1.setText("Vielen Dank, dass Sie an der Pr\u00fcfung teilgenommen haben. Dieser Teil der Pr\u00fcfung ist hiermit beendet.");
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
                    "$lcgap, default:grow, $ugap, $button",
                    "pref"));
                buttonBar.add(textField2, cc.xy(2, 1));

                //---- okButton ----
                okButton.setText("OK");
                okButton.setForeground(Color.orange);
                okButton.setBackground(new Color(51, 51, 51));
                okButton.setEnabled(false);
                buttonBar.add(okButton, cc.xy(4, 1));
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
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
