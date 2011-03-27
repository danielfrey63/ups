/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.application.view.status;

/**
 * TODO: document
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2008/01/06 10:16:23 $
 */
public class SimpleMessage implements Message
{
    private final String text;

    private final Type type;

    public SimpleMessage( final String text, final Type type )
    {
        this.text = text;
        this.type = type;
    }

    public String getText()
    {
        return text;
    }

    public Type getType()
    {
        return type;
    }
}
