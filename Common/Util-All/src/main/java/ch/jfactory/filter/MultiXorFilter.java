/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
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
public class MultiXorFilter implements Filter
{
    private final List filters;

    public MultiXorFilter( final Filter[] filters )
    {
        this.filters = new ArrayList( Arrays.asList( filters ) );
    }

    public void addFilter( final Filter filter )
    {
        filters.add( filter );
    }

    public void removeFilter( final Filter filter )
    {
        filters.remove( filter );
    }

    public boolean matches( final Object obj )
    {
        int count = 0;
        for ( final Object filter1 : filters )
        {
            final Filter filter = (Filter) filter1;
            if ( filter.matches( obj ) )
            {
                count++;
            }
        }
        return count == 1;
    }
}
