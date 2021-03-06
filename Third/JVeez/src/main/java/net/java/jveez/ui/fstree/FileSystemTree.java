/*
 * This project is made available under the terms of the BSD license, more information can be found at : http://www.opensource.org/licenses/bsd-license.html
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * - Neither the name of the java.net website nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright (c) 2004, The JVeez Project Team.
 * All rights reserved.
 */
package net.java.jveez.ui.fstree;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import net.java.jveez.vfs.Directory;

public class FileSystemTree extends JTree
{
    /** Comment for <code>serialVersionUID</code> */
    private static final long serialVersionUID = 3257572789031286832L;

    public FileSystemTree()
    {
        super();
        setModel( new LazyDirectoryTreeModel() );
        setRootVisible( false );
        setCellRenderer( new DirectoryNodeRenderer() );
    }

    private class DirectoryNodeRenderer extends DefaultTreeCellRenderer
    {
        /** Comment for <code>serialVersionUID</code> */
        private static final long serialVersionUID = 3256999952080188472L;

        private final Color normalColor;

        private final Color hiddenColor;

        public DirectoryNodeRenderer()
        {
            normalColor = getForeground();
//      hiddenColor = normalColor.brighter();
            hiddenColor = Color.BLUE;
        }

        public Component getTreeCellRendererComponent( final JTree tree, final Object value, final boolean sel, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus )
        {
            final JLabel label = (JLabel) super.getTreeCellRendererComponent( tree, value, sel, expanded, leaf, row, hasFocus );
            final Directory directory = ( (LazyDirectoryNode) value ).getDirectory();
            if ( directory != null )
            {
                label.setIcon( directory.getIcon() );
                label.setText( directory.getName() );
                label.setForeground( directory.isHidden() ? hiddenColor : normalColor );
            }
            else
            {
                label.setText( "[NULL]" );
            }

            return label;
        }
    }

}
