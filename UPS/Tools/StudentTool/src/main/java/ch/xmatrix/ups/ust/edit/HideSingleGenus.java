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

import ch.jfactory.filter.Filter;
import ch.xmatrix.ups.domain.SimpleTaxon;
import org.apache.log4j.Logger;

/**
 * Returns false for all genus taxa with one child.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2006/07/27 16:38:57 $
 */
public class HideSingleGenus implements Filter
{
    private static final Logger LOGGER = Logger.getLogger( HideSingleGenus.class );

    private static final boolean DEBUG = LOGGER.isDebugEnabled();

    public boolean matches( final Object obj )
    {
        final boolean isShown;
        final SimpleTaxon taxon = (SimpleTaxon) obj;
        final boolean isGenus = SimpleTaxon.isGenus( taxon );
        final boolean hasOneChild = taxon.getChildTaxa() != null && taxon.getChildTaxa().size() == 1;
        isShown = !( isGenus && hasOneChild );
        if ( DEBUG )
        {
            LOGGER.debug( "HideSingleGenus " + ( isShown ? "+ " : "- " ) + taxon );
        }
        return isShown;
    }
}
