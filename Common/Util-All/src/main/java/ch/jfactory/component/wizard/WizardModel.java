package ch.jfactory.component.wizard;

import java.util.Properties;

/**
 * This interface describes the model of the wizard.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2006/03/14 21:27:56 $
 */
public interface WizardModel {

    /**
     * Return the configuration of this mode.
     *
     * @return Properties instance or null if the Properties are unknown
     */
    Properties getConfig();

    /**
     * Return the enable state for the next button of the wizard.
     *
     * @return true = enabled, false = disabled
     */
    boolean isNextEnabled();

    /**
     * Set the enable state for the next button of the wizard.
     *
     * @param isNextEnabled new state of the next button
     */
    void setNextEnabled(boolean isNextEnabled);

    /**
     * Returns all WizardPanes of the model.
     *
     * @return an array of WizardPane objects.
     */
    WizardPane[] getPanes();

    /**
     * Select a specified WizardPane. Attention this method also change the current position to the selected pane.
     *
     * @return the requested pane
     */
    WizardPane getPane(int index);

    /**
     * Get the next WizardPane.
     *
     * @return next WizardPane
     */
    WizardPane getNextPane();

    /**
     * Set the enable state for the finish button of the wizard.
     *
     * @param isFinishEnabled new state of the finish button
     */
    void setFinishEnabled(boolean isFinishEnabled);

    /**
     * Set the enable state for the cancel button of the wizard.
     *
     * @param isCancelEnabled new state of the finish button
     */
    void setCancelEnabled(boolean isCancelEnabled);

    /**
     * Return the enable state for the finish button of the wizard.
     *
     * @return true = enabled, false = disabled
     */
    boolean isFinishEnabled();

    /**
     * Adds a state listener
     *
     * @param listener a listener listens to state changes
     */
    void addWizardStateListener(WizardStateListener listener);

    /**
     * Informs the wizard about the present of a next pane.
     *
     * @return true = next pane available, false = no next pane
     */
    boolean hasNext();

    /**
     * Return the enable state for the previous button of the wizard.
     *
     * @return true = enabled, false = disabled
     */
    boolean isPreviousEnabled();

    /**
     * Return the enable state for the cancel button of the wizard.
     *
     * @return true = enabled, false = disabled
     */
    boolean isCancelEnabled();

    /**
     * get the previous WizardPane.
     *
     * @return previous WizardPane
     */
    WizardPane getPreviousPane();

    /**
     * Informs the wizard about the present of a previous pane.
     *
     * @return true = previous pane available, false = no previous pane
     */
    boolean hasPrevious();

    /**
     * removes a state listener
     *
     * @param listener listener to remove from the list
     */
    void removeWizardStateListener(WizardStateListener listener);

    /**
     * set the enable state for the next previous of the wizard.
     *
     * @param isPreviousEnabled new state of the previous button
     */
    void setPreviousEnabled(boolean isPreviousEnabled);

    /**
     * return the index of the current pane.
     *
     * @return index of the current pane
     */
    int getCurrentPaneIndex();

    /**
     * is called inside the constructor before the panes are initialize
     */
    void init();

    /**
     * return the  dialog title
     *
     * @return the dialog title
     */
    String getDialogTitle();

    /**
     * Sets the starting pane.
     *
     * @param currentPane pane index to set
     */
    void setStart(int currentPane);

    int getStart();

    String getName();
}
