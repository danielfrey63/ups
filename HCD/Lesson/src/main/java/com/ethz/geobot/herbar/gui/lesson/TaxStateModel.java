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
package com.ethz.geobot.herbar.gui.lesson;

import ch.jfactory.lang.ArrayUtils;
import ch.jfactory.math.RandomUtils;
import com.ethz.geobot.herbar.modeapi.HerbarContext;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.EditState.EDIT;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.EditState.USE;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.SubMode.Abfragen;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.SubMode.Lernen;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.Edit;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.Focus;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.Level;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.List;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.Model;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.Ordered;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.Scope;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.SubModus;

public class TaxStateModel
{
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport( this );

    private final HerbarContext context;

    private Taxon[] taxList;

    private HerbarModel herbarModel;

    private final TaxStateValues vals;

    private EditState listState = USE;

    private final HashMap<String, SubMode> subModes = new HashMap<String, SubMode>();

    public TaxStateModel( HerbarContext context, final HerbarModel herbarModel )
    {
        vals = new TaxStateValues();
        this.context = context;
        setModel( herbarModel );
    }

    /**
     * Simply return the next taxon without changing the state.
     *
     * @return the next taxon if exists or null
     */
    public Taxon getNext()
    {
        final int current = ArrayUtils.indexOf( taxList, vals.focus );
        return current < taxList.length - 1 ? taxList[current + 1] : null;
    }

    /**
     * Simply return the previous taxon without changing the state.
     *
     * @return the previous taxon if exists or null
     */
    public Taxon getPrev()
    {
        final int current = ArrayUtils.indexOf( taxList, vals.focus );
        return current == 0 ? null : taxList[current - 1];
    }

    /**
     * The model (also known as the taxon list) ist set here and all states are adapted to a consistent overall state.
     *
     * @param model the model to set
     */
    public void setModel( final HerbarModel model )
    {
        if ( model != null && model != herbarModel )
        {
            final ArrayList<FireArray> fire = new ArrayList<FireArray>();
            setInternalModel( fire, model );
            setInternalScope( fire, model.getRootTaxon() );
            setInternalLevel( fire, vals.level );
            setInternalTaxList( fire );
            setInternalOrdered( fire, true );
            setInternalFocus( fire, taxList[0] );
            setInternalGlobalSubMode( fire, Lernen );
            fireAllPropertyChangeEvents( fire );
        }
    }

    public void setScope( final Taxon scope )
    {
        if ( scope != null && scope != vals.scope )
        {
            final ArrayList<FireArray> fire = new ArrayList<FireArray>();
            setInternalScope( fire, scope );
            setInternalTaxList( fire );
            setInternalOrdered( fire, vals.ordered );
            setInternalFocus( fire, taxList[0] );
            fireAllPropertyChangeEvents( fire );
        }
    }

    public void setLevel( final Level level )
    {
        if ( level != null && level != vals.level )
        {
            final ArrayList<FireArray> fire = new ArrayList<FireArray>();
            setInternalLevel( fire, level );
            setInternalTaxList( fire );
            setInternalOrdered( fire, vals.ordered );
            setInternalFocus( fire, taxList[0] );
            fireAllPropertyChangeEvents( fire );
        }
    }

    public void setOrdered( final boolean ordered )
    {
        if ( ordered != vals.ordered )
        {
            final ArrayList<FireArray> fire = new ArrayList<FireArray>();
            setInternalOrdered( fire, ordered );
            // Bit hacky: In order to get a focus change event thrown despite the fact that the focus taxon remains the
            // same, we change the focus first to another taxon.
            final Taxon focus = getFocus();
            setInternalFocus( fire, taxList[0] );
            setInternalFocus( fire, taxList[ArrayUtils.indexOf( taxList, focus )] );
            setInternalGlobalSubMode( fire, getGlobalSubMode() );
            fireAllPropertyChangeEvents( fire );
        }
    }

    public void setFocus( final Taxon focus )
    {
        if ( focus != null && focus != vals.focus )
        {
            final ArrayList<FireArray> fire = new ArrayList<FireArray>();
            setInternalFocus( fire, focus );
            setInternalGlobalSubMode( fire, getGlobalSubMode() );
            fireAllPropertyChangeEvents( fire );
        }
    }

