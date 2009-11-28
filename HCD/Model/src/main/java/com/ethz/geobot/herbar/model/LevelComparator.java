package com.ethz.geobot.herbar.model;

import java.util.Comparator;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
public class LevelComparator implements Comparator
{
    public int compare( final Object o1, final Object o2 )
    {
        final Level l1 = (Level) o1;
        final Level l2 = (Level) o2;
        if ( l1 == l2 )
        {
            return 0;
        }
        else if ( l1 != null && l1.isHigher( l2 ) )
        {
            return -1;
        }
        else
        {
            return 1;

        }
    }
}
