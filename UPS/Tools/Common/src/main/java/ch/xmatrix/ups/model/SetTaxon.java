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

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2008/01/23 22:19:54 $
 */
public class SetTaxon
{
    private final SpecimenModel specimenModel;

    private final boolean known;

    public SetTaxon( final SpecimenModel specimenModel, final boolean known )
    {
        this.specimenModel = specimenModel;
        this.known = known;
    }

    public SpecimenModel getSpecimenModel()
    {
        return specimenModel;
    }

    public String toString()
    {
        final String id = specimenModel.getId();
        return ( id == null ? "" : id + " " ) + "[" + specimenModel.getTaxon() + ", " +
                ( known ? "weightIfKnown=" + specimenModel.getWeightIfKnown() : "weightIfUnkown=" + specimenModel.getWeightIfUnknown() )
                + ", known=" + known + "]";
    }
}
