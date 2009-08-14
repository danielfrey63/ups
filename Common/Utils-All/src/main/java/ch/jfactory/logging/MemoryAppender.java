package ch.jfactory.logging;

import ch.jfactory.application.SystemUtil;
import java.util.ArrayList;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Priority;
import org.apache.log4j.TTCCLayout;
import org.apache.log4j.spi.LoggingEvent;

/**
 * MemoryAppender appends log events to a ArrayList.
 *
 * @author Thomas Wegmueller
 */
public class MemoryAppender extends AppenderSkeleton {
    private ArrayList list;
    private int pointer = 0;
    private boolean filled = false;
    protected int listSize = 100;
    private Priority _abortPriority = Priority.ERROR;
    private Priority _messagePriority = Priority.ERROR;
    private Priority _logPriority = Priority.DEBUG;
    protected String abortPriority = Priority.ERROR.toString();
    protected String messagePriority = Priority.ERROR.toString();
    protected String logPriority = Priority.DEBUG.toString();

    /**
     * The default constructor constructs a ArrayList
     */
    public MemoryAppender() {
        list = new ArrayList(listSize);
        setLayout(new TTCCLayout());
    }

    public void close() {
    }

    public void activateOptions() {
        setAbortPriority(abortPriority);
        setMessagePriority(messagePriority);
        setLogPriority(logPriority);
    }

    public boolean requiresLayout() {
        return false;
    }

    public String dump() {
        int start = 0;
        if (filled) start = pointer + 1;
        final StringBuffer sb = new StringBuffer();
        while (start != (pointer)) {
            final LoggingEvent e = (LoggingEvent) list.get(start);
            sb.append(getLayout().format(e));
            final String[] t = e.getThrowableStrRep();
            if (t != null) {
                for (int i = 0; i < t.length; i++) {
                    sb.append(t[i] + "\n");
                }
            }
            start++;
            start %= listSize;
        }
        return sb.toString();
    }

    public void append(final LoggingEvent e) {
        if (e.getLevel().isGreaterOrEqual(_logPriority)) {
            appendToList(e);
        }
        if (e.getLevel().isGreaterOrEqual(_messagePriority)) {
            System.out.println(dump());
            final SendFeedbackDialog dialog = new SendFeedbackDialog();
            dialog.setDump(dump());
            dialog.open();
        }
        if (e.getLevel().isGreaterOrEqual(_abortPriority)) {
            SystemUtil.EXIT.exit(1);
        }
    }

    private void appendToList(final LoggingEvent e) {
        if (pointer >= listSize) {
            pointer = 0;
            filled = true;
        }
        if (filled) {
            list.set(pointer, e);
        }
        else {
            list.add(e);
        }
        pointer++;
    }

    /**
     * Returns the abortPriority.
     *
     * @return Priority
     */
    public Priority getAbortPriority() {
        return _abortPriority;
    }

    /**
     * Sets the abortPriority.
     *
     * @param ap The abortPriority to set
     */
    public void setAbortPriority(final String ap) {
        setAbortPriority(Priority.toPriority(ap, _abortPriority));
    }

    public void setAbortPriority(final Priority ap) {
        _abortPriority = ap;
        abortPriority = _abortPriority.toString();
    }

    /**
     * Returns the logPriority.
     *
     * @return Priority
     */
    public Priority getLogPriority() {
        return _logPriority;
    }

    /**
     * Returns the messagePriority.
     *
     * @return Priority
     */
    public Priority getMessagePriority() {
        return _messagePriority;
    }

    /**
     * Sets the logPriority.
     *
     * @param lp The logPriority to set
     */
    public void setLogPriority(final String lp) {
        setLogPriority(Priority.toPriority(lp, _logPriority));
    }

    public void setLogPriority(final Priority lp) {
        _logPriority = lp;
        logPriority = _logPriority.toString();
    }

    /**
     * Sets the messagePriority.
     *
     * @param mp The messagePriority to set
     */
    public void setMessagePriority(final String mp) {
        setMessagePriority(Priority.toPriority(mp, _messagePriority));
    }

    public void setMessagePriority(final Priority mp) {
        _messagePriority = mp;
        messagePriority = _messagePriority.toString();
    }

    /**
     * Returns the listSize.
     *
     * @return int
     */
    public int getListSize() {
        return listSize;
    }

    /**
     * Sets the listSize.
     *
     * @param listSize The listSize to set
     */
    public void setListSize(final int listSize) {
        this.listSize = listSize;
        list.ensureCapacity(listSize);
    }
}
