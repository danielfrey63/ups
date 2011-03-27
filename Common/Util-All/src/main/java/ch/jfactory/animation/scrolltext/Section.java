/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.animation.scrolltext;

/**
 * Class for a section of paragraphs. A section consists of one to many paragraph objects.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
public class Section
{
    /** The paragraphs in this section. */
    private final Paragraph[] paragraphs;

    /** The total height of this section. */
    private int height = -1;

    /**
     * Constructs a section.
     *
     * @param paragraphs an array of paragraphs which build up this section
     */
    public Section( final Paragraph[] paragraphs )
    {
        this.paragraphs = paragraphs;
    }

    /**
     * Returns the paragraphs of this section.
     *
     * @return an array of paragraphs
     */
    public Paragraph[] getParagraphs()
    {
        return paragraphs;
    }

    /**
     * Returns the total height of this section.
     *
     * @return the total height in pixels
     */
    public int getHeight()
    {
        if ( height == -1 )
        {
            for ( final Paragraph paragraph : paragraphs )
            {
                height += paragraph.getHeight();
            }
        }

        return height;
    }
}
