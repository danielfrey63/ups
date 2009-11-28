package com.ethz.geobot.herbar.gui;

import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Taxon;
import java.util.Enumeration;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class DefaultResultModel extends ResultModel
{
    private final HerbarModel model;

    public DefaultResultModel( final HerbarModel model )
    {
        this.model = model;
    }

    public HerbarModel getModel()
    {
        return model;
    }

    public void setTaxFocus( final Taxon focus )
    {
        final Enumeration e = subStateModels();
        while ( e.hasMoreElements() )
        {
            final DetailResultModel detailResultModel = (DetailResultModel) e.nextElement();
            detailResultModel.setTaxFocus( focus );
        }
    }

    public void reset()
    {
        final Enumeration e = subStateModels();
        while ( e.hasMoreElements() )
        {
            final DetailResultModel detailResultModel = (DetailResultModel) e.nextElement();
            detailResultModel.reset();
        }
    }
}
