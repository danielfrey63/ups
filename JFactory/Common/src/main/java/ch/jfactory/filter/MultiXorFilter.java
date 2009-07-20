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
package ch.jfactory.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Allows to set and remove multiple filters and returns true if one and only one of these filters match.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:54:58 $
 */
public class MultiXorFilter implements Filter {

    private List filters;

    public MultiXorFilter(final Filter[] filters) {
        this.filters = new ArrayList(Arrays.asList(filters));
    }

    public void addFilter(final Filter filter) {
        filters.add(filter);
    }

    public void removeFilter(final Filter filter) {
        filters.remove(filter);
    }

    public boolean matches(final Object obj) {
        int count = 0;
        for (int i = 0; i < filters.size(); i++) {
            final Filter filter = (Filter) filters.get(i);
            if (filter.matches(obj)) {
                count++;
            }
        }
        return count == 1;
    }
}
