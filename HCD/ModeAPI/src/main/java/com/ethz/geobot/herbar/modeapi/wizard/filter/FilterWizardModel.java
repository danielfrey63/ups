/*
 * HebarSelectionWizardModel.java
 *
 * Created on 10. Juli 2002, 16:36
 */

package com.ethz.geobot.herbar.modeapi.wizard.filter;

import com.ethz.geobot.herbar.modeapi.HerbarContext;
import com.ethz.geobot.herbar.modeapi.wizard.DefaultWizardModel;
import com.ethz.geobot.herbar.modeapi.wizard.NameValidator;
import com.ethz.geobot.herbar.modeapi.wizard.WizardPane;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.filter.FilterModel;

/**
 * This model is used to add filter definition
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:13 $
 */
public class FilterWizardModel extends DefaultWizardModel {

    public static final String FILTER_NAME = "filterName";
    public static final String FILTER_BASE = "baseModel";
    public static final String FILTER_MODEL = "filterModel";

    private static final String NAME = "FilterWizardModel";

    /**
     * Holds value of property base.
     */
    private HerbarModel base;

    /**
     * Holds value of property filterModel.
     */
    private FilterModel filterModel;

    private NameValidator nameValidator;

    private String title;

    public FilterWizardModel(HerbarContext context, WizardPane[] panes, FilterModel filterModel,
                             NameValidator nameValidator, String title) {
        super(context, panes, NAME);
        this.title = title;
        this.nameValidator = nameValidator;
        setFilterModel(filterModel);
    }

    public String getDialogTitle() {
        return title;
    }

    /**
     * Getter for property filterName.
     *
     * @return Value of property filterName.
     */
    public String getFilterName() {
        return filterModel.getName();
    }

    /**
     * Setter for property filterName.
     *
     * @param filterName New value of property filterName.
     */
    public void setFilterName(String filterName) {
        String oldFilterName = filterModel.getName();
        filterModel.setName(filterName);
        propertySupport.firePropertyChange(FILTER_NAME, oldFilterName, filterName);
    }

    /**
     * Getter for property base.
     *
     * @return Value of property base.
     */
    public HerbarModel getBaseModel() {
        return filterModel.getDependantModel();
    }

    /**
     * Setter for property base.
     *
     * @param baseModel New value of property base.
     */
    public void setBaseModel(HerbarModel baseModel) {
        HerbarModel oldBase = filterModel.getDependantModel();
        filterModel.setDependentModel(baseModel);
        propertySupport.firePropertyChange(FILTER_BASE, oldBase, baseModel);
    }

    /**
     * Getter for property filterModel.
     *
     * @return Value of property filterModel.
     */
    public FilterModel getFilterModel() {
        return this.filterModel;
    }

    /**
     * Setter for property filterModel.
     *
     * @param filterModel New value of property filterModel.
     */
    public void setFilterModel(FilterModel filterModel) {
        FilterModel oldFilterModel = this.filterModel;
        this.filterModel = filterModel;
        this.base = filterModel.getDependantModel();
        propertySupport.firePropertyChange(FILTER_MODEL, oldFilterModel, filterModel);
    }

    public boolean isValidName(String newName) {
        return nameValidator.isValidName(newName);
    }
}
