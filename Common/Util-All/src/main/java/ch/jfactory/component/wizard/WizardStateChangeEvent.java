package ch.jfactory.component.wizard;

import java.util.EventObject;

/**
 * This is the event object which is fired on WizardState changes.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2006/03/14 21:27:56 $
 */
public class WizardStateChangeEvent extends EventObject
{
    private final boolean hasNext;

    private final boolean hasPrevious;

    private final boolean isNextEnabled;

    private final boolean isPreviousEnabled;

    private final boolean isFinishEnabled;

    private final boolean isCancelEnabled;

    public WizardStateChangeEvent( final Object source, final boolean hasNext, final boolean hasPrevious, final boolean
            isNextEnabled, final boolean isPreviousEnabled, final boolean isFinishEnabled, final boolean isCancelEnabled )
    {
        super( source );
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
        this.isNextEnabled = isNextEnabled;
        this.isPreviousEnabled = isPreviousEnabled;
        this.isFinishEnabled = isFinishEnabled;
        this.isCancelEnabled = isCancelEnabled;
    }

    public boolean isNextEnabled()
    {
        return isNextEnabled;
    }

    public boolean isPreviousEnabled()
    {
        return isPreviousEnabled;
    }

    public boolean isFinishEnabled()
    {
        return isFinishEnabled;
    }

    public boolean isCancelEnabled()
    {
        return isCancelEnabled;
    }

    public boolean hasNext()
    {
        return hasNext;
    }

    public boolean hasPrevious()
    {
        return hasPrevious;
    }

    public String toString()
    {
        final StringBuffer textBuffer = new StringBuffer( 150 );
        textBuffer.append( "WizardStateChangeEvent: [" );
        textBuffer.append( " hasNext = " ).append( hasNext() );
        textBuffer.append( " hasPrevious = " ).append( hasPrevious() );
        textBuffer.append( " isNextEnabled = " ).append( isNextEnabled() );
        textBuffer.append( " isPreviousEnabled = " ).append( isPreviousEnabled() );
        textBuffer.append( " isFinishEnabled = " ).append( isFinishEnabled() );
        textBuffer.append( "]" );

        return textBuffer.toString();
    }
}
