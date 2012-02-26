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

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Totally self-contained visual time component that displays an analogue time string.
 *
 * TODO: Setting opaque to false does not work really.
 *
 * @author Daniel Frey 31.07.2008 16:58:26
 */
public class ClockPanel extends JPanel implements ActionListener
{
    /** This class logger. */
    private static final Logger LOG = LoggerFactory.getLogger( ClockPanel.class );

    /** The off-screen image. */
    private Image offScreenImage;

    /** The off-screen graphics. */
    private Graphics2D offScreenGraphics;

    /** The date string. */
    private String date;

    /** The background timer. */
    private Timer timer;

    /** The date format. Parametrize if necessary... */
    private final Format formatter;

    /** The target to format. */
    private final TargetProvider target;

    /** The timely resolution after which an update takes place. */
    private final int resolution;

    /** Instantiates a ClockPanel with a date format pattern of "d.M.yyy HH:mm:ss" and a 1 sec refresh rate. */
    public ClockPanel()
    {
        this( new SimpleDateFormat( "d.M.yyy HH:mm:ss" ), new DateTargetProvider(), 1000 );
    }

    /**
     * Initializes the clock object. Example: <code>new ClockPanel( new SimpleDateFormat( "d.M.yyy HH:mm:ss" ), new DateTargetProvider(), 1000 )</code>.
     *
     * @param formatter  the formatter formatter for the date
     * @param target     the target to format in each cycle
     * @param resolution the timely resolution after which an update takes place
     */
    public ClockPanel( final Format formatter, final TargetProvider target, final int resolution )
    {
        this.target = target;
        this.formatter = formatter;
        this.resolution = resolution;
        init();
    }

    private void init()
    {
        setLayout( null );
        addNotify();
        initOffScreenImage();
    }

    /** Start the timer. */
    public void start()
    {
        if ( timer == null )
        {
            timer = new Timer( resolution, this );
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
        date = formatter.format( target.getTarget() );
        final Font font = getFont();
        final Dimension size = new Dimension( getStringWidth( date ), getStringHeight( font ) );
        setPreferredSize( size );
        invalidate();
        repaint();
    }

    /** {@inheritDoc} */
    public void paint( final Graphics graphics )
    {
        initOffScreenImage();
        if ( offScreenGraphics != null && date != null )
        {
            offScreenGraphics.setColor( isOpaque() ? getBackground() : new Color( 0, 0, 0, 0 ) );
            offScreenGraphics.fillRect( 0, 0, getWidth(), getHeight() );
            System.out.println( getWidth() + " " + getHeight() );
            offScreenGraphics.setColor( getForeground() );
            offScreenGraphics.setFont( getFont() );
            offScreenGraphics.drawString( date, getXLoc( date ), getYLoc() );
            graphics.drawImage( offScreenImage, 0, 0, this );
        }
    }

    private void initOffScreenImage()
    {
        if ( offScreenImage == null && getWidth() > 0 && getHeight() > 0 )
        {
            offScreenImage = new BufferedImage( getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB );
            offScreenGraphics = (Graphics2D) offScreenImage.getGraphics();
            offScreenGraphics.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
            offScreenGraphics.setRenderingHint( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY );
        }
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension( date == null ? 0 : getStringWidth( date ), getStringHeight( getFont() ) );
    }

    private int getXLoc( final String text )
    {
        final int textWidth = getStringWidth( text );
        final int extraPixelsOnSides = getWidth() - textWidth;
        return extraPixelsOnSides / 2;
    }

    private int getYLoc()
    {
        final FontMetrics fontMetrics = getFontMetrics( getFont() );
        return getHeight() - fontMetrics.getDescent();
    }

    private int getStringWidth( final String text )
    {
        final FontMetrics fontMetrics = getFontMetrics( getFont() );
        return fontMetrics.stringWidth( text );
    }

    private int getStringHeight( final Font font )
    {
        final FontMetrics fontMetrics = getFontMetrics( font );
        return fontMetrics.getHeight();
    }

    public static interface TargetProvider<T>
    {
        public T getTarget();
    }

    public static class DateTargetProvider implements TargetProvider<Date>
    {
        public Date getTarget()
        {
            return new Date();
        }
    }

    public static class CountDownProvider implements TargetProvider<Number>
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

    public static class CombinedFormatter extends Format
    {
        private final Format formatMinutes = new SimpleDateFormat( "m 'min'" );

        private final Format formatSeconds = new SimpleDateFormat( "m:ss 'min'" );

        private final int anInt;

        public CombinedFormatter( final int threshold )
        {
            anInt = threshold;
        }

        @Override
        public StringBuffer format( final Object obj, final StringBuffer toAppendTo, final FieldPosition pos )
        {
            if ( obj instanceof Number )
            {
                final long number = ( (Number) obj ).longValue();
                pos.setBeginIndex( 0 );
                pos.setEndIndex( 0 );
                if ( number > anInt )
                {
                    toAppendTo.append( formatMinutes.format( number ) );
                }
                else
                {
                    toAppendTo.append( formatSeconds.format( number ) );
                }
            }
            return toAppendTo;
        }

        @Override
        public Object parseObject( final String source, final ParsePosition pos )
        {
            return null;
        }
    }

    public static void main( String[] args )
    {
        final ClockPanel watch = new ClockPanel();
        watch.setFont( new Font( "SansSerif", Font.BOLD, 20 ) );
        watch.setForeground( Color.ORANGE );
        watch.setBackground( Color.lightGray );
        watch.start();

        final int delay = 1000;
        final ClockPanel stopper = new ClockPanel( new ClockPanel.CombinedFormatter( 12 * delay ), new ClockPanel.CountDownProvider( 15 * delay, null ), delay );
        stopper.setFont( new Font( "SansSerif", Font.BOLD, 20 ) );
        stopper.setForeground( Color.ORANGE );
        stopper.setBackground( Color.darkGray );
        stopper.start();

        final JFrame frame = new JFrame();
        frame.getContentPane().setBackground( Color.orange );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setLayout( new FormLayout( "f:p:g", "c:p:g, c:p:g" ) );
        frame.add( watch, new CellConstraints( 1, 1 ) );
        frame.add( stopper, new CellConstraints( 1, 2 ) );
        frame.setSize( 600, 400 );
        frame.setLocationRelativeTo( null );
        frame.setVisible( true );
    }
}