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
package ch.xmatrix.ups.ust.main.commands;

import ch.jfactory.application.AbstractMainModel;
import ch.xmatrix.ups.domain.Constraints;
import ch.xmatrix.ups.model.SessionModel;
import ch.xmatrix.ups.ust.main.MainModel;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Arrays;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * Opens a default taxon list stored in the constraints.
 *
 * @author Daniel Frey
 * @version $Revision: 1.5 $ $Date: 2007/05/16 17:00:16 $
 */
public class NewDefaultCommand extends ActionCommand
{
    private final MainModel model;

    public NewDefaultCommand( final CommandManager commandManager, final MainModel model )
    {
        super( commandManager, Commands.COMMANDID_NEWDEFAULT );
        this.model = model;
    }

    protected void handleExecute()
    {
        try
        {
            final SessionModel sessionModel = Commands.runExamInfoChooser( model );
            if ( sessionModel != null )
            {
                model.sessionModels.setSelection( sessionModel );
                Commands.setNewUserModel( model );
                final Constraints constraints = (Constraints) AbstractMainModel.findModel( model.getUserModel().getConstraintsUid() );
                model.getUserModel().setTaxa( new ArrayList<String>( Arrays.asList( constraints.getDefaultTaxa() ) ) );
                model.setOpening();
            }
        }
        catch ( PropertyVetoException e )
        {
        }
    }
}
