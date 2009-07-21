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
package ch.xmatrix.ups.model;

import ch.xmatrix.ups.domain.AbstractTaxonBased;
import ch.xmatrix.ups.model.SpecimenModel;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2008/01/23 22:19:54 $
 */
public class SpecimensModel extends AbstractTaxonBased {

    private ArrayList<SpecimenModel> models = new ArrayList<SpecimenModel>();
    private HashMap<String,SpecimenModel> index = new HashMap<String, SpecimenModel>();

    private transient SpecimenModel current = null;

    public SpecimensModel() {
        super();
    }

    public SpecimensModel(final SpecimensModel orig) {
        super(orig);
        models = new ArrayList<SpecimenModel>();
        for (int i = 0; orig.models != null && i < orig.models.size(); i++) {
            final SpecimenModel model = orig.models.get(i);
            final SpecimenModel copy = new SpecimenModel(model);
            models.add(copy);
            index.put(copy.getTaxon(), copy);
        }
    }

    public void setCurrent(final SpecimenModel current) {
        this.current = current;
    }

    public SpecimenModel getCurrent() {
        return current;
    }

    public void addSpecimenModel(final SpecimenModel model) {
        if (models == null) {
            models = new ArrayList<SpecimenModel>();
        }
        if (index == null) {
            index = new HashMap<String, SpecimenModel>();
        }
        models.add(model);
        index.put(model.getTaxon(), model);
    }

    public void remove(final SpecimenModel model) {
        models.remove(model);
        index.remove(model.getTaxon());
    }

    public SpecimenModel find(final String taxon) {
        return index.get(taxon);
    }

    public String toString() {
        return getName();
    }

    public String toDebugString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
    }
}
