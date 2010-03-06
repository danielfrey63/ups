package ch.jfactory.help;

import java.awt.Font;
import javax.help.Map;
import javax.help.plaf.basic.BasicSearchCellRenderer;
import javax.swing.JLabel;

/**
 * Resolves Suns bug 4788532
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class PatchedBasicSearchCellRenderer extends BasicSearchCellRenderer
{
    public PatchedBasicSearchCellRenderer( final Map map )
    {
        super( map );
    }

    public void setFont( final Font font )
    {
        // initialize title variable to avoid NullPointerException in super.setFont
        if ( title == null )
        {
            title = new JLabel();
        }
        super.setFont( font );
    }
}
