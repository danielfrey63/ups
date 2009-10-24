package com.ethz.geobot.herbar.model.relevance;

import ch.jfactory.application.SystemUtil;
import ch.jfactory.model.graph.GraphNodeImpl;
import com.ethz.geobot.herbar.model.MorAttribute;
import com.ethz.geobot.herbar.model.MorValue;
import com.ethz.geobot.herbar.model.Taxon;
import com.ethz.geobot.herbar.model.origin.RoleAssignedImpl;
import com.ethz.geobot.herbar.model.origin.RoleInheritedImpl;
import com.ethz.geobot.herbar.model.origin.RoleUnitedImpl;
import com.ethz.geobot.herbar.model.relevance.marker.DifferentAssigned;
import com.ethz.geobot.herbar.model.relevance.marker.DifferentInherited;
import com.ethz.geobot.herbar.model.relevance.marker.DifferentUnited;
import com.ethz.geobot.herbar.model.relevance.marker.IdentifyingAssigned;
import com.ethz.geobot.herbar.model.relevance.marker.IdentifyingInherited;
import com.ethz.geobot.herbar.model.relevance.marker.IdentifyingUnited;
import com.ethz.geobot.herbar.model.relevance.marker.UniqueAssigned;
import com.ethz.geobot.herbar.model.relevance.marker.UniqueInherited;
import com.ethz.geobot.herbar.model.relevance.marker.UniqueUnited;
import com.ethz.geobot.herbar.model.relevance.marker.UnrelevantAssigned;
import com.ethz.geobot.herbar.model.relevance.marker.UnrelevantInherited;
import com.ethz.geobot.herbar.model.relevance.marker.UnrelevantUnited;
import com.ethz.geobot.herbar.model.relevance.marker.WeakAssigned;
import com.ethz.geobot.herbar.model.relevance.marker.WeakInherited;
import com.ethz.geobot.herbar.model.relevance.marker.WeakUnited;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import org.apache.log4j.Category;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
public abstract class AbsRelevance extends GraphNodeImpl {

    private static final String BASE = "herbar.relevance.";
    private static final Category CAT = Category.getInstance(AbsRelevance.class);

    /**
     * Used to map relevances configured in the properties file to ranks.
     */
    private static Map relevances = new HashMap();

    private static Map roleRelevanceMapping = new HashMap();

    /**
     * The least important relevance to be returned if it is not worth to screen for relevances, i.e. when there is only
     * one Taxon object.
     */
    private static AbsRelevance lastRelevance;

    private int rank;

    static {
        try {
            // Load relevances
            Properties props = System.getProperties();
            Enumeration e = props.keys();
            TreeMap map = new TreeMap();
            while (e.hasMoreElements()) {
                String property = (String) e.nextElement();
                if (property.startsWith(BASE)) {
                    int bLen = BASE.length();
                    int pLen = property.length();
                    String pos = property.substring(bLen, pLen);
                    int index = 0;
                    try {
                        index = Integer.parseInt(pos);
                    }
                    catch (Exception x) {
                        CAT.fatal("Invalid extention \"" + pos + "\" to property " + property);
                        SystemUtil.EXIT.exit(1);
                    }
                    map.put(new Integer(index), property);
                }
            }
            int i = 0;
            for (Iterator iter = map.keySet().iterator(); iter.hasNext(); i++) {
                Object key = iter.next();
                Object prop = map.get(key);
                String name = (String) props.get(prop);
                try {
                    Class clazz = Class.forName(name);
                    lastRelevance = (AbsRelevance) clazz.newInstance();
                    lastRelevance.setRank(i);
                    relevances.put(lastRelevance, new Integer(i));
                    CAT.info("Putting " + lastRelevance.getClass().getName()
                            + " at position " + i);
                }
                catch (Exception ex) {
                    String message = "Could not initiate relevance class: " + name
                            + " from system property " + prop;
                    CAT.fatal(message, ex);
                    throw new IllegalStateException(message);
                }
            }
            // Load mapping
            HashMap relevancesMap;

            relevancesMap = new HashMap();
            relevancesMap.put(IdentifyingRelevance.class, IdentifyingInherited.class);
            relevancesMap.put(UniqueRelevance.class, UniqueInherited.class);
            relevancesMap.put(DifferentRelevance.class, DifferentInherited.class);
            relevancesMap.put(WeakRelevance.class, WeakInherited.class);
            relevancesMap.put(UnRelevance.class, UnrelevantInherited.class);
            roleRelevanceMapping.put(RoleInheritedImpl.class, relevancesMap);

            relevancesMap = new HashMap();
            relevancesMap.put(IdentifyingRelevance.class, IdentifyingUnited.class);
            relevancesMap.put(UniqueRelevance.class, UniqueUnited.class);
            relevancesMap.put(DifferentRelevance.class, DifferentUnited.class);
            relevancesMap.put(WeakRelevance.class, WeakUnited.class);
            relevancesMap.put(UnRelevance.class, UnrelevantUnited.class);
            roleRelevanceMapping.put(RoleUnitedImpl.class, relevancesMap);

            relevancesMap = new HashMap();
            relevancesMap.put(IdentifyingRelevance.class, IdentifyingAssigned.class);
            relevancesMap.put(UniqueRelevance.class, UniqueAssigned.class);
            relevancesMap.put(DifferentRelevance.class, DifferentAssigned.class);
            relevancesMap.put(WeakRelevance.class, WeakAssigned.class);
            relevancesMap.put(UnRelevance.class, UnrelevantAssigned.class);
            roleRelevanceMapping.put(RoleAssignedImpl.class, relevancesMap);
        }
        catch (ExceptionInInitializerError ex) {
            CAT.fatal("Fatal error during static initalization of "
                    + AbsRelevance.class);
            CAT.fatal("Reason", ex.getCause());
        }
    }

