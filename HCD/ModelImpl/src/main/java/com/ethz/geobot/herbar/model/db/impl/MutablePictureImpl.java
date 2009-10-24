package com.ethz.geobot.herbar.model.db.impl;

import com.ethz.geobot.herbar.model.Picture;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:18 $
 */
public class MutablePictureImpl extends MutableGraphNodeImpl implements Picture {

    /**
     * @see com.ethz.geobot.herbar.model.Picture#getRelativURL()
     */
    public String getRelativURL() {
        return getName();
    }

}
