package ch.jfactory.update;

import java.util.EventObject;

/**
 * event object for UpdateChangeListener
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2007/09/27 10:41:22 $
 */
public class UpdateChangeEvent extends EventObject
{

    /** Holds value of property StepsCount. */
    private int stepsCount;

    /** Holds value of property currentStep. */
    private int currentStep;

    /** Holds value of property stepDescription. */
    private String stepDescription;

    public UpdateChangeEvent(final Object source)
    {
        super(source);
    }

    /**
     * Getter for property steps.
     *
     * @return Value of property steps.
     */
    public int getStepsCount()
    {
        return this.stepsCount;
    }

    /**
     * Setter for property steps.
     *
     * @param stepsCount New value of property steps.
     */
    void setStepsCount(final int stepsCount)
    {
        this.stepsCount = stepsCount;
    }

    /**
     * Getter for property currentStep.
     *
     * @return Value of property currentStep.
     */
    public int getCurrentStep()
    {
        return this.currentStep;
    }

    /**
     * Setter for property currentStep.
     *
     * @param currentStep New value of property currentStep.
     */
    void setCurrentStep(final int currentStep)
    {
        this.currentStep = currentStep;
    }

    /**
     * Getter for property stepDescription.
     *
     * @return Value of property stepDescription.
     */
    public String getStepDescription()
    {
        return this.stepDescription;
    }

    /**
     * Setter for property stepDescription.
     *
     * @param stepDescription New value of property stepDescription.
     */
    void setStepDescription(final String stepDescription)
    {
        this.stepDescription = stepDescription;
    }

    public String toString()
    {
        final StringBuffer string = new StringBuffer(super.toString());
        string.insert(string.length() - 1, ", stepsCount=" + stepsCount);
        string.insert(string.length() - 1, ", currentStep=" + currentStep);
        string.insert(string.length() - 1, ", stepDescription=" + stepDescription);
        return string.toString();
    }
}