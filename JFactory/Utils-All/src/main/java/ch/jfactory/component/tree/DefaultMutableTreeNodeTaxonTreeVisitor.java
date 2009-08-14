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
package ch.jfactory.component.tree;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * A simple {@link TreeVisitor} implementation to wrap an object into a {@link DefaultMutableTreeNode}.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:54:58 $
 */
public class DefaultMutableTreeNodeTaxonTreeVisitor implements TreeVisitor {

    public void add(final Object parent, final Object child) {
        ((DefaultMutableTreeNode) parent).add((DefaultMutableTreeNode) child);
    }

    public Object handle(final Object object) {
        return new DefaultMutableTreeNode(object);
    }
}
