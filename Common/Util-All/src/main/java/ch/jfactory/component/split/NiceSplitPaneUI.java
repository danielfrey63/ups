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

import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.metal.MetalSplitPaneUI;

/** Project: $Id: NiceSplitPaneUI.java,v 1.1 2005/06/16 06:28:57 daniel_frey Exp $ $Source: /repository/HerbarCD/Version2.1/xmatrix/src/com/xmatrix/gui/component/split/NiceSplitPaneUI.java,v $ $Revision: 1.1 $, $Author: daniel_frey $ */
public class NiceSplitPaneUI extends MetalSplitPaneUI
{
    public BasicSplitPaneDivider createDefaultDivider()
    {
        return new NiceSplitPaneDivider( this );
    }

}
