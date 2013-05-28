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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    private Map<Taxon, Taxon> filteredTaxonList = new HashMap<Taxon, Taxon>();

    private List<FilterDefinitionDetail> filterDetails = new ArrayList<FilterDefinitionDetail>();

    private final boolean fixed;

    /**
     * Construct a filtered data model.
     *
     * @param dependentModel the model to be filtered
     * @param name           a name for the model
     * @param fixed
     */
    public FilterModel( final HerbarModel dependentModel, final String name, boolean fixed )
    {
        super( name );
        this.fixed = fixed;
        if ( LOG.isDebugEnabled() )
        {
            LOG.debug( "dependentModel: " + dependentModel.getName() );
        }
        this.dependentModel = dependentModel;
        addFilterDetail();
    }

    public Level getRootLevel()
    {
        return dependentModel.getRootLevel();
    }

    public Level getLastLevel()
    {
        return dependentModel.getLastLevel();
    }

    public Taxon getRootTaxon()
    {
        return createFilterTaxon( dependentModel.getRootTaxon() );
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

    public void setDependentModel( final HerbarModel model )
    {
        if ( LOG.isDebugEnabled() )
        {
            LOG.debug( "dependent model change to: " + model );
        }
        if ( dependentModel == model )
        {
            return;
        }
        // Todo: Do not clear the existing filter, but migrate them into the new dependency.
        // One approach could be to set all scope for unknown taxa to the root taxon, and to reestablish the
        // consistency with respect to cached levels, children, references to base models etc (see commented code
        // below). Another approach could be to recreate each scope with respect to the correct dependent taxon, which
        // might also has to be recreated, to reflect the new dependency, as dependency model is stored also in the
        // dependent filter taxon.
        this.dependentModel = model;
        filterDetails.clear();
        addFilterDetail();
        notifyModelChange();
    }

    public Taxon getTaxon( final String taxonName )
    {
        Taxon taxon = null;
        final Taxon dependentTaxon = dependentModel.getTaxon( taxonName );
        if ( dependentTaxon != null )
        {
            // search for taxon in cached list otherwise check if taxon is inside this filter
            taxon = filteredTaxonList.get( dependentTaxon );
            if ( taxon == null && isIn( dependentTaxon ) )
            {
                taxon = createFilterTaxon( dependentTaxon );
            }
        }
        return taxon;
    }

    Taxon createFilterTaxon( final Taxon dependentTaxon )
    {
        Taxon filteredTaxon = filteredTaxonList.get( dependentTaxon );
        if ( filteredTaxon == null )
        {
            filteredTaxon = new FilterTaxon( this, dependentTaxon );
            filteredTaxonList.put( dependentTaxon, filteredTaxon );
        }
        return filteredTaxon;
    }

    public Level[] getLevels()
    {
//        return dependentModel.getLevels();
        final Set<Level> levelsSet = new HashSet<Level>();
        for ( final Object filterDetail : filterDetails )
        {
            final FilterDefinitionDetail detail = (FilterDefinitionDetail) filterDetail;
            levelsSet.addAll( Arrays.asList( detail.getLevels() ) );
        }
        final Level[] levels = levelsSet.toArray( new Level[levelsSet.size()] );
        Arrays.sort( levels, new LevelComparator() );
        return levels;
    }

    /**
     * This method generate a FilterDefinitionDetail for the current model.
     *
     * @param scope  scope Taxon of the detail
     * @param levels array of Level for the detail
     * @return object of type FilterDefinitionDetail
     */
    public FilterDefinitionDetail createFilterDetail( final Taxon scope, final Level[] levels )
    {
        LOG.trace( "create new filter detail for scope: " + scope + " levels: " + Arrays.toString( levels ) );
        return new FilterDefinitionDetail( this, scope, levels );
    }

    public HerbarModel getDependantModel()
    {
        return dependentModel;
    }

    public boolean isIn( final Taxon taxon )
    {
        LOG.trace( "check if taxon \"" + taxon + "\" is in model." );
        boolean isIn = false;
        for ( Iterator<FilterDefinitionDetail> it = filterDetails.iterator(); it.hasNext() && !isIn; )
        {
            final FilterDefinitionDetail detail = it.next();
            isIn = detail.isIn( taxon );
        }
        LOG.trace( "check, if taxon >" + taxon + "< is in FilterModel returns " + isIn );
        return isIn;
    }

    public void addFilterDetail( final FilterDefinitionDetail detail )
    {
        filterDetails.add( detail );
        notifyModelChange();
    }

    /**
     * insert a specified FilterDefinitionDetail
     *
     * @param scope  Taxon which is the scope
     * @param levels Levels of the scope which should be visible
     * @return the FilterDefinitionDetail object which is added to the list
     */
    public FilterDefinitionDetail addFilterDetail( final Taxon scope, final Level[] levels )
    {
        final FilterDefinitionDetail detail = createFilterDetail( scope, levels );
        addFilterDetail( detail );
        return detail;
    }

    /**
     * insert a default FilterDefinitionDetail to the list. It depends on the root taxon of the dependent model and all levels.
     *
     * @return the FilterDefinitionDetail object which is added to the list
     */
    public FilterDefinitionDetail addFilterDetail()
    {
        return addFilterDetail( dependentModel.getRootTaxon(), dependentModel.getLevels() );
    }

    public void removeFilterDetail( final FilterDefinitionDetail detail )
    {
        filterDetails.remove( detail );
        notifyModelChange();
    }

    public FilterDefinitionDetail[] getFilterDetails()
    {
        return filterDetails.toArray( new FilterDefinitionDetail[filterDetails.size()] );
    }

    /** Removes the content of the filter. */
    public void clearFilterDetails()
    {
        LOG.info( "clear all filter details" );
        filterDetails.clear();
        notifyModelChange();
    }

    /** implement behaviour on model change. It destroy the cached collection of FilterTaxon and notify all listeners */
    void notifyModelChange()
    {
        filteredTaxonList.clear();
        this.fireModelChangeEvent( new ModelChangeEvent( this ) );
    }

    public boolean isFixed()
    {
        return fixed;
    }
}
