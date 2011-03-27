/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.component.table;

import ch.jfactory.lang.ArrayUtils;
import com.jgoodies.binding.beans.Model;

/**
 * Holds the state for a column to sort. Maintains three states: Ascending, descending and no sorting.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2007/09/27 10:41:47 $
 */
public class SortState extends Model
{
    public static final State SORT_ASCENDING = new State( "sortAscending" );

    public static final State SORT_DESCENDING = new State( "sortDescending" );

    public static final State SORT_NONE = new State( "sortNone" );

    private static final State DEFAULT_STATE = SORT_NONE;

    private static final State[] STATES = {SORT_NONE, SORT_ASCENDING, SORT_DESCENDING};

    public static final String PROPERTYNAME_DIRECTIVE = "directive";

    public static final Directive DEFAULT_DIRECTIVE = new Directive( DEFAULT_STATE, -1 );

    private Directive directive = DEFAULT_DIRECTIVE;

    public Directive getDirective()
    {
        return directive;
    }

    public void setDirective( final Directive directive )
    {
        final Directive old = getDirective();
        this.directive = directive;
        firePropertyChange( PROPERTYNAME_DIRECTIVE, old, directive );
    }

    public int getColumn()
    {
        return directive.getColumn();
    }

    public State getState()
    {
        return directive.getState();
    }

    public void setIncreasedDirective( final int column )
    {
        setDirective( column, 1 );
    }

    public void setDecreasedDirective( final int column )
    {
        setDirective( column, 2 );
    }

    private void setDirective( final int column, final int delta )
    {
        final State state;
        if ( column == directive.getColumn() )
        {
            state = directive.getState();
        }
        else
        {
            state = SORT_NONE;
        }
        final int index = ArrayUtils.indexOf( STATES, state );
        final int newIndex = ( index + delta ) % 3;
        setDirective( new Directive( STATES[newIndex], column ) );
    }

    public static class Directive
    {
        private final State state;

        private final int column;

        public Directive( final State state, final int column )
        {
            this.state = state;
            this.column = column;
        }

        public State getState()
        {
            return state;
        }

        public int getColumn()
        {
            return column;
        }

        public boolean equals( final Object obj )
        {
            if ( obj == null )
            {
                return false;
            }
            if ( !( obj instanceof Directive ) )
            {
                return false;
            }
            final Directive other = (Directive) obj;
            return ( other.column == column && other.state == state );
        }

        public int hashCode()
        {
            return state.hashCode() ^ column;
        }

        public String toString()
        {
            return state + " " + column;
        }
    }

    public static class State
    {
        private final String state;

        private State( final String state )
        {
            this.state = state;
        }

        public String toString()
        {
            return state;
        }
    }
}
