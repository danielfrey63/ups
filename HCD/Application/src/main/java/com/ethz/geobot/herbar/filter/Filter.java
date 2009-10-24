/*
 * Filter.java
 *
 * Created on 26. Juli 2002, 15:14
 */

package com.ethz.geobot.herbar.filter;

import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.filter.FilterDefinitionDetail;
import com.ethz.geobot.herbar.model.filter.FilterModel;

/**
 * Bean class to load filter definition.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class Filter {

    /**
     * Holds value of property name.
     */
    private String name;

    /**
     * Holds value of property baseFilterName.
     */
    private String baseFilterName;

    /**
     * Holds value of property details.
     */
    private Detail[] details;

    /**
     * Creates a new instance of Filter
     */
    public Filter() {
    }

    public Filter(FilterModel model) {
        name = model.getName();
        HerbarModel baseModel = model.getDependantModel();

        if (baseModel instanceof FilterModel) {
            FilterModel bfm = (FilterModel) baseModel;
            baseFilterName = bfm.getName();
        }
        else {
            baseFilterName = "";
        }

        // copy filter details
        FilterDefinitionDetail[] defDetails = model.getFilterDetails();
        details = new Detail[defDetails.length];
        for (int i = 0; i < defDetails.length; i++) {
            details[ i ] = new Detail(defDetails[ i ]);
        }
    }

    /**
     * Getter for property name.
     *
     * @return Value of property name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter for property name.
     *
     * @param name New value of property name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for property baseFilterName.
     *
     * @return Value of property baseFilterName.
     */
    public String getBaseFilterName() {
        return this.baseFilterName;
    }

    /**
     * Setter for property baseFilterName.
     *
     * @param baseFilterName New value of property baseFilterName.
     */
    public void setBaseFilterName(String baseFilterName) {
        this.baseFilterName = baseFilterName;
    }

    /**
     * Getter for property details.
     *
     * @return Value of property details.
     */
    public Detail[] getDetails() {
        return this.details;
    }

    /**
     * Setter for property details.
     *
     * @param details New value of property details.
     */
    public void setDetails(Detail[] details) {
        this.details = details;
    }
}
