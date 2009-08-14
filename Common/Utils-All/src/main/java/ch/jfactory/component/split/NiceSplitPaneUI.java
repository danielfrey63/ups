package ch.jfactory.component.split;

import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.metal.MetalSplitPaneUI;

/**
 * Project: $Id: NiceSplitPaneUI.java,v 1.1 2005/06/16 06:28:57 daniel_frey Exp $ $Source:
 * /repository/HerbarCD/Version2.1/xmatrix/src/com/xmatrix/gui/component/split/NiceSplitPaneUI.java,v $ $Revision: 1.1
 * $, $Author: daniel_frey $
 */
public class NiceSplitPaneUI extends MetalSplitPaneUI {
    public BasicSplitPaneDivider createDefaultDivider() {
        return new NiceSplitPaneDivider(this);
    }

}
