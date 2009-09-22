/*
 * Copyright (c) 2008, Your Corporation. All Rights Reserved.
 */

/*
 * Created by JFormDesigner on Fri Aug 08 16:16:53 CEST 2008
 */

package ch.xmatrix.ups.pmb.exam;

import java.awt.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author Daniel Frey
 */
public class MessageDialog extends JDialog {
    public MessageDialog(Frame owner) {
        super(owner);
        initComponents();
    }

    public MessageDialog(Dialog owner) {
        super(owner);
        initComponents();
    }

    public JTextField getText() {
        return text;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        text = new JTextField();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setUndecorated(true);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(Borders.DIALOG_BORDER);
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new FormLayout(
                    "default:grow",
                    "fill:default:grow"));

                //---- text ----
                text.setOpaque(false);
                text.setBorder(null);
                contentPanel.add(text, cc.xy(1, 1));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);
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
    private JTextField text;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
