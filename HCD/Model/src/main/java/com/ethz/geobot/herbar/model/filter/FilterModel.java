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

import com.ethz.geobot.herbar.model.AbstractHerbarModel;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.LevelComparator;
import com.ethz.geobot.herbar.model.PictureTheme;
import com.ethz.geobot.herbar.model.Taxon;
import com.ethz.geobot.herbar.model.event.ModelChangeEvent;
import com.ethz.geobot.herbar.model.trait.Ecology;
import com.ethz.geobot.herbar.model.trait.Medicine;
import com.ethz.geobot.herbar.model.trait.Morphology;
import com.ethz.geobot.herbar.model.trait.Name;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to filter the data model. It depends on a initial data model and a special filter settings.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 */
public class FilterModel extends AbstractHerbarModel implements Cloneable
{
    private static final Logger LOG = LoggerFactory.getLogger( FilterModel.class );

    private HerbarModel dependentModel;

    private int rank;

    /**
     * Map between dependent and filtered taxon.
     */
    private Map<Taxon, FilterTaxon> dependentFilteredTaxonList = new HashMap<Taxon, FilterTaxon>();

    /**
     * Map between filtered and dependent taxon.
     */
    private Map<FilterTaxon, Taxon> filteredDependentTaxonList = new HashMap<FilterTaxon, Taxon>();

    private final boolean fixed;

    /**
     * Construct a filtered data model.
     * @param dependentModel the model that this model wraps
     * @param name           a name for the model
     * @param fixed          whether the list ist fixed and cannot be altered
     */
    public FilterModel( final HerbarModel dependentModel, final String name, final boolean fixed, final int rank )
    {
        super( name );
        this.dependentModel = dependentModel;
        this.fixed = fixed;
        this.rank = rank;
    }

    public Level getRootLevel()
    {
        return dependentModel.getRootLevel();
    }

    public Level getLastLevel()
    {
        return dependentModel.getLastLevel();
    }

    public FilterTaxon getRootTaxon()
    {
        return addFilterTaxon( dependentModel.getRootTaxon() );
    }

    public Morphology getMorphology()
    {
        return dependentModel.getMorphology();
    }

    public Ecology getEcology()
    {
        return dependentModel.getEcology();
    }

    public Medicine getMedicine()
    {
        return dependentModel.getMedicine();
    }

    public Name getSynonyms()
    {
        return dependentModel.getSynonyms();
    }

    public PictureTheme[] getPictureThemes()
    {
        return dependentModel.getPictureThemes();
    }

    public PictureTheme getPictureTheme( final String name )
    {
        return dependentModel.getPictureTheme( name );
    }

    public Level getLevel( final String name )
    {
        return dependentModel.getLevel( name );
    }

    public FilterTaxon getTaxon( final String taxonName )
    {
        FilterTaxon filterTaxon = null;
        final Taxon dependentTaxon = dependentModel.getTaxon( taxonName );
        if ( dependentTaxon != null )
        {
            // Todo: Check whether the taxon needs to be created here
            // search for taxon in cached list otherwise check if taxon is inside this filter
            filterTaxon = dependentFilteredTaxonList.get( dependentTaxon );
            if ( filterTaxon == null && contains( dependentTaxon ) )
            {
                filterTaxon = addFilterTaxon( dependentTaxon );
            }
        }
        return filterTaxon;
    }

    public FilterTaxon addFilterTaxon( final Taxon dependentTaxon )
    {
        FilterTaxon filteredTaxon = dependentFilteredTaxonList.get( dependentTaxon );
        if ( filteredTaxon == null )
        {
            filteredTaxon = new FilterTaxon( this, dependentTaxon );
            dependentFilteredTaxonList.put( dependentTaxon, filteredTaxon );
            filteredDependentTaxonList.put( filteredTaxon, dependentTaxon );
        }
        return filteredTaxon;
    }

    public Level[] getLevels()
    {
        final TreeSet<Level> result = new TreeSet<Level>( new LevelComparator() );
        for ( final Taxon filteredTaxon : filteredDependentTaxonList.keySet() )
        {
            result.add( filteredTaxon.getLevel() );
        }
        return result.toArray( new Level[result.size()] );
    }

    public boolean contains( final Taxon dependentTaxon )
    {
        LOG.trace( "check if dependent taxon \"" + dependentTaxon + "\" is in model \"" + getName() + "\"" );
        return dependentFilteredTaxonList.get( dependentTaxon ) != null;
    }

    public void removeFilterTaxon( final FilterTaxon taxon )
    {
        final Taxon dependentTaxon = (taxon instanceof FilterTaxon ? ((FilterTaxon) taxon).getDependentTaxon() : taxon);
        final Taxon filterTaxon = (taxon instanceof FilterTaxon ? taxon : dependentFilteredTaxonList.get( taxon ));
        dependentFilteredTaxonList.remove( dependentTaxon );
        filteredDependentTaxonList.remove( filterTaxon );
        notifyModelChange();
    }

    /**
     * Implement behaviour on model change.
     */
    void notifyModelChange()
    {
        fireModelChangeEvent( new ModelChangeEvent( this ) );
    }

    public boolean isFixed()
    {
        return fixed;
    }

    public int getRank()
    {
        return rank;
    }

    public void setRank( int rank )
    {
        this.rank = rank;
    }
}
