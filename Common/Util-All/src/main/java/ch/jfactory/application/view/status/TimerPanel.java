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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * Panel to display a stop watch. The format of the stop watch is 00:00:00 for durations greater or equal than one hour and 00:00 for less. Register an ActionListener if you want to be notified uppon reach of zero.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
public class TimerPanel extends JLabel implements ActionListener
{
    private final Timer endTimer;

    private final Timer countTimer;

    private int count;

    private final int seconds;

    private final String zero;

    /** Returns an instance for the given delay. The given ActionListener will be called uppon conclusion of the timer. */
    public TimerPanel( final int seconds, final ActionListener actionListener )
    {
        super( "", JLabel.CENTER );
        this.seconds = seconds;
        zero = ( seconds >= 3600 ) ? "00:00:00" : "00:00";
        setText( zero );
        setPreferredSize( getPreferredSize() );
        count = seconds;
        endTimer = new Timer( 1000 * seconds + 1, new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                actionListener.actionPerformed( e );
                setText( zero );
                stop();
            }
        } );
        setText( getTimeString() );
        countTimer = new Timer( 1000, this );
    }

    /**
     * Update component with the current time.
     *
     * @param evt The current event.
     */
    public void actionPerformed( final ActionEvent evt )
    {
        count--;
        final String text = getTimeString();
        setText( text );
    }

    private String getTimeString()
    {
        final DecimalFormat f = new DecimalFormat( "00" );
        String text = ( seconds >= 3600 ) ? f.format( count / 3600 ) + ":" : "";
        text += f.format( ( count - count / 3600 * 3600 ) / 60 ) + ":" + f.format( count % 60 );
        return text;
    }

    public void start()
    {
        countTimer.start();
        endTimer.start();
    }

    public void stop()
    {
        countTimer.stop();
        endTimer.stop();
    }

    public void resetTime()
    {
        count = seconds;
        setText( getTimeString() );
    }

    public static void main( final String[] args )
    {
        final JFrame f = new JFrame();
        final Container contentPane = f.getContentPane();
        final StatusBar statusBar = new StatusBar();
        contentPane.setLayout( new BorderLayout() );
        contentPane.add( statusBar, BorderLayout.SOUTH );
        final TimerPanel counter = new TimerPanel( 61, new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                System.out.println( "stop" );
            }
        } );
        f.addWindowListener( new WindowAdapter()
        {
            public void windowClosed( final WindowEvent e )
            {
                counter.stop();
                System.exit( 0 );
            }
        } );
        statusBar.addStatusComponent( counter );
        f.setSize( 500, 300 );
        f.setVisible( true );
        counter.start();
    }
}

