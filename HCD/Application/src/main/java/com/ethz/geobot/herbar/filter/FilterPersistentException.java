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
 * FilterPersistentException.java
 *
 * Created on 21. August 2002, 14:59
 */
package com.ethz.geobot.herbar.filter;

/**
 * This exception is thrown on a persistents problem occur on save and load of filters.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class FilterPersistentException extends Exception
{
    /** Creates a new instance of <code>FilterPersistentException</code> without detail message. */
    public FilterPersistentException()
    {
    }

    /**
     * Constructs an instance of <code>FilterPersistentException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public FilterPersistentException( final String msg )
    {
        super( msg );
    }

    /**
     * Constructs an instance of <code>FilterPersistentException</code> with the specified detail message.
     *
     * @param msg   the detail message.
     * @param cause the exception that cause this exception
     */
    public FilterPersistentException( final String msg, final Throwable cause )
    {
        super( msg, cause );
    }

    /**
     * Constructs an instance of <code>FilterPersistentException</code> with the specified detail message.
     *
     * @param cause the exception that cause this exception
     */
    public FilterPersistentException( final Throwable cause )
    {
        super( cause );
    }
}
