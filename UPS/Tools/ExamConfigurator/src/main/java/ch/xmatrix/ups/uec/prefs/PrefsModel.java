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
package ch.xmatrix.ups.uec.prefs;

import ch.xmatrix.ups.domain.AbstractTaxonBased;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Model that collects data for the whole configuration of exam set generation.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2006/11/16 13:25:24 $
 */
public class PrefsModel extends AbstractTaxonBased
{

    private int knownTotalCount;

    private int unknownTotalCount;

    private int knownTotalWeight;

    private int unknownTotalWeight;

    private int maximumSeries;

    public PrefsModel()
    {
        super();
    }

    public PrefsModel(final PrefsModel orig)
    {
        super(orig);
        setKnownTotalCount(orig.getKnownTotalCount());
        setKnownTotalWeight(orig.getKnownTotalWeight());
        setUnknownTotalCount(orig.getUnknownTotalCount());
        setUnknownTotalWeight(orig.getUnknownTotalWeight());
        setMaximumSeries(orig.getMaximumSeries());
    }

    public int getKnownTotalCount()
    {
        return knownTotalCount;
    }

    public void setKnownTotalCount(final int knownTotalCount)
    {
        this.knownTotalCount = knownTotalCount;
    }

    public int getUnknownTotalCount()
    {
        return unknownTotalCount;
    }

    public void setUnknownTotalCount(final int unknownTotalCount)
    {
        this.unknownTotalCount = unknownTotalCount;
    }

    public int getKnownTotalWeight()
    {
        return knownTotalWeight;
    }

    public void setKnownTotalWeight(final int knownTotalWeight)
    {
        this.knownTotalWeight = knownTotalWeight;
    }

    public int getUnknownTotalWeight()
    {
        return unknownTotalWeight;
    }

    public void setUnknownTotalWeight(final int unknownTotalWeight)
    {
        this.unknownTotalWeight = unknownTotalWeight;
    }

    public int getMaximumSeries()
    {
        return maximumSeries;
    }

    public void setMaximumSeries(final int maximumSeries)
    {
        this.maximumSeries = maximumSeries;
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
