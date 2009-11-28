/*
 * Herbar CD-ROM version 2
 *
 * TaxFocusListener.java
 *
 * Created on Feb 12, 2003 6:50:51 PM
 * Created by Daniel
 */
package com.ethz.geobot.herbar.modeapi.state;

import com.ethz.geobot.herbar.model.Taxon;

public interface TaxFocusListener
{
    public void taxFocusChanged( Taxon oldFocus, Taxon newFocus );
}
