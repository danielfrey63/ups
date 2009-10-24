package com.ethz.geobot.herbar.modeapi.wizard.filter;

import com.ethz.geobot.herbar.gui.tax.TaxTree;
import com.ethz.geobot.herbar.modeapi.wizard.WizardModel;
import com.ethz.geobot.herbar.modeapi.wizard.WizardPane;
import com.ethz.geobot.herbar.model.filter.FilterModel;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.apache.log4j.Category;

/**
 * WizardPane to display Order selection
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:13 $
 */
public class WizardFilterPreviewPane extends WizardPane {

    private static final Category cat = Category.getInstance(WizardFilterPreviewPane.class);

    /**
     * name of the pane
     */
    public static final String NAME = "filter.preview";

    private String filterPropertyName;
    private TaxTree preview;

    public WizardFilterPreviewPane(String filterPropertyName) {
        super(NAME, new String[]{filterPropertyName});
        this.filterPropertyName = filterPropertyName;
    }

    public void activate() {
        preview.setHerbarModel(getFilter());
    }

    protected JPanel createDisplayPanel(String prefix) {
        JPanel text = createTextPanel(prefix);
        JPanel title = createDefaultTitlePanel(prefix);
        preview = new TaxTree();
        JScrollPane scrollPane = new JScrollPane(preview);
        scrollPane.setPreferredSize(new Dimension(10, 10));

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        panel.add(text, gbc);
        gbc.gridy += 1;
        panel.add(title, gbc);
        gbc.weighty = 1.0;
        gbc.gridy += 1;
        panel.add(scrollPane, gbc);

        return panel;
    }

    private FilterModel getFilter() {
        return (FilterModel) getProperty(filterPropertyName);
    }

    public void registerPropertyChangeListener(WizardModel model) {
        model.addPropertyChangeListener(filterPropertyName, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                cat.info("change preview model");
                FilterModel fmodel = (FilterModel) event.getNewValue();
                preview.setHerbarModel(fmodel);
            }
        });
    }
}
