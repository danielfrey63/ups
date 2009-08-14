/*
 * Herbar CD-ROM version 2
 *
 * PicturePanel.java
 *
 * Created on 30. April 2002
 * Created by dirk
 */
package ch.jfactory.image;


import ch.jfactory.component.ScrollerPanel;
import ch.jfactory.resource.CachedImage;
import ch.jfactory.resource.CachedImageComponent;
import ch.jfactory.resource.PictureCache;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import org.apache.log4j.Category;

public class ThumbNailPanel extends ScrollerPanel {
    private final static int THUMBHEIGHT = 50;
    private static final Category CAT = Category.getInstance(ThumbNailPanel.class);
    private JPanel panel;
    private PictureCache cache;
    private int size = 0;
    private ArrayList listener;

    public ThumbNailPanel(final PictureCache cache) {
        //this.setVerticalScrollBarPolicy(this.VERTICAL_SCROLLBAR_NEVER);
        panel = this;
        this.cache = cache;
        initGUI();
    }

    public Dimension getPreferredSize() {
        int w = panel.getComponentCount() * THUMBHEIGHT;
        int h = THUMBHEIGHT;
        Insets i = getInsets();
        w += i.right + i.left;
        h += i.top + i.top;
        i = CachedImageComponent.BORDER.getBorderInsets(null);
        w += i.right + i.left;
        h += i.top + i.bottom;
        return new Dimension(w, h);
    }

    public Dimension getMinimumSize() {
        int w = THUMBHEIGHT + btnNext.getPreferredSize().width + btnPrev.getPreferredSize().width;
        int h = THUMBHEIGHT;
        Insets i = getInsets();
        w += i.right + i.left;
        h += i.top + i.top;
        i = CachedImageComponent.BORDER.getBorderInsets(null);
        w += i.right + i.left;
        h += i.top + i.bottom;
        return new Dimension(w, h);
    }

    public void initGUI() {
        //panel = new JPanel(new FlowLayout());
        //this.setViewportView(panel);
    }

    public int imageCount() {
        return panel.getComponentCount();
    }

    public CachedImageComponent getComponentAt(final int i) {
        return (CachedImageComponent) panel.getComponent(i);
    }

    public CachedImage getImageAt(final int i) {
        final CachedImageComponent ci = (CachedImageComponent) panel.getComponent(i);
        return ci.getImage();
    }

    public void addImage(final String c, final String tooltip, final boolean revalidate) {
        for (int i = 0; i < panel.getComponentCount(); i++) {
            if (!panel.getComponent(i).isVisible()) {
                final CachedImageComponent ci = (CachedImageComponent) panel.getComponent(i);
                ci.setImage(c, true);
                ci.setVisible(true);
                ci.setToolTipText(tooltip);
                if (revalidate) {
                    revalidate();
                }
                return;
            }
        }
        final CachedImageComponent ci = new CachedImageComponent(cache, THUMBHEIGHT);
        ci.addMouseListener(new MouseAdapter() {
            public void mouseReleased(final MouseEvent e) {
                fireActionEvent((CachedImageComponent) e.getSource());
                e.consume();
            }
        });
        panel.add(ci);
        ci.setImage(c, true);
        ci.setToolTipText(tooltip);
        if (revalidate) {
            revalidate();
        }
    }

    public void addActionListener(final ActionListener l) {
        if (listener == null) listener = new ArrayList();
        listener.add(l);
    }

    public void removeActionListener(final ActionListener l) {
        listener.remove(l);
    }

    public void fireActionEvent(final CachedImageComponent ci) {
        if (listener == null) {
            return;
        }
        ActionEvent e = null;
        for (int i = 0; i < listener.size(); i++) {
            final ActionListener l = (ActionListener) listener.get(i);
            if (e == null) {
                CAT.info("fireActionEvent " + ci.getImage().getName());
                e = new ActionEvent(this, 0, ci.getImage().getName());

            }
            l.actionPerformed(e);
        }
    }

    public void select(final CachedImage ci) {
        for (int i = 0; i < panel.getComponentCount(); i++) {
            final CachedImageComponent cic = (CachedImageComponent) panel.getComponent(i);
            if (cic.getImage() == ci) {
                cic.setBorder(BorderFactory.createLineBorder(Color.black, 2));
            }
            else {
                cic.setBorder(BorderFactory.createEmptyBorder());
            }
        }
        revalidate();
    }

    public void removeImage(final String c) {
        for (int i = 0; i < panel.getComponentCount(); i++) {
            final CachedImageComponent ci = (CachedImageComponent) panel.getComponent(size++);
            if (ci.getName().equals(c)) {
                ci.setImage(null, true);
                ci.setVisible(false);
                revalidate();
                return;
            }
        }
    }

    public void removeAll() {
        for (int i = 0; i < panel.getComponentCount(); i++) {
            final Component c = panel.getComponent(i);
            if (c instanceof CachedImageComponent) {
                final CachedImageComponent ci = (CachedImageComponent) c;
                ci.setImage(null, true);
                ci.setVisible(false);
            }
        }
        revalidate();
    }

}
