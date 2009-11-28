/*
 * Copyright (c) 2008, Your Corporation. All Rights Reserved.
 */

/*
 * Created by JFormDesigner on Mon Aug 04 10:43:51 CEST 2008
 */

package ch.xmatrix.ups.pmb.exam;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * @author Daniel Frey
 */
public class PasswordDialog extends JDialog
{
    public PasswordDialog( final Frame owner )
    {
        super( owner );
        initComponents();
    }

    public PasswordDialog( final Dialog owner )
    {
        super( owner );
        initComponents();
    }

    public JButton getOkButton()
    {
        return okButton;
    }

    public JButton getCancelButton()
    {
        return cancelButton;
    }

    public JTextField getFieldName()
    {
        return fieldName;
    }

    public JPasswordField getFieldPassword()
    {
        return fieldPassword;
    }

    private void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        fieldName = new JTextField();
        final JLabel label2 = new JLabel();
        fieldPassword = new JPasswordField();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();
        final CellConstraints cc = new CellConstraints();

        //======== this ========
        setModal( true );
        setTitle( "Authentisierung" );
        setUndecorated( true );
        setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );
        final Container contentPane = getContentPane();
        contentPane.setLayout( new BorderLayout() );

        //======== dialogPane ========
        {
            dialogPane.setBorder( Borders.DIALOG_BORDER );
            dialogPane.setLayout( new BorderLayout() );

            //======== contentPanel ========
            {
                contentPanel.setLayout( new FormLayout(
                        "default, $lcgap, default:grow",
                        "2*(default, $lgap), default" ) );

                //---- label1 ----
                label1.setText( "Name:" );
                label1.setLabelFor( fieldName );
                contentPanel.add( label1, cc.xy( 1, 1 ) );
                contentPanel.add( fieldName, cc.xy( 3, 1 ) );

                //---- label2 ----
                label2.setText( "Passwort:" );
                label2.setLabelFor( fieldPassword );
                contentPanel.add( label2, cc.xy( 1, 3 ) );
                contentPanel.add( fieldPassword, cc.xy( 3, 3 ) );
            }
            dialogPane.add( contentPanel, BorderLayout.CENTER );

            //======== buttonBar ========
            {
                buttonBar.setBorder( Borders.BUTTON_BAR_GAP_BORDER );
                buttonBar.setLayout( new FormLayout(
                        "$glue, $button, $rgap, $button",
                        "pref" ) );

                //---- okButton ----
                okButton.setText( "OK" );
                okButton.setEnabled( false );
                buttonBar.add( okButton, cc.xy( 2, 1 ) );

                //---- cancelButton ----
                cancelButton.setText( "Cancel" );
                buttonBar.add( cancelButton, cc.xy( 4, 1 ) );
            }
            dialogPane.add( buttonBar, BorderLayout.SOUTH );
        }
        contentPane.add( dialogPane, BorderLayout.CENTER );
        pack();
        setLocationRelativeTo( getOwner() );
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;

    private JPanel contentPanel;

    protected JLabel label1;

    private JTextField fieldName;

    private JPasswordField fieldPassword;

    private JPanel buttonBar;

    private JButton okButton;

    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
