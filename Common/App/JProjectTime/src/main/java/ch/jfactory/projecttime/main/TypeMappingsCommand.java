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
package ch.jfactory.projecttime.main;

import ch.jfactory.application.view.dialog.I15nComponentDialog;
import ch.jfactory.typemapper.TypeMapperBuilder;
import ch.jfactory.typemapper.TypeModel;
import javax.swing.JComponent;
import javax.swing.JDialog;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * TODO: document
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.2 $ $Date: 2006/11/16 13:25:17 $
 */
public class TypeMappingsCommand extends ActionCommand
{
    private final MainModel model;

    public TypeMappingsCommand( final CommandManager commandManager, final MainModel model )
    {
        super( commandManager, Commands.COMMANDID_EDITTYPES );
        this.model = model;
    }

    protected void handleExecute()
    {
        final TypeModel typeModel = model.getTypeModel();
        final TypeMapperBuilder builder = new TypeMapperBuilder( typeModel );
        final JComponent panel = builder.getPanel();
        final JDialog dialog = new I15nComponentDialog( model.getParent(), "typeedit" )
        {
            protected void onApply() throws ComponentDialogException
            {
            }

            protected void onCancel()
            {
            }

            protected JComponent createComponentPanel()
            {
                return panel;
            }
        };
        dialog.setVisible( true );
    }
}
