/*
 * Created by JFormDesigner on Tue May 15 19:02:52 CEST 2007
 */

package ch.xmatrix.ups.uec.sets;

import ch.jfactory.component.table.SortedTable;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/** @author Daniel Frey */
public class SetPanel
{

    private void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        final DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
        panel = new JPanel();
        goodiesFormsSeparator2 = compFactory.createSeparator("Konfiguration");
        label1 = new JLabel();
        combo = new JComboBox();
        label2 = new JLabel();
        field = new JTextField();
        panelToolbar = new JPanel();
        goodiesFormsSeparator1 = compFactory.createSeparator("Pr\u00fcfungslisten");
        scroll = new JScrollPane();
        table = new SortedTable();
        final CellConstraints cc = new CellConstraints();

        //======== panel ========
        {
            panel.setLayout(new FormLayout(
                    new ColumnSpec[]{
                            FormFactory.UNRELATED_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                    },
                    new RowSpec[]{
                            new RowSpec("max(default;16px)"),
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.UNRELATED_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                    }));
            panel.add(goodiesFormsSeparator2, cc.xywh(1, 1, 4, 1));

            //---- label1 ----
            label1.setText("Auswahl:");
            panel.add(label1, cc.xy(2, 3));
            panel.add(combo, cc.xy(4, 3));

            //---- label2 ----
            label2.setText("Random Seed:");
            panel.add(label2, cc.xy(2, 5));
            panel.add(field, cc.xy(4, 5));

            //======== panelToolbar ========
            {
                panelToolbar.setLayout(new FormLayout(
                        new ColumnSpec[]{
                                new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC
                        },
                        RowSpec.decodeSpecs("default")));
                panelToolbar.add(goodiesFormsSeparator1, cc.xy(1, 1));
            }
            panel.add(panelToolbar, cc.xywh(1, 7, 4, 1));

            //======== scroll ========
            {
                scroll.setEnabled(false);

                //---- table ----
                table.setEnabled(false);
                scroll.setViewportView(table);
            }
            panel.add(scroll, cc.xywh(2, 9, 3, 1));
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel panel;

    private JComponent goodiesFormsSeparator2;

    private JLabel label1;

    private JComboBox combo;

    private JLabel label2;

    private JTextField field;

    private JPanel panelToolbar;

    private JComponent goodiesFormsSeparator1;

    private JScrollPane scroll;

    private SortedTable table;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
