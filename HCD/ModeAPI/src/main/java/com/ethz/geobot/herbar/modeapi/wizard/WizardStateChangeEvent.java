package com.ethz.geobot.herbar.modeapi.wizard;

import java.util.EventObject;

/**
 * This is the event object which is fired on WizardState changes.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:11 $
 */
public class WizardStateChangeEvent extends EventObject {

    private boolean hasNext;
    private boolean hasPrevious;
    private boolean isNextEnabled;
    private boolean isPreviousEnabled;
    private boolean isFinishEnabled;
    private boolean isCancelEnabled;

    public WizardStateChangeEvent(Object source, boolean hasNext, boolean hasPrevious, boolean
            isNextEnabled, boolean isPreviousEnabled, boolean isFinishEnabled, boolean isCancelEnabled) {
        super(source);
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
        this.isNextEnabled = isNextEnabled;
        this.isPreviousEnabled = isPreviousEnabled;
        this.isFinishEnabled = isFinishEnabled;
        this.isCancelEnabled = isCancelEnabled;
    }

    public boolean isNextEnabled() {
        return isNextEnabled;
    }

    public boolean isPreviousEnabled() {
        return isPreviousEnabled;
    }

    public boolean isFinishEnabled() {
        return isFinishEnabled;
    }

    public boolean isCancelEnabled() {
        return isCancelEnabled;
    }

    public boolean hasNext() {
        return hasNext;
    }

    public boolean hasPrevious() {
        return hasPrevious;
    }

    public String toString() {
        StringBuffer textBuffer = new StringBuffer(150);
        textBuffer.append("WizardStateChangeEvent: [");
        textBuffer.append(" hasNext = " + hasNext());
        textBuffer.append(" hasPrevious = " + hasPrevious());
        textBuffer.append(" isNextEnabled = " + isNextEnabled());
        textBuffer.append(" isPreviousEnabled = " + isPreviousEnabled());
        textBuffer.append(" isFinishEnabled = " + isFinishEnabled());
        textBuffer.append("]");

        return textBuffer.toString();
    }
}
