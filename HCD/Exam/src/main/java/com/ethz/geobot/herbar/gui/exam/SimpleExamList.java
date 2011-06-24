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

package com.ethz.geobot.herbar.gui.exam;

import ch.jfactory.math.RandomUtils;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;

/**
 * Chooses randomly a list from all taxa on the last level.
 *
 * @author Daniel Frey 24.06.11 16:07
 */
public class SimpleExamList implements ExamList
{
    private HerbarModel herbarModel;

    public Taxon[] getExamList( final int size )
    {
        final Taxon[] result = new Taxon[size];
        final Taxon root = herbarModel.getRootTaxon();
        final Level level = herbarModel.getLastLevel();
        final Taxon[] taxa = root.getAllChildTaxa( level );
        RandomUtils.randomize( taxa );
        System.arraycopy( taxa, 0, result, 0, size );
        return result;
    }

    public void setHerbarModel( final HerbarModel herbarModel )
    {
        this.herbarModel = herbarModel;
    }
}
