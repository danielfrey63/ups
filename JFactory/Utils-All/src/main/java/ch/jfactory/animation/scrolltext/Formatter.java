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

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.beans.XMLDecoder;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/**
 * Collects some formatting properties.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
public class Formatter {
    /**
     * The default line spacing.
     */
    private static final int DEFAULT_LINE_SPACING = 10;

    /**
     * The default paragraph spacing.
     */
    private static final int DEFAULT_SPACE_BEFORE = 3;

    /**
     * The name of this formatting collection.
     */
    private String name = "Default";

    /**
     * The font for this formatting collection.
     */
    private Font font = UIManager.getFont("TextField.font");

    /**
     * The foreground color.
     */
    private Color color = UIManager.getColor("TextField.foreground");

    /**
     * The line spacing in points.
     */
    private int lineSpace = DEFAULT_LINE_SPACING;

    /**
     * The space before paragraphs in points.
     */
    private int spaceBefore = DEFAULT_SPACE_BEFORE;

    /**
     * The alignement as defined in the SwingConstants class.
     */
    private int alignement = SwingConstants.LEFT;

    /**
     * The font metrics associated.
     */
    private FontMetrics fontMetrics;

    /**
     * Whether to glue paragraphs together.
     */
    private boolean keepWithPrevious;

    /**
     * Factory method to deserialize a formatter object. The XMLDecoder is used.
     *
     * @param file the serialized formatter object
     * @return the object
     */
    public static Formatter getFormatter(final String file) {
        final InputStream fileStream = Formatter.class.getResourceAsStream(file);
        final XMLDecoder dec = new XMLDecoder(fileStream);
        final Formatter formatter = (Formatter) dec.readObject();
        dec.close();

        return formatter;
    }

    /**
     * Returns the font.
     *
     * @return the font
     */
    public Font getFont() {
        return font;
    }

    /**
     * Sets the font.
     *
     * @param font the font to set
     */
    public void setFont(final Font font) {
        this.font = font;
    }

    /**
     * Returns the foreground color.
     *
     * @return the foreground color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the foreground color.
     *
     * @param color the foreground color to set
     */
    public void setColor(final Color color) {
        this.color = color;
    }

    /**
     * Returns the line spacing in pixels.
     *
     * @return the line specing
     */
    public int getLineSpace() {
        return lineSpace;
    }

    /**
     * Sets the line spacing.
     *
     * @param lineSpace the line spacing in pixels
     */
    public void setLineSpace(final int lineSpace) {
        this.lineSpace = lineSpace;
    }

    /**
     * Returns the space before a paragraph.
     *
     * @return the spacing in pixels
     */
    public int getSpaceBefore() {
        return spaceBefore;
    }

    /**
     * Sets the paragraph spacing.
     *
     * @param spaceBefore the spacing in pixels
     */
    public void setSpaceBefore(final int spaceBefore) {
        this.spaceBefore = spaceBefore;
    }

    /**
     * Returns the name of this formatter.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this formatter.
     *
     * @param name the new name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Returns the alignment in form an int given by the SwingUtilities constants.
     *
     * @return the alignment
     */
    public int getAlignement() {
        return alignement;
    }

    /**
     * Sets the alignment. Use the SwingUtilities constants for it.
     *
     * @param alignement the new alignement to set
     */
    public void setAlignement(final int alignement) {
        this.alignement = alignement;
    }

    /**
     * Returns the font metrics.
     *
     * @return the fort metrics
     */
    public FontMetrics getFontMetrics() {
        return fontMetrics;
    }

    /**
     * Sets the font metrics.
     *
     * @param fontMetrics the new font metrics to set
     */
    public void setFontMetrics(final FontMetrics fontMetrics) {
        this.fontMetrics = fontMetrics;
    }

    /**
     * Returns the vertical coordinates of all lines.
     *
     * @param lines the lines to retrieve the coordinates from
     * @return lines an array of base line y-coordinates in pixels
     */
    public int[] getVerticalCoordinates(final String[] lines) {
        final int length = lines.length;
        final int[] verticalCoordinates = new int[length];
        int y = getSpaceBefore();

        for (int i = 0; i < length; i++) {
            y += getLineSpace();
            verticalCoordinates[i] = y;
        }

        return verticalCoordinates;
    }

    /**
     * Returns the horizontal starts of the lines.
     *
     * @param lines           the lines to get the starting positions from
     * @param printSpaceWidth the print space width to wrap lines
     * @return an array of pixels for x-coordinates of the first characters of the lines
     */
    public int[] getHorizontalCoordinates(final String[] lines, final int printSpaceWidth) {
        final int length = lines.length;
        final int[] horizontalCoordinates = new int[length];

        for (int i = 0; i < length; i++) {
            final String line = lines[i];
            final int borderWidth = printSpaceWidth - fontMetrics.stringWidth(line);

            if (alignement == SwingConstants.CENTER) {
                horizontalCoordinates[i] = borderWidth / 2;
            } else if (alignement == SwingConstants.RIGHT) {
                horizontalCoordinates[i] = borderWidth;
            } else {
                horizontalCoordinates[i] = 0;
            }
        }

        return horizontalCoordinates;
    }

    /**
     * Calculates the height for the given number of lines. Therefore the line height is multiplied with the number of
     * lines, then added to the space before for the first and the maximum descent for the last line.
     *
     * @param lineCount the number of lines in the paragraph
     * @return the height for the given numer of lines
     */
    public int getHeight(final int lineCount) {
        return spaceBefore + (lineCount * lineSpace) + fontMetrics.getMaxDescent();
    }

    /**
     * Calculates the lines out of the text and font metrics.
     *
     * @param string          the string to break into lines
     * @param printSpaceWidth the print space width to wrap text in
     * @return an array of lines
     */
    public String[] getLines(final String string, final int printSpaceWidth) {
        final List linesList = new ArrayList();

        for (StringTokenizer st1 = new StringTokenizer(string, "#"); st1.hasMoreTokens();) {
            final String paragraph = st1.nextToken();
            String line = "";

            for (StringTokenizer st2 = new StringTokenizer(paragraph, " "); st2.hasMoreTokens();) {
                final String word = st2.nextToken();

                if ((fontMetrics.stringWidth(line) + fontMetrics.stringWidth(word)) > printSpaceWidth) {
                    linesList.add(line);
                    line = "";
                }

                line += (word + " ");
            }

            linesList.add(line);
        }

        return (String[]) linesList.toArray(new String[0]);
    }

    /**
     * Returns the length of an array of strings.
     *
     * @param lines the lines containing the strings
     * @return an array of pixel width of the lines
     */
    public int[] getLength(final String[] lines) {
        final int[] lengths = new int[lines.length];

        for (int i = 0; i < lines.length; i++) {
            final String line = lines[i];
            lengths[i] = fontMetrics.stringWidth(line);
        }

        return lengths;
    }

    /**
     * Returns whether to keep with previous paragraph.
     *
     * @return whether to keep with previous paragraph
     */
    public boolean isKeepWithPrevious() {
        return keepWithPrevious;
    }

    /**
     * Set whether to keep with previous paragraph.
     *
     * @param keepWithPrevious the new value
     */
    public void setKeepWithPrevious(final boolean keepWithPrevious) {
        this.keepWithPrevious = keepWithPrevious;
    }
}
