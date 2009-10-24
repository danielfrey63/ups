package com.ethz.geobot.herbar.modeapi.wizard;

import ch.jfactory.component.EditItem;
import ch.jfactory.resource.Strings;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;

/**
 * WizardPane to display Order selection
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:11 $
 */
public class WizardOrderPane extends WizardPane {

    public static final String NAME = "lesson.order";

    private static final String ORDERED = Strings.getString("WIZARD.LESSON.ORDER.STATE.ORDERED.TEXT");
    private static final String UNORDERED = Strings.getString("WIZARD.LESSON.ORDER.STATE.UNORDERED.TEXT");

    /**
     * name of the pane
     */
    private String orderedPropertyName;
    private EditItem edit;

    public WizardOrderPane(String orderedPropertyName) {
        super(NAME, new String[]{orderedPropertyName});
        this.orderedPropertyName = orderedPropertyName;
    }

    protected JPanel createDisplayPanel(String prefix) {
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!isVisible()) {
                    return;
                }
                setOrdered(!getOrdered());
            }
        };
        edit = createDefaultEdit(prefix, actionListener);
        JPanel panel = createSimpleDisplayPanel(prefix, edit);
        return panel;
    }

    private String getOrderedText(boolean ordered) {
        if (ordered) {
            return ORDERED;
        }
        else {
            return UNORDERED;
        }
    }

    private boolean getOrdered() {
        return ((Boolean) getProperty(orderedPropertyName)).booleanValue();
    }

    private void setOrdered(boolean ordered) {
        setProperty(orderedPropertyName, Boolean.valueOf(ordered));
    }

    public void registerPropertyChangeListener(WizardModel model) {
        model.addPropertyChangeListener(orderedPropertyName, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                boolean ordered = ((Boolean) event.getNewValue()).booleanValue();
                edit.setUserObject(getOrderedText(ordered));
            }
        });
    }

    /**
     * This method should be overwritten to set the standard values.
     */
    public void initDefaultValues() {
        // try to set actual scope
        boolean ordered = getOrdered();
        edit.setUserObject(getOrderedText(ordered));
    }
}
