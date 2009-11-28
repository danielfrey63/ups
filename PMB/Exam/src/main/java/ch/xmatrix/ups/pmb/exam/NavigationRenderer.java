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
package ch.xmatrix.ups.pmb.exam;

import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2007/05/16 17:00:16 $
 */
public class NavigationRenderer extends DefaultTreeCellRenderer
{
    public NavigationRenderer()
    {
        super();
        final ImageIcon icon = null; //new ImageIcon(MainForm.class.getResource("/18x18/arrow.png"));
        setOpenIcon( icon );
        setClosedIcon( icon );
        setLeafIcon( icon );
    }

    @Override
    public Component getTreeCellRendererComponent( final JTree tree, final Object value, final boolean sel, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus )
    {
        final JLabel label = (JLabel) super.getTreeCellRendererComponent( tree, value, sel, expanded, leaf, row, hasFocus );
        label.setOpaque( true );
        label.setForeground( selected ? Color.orange : tree.getForeground() );
        return label;
    }
}
