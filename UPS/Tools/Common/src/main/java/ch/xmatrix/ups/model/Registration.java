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

import ch.xmatrix.ups.domain.PlantList;
import com.wegmueller.ups.lka.IAnmeldedaten;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2008/01/23 22:19:54 $
 */
public class Registration
{
    private IAnmeldedaten anmeldedaten;

    private PlantList plantList;

    private boolean defaultList = false;

    public Registration( final IAnmeldedaten anmeldedaten, final PlantList plantList )
    {
        this.setAnmeldedaten( anmeldedaten );
        this.setPlantList( plantList );
    }

    public IAnmeldedaten getAnmeldedaten()
    {
        return anmeldedaten;
    }

    public void setAnmeldedaten( final IAnmeldedaten anmeldedaten )
    {
        this.anmeldedaten = anmeldedaten;
    }

    public PlantList getPlantList()
    {
        return plantList;
    }

    public void setPlantList( final PlantList plantList )
    {
        this.plantList = plantList;
    }

    public boolean isDefaultList()
    {
        return defaultList;
    }

    public void setDefaultList( final boolean defaultList )
    {
        this.defaultList = defaultList;
    }

    public String toString()
    {
        return anmeldedaten.getVorname() + " " + anmeldedaten.getNachname() + ", " + anmeldedaten.getStudentennummer() +
                ", Pflanzenliste " + ( plantList == null ? "keine" : "vorhanden" );
    }
}
