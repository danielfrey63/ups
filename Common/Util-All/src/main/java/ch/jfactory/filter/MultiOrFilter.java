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
 * Allows to set and remove multiple filters and returns true if one or more of these filters match.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
public class MultiOrFilter implements Filter
{

    private List filters;

    public MultiOrFilter(final Filter[] filters)
    {
        this.filters = new ArrayList(Arrays.asList(filters));
    }

    public void addFilter(final Filter filter)
    {
        filters.add(filter);
    }

    public void removeFilter(final Filter filter)
    {
        filters.remove(filter);
    }

    public boolean matches(final Object obj)
    {
        boolean matches = false;
        for (Object filter1 : filters)
        {
            final Filter filter = (Filter) filter1;
            matches |= filter.matches(obj);
        }
        return matches;
    }
}
