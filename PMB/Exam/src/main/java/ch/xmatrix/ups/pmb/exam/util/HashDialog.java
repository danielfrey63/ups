/*
 * Copyright (c) 2008, Your Corporation. All Rights Reserved.
 */

/*
 * Created by JFormDesigner on Mon Aug 04 11:02:23 CEST 2008
 */
package ch.xmatrix.ups.pmb.exam.util;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

/** @author Daniel Frey */
public class HashDialog extends JDialog
{
    /** This class logger. */
    private static final Logger LOG = LoggerFactory.getLogger( HashDialog.class );

    public HashDialog( final Frame owner )
    {
        super( owner );
        initComponents();
    }

    public HashDialog( final Dialog owner )
    {
        super( owner );
        initComponents();
    }

    private void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        fieldPassword = new JPasswordField();
        fieldHash = new JTextField();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();
        final CellConstraints cc = new CellConstraints();

        //======== this ========
        setTitle( "Compute SHA Hash" );
        setUndecorated( true );
        final Container contentPane = getContentPane();
        contentPane.setLayout( new BorderLayout() );

        //======== dialogPane ========
        {
            dialogPane.setBorder( Borders.DIALOG_BORDER );
            dialogPane.setLayout( new BorderLayout() );

            //======== contentPanel ========
            {
                contentPanel.setLayout( new FormLayout(
                        "default:grow",
                        "default, $lgap, default" ) );
                contentPanel.add( fieldPassword, cc.xy( 1, 1 ) );

                //---- fieldHash ----
                fieldHash.setEditable( false );
                contentPanel.add( fieldHash, cc.xy( 1, 3 ) );
            }
            dialogPane.add( contentPanel, BorderLayout.CENTER );

            //======== buttonBar ========
            {
                buttonBar.setBorder( Borders.BUTTON_BAR_GAP_BORDER );
                buttonBar.setLayout( new FormLayout(
                        "$glue, $button, $rgap, $button",
                        "pref" ) );

                //---- okButton ----
                okButton.setText( "Compute" );
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
        fieldPassword.addCaretListener( new CaretListener()
        {
            public void caretUpdate( final CaretEvent e )
            {
                final boolean hasPassword = fieldPassword.getPassword().length > 0;
                okButton.setEnabled( hasPassword );
                getRootPane().setDefaultButton( hasPassword ? okButton : cancelButton );
            }
        } );
        okButton.addActionListener( new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                try
                {
                    final byte[] selection = new String( fieldPassword.getPassword() ).getBytes();
                    final MessageDigest md = MessageDigest.getInstance( "SHA" );
                    fieldHash.setText( new BASE64Encoder().encode( md.digest( selection ) ) );
                }
                catch ( NoSuchAlgorithmException x )
                {
                    LOG.error( "cannot init digest", x );
                }

            }
        } );
        cancelButton.addActionListener( new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                System.exit( 0 );
            }
        } );
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;

    private JPanel contentPanel;

    private JPasswordField fieldPassword;

    private JTextField fieldHash;

    private JPanel buttonBar;

    private JButton okButton;

    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
