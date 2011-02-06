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

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import net.java.jveez.ui.thumbnails.PictureListCellRenderer;
import net.java.jveez.vfs.Picture;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2007/05/16 17:00:16 $
 */
public class PictureListRenderer extends PictureListCellRenderer
{
    private final JPanel panel = new JPanel( new BorderLayout() );

    private final JLabel text = new JLabel();

    public PictureListRenderer()
    {
        text.setOpaque( true );
        text.setHorizontalTextPosition( JLabel.CENTER );
        text.setHorizontalAlignment( JLabel.CENTER );
        panel.add( text, BorderLayout.SOUTH );
        panel.setOpaque( false );
    }

    @Override
    public Component getListCellRendererComponent( final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus )
    {
        final JLabel image = (JLabel) super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
        panel.add( image, BorderLayout.CENTER );
        final Picture entry = (Picture) value;
        text.setText( entry.getName() );
        text.setBackground( isSelected ? list.getSelectionBackground() : list.getBackground() );
        text.setForeground( isSelected ? list.getSelectionForeground() : list.getForeground() );
        return panel;
    }
}
