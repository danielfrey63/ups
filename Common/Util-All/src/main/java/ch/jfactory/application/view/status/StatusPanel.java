/*
 * Copyright x-matrix Switzerland (c) 2002
 *
 * StatusPanel.java
 *
 * Created on 10. Juli 2002 17:13
 * Created by Daniel Frey
 */
package ch.jfactory.application.view.status;

import ch.jfactory.application.view.border.BevelDirection;
import ch.jfactory.application.view.border.ThinBevelBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class StatusPanel extends JPanel
{
    private final JComponent component;

    private Dimension maxPreferredSize;

    public StatusPanel( final JComponent component )
    {
        this.component = component;
        setBorder( BorderFactory.createCompoundBorder( new ThinBevelBorder( BevelDirection.LOWERED ),
                BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) ) );
        setLayout( new BorderLayout() );
        add( component, BorderLayout.CENTER );
        maxPreferredSize = super.getPreferredSize();
    }

    public Dimension getPreferredSize()
    {
        super.getPreferredSize();
        if ( super.getPreferredSize().width > maxPreferredSize.width )
        {
            maxPreferredSize = super.getPreferredSize();
        }
        return maxPreferredSize;
    }

    public String toString()
    {
        return component.toString();
    }
}
