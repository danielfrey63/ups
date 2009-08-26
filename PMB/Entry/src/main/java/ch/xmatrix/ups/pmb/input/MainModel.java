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
package ch.xmatrix.ups.pmb.input;

import ch.xmatrix.ups.pmb.domain.FileEntry;
import ch.xmatrix.ups.pmb.ui.model.PMBModel;
import java.util.TreeSet;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2008/01/23 22:18:35 $
 */
public class MainModel extends PMBModel {

    private final TreeSet<FileEntry> selectedImageFiles = new TreeSet<FileEntry>();

    private boolean recursiveOverviewList = false;

    public boolean isRecursiveOverviewList() {
        return recursiveOverviewList;
    }

    public void setRecursiveOverviewList(final boolean recursiveOverviewList) {
        this.recursiveOverviewList = recursiveOverviewList;
    }

    public TreeSet<FileEntry> getSelectedImageFiles() {
        return selectedImageFiles;
    }
}
