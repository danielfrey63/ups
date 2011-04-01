/*
 * Copyright (c) 2011.
 *
 * Nutzung und Rechte
 *
 * Die Applikation eBot wurde für Studierende der ETH Zürich entwickelt. Sie  steht
 * allen   an   Hochschulen  oder   Fachhochschulen   eingeschriebenen Studierenden
 * (auch  ausserhalb  der  ETH  Zürich)  für  nichtkommerzielle  Zwecke  im Studium
 * kostenlos zur Verfügung. Nichtstudierende Privatpersonen, die die Applikation zu
 * ihrer  persönlichen  Weiterbildung  nutzen  möchten,  werden  gebeten,  für  die
 * nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.– zu bezahlen.
 *
 * Postkonto
 *
 * Unterricht, 85-761469-0, Vermerk "eBot"
 * IBAN 59 0900 0000 8576  1469 0; BIC POFICHBEXXX
 *
 * Jede andere Nutzung der Applikation  ist vorher mit dem Projektleiter  (Matthias
 * Baltisberger, Email:  balti@ethz.ch) abzusprechen  und mit  einer entsprechenden
 * Vereinbarung zu regeln. Die  Applikation wird ohne jegliche  Garantien bezüglich
 * Nutzungsansprüchen zur Verfügung gestellt.
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
