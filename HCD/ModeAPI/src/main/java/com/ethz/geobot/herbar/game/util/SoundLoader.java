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
package com.ethz.geobot.herbar.game.util;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class SoundLoader extends Thread
{
    SoundList soundList;

    URL completeURL;

    String relativeURL;

    public SoundLoader( final SoundList soundList, final String baseURL, final String relativeURL )
    {
        this.soundList = soundList;
        completeURL = SoundLoader.class.getResource( baseURL + relativeURL );
        this.relativeURL = relativeURL;
        setPriority( MIN_PRIORITY );
        start();
    }

    public void run()
    {
        final AudioClip audioClip = Applet.newAudioClip( completeURL );
        if ( audioClip == null )
        {
            System.err.println( "could not load sound file " + completeURL );
        }
        soundList.putClip( audioClip, relativeURL );
    }
}
