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
package ch.jfactory.projecttime.stats;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.value.ValueModel;

/**
 * TODO: document
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:56:29 $
 */
public class StatsModel extends Model
{
    public static final String PROPERTYNAME_SUM = "sum";

    private String sum;

    private final ValueModel selectionModel;

    private final PresentationModel currentBeanModel;

    public StatsModel( final ValueModel selectionModel, final PresentationModel currentBeanModel )
    {
        this.selectionModel = selectionModel;
        this.currentBeanModel = currentBeanModel;
    }

    public String getSum()
    {
        return sum;
    }

    public void setSum( final String sum )
    {
        final String old = getSum();
        this.sum = sum;
        firePropertyChange( PROPERTYNAME_SUM, old, sum );
    }

    public ValueModel getSelectionModel()
    {
        return selectionModel;
    }

    public PresentationModel getCurrentBeanModel()
    {
        return currentBeanModel;
    }
}
