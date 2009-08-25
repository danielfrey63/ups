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
package ch.xmatrix.ups.pmb.ui.model;

import ch.jfactory.component.tree.AbstractTreeModel;
import ch.xmatrix.ups.pmb.domain.Entry;
import ch.xmatrix.ups.pmb.domain.FileEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.tree.TreePath;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2008/01/23 22:18:48 $
 */
public class EntryTreeModel extends AbstractTreeModel {

    private Comparator<Entry> sorter;

    private Collection<Entry> entries;

    public EntryTreeModel(final Collection<Entry> entries) {
        super(new Entry("root", null, null));
        this.entries = entries;
        sorter = new Comparator<Entry>() {
            public int compare(final Entry e1, final Entry e2) {
                final int p1 = e1.getSettings().getSortedPosition(e1.getPath());
                final int p2 = e2.getSettings().getSortedPosition(e2.getPath());
                return p1 - p2;
            }
        };
    }

    protected void remove(final Object child, final TreePath parentPath) {
    }

    protected void insert(final TreePath childPath, final TreePath parentPath, final int pos) {
    }

    public Object getChild(final Object parent, final int index) {
        if (parent == root) {
            final Entry[] strings = entries.toArray(new Entry[entries.size()]);
            Arrays.sort(strings);
            return strings[index];
        } else {
            final Entry entry = (Entry) parent;
            final List<Entry> children = getNonFileChildren(entry);
            return children.get(index);
        }
    }

    public int getChildCount(final Object parent) {
        if (parent == root) {
            return entries.size();
        } else {
            final Entry entry = (Entry) parent;
            final List<Entry> children = getNonFileChildren(entry);
            return children.size();
        }
    }

    public boolean isLeaf(final Object node) {
        return getChildCount(node) == 0;
    }

    public void valueForPathChanged(final TreePath path, final Object newValue) {
    }

    private List<Entry> getNonFileChildren(final Entry entry) {
        final List<Entry> list = entry.getList();
        final List<Entry> children = new ArrayList<Entry>();
        for (final Entry child : list) {
            if (!(child instanceof FileEntry)) {
                children.add(child);
            }
        }
        Collections.sort(children, sorter);
        return children;
    }
}
