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

package ch.jfactory.animation.scrolltext;

import ch.jfactory.animation.Paintable;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.io.IOUtils;

/**
 * A paintable that displays scrolling text. The behaviour may be influenced by setting: <ul><li>The velocity (actually
 * inverse of the velocity) by setting the {@link #setScrollDelay(int) scroll deley}</li> <li>The delay to wait between
 * pages ({@link #setParagraphDelay(int) paragraph delay})</li> <li>A page mode. If set to true scrolls one paragraph
 * per page, otherwise all paragraphs are adjacent ({@link #setPageModus(boolean) page mode})</li> <li>A {@link
 * #setBackgroundColor(java.awt.Color) background color}</li> </ul>
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
public class ScrollingTextPaintable extends Paintable
{
    /** The default scroll delay. */
    private static final int DEFAULT_SCROLL_DELAY = 10;

    /** The default paragraph delay. */
    private static final int DEFAULT_PARAGRAPH_DELAY = 2000;

    /** The delay between steps during scrolling. */
    private int scrollDelay = DEFAULT_SCROLL_DELAY;

    /** The pause in millis until the next paragraph is shown. */
    private int paragraphDelay = DEFAULT_PARAGRAPH_DELAY;

    /** Whether to display each paragraph on its own page. */
    private boolean pageModus = true;

    /** The background color. */
    private Color backgroundColor = Color.white;

    /** The file parser to parse lines. */
    private final FileParser fileParser = new SimpleFileParser();

    /** The array of sections to display. */
    private transient Section[] sections;

    /** The current y position. */
    private transient int currentY;

    /** Whether the paragraph has reached the top position. */
    private transient boolean isOnTop;

    /**
     * Constructs a new instance.
     *
     * @param textInputStream the text file input stream containing the data to display
     * @param printSpaceWidth the print space width to use to calculate the line wraps
     * @param pageModus       whether to display one block on one page
     */
    public ScrollingTextPaintable(final InputStream textInputStream, final int printSpaceWidth, final boolean pageModus)
    {
        this(pageModus);

        try
        {
            final String text = IOUtils.toString(textInputStream);
            final List rowFileLines = initText(text);
            sections = initSections(rowFileLines, printSpaceWidth);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Consstructs a instance using the given page modus.
     *
     * @param pageModus the page modus to use
     */
    private ScrollingTextPaintable(final boolean pageModus)
    {
        this.pageModus = pageModus;
    }

    /**
     * Sets the delays to sleep. It is dependent of whether a page is at the top of the display area and other
     * configurations.
     */
    public void run()
    {
        initMouseListener();
        currentY = getAnimation().getHeight();

        while (isRun())
        {
            int sleepTime = scrollDelay;
            currentY -= 1;

            if ((pageModus && ((currentY % getAnimation().getHeight()) == 0)) || (isOnTop))
            {
                sleepTime = paragraphDelay;
                isOnTop = false;
            }
            else
            {
                sleepTime = scrollDelay;
            }

            getAnimation().repaint();

            try
            {
                Thread.sleep(sleepTime);
            }
            catch (InterruptedException e)
            {
            }
        }
    }

    /**
     * Draws the text according to the configuration.
     *
     * @param g the graphics object to paint on
     */
    public void paint(final Graphics2D g)
    {
        if (fileParser.getFormatters(g) != null)
        {
            g.setColor(backgroundColor);

            final int h = getAnimation().getHeight();
            g.fillRect(0, 0, getAnimation().getWidth(), h);

            final int numberOfSections = sections.length;
            int sum = 0;
            int lineY = 0;

            for (int i = 0; (i < Integer.MAX_VALUE) && (lineY < h); i++)
            {
                final int modIndex = i % numberOfSections;
                final Section section = sections[modIndex];
                final Paragraph[] paragraphs = section.getParagraphs();

                for (int p = 0; p < paragraphs.length; p++)
                {
                    final Paragraph par = paragraphs[p];
                    final Formatter formatter = par.getFormatter();
                    final String[] lines = par.getLines();
                    final int[] xs = par.getHorizontalCoords();
                    final int[] ys = par.getVerticalCoords();
                    g.setFont(formatter.getFont());
                    g.setColor(formatter.getColor());

                    for (int l = 0; l < lines.length; l++)
                    {
                        lineY = currentY + ys[l] + sum;

                        if (!pageModus && (l == 0) && (p == 0) && (lineY == ys[0]))
                        {
                            isOnTop = true;
                        }

                        g.drawString(lines[l], xs[l] + getAnimation().getInsets().left, lineY);
                    }

                    sum += par.getHeight();
                }

                if (pageModus)
                {
                    sum += (h - section.getHeight() - 1);
                }

                if (i > (Integer.MAX_VALUE / 2))
                {
                    i = modIndex;
                }
            }
        }
    }

    /**
     * Sets the delay for the animation. The smaller the value, the faster the animation. Defaults to 10 millis.
     *
     * @param scrollDelay the delay in millis
     */
    public void setScrollDelay(final int scrollDelay)
    {
        this.scrollDelay = scrollDelay;
    }

    /**
     * Sets the delay until the next page is displayed. Defaults to 2000 millis (2 seconds).
     *
     * @param paragraphDelay the delay in millis
     */
    public void setParagraphDelay(final int paragraphDelay)
    {
        this.paragraphDelay = paragraphDelay;
    }

    /**
     * Whether one paragraph should be displayed per page. If set to false, all paragraphs are subsequently appended and
     * displayed. Defaults to true.
     *
     * @param pageModus whether to display one paragraph per page
     */
    public void setPageModus(final boolean pageModus)
    {
        this.pageModus = pageModus;
    }

    /**
     * Returns the background color. Defaults to white.
     *
     * @return the background color
     */
    public Color getBackgroundColor()
    {
        return backgroundColor;
    }

    /**
     * Sets the background color.
     *
     * @param backgroundColor the background color to set
     */
    public void setBackgroundColor(final Color backgroundColor)
    {
        this.backgroundColor = backgroundColor;
    }

    /**
     * Splits the given text into lines and returns them as a list.
     *
     * @param text the text to split
     * @return a list of lines
     */
    private List initText(final String text)
    {
        final String sep = System.getProperty("line.separator");
        final List list = new ArrayList();
        final StringTokenizer tokenizer = new StringTokenizer(text, sep, false);

        while (tokenizer.hasMoreElements())
        {
            list.add(tokenizer.nextElement());
        }

        return list;
    }

    /**
     * Takes a list of file lines and a print space width and returns an array of sections.
     *
     * @param rowFileLines    the list of lines to make sections from
     * @param printSpaceWidth the print space width to wrap paragraphs in
     * @return an array of sections
     */
    private Section[] initSections(final List rowFileLines, final int printSpaceWidth)
    {
        final FileLine[] fileLines = fileParser.initText(rowFileLines);

        return SectionFactory.initSections(fileLines, printSpaceWidth);
    }

    /** Init mouse listener. */
    private void initMouseListener()
    {
        getAnimation().addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(final MouseEvent e)
            {
                getRunner().interrupt();
            }
        });
    }
}
