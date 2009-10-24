package com.ethz.geobot.herbar.model.graph;

import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.TypeFactory;
import com.ethz.geobot.herbar.model.db.impl.MutableEcologyAttributeImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableEcologyImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableEcologySubjectImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableEcologyTextImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableEcologyValueImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableMedicineAttributeImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableMedicineImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableMedicineSubjectImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableMedicineTextImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableMedicineValueImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableMorphologyAttributeImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableMorphologyImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableMorphologySubjectImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableMorphologyValueImpl;
import com.ethz.geobot.herbar.model.db.impl.MutablePictureImpl;
import com.ethz.geobot.herbar.model.db.impl.MutablePictureTextImpl;
import com.ethz.geobot.herbar.model.db.impl.MutablePictureThemeImpl;
import com.ethz.geobot.herbar.model.db.impl.MutablePicturesImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableRootImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableTaxonImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableTaxonLevelImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableTaxonSynonymImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableTextImpl;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.log4j.Category;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:20 $
 */
public class TypeFactoryImpl implements TypeFactory {

    private static final Category cat = Category.getInstance(TypeFactoryImpl.class);

    /**
     * Used for the TYPE field in the db.
     */
    private static HashMap typeMapping = new HashMap();
    private static HashMap implMapping = new HashMap();

    static {
        try {
            typeMapping.put("ROOT", MutableRootImpl.class);
            typeMapping.put("TAXON", MutableTaxonImpl.class);
            typeMapping.put("TAXONLEVEL", MutableTaxonLevelImpl.class);
            typeMapping.put("TAXONSYNONYM", MutableTaxonSynonymImpl.class);

            typeMapping.put("MORPHOLOGY", MutableMorphologyImpl.class);
            typeMapping.put("MORSUBJECT", MutableMorphologySubjectImpl.class);
            typeMapping.put("MORATTRIBUTE", MutableMorphologyAttributeImpl.class);
            typeMapping.put("MORVALUE", MutableMorphologyValueImpl.class);
            typeMapping.put("MORTEXT", MutableTextImpl.class);

            typeMapping.put("MEDICINE", MutableMedicineImpl.class);
            typeMapping.put("MEDSUBJECT", MutableMedicineSubjectImpl.class);
            typeMapping.put("MEDATTRIBUTE", MutableMedicineAttributeImpl.class);
            typeMapping.put("MEDVALUE", MutableMedicineValueImpl.class);
            typeMapping.put("MEDTEXT", MutableMedicineTextImpl.class);

            typeMapping.put("ECOLOGY", MutableEcologyImpl.class);
            typeMapping.put("ECOSUBJECT", MutableEcologySubjectImpl.class);
            typeMapping.put("ECOATTRIBUTE", MutableEcologyAttributeImpl.class);
            typeMapping.put("ECOVALUE", MutableEcologyValueImpl.class);
            typeMapping.put("ECOTEXT", MutableEcologyTextImpl.class);

            typeMapping.put("PICTURE", MutablePictureImpl.class);
            typeMapping.put("PICTURES", MutablePicturesImpl.class);
            typeMapping.put("PICTURETEXT", MutablePictureTextImpl.class);
            typeMapping.put("PICTURETHEME", MutablePictureThemeImpl.class);
        }
        catch (Exception e) {
            cat.fatal("Fatal error during initialization of model", e);
        }
    }

    public GraphNode getInstance(String type) {
        Class clazz = (Class) typeMapping.get(type);
        if (clazz == null) {
            throw new IllegalStateException("Class for type " + type + " not found");
        }
        return getInstance(clazz);
    }

    /**
     * @see ch.jfactory.model.graph.TypeFactory#getInstance(Class)
     */
    public GraphNode getInstance(Class type) {
        GraphNode node = null;
        try {
            node = (GraphNode) type.newInstance();
        }
        catch (InstantiationException e) {
            // Most propably an interface was about to instantiate, so screen
            // for appropriate implementation.
            Class impl = (Class) implMapping.get(type);
            if (impl == null) {
                for (Iterator iter = typeMapping.values().iterator(); iter.hasNext();) {
                    Class clazz = (Class) iter.next();
                    if (type.isAssignableFrom(clazz)) {
                        impl = clazz;
                        implMapping.put(type, clazz);
                    }
                }
                // Appropriate mapping has not been found.
                if (impl == null) {
                    cat.fatal("Mapping for " + type + " not found.");
                }
            }
            try {
                node = (GraphNode) impl.newInstance();
            }
            catch (InstantiationException ex) {
                cat.fatal("Fatal error during creation of new node of type " + type, e);
            }
            catch (IllegalAccessException ex) {
                cat.fatal("Fatal error during creation of new node of type " + type, e);
            }
        }
        catch (IllegalAccessException e) {
            cat.fatal("fatal error during creation of new node of type " + type, e);
        }
        return node;
    }

    public String getType(GraphNode node) {
        Class clazz = node.getClass();
        for (Iterator iter = typeMapping.keySet().iterator(); iter.hasNext();) {
            String key = (String) iter.next();
            Class claz = (Class) typeMapping.get(key);
            if (claz == clazz) {
                return key;
            }
        }
        throw new IllegalStateException("type not found for " + node + " " + node.getClass().getName());
    }
}

