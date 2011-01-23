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
 * TaxonPopup control, used to select the taxon of a list of them. You have to override the itemSelected method to get
 * notified of the level change.
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
