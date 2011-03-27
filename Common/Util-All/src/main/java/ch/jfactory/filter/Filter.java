/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.filter;

import java.io.Serializable;

/**
 * A generalized filter to restrict visibility or mutability on a list.
 *
 * @author Jools Enticknap
 * @author Bradley S. Huffman
 * @version $Revision: 1.1 $, $Date: 2005/11/17 11:54:58 $
 */
public interface Filter extends Serializable
{
    /** Filter that returns always true. */
    public static final Filter TRUEFILTER = new Filter()
    {
        public boolean matches( final Object obj )
        {
            return true;
        }
    };

    /** Filter that returns always false. */
    public static final Filter FALSEFILTER = new Filter()
    {
        public boolean matches( final Object obj )
        {
            return false;
        }
    };

    /**
     * Check to see if the object matches a predefined set of rules.
     *
     * @param obj The object to verify.
     * @return <code>true</code> if the object matches a predfined set of rules.
     */
    public boolean matches( Object obj );
}
