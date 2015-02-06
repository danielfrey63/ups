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
 * Filter.java
 *
 * Created on 26. Juli 2002, 15:14
 */
package com.ethz.geobot.herbar.filter;

/**
 * Bean class to load filter definition.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class Filter
{
    /**
     * Holds value of property name.
     */
    private String name;

    /**
     * Holds value of property details.
     */
    private Detail[] details;

    /**
     * Sets whether the filter is modifiable or not.
     */
    private boolean fixed;

    /**
     * Indicates the sort order of this filter.
     */
    private int rank;

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

    public boolean getFixed()
    {
        return fixed;
    }

    public void setFixed( final boolean fixed )
    {
        this.fixed = fixed;
    }

    public int getRank()
    {
        return rank;
    }

    public void setRank( int rank )
    {
        this.rank = rank;
    }
}
