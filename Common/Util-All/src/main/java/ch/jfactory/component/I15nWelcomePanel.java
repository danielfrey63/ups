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
package ch.jfactory.component;

import ch.jfactory.action.ActionBuilder;
import ch.jfactory.binding.DefaultInfoModel;
import ch.jfactory.binding.InfoModel;
import ch.jfactory.binding.Note;
import ch.jfactory.color.ColorUtils;
import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.ResourceHelper;
import ch.jfactory.resource.Strings;
import com.jgoodies.forms.debug.FormDebugPanel;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.uifextras.panel.GradientBackgroundPanel;
import com.jgoodies.uifextras.util.ActionLabel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import net.infonode.util.ImageException;
import net.infonode.util.ImageUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * Retrives the texts for the welcome panel from different keys by extending the given key as follows:
 *
 * <ul>
 *
 * <li>key + <code>.title</code> (see {@link I15nWelcomePanel})</li>
 *
 * <li>key + <code>.text</code></li>
 *
 * <li>key + <code>.logo</code></li>
 *
 * </ul>
 *
 * @author Daniel Frey
 * @version $Revision: 1.8 $ $Date: 2008/01/06 10:16:23 $
 */
public class I15nWelcomePanel extends GradientBackgroundPanel
{

    public static final String SEPARATOR = "-";

    private static final Logger LOGGER = Logger.getLogger(I15nWelcomePanel.class);

    private static final boolean DEBUG = LOGGER.isDebugEnabled();

    private static final int BORDER_HEIGHT = 80;

    private String resourceKey;

    private String infoString = "Starting...";

    private Color infoColor = new Color(0, 127, 0);

    private Font infoFont;

    private Rectangle infoRect;

    private Timer timer;

    private long lastTime;

    private int infoY;

    private Image backgroundImage;

    private Image upperRightLogo;

    private Image lowerRightLogo;

    private Image lowerLeftLogo;

    private CommandManager commandManager;

    private JPanel panel;

    private Map<RenderingHints.Key, Object> hints;

    private InfoModel infoModel = new DefaultInfoModel();

    private final PropertyChangeListener noteListener;

    private static final String KEYSUFFIX_WELCOME_TITLECOLOR = ".title.color";

    private static final String KEYSUFFIX_WELCOME_TITLETEXT = ".title.text";

    public static final String TEXT = "+";

