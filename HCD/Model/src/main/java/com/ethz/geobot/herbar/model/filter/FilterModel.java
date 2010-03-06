package com.ethz.geobot.herbar.model.filter;

import com.ethz.geobot.herbar.model.AbstractHerbarModel;
import com.ethz.geobot.herbar.model.EcoSubject;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.LevelComparator;
import com.ethz.geobot.herbar.model.MedSubject;
import com.ethz.geobot.herbar.model.MorSubject;
import com.ethz.geobot.herbar.model.MorValue;
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
 * This class is used to filter the datamodel. It depends on a initial data model and a special filter settings.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 */
public class FilterModel extends AbstractHerbarModel implements Cloneable
{
    private static final Logger LOG = LoggerFactory.getLogger( FilterModel.class );

    private HerbarModel dependentModel;

    private Map filteredTaxonList = new HashMap();

    private List filterDetails = new ArrayList();

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

    public MorSubject getRootMorSubject()
    {
        return dependentModel.getRootMorSubject();
    }

    public EcoSubject getRootEcoSubject()
    {
        return dependentModel.getRootEcoSubject();
    }

    public MedSubject getRootMedSubject()
    {
        return dependentModel.getRootMedSubject();
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
        // One approach could be to set all scope for unknwon taxa to the root taxon, and to reestablish the
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
            taxon = (Taxon) filteredTaxonList.get( dependentTaxon );
            if ( taxon == null && isIn( dependentTaxon ) )
            {
                taxon = createFilterTaxon( dependentTaxon );
            }
        }
        return taxon;
    }

    Taxon createFilterTaxon( final Taxon dependentTaxon )
    {
        Taxon filteredTaxon = (Taxon) filteredTaxonList.get( dependentTaxon );
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
        final Set levelsSet = new HashSet();
        for ( final Object filterDetail : filterDetails )
        {
            final FilterDefinitionDetail detail = (FilterDefinitionDetail) filterDetail;
            levelsSet.addAll( Arrays.asList( detail.getLevels() ) );
        }
        final Level[] levels = (Level[]) levelsSet.toArray( new Level[0] );
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
        if ( LOG.isDebugEnabled() )
        {
            LOG.debug( "create new filter detail for scope: " + scope + " levels: " + levels );
        }
        return new FilterDefinitionDetail( this, scope, levels );
    }

    public Object clone()
    {
        try
        {
            final FilterModel model = (FilterModel) super.clone();
            // prepare FilterModel to be undependend
            model.filteredTaxonList = new HashMap();
            model.filterDetails = new ArrayList( this.filterDetails.size() );
            for ( final Object filterDetail : filterDetails )
            {
                final FilterDefinitionDetail filterDefinitionDetail = (FilterDefinitionDetail) filterDetail;
                model.filterDetails.add( new FilterDefinitionDetail( model, filterDefinitionDetail.getScope(), filterDefinitionDetail.getLevels() ) );
            }
            return model;
        }
        catch ( CloneNotSupportedException ex )
        {
            LOG.error( "clone isn't supported by cloneable class !?!?!", ex );
            throw new RuntimeException( ex );
        }
    }

    public HerbarModel getDependantModel()
    {
        return dependentModel;
    }

    public boolean isIn( final Taxon taxon )
    {
        if ( LOG.isInfoEnabled() )
        {
            LOG.info( "check, if taxon >" + taxon + "< is in FilterModel." );
        }
        boolean isIn = false;
        for ( Iterator it = filterDetails.iterator(); it.hasNext() && !isIn; )
        {
            final FilterDefinitionDetail detail = (FilterDefinitionDetail) it.next();
            isIn = detail.isIn( taxon );
        }
        if ( LOG.isDebugEnabled() )
        {
            LOG.debug( "check, if taxon >" + taxon + "< is in FilterModel returns " + isIn );
        }
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
     * @return the FilterDefintionDetail object which is added to the list
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
     * @return the FilterDefintionDetail object which is added to the list
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
        return (FilterDefinitionDetail[]) filterDetails.toArray( new FilterDefinitionDetail[0] );
    }

    public void clearFilterDetails()
    {
        LOG.info( "clear all filter details" );
        filterDetails.clear();
        notifyModelChange();
    }

    /**
     * implement behaviour on model change. It destroy the cached FilterTaxon's and notify all listeners
     */
    void notifyModelChange()
    {
        filteredTaxonList.clear();
        this.fireModelChangeEvent( new ModelChangeEvent( this ) );
    }

    /**
     * @see HerbarModel#getValues(String)
     */
    public MorValue[] getValues( final String name )
    {
        throw new NoSuchMethodError( "getValue(String) not implemented yet" );
    }

    /**
     * @see HerbarModel#getTaxa(MorValue)
     */
    public Taxon[] getTaxa( final MorValue mor )
    {
        throw new NoSuchMethodError( "getTaxon(MorValue) not implemented yet" );
    }
}
