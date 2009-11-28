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
package ch.xmatrix.ups.uec.exam;

import ch.xmatrix.ups.domain.AbstractTaxonBased;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2007/05/16 17:00:15 $
 */
public class ExamModel extends AbstractTaxonBased
{
    private String prefsUid;

    private String levelsUid;

    private String groupsUid;

    private String specimensUid;

    private String constraintsUid;

    private String submitUid;

    public ExamModel( final ExamModel original )
    {
        prefsUid = original.prefsUid;
        levelsUid = original.levelsUid;
        groupsUid = original.groupsUid;
        specimensUid = original.specimensUid;
        constraintsUid = original.constraintsUid;
        submitUid = original.submitUid;
    }

    public ExamModel()
    {
        super();
    }

    public String getPrefsUid()
    {
        return prefsUid;
    }

    public void setPrefsUid( final String prefsUid )
    {
        this.prefsUid = prefsUid;
    }

    public String getLevelsUid()
    {
        return levelsUid;
    }

    public void setLevelsUid( final String levelsUid )
    {
        this.levelsUid = levelsUid;
    }

    public String getGroupsUid()
    {
        return groupsUid;
    }

    public void setGroupsUid( final String groupsUid )
    {
        this.groupsUid = groupsUid;
    }

    public String getSpecimensUid()
    {
        return specimensUid;
    }

    public void setSpecimensUid( final String specimensUid )
    {
        this.specimensUid = specimensUid;
    }

    public String getConstraintsUid()
    {
        return constraintsUid;
    }

    public void setConstraintsUid( final String constraintsUid )
    {
        this.constraintsUid = constraintsUid;
    }

    public String toString()
    {
        return getName();
    }

    public String toDebugString()
    {
        return getName();
    }

    /**
     * Returns the uids by index in the following order: (0) prefs, (1) groups, (2) specimens, (3) levesl and (4)
     * constraints.
     *
     * @param index the uid index
     * @return the uid
     */
    public String get( final int index )
    {
        switch ( index )
        {
            case 0:
                return prefsUid;
            case 1:
                return groupsUid;
            case 2:
                return specimensUid;
            case 3:
                return levelsUid;
            case 4:
                return constraintsUid;
            default:
                throw new IllegalArgumentException( "uid " + index + " not known" );
        }
    }

    public void setUid( final int which, final String uid )
    {
        switch ( which )
        {
            case 0:
                setPrefsUid( uid );
                break;
            case 1:
                setGroupsUid( uid );
                break;
            case 2:
                setSpecimensUid( uid );
                break;
            case 3:
                setLevelsUid( uid );
                break;
            case 4:
                setConstraintsUid( uid );
                break;
            default:
                throw new IllegalArgumentException( "uid " + which + " cannot be set" );
        }
    }
}
