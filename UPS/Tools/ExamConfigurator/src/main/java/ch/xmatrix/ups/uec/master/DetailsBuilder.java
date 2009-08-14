package ch.xmatrix.ups.uec.master;

import ch.jfactory.application.view.builder.Builder;
import ch.xmatrix.ups.domain.TaxonBased;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2006/04/17 23:29:42 $
 */
public interface DetailsBuilder extends Builder
{

    /**
     * Apply the new model to your editor.
     *
     * @param taxonBased the new model
     */
    void setModel(TaxonBased taxonBased);

    /**
     * The details builder is called to enable the details when a selection in the master details builder changes. Make
     * sure to ajust enabled-state of all detail components as needed taking the state passed into account.
     *
     * @param enabled whether the details should be enabled or not.
     */
    void setEnabled(boolean enabled);

    void load();

    /** Save the data. */
    void save();

    /** Locks the gui and sets the models fixed state. */
    void lock();

    boolean handleMigration(TaxonBased model, String uid);

    void registerDirtyListener(DirtyListener dirtyListener);
}
