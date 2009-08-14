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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2008/01/23 22:19:54 $
 */
public class SpecimenModel
{

    private String taxon;

    private String id;

    private int weightIfKnown;

    private int weightIfUnknown;

    private boolean deactivatedIfKnown;

    private boolean deactivatedIfUnknown;

    private int numberOfSpecimens;

    private boolean backup;

    private String[] aspects;

    private transient String string;

    public SpecimenModel()
    {
        super();
    }

    public SpecimenModel(final SpecimenModel orig)
    {
        taxon = orig.taxon;
        id = orig.id;
        weightIfKnown = orig.weightIfKnown;
        weightIfUnknown = orig.weightIfUnknown;
        deactivatedIfKnown = orig.deactivatedIfKnown;
        deactivatedIfUnknown = orig.deactivatedIfUnknown;
        numberOfSpecimens = orig.numberOfSpecimens;
        aspects = orig.aspects;
    }

    public String getTaxon()
    {
        return taxon;
    }

    public void setTaxon(final String taxon)
    {
        string = null;
        this.taxon = taxon;
    }

    public String getId()
    {
        return id;
    }

    public void setId(final String id)
    {
        string = null;
        this.id = id;
    }

    public int getWeightIfKnown()
    {
        return weightIfKnown;
    }

    public void setWeightIfKnown(final int weightIfKnown)
    {
        string = null;
        this.weightIfKnown = weightIfKnown;
    }

    public int getWeightIfUnknown()
    {
        return weightIfUnknown;
    }

    public void setWeightIfUnknown(final int weightIfUnknown)
    {
        string = null;
        this.weightIfUnknown = weightIfUnknown;
    }

    public boolean isDeactivatedIfKnown()
    {
        if (numberOfSpecimens == 0)
        {
            deactivatedIfKnown = true;
        }
        return deactivatedIfKnown;
    }

    public void setDeactivatedIfKnown(final boolean deactivatedIfKnown)
    {
        string = null;
        this.deactivatedIfKnown = deactivatedIfKnown;
    }

    public boolean isDeactivatedIfUnknown()
    {
        if (numberOfSpecimens == 0)
        {
            deactivatedIfUnknown = true;
        }
        return deactivatedIfUnknown;
    }

    public void setDeactivatedIfUnknown(final boolean deactivatedIfUnknown)
    {
        string = null;
        this.deactivatedIfUnknown = deactivatedIfUnknown;
    }

    public int getNumberOfSpecimens()
    {
        return numberOfSpecimens;
    }

    public void setNumberOfSpecimens(final int numberOfSpecimens)
    {
        string = null;
        this.numberOfSpecimens = numberOfSpecimens;
    }

    public boolean isBackup()
    {
        return backup;
    }

    public void setBackup(final boolean backup)
    {
        this.backup = backup;
    }

    public String[] getAspects()
    {
        return aspects;
    }

    public void setAspects(final String[] aspects)
    {
        this.aspects = aspects;
    }

    public String toDebugString()
    {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append(id).append(taxon).append(numberOfSpecimens).toString();
    }

    public String toString()
    {
        if (string == null)
        {
            final StringBuffer buffer = new StringBuffer();
            buffer.append(" (");
            buffer.append(id == null ? "<keine ID definiert>" : id).append("/");
            buffer.append(getNumberOfSpecimens()).append("/");
            buffer.append(getWeightIfKnown()).append("/");
            buffer.append(getWeightIfUnknown()).append("/");
            buffer.append(!isDeactivatedIfKnown() ? "aktiv" : "inaktiv").append("/");
            buffer.append(!isDeactivatedIfUnknown() ? "aktiv" : "inaktiv").append(")");
            string = buffer.toString();
        }
        return string;
    }
}
