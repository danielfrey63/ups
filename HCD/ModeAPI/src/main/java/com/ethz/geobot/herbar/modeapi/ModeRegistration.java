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
package com.ethz.geobot.herbar.modeapi;

/**
 * This class is used by the Mode to register itself into the application.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 */
public class ModeRegistration
{
    private static ModeRegistrationSupport registrationSupport;

    /**
     * This method is used by a Mode to register itself.
     *
     * @param mode reference to itself
     */
    public static void register( final Mode mode )
    {
        registrationSupport.register( mode );
    }

    /**
     * This method is used by the Application to set the reference to the object where the modes should registered. This Method should not used by the Mode.
     *
     * @param registrationSupport reference to registration object
     */
    public static void setRegistrationSupport( final ModeRegistrationSupport registrationSupport )
    {
        ModeRegistration.registrationSupport = registrationSupport;
    }
}
