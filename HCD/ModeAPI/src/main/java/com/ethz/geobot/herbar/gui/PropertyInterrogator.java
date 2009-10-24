/*
 * Herbar CD-ROM version 2
 *
 * PropertyInterrogator.java
 *
 * Created on Jan 23, 2003 2:37:47 PM
 * Created by Daniel
 */
package com.ethz.geobot.herbar.gui;

import ch.jfactory.component.tab.NiceTabbedPane;
import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.modeapi.HerbarContext;
import com.ethz.geobot.herbar.model.Taxon;
import java.util.Enumeration;
import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;

/**
 * Displays three tabs which contain the interrogation panels.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class PropertyInterrogator extends NiceTabbedPane {

    private ResultModel resultModel;

    public PropertyInterrogator(HerbarContext herbarContext, ResultModel resultModel) {

        this.resultModel = resultModel;

        setTabPlacement(JTabbedPane.BOTTOM);
        Enumeration subModels = resultModel.subStateModels();
        PropertyInterrogatorPanel tab = null;
        while (subModels.hasMoreElements()) {
            DetailResultModel stateModel = (DetailResultModel) subModels.nextElement();
            tab = new PropertyInterrogatorPanel(stateModel);
            String base = tab.getBase();
            String label = Strings.getString("PROPERTY." + base + ".TEXT");
            String icon = Strings.getString("PROPERTY." + base + ".ICON");
            addTab(label, ImageLocator.getIcon(icon), tab);
        }
        setBorder(BorderFactory.createEmptyBorder());
    }

    public void synchronizeTabs(JTabbedPane othertab) {
        this.setSelectedIndex(othertab.getSelectedIndex());
    }

    public void setTaxFocus(Taxon focus) {
        // Update model
        resultModel.setTaxFocus(focus);
        // Update sub components
        for (int i = 0; i < getTabCount(); i++) {
            PropertyInterrogatorPanel panel = (PropertyInterrogatorPanel) getComponentAt(i);
            panel.setTaxFocus(focus);
        }
    }
}
