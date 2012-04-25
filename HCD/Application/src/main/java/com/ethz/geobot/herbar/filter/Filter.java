/*
 * Copyright (c) 2011.
 *
 * Nutzung und Rechte
 *
 * Die Applikation eBot wurde f�r Studierende der ETH Z�rich entwickelt. Sie  steht
 * allen   an   Hochschulen  oder   Fachhochschulen   eingeschriebenen Studierenden
 * (auch  ausserhalb  der  ETH  Z�rich)  f�r  nichtkommerzielle  Zwecke  im Studium
 * kostenlos zur Verf�gung. Nichtstudierende Privatpersonen, die die Applikation zu
 * ihrer  pers�nlichen  Weiterbildung  nutzen  m�chten,  werden  gebeten,  f�r  die
 * nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.� zu bezahlen.
 *
 * Postkonto
 *
 * Unterricht, 85-761469-0, Vermerk "eBot"
 * IBAN 59 0900 0000 8576  1469 0; BIC POFICHBEXXX
 *
 * Jede andere Nutzung der Applikation  ist vorher mit dem Projektleiter  (Matthias
 * Baltisberger, Email:  balti@ethz.ch) abzusprechen  und mit  einer entsprechenden
 * Vereinbarung zu regeln. Die  Applikation wird ohne jegliche  Garantien bez�glich
 * Nutzungsanspr�chen zur Verf�gung gestellt.
 */

/*
 * Filter.java
 *
 * Created on 26. Juli 2002, 15:14
 */
package com.ethz.geobot.herbar.filter;

import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.filter.FilterDefinitionDetail;
import com.ethz.geobot.herbar.model.filter.FilterModel;

/**
 * Bean class to load filter definition.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class Filter
{
    /** Holds value of property name. */
    private String name;

    /** Holds value of property baseFilterName. */
    private String baseFilterName;

    /** Holds value of property details. */
    private Detail[] details;

    /** Sets whether the filter is modifiable or not. */
    private boolean fixed;

    /** Creates a new instance of Filter */
    public Filter()
    {
    }

    public Filter( final FilterModel model )
    {
        name = model.getName();
        final HerbarModel baseModel = model.getDependantModel();

        if ( baseModel instanceof FilterModel )
        {
            final FilterModel bfm = (FilterModel) baseModel;
            baseFilterName = bfm.getName();
        }
        else
        {
            baseFilterName = "";
        }
        fixed = model.isFixed();

        // copy filter details
        final FilterDefinitionDetail[] defDetails = model.getFilterDetails();
        details = new Detail[defDetails.length];
        for ( int i = 0; i < defDetails.length; i++ )
        {
            details[i] = new Detail( defDetails[i] );
        }
    }

    /**
     * Getter for property name.
     *
     * @return Value of property name.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Setter for property name.
     *
     * @param name New value of property name.
     */
    public void setName( final String name )
    {
        this.name = name;
    }

    /**
     * Getter for property baseFilterName.
     *
     * @return Value of property baseFilterName.
     */
    public String getBaseFilterName()
    {
        return this.baseFilterName;
    }

    /**
     * Setter for property baseFilterName.
     *
     * @param baseFilterName New value of property baseFilterName.
     */
    public void setBaseFilterName( final String baseFilterName )
    {
        this.baseFilterName = baseFilterName;
    }

    /**
     * Getter for property details.
     *
     * @return Value of property details.
     */
    public Detail[] getDetails()
    {
        return this.details;
    }

    /**
     * Setter for property details.
     *
     * @param details New value of property details.
     */
    public void setDetails( final Detail[] details )
    {
        this.details = details;
    }

    public boolean getFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }
}
