/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.application.view.status;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Makes a simple status bar to display i.e. at the bottom of a frame.
 *
 * @author $Author: daniel_frey $
 * @version $Date: 2008/01/06 10:16:23 $ $Revision: 1.4 $
 */
public class StatusBar extends JPanel
{
    /** This class logger. */
    private static final Logger LOG = LoggerFactory.getLogger( StatusBar.class );

    private static final String LOGO = "\u00A9 " + Calendar.getInstance().get( Calendar.YEAR ) + " www.xmatrix.ch";

    private final GridBagConstraints constraints = new GridBagConstraints();

    private List<JComponent> components = new ArrayList<>();

    private List<StatusPanel> statusPanels = new ArrayList<>();

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
        LOG.debug( "adding component " + component.hashCode() );
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
        LOG.debug( "adding at " + index + " component " + component.hashCode() );
        final List<JComponent> componentsCopy = new ArrayList<>( components );
        final List<StatusPanel> statusPanelsCopy = new ArrayList<>( statusPanels );
        for ( int i = components.size() - 1; i >= 0; i-- )
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
        LOG.debug( "removing " + component.hashCode() );
        boolean found = false;
        final List<JComponent> componentsCopy = new ArrayList<>( components );
        final List<StatusPanel> statusPanelsCopy = new ArrayList<>( statusPanels );
        for ( int i = componentsCopy.size() - 1; i >= 0; i-- )
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

    public static void main( final String[] args )
    {
        final Map<String, JLabel> map = new HashMap<>();
        // For the logo
        final JLabel label = new JLabel();
        map.put( "0", label );

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

        add.addActionListener( e -> {
            final JLabel label12 = new JLabel( text.getText() );
            if ( index.getText() == null || "".equals( index.getText().trim() ) )
            {
                status.addStatusComponent( label12 );
                map.put( text.getText(), label12 );
            }
            else
            {
                final int i = Integer.parseInt( index.getText() );
                status.addStatusComponent( label12, i );
                map.put( text.getText(), label12 );
            }
            status.validate();
        } );

        remove.addActionListener( e -> {
            final JLabel label1 = map.get( text.getText() );
            if ( label1 != null )
            {
                status.removeStatusComponent( label1 );
                status.validate();
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
