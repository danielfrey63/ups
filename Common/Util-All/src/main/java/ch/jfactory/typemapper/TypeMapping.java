/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.typemapper;

import com.jgoodies.binding.beans.Model;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:54:59 $
 */
public class TypeMapping extends Model
{
    public static final String PROPERTYNAME_ICON = "icon";

    public static final String PROPERTYNAME_TEXT = "text";

    private String icon;

    private String text;

    public TypeMapping( final String text, final String icon )
    {
        this.icon = icon;
        this.text = text;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon( final String icon )
    {
        this.icon = icon;
    }

    public String getText()
    {
        return text;
    }

    public void setText( final String text )
    {
        this.text = text;
    }

    public String toString()
    {
        return text + " " + icon;
    }

    public int hashCode()
    {
        return text.hashCode();
    }

    public boolean equals( final Object obj )
    {
        if ( !( obj instanceof TypeMapping ) )
        {
            return false;
        }
        if ( this == obj )
        {
            return true;
        }
        final TypeMapping other = (TypeMapping) obj;
        return getText().equals( other.getText() );
    }
}
