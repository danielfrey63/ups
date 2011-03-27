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

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Simple parser for file lines. See {@link #initText(List) initText} for a detailed description of the parser rules.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
public class SimpleFileParser implements FileParser
{
    /** The formatters to generate the file lines. */
    private Formatter[] formatters;

    /**
     * Parses a list of text lines. Each line consists of several tokens separated by "|". The tokens are interpreted as follows: <ol><li>The first token is the path and name of a serialized {@link Formatter} object. The file must be in the classpath.</li> <li>The second token is the text for the paragraph.</li> <li>The third token is optinal and may contain a URL link.</li> </ol> <p>Todo: Make links work</p>
     *
     * @param fileLines a list of lines containing text separated by "|"
     * @return an array of FileLine objects
     */
    public FileLine[] initText( final List fileLines )
    {
        final List lines = new ArrayList();
        final String delim = "|";
        final Iterator iter = fileLines.listIterator();
        final HashMap formatterMap = new HashMap();

        while ( iter.hasNext() )
        {
            final String line = (String) iter.next();
            final StringTokenizer tokenizer = new StringTokenizer( line, delim, true );
            int tokenCounter = 0;
            final FileLine fileLine = new FileLine();
            lines.add( fileLine );

            while ( tokenizer.hasMoreTokens() )
            {
                String token = tokenizer.nextToken();

                if ( token.equals( delim ) )
                {
                    tokenCounter++;
                }
                else
                {
                    token = ( ( token == null ) ? "" : token );

                    switch ( tokenCounter )
                    {
                        case 0:

                            Formatter formatter = (Formatter) formatterMap.get( token );

                            if ( formatter == null )
                            {
                                formatter = Formatter.getFormatter( token );
                                formatterMap.put( token, formatter );
                            }

                            fileLine.setFormatter( formatter );

                            break;

                        case 1:
                            fileLine.setText( token );

                            break;

                        case 2:
                            fileLine.setLink( token );

                            break;

                        default:
                            break;
                    }
                }
            }
        }

        formatters = (Formatter[]) formatterMap.values().toArray( new Formatter[formatterMap.values().size()] );

        return (FileLine[]) lines.toArray( new FileLine[lines.size()] );
    }

    /**
     * Returns an array of formatter objects. Formatting of the text is dependent of the graphics object present. As at build time this object usually does not exist, this method is used to return the formatters in exchange with the graphics object. This guarantees to return formatters with -- in respect to the given graphics object -- valid formatting numbers.
     *
     * @param g the graphics object
     * @return an array of formatters
     */
    public Formatter[] getFormatters( final Graphics g )
    {
        if ( formatters != null )
        {
            for ( final Formatter formatter : formatters )
            {
                formatter.setFontMetrics( g.getFontMetrics( formatter.getFont() ) );
            }
        }

        return formatters;
    }
}
