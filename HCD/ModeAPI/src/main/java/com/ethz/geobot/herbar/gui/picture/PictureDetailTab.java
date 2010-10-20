package com.ethz.geobot.herbar.gui.picture;

import ch.jfactory.image.PictureDetailPanel;
import ch.jfactory.image.TabbedPictureDetailPanel;
import ch.jfactory.resource.CachedImageLocator;
import com.ethz.geobot.herbar.model.PictureTheme;

/**
 * This control is used to display the Pictures of the different Picture-Themes.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class PictureDetailTab extends TabbedPictureDetailPanel
{
    public PictureDetailTab( final CachedImageLocator cache )
    {
        super( cache );
    }

    public void setEnabled( final int t, final boolean b )
    {
        final PictureTheme theme = (PictureTheme) getObjectAt( t );
        final String color = b ? "000000" : "999999";
        this.setTitleAt( t, "<html><body><font color='#" + color + "'>" + theme.getName() + "</font></body></html>" );
    }

    public PictureTheme getTheme()
    {
        return (PictureTheme) getSelectedObject();
    }

    public PictureDetailPanel getThemePanel( final PictureTheme t )
    {
        return getDetail( getObjectIndex( t ) );
    }
}
