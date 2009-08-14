package ch.jfactory.collection.cursor;

/**
 * Enhance the cursor interface to support notifiable cursor.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $
 */
public interface NotifiableCursor extends Cursor
{
    void addCursorChangeListener(CursorChangeListener listener);

    void removeCursorChangeListener(CursorChangeListener listener);

    /**
     * @link
     * @shapeType PatternLink
     * @pattern Observer
     * @supplierRole Concrete subjects
     */
    /*# private DefaultNotifiableCursor _concreteSubject; */

    /**
     * @link
     * @shapeType PatternLink
     * @pattern <{com.ethz.geobot.herbar.util.cursor.CursorChangeListener}>
     * @supplierRole <{com.ethz.geobot.herbar.util.cursor.CursorChangeListener}>
     * @hidden
     */
    /*# private CursorChangeListener _observer; */
}
