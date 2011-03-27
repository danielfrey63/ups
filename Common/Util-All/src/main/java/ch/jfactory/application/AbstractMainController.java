/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.application;

import ch.jfactory.binding.CodedNote;
import ch.jfactory.binding.InfoModel;
import ch.jfactory.resource.Strings;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2006/04/25 11:09:31 $
 */
public abstract class AbstractMainController
{
    public AbstractMainController( final AbstractMainModel model, final InfoModel infoModel )
    {
        model.queue( new Runnable()
        {
            public void run()
            {
                infoModel.setNote( new CodedNote( Strings.getString( "startup.data" ) ) );
            }
        } );
        initModel( model );
    }

    protected abstract void initModel( AbstractMainModel model );
}
