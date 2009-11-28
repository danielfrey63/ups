package com.ethz.geobot.herbar.gui;

import com.ethz.geobot.herbar.modeapi.state.StateCompositeModel;
import com.ethz.geobot.herbar.model.Taxon;
import java.util.prefs.Preferences;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public abstract class ResultModel extends StateCompositeModel
{
    public Preferences loadCompositeState( final Preferences node )
    {
        return null;
    }

    public Preferences storeCompositeState( final Preferences node )
    {
        return null;
    }

    public abstract void setTaxFocus( Taxon focus );

    public abstract void reset();
}
