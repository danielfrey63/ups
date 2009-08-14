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
package ch.jfactory.binding;

import com.jgoodies.binding.beans.Model;

/**
 * Default {@link InfoModel} implementation simply notifying listeners.
 *
 * @author Daniel Frey
 * @version $Revision: 1.5 $ $Date: 2008/01/06 10:16:23 $
 */
public class DefaultInfoModel extends Model implements InfoModel
{

    private Note note = new SimpleNote("", 0);

    public Note getNote()
    {
        return note;
    }

    public void setNote(final Note note)
    {
        final Note old = getNote();
        this.note = note;
        firePropertyChange(InfoModel.PROPERTYNAME_NOTE, old, note);
    }
}
