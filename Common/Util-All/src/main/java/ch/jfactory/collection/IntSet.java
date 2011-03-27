/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.collection;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
public class IntSet
{
    private int[] ints = new int[1];

    private int size = 0;

    public void add( final int i )
    {
        for ( int j = 0; j < size; j++ )
        {
            if ( ints[j] == i )
            {
                return;
            }
        }
        if ( size == ints.length )
        {
            final int[] newInts = new int[ints.length * 2];
            System.arraycopy( ints, 0, newInts, 0, ints.length );
            ints = newInts;
        }
        ints[size++] = i;
    }

    public int[] getArray()
    {
        final int[] result = new int[size];
        System.arraycopy( ints, 0, result, 0, size );
        return result;
    }

    public int size()
    {
        return size;
    }

    /** @see Object#toString() */
    public String toString()
    {
        return "" + ints;
    }

    public int get( final int i )
    {
        return ints[i];
    }

    public boolean contains( final int pId )
    {
        boolean found = false;
        int i = 0;
        while ( !found && i < size )
        {
            found = ints[i++] == pId;
        }
        return found;
    }
}
