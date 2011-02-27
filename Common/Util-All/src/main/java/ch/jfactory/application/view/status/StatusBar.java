/*
 * Copyright x-matrix Switzerland (c) 2002
 *
 * StatusBar.java
 *
 * Created on 10. Juli 2002, 17:13
 * Created by Daniel Frey
 */
package ch.jfactory.application.view.status;

import com.jgoodies.looks.windows.WindowsLookAndFeel;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Makes a simple status bar to display i.e. at the bottom of a frame.
 *
 * @author $Author: daniel_frey $
 * @version $Date: 2008/01/06 10:16:23 $ $Revision: 1.4 $
 */
public class StatusBar extends JPanel
{
    private static final String LOGO = "\u00A9 " + Calendar.getInstance().get( Calendar.YEAR ) + " www.xmatrix.ch";

    private final GridBagConstraints constraints = new GridBagConstraints();

    private List<JComponent> components = new ArrayList<JComponent>();

    private List<StatusPanel> statusPanels = new ArrayList<StatusPanel>();

    public StatusBar()
    {
        setLayout( new GridBagLayout() );
        constraints.gridx = 0;
        constraints.weightx = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.BOTH;
        final JLabel logo = new JLabel( LOGO );
        addStatusComponent( new StatusPanel( logo ) );
        constraints.weightx = 0;
    }

    /**
     * Add the new component and makes sure it looks the same like a status item.
     *
     * @param component component to add
     */
    public synchronized void addStatusComponent( final JComponent component )
    {
        final StatusPanel statusPanel;
        statusPanel = StatusPanel.class.isAssignableFrom( component.getClass() ) ? (StatusPanel) component : new StatusPanel( component );
        components.add( component );
        statusPanels.add( statusPanel );
        add( statusPanel, constraints );
        constraints.gridx += 1;
        validate();
        repaint();
    }

    public synchronized void addStatusComponent( final JComponent component, final int index )
    {
        final List<JComponent> componentsCopy = new ArrayList<JComponent>( components );
        final List<StatusPanel> statusPanelsCopy = new ArrayList<StatusPanel>( statusPanels );
        for ( int i = 1; i < components.size(); i++ )
        {
            remove( statusPanels.get( i ) );
            components.remove( i );
            statusPanels.remove( i );
        }
        componentsCopy.add( index, component );
        statusPanelsCopy.add( index, new StatusPanel( component ) );
        constraints.gridx = 1;
        components = componentsCopy;
        statusPanels = statusPanelsCopy;
        for ( int i = 1; i < components.size(); i++ )
        {
            add( statusPanels.get( i ), constraints );
            constraints.gridx += 1;
        }
        validate();
        repaint();
    }

    /**
     * Removes the component previously added and shifts the trailing components one to the left.
     *
     * @param component the component to be removed
     */
    public synchronized void removeStatusComponent( final JComponent component )
    {
        boolean found = false;
        final List<JComponent> componentsCopy = new ArrayList<JComponent>( components );
        final List<StatusPanel> statusPanelsCopy = new ArrayList<StatusPanel>( statusPanels );
        for ( int i = 0; i < componentsCopy.size(); i++ )
        {
            final JComponent comp = componentsCopy.get( i );
            if ( found || comp == component )
            {
                remove( statusPanelsCopy.get( i ) );
                if ( comp == component )
                {
                    components.remove( i );
                    statusPanels.remove( i );
                    found = true;
                    constraints.gridx -= 1;
                }
                else
                {
                    add( statusPanelsCopy.get( i ), constraints );
                }
            }
            constraints.gridx = i;
        }
        validate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize()
    {
        final int h = super.getPreferredSize().height;
        final Container parent = getTopLevelAncestor();
        final int w = parent.getSize().width;
        return new Dimension( w, h );
    }

    public static void main( final String[] args ) throws UnsupportedLookAndFeelException
    {
        UIManager.setLookAndFeel( new WindowsLookAndFeel() );

        final Map<String, JLabel> map = new HashMap<String, JLabel>();
        final List<JLabel> list = new ArrayList<JLabel>();
        // For the logo
        final JLabel label = new JLabel();
        map.put( "0", label );
        list.add( label );

        final JButton remove = new JButton( "Remove" );
        final JButton add = new JButton( "Add" );
        final JLabel indexLabel = new JLabel( "Index:" );
        final JTextField index = new JTextField( 4 );
        final JTextField text = new JTextField( 20 );
        final StatusBar status = new StatusBar();

        final JPanel panel = new JPanel( new FlowLayout() );
        panel.add( add );
        panel.add( remove );
        panel.add( text );
        panel.add( indexLabel );
        panel.add( index );

        add.addActionListener( new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                final JLabel label = new JLabel( text.getText() );
                if ( index.getText() == null || "".equals( index.getText().trim() ) )
                {
                    status.addStatusComponent( label );
                    map.put( text.getText(), label );
                    list.add( label );
                }
                else
                {
                    final int i = Integer.parseInt( index.getText() );
                    status.addStatusComponent( label, i );
                    map.put( text.getText(), label );
                    list.add( i, label );
                }
                status.validate();
            }
        } );

        remove.addActionListener( new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                final JLabel label = map.get( text.getText() );
                if ( label != null )
                {
                    status.removeStatusComponent( label );
                    status.validate();
                }
            }
        } );

        final JFrame f = new JFrame();
        f.getContentPane().add( panel, BorderLayout.CENTER );
        f.getContentPane().add( status, BorderLayout.SOUTH );
        f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        f.setSize( 600, 300 );
        f.setVisible( true );
    }
}
