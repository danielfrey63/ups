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
package ch.xmatrix.ups.ust.edit;

/**
 * Action command ids.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2006/07/27 16:38:57 $
 */
public class Commands {

    // Common

    public static final String FACENAME_TOOLBAR = "toolbar";

    // For TaxonCheckBuilder

    public static final String COMMANDID_TAXTREE_SELECTEDONLY = "taxontree.showselected";
    public static final String COMMANDID_TAXTREE_ALLSPECIES = "taxontree.showall";
    public static final String COMMANDID_TAXTREE_DEEP = "taxontree.deep";
    public static final String COMMANDID_TAXTREE_FLAT = "taxontree.flat";
    public static final String GROUPID_TAXTREE_TOOLBAR = "taxontree.toolbar";

    // For ConstraintsSelectionBuilder

    public static final String COMMANDID_CONSTRAINTS_OPEN = "constraintstree.showopen";
    public static final String COMMANDID_CONSTRAINTS_ALL = "constraintstree.showall";
    public static final String GROUPID_CONSTRAINTS_TOOLBAR = "constraintstree.toolbar";
}
