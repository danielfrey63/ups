package ch.jfactory.logging;

import ch.jfactory.application.SystemUtil;
import java.util.ArrayList;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.TTCCLayout;
import org.apache.log4j.spi.LoggingEvent;

/**
 * MemoryAppender appends log events to a ArrayList.
 *
 * @author Thomas Wegmueller
 */
public class MemoryAppender extends AppenderSkeleton
{
    private ArrayList<LoggingEvent> list;

    private int pointer = 0;

    private boolean filled = false;

    protected int listSize = 100;

    private Level _abortLevel = Level.ERROR;

    private Level _messageLevel = Level.ERROR;

    private Level _logLevel = Level.DEBUG;

    protected String abortLevel = Level.ERROR.toString();

    protected String messageLevel = Level.ERROR.toString();

    protected String logLevel = Level.DEBUG.toString();

    /** The default constructor constructs a ArrayList */
    public MemoryAppender()
    {
        list = new ArrayList<LoggingEvent>(listSize);
        setLayout(new TTCCLayout());
    }

    public void close()
    {
    }

    public void activateOptions()
    {
        setAbortLevel(abortLevel);
        setMessageLevel(messageLevel);
        setLogLevel(logLevel);
    }

    public boolean requiresLayout()
    {
        return false;
    }

    public String dump()
    {
        int start = 0;
        if (filled)
        {
            start = pointer + 1;
        }
        final StringBuffer sb = new StringBuffer();
        while (start != (pointer))
        {
            final LoggingEvent e = list.get(start);
            sb.append(getLayout().format(e));
            final String[] t = e.getThrowableStrRep();
            if (t != null)
            {
                for (final String aT : t)
                {
                    sb.append(aT).append("\n");
                }
            }
            start++;
            start %= listSize;
        }
        return sb.toString();
    }

    public void append(final LoggingEvent e)
    {
        if (e.getLevel().isGreaterOrEqual(_logLevel))
        {
            appendToList(e);
        }
        if (e.getLevel().isGreaterOrEqual(_messageLevel))
        {
            System.out.println(dump());
            final SendFeedbackDialog dialog = new SendFeedbackDialog();
            dialog.setDump(dump());
            dialog.open();
        }
        if (e.getLevel().isGreaterOrEqual(_abortLevel))
        {
            SystemUtil.EXIT.exit(1);
        }
    }

    private void appendToList(final LoggingEvent e)
    {
        if (pointer >= listSize)
        {
            pointer = 0;
            filled = true;
        }
        if (filled)
        {
            list.set(pointer, e);
        }
        else
        {
            list.add(e);
        }
        pointer++;
    }

    /**
     * Returns the abortLevel.
     *
     * @return Level
     */
    public Level getAbortLevel()
    {
        return _abortLevel;
    }

    /**
     * Sets the abortLevel.
     *
     * @param ap The abortLevel to set
     */
    public void setAbortLevel(final String ap)
    {
        setAbortLevel(Level.toLevel(ap, _abortLevel));
    }

    public void setAbortLevel(final Level ap)
    {
        _abortLevel = ap;
        abortLevel = _abortLevel.toString();
    }

    /**
     * Returns the logLevel.
     *
     * @return Level
     */
    public Level getLogLevel()
    {
        return _logLevel;
    }

    /**
     * Returns the messageLevel.
     *
     * @return Level
     */
    public Level getMessageLevel()
    {
        return _messageLevel;
    }

    /**
     * Sets the logLevel.
     *
     * @param lp The logLevel to set
     */
    public void setLogLevel(final String lp)
    {
        setLogLevel(Level.toLevel(lp, _logLevel));
    }

    public void setLogLevel(final Level lp)
    {
        _logLevel = lp;
        logLevel = _logLevel.toString();
    }

    /**
     * Sets the messageLevel.
     *
     * @param mp The messageLevel to set
     */
    public void setMessageLevel(final String mp)
    {
        setMessageLevel(Level.toLevel(mp, _messageLevel));
    }

    public void setMessageLevel(final Level mp)
    {
        _messageLevel = mp;
        messageLevel = _messageLevel.toString();
    }

    /**
     * Returns the listSize.
     *
     * @return int
     */
    public int getListSize()
    {
        return listSize;
    }

    /**
     * Sets the listSize.
     *
     * @param listSize The listSize to set
     */
    public void setListSize(final int listSize)
    {
        this.listSize = listSize;
        list.ensureCapacity(listSize);
    }
}
