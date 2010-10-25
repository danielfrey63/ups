package com.ethz.geobot.herbar.gui.lesson;

import ch.jfactory.math.RandomUtils;
import com.ethz.geobot.herbar.modeapi.SimpleTaxStateModel;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaxStateModel
{
    public static final String MODEL = "MODEL";

    public static final String TAX_LIST = "TAXON_LIST";

    private static final Logger LOG = LoggerFactory.getLogger( TaxStateModel.class );

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport( this );

    private Taxon[] taxList;

    private HerbarModel herbarModel;

    private final SimpleTaxStateModel taxModel;

    public TaxStateModel( final HerbarModel model )
    {
        this.herbarModel = model;
        taxModel = new SimpleTaxStateModel( propertyChangeSupport );
        init( model );
    }

    public HerbarModel getModel()
    {
        return herbarModel;
    }

    public void setModel( final HerbarModel model )
    {
        final HerbarModel oldModel = this.herbarModel;
        this.herbarModel = model;
        propertyChangeSupport.firePropertyChange( MODEL, oldModel, model );
        init( model );
    }

    public Taxon getNext( final Taxon t )
    {
        for ( int i = 0; i < taxList.length; i++ )
        {
            if ( taxList[i] == t )
            {
                if ( ( i + 1 ) < taxList.length )
                {
                    return taxList[i + 1];
                }
                return null;
            }
        }
        return null;
    }

    public Taxon getPrev( final Taxon t )
    {
        for ( int i = 0; i < taxList.length; i++ )
        {
            if ( taxList[i] == t )
            {
                if ( i > 0 )
                {
                    return taxList[i - 1];
                }
                return null;
            }
        }
        return null;
    }

    public void setScope( final Taxon scope )
    {
        if ( taxModel.getScope() != scope )
        {
            taxModel.setScope( scope );
            changeTaxList();
        }
    }

    public void setLevel( final Level level )
    {
        if ( taxModel.getLevel() != level )
        {
            taxModel.setLevel( level );
            changeTaxList();
        }
    }

    public void setSortedList( final boolean sort )
    {
        if ( taxModel.isOrdered() != sort )
        {
            taxModel.setOrdered( sort );
            changeTaxList();
        }
    }

    public void setFocus( final Taxon focus )
    {
        taxModel.setFocus( focus );
    }

    public Taxon getScope()
    {
        return taxModel.getScope();
    }

    public Level getLevel()
    {
        return taxModel.getLevel();
    }

    public boolean isSortedList()
    {
        return taxModel.isOrdered();
    }

    public Taxon getFocus()
    {
        return taxModel.getFocus();
    }

    public Taxon[] getTaxList()
    {
        return taxList;
    }

    private void changeTaxList()
    {
        final Taxon[] oldList = taxList;
        final Taxon scope = getScope();
        final Level level = getLevel();
        taxList = scope.getAllChildTaxa( level );
        if ( ( taxList == null ) || ( taxList.length == 0 ) )
        {
            taxList = new Taxon[]{scope};
            LOG.error( "taxon list for scope " + scope + " and level " + level + " is null or empty" );
        }
        if ( !taxModel.isOrdered() )
        {
            RandomUtils.randomize( taxList );
        }
        else
        {
            Arrays.sort( taxList );
        }
        propertyChangeSupport.firePropertyChange( TAX_LIST, oldList, taxList );
    }

    private void init( final HerbarModel model )
    {
        final Taxon rootTaxon = model.getRootTaxon();
        final Level[] subLevels = rootTaxon.getSubLevels();
        setScope( rootTaxon );
        setLevel( subLevels[subLevels.length - 1] );
        setFocus( taxList[0] );
        setSortedList( true );
    }

    public synchronized void addPropertyChangeListener( final String property, final PropertyChangeListener listener )
    {
        propertyChangeSupport.addPropertyChangeListener( property, listener );
        if ( SimpleTaxStateModel.FOCUS.equals( property ) )
        {
            listener.propertyChange( new PropertyChangeEvent( this, property, getFocus(), getFocus() ) );
        }
    }

    public synchronized void addPropertyChangeListener( final PropertyChangeListener listener )
    {
        propertyChangeSupport.addPropertyChangeListener( listener );
    }
}