    /**
     * Constructs a <code>I15nWelcomePanel</code> with the specified key.
     *
     * @param resourceKey    the base resourceKey for looking up the strings
     * @param actionKeys     the actions to put into the panel
     * @param commandManager if the commands are based on {@link org.pietschy.command.ActionCommand} implementations.
     *                       Otherwise {@link com.jgoodies.uif.action.ActionManager} is used.
     */
    public I15nWelcomePanel(final String resourceKey, final String[] actionKeys, final CommandManager commandManager)
    {
        super(false);

        this.commandManager = commandManager;
        this.resourceKey = resourceKey;

        try
        {
            backgroundImage = getImage(".image");
            upperRightLogo = getImage(".upper.logo");
            lowerRightLogo = getImage(".lower.right.logo");
            lowerLeftLogo = getImage(".lower.left.logo");

        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        hints = new HashMap<RenderingHints.Key, Object>();
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        initLayout(actionKeys);

        noteListener = new PropertyChangeListener()
        {
            public void propertyChange(final PropertyChangeEvent evt)
            {
                final Note note = (Note) evt.getNewValue();
                infoString = note.getMessage();
                if (note.getColor() != null)
                {
                    infoColor = note.getColor();
                }
                initInfoStringRect();
                // TODO: updateer logic
//                if (!note.isUpdate()) {
//                    lastTime = System.currentTimeMillis();
//                    if (timer != null) timer.stop();
//                    timer = new Timer(50, new ActionListener() {
//                        public void actionPerformed(ActionEvent e) {
//                            final int delay = timer.getDelay();
//                            timer.setDelay((int) (delay * 1.1));
//                            if (delay > 1500) {
//                                timer.stop();
//                                infoString = null;
//                            }
//                            repaint();
//                        }
//                    });
//                    timer.setCoalesce(true);
//                    timer.start();
//                }
            }
        };
    }

    /**
     * Constructs a <code>I15nWelcomePanel</code> with the specified key.
     *
     * @param resourceKey the base resourceKey for looking up the strings
     * @param actionKeys  the actions to put into the panel
     */
    public I15nWelcomePanel(final String resourceKey, final String[] actionKeys)
    {
        this(resourceKey, actionKeys, null);
    }

    protected void paintComponent(final Graphics g)
    {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHints(hints);
        paintHorizontalLine(g2, BORDER_HEIGHT, Color.black);
        paintUpperRightLogo(g2);
        paintBackgroundImage(g2);
        paintGradient(g2);
        paintLowerRightLogo(g2);
        paintLowerLeftLogo(g2);
        paintInfoString(g2);
    }

    public void setInfoModel(final InfoModel infoModel)
    {
        if (infoModel == null)
        {
            throw new NullPointerException("info model may not be null");
        }
        this.infoModel.removePropertyChangeListener(InfoModel.PROPERTYNAME_NOTE, noteListener);
        this.infoModel = infoModel;
        this.infoModel.addPropertyChangeListener(InfoModel.PROPERTYNAME_NOTE, noteListener);
        final Note note = this.infoModel.getNote();
        infoString = note.getMessage();
        if (note.getColor() != null)
        {
            infoColor = note.getColor();
        }
    }

    private void initInfoStringRect()
    {
        final Graphics2D g2 = (Graphics2D) getGraphics();
        if (g2 != null)
        {
            if (infoFont == null)
            {
                infoFont = g2.getFont().deriveFont(Font.BOLD, (int) (g2.getFont().getSize() * 1.5));
            }
            final Rectangle bounds = getInfoStringBounds(g2);
            infoRect = (infoRect == null ? bounds : bounds.union(infoRect));
        }
    }

    public Dimension getPreferredSize()
    {
        final int w;
        if (backgroundImage != null)
        {
            final int iW = backgroundImage.getWidth(this);
            final int iH = backgroundImage.getHeight(this);
            final int pH = panel.getPreferredSize().height;
            w = 80 + iW * ((pH - (2 * BORDER_HEIGHT)) / 10 * 7) / iH;
        }
        else
        {
            w = 80;
        }
        if (DEBUG)
        {
            System.out.println("Panel: " + panel.getPreferredSize());
        }
        final Dimension panelSize = panel.getPreferredSize();
        return new Dimension(panelSize.width + w, panelSize.height);
    }

    /**
     * Creates a welcom panel. The welcome panel is shown uppon startup.
     *
     * @param actionKeys keys for the actions to show
     */
    private void initLayout(final String[] actionKeys)
    {
        final String colSpecs = BORDER_HEIGHT + "px, 8dlu, p:g(1.0)";
        final String rows = StringUtils.repeat(", b:p, 8dlu", actionKeys.length + 2);
        final String rowSpecs = BORDER_HEIGHT + "px, 8dlu" + rows + ", 36dlu, " + BORDER_HEIGHT + "px";
        final FormLayout layout = new FormLayout(colSpecs, rowSpecs);
        panel = (DEBUG ? new FormDebugPanel(layout) : new JPanel(layout));

        int subtitleCounter = 0;
        final CellConstraints cc = new CellConstraints();
        panel.add(createLogoLabel(), cc.xyw(2, 3, 2));
        panel.add(createDescription(), cc.xyw(2, 5, 2));
        for (int i = 0; i < actionKeys.length; i++)
        {
            final String actionKey = actionKeys[i];
            if (actionKey.equals(SEPARATOR))
            {
            }
            else if (actionKey.equals(TEXT))
            {
                final JLabel label = new JLabel(getString(".subtitle." + subtitleCounter++));

                if (DEBUG)
                {
                    System.out.println("Label: " + label.getPreferredSize());
                }
                panel.add(label, cc.xyw(2, 7 + i * 2, 2));
            }
            else
            {
                final ActionLabel label = createActionButton(actionKey);
                if (DEBUG)
                {
                    System.out.println("Actionbutton: " + label.getPreferredSize());
                }
                panel.add(label, cc.xyw(2, 7 + i * 2, 2));
            }
        }
        panel.setOpaque(false);
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter()
        {
            public void componentResized(final ComponentEvent e)
            {
                infoRect = null;
                initInfoStringRect();
            }
        });
    }

