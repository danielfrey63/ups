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
 * A file line consisting of a formatter, a text and a link.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
public class FileLine {
    /**
     * The formatter object associated with this file line.
     */
    private Formatter formatter;

    /**
     * The text of this file line.
     */
    private String text;

    /**
     * The link associated with this file line.
     */
    private String link;

    /**
     * Returns the formatter object for this file line.
     *
     * @return the formatter object
     */
    public Formatter getFormatter() {
        return formatter;
    }

    /**
     * Sets the formatter object for this file line.
     *
     * @param formatter the formatter object to set
     */
    public void setFormatter(final Formatter formatter) {
        this.formatter = formatter;
    }

    /**
     * Returns the text of this file line.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text of this file line.
     *
     * @param text the text to set
     */
    public void setText(final String text) {
        this.text = text;
    }

    /**
     * Returns the link of this file line.
     * Todo: Might be better as URL?
     *
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * Sets the link for this file line. Todo: Might be better as URL?
     *
     * @param link the link to set
     */
    public void setLink(final String link) {
        this.link = link;
    }
}
