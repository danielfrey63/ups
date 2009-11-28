package ch.jfactory.component.split;

import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JSplitPane;

/**
 * Project: $Id: NiceSplitPane.java,v 1.1 2005/06/16 06:28:57 daniel_frey Exp $ $Source:
 * /repository/HerbarCD/Version2.1/xmatrix/src/com/xmatrix/gui/component/split/NiceSplitPane.java,v $ $Revision: 1.1 $,
 * $Author: daniel_frey $
 */
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