    private Image getImage(final String key)
    {
        Image image = null;
        try
        {
            final String imageName = getString(key + ".name");
            final String opacityString = getString(key + ".opacity");
            if (opacityString == null)
            {
                throw new NullPointerException("resource for \"" + resourceKey + key + ".opacity" + "\" not found");
            }
            final float opacity = Float.parseFloat(opacityString);
            // Load image from specified resource path
            final String resourceName = System.getProperty("jfactory.resource.path") + "/" + imageName;
            URL resource = I15nWelcomePanel.class.getResource(resourceName);
            // Or load it from the classpath
            if (resource == null)
            {
                resource = I15nWelcomePanel.class.getResource(imageName);
            }
            if (resource == null)
            {
                throw new NullPointerException("resource for \"" + resourceName + "\" not found");
            }
            final Image iconImage = ImageUtils.create(resource);
            final TransparentImageFilter filter = new TransparentImageFilter(opacity);
            final FilteredImageSource producer = new FilteredImageSource(iconImage.getSource(), filter);
            image = createImage(producer);
            ImageUtils.waitImage(image);
        }
        catch (NullPointerException e)
        {
            LOGGER.warn(e.getMessage());
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.warn("image or logo configuration not found for \"" + key + "\"");
        }
        catch (ImageException e)
        {
            LOGGER.warn("image or logo cannot be loaded \"" + key + "\"");
        }
        return image;
    }

    private String getString(final String extension)
    {
        String string = null;
        try
        {
            string = Strings.getSilentString(resourceKey + extension);
        }
        catch (Exception e)
        {
            // Do nothing;
        }
        return string;
    }

    private JComponent createDescription()
    {
        final JMultiLineLabel label = new JMultiLineLabel(getString(".text"));
        if (DEBUG)
        {
            System.out.println("Description: " + label.getPreferredSize());
        }
        return label;
    }

    /**
     * Creates a label containing the application logo only.
     *
     * @return the label
     */
    private JComponent createLogoLabel()
    {

        final JLabel label = new JLabel(getString(KEYSUFFIX_WELCOME_TITLETEXT));
        final String colorString = getString(KEYSUFFIX_WELCOME_TITLECOLOR);
        label.setForeground(ResourceHelper.decode(colorString));
        label.setVerticalTextPosition(JLabel.BOTTOM);
        label.setHorizontalTextPosition(JLabel.LEFT);
        label.setFont(label.getFont().deriveFont(Font.BOLD, label.getFont().getSize() * 2f));

        final String iconString = getString(".text.logo");
        if (!"".equals(iconString))
        {
            final ImageIcon icon = ImageLocator.getIcon(iconString);
            label.setIcon(icon);
            label.setIconTextGap(8);
        }

        if (DEBUG)
        {
            System.out.println("Logolabel: " + label.getPreferredSize());
        }
        return label;
    }

    /**
     * Creates an action button with the given label and action base.
     *
     * @param base the action base
     * @return an action button
     */
    private ActionLabel createActionButton(final String base)
    {
        final ActionLabel go;
        final Action action;
        if (commandManager != null)
        {
            final ActionCommand command = commandManager.getCommand(base);
            if (command == null)
            {
                throw new NullPointerException("command not found for \"" + base + "\"");
            }
            action = command.getActionAdapter("label");
            go = new ActionLabel(action);
        }
        else
        {
            action = ActionBuilder.getAction(base);
            go = new ActionLabel((String) action.getValue(Action.LONG_DESCRIPTION));
            go.setAction(action);
        }
        go.setIcon(ImageLocator.getIcon(getString(".action.icon")));
        return go;
    }

    private void paintHorizontalLine(final Graphics2D g2, final int y, final Color color)
    {

        final int width = getWidth() * 2 / 5;
        final Color startColor = ColorUtils.fade(color, 0.8);
        g2.setPaint(new GradientPaint(0, 0, startColor, width, 0, color));
        g2.fillRect(0, y, width, 1);
        g2.setPaint(new GradientPaint(width, 0, color, getWidth(), 0, startColor));
        g2.fillRect(width, y, getWidth(), 1);
    }

    private void paintLowerLeftLogo(final Graphics2D g2)
    {
        if (lowerLeftLogo != null)
        {
            final int height = lowerLeftLogo.getHeight(this);
            final int width = lowerLeftLogo.getWidth(this);
            g2.drawImage(lowerLeftLogo, 20, getHeight() - height - ((BORDER_HEIGHT - height) / 2), width, height, this);
        }
    }

