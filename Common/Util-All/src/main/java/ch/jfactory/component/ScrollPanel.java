/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.component;

import ch.jfactory.layout.ScrollerLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicArrowButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This control is used to different Components as a Scrollable List without using ScrollPane, because of sizing Problems
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
public class ScrollPanel extends JPanel
{
    private final static Logger LOGGER = LoggerFactory.getLogger( ScrollPanel.class );

    protected JButton btnPrev = new BasicArrowButton( BasicArrowButton.WEST );

    protected JButton btnNext = new BasicArrowButton( BasicArrowButton.EAST );

    private ScrollerLayout layout;

    public ScrollPanel()
    {
        initGUI();
    }

    private void initGUI()
    {
        this.setLayout( layout = new ScrollerLayout( this ) );
        btnPrev.setFocusPainted( false );
        btnNext.setFocusPainted( false );
        addButtons();
        addActionListeners();
    }

    public Component add( final Component c )
    {
        return super.add( c );
    }

    private void addButtons()
    {
        LOGGER.trace( "adding buttons" );
        add( btnPrev, ScrollerLayout.PREVSCROLLER );
        add( btnNext, ScrollerLayout.NEXTSCROLLER );
    }

    private void addActionListeners()
    {
        btnNext.addActionListener( new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                LOGGER.debug( "actionPerformed(" + e + ")" );
                layout.incStart();
            }
        } );
        btnPrev.addActionListener( new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                LOGGER.debug( "actionPerformed(" + e + ")" );
                layout.decStart();
            }
        } );
    }

    /** remove all components from the scrollable section of the panel */
    public void removeAll()
    {
        LOGGER.trace( "removing all components and re-adding buttons" );
        super.removeAll();
        addButtons();
        repaint();
    }
}
