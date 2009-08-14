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
package ch.jfactory.event;

import javax.swing.event.ChangeEvent;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2006/03/22 15:05:10 $
 */
public class VetoableChangeEvent extends ChangeEvent
{

    private Object newValue;

    private Object oldValue;

    public VetoableChangeEvent(final Object source, final Object oldValue, final Object newValue)
    {
        super(source);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public Object getNewValue()
    {
        return newValue;
    }

    public Object getOldValue()
    {
        return oldValue;
    }
}
