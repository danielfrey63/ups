package com.ethz.geobot.herbar.model.filter;

import com.ethz.geobot.herbar.model.AbstractHerbarModel;
import com.ethz.geobot.herbar.model.Ecology;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.LevelComparator;
import com.ethz.geobot.herbar.model.Medicine;
import com.ethz.geobot.herbar.model.Morphology;
import com.ethz.geobot.herbar.model.MorphologyValue;
import com.ethz.geobot.herbar.model.PictureTheme;
import com.ethz.geobot.herbar.model.Taxon;
import com.ethz.geobot.herbar.model.event.ModelChangeEvent;
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

    /**
     * Construct a filtered data model.
     *
     * @param dependentModel the model to be filtered
     * @param name           a name for the model
     */
    public FilterModel( final HerbarModel dependentModel, final String name )
    {
        super( name );
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
//        for (Iterator iterator = filterDetails.iterator(); iterator.hasNext();) {
//            FilterDefinitionDetail detail = (FilterDefinitionDetail) iterator.next();
//
//            Taxon scope = detail.getScope();
//            if (dependentModel instanceof FilterModel && !((FilterModel)dependentModel).isIn(scope)) {
//                scope = getRootTaxon();
//            }
//            FilterTaxon filterTaxon = (FilterTaxon) scope;
//            filterTaxon.clearCachedSubLevels();
//            Taxon dependent = filterTaxon.getDependentTaxon();
//            if (dependent instanceof FilterTaxon) {
//                FilterTaxon dependentTaxon = (FilterTaxon) dependent;
//                dependentTaxon.setFilterModel((FilterModel)model);
//            }
//            detail.setScope(scope);
//
//            Level[] levels = detail.getLevels();
//            levels = (Level[]) ArrayUtils.intersect(levels, dependentModel.getLevels(), new Level[0]);
//            detail.setLevels(levels);
//        }
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

    public Object clone() throws CloneNotSupportedException
    {
        try
        {
            final FilterModel model = (FilterModel) super.clone();
            // prepare FilterModel to be independent
            model.filteredTaxonList = new HashMap<Taxon, Taxon>();
            model.filterDetails = new ArrayList<FilterDefinitionDetail>( this.filterDetails.size() );
            for ( final Object filterDetail : filterDetails )
            {
                final FilterDefinitionDetail filterDefinitionDetail = (FilterDefinitionDetail) filterDetail;
                model.filterDetails.add( new FilterDefinitionDetail( model, filterDefinitionDetail.getScope(), filterDefinitionDetail.getLevels() ) );
            }
            return model;
        }
        catch ( CloneNotSupportedException ex )
        {
            LOG.error( "clone isn't supported", ex );
            throw new RuntimeException( ex );
        }
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
     * insert a default FilterDefinitionDetail to the list. It depends on the root taxon of the dependent model and all
     * levels.
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

    /** @see HerbarModel#getValues(String) */
    public MorphologyValue[] getValues( final String name )
    {
        throw new NoSuchMethodError( "getValue(String) not implemented yet" );
    }

    /** @see HerbarModel#getTaxa(com.ethz.geobot.herbar.model.MorphologyValue) */
    public Taxon[] getTaxa( final MorphologyValue morphologyValue )
    {
        throw new NoSuchMethodError( "getTaxon(MorphologyValue) not implemented yet" );
    }
}
