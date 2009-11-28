/*
 * LessonWizardModel.java
 *
 * Created on 10. Juli 2002, 16:36
 */

package com.ethz.geobot.herbar.modeapi.wizard;

import com.ethz.geobot.herbar.modeapi.HerbarContext;
import com.ethz.geobot.herbar.modeapi.SimpleTaxStateModel;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;

/**
 * This model is used to select the region for lesson, ask mode.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:11 $
 */
public class LessonWizardModel extends DefaultWizardModel
{
    public final static String MODEL = "model";

    public final static String TAXLIST = "taxList";

    public static final String NAME = "LessonWizardModel";

    /**
     * Holds value of property model.
     */
    private HerbarModel model;

    private final SimpleTaxStateModel taxModel;

    private final String title;

    public LessonWizardModel( final HerbarContext context, final WizardPane[] panes, final String title )
    {
        super( context, panes, NAME );
        this.title = title;
        taxModel = new SimpleTaxStateModel( propertySupport );
    }

    public String getDialogTitle()
    {
        return title;
    }

    /**
     * Getter for property model.
     *
     * @return Value of property model.
     */
    public HerbarModel getModel()
    {
        return this.model;
    }

    /**
     * Setter for property model.
     *
     * @param model New value of property model.
     */
    public void setModel( final HerbarModel model )
    {
        final HerbarModel oldModel = this.model;
        this.model = model;

        // refresh all properties depending on the model
        final Taxon rootTaxon = model.getRootTaxon();
        final Level[] subLevels = rootTaxon.getSubLevels();
        setScope( rootTaxon );
        setLevel( subLevels[subLevels.length - 1] );
        setFocus( getTaxList()[0] );
        setOrdered( true );

        propertySupport.firePropertyChange( MODEL, oldModel, model );
    }

    /**
     * get selected list of taxa
     *
     * @return array of Taxon objects
     */
    public Taxon[] getTaxList()
    {
        return getScope().getAllChildTaxa( getLevel() );
    }

    /**
     * @see SimpleTaxStateModel#getScope()
     */
    public Taxon getScope()
    {
        return ( taxModel == null ? null : taxModel.getScope() );
    }

    /**
     * @see SimpleTaxStateModel#setScope(Taxon)
     */
    public void setScope( final Taxon scope )
    {
        taxModel.setScope( scope );
    }

    /**
     * @see SimpleTaxStateModel#getLevel()
     */
    public Level getLevel()
    {
        return ( taxModel == null ? null : taxModel.getLevel() );
    }

    /**
     * @see SimpleTaxStateModel#setLevel(Level)
     */
    public void setLevel( final Level level )
    {
        taxModel.setLevel( level );
    }

    /**
     * @see SimpleTaxStateModel#getFocus()
     */
    public Taxon getFocus()
    {
        return ( taxModel == null ? null : taxModel.getFocus() );
    }

    /**
     * @see SimpleTaxStateModel#setFocus(Taxon)
     */
    public void setFocus( final Taxon focus )
    {
        taxModel.setFocus( focus );
    }

    /**
     * @see SimpleTaxStateModel#isOrdered()
     */
    public boolean isOrdered()
    {
        return ( taxModel != null && taxModel.isOrdered() );
    }

    /**
     * @see SimpleTaxStateModel#setOrdered(boolean)
     */
    public void setOrdered( final boolean ordered )
    {
        taxModel.setOrdered( ordered );
    }
}
