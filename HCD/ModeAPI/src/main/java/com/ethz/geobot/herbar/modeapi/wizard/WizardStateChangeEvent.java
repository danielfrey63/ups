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
package com.ethz.geobot.herbar.modeapi.wizard;

import java.util.EventObject;

/**
 * This is the event object which is fired on WizardState changes.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:11 $
 */
public class WizardStateChangeEvent extends EventObject
{
    private final boolean hasNext;

    private final boolean hasPrevious;

    private final boolean isNextEnabled;

    private final boolean isPreviousEnabled;

    private final boolean isFinishEnabled;

    private final boolean isCancelEnabled;

    public WizardStateChangeEvent( final Object source, final boolean hasNext, final boolean hasPrevious, final boolean
            isNextEnabled, final boolean isPreviousEnabled, final boolean isFinishEnabled, final boolean isCancelEnabled )
    {
        super( source );
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
        this.isNextEnabled = isNextEnabled;
        this.isPreviousEnabled = isPreviousEnabled;
        this.isFinishEnabled = isFinishEnabled;
        this.isCancelEnabled = isCancelEnabled;
    }

    public boolean isNextEnabled()
    {
        return isNextEnabled;
    }

    public boolean isPreviousEnabled()
    {
        return isPreviousEnabled;
    }

    public boolean isFinishEnabled()
    {
        return isFinishEnabled;
    }

    public boolean isCancelEnabled()
    {
        return isCancelEnabled;
    }

    public boolean hasNext()
    {
        return hasNext;
    }

    public boolean hasPrevious()
    {
        return hasPrevious;
    }

    public String toString()
    {
        final StringBuffer textBuffer = new StringBuffer( 150 );
        textBuffer.append( "WizardStateChangeEvent: [" );
        textBuffer.append( " hasNext = " + hasNext() );
        textBuffer.append( " hasPrevious = " + hasPrevious() );
        textBuffer.append( " isNextEnabled = " + isNextEnabled() );
        textBuffer.append( " isPreviousEnabled = " + isPreviousEnabled() );
        textBuffer.append( " isFinishEnabled = " + isFinishEnabled() );
        textBuffer.append( "]" );

        return textBuffer.toString();
    }
}
