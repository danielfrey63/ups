/*
 * Created by JFormDesigner on Tue Jun 17 17:18:19 CEST 2008
 */

package ch.xmatrix.ups.pmb.exam;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

/**
 * @author Daniel Frey
 */
public class StudentsDialog extends JDialog
{
    public StudentsDialog( final Frame owner )
    {
        super( owner );
        initComponents();
    }

    public StudentsDialog( final Dialog owner )
    {
        super( owner );
        initComponents();
    }

    public JButton getCancelButton()
    {
        return cancelButton;
    }

    public JButton getOkButton()
    {
        return okButton;
    }

    public JList getStudentsList()
    {
        return studentsList;
    }

    public JTextField getFieldExamsets()
    {
        return fieldExamsets;
    }

    public JButton getButtonOpenExamsets()
    {
        return buttonOpenExamsets;
    }

    public JTextField getFieldPictureDirectory()
    {
        return fieldPictureDirectory;
    }

    public JButton getButtonOpenPictureDirectory()
    {
        return buttonOpenPictureDirectory;
    }

    public JCheckBox getCheckBoxCurrent()
    {
        return checkBoxCurrent;
    }

    private void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        final JPanel contentPanel = new JPanel();
        label2 = new JLabel();
        fieldPictureDirectory = new JTextField();
        buttonOpenPictureDirectory = new JButton();
        final JLabel label1 = new JLabel();
        fieldExamsets = new JTextField();
        buttonOpenExamsets = new JButton();
        final JScrollPane studentsScroll = new JScrollPane();
        studentsList = new JList();
        final JPanel buttonBar = new JPanel();
        checkBoxCurrent = new JCheckBox();
        okButton = new JButton();
        cancelButton = new JButton();
        final CellConstraints cc = new CellConstraints();

        //======== this ========
        setModal( true );
        setTitle( "Liste der Pr\u00fcfungskandidaten" );
        setBackground( Color.gray );
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
                        "default, $lcgap, default:grow, $rgap, default",
                        "default, $lgap, default, $ugap, fill:default:grow" ) );

                //---- label2 ----
                label2.setText( "Bilderverzeichnis:" );
                contentPanel.add( label2, cc.xy( 1, 1 ) );
                contentPanel.add( fieldPictureDirectory, cc.xy( 3, 1 ) );

                //---- buttonOpenPictureDirectory ----
                buttonOpenPictureDirectory.setText( "..." );
                contentPanel.add( buttonOpenPictureDirectory, cc.xy( 5, 1 ) );

                //---- label1 ----
                label1.setText( "Pr\u00fcfungssets:" );
                contentPanel.add( label1, cc.xy( 1, 3 ) );
                contentPanel.add( fieldExamsets, cc.xy( 3, 3 ) );

                //---- buttonOpenExamsets ----
                buttonOpenExamsets.setText( "..." );
                contentPanel.add( buttonOpenExamsets, cc.xy( 5, 3 ) );

                //======== studentsScroll ========
                {
                    studentsScroll.setBorder( null );

                    //---- studentsList ----
                    studentsList.setBackground( new Color( 51, 51, 51 ) );
                    studentsList.setForeground( new Color( 204, 204, 204 ) );
                    studentsList.setBorder( null );
                    studentsList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
                    studentsScroll.setViewportView( studentsList );
                }
                contentPanel.add( studentsScroll, cc.xywh( 1, 5, 5, 1 ) );
            }
            dialogPane.add( contentPanel, BorderLayout.CENTER );

            //======== buttonBar ========
            {
                buttonBar.setBorder( Borders.BUTTON_BAR_GAP_BORDER );
                buttonBar.setLayout( new FormLayout(
                        "$lcgap, default, $glue, $button, $rgap, $button",
                        "pref" ) );

                //---- checkBoxCurrent ----
                checkBoxCurrent.setText( "Nur aktuelle anzeigen" );
                buttonBar.add( checkBoxCurrent, cc.xy( 2, 1 ) );

                //---- okButton ----
                okButton.setText( "OK" );
                okButton.setEnabled( false );
                buttonBar.add( okButton, cc.xy( 4, 1 ) );

                //---- cancelButton ----
                cancelButton.setText( "Cancel" );
                buttonBar.add( cancelButton, cc.xy( 6, 1 ) );
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

    private JLabel label2;

    private JTextField fieldPictureDirectory;

    private JButton buttonOpenPictureDirectory;

    private JTextField fieldExamsets;

    private JButton buttonOpenExamsets;

    private JList studentsList;

    private JCheckBox checkBoxCurrent;

    private JButton okButton;

    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
