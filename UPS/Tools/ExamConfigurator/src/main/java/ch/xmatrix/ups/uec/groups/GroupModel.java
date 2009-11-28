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
package ch.xmatrix.ups.uec.groups;

import java.util.ArrayList;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2006/04/18 12:10:54 $
 */
public class GroupModel
{
    private String name;

    private int minimum;

    private int maximum;

    /**
     * List of taxon strings.
     */
    private ArrayList<String> taxa;

    public GroupModel( final String name )
    {
        setName( name );
        setTaxa( new ArrayList<String>() );
    }

    public GroupModel( final GroupModel orig )
    {
        this( orig.getName() );
        setMinimum( orig.getMinimum() );
        setMaximum( orig.getMaximum() );
        getTaxa().addAll( orig.getTaxa() );
    }

    public String getName()
    {
        return name;
    }

    public void setName( final String name )
    {
        this.name = name;
    }

    public int getMinimum()
    {
        return minimum;
    }

    public void setMinimum( final int minimum )
    {
        this.minimum = minimum;
    }

    public int getMaximum()
    {
        return maximum;
    }

    public void setMaximum( final int maximum )
    {
        this.maximum = maximum;
    }

    public ArrayList<String> getTaxa()
    {
        return taxa;
    }

    public void setTaxa( final ArrayList<String> taxa )
    {
        this.taxa = taxa;
    }

    public void addTaxon( final String taxon )
    {
        taxa.add( taxon );
    }

    public void removeTaxon( final String taxon )
    {
        taxa.remove( taxon );
    }

    public String toString()
    {
        return getName() + ( taxa == null ? "" : " (" + taxa.size() + ")" );
    }
}