    public void setGlobalSubMode( final SubMode subMode )
    {
        final ArrayList<FireArray> fire = new ArrayList<FireArray>();
        setInternalGlobalSubMode( fire, subMode );
        fireAllPropertyChangeEvents( fire );
    }

    public void setSubMode( final String taxon, final SubMode subMode )
    {
        final ArrayList<FireArray> fire = new ArrayList<FireArray>();
        final SubMode oldGlobalSubMode = getGlobalSubMode();
        subModes.put( taxon, subMode );
        final SubMode newGlobalSubMode = getGlobalSubMode();
        fire.add( new FireArray( SubModus.name(), oldGlobalSubMode, newGlobalSubMode ) );
        fireAllPropertyChangeEvents( fire );
    }

    public void setEditMode( final EditState mode )
    {
        final ArrayList<FireArray> fire = new ArrayList<FireArray>();
        setInternalGlobalSubMode( fire, Lernen );
        setInternalEditMode( fire, mode );
        fireAllPropertyChangeEvents( fire );
    }

    private void setInternalEditMode( ArrayList<FireArray> fire, EditState mode )
    {
        EditState oldMode = listState;
        listState = mode;
        fire.add( new FireArray( Edit.name(), oldMode, mode ) );
    }

    /**
     * Set the model if needed and registers a notification if needed.
     *
     * @param fire  the notifications list
     * @param model the new model
     */
    private void setInternalModel( final ArrayList<FireArray> fire, final HerbarModel model )
    {
        if ( model != null && model != herbarModel )
        {
            fire.add( new FireArray( Model.name(), herbarModel, model ) );
            herbarModel = model;
        }
    }

    /**
     * Sets the scope if needed, registers a notification if needed and adjusts the level if needed. Make sure the
     * model is set correctly before. Handles also the case where a new scope doesn't match the current level.
     *
     * @param fire  the notifications list
     * @param scope the new scope
     */
    private void setInternalScope( final ArrayList<FireArray> fire, final Taxon scope )
    {
        if ( scope != null && scope != vals.scope )
        {
            fire.add( new FireArray( Scope.name(), vals.scope, scope ) );
            vals.scope = scope;

            final Level[] levels = vals.scope.getSubLevels();
            if ( !ArrayUtils.contains( levels, vals.level ) )
            {
                final Level level = levels[levels.length - 1];
                fire.add( new FireArray( Level.name(), vals.level, level ) );
                vals.level = level;
            }
        }
    }

    /**
     * Sets the level if needed and registers a notification if needed. Make sure model and scope is set correctly
     * before.
     *
     * @param fire  the notifications list
     * @param level
     */
    private void setInternalLevel( ArrayList<FireArray> fire, Level level )
    {
        if ( level != null && level != vals.level )
        {
            fire.add( new FireArray( Level.name(), vals.level, level ) );
            vals.level = level;
        }
    }

    /**
     * Recreates the taxon list. Make sure the model, scope and level have been set correctly before. Handles the case
     * where the scope equals to the focus.
     *
     * @param fire
     */
    private void setInternalTaxList( ArrayList<FireArray> fire )
    {
        Taxon[] newTaxList = vals.scope.getAllChildTaxa( vals.level );
        // Todo: silly workaround b/c the child getter doesn't retrieve the scope.
        if ( vals.scope.getLevel() == vals.level )
        {
            newTaxList = new Taxon[]{vals.scope};
        }
        fire.add( new FireArray( List.name(), taxList, newTaxList ) );
        taxList = newTaxList;
    }

    /**
     * Randomizes the taxon list if the ordered flag is set to true. Otherwise sorts the taxon list. Make sure model,
     * scope, level and taxon list have been set correctly before.
     *
     * @param fire    the notifications list
     * @param ordered whether the taxon list should be ordered
     */
    private void setInternalOrdered( ArrayList<FireArray> fire, boolean ordered )
    {
        fire.add( new FireArray( Ordered.name(), vals.ordered, ordered ) );
        if ( ordered )
        {
            Arrays.sort( taxList );
        }
        else
        {
            RandomUtils.randomize( taxList );
        }
        vals.ordered = ordered;
    }

