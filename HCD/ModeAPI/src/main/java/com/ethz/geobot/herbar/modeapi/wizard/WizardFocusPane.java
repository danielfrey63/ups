package com.ethz.geobot.herbar.modeapi.wizard;

import ch.jfactory.application.view.dialog.ListDialog;
import ch.jfactory.component.EditItem;
import ch.jfactory.lang.ToStringComparator;
import com.ethz.geobot.herbar.model.Taxon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

/**
 * WizardPane to display Focus selection
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:11 $
 */
public class WizardFocusPane extends WizardPane {

    public static final String NAME = "lesson.focus";

    /**
     * name of the pane
     */
    EditItem edit;
    private String taxListPropertyName;
    private String focusPropertyName;

    public WizardFocusPane(String taxListPropertyName, String focusPropertyName) {
        super(NAME, new String[]{taxListPropertyName, focusPropertyName});
        this.taxListPropertyName = taxListPropertyName;
        this.focusPropertyName = focusPropertyName;
    }

    protected JPanel createDisplayPanel(String prefix) {
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!isVisible()) {
                    return;
                }
                final Taxon[] list = getTaxList();
                Arrays.sort(list, new ToStringComparator());
                ListDialog dialog = new ListDialog((JDialog) getTopLevelAncestor(), "DIALOG.FOCUS", list);
                dialog.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                dialog.setSize(300, 300);
                dialog.setLocationRelativeTo(WizardFocusPane.this);
                dialog.setVisible(true);
                if (dialog.isAccepted()) {
                    setFocus((Taxon) dialog.getSelectedData()[ 0 ]);
                }
            }
        };
        edit = createDefaultEdit(prefix, actionListener);
        return createSimpleDisplayPanel(prefix, edit);
    }

    private Taxon getFocus() {
        return (Taxon) getProperty(focusPropertyName);
    }

    private void setFocus(Taxon level) {
        setProperty(focusPropertyName, level);
    }

    private Taxon[] getTaxList() {
        return (Taxon[]) getProperty(taxListPropertyName);
    }

    public void registerPropertyChangeListener(WizardModel model) {
        model.addPropertyChangeListener(focusPropertyName,
                new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent event) {
                        Taxon taxon = (Taxon) event.getNewValue();
                        edit.setUserObject(taxon.getName());
                    }
                });
    }

    /**
     * This method should be overwritten to set the standard values.
     */
    public void initDefaultValues() {
        // try to set actual scope
        Taxon focus = getFocus();
        if (focus != null) {
            edit.setUserObject(focus.getName());
        }
    }
}
