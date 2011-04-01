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
package com.ethz.geobot.herbar.game.catcher;

import com.ethz.geobot.herbar.modeapi.AbstractModeAdapter;
import com.ethz.geobot.herbar.modeapi.ModeRegistration;

/**
 * Modeclass for game catcher.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 * @created $Date: 2007/09/17 11:05:58 $
 */
public class CatcherMode extends AbstractModeAdapter
{
    /** @see Object#Object() */
    public CatcherMode()
    {
        super( Catcher.class );
    }

    static
    {
        ModeRegistration.register( new CatcherMode() );
    }
}
