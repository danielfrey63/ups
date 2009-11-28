package ch.jfactory.event;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2006/03/22 15:05:10 $
 */
public interface VetoableComboBoxSelectionListener extends java.util.EventListener
{
    boolean selectionChanged( VetoableChangeEvent e );
}