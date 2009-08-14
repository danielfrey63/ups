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

import ch.xmatrix.ups.domain.TaxonBased;
import ch.xmatrix.ups.uec.master.MasterDetailsFactory;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2006/04/17 23:29:42 $
 */
public class LevelsFactory extends MasterDetailsFactory
{

    public TaxonBased createInstance()
    {
        return new LevelsModel();
    }

    public TaxonBased createCopy(final TaxonBased orig)
    {
        if (!(orig instanceof LevelsModel))
        {
            throw new IllegalArgumentException("copy only allowed for instances of " + LevelsModel.class);
        }
        return new LevelsModel((LevelsModel) orig);
    }
}
