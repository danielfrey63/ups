/*
 * Created by JFormDesigner on Sat Jun 27 07:45:18 CEST 2009
 */

package ch.xmatrix.ups.uec.constraints;

import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.uif.component.UIFSplitPane;
import java.util.ResourceBundle;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;

/**
 * @author Daniel Frey
 */
public class ConstraintsPanel
{
    private void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        final ResourceBundle bundle = ResourceBundle.getBundle( "ch.xmatrix.ups.uec.constraints.constraintspanel" );
        final DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
        panelConstraints = new JPanel();
        panelSeparator = new JPanel();
        separator = compFactory.createSeparator( bundle.getString( "constraintspanel.separator.text" ) );
        scrollDescription = new JScrollPane();
        fieldDescription = new JTextArea();
        uIFSplitPane1 = new UIFSplitPane();
        uIFSplitPane2 = new UIFSplitPane();
        scrollConstraints = new JScrollPane();
        listConstraints = new JList();
        panelEditor = new JPanel();
        scrollTaxa = new JScrollPane();
        listTaxa = new JList();
        labelName = new JLabel();
        fieldConstraintName = new JTextField();
        labelCount = new JLabel();
        spinnerCount = new JSpinner();
        scrollTree = new JScrollPane();
        treeTaxa = new JTree();
        final CellConstraints cc = new CellConstraints();

        //======== panelConstraints ========
        {
            panelConstraints.setLayout( new FormLayout(
                    "$ugap, 100dlu:grow",
                    "default, $lgap, fill:24dlu, $rgap, fill:default:grow" ) );

            //======== panelSeparator ========
            {
                panelSeparator.setLayout( new FormLayout(
                        "default:grow, $lcgap, default",
                        "default" ) );
                panelSeparator.add( separator, cc.xy( 1, 1 ) );
            }
            panelConstraints.add( panelSeparator, cc.xywh( 1, 1, 2, 1 ) );

            //======== scrollDescription ========
            {
                //---- fieldDescription ----
                fieldDescription.setLineWrap( true );
                fieldDescription.setWrapStyleWord( true );
                fieldDescription.setEnabled( false );
                fieldDescription.setName( bundle.getString( "constraintspanel.fieldDescription.name" ) );
                scrollDescription.setViewportView( fieldDescription );
            }
            panelConstraints.add( scrollDescription, cc.xy( 2, 3 ) );

            //======== uIFSplitPane1 ========
            {
                uIFSplitPane1.setContinuousLayout( true );
                uIFSplitPane1.setBorder( null );

                //======== uIFSplitPane2 ========
                {
                    uIFSplitPane2.setOrientation( JSplitPane.VERTICAL_SPLIT );
                    uIFSplitPane2.setBorder( null );
                    uIFSplitPane2.setResizeWeight( 0.8 );

                    //======== scrollConstraints ========
                    {
                        //---- listConstraints ----
                        listConstraints.setName( bundle.getString( "constraintspanel.listConstraints.name" ) );
                        scrollConstraints.setViewportView( listConstraints );
                    }
                    uIFSplitPane2.setTopComponent( scrollConstraints );

                    //======== panelEditor ========
                    {
                        panelEditor.setLayout( new FormLayout(
                                "default, $lcgap, default:grow",
                                "fill:default:grow, 2*($lgap, default)" ) );

                        //======== scrollTaxa ========
                        {
                            //---- listTaxa ----
                            listTaxa.setName( bundle.getString( "constraintspanel.listTaxa.name" ) );
                            scrollTaxa.setViewportView( listTaxa );
                        }
                        panelEditor.add( scrollTaxa, cc.xywh( 1, 1, 3, 1 ) );

                        //---- labelName ----
                        labelName.setText( bundle.getString( "constraintspanel.labelName.text" ) );
                        panelEditor.add( labelName, cc.xy( 1, 3 ) );

                        //---- fieldConstraintName ----
                        fieldConstraintName.setEnabled( false );
                        fieldConstraintName.setName( bundle.getString( "constraintspanel.fieldConstraintName.name" ) );
                        panelEditor.add( fieldConstraintName, cc.xy( 3, 3 ) );

                        //---- labelCount ----
                        labelCount.setText( bundle.getString( "constraintspanel.labelCount.text" ) );
                        panelEditor.add( labelCount, cc.xy( 1, 5 ) );

                        //---- spinnerCount ----
                        spinnerCount.setEnabled( false );
                        spinnerCount.setToolTipText( bundle.getString( "constraintspanel.spinnerCount.toolTipText" ) );
                        spinnerCount.setName( bundle.getString( "constraintspanel.spinnerCount.name" ) );
                        panelEditor.add( spinnerCount, cc.xy( 3, 5 ) );
                    }
                    uIFSplitPane2.setBottomComponent( panelEditor );
                }
                uIFSplitPane1.setLeftComponent( uIFSplitPane2 );

                //======== scrollTree ========
                {
                    //---- treeTaxa ----
                    treeTaxa.setName( bundle.getString( "constraintspanel.treeTaxa.name" ) );
                    scrollTree.setViewportView( treeTaxa );
                }
                uIFSplitPane1.setRightComponent( scrollTree );
            }
            panelConstraints.add( uIFSplitPane1, cc.xy( 2, 5 ) );
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel panelConstraints;

    private JPanel panelSeparator;

    private JComponent separator;

    private JScrollPane scrollDescription;

    private JTextArea fieldDescription;

    private UIFSplitPane uIFSplitPane1;

    private UIFSplitPane uIFSplitPane2;

    private JScrollPane scrollConstraints;

    private JList listConstraints;

    private JPanel panelEditor;

    private JScrollPane scrollTaxa;

    private JList listTaxa;

    private JLabel labelName;

    private JTextField fieldConstraintName;

    private JLabel labelCount;

    private JSpinner spinnerCount;

    private JScrollPane scrollTree;

    private JTree treeTaxa;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
