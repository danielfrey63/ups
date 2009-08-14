/*
 * Herbar CD-ROM version 2
 *
 * PicturePanel.java
 *
 * Created on 30. April 2002
 * Created by dirk
 */
package ch.jfactory.image;

import ch.jfactory.component.tab.NiceTabbedPane;
import ch.jfactory.resource.CachedImageLocator;
import ch.jfactory.resource.PictureCache;
import java.util.ArrayList;

public class TabbedPictureDetailPanel extends NiceTabbedPane {
    private PictureCache cache;
    private ArrayList list = new ArrayList();

    public TabbedPictureDetailPanel(final CachedImageLocator locator) {
        this.cache = new PictureCache(locator);
    }

    public void clearCachingList() {
        cache.clearCachingList();
    }

    public void cacheImage(final String name, final boolean thumb) {
        cache.cacheImage(name, thumb, false);
    }

    public PictureDetailPanel getDetail(final int i) {
        return (PictureDetailPanel) getComponentAt(i);
    }

    public PictureDetailPanel getDetail(final String str) {
        for (int i = 0; i < list.size(); i++) {
            if (getTitleAt(i).equals(str)) return getDetail(i);
        }
        return null;
    }

    public PictureDetailPanel addTab(final Object o, final String s) {
        final PictureDetailPanel panel = new PictureDetailPanel(cache);
        this.add(panel, s);
        list.add(o);
        return panel;
    }

    public Object getObjectAt(final int i) {
        return list.get(i);
    }

    public Object getSelectedObject() {
        return getObjectAt(getSelectedIndex());
    }

    public int getObjectIndex(final Object o) {
        for (int i = 0; i < list.size(); i++) {
            if (o.equals(getObjectAt(i))) return i;
        }
        return -1;
    }

    public int countTabs() {
        return getComponentCount();
    }

    public void clearAll() {
        cache.clearCachingList();
        for (int i = 0; i < list.size(); i++) {
            getDetail(i).clear();
        }
    }
}
