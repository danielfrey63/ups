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
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2006/08/04 15:50:01 $
 */
public class SimpleLevel
{
    public static final SimpleLevel DUMMY_LEVEL = new SimpleLevel( "DUMMY LEVEL", null, 0 );

    private final String name;

    private int rank;

    private SimpleLevel childLevel;

    private final SimpleLevel parentLevel;

    public SimpleLevel( final String name, final SimpleLevel parent, final int rank )
    {
        this.name = name;
        this.parentLevel = parent;
        if ( parent != null )
        {
            parent.childLevel = this;
        }
    }

    public String getName()
    {
        return name;
    }

    public SimpleLevel getChildLevel()
    {
        return childLevel;
    }

    public SimpleLevel getParentLevel()
    {
        return parentLevel;
    }

    public int getRank()
    {
        return rank;
    }

    public boolean isLower( final SimpleLevel level )
    {
        // root has null level
        return level == null || getRank() > level.getRank();
    }
}
