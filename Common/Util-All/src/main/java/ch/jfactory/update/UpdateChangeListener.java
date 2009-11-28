/* Generated by Together */

package ch.jfactory.update;

public interface UpdateChangeListener extends java.util.EventListener
{
    /**
     * this method is invoke if the UpdateProcessor begins update.
     *
     * @param event information aboud the update state
     */
    void beginUpdate( UpdateChangeEvent event );

    /**
     * this method is invoke if the UpdateProcessor begins update.
     *
     * @param event information aboud the update state
     */
    void progressUpdate( UpdateChangeEvent event );

    /**
     * this method is invoke if the UpdateProcessor finish update.
     *
     * @param event information aboud the update state
     */
    void finishUpdate( UpdateChangeEvent event );
    /** @link dependency */
    /*# UpdateChangeEvent lnkUpdateChangeEvent; */
}
