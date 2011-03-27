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

/**
 * Filter that is the logical <b>negation</b> operation of another filter.
 *
 * @author Bradley S. Huffman
 * @version $Revision: 1.1 $, $Date: 2005/11/17 11:54:58 $
 */
public final class NegateFilter extends AbstractFilter
{
    // Underlying filter.
    private final Filter filter;

    /**
     * Match if the supplied filter <b>does not</b> match.
     *
     * @param filter filter to use.
     */
    public NegateFilter( final Filter filter )
    {
        this.filter = filter;
    }

    public boolean matches( final Object obj )
    {
        return !filter.matches( obj );
    }

    public Filter negate()
    {
        return filter;
    }

    public boolean equals( final Object obj )
    {
        if ( this == obj )
        {
            return true;
        }

        if ( obj instanceof NegateFilter )
        {
            return filter.equals( ( (NegateFilter) obj ).filter );
        }
        return false;
    }

    public int hashCode()
    {
        return ~filter.hashCode();
    }

    public String toString()
    {
        return new StringBuffer( 64 )
                .append( "[NegateFilter: " )
                .append( filter.toString() )
                .append( "]" )
                .toString();
    }
}
