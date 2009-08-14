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
package ch.jfactory.typemapper;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueHolder;
import javax.swing.ListModel;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:54:59 $
 */
public class TypeModel extends Model
{

    public static final String PROPERTYNAME_DATAMODEL = "dataModel";

    private ArrayListModel dataModel = new ArrayListModel();

    private PresentationModel editorModel = new PresentationModel(new ValueHolder(null, true));

    private SelectionInList selectionInList = new SelectionInList((ListModel) dataModel);

    public ArrayListModel getDataModel()
    {
        return dataModel;
    }

    public void setDataModel(final ArrayListModel dataModel)
    {
        final ArrayListModel old = getDataModel();
        this.dataModel = dataModel;
        this.selectionInList = new SelectionInList((ListModel) dataModel);
        firePropertyChange(PROPERTYNAME_DATAMODEL, old, dataModel);
    }

    public PresentationModel getEditorModel()
    {
        return editorModel;
    }

    public SelectionInList getSelectionInList()
    {
        return selectionInList;
    }
}
