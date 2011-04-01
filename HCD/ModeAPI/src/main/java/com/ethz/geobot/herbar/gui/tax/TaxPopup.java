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

/*
 * TaxPopup.java
 *
 * Created on 22. Juli 2002, 15:56
 */
package com.ethz.geobot.herbar.gui.tax;

import ch.jfactory.component.ObjectPopup;
import com.ethz.geobot.herbar.model.Taxon;
import java.awt.Component;

/**
 * TaxonPopup control, used to select the taxon of a list of them. You have to override the itemSelected method to get notified of the level change.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
abstract public class TaxPopup extends ObjectPopup
{
    /**
     * Construct a TaxPopup with all given taxa.
     *
     * @param taxa all taxa which should be displayed
     */
    public TaxPopup( final Taxon[] taxa )
    {
        super( taxa );
    }

    /**
     * change taxa selection list.
     *
     * @param taxa array of displayed taxa
     */
    public void setTaxa( final Taxon[] taxa )
    {
        super.setObjects( taxa );
    }

    /**
     * displays the taxa popup and set the current taxa.
     *
     * @param jb           parent component
     * @param currentTaxon current selected taxon
     */
    public void showPopup( final Component jb, final Taxon currentTaxon )
    {
        super.showPopUp( jb, currentTaxon );
    }
}
