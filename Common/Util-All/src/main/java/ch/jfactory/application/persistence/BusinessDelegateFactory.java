/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.application.persistence;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
public class BusinessDelegateFactory
{
    private static IFBusinessDelegate businessDelegate;

    public static IFBusinessDelegate getBusinessDelegate()
    {
        if ( businessDelegate == null )
        {
            final String clazz = System.getProperty( "xmatrix.businessdelegate" );
            try
            {
                businessDelegate = (IFBusinessDelegate) Class.forName( clazz ).newInstance();
            }
            catch ( ClassNotFoundException e )
            {
                e.printStackTrace();
            }
            catch ( IllegalAccessException e )
            {
                e.printStackTrace();
            }
            catch ( InstantiationException e )
            {
                e.printStackTrace();
            }
        }
        return businessDelegate;
    }
}