    private void paintLowerRightLogo(final Graphics2D g2)
    {
        if (lowerRightLogo != null)
        {
            final int height = lowerRightLogo.getHeight(this);
            final int width = lowerRightLogo.getWidth(this);
            final int y = getHeight() - height - ((BORDER_HEIGHT - height) / 2);
            final int x = getWidth() - width - 20;
            g2.drawImage(lowerRightLogo, x, y, width, height, this);
        }
    }

    private void paintGradient(final Graphics2D g2)
    {
        g2.setColor(Color.white);
        g2.fillRect(0, getHeight() - BORDER_HEIGHT, getWidth(), BORDER_HEIGHT);
        paintHorizontalLine(g2, getHeight() - BORDER_HEIGHT, new Color(177, 227, 2));
        g2.setPaint(Color.white);
    }

    private void paintBackgroundImage(final Graphics2D g2)
    {
        if (backgroundImage != null)
        {
            // Place image at 20% below upper and 10% above lower border
            final int width1 = backgroundImage.getWidth(this);
            final int height1 = backgroundImage.getHeight(this);
            final int imageH = (getHeight() - (2 * BORDER_HEIGHT)) / 10 * 7;
            final int imageW = width1 * imageH / height1;
            final int x = getWidth() * 9 / 10 - imageW;
            final int y = (getHeight() - (2 * BORDER_HEIGHT)) / 10 * 2 + BORDER_HEIGHT;
            if (DEBUG)
            {
                g2.setColor(ColorUtils.fade(Color.blue, 0.98));
                g2.fillRect(x, y, imageW, imageH);
                g2.setColor(Color.blue);
                g2.drawRect(x, y, imageW, imageH);
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 12.0f));
                final String widthString = imageW + " px";
                final int stringW = g2.getFontMetrics().stringWidth(widthString);
                final int stringH = (int) g2.getFontMetrics().getStringBounds(widthString, g2).getHeight();
                g2.drawString(widthString, x + (imageW - stringW) / 2, y + 2 + stringH);
            }
            g2.drawImage(backgroundImage, x, y, imageW, imageH, this);
        }
    }

    private void paintUpperRightLogo(final Graphics2D g2)
    {
        if (upperRightLogo != null)
        {
            // Place logo vertically centered at right edge
            final int height = upperRightLogo.getHeight(this);
            final int width = upperRightLogo.getWidth(this);
            g2.drawImage(upperRightLogo, getWidth() - width - 20, (int) ((BORDER_HEIGHT - height) / 2), width, height, this);
        }
    }

    private void paintInfoString(final Graphics2D g2)
    {
        if (infoString != null && infoRect != null)
        {
            if (DEBUG)
            {
                g2.setColor(ColorUtils.fade(Color.orange, 0.9));
                g2.fill(infoRect);
                System.out.println(infoRect);
                g2.setColor(Color.orange);
                g2.draw(infoRect);
            }
            if (infoColor != null)
            {
                final int d = 1000;
                g2.setColor(ColorUtils.fade(infoColor, 1.0 - (1.0 * d / (System.currentTimeMillis() - lastTime + d))));
                g2.setFont(infoFont);
                g2.drawString(infoString, BORDER_HEIGHT, infoY);
            }
        }
    }

    private Rectangle getInfoStringBounds(final Graphics2D g2)
    {
        if (infoString == null)
        {
            return null;
        }
        g2.setFont(infoFont);
        final Rectangle2D r = g2.getFontMetrics().getStringBounds(infoString, g2);
        infoY = (int) (getHeight() - ((BORDER_HEIGHT - r.getHeight()) / 2) - r.getHeight() - r.getY());
        return new Rectangle(BORDER_HEIGHT,
                (int) (getHeight() - ((BORDER_HEIGHT - r.getHeight()) / 2) - r.getHeight()),
                (int) r.getWidth(), (int) r.getHeight());
    }

    private static class TransparentImageFilter extends RGBImageFilter
    {

        float alphaPercent;

        public TransparentImageFilter()
        {
            this(0.75f);
        }

        public TransparentImageFilter(final float aPercent) throws IllegalArgumentException
        {
            if ((aPercent < 0.0) || (aPercent > 1.0))
            {
                throw new IllegalArgumentException();
            }
            alphaPercent = aPercent;
            canFilterIndexColorModel = true;
        }

        public int filterRGB(final int x, final int y, final int rgb)
        {
            int a = (rgb >> 24) & 0xff;
            a *= alphaPercent;
            return ((rgb & 0x00ffffff) | (a << 24));
        }
    }
}
