/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.application.view.border;

/** This class delivers constants for the direction of bevel borders. */
public class BevelDirection
{
    /** The bevel border is drawn like a lowerd bevel */
    public static final BevelDirection LOWERED = new BevelDirection();

    /** The bevel border is drawn like a raised bevel */
    public static final BevelDirection RAISED = new BevelDirection();

    /** Keep constructor privat for enumeration pattern */
    private BevelDirection()
    {
    }
}

