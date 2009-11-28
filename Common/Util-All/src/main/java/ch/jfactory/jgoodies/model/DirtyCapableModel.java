/* ====================================================================
 *  Copyright 2004-2005 www.xmatrix.ch
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.jfactory.jgoodies.model;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.6 $ $Date: 2006/03/14 21:27:55 $
 */
public class DirtyCapableModel extends Model implements DirtyCapable
{
    public static final String PROPERTYNAME_DIRTY = DirtyCapableModel.class.getName() + ".dirty";

    /**
     * Dummy value model for initialization.
     */
    public static final ValueModel DEFAULT_VALUEMODEL = new ValueHolder();

    private transient boolean dirty = false;

    private transient List subModels = new ArrayList();

    private transient Set superModels = new HashSet();

    public void setDirty( final boolean dirty )
    {
        final boolean old = isDirty();
        this.dirty = dirty;
        if ( dirty )
        {
            for ( final Object superModel : superModels )
            {
                final DirtyCapable model = (DirtyCapable) superModel;
                model.setDirty( dirty );
            }
        }
        else
        {
            for ( int i = 0; subModels != null && i < subModels.size(); i++ )
            {
                final DirtyCapable model = (DirtyCapable) subModels.get( i );
                model.setDirty( dirty );
            }
        }
        firePropertyChange( DirtyCapableModel.PROPERTYNAME_DIRTY, old, dirty );
    }

    public boolean isDirty()
    {
        return dirty;
    }

    /**
     * Registers a dirty listener in the submodel.
     *
     * @param subModel
     */
    protected void addSubModel( final DirtyCapableModel subModel )
    {
        if ( superModels == null )
        {
            superModels = new HashSet();
        }
        subModels.add( subModel );
        subModel.addSuperModel( this );
    }

    private void addSuperModel( final DirtyCapable superModel )
    {
        if ( superModels == null )
        {
            superModels = new HashSet();
        }
        superModels.add( superModel );
    }

    /**
     * @param model
     */
    public void removeSubModel( final DirtyCapableModel model )
    {
        if ( subModels == null )
        {
            subModels = new ArrayList();
        }
        subModels.remove( model );
    }

    /**
     * Removes the dirty PropertyChangeListener from the old model if it is not the default model. Adds the same
     * listener to the new model if it is not null.
     *
     * @param oldModel the old DirtyCapableModel
     * @param newModel the new DirtyCapableModel
     * @return returns the model set
     */
    protected DirtyCapable handleSubModel( final DirtyCapableModel oldModel, final DirtyCapableModel newModel )
    {
        removeSubModel( oldModel );
        addSubModel( newModel );

        return newModel;
    }

    /**
     * Fires a property change event and sets the dirty flag to true.
     *
     * @param property the property to fire for
     * @param oldValue the oldValue value
     * @param newValue the new value
     */
    protected void fireTouchedPropertyChange( final String property, final Object oldValue, final Object newValue )
    {
        firePropertyChange( property, oldValue, newValue );
    }

    protected void fireTouchedPropertyChange( final String property, final boolean oldValue, final boolean newValue )
    {
        firePropertyChange( property, oldValue, newValue );
    }

    protected class AndingDirtyListener implements PropertyChangeListener
    {
        private final DirtyCapable[] dirtyCapableModels;

        public AndingDirtyListener( final DirtyCapable[] dirtyCapableModels )
        {
            this.dirtyCapableModels = dirtyCapableModels;
        }

        public void propertyChange( final PropertyChangeEvent evt )
        {
            boolean dirty = false;
            for ( final DirtyCapable dirtyCapable : dirtyCapableModels )
            {
                dirty |= dirtyCapable.isDirty();
            }
            setDirty( dirty );
        }
    }
}
