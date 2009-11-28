package com.ethz.geobot.herbar.modeapi.wizard;

import com.ethz.geobot.herbar.modeapi.HerbarContext;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeListener;
import javax.swing.Action;

/**
 * This interface descripe the model of the wizard.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:11 $
 */
public interface WizardModel
{
    /**
     * Return the herbar context of this mode.
     *
     * @return HerbarContext instance or null if the HerbarContext is unknown
     */
    HerbarContext getHerbarContext();

    /**
     * Return the enable state for the next button of the wizard.
     *
     * @return true = enabled, false = disabled
     */
    boolean isNextEnabled();

    /**
     * set the enable state for the next button of the wizard.
     *
     * @param isNextEnabled new state of the next button
     */
    void setNextEnabled( boolean isNextEnabled );

    /**
     * returns all WizardPanes of the model.
     *
     * @return an array of WizardPane objects.
     */
    WizardPane[] getPanes();

    /**
     * select a specified WizardPane. Attention this method also change the current position to the selected pane.
     *
     * @return the requested pane
     */
    WizardPane getPane( int index );

    /**
     * get the next WizardPane.
     *
     * @return next WizardPane
     */
    WizardPane getNextPane();

    /**
     * set the enable state for the finish button of the wizard.
     *
     * @param isFinishEnabled new state of the finish button
     */
    void setFinishEnabled( boolean isFinishEnabled );

    /**
     * Set the enable state for the cancel button of the wizard.
     *
     * @param isCancelEnabled new state of the finish button
     */
    void setCancelEnabled( boolean isCancelEnabled );

    /**
     * Return the enable state for the finish button of the wizard.
     *
     * @return true = enabled, false = disabled
     */
    boolean isFinishEnabled();

    /**
     * adds a state listener
     *
     * @param listener a listener listens to state changes
     */
    void addWizardStateListener( WizardStateListener listener );

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
    void removeWizardStateListener( WizardStateListener listener );

    /**
     * set the enable state for the next previous of the wizard.
     *
     * @param isPreviousEnabled new state of the previous button
     */
    void setPreviousEnabled( boolean isPreviousEnabled );

    /**
     * return the index of the current pane.
     *
     * @return index of the current pane
     */
    int getCurrentPaneIndex();

    /**
     * return the bean information of this model.
     *
     * @return BeanInfo object of this model
     * @throws IntrospectionException if the model could not be introspected
     */
    BeanInfo getBeanInfo() throws IntrospectionException;

    /**
     * add a property change listener
     *
     * @param listener the listener which will be notified
     */
    void addPropertyChangeListener( PropertyChangeListener listener );

    /**
     * add a property change listener for a specific property
     *
     * @param propertyName the name of the property
     * @param listener     the listener which will be notified
     */
    void addPropertyChangeListener( String propertyName, PropertyChangeListener listener );

    /**
     * remove a property change listener
     *
     * @param listener the listener which will be notified
     */
    void removePropertyChangeListener( PropertyChangeListener listener );

    /**
     * remove a property change listener for a specific property
     *
     * @param propertyName the name of the property
     * @param listener     the listener which will be notified
     */
    void removePropertyChangeListener( String propertyName, PropertyChangeListener listener );

    /**
     * is called inside the constructor before the panes are initialize
     */
    void init();

    /**
     * finish the wizard
     */
    void finishWizard();

    /**
     * register a finish action
     *
     * @param finishAction
     */
    void registerFinishAction( Action finishAction );

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
    void setStart( int currentPane );

    int getStart();

    String getName();
}
