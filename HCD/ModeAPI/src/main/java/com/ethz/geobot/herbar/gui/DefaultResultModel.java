package com.ethz.geobot.herbar.gui;

import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Taxon;
import java.util.Enumeration;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class DefaultResultModel extends ResultModel {

    private HerbarModel model;

    public DefaultResultModel(HerbarModel model) {
        this.model = model;
    }

    public HerbarModel getModel() {
        return model;
    }

    public void setTaxFocus(Taxon focus) {
        Enumeration e = subStateModels();
        while (e.hasMoreElements()) {
            DetailResultModel detailResultModel = (DetailResultModel) e.nextElement();
            detailResultModel.setTaxFocus(focus);
        }
    }

    public void reset() {
        Enumeration e = subStateModels();
        while (e.hasMoreElements()) {
            DetailResultModel detailResultModel = (DetailResultModel) e.nextElement();
            detailResultModel.reset();
        }
    }
}