    /**
     * Returns the first relevant AbsRelevance found. The relevances are tested in the order they are given during
     * initialization.
     */
    public static AbsRelevance getRelevance(Taxon[] taxa, MorValue value) {
//        if (taxa.length <= 1) {
//            return lastRelevance;
//        }
        RelevanceMetaData md = new RelevanceMetaData();
        md.taxaWithThisValue = getTaxa(taxa, value);
        md.siblingsWithThisValue = md.taxaWithThisValue.length;
        md.parentAttribute = (MorAttribute) value.getParentAttribute();
        md.siblingsWithThisAttribute = getTaxa(taxa, md.parentAttribute).length;
        for (Iterator iter = relevances.keySet().iterator(); iter.hasNext();) {
            AbsRelevance rel = (AbsRelevance) iter.next();
            if (rel.isRelevant(taxa, value, md)) {
                return rel;
            }
        }
        return null;
    }

    /**
     * Returns one of the configured relevances taking all given MorValue objects into account. Be warned that this is
     * <b>not</b> the same as calling {@link #getRelevance(Taxon[],MorValue)} for each MorValue object. This method the
     * best relevance for any MorValue object given. <ul> <li>{@link com.ethz.geobot.herbar.model.relevance.IdentifyingRelevance
     * IdentifyingRelevance}</li> <li>{@link com.ethz.geobot.herbar.model.relevance.UniqueRelevance
     * UniqueRelevance}</li> <li>{@link com.ethz.geobot.herbar.model.relevance.DifferentRelevance
     * DifferentRelevance}</li> <li>{@link com.ethz.geobot.herbar.model.relevance.WeakRelevance WeakRelevance}</li>
     * <li>{@link com.ethz.geobot.herbar.model.relevance.UnRelevance UnRelevance}</li> </ul>
     */
    public static AbsRelevance getRelevance(Taxon[] taxa, MorValue[] values) {
//        if (taxa.length <= 1) {
//            return lastRelevance;
//        }
        for (int i = 0; i < values.length; i++) {
            MorValue value = values[ i ];
            RelevanceMetaData md = new RelevanceMetaData();
            md.taxaWithThisValue = getTaxa(taxa, value);
            md.siblingsWithThisValue = md.taxaWithThisValue.length;
            md.parentAttribute = (MorAttribute) value.getParentAttribute();
            md.siblingsWithThisAttribute = getTaxa(taxa, md.parentAttribute).length;
            for (Iterator iter = relevances.keySet().iterator(); iter.hasNext();) {
                AbsRelevance rel = (AbsRelevance) iter.next();
                if (rel.isRelevant(taxa, value, md)) {
                    return rel;
                }
            }
        }
        return null;
    }