    /**
     * Sets the focus if needed and registers a notification if needed. Make sure model, scope, level, taxon list and
     * the order has been set correctly before.
     *
     * @param fire  the notifications list
     * @param focus the new focus taxon
     */
    private void setInternalFocus( final ArrayList<FireArray> fire, final Taxon focus )
    {
        if ( focus != null && !focus.equals( vals.focus ) )
        {
            fire.add( new FireArray( Focus.name(), vals.focus, focus ) );
            vals.focus = focus;
        }
    }

    private void setInternalGlobalSubMode( ArrayList<FireArray> fire, SubMode subMode )
    {
        final SubMode oldGlobalSubMode = getGlobalSubMode();
        if ( subMode != null && subMode != oldGlobalSubMode )
        {
            fire.add( new FireArray( SubModus.name(), oldGlobalSubMode, subMode ) );
        }
        subModes.clear();
        Taxon taxon = getFocus();
        // Make sure the root taxon is not included
        while ( taxon.getParentTaxon() != null )
        {
            //fire.add( new FireArray( SubModus.name(), , subMode ) );
            subModes.put( taxon.getName(), subMode );
            taxon = taxon.getParentTaxon();
        }
    }

    public HerbarModel getModel()
    {
        return herbarModel;
    }

    public Taxon[] getTaxList()
    {
        return taxList;
    }

    public Taxon getScope()
    {
        return vals.scope;
    }

    public Level getLevel()
    {
        return vals.level;
    }

    public Taxon getFocus()
    {
        return vals.focus;
    }

    public boolean isOrdered()
    {
        return vals.ordered;
    }

    public SubMode getSubMode( final String taxon )
    {
        return subModes.get( taxon );
    }

    public SubMode getGlobalSubMode()
    {
        SubMode overallSubMode = Lernen;
        for ( final SubMode subMode : subModes.values() )
        {
            if ( subMode == Abfragen )
            {
                overallSubMode = Abfragen;
            }
        }
        return overallSubMode;
    }

    public EditState getEditMode()
    {
        return listState;
    }

    //
    //** Helpers
    //

    public synchronized void addPropertyChangeListener( final String property, final PropertyChangeListener listener )
    {
        propertyChangeSupport.addPropertyChangeListener( property, listener );
    }

    /**
     * Notifies all the registered events.
     *
     * @param fire the notifications list
     */
    private void fireAllPropertyChangeEvents( final ArrayList<FireArray> fire )
    {
        for ( final FireArray f : fire )
        {
            propertyChangeSupport.firePropertyChange( f.name, f.oldVal, f.newVal );
        }
    }

    class TaxStateValues
    {
        /**
         * Holds the value for the property model.
         */
        public HerbarModel model;

        /**
         * Holds value of property scope.
         */
        public Taxon scope;

        /**
         * Holds value of property level.
         */
        public Level level;

        /**
         * Holds value of property focus.
         */
        public Taxon focus;

        /**
         * Holds value of property ordered.
         */
        public boolean ordered;
    }

    public enum TaxState
    {
        List, Scope, Level, Focus, Ordered, SubModus, Model, Edit
    }

    /**
     * States representing the two sub-modes.
     */
    public enum SubMode
    {
        Lernen, Abfragen
    }

    /**
     * Keeps track of list states which either are in edit mode or in use/read mode.
     */
    public enum EditState
    {
        EDIT, USE
    }

    /**
     * Keeps track of generals TaxStateModel changes.
     */
    class FireArray
    {
        public final String name;
        public final Object oldVal;
        public final Object newVal;

        /**
         * @param name   type of the event
         * @param oldVal old state value or Taxon object (for type {@link SubMode SubMode} only)
         * @param newVal new state value
         */
        FireArray( final String name, final Object oldVal, final Object newVal )
        {
            this.name = name;
            this.oldVal = oldVal;
            this.newVal = newVal;
        }
    }
}
