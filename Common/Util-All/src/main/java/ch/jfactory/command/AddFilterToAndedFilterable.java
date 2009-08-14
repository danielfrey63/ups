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
 * @version $Revision: 1.1 $ $Date: 2006/08/29 13:10:43 $
 */
public class AddFilterToAndedFilterable extends ToggleCommand
{

    private Filterable filterable;

    private Filter filter;

    private TreeExpandedRestorer expander;

    public AddFilterToAndedFilterable(final CommandManager manager, final String id, final Filterable filterable,
                                      final Filter filter, final TreeExpandedRestorer expander)
    {
        super(manager, id);
        this.filterable = filterable;
        this.filter = filter;
        this.expander = expander;
    }

    protected void handleSelection(final boolean selected) throws ToggleVetoException
    {
        if (selected)
        {
            expander.save();
            ((MultiAndFilter) filterable.getFilter()).addFilter(filter);
            filterable.filter();
            expander.restore();
        }
    }
}
