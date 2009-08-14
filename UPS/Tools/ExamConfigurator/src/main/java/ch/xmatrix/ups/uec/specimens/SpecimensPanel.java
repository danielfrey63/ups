/*
 * Copyright (c) 2008, Your Corporation. All Rights Reserved.
 */

/*
 * Created by JFormDesigner on Tue Jun 17 14:55:14 CEST 2008
 */

package ch.xmatrix.ups.uec.specimens;

import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JTree;

/** @author Daniel Frey */
public class SpecimensPanel
{

    private void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        ResourceBundle bundle = ResourceBundle.getBundle("ch.xmatrix.ups.uec.specimens.specimenspanel");
        DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
        panel = new JPanel();
        panelToolbar = new JPanel();
        JComponent separator = compFactory.createSeparator(bundle.getString("specimenspanel.separator.text"));
        scrollTree = new JScrollPane();
        tree = new JTree();
        panelEdit = new JPanel();
        JLabel labelCount = new JLabel();
        spinnerCount = new JSpinner();
        JLabel labelBackup = new JLabel();
        checkBackup = new JCheckBox();
        JLabel labelId = new JLabel();
        fieldId = new JTextField();
        JLabel labelKnown = new JLabel();
        JLabel labelUnknown = new JLabel();
        JLabel labelWeight = new JLabel();
        spinnerKnown = new JSpinner();
        spinnerUnknown = new JSpinner();
        JLabel labelDeactivate = new JLabel();
        checkKnown = new JCheckBox();
        checkUnknown = new JCheckBox();
        panel1 = new JPanel();
        JLabel labelDeactivate2 = new JLabel();
        buttonAddAspect = new JButton();
        buttonDeleteAspect = new JButton();
        scrollPaneAspects = new JScrollPane();
        listAspects = new JList();
        CellConstraints cc = new CellConstraints();

        //======== panel ========
        {
            panel.setLayout(new FormLayout(
                    new ColumnSpec[]{
                            FormFactory.UNRELATED_GAP_COLSPEC,
                            new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                            FormFactory.DEFAULT_COLSPEC
                    },
                    new RowSpec[]{
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.RELATED_GAP_ROWSPEC,
                            new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                    }));

            //======== panelToolbar ========
            {
                panelToolbar.setLayout(new FormLayout(
                        new ColumnSpec[]{
                                new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC
                        },
                        RowSpec.decodeSpecs("default")));
                panelToolbar.add(separator, cc.xy(1, 1));
            }
            panel.add(panelToolbar, cc.xywh(1, 1, 3, 1));

            //======== scrollTree ========
            {
                scrollTree.setViewportView(tree);
            }
            panel.add(scrollTree, cc.xy(2, 3));

            //======== panelEdit ========
            {
                panelEdit.setLayout(new FormLayout(
                        new ColumnSpec[]{
                                FormFactory.UNRELATED_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                        },
                        new RowSpec[]{
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.UNRELATED_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.UNRELATED_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                        }));
                ((FormLayout) panelEdit.getLayout()).setColumnGroups(new int[][]{{4, 6}});

                //---- labelCount ----
                labelCount.setText(bundle.getString("specimenspanel.labelCount.text"));
                panelEdit.add(labelCount, cc.xywh(2, 1, 3, 1));

                //---- spinnerCount ----
                spinnerCount.setEnabled(false);
                panelEdit.add(spinnerCount, cc.xy(6, 1));

                //---- labelBackup ----
                labelBackup.setText(bundle.getString("specimenspanel.labelBackup.text"));
                panelEdit.add(labelBackup, cc.xywh(2, 3, 3, 1));
                panelEdit.add(checkBackup, cc.xy(6, 3));

                //---- labelId ----
                labelId.setText(bundle.getString("specimenspanel.labelId.text"));
                panelEdit.add(labelId, cc.xy(2, 5));

                //---- fieldId ----
                fieldId.setEnabled(false);
                panelEdit.add(fieldId, cc.xywh(4, 5, 3, 1));

                //---- labelKnown ----
                labelKnown.setText(bundle.getString("specimenspanel.labelKnown.text"));
                panelEdit.add(labelKnown, cc.xy(4, 7));

                //---- labelUnknown ----
                labelUnknown.setText(bundle.getString("specimenspanel.labelUnknown.text"));
                panelEdit.add(labelUnknown, cc.xy(6, 7));

                //---- labelWeight ----
                labelWeight.setText(bundle.getString("specimenspanel.labelWeight.text"));
                panelEdit.add(labelWeight, cc.xy(2, 9));

                //---- spinnerKnown ----
                spinnerKnown.setEnabled(false);
                panelEdit.add(spinnerKnown, cc.xy(4, 9));

                //---- spinnerUnknown ----
                spinnerUnknown.setEnabled(false);
                panelEdit.add(spinnerUnknown, cc.xy(6, 9));

                //---- labelDeactivate ----
                labelDeactivate.setText(bundle.getString("specimenspanel.labelDeactivate.text"));
                panelEdit.add(labelDeactivate, cc.xy(2, 11));

                //---- checkKnown ----
                checkKnown.setEnabled(false);
                checkKnown.setSelected(true);
                panelEdit.add(checkKnown, cc.xy(4, 11));

                //---- checkUnknown ----
                checkUnknown.setEnabled(false);
                checkUnknown.setSelected(true);
                panelEdit.add(checkUnknown, cc.xy(6, 11));

                //======== panel1 ========
                {
                    panel1.setLayout(new FormLayout(
                            new ColumnSpec[]{
                                    FormFactory.DEFAULT_COLSPEC,
                                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                    FormFactory.DEFAULT_COLSPEC,
                                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                            },
                            RowSpec.decodeSpecs("default")));
                    ((FormLayout) panel1.getLayout()).setColumnGroups(new int[][]{{1, 3}});
                }
                panelEdit.add(panel1, cc.xywh(4, 13, 3, 1));
            }
            panel.add(panelEdit, cc.xy(3, 3));
        }

        //---- labelDeactivate2 ----
        labelDeactivate2.setText(bundle.getString("specimenspanel.labelDeactivate2.text"));

        //---- buttonAddAspect ----
        buttonAddAspect.setText(bundle.getString("specimenspanel.buttonAddAspect.text"));

        //---- buttonDeleteAspect ----
        buttonDeleteAspect.setText(bundle.getString("specimenspanel.buttonDeleteAspect.text"));

        //======== scrollPaneAspects ========
        {
            scrollPaneAspects.setViewportView(listAspects);
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel panel;

    private JPanel panelToolbar;

    private JScrollPane scrollTree;

    private JTree tree;

    private JPanel panelEdit;

    private JSpinner spinnerCount;

    private JCheckBox checkBackup;

    private JTextField fieldId;

    private JSpinner spinnerKnown;

    private JSpinner spinnerUnknown;

    private JCheckBox checkKnown;

    private JCheckBox checkUnknown;

    private JPanel panel1;

    private JButton buttonAddAspect;

    private JButton buttonDeleteAspect;

    private JScrollPane scrollPaneAspects;

    private JList listAspects;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
