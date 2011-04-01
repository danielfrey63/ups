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
package com.ethz.geobot.herbar.game.util;

import java.applet.AudioClip;
import java.util.Hashtable;
import javax.swing.JApplet;

/** Loads and holds a bunch of audio files whose locations are specified relative to a fixed base URL. */
public class SoundList extends Hashtable
{
    JApplet applet;

    String baseURL;

    public SoundList( final String baseURL )
    {
        super( 5 ); //Initialize Hashtable with capacity of 5 entries.
        this.baseURL = baseURL;
    }

    public void startLoading( final String relativeURL )
    {
        new SoundLoader( this, baseURL, relativeURL );
    }

    public AudioClip getClip( final String relativeURL )
    {
        return (AudioClip) get( relativeURL );
    }

    public void putClip( final AudioClip clip, final String relativeURL )
    {
        put( relativeURL, clip );
    }
}
// $Log