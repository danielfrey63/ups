package ch.jfactory.component.wizard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * This WizardModel is used to has two models one is a static model and the cascade one which is dependend from the
 * first one. The User should extends this class and use the setCascadeWizardModel method to set the cascaded
 * WizardModel.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2006/03/14 21:27:56 $
 */
abstract public class CascadeWizardModel extends DefaultWizardModel implements WizardStateListener {

    private static final String NAME = "CascadeWizardModel";

    private WizardModel cascadeWizardModel = new DefaultWizardModel(new Properties(), new WizardPane[0], NAME);

    private boolean inCascadeWizard = false;

    public CascadeWizardModel(final Properties properties, final WizardPane[] panes) {
        super(properties, panes, NAME);
    }

    public void setNextEnabled(final boolean isNextEnabled) {
        if (inCascadeWizard()) {
            cascadeWizardModel.setNextEnabled(isNextEnabled);
        }
        else {
            super.setNextEnabled(isNextEnabled);
        }
    }

    public void setFinishEnabled(final boolean isFinishEnabled) {
        if (inCascadeWizard()) {
            cascadeWizardModel.setFinishEnabled(isFinishEnabled);
        }
        else {
            super.setFinishEnabled(isFinishEnabled);
        }
    }

    public void setPreviousEnabled(final boolean isPreviousEnabled) {
        if (inCascadeWizard()) {
            cascadeWizardModel.setPreviousEnabled(isPreviousEnabled);
        }
        else {
            super.setPreviousEnabled(isPreviousEnabled);
        }
    }

    public void setCancelEnabled(final boolean isCancelEnabled) {
        if (inCascadeWizard()) {
            cascadeWizardModel.setCancelEnabled(isCancelEnabled);
        }
        else {
            super.setCancelEnabled(isCancelEnabled);
        }
    }

    public void setCascadeWizardModel(final WizardModel newCascadeModel) {
        if (cascadeWizardModel != null) {
            cascadeWizardModel.removeWizardStateListener(this);
        }
        cascadeWizardModel = newCascadeModel;
        cascadeWizardModel.addWizardStateListener(this);
        fireInternalState();
    }

    public boolean isNextEnabled() {
        if (inCascadeWizard()) {
            return cascadeWizardModel.isNextEnabled();
        }
        else {
            return super.isNextEnabled();
        }
    }

    public WizardPane[] getPanes() {
        final List list = new ArrayList();
        list.addAll(Arrays.asList(super.getPanes()));
        list.addAll(Arrays.asList(cascadeWizardModel.getPanes()));
        return (WizardPane[]) list.toArray(new WizardPane[0]);
    }

    public WizardPane getPane() {
        final int index = super.getCurrentPaneIndex();
        final int panesCount = super.getPanes().length;
        if (index > panesCount) {
            return cascadeWizardModel.getPane(index - panesCount);
        }
        else {
            return super.getPane(index);
        }
    }

    public WizardPane getNextPane() {
        final int currentPaneIndex = getCurrentPaneIndex();
        final int staticPaneCount = super.getPanes().length;
        WizardPane pane = null;

        if (currentPaneIndex == (staticPaneCount - 1)) {
            // wizard will switch to mode wizard
            if (cascadeWizardModel.getPanes().length > 0) {
                inCascadeWizard = true;
                pane = cascadeWizardModel.getPane(0);
            }
        }
        else if (currentPaneIndex > (staticPaneCount - 1)) {
            pane = cascadeWizardModel.getNextPane();
        }
        else {
            pane = super.getNextPane();
        }

        return pane;
    }

    public boolean isFinishEnabled() {
        if (inCascadeWizard()) {
            return cascadeWizardModel.isFinishEnabled();
        }
        else {
            return super.isFinishEnabled();
        }
    }

    public boolean isCancelEnabled() {
        if (inCascadeWizard()) {
            return cascadeWizardModel.isCancelEnabled();
        }
        else {
            return super.isCancelEnabled();
        }
    }

    public boolean isPreviousEnabled() {
        if (inCascadeWizard()) {
            return cascadeWizardModel.isPreviousEnabled();
        }
        else {
            return super.isPreviousEnabled();
        }
    }

    public WizardPane getPreviousPane() {
        WizardPane pane = null;

        if (inCascadeWizard()) {
            if (cascadeWizardModel.getCurrentPaneIndex() > 0) {
                pane = cascadeWizardModel.getPreviousPane();
            }
            else {
                inCascadeWizard = false;
                pane = super.getPane(super.getCurrentPaneIndex());
            }
        }
        else {
            pane = super.getPreviousPane();
        }
        return pane;
    }

    public int getCurrentPaneIndex() {
        if (inCascadeWizard()) {
            final int panesCount = super.getPanes().length;
            return cascadeWizardModel.getCurrentPaneIndex() + panesCount;
        }
        else {
            return super.getCurrentPaneIndex();
        }
    }

    public boolean hasNext() {
        final int currentPaneIndex = getCurrentPaneIndex();
        final int staticPaneCount = super.getPanes().length;
        boolean hasNext = false;

        if (currentPaneIndex == (staticPaneCount - 1)) {
            // wizard will switch to mode wizard
            if (cascadeWizardModel != null && cascadeWizardModel.getPanes().length > 0) {
                hasNext = true;
            }
        }
        else if (cascadeWizardModel != null && currentPaneIndex > (staticPaneCount - 1)) {
            hasNext = cascadeWizardModel.hasNext();
        }
        else {
            hasNext = super.hasNext();
        }

        return hasNext;
    }

    public boolean hasPrevious() {
        if (inCascadeWizard()) {
            // there is every time a previous pane
            return true;
        }
        else {
            return super.hasPrevious();
        }
    }

    public void change(final WizardStateChangeEvent event) {
        // delegate event, but enable previous button
        final WizardStateChangeEvent translated = new WizardStateChangeEvent(event.getSource(), event.hasNext(), true,
                event.isNextEnabled(), event.isPreviousEnabled(), event.isFinishEnabled(), event.isCancelEnabled());

        super.fireChange(translated);
    }

    private boolean inCascadeWizard() {
        return inCascadeWizard;
    }
}
