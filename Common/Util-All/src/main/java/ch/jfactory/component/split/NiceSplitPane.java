/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.component.split;

import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JSplitPane;

/** Project: $Id: NiceSplitPane.java,v 1.1 2005/06/16 06:28:57 daniel_frey Exp $ $Source: /repository/HerbarCD/Version2.1/xmatrix/src/com/xmatrix/gui/component/split/NiceSplitPane.java,v $ $Revision: 1.1 $, $Author: daniel_frey $ */
public class NiceSplitPane extends JSplitPane
{
    public NiceSplitPane()
    {
        init();
    }

    public NiceSplitPane( final int orientation )
    {
        super( orientation );
        init();
    }

    public NiceSplitPane( final int orientation, final boolean continuous )
    {
        super( orientation, continuous );
        init();
    }

    public NiceSplitPane( final int orientation, final boolean continuous, final Component left, final Component right )
    {
        super( orientation, continuous, left, right );
        init();
    }

    public NiceSplitPane( final int orientation, final Component left, final Component right )
    {
        super( orientation, left, right );
        init();
    }

    private void init()
    {
        final NiceSplitPaneUI newUI = new NiceSplitPaneUI();
        setUI( newUI );
        setBorder( BorderFactory.createEmptyBorder() );
    }
}
