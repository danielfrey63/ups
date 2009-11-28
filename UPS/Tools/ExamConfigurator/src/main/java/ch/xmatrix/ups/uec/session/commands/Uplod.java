/*
 * Created by JFormDesigner on Fri May 04 15:20:35 CEST 2007
 */

package ch.xmatrix.ups.uec.session.commands;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * @author Daniel Frey
 */
public class Uplod extends JDialog
{
    public Uplod( final Frame owner )
    {
        super( owner );
        initComponents();
    }

    public Uplod( final Dialog owner )
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
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        final Container contentPane = getContentPane();
        contentPane.setLayout( new BorderLayout() );

        //======== dialogPane ========
        {
            dialogPane.setBorder( new EmptyBorder( 12, 12, 12, 12 ) );
            dialogPane.setLayout( new BorderLayout() );

            //======== contentPanel ========
            {
                contentPanel.setLayout( new BorderLayout() );
            }
            dialogPane.add( contentPanel, BorderLayout.CENTER );

            //======== buttonBar ========
            {
                buttonBar.setBorder( new EmptyBorder( 12, 0, 0, 0 ) );
                buttonBar.setLayout( new GridBagLayout() );
                ( (GridBagLayout) buttonBar.getLayout() ).columnWidths = new int[]{0, 85, 80};
                ( (GridBagLayout) buttonBar.getLayout() ).columnWeights = new double[]{1.0, 0.0, 0.0};

                //---- okButton ----
                okButton.setText( "OK" );
                buttonBar.add( okButton, new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets( 0, 0, 0, 5 ), 0, 0 ) );

                //---- cancelButton ----
                cancelButton.setText( "Cancel" );
                buttonBar.add( cancelButton, new GridBagConstraints( 2, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets( 0, 0, 0, 0 ), 0, 0 ) );
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

    private JPanel buttonBar;

    private JButton okButton;

    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