    /**
     * Looks up the role in a map to get an array of combinations of this role with any relevance. It then picks out the
     * appropriate combined object.
     */
/*
    public static Role getRoleRelevance(Role role,  AbsRelevance relevance) {
        HashMap map = (HashMap)roleRelevanceMapping.get(role.getClass());
        if (map != null) {
            Class clazz = (Class)map.get(relevance.getClass());
            Role newRole = AbsGraphModel.getModel().getRole(clazz);
            Integer i = (Integer)relevances.get(relevance);
            if (i == null) {
                String message = "Rank for relevance "
                    + relevance.getClass().getName() + " with role "
                    + role.getClass().getName() + " not found. "
                    + "Available registered relevaces are: " + relevances;
                CAT.fatal(message);
                throw new IllegalStateException(message);
            }
            newRole.setRank(i.intValue());
            return newRole;
        }
        else {
            String message = "Class for origin " + role.getClass().getName()
                + " not found.";
            CAT.fatal(message);
            throw new IllegalStateException(message);
        }
    }
*/
    private static String getKey(Object relevance) {
        String name = relevance.getClass().getName();
        return name.substring(name.lastIndexOf('.') + 1, name.length());
    }

    /**
     * Returns the Taxa containing the given MorValue.
     *
     * @param taxa the Taxon objects to search for the MorValue
     * @param val  the MorValue to search in the Taxon objects
     * @return the Taxon objects referencing MorValue
     */
    protected static Taxon[] getTaxa(Taxon[] taxa, MorValue val) {
        ArrayList result = new ArrayList();
        for (int i = 0; i < taxa.length; i++) {
            if (Arrays.asList(taxa[ i ].getMorValues()).contains(val)) {
                result.add(taxa[ i ]);
            }
        }
        return (Taxon[]) result.toArray(new Taxon[0]);
    }

    /**
     * Returns the Taxa containing the given MorValue.
     *
     * @param taxa the Taxon objects to search for the MorValue
     * @param att  Description of the Parameter
     * @return the Taxon objects referencing MorValue
     */
    protected static Taxon[] getTaxa(Taxon[] taxa, MorAttribute att) {
        ArrayList result = new ArrayList();
        for (int i = 0; i < taxa.length; i++) {
            if (Arrays.asList(taxa[ i ].getMorAttributes()).contains(att)) {
                result.add(taxa[ i ]);
            }
        }
        return (Taxon[]) result.toArray(new Taxon[0]);
    }

    protected static MorValue[] getValues(Taxon taxon, MorAttribute att) {
        ArrayList result = new ArrayList();
        MorValue[] vals = taxon.getMorValues();
        for (int i = 0; i < vals.length; i++) {
            if (vals[ i ].getParentAttribute() == att) {
                result.add(vals[ i ]);
            }
        }
        return (MorValue[]) result.toArray(new MorValue[0]);
    }

    /**
     * Returns whether the given MorValue objects are relevant in context of the given Taxon objects. This default
     * implementation returns true as soon as at least one of the Values given meets the Relevance implementations
     * criteria to be relevant.
     */
    public boolean isRelevant(Taxon[] taxa, MorValue[] values) {
        for (int i = 0; i < values.length; i++) {
            if (isRelevant(taxa, values[ i ])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return whether the MorValue given is relevant in context of the given Taxon objects.
     */
    public abstract boolean isRelevant(Taxon[] taxa, MorValue value);

    /**
     * Same as {@link #isRelevant(Taxon[],MorValue)}, but already calculated data may be reused to increase
     * performance.
     */
    public abstract boolean isRelevant(Taxon[] taxa, MorValue value,
                                       RelevanceMetaData metadata);

    /**
     * @see ch.jfactory.model.graph.AbsSimpleGraphNode#toString()
     */
    public String toString() {
        String clazz = this.getClass().getName();
        String name = clazz.substring(clazz.lastIndexOf('.') + 1, clazz.length());
        return getName() + " (" + getRank() + "/" + name + ")";
    }
}

/**
 * For reuse of metadata used for relevance calculations. Makes them approximatly three times faster.
 */
class RelevanceMetaData {

    Taxon[] taxaWithThisValue = null;
    MorAttribute parentAttribute = null;
    int siblingsWithThisValue = -1;
    int siblingsWithThisAttribute = -1;
}
