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
package com.ethz.geobot.herbar.model.db.impl;

import ch.jfactory.model.graph.GraphNodeImpl;
import ch.jfactory.model.graph.GraphNodeList;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:18 $
 */
public class LevelImpl extends GraphNodeImpl implements Level
{
    /** @see Level#isLower(Level) */
    public boolean isLower( final Level level )
    {
        // root taxon has a null level.
        return level == null || getRank() > ( (LevelImpl) level ).getRank();
    }

    /** @see Level#isHigher(Level) */
    public boolean isHigher( final Level level )
    {
        // root taxon has a null level.
        return level != null && getRank() < ( (LevelImpl) level ).getRank();
    }

    /** @see Level#getChildLevel() */
    public Level getChildLevel()
    {
        final GraphNodeList list = getChildren( LevelImpl.class );
        return (LevelImpl) list.get( 0 );
    }

    /** @see Level#getParentLevel() */
    public Level getParentLevel()
    {
        final GraphNodeList list = getParents( LevelImpl.class );
        return (LevelImpl) list.get( 0 );
    }

    /** @see Level#getTaxa() */
    public Taxon[] getTaxa()
    {
        final GraphNodeList taxa = getChildren( TaxonImpl.class );
        return (TaxonImpl[]) taxa.getAll();
    }

    /** @see Level#getSubLevels() */
    public Level[] getSubLevels()
    {
        // TODO: Unite as redundant with MutableLevelImpl
        int size = 0;
        Level level = this;
        while ( ( level = level.getChildLevel() ) != null )
        {
            size++;
        }
        level = getChildLevel();
        final Level[] result = new Level[size];
        for ( int i = 0; i < result.length; i++ )
        {
            result[i] = level;
            level = level.getChildLevel();
        }
        return result;
    }

    /** @see Level#getSuperLevels() */
    public Level[] getSuperLevels()
    {
        // TODO: Unite as redundant with MutableLevelImpl
        int size = 0;
        Level level = this;
        while ( ( level = level.getParentLevel() ) != null )
        {
            size++;
        }
        level = getParentLevel();
        final Level[] result = new Level[size];
        for ( int i = 0; i < result.length; i++ )
        {
            result[i] = level;
            level = level.getParentLevel();
        }
        return result;
    }

}
