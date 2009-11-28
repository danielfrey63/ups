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
package ch.xmatrix.ups.pmb.ui.view;

import ch.xmatrix.ups.pmb.domain.SpeciesEntry;
import ch.xmatrix.ups.pmb.ui.model.Settings;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2007/05/16 17:00:16 $
 */
public class SpeciesListRenderer extends DefaultListCellRenderer
{
    private final Settings settings;

    public SpeciesListRenderer( final Settings settings )
    {
        this.settings = settings;
    }

    @Override
    public Component getListCellRendererComponent( final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus )
    {
        final JLabel label = (JLabel) super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
        final SpeciesEntry speciesEntry = (SpeciesEntry) value;
        label.setText( settings.getCleanedSpeciesName( speciesEntry ) );
        return label;
    }
}
