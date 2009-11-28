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
public class PropertyInterrogator extends NiceTabbedPane
{
    private final ResultModel resultModel;

    public PropertyInterrogator( final HerbarContext herbarContext, final ResultModel resultModel )
    {
        this.resultModel = resultModel;

        setTabPlacement( JTabbedPane.BOTTOM );
        final Enumeration subModels = resultModel.subStateModels();
        PropertyInterrogatorPanel tab = null;
        while ( subModels.hasMoreElements() )
        {
            final DetailResultModel stateModel = (DetailResultModel) subModels.nextElement();
            tab = new PropertyInterrogatorPanel( stateModel );
            final String base = tab.getBase();
            final String label = Strings.getString( "PROPERTY." + base + ".TEXT" );
            final String icon = Strings.getString( "PROPERTY." + base + ".ICON" );
            addTab( label, ImageLocator.getIcon( icon ), tab );
        }
        setBorder( BorderFactory.createEmptyBorder() );
    }

    public void synchronizeTabs( final JTabbedPane othertab )
    {
        this.setSelectedIndex( othertab.getSelectedIndex() );
    }

    public void setTaxFocus( final Taxon focus )
    {
        // Update model
        resultModel.setTaxFocus( focus );
        // Update sub components
        for ( int i = 0; i < getTabCount(); i++ )
        {
            final PropertyInterrogatorPanel panel = (PropertyInterrogatorPanel) getComponentAt( i );
            panel.setTaxFocus( focus );
        }
    }
}
