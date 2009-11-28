package ch.jfactory.binding;

import java.beans.PropertyChangeListener;

/**
 * Model to set information messages in form of {@link Note}s.
 *
 * @author Daniel Frey
 * @version $Revision: 1.5 $ $Date: 2008/01/06 10:16:23 $
 */
public interface InfoModel
{
    static String PROPERTYNAME_NOTE = "note";

    void addPropertyChangeListener( PropertyChangeListener listener );

    void addPropertyChangeListener( String propertyName, PropertyChangeListener listener );

    void removePropertyChangeListener( PropertyChangeListener listener );

    void removePropertyChangeListener( String propertyName, PropertyChangeListener listener );

    Note getNote();

    void setNote( Note note );
}
