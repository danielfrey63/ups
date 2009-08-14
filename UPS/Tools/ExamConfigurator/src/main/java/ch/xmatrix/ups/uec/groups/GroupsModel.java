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
package ch.xmatrix.ups.uec.groups;

import ch.xmatrix.ups.domain.AbstractTaxonBased;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2006/04/17 23:29:42 $
 */
public class GroupsModel extends AbstractTaxonBased
{

    /** List of {@link GroupModel}s. */
    private ArrayList<GroupModel> groups = new ArrayList<GroupModel>();

    private Map<String, GroupModel> index = new HashMap<String, GroupModel>();

    private transient GroupModel currentGroup;

    public GroupsModel()
    {
        super();
    }

    public GroupsModel(final GroupsModel orig)
    {
        super(orig);
        for (int i = 0; i < orig.groups.size(); i++)
        {
            final GroupModel origModel = orig.groups.get(i);
            final GroupModel group = new GroupModel(origModel);
            groups.add(group);
            final ArrayList taxa = group.getTaxa();
            for (int j = 0; j < taxa.size(); j++)
            {
                final String taxon = (String) taxa.get(j);
                index.put(taxon, group);
            }
        }
    }

    public void addTaxon(final String taxon)
    {
        if (index == null)
        {
            index = new HashMap<String, GroupModel>();
        }
        index.put(taxon, currentGroup);
        currentGroup.addTaxon(taxon);
    }

    public void removeTaxon(final String taxon)
    {
        index.remove(taxon);
        currentGroup.removeTaxon(taxon);
    }

    public void setCurrentGroup(final GroupModel currentGroup)
    {
        this.currentGroup = currentGroup;
    }

    public GroupModel getCurrentGroup()
    {
        return currentGroup;
    }

    public void addGroup(final GroupModel group)
    {
        if (groups == null)
        {
            groups = new ArrayList<GroupModel>();
        }
        groups.add(group);
        currentGroup = group;
    }

    public void removeGroup()
    {
        final int i = groups.indexOf(currentGroup);
        final ArrayList taxa = currentGroup.getTaxa();
        for (final Iterator iterator = taxa.iterator(); iterator.hasNext();)
        {
            final String taxon = (String) iterator.next();
            index.remove(taxon);
            iterator.remove();
        }
        groups.remove(currentGroup);
        final int size = groups.size();
        currentGroup = (GroupModel) (size == 0 ? null : groups.get(i < size ? i : (size - 1)));
    }

    public ArrayList<GroupModel> getGroups()
    {
        return groups;
    }

    public GroupModel find(final String taxon)
    {
        return index.get(taxon);
    }

    public String toString()
    {
        return getName();
    }

    public String toDebugString()
    {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
    }
}
