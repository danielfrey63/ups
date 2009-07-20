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
package ch.jfactory.application.presentation;

import ch.jfactory.application.persistence.IFBusinessDelegate;
import ch.jfactory.application.persistence.SourceStateEvent;
import ch.jfactory.application.persistence.SourceStateListener;
import ch.jfactory.application.persistence.SourceVetoedException;
import ch.jfactory.component.Dialogs;
import java.util.Properties;
import javax.swing.Action;
import javax.swing.JFrame;

/**
 * Ajusts the title of a frame according to the save state and dis-/enables the save action. Make sure the frame has its
 * title set when this object is build. The title will be completed by the source name and an asterisk if the source has
 * not been flushed.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
public class SourceManager implements SourceStateListener {

    /**
     * The action to disable or enable.
     */
    private Action saveAction;

    /**
     * The frame to ajust the title.
     */
    private JFrame frame;

    /**
     * The original title of the frame.
     */
    private String title;

    /**
     * Keeps the last state for evaluation when asking for loosing data.
     */
    private SourceStateEvent.SourceStateEventType state;

    /**
     * Constructs a file manager object.
     *
     * @param frame      the frame to ajust the title for.
     * @param saveAction the save command to dis- or enable.
     */
    public SourceManager(final JFrame frame, final Action saveAction) {
        this.frame = frame;
        this.saveAction = saveAction;
        this.title = frame.getTitle();

        saveAction.setEnabled(false);
    }

    public void sourceStateMayChange(final SourceStateEvent e) throws SourceVetoedException {
        checkForLostData(e);
    }

    public void sourceStateChanged(final SourceStateEvent e) {
        ajustTitle(e);
        ajustAction(e);
        state = e.getType();
    }

    private void checkForLostData(final SourceStateEvent e) throws SourceVetoedException {
        boolean okToFire = true;
        if (state == SourceStateEvent.DIRTY) {
            final String question = "Sie haben noch ungespeicherte Eingaben. Wollen Sie diese wirklich verwerfen?";
            final int res = Dialogs.showQuestionMessageOk(frame.getRootPane(), "Beenden", question);
            okToFire = (res == Dialogs.OK);
        }
        if (!okToFire) {
            throw new SourceVetoedException();
        }
    }

    private void ajustAction(final SourceStateEvent e) {
        final SourceStateEvent.SourceStateEventType type = e.getType();
        saveAction.setEnabled(type == SourceStateEvent.DIRTY);
    }

    /**
     * Builds a title out of the original title and the source name. If the source is dirty, an asterisk is appended.
     */
    private void ajustTitle(final SourceStateEvent e) {
        final IFBusinessDelegate delegate = (IFBusinessDelegate) e.getSource();
        final Properties props = delegate.getProperties();

        final StringBuffer buffer = new StringBuffer(title);
        buffer.append(" :: ");
        buffer.append(props.getProperty(IFBusinessDelegate.PROPERTY_SOURCE_NAME));
        buffer.append(e.getType() == SourceStateEvent.DIRTY ? " *" : "");

        System.out.println(e.getType() + " " + buffer.toString());

        frame.setTitle(buffer.toString());
    }
}