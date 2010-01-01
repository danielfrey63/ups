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

import ch.jfactory.binding.DefaultInfoModel;
import ch.jfactory.binding.InfoModel;
import ch.jfactory.binding.Note;
import ch.jfactory.binding.SimpleNote;
import ch.jfactory.convert.Converter;
import ch.jfactory.model.SimpleModelList;
import ch.jfactory.xstream.XStreamConverter;
import ch.xmatrix.ups.controller.Loader;
import ch.xmatrix.ups.domain.SimpleLevel;
import ch.xmatrix.ups.domain.SimpleTaxon;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractListModel;
import javax.swing.ListModel;
import org.apache.log4j.Logger;

/**
 * Holds taxon trees.
 *
 * @author Daniel Frey
 * @version $Revision: 1.8 $ $Date: 2008/01/06 10:16:20 $
 */
public class TaxonModels
{
    // Todo: Remove redundant constant in ModelUtils

    public static final String RESOURCE_MODEL = "/data/taxa.xml";

    private static final Logger LOG = Logger.getLogger( TaxonModels.class );

    private static final boolean DEBUG = LOG.isDebugEnabled();

    private static SimpleModelList TREES;

    private static InfoModel INFO_MODEL = new DefaultInfoModel();

    public static TaxonTree[] getTaxonTreesArray()
    {
        final ListModel list = getTaxonTrees();
        final TaxonTree[] trees = new TaxonTree[list.getSize()];
        for ( int i = 0; i < list.getSize(); i++ )
        {
            trees[i] = (TaxonTree) list.getElementAt( i );
        }
        return trees;
    }

    /**
     * Sets the info model that will be used during model loading.
     *
     * @param infoModel the info model
     */
    public static void setInfoModel( final InfoModel infoModel )
    {
        INFO_MODEL = infoModel;
    }

    public static SimpleModelList getTaxonTrees()
    {
        loadTaxonTrees();
        return TREES;
    }

    public static void loadTaxonTrees()
    {
        if ( TREES == null )
        {
            final Note note = INFO_MODEL.getNote();
            INFO_MODEL.setNote( new SimpleNote( "Lade Taxonbäume", note.getPercentage() + 10, note.getColor() ) );
            final long start = System.currentTimeMillis();
            TREES = Loader.loadModel( RESOURCE_MODEL, "", getConverter() );
            final long end = System.currentTimeMillis();
            final float diff = (float) ( end - start ) / 1000;
            INFO_MODEL.setNote( new SimpleNote( "Lade Taxonbäume in " + diff + "s" ) );
        }
    }

    public static Converter getConverter()
    {
        final Map<String, Class> aliases = new HashMap<String, Class>();
        aliases.put( "taxonTrees", SimpleModelList.class );
        aliases.put( "taxonTree", TaxonTree.class );
        aliases.put( "taxon", SimpleTaxon.class );
        aliases.put( "level", SimpleLevel.class );

        final Map<Class, String> implicitCollections = new HashMap<Class, String>();
        implicitCollections.put( SimpleModelList.class, "models" );

        final Map<Class, String> omits = new HashMap<Class, String>();
        omits.put( AbstractListModel.class, "listenerList" );

        return new XStreamConverter<TaxonModels>( aliases, implicitCollections, omits );
    }

    /**
     * Tries to find the taxon tree with the given uid and returns it, or null if it cannot be found.
     *
     * @param uid the uid of the taxon tree
     * @return the found taxon tree or null
     */
    public static TaxonTree find( final String uid )
    {
        final ListModel trees = getTaxonTrees();
        for ( int i = 0; i < trees.getSize(); i++ )
        {
            final TaxonTree tree = (TaxonTree) trees.getElementAt( i );
            if ( tree.getUid().equals( uid ) )
            {
                return tree;
            }
        }
        if ( DEBUG && uid != null )
        {
            LOG.debug( "could not find taxon tree for uid \"" + uid + "\"" );
        }
        return null;
    }
}
