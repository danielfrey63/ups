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
 * Allow two filters to be chained together with a logical <b>and</b> operation.
 *
 * @author Bradley S. Huffman
 * @version $Revision: 1.1 $, $Date: 2005/11/17 11:54:58 $
 */
public final class AndFilter extends AbstractFilter
{
    // Filter for left side of logical <b>and</b>.
    private final Filter left;

    // Filter for right side of logical <b>and</b>.
    private final Filter right;

    /**
     * Match if only both supplied filters match.
     *
     * @param left  left side of logical <b>and</b>
     * @param right right side of logical <b>and</b>
     * @throws IllegalArgumentException if either supplied filter is null
     */
    public AndFilter( final Filter left, final Filter right )
    {
        if ( ( left == null ) || ( right == null ) )
        {
            throw new IllegalArgumentException( "null filter not allowed" );
        }
        this.left = left;
        this.right = right;
    }

    public boolean matches( final Object obj )
    {
        return left.matches( obj ) && right.matches( obj );
    }

    public boolean equals( final Object obj )
    {
        if ( this == obj )
        {
            return true;
        }

        if ( obj instanceof AndFilter )
        {
            final AndFilter filter = (AndFilter) obj;
            if ( ( left.equals( filter.left ) && right.equals( filter.right ) ) ||
                    ( left.equals( filter.right ) && right.equals( filter.left ) ) )
            {
                return true;
            }
        }
        return false;
    }

    public int hashCode()
    {
        return ( 31 * left.hashCode() ) + right.hashCode();
    }

    public String toString()
    {
        return new StringBuffer( 64 )
                .append( "[AndFilter: " )
                .append( left.toString() )
                .append( ",\n" )
                .append( "            " )
                .append( right.toString() )
                .append( "]" )
                .toString();
    }
}
