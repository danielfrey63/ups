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
package ch.xmatrix.ups.uec.level;

import ch.xmatrix.ups.domain.AbstractTaxonBased;
import java.util.ArrayList;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2006/04/17 23:29:42 $
 */
public class LevelsModel extends AbstractTaxonBased
{

    /** List of {@link LevelModel}s. */
    private ArrayList<LevelModel> models = new ArrayList<LevelModel>();

    public LevelsModel()
    {
        super();
    }

    public LevelsModel(final LevelsModel orig)
    {
        super(orig);
        for (int i = 0; i < orig.models.size(); i++)
        {
            final LevelModel origModel = orig.models.get(i);
            models.add(new LevelModel(origModel));
        }
    }

    public ArrayList<LevelModel> getLevelModels()
    {
        return models;
    }

    public String toString()
    {
        return getName();
    }

    public String toDebugString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
