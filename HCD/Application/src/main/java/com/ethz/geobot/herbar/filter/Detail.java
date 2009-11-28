/*
 * Detail.java
 *
 * Created on 26. Juli 2002, 15:14
 */

package com.ethz.geobot.herbar.filter;

import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.filter.FilterDefinitionDetail;

/**
 * Bean class to load filter detail definition
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class Detail
{
    /**
     * Holds value of property scope.
     */
    private String scope;

    /**
     * Holds value of property levels.
     */
    private String[] levels;

    /**
     * Creates a new instance of Detail
     */
    public Detail()
    {
    }

    public Detail( final FilterDefinitionDetail detail )
    {
        scope = detail.getScope().getName();
        final Level[] detailLevels = detail.getLevels();
        levels = new String[detailLevels.length];
        for ( int i = 0; i < detailLevels.length; i++ )
        {
            levels[i] = detailLevels[i].getName();
        }
    }

    /**
     * Getter for property scope.
     *
     * @return Value of property scope.
     */
    public String getScope()
    {
        return this.scope;
    }

    /**
     * Setter for property scope.
     *
     * @param scope New value of property scope.
     */
    public void setScope( final String scope )
    {
        this.scope = scope;
    }

    /**
     * Getter for property levels.
     *
     * @return Value of property levels.
     */
    public String[] getLevels()
    {
        return this.levels;
    }

    /**
     * Setter for property levels.
     *
     * @param levels New value of property levels.
     */
    public void setLevels( final String[] levels )
    {
        this.levels = levels;
    }
}
