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

/**
 * Interface to implement for tree like objects/wrappers.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2006/04/21 11:02:52 $
 */
public interface TreeObject
{

    /**
     * Returns the children of this object. May be null for leafs.
     *
     * @return the children
     */
    TreeObject[] getChildren();

    /**
     * Sets the children for this object.
     *
     * @param children the children to set
     */
    void setChildren(TreeObject[] children);

    /**
     * Returns the parent of this object. May be null for the root object.
     *
     * @return the praent
     */
    TreeObject getParent();

    /**
     * Sets the parent for this object.
     *
     * @param parent the parent to set
     */
    void setParent(TreeObject parent);

    /**
     * Returns the wrapped taxon.
     *
     * @return the taxon
     */
    public SimpleTaxon getTaxon();
}
