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
package ch.xmatrix.ups.uec.level;

import ch.jfactory.resource.Strings;
import ch.xmatrix.ups.domain.SimpleLevel;
import com.jgoodies.binding.list.SelectionInList;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2006/04/17 23:29:42 $
 */
public class LevelTableModel extends AbstractTableModel
{

    private final ArrayList<String> list = new ArrayList<String>();

    private final SelectionInList models;

    private final String[] columnNames = new String[]{
            Strings.getString("levels.taxon.level.name"),
            Strings.getString("levels.taxon.maximum.name")
    };

    public LevelTableModel(final SelectionInList models)
    {
        this.models = models;
        setModel(null);
    }

    public void setModel(final SimpleLevel rootLevel)
    {
        list.clear();
        SimpleLevel level = rootLevel;
        while (level != null)
        {
            list.add(level.getName());
            level = level.getChildLevel();
        }
        fireTableDataChanged();
    }

    public int getColumnCount()
    {
        return 2;
    }

    public int getRowCount()
    {
        return list.size();
    }

    public boolean isCellEditable(final int rowIndex, final int columnIndex)
    {
        return columnIndex == 1;
    }

    public Class getColumnClass(final int columnIndex)
    {
        return getValueAt(0, columnIndex).getClass();
    }

    public Object getValueAt(final int rowIndex, final int columnIndex)
    {
        final String level = list.get(rowIndex);
        if (columnIndex == 0)
        {
            return level;
        }
        else
        {
            final LevelModel result = getLevelModel(level);
            return new Integer(result == null ? 0 : result.getMaximum());
        }
    }

    public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex)
    {
        final LevelsModel levels = (LevelsModel) models.getSelection();
        final ArrayList<LevelModel> levelModels = levels.getLevelModels();
        final String level = list.get(rowIndex);
        LevelModel levelModel = getLevelModel(level);
        final int newValue;
        if (aValue != null)
        {
            newValue = ((Integer) aValue).intValue();
            if (newValue != 0 && levelModel == null)
            {
                levelModel = new LevelModel(level, 0);
                levelModels.add(levelModel);
            }
            else if (newValue == 0 && levelModel != null)
            {
                levelModels.remove(levelModel);
            }
            levelModel.setMaximum(newValue);
        }
        else
        {
            levelModels.remove(levelModel);
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public String getColumnName(final int columnIndex)
    {
        return columnNames[columnIndex];
    }

    private LevelModel getLevelModel(final String level)
    {
        final LevelsModel levels = (LevelsModel) models.getSelection();
        LevelModel result = null;
        if (levels != null)
        {
            final List<LevelModel> model = levels.getLevelModels();
            for (int i = 0; model != null && i < model.size(); i++)
            {
                final LevelModel levelModel = model.get(i);
                if (levelModel.getLevel().equals(level))
                {
                    result = levelModel;
                }
            }
        }
        return result;
    }
}
