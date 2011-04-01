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
package com.ethz.geobot.herbar.gui.tax;

import ch.jfactory.component.ObjectPopup;
import com.ethz.geobot.herbar.model.Level;
import java.awt.Component;

/**
 * LevelPopup control, used to select the level of a scope. You have to override the itemSelected method to get notified of the level change.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
abstract public class LevelPopUp extends ObjectPopup
{
    /**
     * Construct a LevelPopup with all given levels.
     *
     * @param levels all levels which should be displayed
     */
    public LevelPopUp( final Level[] levels )
    {
        super( levels );
    }

    /**
     * displays the level popup and set the current level settings.
     *
     * @param jb           parent component
     * @param scopeLevels  all levels available for this scope
     * @param currentLevel current selected level
     */
    public void showPopUp( final Component jb, final Level[] scopeLevels, final Level currentLevel )
    {
        super.showPopUp( jb, scopeLevels, currentLevel );
    }
}
