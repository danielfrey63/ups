package com.ethz.geobot.herbar.model.db.impl;

import ch.jfactory.model.graph.GraphNodeImpl;
import com.ethz.geobot.herbar.model.Picture;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:18 $
 */
public class PictureImpl extends GraphNodeImpl implements Picture
{
    /**
     * @see Picture#getRelativURL()
     */
    public String getRelativURL()
    {
        return getName();
    }

}
