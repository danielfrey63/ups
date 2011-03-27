/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.command;

import ch.jfactory.component.tree.TreeExpandedRestorer;
import ch.jfactory.filter.Filter;
import ch.jfactory.filter.Filterable;
import ch.jfactory.filter.MultiAndFilter;
import org.pietschy.command.CommandManager;
import org.pietschy.command.ToggleCommand;
import org.pietschy.command.ToggleVetoException;

/**
 * Removes the specified filter from the filterable tree model. The expanded state of the tree is restored.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:54:58 $
 */
public class RemoveFilterFromAndedFilterable extends ToggleCommand
{
    private final Filterable filterable;

    private final Filter filter;

    private final TreeExpandedRestorer expander;

    public RemoveFilterFromAndedFilterable( final CommandManager manager, final String id, final Filterable filterable,
                                            final Filter filter, final TreeExpandedRestorer expander )
    {
        super( manager, id );
        this.filterable = filterable;
        this.filter = filter;
        this.expander = expander;
    }

    protected void handleSelection( final boolean selected ) throws ToggleVetoException
    {
        if ( selected )
        {
            expander.save();
            ( (MultiAndFilter) filterable.getFilter() ).removeFilter( filter );
            filterable.filter();
            expander.restore();
        }
    }
}
