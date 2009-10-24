/*
 * FilterFactory.java
 *
 * Created on 21. August 2002, 15:15
 */

package com.ethz.geobot.herbar.filter;

import com.ethz.geobot.herbar.Application;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import com.ethz.geobot.herbar.model.filter.FilterModel;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Category;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * This class is used to store and load FilterModel from the persistent storage.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class FilterFactory {

    private static final String FILTER_LOCATION = System.getProperty("user.home") + "/" + System.getProperty("herbar.filter.location");
    private static final Category cat = Category.getInstance(FilterFactory.class);

    private static FilterFactory instance = null;

    private Mapping filterMapping = null;
    private HerbarModel model = Application.getInstance().getModel();
    private Map<String, FilterModel> cachedFilterModels = new HashMap<String, FilterModel>();

    /**
     * Creates a new instance of FilterFactory
     */
    protected FilterFactory() {
        try {
            cat.info("reading mapping file for filter definition.");
            filterMapping = new Mapping();
            filterMapping.loadMapping(this.getClass().getResource("filtermapping.xml"));
        }
        catch (Exception ex) {
            filterMapping = null;
            cat.error("failed to load mapping file for filter definition.", ex);
        }
    }

    /**
     * This method returns the reference to the FilterFactory.
     *
     * @return the FilterFactory singleton
     */
    public static FilterFactory getInstance() {
        if (instance == null) {
            instance = new FilterFactory();
        }
        return instance;
    }

    private Level[] collectLevels(String[] levelNames) {
        Level[] levels = new Level[0];
        if (levelNames != null) {
            levels = new Level[levelNames.length];
            for (int i = 0; i < levelNames.length; i++) {
                levels[ i ] = model.getLevel(levelNames[ i ]);
            }
        }
        else {
            cat.warn("filter has no levels");
        }
        return levels;
    }

    private FilterModel generateFilterModel(Filter filter) throws FilterPersistentException {
        String baseModelName = filter.getBaseFilterName();
        HerbarModel baseModel = Application.getInstance().getModel();

        if (!"".equals(filter.getBaseFilterName())) {
            baseModel = getFilterModel(baseModelName);
        }

        FilterModel model = new FilterModel(baseModel, filter.getName());
        model.clearFilterDetails();

        Detail details[] = filter.getDetails();
        for (Detail detail : details) {
            Taxon scope = baseModel.getTaxon(detail.getScope());
            Level[] levels = collectLevels(detail.getLevels());
            if (scope != null) {
                model.addFilterDetail(scope, levels);
            }
            else {
                cat.warn("cannot find scope: " + detail.getScope() + " in dependent model: " + baseModelName +
                        " for model: " + filter.getName() + " skip detail");
            }
        }
        return model;
    }

    /**
     * Return a filter definition from the persistent storage. It also load its dependend filters.<p> This method makes
     * sure the base model exists, otherwise it is reset to the default. It guarantees also that the name of the model
     * is equal to the name of the file.
     *
     * @param name the name of the filer
     * @return the loaded FilterModel
     * @throws FilterPersistentException is thrown if the load of the filter failed
     */
    public FilterModel getFilterModel(String name) throws FilterPersistentException {
        FilterModel model = cachedFilterModels.get(name);
        if (model == null) {
            Filter filter = loadFilter(name);
            // make sure renamed filter files are still consistent
            filter.setName(name);
            // make sure the base name exists
            String base = filter.getBaseFilterName();
            boolean baseFilterFound = false;
            for (Iterator<String> it = getFilterNames().iterator(); it.hasNext() && !baseFilterFound;) {
                String filterNameToCompare = it.next();
                baseFilterFound |= base.equals(filterNameToCompare);
            }
            if (!baseFilterFound || base.equals(name)) {
                filter.setBaseFilterName("");
            }
            // finally load model
            model = generateFilterModel(filter);
            saveFilterModel(model);
            cachedFilterModels.put(name, model);
        }
        return model;
    }

    /**
     * This mehod stores the FilterModel to the persistent storage.
     *
     * @param filterModel the FilerDefinition which should be stored
     * @throws FilterPersistentException is thrown if filter couldn't be stored
     */
    public void saveFilterModel(FilterModel filterModel) throws FilterPersistentException {
        // saving to a new file includes removing the old
        Collection<FilterModel> values = cachedFilterModels.values();
        Collection<String> keys = cachedFilterModels.keySet();
        Iterator<String> keyIterator = keys.iterator();
        String modelName = null;
        for (FilterModel model : values) {
            String name = keyIterator.next();
            if (model == filterModel) {
                modelName = name;
                break;
            }
        }
        modelName = (modelName == null ? filterModel.getName() : modelName);
        cachedFilterModels.remove(modelName);
        String filename = generateFilterFileName(modelName);
        File file = new File(filename);
        file.delete();
        // save
        Filter filter = new Filter(filterModel);
        saveFilter(filter);
        // save FilterModel to cached list, could be new one or is overwritten
        cachedFilterModels.put(filterModel.getName(), filterModel);
    }

    /**
     * This method remove a FilterModel from the persistent storage.
     *
     * @param filterModel the name of the filter
     * @throws FilterPersistentException is thrown if filter couldn't be deleted
     */
    public void removeFilterModel(FilterModel filterModel) throws FilterPersistentException {
        // make sure the right model is handled even if the name has changed.
        Collection<FilterModel> values = cachedFilterModels.values();
        Collection<String> keys = cachedFilterModels.keySet();
        Iterator<String> keyIterator = keys.iterator();
        String modelName = null;
        for (FilterModel model : values) {
            String name = keyIterator.next();
            if (model == filterModel) {
                modelName = name;
                break;
            }
        }
        modelName = (modelName == null ? filterModel.getName() : modelName);
        String filename = generateFilterFileName(modelName);
        File file = new File(filename);
        if (!file.delete()) {
            throw new FilterPersistentException("Failed to remove filter " + filename + " (" + file + ")");
        }
        else {
            cachedFilterModels.remove(modelName);
        }
    }

    private String generateFilterFileName(String name) {
        return FILTER_LOCATION + "/" + name + ".xml";
    }

    private String generateFilterName(String filename) {
        return filename.substring(0, filename.lastIndexOf('.'));
    }

    private Filter loadFilter(String name) throws FilterPersistentException {
        if (filterMapping != null) {
            String filename = generateFilterFileName(name);
            try {
                Reader in = new BufferedReader(new FileReader(filename));
                Unmarshaller unmarshaller = new Unmarshaller(filterMapping);
                return (Filter) unmarshaller.unmarshal(in);
            }
            catch (Exception ex) {
                String msg = "failed to load filter " + name + " (" + filename + ").";
                cat.error(msg, ex);
                throw new FilterPersistentException(msg, ex);
            }
        }
        else {
            throw new FilterPersistentException("filer persistents not initialized.");
        }
    }

    private void saveFilter(Filter filter) throws FilterPersistentException {
        if (filterMapping != null) {
            String filename = generateFilterFileName(filter.getName());
            try {
                Writer out = new BufferedWriter(new FileWriter(filename));
                Marshaller marshaller = new Marshaller(out);
                marshaller.setMapping(filterMapping);
                marshaller.marshal(filter);
                out.close();
            }
            catch (Exception ex) {
                String msg = "Failed to save filter " + filter.getName() + " (" + filename + ").";
                cat.error(msg, ex);
                throw new FilterPersistentException(msg, ex);
            }
        }
        else {
            throw new FilterPersistentException("filer persistents not initialized.");
        }
    }

    /**
     * This method is called to get all available filters.
     *
     * @return a set of filter names, represent by String objects
     */
    public Set<String> getFilterNames() {
        Set<String> list = new HashSet<String>();
        if (FILTER_LOCATION != null) {
            File file = new File(FILTER_LOCATION);
            if (file.exists()) {
                File[] files = file.listFiles();
                for (File file1 : files) {
                    if (cat.isDebugEnabled()) {
                        cat.debug("filter with name " + file1.getName() + " found.");
                    }
                    list.add(generateFilterName(file1.getName()));
                }
            }
            else {
                cat.info("create filter path (" + FILTER_LOCATION + ")");
                file.mkdirs();
            }
        }
        else {
            cat.warn("location of filter is not set (please check configuration file)");
        }
        return list;
    }

    public Set<? extends HerbarModel> getFilters() {
        Set<FilterModel> result = new HashSet<FilterModel>();
        Set<String> names = getFilterNames();
        for (String name : names) {
            try {
                result.add(getFilterModel(name));
            }
            catch (FilterPersistentException e) {
                cat.error("failed to laod filter " + name, e);
            }
        }
        return result;
    }
}
