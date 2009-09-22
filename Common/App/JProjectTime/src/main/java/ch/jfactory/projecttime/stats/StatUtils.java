/* ====================================================================
 *  Copyright 2004-2005 www.xmatrix.ch
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.jfactory.projecttime.stats;

import ch.jfactory.projecttime.domain.api.IFEntry;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Calendar;

/**
 * Utility methods for statistics.
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:56:29 $
 */
public class StatUtils {

    public static long sumAll(IFEntry entry, long sum) {
        final IFEntry[] children = entry.getChildren();
        final Calendar start = entry.getStart();
        final Calendar end = entry.getEnd();
        if (start != null && end != null) {
            sum += (end.getTime().getTime() - start.getTime().getTime());
        }
        for (int i = 0; children != null && i < children.length; i++) {
            final IFEntry child = children[i];
            sum += sumAll(child, 0);
        }
        return sum;
    }

    public static long sumAll(IFEntry[] entries, long sum) {
        for (int i = 0; i < entries.length; i++) {
            final IFEntry entry = entries[i];
            sum += sumAll(entry, 0);
        }
        return sum;
    }

    public static class StatsValueHolder {

        public long duration;
        public Date start;
        public Date end;
        public Map dailySum = new HashMap();
        public Map categorySum = new HashMap();
    }
}
