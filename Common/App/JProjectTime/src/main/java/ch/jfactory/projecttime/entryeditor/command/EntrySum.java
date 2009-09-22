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
package ch.jfactory.projecttime.entryeditor.command;

import ch.jfactory.lang.DateUtils;
import ch.jfactory.projecttime.domain.api.IFEntry;
import ch.jfactory.projecttime.entryeditor.builder.EntryModel;
import java.util.Calendar;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * TODO: document
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2005/09/04 19:54:10 $
 */
public class EntrySum extends ActionCommand {

    private EntryModel model;

    public EntrySum(final EntryModel model, final CommandManager manager) {
        super(manager, Commands.SUM_COMMAND);
        this.model = model;
    }

    protected void handleExecute() {
        final IFEntry entry = (IFEntry) model.getModel().getBean();
        model.setSum(DateUtils.dateDifference(sumAll(entry, 0), "HH:mm:ss"));
    }

    private long sumAll(IFEntry entry, long sum) {
        final IFEntry[] children = entry.getChildren();
        final Calendar start = entry.getStart();
        final Calendar end = entry.getEnd();
        if (start != null && end != null) {
            sum += (end.getTime().getTime() - start.getTime().getTime());
        }
        for (int i = 0; children !=  null && i < children.length; i++) {
            final IFEntry child = children[i];
            sum += sumAll(child, 0);
        }
        return sum;
    }
}
