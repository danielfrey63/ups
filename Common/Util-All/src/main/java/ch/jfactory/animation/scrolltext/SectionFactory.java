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

import java.util.ArrayList;
import java.util.List;

/**
 * Factory to make section objects out of an array of file lines.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
public final class SectionFactory
{
    /** Make construction impossible. */
    private SectionFactory()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Generates an array of sections out of file lines. Each section contains some {@link Paragraph} objects.
     *
     * @param fileLines       an array of file lines
     * @param printSpaceWidth the print space width of the paragraphs/sections
     * @return an array of sections
     */
    public static Section[] initSections( final FileLine[] fileLines, final int printSpaceWidth )
    {
        final List sections = new ArrayList();
        List lastFileLines = new ArrayList();

        for ( int i = 0; i < fileLines.length; i++ )
        {
            final FileLine line = fileLines[i];
            final Formatter formatter = line.getFormatter();
            final Paragraph par = new Paragraph( formatter, line.getText(), printSpaceWidth );

            if ( !formatter.isKeepWithPrevious() && ( i > 0 ) )
            {
                sections.add( new Section( (Paragraph[]) lastFileLines.toArray( new Paragraph[lastFileLines.size()] ) ) );
                lastFileLines = new ArrayList();
            }

            lastFileLines.add( par );
        }

        sections.add( new Section( (Paragraph[]) lastFileLines.toArray( new Paragraph[lastFileLines.size()] ) ) );

        return (Section[]) sections.toArray( new Section[sections.size()] );
    }
}
