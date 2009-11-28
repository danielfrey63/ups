package ch.jfactory.component.tab;

import javax.swing.JTabbedPane;

/**
 * Project: $Id: NiceTabbedPane.java,v 1.1 2005/06/16 06:28:57 daniel_frey Exp $ $Source:
 * /repository/HerbarCD/Version2.1/xmatrix/src/com/xmatrix/gui/component/tab/NiceTabbedPane.java,v $ $Revision: 1.1 $,
 * $Author: daniel_frey $
 */
public class NiceTabbedPane extends JTabbedPane
{
    public NiceTabbedPane()
    {
        init();
    }

    public NiceTabbedPane( final int top )
    {
        super( top );
        init();
    }

    private void init()
    {
//      setUI(new NicedTabbedPaneUI());
    }
}
