package ch.xmatrix.ups.pmb.exam;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClockPanel extends JLabel implements ActionListener
{
    /** This class logger. */
    private static final Logger LOG = LoggerFactory.getLogger( ClockPanel.class );

    /** The date format. Parametrize if necessary... */
    private final Format formatter;

    /** The target to format. */
    private final TargetProvider target;

    /** The background timer. */
    private Timer timer;

    /** Instantiates a ClockPanel with a date format pattern of "d.M.yyy HH:mm:ss" and a 1 sec refresh rate. */
    public ClockPanel()
    {
        this( new SimpleDateFormat( "d.M.yyy HH:mm:ss" ), new DateTargetProvider() );
    }

    /**
     * Initializes the clock object. Example: <code>new ClockPanel( new SimpleDateFormat( "d.M.yyy HH:mm:ss" ), new DateTargetProvider(), 1000 )</code>.
     *
     * @param formatter the formatter formatter for the date
     * @param target    the target to format in each cycle
     */
    public ClockPanel( final Format formatter, final TargetProvider target )
    {
        this.formatter = formatter;
        this.target = target;
    }

    /** Start the timer. */
    public void start()
    {
        if ( timer == null )
        {
            timer = new Timer( 1000, this );
            LOG.info( "starting timer" );
            timer.start();
        }
    }

    /** Stop the timer. */
    public void stop()
    {
        if ( timer != null )
        {
            LOG.info( "stopping timer" );
            timer.stop();
            timer = null;
        }
    }

    /** {@inheritDoc} */
    public void actionPerformed( final ActionEvent actionEvent )
    {
        setText( formatter.format( target.getTarget() ) );
    }

    public static interface TargetProvider<T>
    {
        public T getTarget();
    }

    public static class DateTargetProvider implements ClockPanel.TargetProvider<Date>
    {
        public Date getTarget()
        {
            return new Date();
        }
    }

    public static class CountDownProvider implements ClockPanel.TargetProvider<Number>
    {
        private final long duration;

        private final ActionListener feedBacker;

        private Date end;

        public CountDownProvider( final long duration, final ActionListener feedbackListener )
        {
            this.duration = duration;
            this.feedBacker = feedbackListener;
        }

        public void reset()
        {
            end = null;
        }

        public Number getTarget()
        {
            if ( end == null )
            {
                end = new Date( new Date().getTime() + duration );
            }
            final long remainingTime = end.getTime() - new Date().getTime();
            if ( feedBacker != null && remainingTime <= 0 )
            {
                feedBacker.actionPerformed( new ActionEvent( this, -1, null ) );
            }
            return remainingTime;
        }
    }

    public static void main( String[] args )
    {
        final int delay = 1000;
        final ClockPanel timer = new ClockPanel( new SimpleDateFormat( "m:ss 'min'" ), new ClockPanel.CountDownProvider( 5 * delay, new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                LOG.info( "BANG" );
                System.exit( 0 );
            }
        } ) );
        timer.start();

        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        frame.setLayout( new BorderLayout() );
        frame.add( timer, BorderLayout.CENTER );
        frame.setSize( 600, 400 );
        frame.setLocationRelativeTo( null );
        frame.setVisible( true );
    }
}