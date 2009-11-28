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
package ch.jfactory.projecttime.domain.impl;

import ch.jfactory.projecttime.domain.api.IFEntry;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * TODO: document
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:56:29 $
 */
public class Invoice
{
    private String number = "Invoice001";

    private Calendar charged = Calendar.getInstance();

    private Calendar due = Calendar.getInstance();

    private Set entries;

    public String getNumber()
    {
        return number;
    }

    public void setNumber( final String number )
    {
        this.number = number;
    }

    public Calendar getCharged()
    {
        return charged;
    }

    public void setCharged( final Calendar charged )
    {
        this.charged = charged;
    }

    public Calendar getDue()
    {
        return due;
    }

    public void setDue( final Calendar due )
    {
        this.due = due;
    }

    public void addEntry( final IFEntry entry )
    {
        if ( entries == null )
        {
            entries = new HashSet();
        }
        entries.add( entry );
    }

    public Collection getEntries()
    {
        return entries;
    }

    public void removeEntry( final IFEntry entry )
    {
        if ( entries != null )
        {
            entries.remove( entry );
        }
    }
}
