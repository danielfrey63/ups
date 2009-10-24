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
public class PictureDetailTab extends TabbedPictureDetailPanel {

    public PictureDetailTab(CachedImageLocator cache) {
        super(cache);
    }

    public void setEnabled(int t, boolean b) {
        PictureTheme theme = (PictureTheme) getObjectAt(t);
        if (b) {
            this.setTitleAt(t, "<html><body><font color='#000000'>" + theme.getName() + "</font></body></html>");
        }
        else {
            this.setTitleAt(t, "<html><body><font color='#999999'>" + theme.getName() + "</font></body></html>");
        }
    }

    public PictureDetailPanel addTheme(PictureTheme t) {
        return super.addTab(t, t.getName());
    }

    public PictureTheme getTheme() {
        return (PictureTheme) getSelectedObject();
    }

    public PictureDetailPanel getThemePanel(PictureTheme t) {
        return getDetail(getObjectIndex(t));
    }
}
