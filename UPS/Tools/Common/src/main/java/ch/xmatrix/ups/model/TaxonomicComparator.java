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

import ch.xmatrix.ups.domain.SimpleTaxon;
import java.util.Comparator;

/**
 * TODO: document
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2006/11/16 13:25:24 $
 */
public class TaxonomicComparator implements Comparator<String>
{
    private final TaxonTree taxa;

    public TaxonomicComparator( final TaxonTree taxa )
    {
        this.taxa = taxa;
    }

    public int compare( final String o1, final String o2 )
    {
        final SimpleTaxon t1 = taxa.findTaxonByName( o1 );
        final SimpleTaxon t2 = taxa.findTaxonByName( o2 );
        if ( t1 == null )
        {
            throw new NullPointerException( "Cannot find taxon for " + o1 );
        }
        if ( t2 == null )
        {
            throw new NullPointerException( "Cannot find taxon for " + o2 );
        }
        final int r1 = t1.getRank();
        final int r2 = t2.getRank();
        return ( r1 == r2 ? 0 : ( r1 < r2 ? -1 : 1 ) );
    }
}
