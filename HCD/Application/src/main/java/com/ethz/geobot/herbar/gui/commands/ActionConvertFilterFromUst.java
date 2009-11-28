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
package com.ethz.geobot.herbar.gui.commands;

import com.ethz.geobot.herbar.Application;
import com.ethz.geobot.herbar.filter.FilterFactory;
import com.ethz.geobot.herbar.filter.FilterPersistentException;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.filter.FilterModel;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 * TODO: document
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class ActionConvertFilterFromUst extends AbstractAction
{
    public void actionPerformed( final ActionEvent e )
    {
        try
        {
            final HerbarModel base = Application.getInstance().getModel();
            final FilterModel model = new FilterModel( base, "Pruefungsliste" );
            FilterFactory.getInstance().saveFilterModel( model );
        }
        catch ( FilterPersistentException e1 )
        {
            e1.printStackTrace();
        }
    }
}
