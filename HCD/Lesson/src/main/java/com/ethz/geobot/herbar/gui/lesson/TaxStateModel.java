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
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.EditState.MODIFY;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.EditState.USE;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.SubMode.ABFRAGEN;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.SubMode.LERNEN;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.COLLAPSE;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.EDIT;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.FOCUS;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.LEVEL;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.LIST;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.ORDER;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.RENAME;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.SCOPE;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.SUB_MODUS;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.TAXA;
import com.ethz.geobot.herbar.modeapi.HerbarContext;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class TaxStateModel
{
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport( this );

    private final HerbarContext context;

    private final TaxStateValues vals = new TaxStateValues();

    private final HashMap<String, SubMode> subModes = new HashMap<String, SubMode>();

    private Taxon[] taxList;

    private HerbarModel herbarModel;

    private EditState listState = USE;

    public TaxStateModel( final HerbarContext context )
    {
        this.context = context;
        setModel( context.getDataModel() );
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

    public void setCollapse()
    {
        final ArrayList<FireArray> fire = new ArrayList<FireArray>();
        fire.add( new FireArray( COLLAPSE.name(), false, true ) );
        fireAllPropertyChangeEvents( fire );
    }

    public void setNewModel( final HerbarModel model )
    {
        if ( model != null )
        {
            final ArrayList<FireArray> fire = new ArrayList<FireArray>();
            setInternalModel( fire, model );
            setInternalScope( fire, model.getRootTaxon() );
            setInternalLevel( fire, vals.level );
            setInternalGlobalSubMode( fire, LERNEN );
            setInternalEditMode( fire, MODIFY );
            fireAllPropertyChangeEvents( fire );
        }
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
            setInternalGlobalSubMode( fire, LERNEN );
            setInternalEditMode( fire, USE );
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

    public void setLevel( final Taxon taxon )
    {
        if ( taxon != null && taxon.getLevel() != vals.level )
        {
            final ArrayList<FireArray> fire = new ArrayList<FireArray>();
            setInternalLevel( fire, taxon.getLevel() );
            setInternalTaxList( fire );
            setInternalOrdered( fire, vals.ordered );
            setInternalFocus( fire, taxon );
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
        fire.add( new FireArray( SUB_MODUS.name(), oldGlobalSubMode, newGlobalSubMode ) );
        fireAllPropertyChangeEvents( fire );
    }

    public void setEditMode( final EditState mode )
    {
        final ArrayList<FireArray> fire = new ArrayList<FireArray>();
        setInternalGlobalSubMode( fire, LERNEN );
        setInternalEditMode( fire, mode );
        final Level[] levels = getModel().getLevels();
        setInternalLevel( fire, ArrayUtils.contains( levels, vals.level ) ? vals.level : levels[levels.length - 1] );
        setInternalTaxList( fire );
        setInternalFocus( fire, taxList[0] );
        fireAllPropertyChangeEvents( fire );
    }

    public void setName( final String name )
    {
        final ArrayList<FireArray> fire = new ArrayList<FireArray>();
        setInternalName( fire, name );
        fireAllPropertyChangeEvents( fire );
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
            fire.add( new FireArray( LIST.name(), herbarModel, model ) );
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
            fire.add( new FireArray( SCOPE.name(), vals.scope, scope ) );
            vals.scope = scope;

            final Level[] levels = vals.scope.getSubLevels();
            if ( !ArrayUtils.contains( levels, vals.level ) )
            {
                final Level level = levels.length == 0 ? scope.getLevel() : levels[levels.length - 1];
                fire.add( new FireArray( LEVEL.name(), vals.level, level ) );
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
    private void setInternalLevel( final ArrayList<FireArray> fire, final Level level )
    {
        if ( level != null && level != vals.level )
        {
            fire.add( new FireArray( LEVEL.name(), vals.level, level ) );
            vals.level = level;
        }
    }

    /**
     * Recreates the taxon list. Make sure the model, scope and level have been set correctly before. Handles the case
     * where the scope equals to the focus.
     *
     * @param fire
     */
    private void setInternalTaxList( final ArrayList<FireArray> fire )
    {
        Taxon[] newTaxList = vals.scope.getAllChildTaxa( vals.level );
        // Todo: silly workaround b/c the child getter doesn't retrieve the scope.
        if ( vals.scope.getLevel() == vals.level )
        {
            newTaxList = new Taxon[]{vals.scope};
        }
        fire.add( new FireArray( TAXA.name(), taxList, newTaxList ) );
        taxList = newTaxList;
    }

    /**
     * Randomizes the taxon list if the ordered flag is set to true. Otherwise sorts the taxon list. Make sure model,
     * scope, level and taxon list have been set correctly before.
     *
     * @param fire    the notifications list
     * @param ordered whether the taxon list should be ordered
     */
    private void setInternalOrdered( final ArrayList<FireArray> fire, final boolean ordered )
    {
        fire.add( new FireArray( ORDER.name(), vals.ordered, ordered ) );
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
        if ( focus != null && focus != vals.focus )
        {
            fire.add( new FireArray( FOCUS.name(), vals.focus, focus ) );
            vals.focus = focus;
        }
    }

    private void setInternalGlobalSubMode( final ArrayList<FireArray> fire, final SubMode subMode )
    {
        final SubMode oldGlobalSubMode = getGlobalSubMode();
        if ( subMode != null && subMode != oldGlobalSubMode )
        {
            fire.add( new FireArray( SUB_MODUS.name(), oldGlobalSubMode, subMode ) );
        }
        subModes.clear();
        Taxon taxon = getFocus();
        // Make sure the root taxon is not included
        while ( taxon.getParentTaxon() != null )
        {
            subModes.put( taxon.getName(), subMode );
            taxon = taxon.getParentTaxon();
        }
    }

    private void setInternalEditMode( final ArrayList<FireArray> fire, final EditState mode )
    {
        EditState oldMode = listState;
        listState = mode;
        fire.add( new FireArray( EDIT.name(), oldMode, mode ) );
    }

    private void setInternalName( final ArrayList<FireArray> fire, final String name )
    {
        final String oldName = getModel().getName();
        getModel().setName( name );
        context.saveModel( getModel() );
        fire.add( new FireArray( RENAME.name(), oldName, name ) );

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
        SubMode overallSubMode = LERNEN;
        for ( final SubMode subMode : subModes.values() )
        {
            if ( subMode == ABFRAGEN )
            {
                overallSubMode = ABFRAGEN;
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
        TAXA, SCOPE, LEVEL, FOCUS, ORDER, SUB_MODUS, LIST, EDIT, RENAME, COLLAPSE
    }

    /**
     * States representing the two sub-modes.
     */
    public enum SubMode
    {
        LERNEN
                {
                    @Override
                    public String toString()
                    {
                        return "Lernen";
                    }
                },
        ABFRAGEN
                {
                    @Override
                    public String toString()
                    {
                        return "Abfragen";
                    }
                }
    }

    /**
     * Keeps track of list states which either are in edit mode or in use (=read) mode.
     */
    public enum EditState
    {
        MODIFY, USE
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

        @Override
        public String toString()
        {
            return "FireArray[" + name + "," + oldVal + "," + newVal + "]";
        }
    }
}
