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
package com.ethz.geobot.herbar.model.filter;

import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains a definition detail.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 */
public class FilterDefinitionDetail implements Cloneable
{
    private final static Logger LOG = LoggerFactory.getLogger( FilterDefinitionDetail.class );

    private Taxon scope;

    private Level[] levels;

    private final FilterModel model;

    FilterDefinitionDetail( final FilterModel model, final Taxon scope, final Level[] levels )
    {
        this.model = model;
        this.scope = scope;
        this.levels = levels;
    }

    public Taxon getScope()
    {
        return scope;
    }

    public void setScope( final Taxon scope )
    {
        this.scope = scope;
        model.notifyModelChange();
    }

    public Level[] getLevels()
    {
        return levels;
    }

    public void setLevels( final Level[] levels )
    {
        this.levels = levels;
        model.notifyModelChange();
    }

    public boolean isIn( final Taxon taxon )
    {
        LOG.trace( "check taxon: " + taxon );
        LOG.trace( "isChild of scope " + scope + ": " + isChild( taxon ) );
        LOG.trace( "level check " + Arrays.asList( levels ) + ": " + taxon.getLevel() );

        // is scope root ?
        if ( isRootTaxon( taxon ) )
        {
            return true;
        }

        if ( scope.equals( taxon ) || isChild( taxon ) )
        {
            for ( final Level level : levels )
            {
                if ( taxon.getLevel().equals( level ) )
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isChild( final Taxon tax )
    {
        Taxon parent = tax.getParentTaxon();
        while ( parent != null )
        {
            if ( scope.equals( parent ) )
            {
                return true;
            }
            parent = parent.getParentTaxon();
        }
        return false;
    }

    private boolean isRootTaxon( final Taxon tax )
    {
        return tax.getParentTaxon() == null;
    }

    public String toString()
    {
        return scope + " " + Arrays.asList( levels );
    }
}
