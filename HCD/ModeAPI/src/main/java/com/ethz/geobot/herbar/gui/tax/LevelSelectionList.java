package com.ethz.geobot.herbar.gui.tax;

import ch.jfactory.component.list.DefaultJList;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import javax.swing.ListSelectionModel;
import org.apache.log4j.Logger;

/**
 * list box, to select multiple levels.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class LevelSelectionList extends DefaultJList
{
    private static final Logger LOG = Logger.getLogger( LevelSelectionList.class );

    public LevelSelectionList()
    {
        super();
        setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );

        if ( LOG.isDebugEnabled() )
        {
            LOG.debug( "create level selection list with no entries" );
        }
    }

    public LevelSelectionList( final HerbarModel model )
    {
        super( model.getLevels() );

        if ( LOG.isDebugEnabled() )
        {
            LOG.debug( "create level selection list for model " + model.getName() );
            LOG.debug( "create level selection with levels = " + model.getLevels() );
        }
    }

    public void setHerbarModel( final HerbarModel model )
    {
        if ( LOG.isDebugEnabled() )
        {
            LOG.debug( "init level selection list for model " + model.getName() );
            LOG.debug( "init level selection with levels = " + model.getLevels() );
        }
        this.setListData( model.getLevels() );
    }

    public void setSelectedLevels( final Level[] levels )
    {
        if ( LOG.isDebugEnabled() )
        {
            LOG.debug( "preselected levels: " + levels );
        }

        setSelectedValues( levels );
    }

    public Level[] getSelectedLevels()
    {
        final Object[] objs = this.getSelectedValues();
        final Level[] levels = new Level[objs.length];
        System.arraycopy( objs, 0, levels, 0, objs.length );
        return levels;
    }
}
