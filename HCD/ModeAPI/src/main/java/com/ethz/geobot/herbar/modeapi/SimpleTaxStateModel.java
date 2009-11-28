package com.ethz.geobot.herbar.modeapi;

import ch.jfactory.lang.ArrayUtils;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import java.beans.PropertyChangeSupport;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class SimpleTaxStateModel
{
    public final static String SCOPE = "scope";

    public final static String LEVEL = "level";

    public final static String FOCUS = "focus";

    public final static String ORDER = "ordered";

    /**
     * Holds value of property scope.
     */
    private Taxon scope;

    /**
     * Holds value of property level.
     */
    private Level level;

    /**
     * Holds value of property focus.
     */
    private Taxon focus;

    /**
     * Holds value of property ordered.
     */
    private boolean ordered;

    private PropertyChangeSupport propertySupport;

    public SimpleTaxStateModel( final PropertyChangeSupport propertyChangeSupport )
    {
        this.propertySupport = propertyChangeSupport;
    }

    /**
     * Gets the scope state.
     *
     * @return the current scope taxon
     */
    public Taxon getScope()
    {
        return this.scope;
    }

    /**
     * Sets the scope state.
     *
     * @param scope the new scope to set
     */
    public void setScope( final Taxon scope )
    {
        final Taxon oldScope = this.scope;
        this.scope = scope;
        validateLevel();
        propertySupport.firePropertyChange( SCOPE, oldScope, scope );
    }

    /**
     * Gets the level state.
     *
     * @return the current level.
     */
    public Level getLevel()
    {
        return this.level;
    }

    /**
     * Sets the level state.
     *
     * @param level the new level to set
     */
    public void setLevel( final Level level )
    {
        final Level oldLevel = this.level;
        this.level = level;
        validateFocus();
        propertySupport.firePropertyChange( LEVEL, oldLevel, level );
    }

    /**
     * Gets the focus state.
     *
     * @return the current focus state taxon.
     */
    public Taxon getFocus()
    {
        return this.focus;
    }

    /**
     * Sets the focus state.
     *
     * @param focus the new focus state to set.
     */
    public void setFocus( final Taxon focus )
    {
        final Taxon oldFocus = this.focus;
        this.focus = focus;
        propertySupport.firePropertyChange( FOCUS, oldFocus, focus );
    }

    /**
     * Gets the ordered property.
     *
     * @return whether the state currently is ordered
     */
    public boolean isOrdered()
    {
        return this.ordered;
    }

    /**
     * Sets the ordered property.
     *
     * @param ordered whether state is ordered.
     */
    public void setOrdered( final boolean ordered )
    {
        final boolean oldOrdered = this.ordered;
        this.ordered = ordered;
        propertySupport.firePropertyChange( ORDER, new Boolean( oldOrdered ), new Boolean( ordered ) );
    }

    private void validateLevel()
    {
        final Level[] levels = scope.getSubLevels();
        if ( !ArrayUtils.contains( levels, level ) )
        {
            setLevel( levels[levels.length - 1] );
        }
        validateFocus();
    }

    private void validateFocus()
    {
        Taxon[] taxa = scope.getAllChildTaxa( level );
        taxa = (Taxon[]) ArrayUtils.insert( taxa, scope, 0, new Taxon[0] );
        if ( !ArrayUtils.contains( taxa, focus ) )
        {
            setFocus( taxa[0] );
        }
    }

    public void setPropertyChangeSupport( final PropertyChangeSupport propertySupport )
    {
        this.propertySupport = propertySupport;
    }
}

