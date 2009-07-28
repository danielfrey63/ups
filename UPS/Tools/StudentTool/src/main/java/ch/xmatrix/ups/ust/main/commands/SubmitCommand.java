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

import ch.jfactory.application.presentation.WindowUtils;
import ch.jfactory.application.view.dialog.ListDialog;
import ch.xmatrix.ups.model.CourseModel;
import ch.xmatrix.ups.ust.main.MainModel;
import ch.xmatrix.ups.ust.main.PlantListSubmitDialog;
import ch.xmatrix.ups.view.CredentialsDialog;
import javax.swing.JFrame;
import javax.swing.ListModel;
import org.apache.log4j.Logger;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * Sets the submitting status of the model to true.
 *
 * @author Daniel Frey
 * @version $Revision: 1.8 $ $Date: 2008/01/06 10:16:19 $
 */
public class SubmitCommand extends ActionCommand {

    private static final Logger LOG = Logger.getLogger(SubmitCommand.class);

    private MainModel model;

    public SubmitCommand(final CommandManager commandManager, final MainModel model) {
        super(commandManager, Commands.COMMANDID_SUBMIT);
        this.model = model;
    }

    protected void handleExecute() {
        final ListModel courseInfoModels = MainModel.findModelById(MainModel.MODELID_COURSES);
        if (courseInfoModels != null) {
            final int size = courseInfoModels.getSize();
            if (size > 1) {
                final CourseModel[] courses = new CourseModel[size];
                for (int i = 0; i < size; i++) {
                    courses[i] = (CourseModel) courseInfoModels.getElementAt(i);
                }
                final ListDialog courseDialog = new ListDialog((JFrame) null, "course", courses);
                courseDialog.setSize(300, 600);
                WindowUtils.centerOnScreen(courseDialog);
                courseDialog.setVisible(true);

                if (courseDialog.isAccepted()) {
                    final CourseModel course = (CourseModel) courseDialog.getSelectedData()[0];
                    submitPlantlist(course);
                }
            }
            else if (size == 1) {
                submitPlantlist((CourseModel) courseInfoModels.getElementAt(0));
            }
        }
        else {
            LOG.fatal("no course models found");
        }
    }

    private void submitPlantlist(final CourseModel course) {
        final CredentialsDialog dialog = new PlantListSubmitDialog(null, model.getUserModel(), course.getUid());
        dialog.setSize(500, 300);
        WindowUtils.centerOnScreen(dialog);
        dialog.setVisible(true);
    }
}
