package com.ethz.geobot.herbar.model;

import java.util.Comparator;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
public class LevelComparator implements Comparator<Level>
{
    public int compare( final Level o1, final Level o2 )
    {
        if ( o1 == o2 )
        {
            return 0;
        }
        else if ( o1 != null && o1.isHigher( o2 ) )
        {
            return -1;
        }
        else
        {
            return 1;

        }
    }
}
