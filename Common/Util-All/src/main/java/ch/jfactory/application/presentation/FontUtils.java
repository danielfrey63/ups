/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.application.presentation;

import ch.jfactory.reflection.ReflectionUtils;
import java.awt.Font;

/** Todo: document $Author: daniel_frey $ $Revision: 1.1 $ */
public class FontUtils
{
    public static int parseFontStyle( final String name )
    {
        return parseFontStyle( name, Font.PLAIN );
    }

    public static int parseFontStyle( final String name, final int defaultFontStyle )
    {
        final Object ret = ReflectionUtils.getField( name, defaultFontStyle, Font.class, int.class );
        return (Integer) ret;
    }

    public static void main( final String[] args )
    {
        System.out.println( parseFontStyle( "plain" ) );
        System.out.println( parseFontStyle( "BOLD" ) );
        System.out.println( parseFontStyle( "ITALIC" ) );
    }
}
