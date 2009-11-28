/* ====================================================================
 *  Copyright 2004 www.jfactory.ch
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.
 * ====================================================================
 */
package ch.jfactory.searchtree;

import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JToolTip;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;

/**
 * The Search Tree componenten
 *
 * @author <a href="mail@wegmueller.com">Thomas Wegmueller</a>
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
public class JSearchTree
{
    private String tipText = "";

    private JToolTip toolTip;

    private SearchPopup tipWindow;

    private TreePath preSearchSelection;

    private ITreeSearcher searcher;

    private boolean shown = false;

    private static KeyAdapter keyL;

    private final JTree tree;

    private String prefixText = "";

    public static JSearchTree decorate( final JTree tree, final ITreeSearcher s, final String prefix )
    {
        final JSearchTree st = new JSearchTree( tree, s );
        st.setPrefixText( prefix );
        return st;
    }

    public static JSearchTree decorate( final JTree tree, final boolean starting, final boolean ignoreCase, final String prefix )
    {
        final TreeSearcher s = new TreeSearcher( tree, starting, ignoreCase );
        final JSearchTree st = new JSearchTree( tree, s );
        st.setPrefixText( prefix );
        return st;
    }

    public void undecorate()
    {
        hideTip();
        tree.removeKeyListener( keyL );
    }

    private JSearchTree( final JTree tree, final ITreeSearcher s )
    {
        this.tree = tree;
        searcher = s;
        keyL = ( new KeyAdapter()
        {
            public void keyPressed( final KeyEvent e )
            {
                manageKey( e );
            }

            public void keyTyped( final KeyEvent e )
            {
                e.consume();
            }

            public void keyReleased( final KeyEvent e )
            {
                e.consume();
            }
        } );
        tree.addKeyListener( keyL );
    }

    public void setSearcher( final ITreeSearcher searcher )
    {
        this.searcher = searcher;
    }

    private void setSearchText( final String s )
    {
        tipText = s;
        if ( toolTip != null )
        {
            toolTip.setTipText( prefixText + s );
            SwingUtilities.invokeLater( new Runnable()
            {
                public void run()
                {
                    toolTip.setSize( toolTip.getPreferredSize() );
                    tipWindow.pack();
                }
            } );
        }

        final TreePath selection = searcher.setSearchString( tipText );
        select( selection );
    }

    private void manageKey( final KeyEvent e )
    {
        final char ch = e.getKeyChar();
        final int keyCode = e.getKeyCode();
        if ( !shown )
        {
            if ( isPrintableDefined( ch ) )
            {
                showTip( "" + ch );
                e.consume();
            }
        }
        else
        {
            if ( keyCode == KeyEvent.VK_ESCAPE )
            {
                hideTip();
                select( preSearchSelection );
            }
            else if ( keyCode == KeyEvent.VK_ENTER )
            {
                hideTip();
            }
            else if ( keyCode == KeyEvent.VK_DOWN )
            {
                select( searcher.next() );
            }
            else if ( keyCode == KeyEvent.VK_LEFT )
            {
                tree.collapsePath( tree.getSelectionPath() );
            }
            else if ( keyCode == KeyEvent.VK_RIGHT )
            {
                tree.expandPath( tree.getSelectionPath() );
            }
            else if ( keyCode == KeyEvent.VK_UP )
            {
                select( searcher.prev() );
            }
            else if ( keyCode == KeyEvent.VK_PAGE_DOWN )
            {
                select( searcher.next() );
            }
            else if ( keyCode == KeyEvent.VK_PAGE_UP )
            {
                select( searcher.prev() );
            }
            else if ( keyCode == KeyEvent.VK_BACK_SPACE )
            {
                if ( tipText.length() <= 0 )
                {
                    hideTip();
                    select( preSearchSelection );
                }
                else
                {
                    setSearchText( tipText.substring( 0, tipText.length() - 1 ) );
                }
            }
            else if ( isPrintableDefined( ch ) )
            {
                setSearchText( tipText + e.getKeyChar() );
            }
            e.consume();
        }
    }

    private boolean isPrintableDefined( final char ch )
    {
        final int type = Character.getType( ch );
        if ( type == Character.UPPERCASE_LETTER )
        {
            return true;
        }
        if ( type == Character.OTHER_LETTER )
        {
            return true;
        }
        if ( type == Character.OTHER_NUMBER )
        {
            return true;
        }
        if ( type == Character.OTHER_PUNCTUATION )
        {
            return true;
        }
        if ( type == Character.OTHER_SYMBOL )
        {
            return true;
        }
        if ( type == Character.SPACE_SEPARATOR )
        {
            return true;
        }
        if ( type == Character.START_PUNCTUATION )
        {
            return true;
        }
        if ( type == Character.TITLECASE_LETTER )
        {
            return true;
        }
        if ( type == Character.CURRENCY_SYMBOL )
        {
            return true;
        }
        if ( type == Character.DASH_PUNCTUATION )
        {
            return true;
        }
        if ( type == Character.DECIMAL_DIGIT_NUMBER )
        {
            return true;
        }
        if ( type == Character.ENCLOSING_MARK )
        {
            return true;
        }
        if ( type == Character.END_PUNCTUATION )
        {
            return true;
        }
        if ( type == Character.FINAL_QUOTE_PUNCTUATION )
        {
            return true;
        }
        if ( type == Character.FORMAT )
        {
            return true;
        }
        if ( type == Character.INITIAL_QUOTE_PUNCTUATION )
        {
            return true;
        }
        if ( type == Character.LETTER_NUMBER )
        {
            return true;
        }
        if ( type == Character.LINE_SEPARATOR )
        {
            return true;
        }
        if ( type == Character.LOWERCASE_LETTER )
        {
            return true;
        }
        if ( type == Character.MATH_SYMBOL )
        {
            return true;
        }
        if ( type == Character.MODIFIER_LETTER )
        {
            return true;
        }
        if ( type == Character.MODIFIER_SYMBOL )
        {
            return true;
        }

        /*
        if (type==Character.COMBINING_SPACING_MARK) return false;
        if (type==Character.CONNECTOR_PUNCTUATION) return false;
        if (type==Character.CONTROL) return false;
        if (type==Character.NON_SPACING_MARK) return false;
        if (type==Character.PARAGRAPH_SEPARATOR) return false;
        if (type==Character.PRIVATE_USE) return false;
        if (type==Character.SURROGATE) return false;
        if (type==Character.UNASSIGNED) return false;
        */
        return false;
    }

    public void showTip( final String ch )
    {
        shown = true;
        final String tipText = "" + ch;
        preSearchSelection = tree.getSelectionPath();
        setSearchText( tipText );
        toolTip = tree.createToolTip();
        toolTip.setTipText( prefixText + tipText );
        toolTip.setVisible( true );
        final Point p = new Point( tree.getVisibleRect().x, tree.getVisibleRect().y );
        SwingUtilities.convertPointToScreen( p, tree );
        tipWindow = new SearchPopup( tree, toolTip, p.x, p.y );
        tipWindow.show();
        toolTip.addComponentListener( new ComponentAdapter()
        {
            public void componentHidden( final ComponentEvent e )
            {
                hideTip();
            }
        } );
        toolTip.addFocusListener( new FocusAdapter()
        {
            public void focusLost( final FocusEvent e )
            {
                hideTip();
            }
        } );
        toolTip.requestFocus( true );
        toolTip.addKeyListener( keyL );

    }

    public void hideTip()
    {
        if ( tipWindow != null )
        {
            tipWindow.hide();
        }
        tipWindow = null;
        shown = false;

    }

    private void select( final TreePath path )
    {
        if ( path == null )
        {
            return;
        }
        SwingUtilities.invokeLater( new Runnable()
        {
            public void run()
            {
                tree.setSelectionPath( path );
                //tree.expandPath(path);
                tree.scrollPathToVisible( path );
                tree.repaint();
            }
        } );

    }

    public String getPrefixText()
    {
        return prefixText;
    }

    public void setPrefixText( final String prefixText )
    {
        this.prefixText = prefixText;
    }

}