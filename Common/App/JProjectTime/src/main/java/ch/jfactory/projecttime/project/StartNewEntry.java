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
package ch.jfactory.projecttime.project;

import ch.jfactory.projecttime.domain.api.IFEntry;
import java.util.Calendar;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * TODO: document
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:56:29 $
 */
public class StartNewEntry extends ActionCommand
{
    private final ProjectModel model;

    public StartNewEntry( final CommandManager manager, final ProjectModel model )
    {
        super( manager, Commands.COMMANDID_START );
        this.model = model;
    }

    protected void handleExecute()
    {
        getCommandManager().getCommand( Commands.COMMANDID_ADD ).execute();
        final IFEntry entry = model.getNewChild();
        entry.setName( "Bemerkung zur Arbeit in dieser Zeit" );
        entry.setType( "Zeiteintrag" );
        entry.setStart( Calendar.getInstance() );
        model.setRunning( entry );
        model.getCurrentBeanModel().setBean( entry );
    }
}
