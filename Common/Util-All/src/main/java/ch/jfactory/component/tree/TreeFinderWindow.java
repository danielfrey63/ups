/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.component.tree;

import ch.jfactory.component.FinderWindow;
import ch.jfactory.component.SimpleDocumentListener;
import ch.jfactory.resource.Strings;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.tree.TreePath;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.3 $ $Date: 2006/03/14 21:27:55 $
 */
public class TreeFinderWindow implements TreeFinder
{
    private static final String KEY_PREFIX = "FINDER.WINDOW.PREFIX";

    private FinderWindow finderWindow;

    private TreeFinderModel treeFinderModel;

    private JTree tree;

    public TreeFinderWindow( final JTree tree )
    {
        setTree( tree );
    }

    public void setTree( final JTree tree )
    {
        finderWindow = new FinderWindow( tree, Strings.getString( KEY_PREFIX ) + " " );
        this.tree = tree;
        init();
    }

    private void init()
    {
        treeFinderModel = new TreeFinderModel( tree.getModel() );
        finderWindow.setDocument( new TreeFinderDocument( treeFinderModel ) );
        finderWindow.addDocumentListener( new SimpleDocumentListener()
        {
            public void changedUpdate( final DocumentEvent e )
            {
                setSelection( treeFinderModel.get() );
            }
        } );
        finderWindow.setNextAction( new AbstractAction()
        {
            public void actionPerformed( final ActionEvent e )
            {
                if ( treeFinderModel.hasNext() )
                {
                    treeFinderModel.next();
                    setSelection( treeFinderModel.get() );
                }
                else
                {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        } );
        finderWindow.setPrevAction( new AbstractAction()
        {
            public void actionPerformed( final ActionEvent e )
            {
                if ( treeFinderModel.hasPrevious() )
                {
                    treeFinderModel.previous();
                    setSelection( treeFinderModel.get() );
                }
                else
                {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        } );
    }

    public void setSelection( final TreePath tp )
    {
        tree.setSelectionPath( tp );
        final int iVis = tree.getVisibleRowCount() / 2;
        final int iRow = tree.getRowForPath( tp );
        final int iRowMax = Math.min( iRow + iVis, tree.getRowCount() - 1 );
        final int iRowMin = Math.max( 0, iRow - iVis );
        tree.scrollRowToVisible( iRowMin );
        tree.scrollRowToVisible( iRowMax );
    }

    public static void main( final String[] args )
    {
        final JFrame f = new JFrame( "Tree Finder Test" );
        f.addWindowListener( new WindowAdapter()
        {
            public void windowClosing( final WindowEvent e )
            {
                System.exit( 0 );
            }
        } );
        final Container content = f.getContentPane();
        final JTree tree = new JTree();
        new TreeFinderWindow( tree );
        final JPanel panel = new JPanel( new BorderLayout() );
        panel.setBorder( new EmptyBorder( 20, 20, 20, 20 ) );
        panel.add( new JScrollPane( tree ), BorderLayout.CENTER );
        content.setLayout( new BorderLayout() );
        content.add( panel, BorderLayout.CENTER );
        f.setSize( 200, 200 );
        f.setVisible( true );
    }
}
