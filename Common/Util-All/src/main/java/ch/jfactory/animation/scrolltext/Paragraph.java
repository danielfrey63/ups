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

/**
 * Represents a paragraph object. A paragraph consists of one to many lines of text, an associated formatting and a
 * total height and width.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
public class Paragraph
{
    /**
     * The formatter assicitated with this paragraph.
     */
    private Formatter formatter = new Formatter();

    /**
     * The lines in this paragraph.
     */
    private String[] lines;

    /**
     * The total height of this paragraph.
     */
    private int height;

    /**
     * The total width of this paragraph.
     */
    private final int width;

    /**
     * The The text of the paragraph.
     */
    private final String text;

    /**
     * The vertical line coordinates of the base lines in pixels.
     */
    private int[] verticalLineCoords;

    /**
     * The horizontal starts of the first character in pixels.
     */
    private int[] horizontalLineCoords;

    /**
     * The total line lengths in pixels.
     */
    private int[] lengths;

    /**
     * Constructs a paragraph.
     *
     * @param formatter the formatter to format the text
     * @param text      the text to break into lines
     * @param width     the width of the paragraph
     */
    public Paragraph( final Formatter formatter, final String text, final int width )
    {
        this.formatter = formatter;
        this.text = text;
        this.width = width;
    }

    /**
     * Returns the lines of this paragraph.
     *
     * @return an array of lines
     */
    public String[] getLines()
    {
        if ( lines == null )
        {
            init();
        }

        return lines;
    }

    /**
     * Returns the total height of this paragraph in pixels.
     *
     * @return the total height
     */
    public int getHeight()
    {
        if ( lines == null )
        {
            init();
        }

        return height;
    }

    public int[] getLength()
    {
        return lengths;
    }

    /**
     * Returns the vertical coordinates of each lines basline height.
     *
     * @return an array of vertical coordinates in pixels
     */
    public int[] getVerticalCoords()
    {
        if ( lines == null )
        {
            init();
        }

        return verticalLineCoords;
    }

    /**
     * Returns the horizontal coordinates of the first characters. For left aligned text, each value is the same. It may
     * be different for center or right alligned text.
     *
     * @return an array of horizontal coordinates in pixels.
     */
    public int[] getHorizontalCoords()
    {
        if ( lines == null )
        {
            init();
        }

        return horizontalLineCoords;
    }

    /**
     * Returns the formatter of this paragraph.
     *
     * @return the formatter used to format the paragraph
     */
    public Formatter getFormatter()
    {
        return formatter;
    }

    /**
     * Initializes the properties.
     */
    private void init()
    {
        lines = getFormatter().getLines( text, width );
        lengths = getFormatter().getLength( lines );
        height = getFormatter().getHeight( lines.length );
        verticalLineCoords = getFormatter().getVerticalCoordinates( lines );
        horizontalLineCoords = getFormatter().getHorizontalCoordinates( lines, width );
    }
}
