/*
 * ====================================================================
 *  Copyright 2004-2006 www.xmatrix.ch
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */

package ch.xmatrix.ups.domain;

import java.util.EventObject;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.4 $ $Date: 2006/04/21 11:02:52 $
 */
public class ConstraintStateEvent extends EventObject
{

    private TreeObject[] taxa;

    public ConstraintStateEvent(final Object source, final TreeObject[] taxa)
    {
        super(source);
        this.setTaxa(taxa);
    }

    public TreeObject[] getTaxa()
    {
        return taxa;
    }

    private void setTaxa(final TreeObject[] taxa)
    {
        this.taxa = taxa;
    }
}
