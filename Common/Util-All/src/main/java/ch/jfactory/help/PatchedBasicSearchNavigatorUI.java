package ch.jfactory.help;

import javax.help.JHelpSearchNavigator;
import javax.help.NavigatorView;
import javax.help.plaf.basic.BasicSearchNavigatorUI;
import javax.swing.JComponent;
import javax.swing.JTree;

/**
 * Resolves Suns bug 4788532
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
public class PatchedBasicSearchNavigatorUI extends BasicSearchNavigatorUI
{
    public static javax.swing.plaf.ComponentUI createUI( final JComponent x )
    {
        return new PatchedBasicSearchNavigatorUI( (JHelpSearchNavigator) x );
    }

    public PatchedBasicSearchNavigatorUI( final JHelpSearchNavigator b )
    {
        super( b );
    }

    protected void setCellRenderer( final NavigatorView view, final JTree tree )
    {
        if ( view == null )
        {
            return;
        }
        final javax.help.Map map = view.getHelpSet().getCombinedMap();
        tree.setCellRenderer( new PatchedBasicSearchCellRenderer( map ) );
    }
}
