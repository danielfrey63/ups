/*
 * Copyright (c) 2011.
 *
 * Nutzung und Rechte
 *
 * Die Applikation eBot wurde für Studierende der ETH Zürich entwickelt. Sie  steht
 * allen   an   Hochschulen  oder   Fachhochschulen   eingeschriebenen Studierenden
 * (auch  ausserhalb  der  ETH  Zürich)  für  nichtkommerzielle  Zwecke  im Studium
 * kostenlos zur Verfügung. Nichtstudierende Privatpersonen, die die Applikation zu
 * ihrer  persönlichen  Weiterbildung  nutzen  möchten,  werden  gebeten,  für  die
 * nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.– zu bezahlen.
 *
 * Postkonto
 *
 * Unterricht, 85-761469-0, Vermerk "eBot"
 * IBAN 59 0900 0000 8576  1469 0; BIC POFICHBEXXX
 *
 * Jede andere Nutzung der Applikation  ist vorher mit dem Projektleiter  (Matthias
 * Baltisberger, Email:  balti@ethz.ch) abzusprechen  und mit  einer entsprechenden
 * Vereinbarung zu regeln. Die  Applikation wird ohne jegliche  Garantien bezüglich
 * Nutzungsansprüchen zur Verfügung gestellt.
 */

/*
 * FilterWizardModel.java
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
public class FilterWizardModel extends DefaultWizardModel
{
    public static final String FILTER_NAME = "filterName";

    public static final String FILTER_BASE = "baseModel";

    public static final String FILTER_MODEL = "filterModel";

    private static final String NAME = "FilterWizardModel";

    /** Holds value of property filterModel. */
    private FilterModel filterModel;

    private final NameValidator nameValidator;

    private final String title;

    public FilterWizardModel( final HerbarContext context, final WizardPane[] panes, final FilterModel filterModel,
                              final NameValidator nameValidator, final String title )
    {
        super( context, panes, NAME );
        this.title = title;
        this.nameValidator = nameValidator;
        setFilterModel( filterModel );
        initPaneList();
    }

    public String getDialogTitle()
    {
        return title;
    }

    /**
     * Getter for property filterName.
     *
     * @return Value of property filterName.
     */
    public String getFilterName()
    {
        return filterModel.getName();
    }

    /**
     * Setter for property filterName.
     *
     * @param filterName New value of property filterName.
     */
    public void setFilterName( final String filterName )
    {
        final String oldFilterName = filterModel.getName();
        filterModel.setName( filterName );
        propertySupport.firePropertyChange( FILTER_NAME, oldFilterName, filterName );
    }

    /**
     * Getter for property base.
     *
     * @return Value of property base.
     */
    public HerbarModel getBaseModel()
    {
        return filterModel.getDependantModel();
    }

    /**
     * Setter for property base.
     *
     * @param baseModel New value of property base.
     */
    public void setBaseModel( final HerbarModel baseModel )
    {
        final HerbarModel oldBase = filterModel.getDependantModel();
        filterModel.setDependentModel( baseModel );
        propertySupport.firePropertyChange( FILTER_BASE, oldBase, baseModel );
    }

    /**
     * Getter for property filterModel.
     *
     * @return Value of property filterModel.
     */
    public FilterModel getFilterModel()
    {
        return this.filterModel;
    }

    /**
     * Setter for property filterModel.
     *
     * @param filterModel New value of property filterModel.
     */
    public void setFilterModel( final FilterModel filterModel )
    {
        final FilterModel oldFilterModel = this.filterModel;
        this.filterModel = filterModel;
        propertySupport.firePropertyChange( FILTER_MODEL, oldFilterModel, filterModel );
    }

    public boolean isValidName( final String newName )
    {
        return nameValidator.isValidName( newName );
    }
}
