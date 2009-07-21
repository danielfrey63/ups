/*
 * ====================================================================
 *  Copyright 2004-2006 www.xmatrix.ch
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.xmatrix.ups.model;

import ch.jfactory.binding.InfoModel;
import ch.jfactory.binding.Note;
import ch.jfactory.binding.SimpleNote;
import ch.jfactory.binding.DefaultInfoModel;
import ch.jfactory.model.SimpleModelList;
import ch.xmatrix.ups.controller.Loader;
import ch.xmatrix.ups.domain.SimpleLevel;
import ch.xmatrix.ups.domain.SimpleTaxon;
import com.thoughtworks.xstream.XStream;
import javax.swing.ListModel;
import org.apache.log4j.Logger;

/**
 * Holds taxon trees.
 *
 * @author Daniel Frey
 * @version $Revision: 1.8 $ $Date: 2008/01/06 10:16:20 $
 */
public class TaxonModels {

    // Todo: Remove redundant constant in ModelUtils
    public static final String RESOURCE_MODEL = "/data/taxa.xml";

    private static final Logger LOG = Logger.getLogger(TaxonModels.class);
    private static final boolean DEBUG = LOG.isDebugEnabled();

    private static SimpleModelList TREES;
    private static InfoModel INFO_MODEL = new DefaultInfoModel();

    public static TaxonTree[] getTaxonTreesArray() {
        final ListModel list = getTaxonTrees();
        final TaxonTree[] trees = new TaxonTree[list.getSize()];
        for (int i = 0; i < list.getSize(); i++) {
            trees[i] = (TaxonTree) list.getElementAt(i);
        }
        return trees;
    }

    /**
     * Sets the info model that will be used during model loading.
     *
     * @param infoModel the info model
     */
    public static void setInfoModel(final InfoModel infoModel) {
        INFO_MODEL = infoModel;
    }

    public static SimpleModelList getTaxonTrees() {
        loadTaxonTrees();
        return TREES;
    }

    public static void loadTaxonTrees() {
        if (TREES == null) {
            final Note note = INFO_MODEL.getNote();
            INFO_MODEL.setNote(new SimpleNote("Lade Taxonbäume", note.getPercentage() + 10, note.getColor()));
            final long start = System.currentTimeMillis();
            TREES = Loader.loadModel(RESOURCE_MODEL, "", getConverter());
            float diff = 0;
            final long end = System.currentTimeMillis();
            diff = (float) (end - start) / 1000;
            INFO_MODEL.setNote(new SimpleNote("Lade Taxonbäume in " + diff + "s"));
        }
    }

    public static XStream getConverter() {
        final XStream x = SimpleModelList.getConverter();
        x.setMode(XStream.ID_REFERENCES);
        x.alias("taxonTrees", SimpleModelList.class);
        x.alias("taxonTree", TaxonTree.class);
        x.alias("taxon", SimpleTaxon.class);
        x.alias("level", SimpleLevel.class);
        return x;
    }

    /**
     * Tries to find the taxon tree with the given uid and returns it, or null if it cannot be found.
     *
     * @param uid the uid of the taxon tree
     * @return the found taxon tree or null
     */
    public static TaxonTree find(final String uid) {
        final ListModel trees = getTaxonTrees();
        for (int i = 0; i < trees.getSize(); i++) {
            final TaxonTree tree = (TaxonTree) trees.getElementAt(i);
            if (tree.getUid().equals(uid)) {
                return tree;
            }
        }
        if (DEBUG && uid != null) LOG.debug("could not find taxon tree for uid \"" + uid + "\"");
        return null;
    }
}
