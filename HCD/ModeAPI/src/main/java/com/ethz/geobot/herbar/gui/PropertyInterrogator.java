/*
 * Herbar CD-ROM version 2
 *
 * PropertyInterrogator.java
 *
 * Created on Jan 23, 2003 2:37:47 PM
 * Created by Daniel
 */
package com.ethz.geobot.herbar.gui;

import ch.jfactory.resource.ImageLocator;
import com.ethz.geobot.herbar.model.Taxon;
import java.util.Enumeration;
import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Displays three tabs which contain the interrogation panels.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class PropertyInterrogator extends JTabbedPane
{
    private final ResultModel resultModel;

    public PropertyInterrogator( final ResultModel resultModel )
    {
        this.resultModel = resultModel;
        final String subject = System.getProperty( "xmatrix.subject" );
        final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext( "spring/lesson-" + subject + ".xml" );
        setTabPlacement( JTabbedPane.BOTTOM );
        final Enumeration subModels = resultModel.subStateModels();
        while ( subModels.hasMoreElements() )
        {
            final DetailResultModel stateModel = (DetailResultModel) subModels.nextElement();
            final PropertyInterrogatorPanel tab = new PropertyInterrogatorPanel( stateModel );
            final String base = tab.getBase();
            final String label = (String) context.getBean( "title" + base );
            final String icon = (String) context.getBean( "icon" + base );
            addTab( label, ImageLocator.getIcon( icon ), tab );
        }
        setBorder( BorderFactory.createEmptyBorder() );
    }

    public void synchronizeTabs( final JTabbedPane otherTab )
    {
        this.setSelectedIndex( otherTab.getSelectedIndex() );
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
