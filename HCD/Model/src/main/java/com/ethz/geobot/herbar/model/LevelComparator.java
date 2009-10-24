package com.ethz.geobot.herbar.model;

import java.util.Comparator;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
public class LevelComparator implements Comparator {

    public int compare(Object o1, Object o2) {
        Level l1 = (Level) o1;
        Level l2 = (Level) o2;
        if (l1 == l2) {
            return 0;
        }
        else if (l1 != null && l1.isHigher(l2)) {
            return -1;
        }
        else {
            return 1;

        }
    }
}
