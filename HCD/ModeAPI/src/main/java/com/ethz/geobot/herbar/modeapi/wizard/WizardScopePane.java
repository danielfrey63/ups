package com.ethz.geobot.herbar.modeapi.wizard;

import ch.jfactory.component.EditItem;
import com.ethz.geobot.herbar.gui.tax.TaxTreeDialog;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Taxon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * WizardPane to display Scope selection
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:11 $
 */
public class WizardScopePane extends WizardPane {

    /**
     * name of the pane
     */
    public final static String NAME = "lesson.scope";

    /**
     * display field for the selected scope
     */
    private String modelPropertyName;
    private String scopePropertyName;
    private EditItem edit;

    public WizardScopePane(String modelPropertyName, String scopePropertyName) {
        super(NAME, new String[]{modelPropertyName, scopePropertyName});
        this.modelPropertyName = modelPropertyName;
        this.scopePropertyName = scopePropertyName;
    }

    protected JPanel createDisplayPanel(String prefix) {
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                if (!isVisible()) {
                    return;
                }
                HerbarModel herbarModel = (HerbarModel) getProperty(modelPropertyName);
                Taxon rootTaxon = herbarModel.getRootTaxon();
                TaxTreeDialog dlg = new TaxTreeDialog((JDialog) getTopLevelAncestor(), rootTaxon);
                dlg.setSelectedTaxon((Taxon) getProperty(scopePropertyName));
                dlg.setSize(400, 500);
                dlg.setLocationRelativeTo(WizardScopePane.this);
                dlg.setVisible(true);
                if (dlg.isAccepted()) {
                    Taxon selectedTaxon = dlg.getSelectedTaxon();
                    if (selectedTaxon != null) {
                        setProperty(scopePropertyName, selectedTaxon);
                    }
                }
            }
        };
        edit = createDefaultEdit(prefix, actionListener);
        JPanel panel = createSimpleDisplayPanel(prefix, edit);
        return panel;
    }

    public void registerPropertyChangeListener(WizardModel model) {
        model.addPropertyChangeListener(scopePropertyName, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                edit.setUserObject(event.getNewValue());
            }
        });
    }

    /**
     * This method should be overwritten to set the standard values.
     */
    public void initDefaultValues() {
        // try to set actual scope
        Object scope = getProperty(scopePropertyName);
        if (scope != null) {
            edit.setUserObject(scope);
        }
    }
}
