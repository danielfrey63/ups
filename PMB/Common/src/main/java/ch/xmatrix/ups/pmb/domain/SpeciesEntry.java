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
package ch.xmatrix.ups.pmb.domain;

import ch.xmatrix.ups.pmb.ui.model.Settings;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2007/09/27 10:47:37 $
 */
public class SpeciesEntry extends Entry implements Comparable<Entry> {

    public SpeciesEntry(final String name, final Entry parent, final Settings settings) {
        super(name, parent, settings);
    }

    public int compareTo(final Entry o) {
        if (!(o instanceof SpeciesEntry)) {
            return 0;
        }
        final SpeciesEntry that = (SpeciesEntry) o;
        final String thisName = getSettings().getCleanedSpeciesName(this);
        final String thatName = getSettings().getCleanedSpeciesName(that);
        return thisName.compareTo(thatName);
    }
}
