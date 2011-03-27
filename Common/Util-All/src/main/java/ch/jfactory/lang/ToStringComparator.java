/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.lang;

import java.text.Collator;
import java.util.Comparator;

/**
 * Invokes the toString() method and compares the resulting strings.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
public class ToStringComparator<T> implements Comparator<T>
{
    /** @see Comparator#compare(Object, Object) */
    public int compare( final T o1, final T o2 )
    {
        return Collator.getInstance().compare( o1.toString(), o2.toString() );
    }
}
